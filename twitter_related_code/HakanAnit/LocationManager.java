import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

import opennlp.tools.util.Span;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.GetMethod;
import org.bson.types.ObjectId;
import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.dbpedia.spotlight.model.DBpediaResource;
import org.dbpedia.spotlight.model.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.util.Hash;

import twitter4j.GeoLocation;

//GPS cevir fonksiyonu, profil lokasyonunu cikaran, timezone bilgisini cikaran fonksiyonlar

public class LocationManager extends AnnotationClient 
{

	//	private final static String API_URL_DBPEDIA = "http://spotlight.dbpedia.org/";
	private final static String API_URL_MAPQUEST = "http://open.mapquestapi.com/geocoding/v1/";
	private final static String API_URL_DBPEDIA = "http://localhost:2222/";
	private static final int DISTANCE_THRESHOLD = 2; //1000m 
	private static final int TIME_THRESHOLD = 5*60; //5 dk

	private static final double CONFIDENCE = 0.0;
	private static final int SUPPORT = 0;
	//private String spotlightResponse;
	//private JSONObject resultJSON = null;
	//private JSONArray entities = null;
	//private LinkedList<DBpediaResource> resources;

	//private HashMap<String, String> hashMapEntityType;
	//buyuk kucuk harf fark etmez hepsi kucuk eslenecek
	private enum locationTypes{settlement,city,country,town,state};
	//asagidaki iki setin de icinde textten cikan seyin olmasi gerekli. 
	private List<String> listLocationRequest1;
	private List<String> listLocationRequest2;
	private TextAnalytics textAnalytics;
	private Boolean boolGPSAttachedToTweet,boolProfileLocationAvail,boolTimezoneAvail;
	private String strProfileCity,strProfileState,strProfileCountry,strProfileTown,strGPSCountry,strGPSCity,strGPSTown,strGPSState,strGPSStreet,strGPSLat,strGPSLong,strProfileStreet,strProfileLat,strProfileLong,
	strTweetTextCountry,strTweetTextCity,strTweetTextTown,strTweetTextState,strTweetTextStreet,strTweetTextLat,strTweetTextLong,
	strTweetCountry,strTweetCity,strTweetTown,strTweetState,strTweetStreet,strTweetLat,strTweetLong,strEventType;
	private GeoLocation geoProfileLoc,geoGPS,geoTweetTextLoc,geoTweetLoc;
	private List<String> listAllCitiesFromTimezone;
	//	private HashMap< String, String> hashmapLoc=null;

	public List<String> batchRequestOSM(List<ObjectId> listIds,List<String> listRequest) throws AnnotationException, JSONException, UnsupportedEncodingException 
	{
		String spotlightResponse;
		JSONObject resultJSON = null;
		JSONArray entities = null;
		LinkedList<DBpediaResource> resources;
		List<String> listBatch=new ArrayList<String>();

		//hersey formatli sadece yolliyip response alicak
		String strTemp="";
		for (String string : listRequest)
		{
			strTemp+="&location="+URLEncoder.encode(string, "utf-8");

		}
		//		strTemp="&location="+URLEncoder.encode("York,PA", "utf-8")+"&location="+URLEncoder.encode("Red Lion", "utf-8")+"&location="+URLEncoder.encode("40.0755 -76.329999", "utf-8");
		GetMethod getMethod = new GetMethod(API_URL_MAPQUEST + "batch?callback=renderBatch" +
				strTemp
				+ "&maxResults=" + "1"
				); 
		synchronized (getMethod) {
			getMethod.addRequestHeader(new Header("Accept", "application/json"));

			spotlightResponse = request(getMethod);

		}
		assert spotlightResponse != null;
		spotlightResponse=spotlightResponse.replace("renderBatch(", ""); //Mapquest jsonda donemiyor duzgun ondan basina { ekliorm
		//		System.out.println(spotlightResponse);

		resultJSON = new JSONObject(spotlightResponse);

		System.out.println(resultJSON.toString());
		//cevap geldikten sonra ulke state twon vs ne varsa koy map e gonder
		try {

			entities = resultJSON.getJSONArray("results");

		} catch (JSONException e) {
			//			throw new AnnotationException("Received invalid response from DBpedia Spotlight API.");
		}

		resources = new LinkedList<DBpediaResource>();
		//		System.out.println(entities.length());
		for(int i = 0; i < entities.length(); i++) 
		{
			try {
				JSONObject entity = entities.getJSONObject(i);

				JSONArray locations=entity.getJSONArray("locations");
				//locations.size() idi
				if(entities.length()==listIds.size())
				{
					for (int j = 0; j < listIds.size(); j++)
					{
						JSONObject ent = locations.getJSONObject(j);

						System.out.println("ENTITY len:"+entities.length()+" LISTIDS:"+listIds.size()+" LISTREQS:"+listRequest.size());
						//					System.out.println(ent.getString("adminArea4"));
						listBatch.add(listIds.get(i).toString()+",id:"+ "id"); //ilkini bos harcasin
						listBatch.add(listIds.get(i).toString()+",town:"+ ent.getString("adminArea4")); //county
						System.out.println( ent.getString("adminArea4"));
						listBatch.add(listIds.get(i).toString()+",city:"+ ent.getString("adminArea5"));//city
						System.out.println( ent.getString("adminArea5"));

						listBatch.add(listIds.get(i).toString()+",state:"+ ent.getString("adminArea3"));//state
						System.out.println( ent.getString("adminArea3"));

						listBatch.add(listIds.get(i).toString()+",country:"+ ent.getString("adminArea1"));//country
						System.out.println( ent.getString("adminArea1"));

						listBatch.add(listIds.get(i).toString()+",street:"+ ent.getString("street"));//street
						System.out.println( ent.getString("street"));

						listBatch.add(listIds.get(i).toString()+",lat:"+ ent.getJSONObject("latLng").getString("lat"));//lat
						System.out.println( ent.getJSONObject("latLng").getString("lat"));

						listBatch.add(listIds.get(i).toString()+",long:"+ ent.getJSONObject("latLng").getString("lng"));//long
						System.out.println( ent.getJSONObject("latLng").getString("lng"));

					}
				}

			} catch (JSONException e) {
				LOG.error("JSON exception "+e);
			}

		}

		return listBatch;
	}


	public LocationManager(String strEventType) throws UnknownHostException, FileNotFoundException
	{
		DBConnect.connect();
		//		hashmapLoc=new HashMap<String, String>();
		//hashMapEntityType=new HashMap<String, String>();
		this.strEventType=strEventType;
		textAnalytics=new TextAnalytics();
		//birinci liste ile ikinci liste birebirlerinin zorunluluklari gibi
		listLocationRequest1=new ArrayList<String>();
		listLocationRequest2=new ArrayList<String>();
		listLocationRequest1.add("country");
		listLocationRequest2.add("city");
		listLocationRequest2.add("town");
		listLocationRequest2.add("state");
		listAllCitiesFromTimezone=new ArrayList<String>();
		strGPSCity="";
		strGPSCountry="";
		strGPSState="";
		strGPSTown="";
		strProfileCity="";
		strProfileCountry="";
		strProfileState="";
		strProfileTown="";
		strProfileStreet="";
		strGPSLat="";
		strGPSLong="";
		strProfileLat="";
		strProfileLong="";
		strTweetTextCity="";
		strTweetTextCountry="";
		strTweetTextState="";
		strTweetTextTown="";
		strTweetTextStreet="";
		strTweetTextLat="";
		strTweetTextLong="";
		strTweetCity="";
		strTweetCountry="";
		strTweetState="";
		strTweetTown="";
		strTweetStreet="";
		strTweetLat="";
		strTweetLong="";
	}
	public void clear()
	{
		textAnalytics.clear();
		textAnalytics=null;
		listLocationRequest1.clear();
		listLocationRequest2.clear();
		listLocationRequest1=null;
		listLocationRequest2=null;
		//hashMapEntityType.clear();
		//hashMapEntityType=null;
		clearVars();

		//System.gc(); //yeni kapattim
	}
	private void clearVars()
	{
		strProfileCity="";
		strProfileState="";
		strProfileCountry="";
		strProfileTown="";
		strGPSCountry="";
		strGPSCity="";
		strGPSTown="";
		strGPSState="";
		strGPSStreet="";
		strGPSLat="";
		strGPSLong="";strProfileStreet="";strProfileLat="";strProfileLong="";
		strTweetTextCountry="";strTweetTextCity="";strTweetTextTown="";strTweetTextState="";strTweetTextStreet="";strTweetTextLat="";strTweetTextLong="";
		strTweetCountry="";strTweetCity="";strTweetTown="";strTweetState="";strTweetStreet="";strTweetLat="";strTweetLong="";
	}

	//sadece geonames timezone dosyasini dbye atmak icin
	public void readTimeZonesFromFileInsertToDB() throws IOException
	{
		BasicDBObject obj=new BasicDBObject();
		BufferedReader br = new BufferedReader(new FileReader("./timeZones.txt"));
		Integer cnt=0;
		//ilk token ulke kodu ikinci id text 3. gmt timezone
		try 
		{
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null)
			{

				StringTokenizer st = new StringTokenizer(line);
				cnt=0;
				while (st.hasMoreTokens())
				{
					if(cnt>2)
						break;
					switch (cnt) {
					case 0://US
						obj.put("countryid", st.nextToken());
						//						System.out.println( st.nextToken());
						break;
					case 1://America/Indiana/Winamac
						String temp=st.nextToken();
						String[] t=temp.split("/");
						obj.put("continent",t[0]);//kita
						obj.put("city", t[1]);//sehir

						break;
					case 2://-5.0
						obj.put("gmtoffset", st.nextToken());
						//						System.out.println( st.nextToken());
						break;
					}
					cnt++;
				}
				DBConnect.getCollTimezones().insert(obj);
				obj.clear();
				line = br.readLine();


			}
		}
		finally
		{
			br.close();
		}

	}


	//http://open.mapquestapi.com/geocoding/
	//verilen GPSi address e cevirir ulke, sehir, state, sokak ne ise
	//lat long seklinde alir
	public HashMap<String, String> GPSToAddressConverter(GeoLocation geoLocation) throws AnnotationException, JSONException, UnsupportedEncodingException
	{
		String spotlightResponse="";
		JSONObject resultJSON = null;
		JSONArray entities = null;
		LinkedList<DBpediaResource> resources=null;
		HashMap<String, String> hashmapLocation=new HashMap<String, String>();

		//System.out.println(geoLocation.getLatitude()+","+geoLocation.getLongitude());
		GetMethod getMethod = new GetMethod(API_URL_MAPQUEST + "reverse?&callback=renderReverse&location=" 
				+ String.valueOf(geoLocation.getLatitude())+URLEncoder.encode(",", "utf-8")+String.valueOf(geoLocation.getLongitude())
				); 
		getMethod.addRequestHeader(new Header("Accept", "application/json"));
		spotlightResponse = request(getMethod);
		assert spotlightResponse != null;
		spotlightResponse=spotlightResponse.replace("renderReverse(", ""); //Mapquest jsonda donemiyor duzgun ondan basina { ekliorm

		resultJSON = new JSONObject(spotlightResponse);
		System.out.println(resultJSON.toString());

		try {

			entities = resultJSON.getJSONArray("results");

		} catch (JSONException e) {
			throw new AnnotationException("Received invalid response from DBpedia Spotlight API.");
		}

		resources = new LinkedList<DBpediaResource>();

		for(int i = 0; i < entities.length(); i++) 
		{
			try {
				JSONObject entity = entities.getJSONObject(i);

				JSONArray locations=entity.getJSONArray("locations");
				for (int j = 0; j < locations.length(); j++)
				{
					JSONObject ent = locations.getJSONObject(j);

					System.out.println(ent.getString("adminArea4"));
					hashmapLocation.put("county", ent.getString("adminArea4"));
					hashmapLocation.put("city", ent.getString("adminArea5"));
					hashmapLocation.put("state", ent.getString("adminArea3"));
					hashmapLocation.put("country", ent.getString("adminArea1"));
					hashmapLocation.put("street", ent.getString("street"));
					hashmapLocation.put("lat", ent.getJSONObject("latLng").getString("lat"));
					hashmapLocation.put("long", ent.getJSONObject("latLng").getString("lng"));


				}

			} catch (JSONException e) {
				LOG.error("JSON exception "+e);
			}

		}

		return hashmapLocation;
	}
	//ne dondurecek GPS locasyonunu yani hangi formatta
	public HashMap<String, String> addressToGPSConverter(String address) throws AnnotationException, JSONException, UnsupportedEncodingException 
	{
		String spotlightResponse="";
		JSONObject resultJSON = null;
		JSONArray entities = null;
		LinkedList<DBpediaResource> resources=null;
		//addressi duzgun formata sokamak lazim duzgun skeilde virgullerle ayrili vs su sekilde olmamali
		//,,,,,Istanbul,,,Turkey -> Istanbul,Turkey
		String strNewaddress="";
		address=address.replaceAll("null", "");
		char chPrev = 0;
		for (char ch: address.toCharArray())
		{
			if(chPrev==','&&ch!=',')
			{
				strNewaddress+=chPrev;
				strNewaddress+=ch;
			}
			else if(chPrev!=','&&ch!=',')
			{
				strNewaddress+=ch;
			}

			chPrev=ch;
		}
		System.out.println(strNewaddress);
		HashMap<String, String> hashmapLocation=new HashMap<String, String>();
		GetMethod getMethod = new GetMethod(API_URL_MAPQUEST + "address?outFormat=json&" +
				"location=" + URLEncoder.encode(address, "utf-8")
				+ "&maxResults=" + "1"
				); 
		getMethod.addRequestHeader(new Header("Accept", "application/json"));
		spotlightResponse = request(getMethod);
		assert spotlightResponse != null;
		resultJSON = new JSONObject(spotlightResponse);

		System.out.println(resultJSON.toString());
		//cevap geldikten sonra ulke state twon vs ne varsa koy map e gonder

		try {

			entities = resultJSON.getJSONArray("results");

		} catch (JSONException e) {
			throw new AnnotationException("Received invalid response from DBpedia Spotlight API.");
		}

		resources = new LinkedList<DBpediaResource>();

		for(int i = 0; i < entities.length(); i++) 
		{
			try {
				JSONObject entity = entities.getJSONObject(i);

				JSONArray locations=entity.getJSONArray("locations");
				for (int j = 0; j < locations.length(); j++)
				{
					JSONObject ent = locations.getJSONObject(j);

					System.out.println(ent.getString("adminArea4"));
					hashmapLocation.put("county", ent.getString("adminArea4"));
					hashmapLocation.put("city", ent.getString("adminArea5"));
					hashmapLocation.put("state", ent.getString("adminArea3"));
					hashmapLocation.put("country", ent.getString("adminArea1"));
					hashmapLocation.put("street", ent.getString("street"));
					hashmapLocation.put("lat", ent.getJSONObject("latLng").getString("lat"));
					hashmapLocation.put("long", ent.getJSONObject("latLng").getString("lng"));
				}

			} catch (JSONException e) {
				LOG.error("JSON exception "+e);
			}

		}

		return hashmapLocation;
	}
	//eger profil lokasyonu, gps yada timezonedan bisiler bulduysa onlari dondurur
	private HashMap<String, String> additionalInfoCheck(ObjectId tweetid)
	{
		Boolean boolEnd=false;
		HashMap<String, String> hashmapResults = new HashMap<String, String>();
		Integer valStage=0;
		String strGpsAvail="", strTZAvail="", strProfileAvail="",strCities="";
		while(true)
		{
			if(boolEnd)
				break;
			switch (valStage)
			{
			case 0:
				strGpsAvail=(String)DBConnect.fetchFieldFromDBGivenID("gpsLocationAvailable", tweetid);
				strTZAvail=(String)DBConnect.fetchFieldFromDBGivenID("timezoneAvailable", tweetid);
				strProfileAvail=(String)DBConnect.fetchFieldFromDBGivenID("profileLocationAvailable", tweetid);

				System.out.println("gps varmi="+strGpsAvail);
				if(strGpsAvail!=null&&strGpsAvail.equals("1"))
				{
					valStage=1;
				}
				else
				{
					valStage=2;
				}
				break;

			case 1:
				System.out.print("additional GPS var");

				//gps bilgisi var ne bulunduysa cek country stae vs DBDen
				strGPSCountry=(String)DBConnect.fetchFieldFromDBGivenID("GPSCountry", tweetid);
				strGPSCity=(String)DBConnect.fetchFieldFromDBGivenID("GPSCity", tweetid);
				strGPSState=(String)DBConnect.fetchFieldFromDBGivenID("GPSState", tweetid);
				strGPSTown=(String)DBConnect.fetchFieldFromDBGivenID("GPSTown", tweetid);
				strGPSStreet=(String)DBConnect.fetchFieldFromDBGivenID("GPSStreet", tweetid);
				strGPSLat=(String)DBConnect.fetchFieldFromDBGivenID("GPSLat", tweetid); 
				strGPSLong=(String)DBConnect.fetchFieldFromDBGivenID("GPSLong", tweetid); 
				hashmapResults.put("country", strGPSCountry);
				hashmapResults.put("city", strGPSCity);
				hashmapResults.put("state", strGPSState);
				hashmapResults.put("town", strGPSTown);
				hashmapResults.put("street", strGPSStreet);
				hashmapResults.put("lat", strGPSLat);
				hashmapResults.put("long", strGPSLong);
				valStage=6;

				break;
			case 2:
				//profil bilgisi varmi gps yokkmus
				System.out.println("str prof avail"+strProfileAvail);
				if(strProfileAvail!=null&&strProfileAvail.equals("1"))
				{
					valStage=3;
				}
				else
				{
					valStage=4;
				}
				break;

			case 3:
				//profil bilgisi  var
				strProfileCountry=(String)DBConnect.fetchFieldFromDBGivenID("ProfileCountry", tweetid);
				strProfileCity=(String)DBConnect.fetchFieldFromDBGivenID("ProfileCity", tweetid);
				strProfileState=(String)DBConnect.fetchFieldFromDBGivenID("ProfileState", tweetid);
				strProfileTown=(String)DBConnect.fetchFieldFromDBGivenID("ProfileTown", tweetid);
				strProfileStreet=(String)DBConnect.fetchFieldFromDBGivenID("ProfileStreet", tweetid);
				strProfileLat=(String)DBConnect.fetchFieldFromDBGivenID("ProfileLat", tweetid); 
				strProfileLong=(String)DBConnect.fetchFieldFromDBGivenID("ProfileLong", tweetid); 
				hashmapResults.clear();
				hashmapResults.put("country", strProfileCountry);
				hashmapResults.put("city", strProfileCity);
				hashmapResults.put("state", strProfileState);
				hashmapResults.put("town", strProfileTown);
				hashmapResults.put("street", strProfileStreet);
				hashmapResults.put("lat", strProfileLat);
				hashmapResults.put("long", strProfileLong);
				valStage=6;

				break;
			case 4:
				//profil bilgisi yok tz varmi
				if(strTZAvail!=null&&strTZAvail.equals("1"))
				{
					valStage=5;
				}
				else
				{
					valStage=6;
				}
				break;
			case 5:
				//tz var ne gereksiorsa cek
				strCities=(String)DBConnect.fetchFieldFromDBGivenID("timezoneCities", tweetid);
				hashmapResults.clear();
				hashmapResults.put("cities", strCities);
				valStage=6;
				break;
			case 6:
				//tz yok. ve diger ne bulunduysa geri don
				boolEnd=true;
				break;
			}
		}
		clearVars();
		return hashmapResults;
	}
	public void extractLocationFromTweetText(HashMap< String, String> hashmapTemp,Boolean boolRequest,String strOrgTweettext,ObjectId tweetid, Integer valState) throws IOException, AnnotationException, JSONException
	{
		Span[] spanPositionLocations;
		String strFunctionName="extractLocationFromTweetText";
		String strLocationText="";
		String[] arrStrTextlocation=strOrgTweettext.split(" ");
		Boolean boolReq1=false,boolReq2=false;
		Boolean boolEnd=false,boolNext=false;
		Set set;
		Iterator it;
		HashMap<String, String> hashmapEntityType=null;
		HashMap< String, String> hashmapAdditional=null;
		System.out.println("extractLocationFromTweetText;"+tweetid.toString());
		while(true)
		{
			if(boolEnd)
			{
				System.out.println("extractLocationFromTweetTextBOOLEDN"+tweetid.toString());

				if(hashmapTemp!=null)
				{
					hashmapTemp.clear();
				}
				hashmapTemp=null;
				if(boolNext)
				{
					System.out.println("extractLocationFromTweetTextBOOLNEXT"+tweetid.toString());

					extractLocationFromTweet(hashmapTemp,strOrgTweettext,tweetid,0);
				}
				//				System.gc();
				break;
			}
			switch (valState) 
			{
			case 0:	

				System.out.println("textAnalytics.detectLocationEntitiesFromTextGIRDI"); //sadece lokasyonun oldugu text var

				spanPositionLocations=textAnalytics.detectLocationEntitiesFromText(strOrgTweettext).clone();
				System.out.println("textAnalytics.detectLocationEntitiesFromTextCITKI"); //sadece lokasyonun oldugu text var

				for (Span spn:spanPositionLocations)
				{
					strLocationText+=arrStrTextlocation[spn.getStart()]+",";

				}
				System.out.println(strLocationText); //sadece lokasyonun oldugu text var
				//detect ve recognize et 1) ya hicbisi dondurmez. 2)ya en az ulke ve sehir 3)tek sehir tek state tek ulke dondurur
				hashmapEntityType=detectAndRecognizeLocationEntitiesFromText(strOrgTweettext);
				valState=1;
				break;

			case 1:
				if(hashmapEntityType.isEmpty())
				{
					System.out.println("extractLocationFromTweetText1"+tweetid.toString());

					valState=2;
					hashmapEntityType.clear();
					hashmapEntityType=null;
				}
				else
				{
					//2 ve 3 secenekler
					set = hashmapEntityType.entrySet();
					Iterator i = set.iterator();
					while(i.hasNext())
					{
						Map.Entry me = (Map.Entry)i.next();
						String strLocationType=me.getValue().toString();
						//strval setlerde varmi bunu kontrol edicez. eger sadece biri icin ok donuyorsa ambiguation
						if(boolReq1.equals(false))
							boolReq1=checkIfItemExistsInStringList(listLocationRequest1, strLocationType);
						if(boolReq2.equals(false))
							boolReq2=checkIfItemExistsInStringList(listLocationRequest2, strLocationType);

					} 
					System.out.println("extractLocationFromTweetText1de;"+tweetid.toString());

					//her iki listede de var yani verilen textin icinde hem profile country vede ona destek olacak state vs var	

					if(boolReq1&&boolReq2||boolReq1) //sadece ulke yada ulke ve sehir (yada state twown vvs)
					{
						valState=3;
					}
					else //sadece 1 listede uyusma var
					{
						System.out.println("sadece 1 uyusma var");

						valState=4;
					}
				}
				break;
			case 2: //dbpediadan sonuc donmedi
				//BUNU SIL NER DUZGUN TRAIN EDILINCE
				//				if(strLocationText==null||strLocationText.equals(""))
				//					strLocationText=strOrgTweettext;
				//BURa
				if(!strLocationText.equals(""))
				{
					System.out.println("extractLocationFromTweetText2"+tweetid.toString());

					hashmapEntityType=highlyPossibleLocationFromText(strLocationText);
					HashMap< String, String> temp;
					temp=hashMapKeyValSwitch(hashmapEntityType);

					hashmapEntityType.clear();
					hashmapEntityType=null;

					strTweetTextCity=temp.get("city");
					strTweetTextCountry=temp.get("country");
					strTweetTextTown=temp.get("town");
					strTweetTextState=temp.get("state");
					System.out.println("extractLocationFromTweetText2de:"+strTweetCity+" "+strTweetTextCountry+" "+strTweetTextTown+" "+strTweetTextState);

					if(temp.isEmpty())
					{
						valState=9;
					}
					else if(!temp.isEmpty()&&!boolRequest)
					{
						//					hashmapLoc=addressToGPSConverter(strTweetTextTown+","+strTweetTextState+","+strTweetTextCity+","+strTweetTextCountry);
						//ustteki bazi fieldlar bos olabilirdi ama addressToGPS bisiler dondurecek
						osmRequest(false,tweetid, strFunctionName, strTweetTextCountry,strTweetTextCity, strTweetTextTown, strTweetTextState,strTweetTextStreet, "","", "2");
						boolEnd=true;
					}
					else
					{
						if(!hashmapTemp.isEmpty())
						{
							System.out.println("hashmap dolu 2");
							strTweetTextCity=hashmapTemp.get("city");
							strTweetTextCountry=hashmapTemp.get("country");
							strTweetTextTown=hashmapTemp.get("town");
							strTweetTextState=hashmapTemp.get("state");
							strTweetTextStreet=hashmapTemp.get("street");
							try
							{
								geoTweetTextLoc=new GeoLocation(Double.parseDouble(hashmapTemp.get("lat")), Double.parseDouble(hashmapTemp.get("long")));
							}
							catch(Exception e)
							{
								geoTweetTextLoc=null;
							}
							valState=7;

						}
						else //address cevrilemedi
						{
							valState=9;

						}

					}
					temp.clear();
					temp=null;
				}
				else
				{
					System.out.println("extractLocationFromTweetText2de"+tweetid.toString());

					valState=8;

				}

				break;
			case 3:
				//dbpedia sonuc dondu
				//dbpediadan geleni duzgun formata sok cevir.

				//				hashmapLoc=addressToGPSConverter(strTweetTextTown+","+strTweetTextState+","+strTweetTextCity+","+strTweetTextCountry);
				if(!boolRequest)
				{
					set = hashmapEntityType.entrySet();
					it = set.iterator();
					while(it.hasNext())
					{
						Map.Entry me = (Map.Entry)it.next();
						if(me.getValue().toString().equals("city"))
						{
							strTweetTextCity=me.getKey().toString();
						}
						else if(me.getValue().toString().equals("country"))
						{
							strTweetTextCountry=me.getKey().toString();
						}
						else if(me.getValue().toString().equals("state"))
						{
							strTweetTextState=me.getKey().toString();
						}
						else if(me.getValue().toString().equals("town"))
						{
							strTweetTextTown=me.getKey().toString();
						}

					} 
					//hashmapLoc=addressToGPSConverter(strTweetTextTown+","+strTweetTextState+","+strTweetTextCity+","+strTweetTextCountry);
					//ustteki bazi fieldlar bos olabilirdi ama addressToGPS bisiler dondurecek
					osmRequest(false,tweetid, strFunctionName, strTweetTextCountry,strTweetTextCity, strTweetTextTown, strTweetTextState,strTweetTextStreet, "","", "3");
					boolEnd=true;
					hashmapEntityType.clear();
					hashmapEntityType=null;
				}
				else
				{
					//verilen fieldlari guncelle openstreet dondukten sonra
					if(!hashmapTemp.isEmpty())
					{
						System.out.println("hashmap dolu 3");

						strTweetTextCity=hashmapTemp.get("city");
						strTweetTextCountry=hashmapTemp.get("country");
						strTweetTextTown=hashmapTemp.get("twon");
						strTweetTextState=hashmapTemp.get("state");
						strTweetTextStreet=hashmapTemp.get("street");
						try
						{
							geoTweetTextLoc=new GeoLocation(Double.parseDouble(hashmapTemp.get("lat")), Double.parseDouble(hashmapTemp.get("long")));
						}
						catch(Exception e)
						{
							geoTweetTextLoc=null;
						}
						valState=7;
					}
					else
					{
						System.out.println("hashmap bos 3");

						valState=8;
					}
				}
				break;
			case 4:

				//ambiguous location info var additional info ile karsilastirilsin
				//bilgileri cekti ne varsa boolGpsAvail fln onlari doldurdu ordan kontrol et match varmi diye
				hashmapAdditional=additionalInfoCheck(tweetid);
				if(!hashmapAdditional.isEmpty())
				{
					System.out.print("additional bilgi var");
					valState=5; //additional bilgi var
				}
				else
				{
					System.out.print("additional bilgi yok");
					hashmapEntityType.clear();
					hashmapEntityType=null;
					hashmapAdditional.clear();
					hashmapAdditional=null;
					valState=6; //additional bilgi yok
				}
				break;
			case 5:
				//additional bilgi var bunu nasil kullanacaksin?
				//cikarttigimizda ne varsa onunla ayni key olani bulup eslesiyorsa additional olarak neler gelmis onlari alicam
				//ondan sonra bunu duzenleyip tekrar dbpedia yada osm sorcam
				//buldugunun keyini al
				System.out.println("additional bilgi var");

				set = hashmapEntityType.entrySet();
				it = set.iterator();
				Boolean boolMoreInfo=false;
				while(it.hasNext())
				{
					Map.Entry me = (Map.Entry)it.next();
					//bu city vs hashmapEntityde bu sekilde sakli
					if(hashmapAdditional.containsKey(me.getValue().toString())) 
					{
						//eslesen bisiler var ise diger bilgileride cek additionalda eklenen
						//eger ikiside ayni ise veriler ayni ise oldu
						if(hashmapAdditional.get(me.getValue())!=null&&me.getKey()!=null)
						{
							System.out.println("ne varmis esit:"+hashmapAdditional.get(me.getValue()).toString()+" "+me.getKey().toString() );
							if(hashmapAdditional.get(me.getValue().toString()).equals(me.getKey().toString()))
							{
								//ne tutarsa tutsun gerisini hashmapLoctan doldur
								boolMoreInfo=true;
								break;
							}
						}
					}

				} 
				if(boolMoreInfo)
				{
					System.out.print("additional bilgi var icindesin");

					strTweetTextCity=hashmapAdditional.get("city");
					strTweetTextCountry=hashmapAdditional.get("country");
					strTweetTextTown=hashmapAdditional.get("town");
					strTweetTextState=hashmapAdditional.get("state");
					strTweetTextStreet=hashmapAdditional.get("street");
					try
					{
						geoTweetTextLoc=new GeoLocation(Double.parseDouble(hashmapAdditional.get("lat")), Double.parseDouble(hashmapAdditional.get("long")));
					}
					catch(Exception e)
					{
						geoTweetTextLoc=null;
					}	
					valState=7;
				}
				else
				{
					System.out.println("additional ile text tutusmadi");
					valState=2;
				}
				hashmapAdditional.clear();
				hashmapAdditional=null;
				hashmapEntityType.clear();
				hashmapEntityType=null;
				break;
			case 6:
				//highly possible al tweet textten
				System.out.println("text neymis case6:"+strLocationText);
				valState=2;
				break;
			case 7:
				//ne bulunduysa ekle db ye
				DBConnect.insertFieldToTweetGivenID("tweetTextLocationAvailable","1", tweetid);
				//dbyi update et
				DBConnect.insertFieldToTweetGivenID("TweetTextCity",strTweetTextCity, tweetid);
				DBConnect.insertFieldToTweetGivenID("TweetTextCountry",strTweetTextCountry, tweetid);
				DBConnect.insertFieldToTweetGivenID("TweetTextState",strTweetTextState, tweetid);
				DBConnect.insertFieldToTweetGivenID("TweetTextTown",strTweetTextTown, tweetid);
				DBConnect.insertFieldToTweetGivenID("TweetTextStreet",strTweetTextStreet, tweetid);

				if(geoTweetTextLoc!=null)
				{
					DBConnect.insertFieldToTweetGivenID("TweetTextLong",String.valueOf(geoTweetTextLoc.getLongitude()), tweetid);
					DBConnect.insertFieldToTweetGivenID("TweetTextLat",String.valueOf(geoTweetTextLoc.getLatitude()), tweetid);
				}

				boolNext=true;
				boolEnd=true;
				break;
			case 8:
				DBConnect.insertFieldToTweetGivenID("tweetTextLocationAvailable","0", tweetid);
				System.out.println("extractLocationFromTweetText8de"+tweetid.toString());

				boolNext=true;
				boolEnd=true;
				break;
			case 9:
				System.out.println("hashmap bos 2");
				//son care direk osm ye sormak
				//				hashmapLoc=addressToGPSConverter(strLocationText);
				//				if(!boolRequest)
				//				{
				//					osmRequest(false,tweetid, strFunctionName, strLocationText,"", "", "","", "","", "9");
				//					boolEnd=true;
				//				}
				//				else
				//				{
				//					if(!hashmapTemp.isEmpty())
				//					{
				//						System.out.println("hashmap doldu 2");
				//						strTweetTextCity=hashmapTemp.get("city");
				//						strTweetTextCountry=hashmapTemp.get("country");
				//						strTweetTextTown=hashmapTemp.get("town");
				//						strTweetTextState=hashmapTemp.get("state");
				//						strTweetTextStreet=hashmapTemp.get("street");
				//						geoTweetTextLoc=new GeoLocation(Double.parseDouble(hashmapTemp.get("lat")), Double.parseDouble(hashmapTemp.get("long")));
				//						valState=7;
				//					}
				//					else
				//					{
				//						valState=8;
				//					}
				//				}
				valState=8;
				break;
			}

		}
		clearVars();


	}
	//uzerinde calistigimiz tweet herhangi bir olaya dahil mi? dahilse o olaya ekle degilse yeni olay yarat
	public void assignTweetToEvent(ObjectId tweetid)
	{
		DBObject query=null;
		DBCursor cursor=null;
		String strTweetLocAvail="";	
		strTweetLocAvail=(String)DBConnect.fetchFieldFromDBGivenID("tweetLocationAvailable", tweetid);
		Integer valTime1,valTime2;
		Boolean boolEventFound=false;
		if(strTweetLocAvail!=null&&strTweetLocAvail.equals("1"))
		{
			strTweetCountry=(String)DBConnect.fetchFieldFromDBGivenID("TweetCountry", tweetid);
			strTweetCity=(String)DBConnect.fetchFieldFromDBGivenID("TweetCity", tweetid);
			strTweetState=(String)DBConnect.fetchFieldFromDBGivenID("TweetState", tweetid);
			strTweetTown=(String)DBConnect.fetchFieldFromDBGivenID("TweetTown", tweetid);
			strTweetStreet=(String)DBConnect.fetchFieldFromDBGivenID("TweetStreet", tweetid);
			strTweetLat=(String)DBConnect.fetchFieldFromDBGivenID("TweetLat", tweetid); 
			strTweetLong=(String)DBConnect.fetchFieldFromDBGivenID("TweetLong", tweetid); 
			if(!strTweetLat.equals("")&&!strTweetLong.equals(""))
			{
				geoTweetLoc=new GeoLocation(Double.valueOf(strTweetLat), Double.valueOf(strTweetLong));
			}
			else
			{
				geoTweetLoc=null;
			}
			valTime1=(Integer)DBConnect.fetchTweetFieldFromDBGivenID("createdAt", tweetid); 
			//DB de butun olaylarin icinde dolas

			query = QueryBuilder.start().and(
					QueryBuilder.start("main").is("1").get() //yani ana olay i getir
					).get();


			DBObject o=null;
			cursor = DBConnect.getCollSituation().find(query);
			//butun olaylarin icinde dolas ve GPS karsilastir elindeki tweet ile
			DBObject oClosest=null;
			double valClosest=999999,valTemp=0;
			while(cursor.hasNext())
			{
				o=cursor.next();
				geoGPS=new GeoLocation(Double.valueOf(o.get("TweetLat").toString()), Double.valueOf(o.get("TweetLong").toString()));
				System.out.println("POS1:"+Double.valueOf(o.get("TweetLat").toString())+","+Double.valueOf(o.get("TweetLong").toString()));
				System.out.println("POS2:"+strTweetLat+","+strTweetLong);

				valTime2=Integer.parseInt(o.get("timestamp").toString());
				//simdi mesafeyi karsilastir
				valTemp=DistanceBetweenTwoGPS.distanceBetweenTwoGPS(geoTweetLoc, geoGPS);
				System.out.println("DISTANVE"+valTemp);
				if(valTemp<=DISTANCE_THRESHOLD
						&& Math.abs(valTime2-valTime1)<TIME_THRESHOLD) 
				{
					//olaya yakin peki zaman ayni mi
					//en yakin olayi bulmaliyiz
					if(valTemp<valClosest)
					{
						valClosest=valTemp;
						oClosest=o;
						boolEventFound=true;
					}

				}
			}
			//olay var ona ilistir
			if(boolEventFound)
			{
				Integer valSituationId=Integer.parseInt(oClosest.get("situationid").toString());
				DBConnect.insertToExistingSituation(valSituationId, tweetid, strTweetCountry, strTweetCity, strTweetState, strTweetTown, strTweetStreet, strTweetLat, strTweetLong, valTime1,strEventType);
				//oeger yenisi daha dolu veriler sagliyorsa main olani update et


				//official ise mesaj ana mesaji officialArrived olarak update et.
				if(((String)DBConnect.fetchTweetFieldFromDBGivenID("classifiedAs",tweetid)).equals("official positive"))
				{
					DBObject query1 = new BasicDBObject();
					query1 = QueryBuilder.start().and(
							QueryBuilder.start("situationid").is(valSituationId).get(),
							QueryBuilder.start("main").is("1").get()
							).get();
					DBObject newObject =  DBConnect.getCollSituation().find(query1).toArray().get(0);
					newObject.put("officialArrived","1");
					DBConnect.getCollSituation().findAndModify(query1, newObject);
				}

			}
			else
			{
				//yeni olay 
				DBConnect.insertNewSituation(tweetid,strTweetCountry, strTweetCity, strTweetState, strTweetTown, strTweetStreet, strTweetLat, strTweetLong, valTime1,strEventType);
			}

			cursor.close();
		}
		else
		{
			//tweette lokasyon hic cikmamis SVm nasil oldu da positive classify etti
		}
		//herseyi clear et
		clearVars();
		
		System.out.println("TWEET_PROCESS_OLCUM_CIKIS:"+tweetid.toString()+"-"+System.currentTimeMillis());



	}

	//gelen fonksiyonu adina gonderir. giris cikislarini ayarlamali
	void functionDispatcher(ObjectId tweetid,String strOrgTweetText, String strFnName,String strTweetCountry,String strTweetCity,String strTweetTown,String strTweetState,String strTweetStreet,String strTweetLat,String strTweetLong,String strStatus, Boolean boolRequest) throws NumberFormatException, AnnotationException, JSONException, IOException
	{
		HashMap< String, String> hashmapTemp=new HashMap<String, String>();

		hashmapTemp.put("country", strTweetCountry);
		hashmapTemp.put("city", strTweetCity);
		hashmapTemp.put("town", strTweetTown);
		hashmapTemp.put("state",strTweetState);
		hashmapTemp.put("street",strTweetStreet);
		hashmapTemp.put("lat",strTweetLat);
		hashmapTemp.put("long",strTweetLong);
		//		System.out.println("FN DISPATCHER fn name"+strFnName);
		if(strFnName.equals("prepareAdditionalLocationInfoFromTweet"))
		{
			prepareAdditionalLocationInfoFromTweet((HashMap< String, String>)hashmapTemp.clone(),tweetid,Integer.parseInt(strStatus), boolRequest);

		}
		else if(strFnName.equals("extractProfileLocation"))
		{
			System.out.println("functionDispatcher:"+tweetid.toString());
			extractProfileLocation((HashMap< String, String>)hashmapTemp.clone(),true, tweetid, null, Integer.parseInt(strStatus));
		}
		else if(strFnName.equals("extractLocationFromTweetText"))
		{
			extractLocationFromTweetText((HashMap< String, String>)hashmapTemp.clone(),true, (String)DBConnect.fetchTweetFieldFromDBGivenID("originalTweet", tweetid), tweetid, Integer.parseInt(strStatus));
		}
		else if(strFnName.equals("extractLocationFromTweet"))
		{
			extractLocationFromTweet((HashMap< String, String>)hashmapTemp.clone(),strOrgTweetText, tweetid, Integer.parseInt(strStatus));
		}
		hashmapTemp.clear();
		hashmapTemp=null;
		clearVars();
		//		System.gc(); //yeni kapattim
	}
	//tweetid fn adi, state, istenilen loclari alir requestOSM tablosuna atar
	void osmRequest(Boolean boolGPSToAddress,ObjectId tweetid, String strFnName,String strTweetCountry,String strTweetCity,String strTweetTown,String strTweetState,String strTweetStreet,String strTweetLat,String strTweetLong,String strStatus)
	{
		Integer valTimeStamp=(Integer)DBConnect.fetchTweetFieldFromDBGivenID("createdAt", tweetid);
		DBConnect.insertToRequestOSM(boolGPSToAddress,valTimeStamp,tweetid, strFnName, strTweetCountry, strTweetCity, strTweetTown, strTweetState, strTweetStreet, strTweetLat, strTweetLong, strStatus);
	}

	//tweeti ver ondan ne kadar lokasyon cikartirsa cikartir disaridan sadece bunu cagir
	public void extractLocationFromTweet(HashMap< String, String> hashmapTemp,String strOrgTweettext,ObjectId tweetid,Integer valState) throws AnnotationException, JSONException, IOException
	{

		//akisa basla hersey hazir
		//DBPEdia yada OSM ye baglanti gerekmiyor bu noktadan sonra
		Boolean boolEnd=false;
		String strGpsAvail,strTZAvail,strProfileAvail,strTextLocAvail;
		strGpsAvail=(String)DBConnect.fetchFieldFromDBGivenID("gpsLocationAvailable", tweetid);
		strTZAvail=(String)DBConnect.fetchFieldFromDBGivenID("timezoneAvailable", tweetid);
		strProfileAvail=(String)DBConnect.fetchFieldFromDBGivenID("profileLocationAvailable", tweetid);
		strTextLocAvail=(String)DBConnect.fetchFieldFromDBGivenID("tweetTextLocationAvailable", tweetid);

		while(true)
		{
			if(boolEnd)
			{
				//tweeti assign et bi olaya
				//burda hata var assginda Double val gpsle ilgili?
				if(((String)DBConnect.fetchFieldFromDBGivenID("tweetLocationAvailable", tweetid)).equals("1")
						&&DBConnect.fetchFieldFromDBGivenID("TweetLat", tweetid)!=null)
				{
					System.out.println("TWEET ASSIGNED:"+tweetid.toString());
					assignTweetToEvent(tweetid);
				}
				break;
			}
			switch (valState)
			{
			case 0: //tweet text te location varmi
				if(strTextLocAvail!=null&&strTextLocAvail.equals("1"))
				{
					valState=1;
				}
				else
				{
					valState=2;
				}
				break;
			case 1:
				//tweetten lokasyon cikarilmis gps varmi
				//tweetten lokasyon cikartilamamais
				//GPS varmi bak
				if(strGpsAvail!=null&&strGpsAvail.equals("1"))
				{
					valState=12;
				}
				else
				{
					valState=13;
				}
				break;
			case 2:
				//tweetten lokasyon cikartilamamais
				//GPS varmi bak
				if(strGpsAvail!=null&&strGpsAvail.equals("1"))
				{
					valState=3;
				}
				else
				{
					valState=4;
				}
				break;
			case 3:
				//tweet lok yok gps var
				//dbden herturlu gps isini al store etmeye gonder son karar olarak tweet budur diye
				strTweetCountry=(String)DBConnect.fetchFieldFromDBGivenID("GPSCountry", tweetid);
				strTweetCity=(String)DBConnect.fetchFieldFromDBGivenID("GPSCity", tweetid);
				strTweetState=(String)DBConnect.fetchFieldFromDBGivenID("GPSState", tweetid);
				strTweetTown=(String)DBConnect.fetchFieldFromDBGivenID("GPSTown", tweetid);
				strTweetStreet=(String)DBConnect.fetchFieldFromDBGivenID("GPSStreet", tweetid);
				strTweetLat=(String)DBConnect.fetchFieldFromDBGivenID("GPSLat", tweetid); 
				strTweetLong=(String)DBConnect.fetchFieldFromDBGivenID("GPSLong", tweetid); 
				valState=5;
				break;
			case 4:
				//tweet lok yok gps yok profil lok varmi
				if(strProfileAvail!=null&&strProfileAvail.equals("1"))
				{
					valState=6;
				}
				else
				{
					valState=7;
				}
				break;
			case 5: //dbye save etme isi bunda
				DBConnect.insertFieldToTweetGivenID("tweetLocationAvailable","1", tweetid);

				DBConnect.insertFieldToTweetGivenID("TweetCity",strTweetCity, tweetid);
				DBConnect.insertFieldToTweetGivenID("TweetCountry",strTweetCountry, tweetid);
				DBConnect.insertFieldToTweetGivenID("TweetState",strTweetState, tweetid);
				DBConnect.insertFieldToTweetGivenID("TweetStreet",strTweetStreet, tweetid);
				DBConnect.insertFieldToTweetGivenID("TweetTown",strTweetTown, tweetid);
				DBConnect.insertFieldToTweetGivenID("TweetLat",strTweetLat, tweetid);
				DBConnect.insertFieldToTweetGivenID("TweetLong",strTweetLong, tweetid);
				boolEnd=true;
				break;
			case 6: ////tweet lok yok gps yok profil var
				strProfileCountry=(String)DBConnect.fetchFieldFromDBGivenID("ProfileCountry", tweetid);
				strProfileCity=(String)DBConnect.fetchFieldFromDBGivenID("ProfileCity", tweetid);
				strProfileState=(String)DBConnect.fetchFieldFromDBGivenID("ProfileState", tweetid);
				strProfileTown=(String)DBConnect.fetchFieldFromDBGivenID("ProfileTown", tweetid);
				strProfileStreet=(String)DBConnect.fetchFieldFromDBGivenID("ProfileStreet", tweetid);
				strProfileLat=(String)DBConnect.fetchFieldFromDBGivenID("ProfileLat", tweetid); 
				strProfileLong=(String)DBConnect.fetchFieldFromDBGivenID("ProfileLong", tweetid); 
				valState=8;
				break;
			case 7: ////tweet lok yok gps yok profil yok tz varmi
				if(strTZAvail!=null&&strTZAvail.equals("1"))
				{
					valState=9;
				}
				else
				{
					valState=10;
				}
				break;
			case 8://tweet lok yok gps yok profil var TZ varmi
				if(strTZAvail!=null&&strTZAvail.equals("1"))
				{
					valState=11;		
				}
				else
				{
					//sadece profildeki deki bilgiler var
					strTweetCountry=(String)DBConnect.fetchFieldFromDBGivenID("ProfileCountry", tweetid);
					strTweetCity=(String)DBConnect.fetchFieldFromDBGivenID("ProfileCity", tweetid);
					strTweetState=(String)DBConnect.fetchFieldFromDBGivenID("ProfileState", tweetid);
					strTweetTown=(String)DBConnect.fetchFieldFromDBGivenID("ProfileTown", tweetid);
					strTweetStreet=(String)DBConnect.fetchFieldFromDBGivenID("ProfileStreet", tweetid);
					strTweetLat=(String)DBConnect.fetchFieldFromDBGivenID("ProfileLat", tweetid); 
					strTweetLong=(String)DBConnect.fetchFieldFromDBGivenID("ProfileLong", tweetid); 
					valState=5;
				}
				break;
			case 9://tweet lok yok gps yok profil yok TZ var
				DBConnect.insertFieldToTweetGivenID("tweetLocationAvailable","1", tweetid);
				strTweetCity=(String)DBConnect.fetchFieldFromDBGivenID("timezoneCities", tweetid);
				DBConnect.insertFieldToTweetGivenID("TweetCities",strTweetCity, tweetid);
				boolEnd=true;
				break;
			case 10://tweet lok yok gps yok profil yok TZ yok
				DBConnect.insertFieldToTweetGivenID("tweetLocationAvailable","0", tweetid);
				boolEnd=true;
				break;
			case 11:
				//tweet lok yok gps yok profil var TZ var
				//timezone ile profile city eslesiyormu?
				strProfileCity=(String)DBConnect.fetchFieldFromDBGivenID("ProfileCity", tweetid);
				strTweetCity=(String)DBConnect.fetchFieldFromDBGivenID("timezoneCities", tweetid);
				if(strTweetCity.contains(strProfileCity))
				{
					//match var timezone dahil ekle yeniseye sadece bu var farkli
					DBConnect.insertFieldToTweetGivenID("TweetCities",strTweetCity, tweetid);
				}
				strTweetCountry=(String)DBConnect.fetchFieldFromDBGivenID("ProfileCountry", tweetid);
				strTweetCity=(String)DBConnect.fetchFieldFromDBGivenID("ProfileCity", tweetid);
				strTweetState=(String)DBConnect.fetchFieldFromDBGivenID("ProfileState", tweetid);
				strTweetTown=(String)DBConnect.fetchFieldFromDBGivenID("ProfileTown", tweetid);
				strTweetStreet=(String)DBConnect.fetchFieldFromDBGivenID("ProfileStreet", tweetid);
				strTweetLat=(String)DBConnect.fetchFieldFromDBGivenID("ProfileLat", tweetid); 
				strTweetLong=(String)DBConnect.fetchFieldFromDBGivenID("ProfileLong", tweetid); 
				valState=5;
				break;
			case 12://tweetten lok cikartilmis gps var
				//gps le iligili herseyi cek
				strTweetCountry=(String)DBConnect.fetchFieldFromDBGivenID("GPSCountry", tweetid);
				strTweetCity=(String)DBConnect.fetchFieldFromDBGivenID("GPSCity", tweetid);
				strTweetState=(String)DBConnect.fetchFieldFromDBGivenID("GPSState", tweetid);
				strTweetTown=(String)DBConnect.fetchFieldFromDBGivenID("GPSTown", tweetid);
				strTweetStreet=(String)DBConnect.fetchFieldFromDBGivenID("GPSStreet", tweetid);
				strTweetLat=(String)DBConnect.fetchFieldFromDBGivenID("GPSLat", tweetid); 
				strTweetLong=(String)DBConnect.fetchFieldFromDBGivenID("GPSLong", tweetid); 
				//tweet text ile ilgili herseyi cek
				strTweetTextCountry=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextCountry", tweetid);
				strTweetTextCity=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextCity", tweetid);
				strTweetTextState=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextState", tweetid);
				strTweetTextTown=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextTown", tweetid);
				strTweetTextStreet=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextStreet", tweetid);
				strTweetTextLat=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextLat", tweetid); 
				strTweetTextLong=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextLong", tweetid);
				if(strTweetCountry.equals(strTweetTextCountry)
						&&strTweetCity.equals(strTweetTextCity))
				{
					//adam olayin icinden tweet atmis ve gps var onu kullan
				}
				else
				{
					//gps var ama tweet ici lokasyon daha dogrudur
					strTweetCountry=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextCountry", tweetid);
					strTweetCity=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextCity", tweetid);
					strTweetState=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextState", tweetid);
					strTweetTown=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextTown", tweetid);
					strTweetStreet=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextStreet", tweetid);
					strTweetLat=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextLat", tweetid); 
					strTweetLong=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextLong", tweetid);
				}
				valState=5;
				break;
			case 13://tweetten lok cikartilmis gps yok
				//profil varmi?
				if(strProfileAvail!=null&&strProfileAvail.equals("1"))
				{
					valState=14;
				}
				else
				{
					valState=15;
				}
				break;
			case 14://tweetten lok cikartilmis gps yok profil var
				//profil hk al herseyi
				strProfileCountry=(String)DBConnect.fetchFieldFromDBGivenID("ProfileCountry", tweetid);
				strProfileCity=(String)DBConnect.fetchFieldFromDBGivenID("ProfileCity", tweetid);
				strProfileState=(String)DBConnect.fetchFieldFromDBGivenID("ProfileState", tweetid);
				strProfileTown=(String)DBConnect.fetchFieldFromDBGivenID("ProfileTown", tweetid);
				strProfileStreet=(String)DBConnect.fetchFieldFromDBGivenID("ProfileStreet", tweetid);
				strProfileLat=(String)DBConnect.fetchFieldFromDBGivenID("ProfileLat", tweetid); 
				strProfileLong=(String)DBConnect.fetchFieldFromDBGivenID("ProfileLong", tweetid); 
				//tweet hakinda alherseyi bilerek TweetCountrye ekledim
				strTweetCountry=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextCountry", tweetid);
				strTweetCity=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextCity", tweetid);
				strTweetState=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextState", tweetid);
				strTweetTown=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextTown", tweetid);
				strTweetStreet=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextStreet", tweetid);
				strTweetLat=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextLat", tweetid); 
				strTweetLong=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextLong", tweetid);
				if(strProfileCountry.equals(strTweetCountry)
						&&strProfileCity.equals(strTweetCity))
				{
					//adam olayin oldugu yere yakin bi yerde yasiyor demek
					//bunu istersen DBye ekleyebilirsin
				}
				else
				{
					//adam baska bir yerden atiyor ama tweet ici lokasyon olayi anlatirken genelde dogru
				}
				valState=5;
				break;
			case 15://tweetten lok cikartilmis gps yok profil yok
				//timezone varmi?
				if(strTZAvail!=null&&strTZAvail.equals("1"))
				{
					valState=16;
				}
				else
				{
					valState=18;
				}
				break;
			case 16://tweetten lok cikartilmis gps yok profil yok tz var
				strTweetTextCity=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextCity", tweetid);
				strTweetCity=(String)DBConnect.fetchFieldFromDBGivenID("timezoneCities", tweetid);
				if(strTweetCity.contains(strTweetTextCity))
				{
					//match var timezone dahil ekle yeniseye sadece bu var farkli
					//TZ tweet ici lokasyon ile ayni yani bilgi dogru denebilir
					DBConnect.insertFieldToTweetGivenID("TweetCities",strTweetCity, tweetid);
				}
				strTweetCountry=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextCountry", tweetid);
				strTweetCity=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextCity", tweetid);
				strTweetState=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextState", tweetid);
				strTweetTown=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextTown", tweetid);
				strTweetStreet=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextStreet", tweetid);
				strTweetLat=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextLat", tweetid); 
				strTweetLong=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextLong", tweetid); 
				valState=5;
				break;
			case 17://tweetten lok cikartilmis gps yok profil yok tz yok
				//hic bis lokasyon bulgisi yok
				DBConnect.insertFieldToTweetGivenID("tweetLocationAvailable","0", tweetid);
				boolEnd=true;
				break;
			case 18:
				strTweetCountry=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextCountry", tweetid);
				strTweetCity=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextCity", tweetid);
				strTweetState=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextState", tweetid);
				strTweetTown=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextTown", tweetid);
				strTweetStreet=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextStreet", tweetid);
				strTweetLat=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextLat", tweetid); 
				strTweetLong=(String)DBConnect.fetchFieldFromDBGivenID("TweetTextLong", tweetid); 
				valState=5;
				break;

			}
		}
		clearVars();


	}
	//tweete bagli hersey cikartilir lokasyon ve DBde update edilir. sonraki asamada
	//profil lokasyon bilgisini gonder dediginde bunun kaydettigi cikar 
	public void prepareAdditionalLocationInfoFromTweet(HashMap< String, String> hashmapTemp,ObjectId tweetid, Integer valState,Boolean boolRequest) throws AnnotationException, JSONException, IOException
	{
		String strFunctionName="prepareAdditionalLocationInfoFromTweet";
		String strGpsAvail=(String)DBConnect.fetchTweetFieldFromDBGivenID("geo", tweetid);
		String strTZAvail=(String)DBConnect.fetchTweetFieldFromDBGivenID("timezone", tweetid);
		String strProfileLocation=(String)DBConnect.fetchTweetFieldFromDBGivenID("location", tweetid);
		Boolean boolEnd=false,boolNext=false;
		//gelen GPS formati duzuenlemek icin sacma burasi
		String[] arrGps={};
		String[] arrGps1={};
		String[] arrGps2={};
		//		System.out.println("strGpsAvail"+strGpsAvail);
		if(strGpsAvail!=null)
		{
			arrGps=strGpsAvail.split(",");
			arrGps1=arrGps[0].split("=");
			arrGps2=arrGps[1].split("=");
		}
		while(true)
		{
			if(boolEnd)
			{
				//hashmap temp illaki yaratõlacak buna clone gšnderilecek 
				if(hashmapTemp!=null)
				{
					hashmapTemp.clear();
				}
				hashmapTemp=null;
				if(boolNext)
				{
					System.out.println("boolnextte profile locta giricek");
					extractLocationFromTweetText(hashmapTemp,false,(String)DBConnect.fetchTweetFieldFromDBGivenID("originalTweet", tweetid), tweetid, 0);
					System.out.println("boolnextte profile locta CIKTI");

				}
				//System.gc();
				break;
			}
			switch (valState)
			{
			case 0: //GPS varmi kontrol et
				//tweete gps attach edilmismi onu cek
				//yok ise
				System.out.println("prepareAdditionalLocationInfoFromTweet0strGpsAvail:"+strGpsAvail);
				if(strGpsAvail==null||strGpsAvail.equals("")||strGpsAvail.equals("0")) 
				{
					boolGPSAttachedToTweet=false;
					DBConnect.insertFieldToTweetGivenID("gpsLocationAvailable","0", tweetid);
					valState=1;
				}
				else
				{
					valState=2;
				}
				break;
			case 1: //timezone bilgisi varmi
				//tz yok
				System.out.println("prepareAdditionalLocationInfoFromTweet1strTZAvail:"+strTZAvail);
				if(strTZAvail==null||strTZAvail.equals("")||strTZAvail.equals("0"))
				{
					boolTimezoneAvail=false;

					DBConnect.insertFieldToTweetGivenID("timezoneAvailable","0", tweetid);
					valState=5;
				}
				else //timezone var
				{
					valState=4;
				}
				break;
			case 2: 
				if(!boolRequest)
				{
					//gps bilgisi var konvert et
					boolGPSAttachedToTweet=true;
					DBConnect.insertFieldToTweetGivenID("gpsLocationAvailable","1", tweetid);
					GeoLocation geoloc=new GeoLocation(Double.parseDouble(arrGps1[1]), Double.parseDouble(arrGps2[1].replace("}", "")));
					//bu noktada talep edicek noktaya gidecek
					osmRequest(true,tweetid, strFunctionName, "", "", "", "", "", String.valueOf(geoloc.getLatitude()), String.valueOf(geoloc.getLongitude()), "2");
					//cevap geldigi zaman hashMapLoc a gitmesi gerekiyor
					boolEnd=true;
				}
				else
				{
					//fn dispatcher sonucu hashmaploc a koyar
					//					hashmapLoc=GPSToAddressConverter(geoloc);
					//dbyi update et
					DBConnect.insertFieldToTweetGivenID("GPSCity",hashmapTemp.get("city"), tweetid);
					DBConnect.insertFieldToTweetGivenID("GPSCountry",hashmapTemp.get("country"), tweetid);
					DBConnect.insertFieldToTweetGivenID("GPSState",hashmapTemp.get("state"), tweetid);
					DBConnect.insertFieldToTweetGivenID("GPSTown",hashmapTemp.get("county"), tweetid);
					DBConnect.insertFieldToTweetGivenID("GPSStreet",hashmapTemp.get("street"), tweetid);

					if(!hashmapTemp.get("lat").equals("")&&!hashmapTemp.get("long").equals(""))
					{
						DBConnect.insertFieldToTweetGivenID("GPSLat",hashmapTemp.get("lat"), tweetid);
						DBConnect.insertFieldToTweetGivenID("GPSLong",hashmapTemp.get("long"), tweetid);		
					}


					valState=1;
				}
				break;
			case 4: //tz var
				boolTimezoneAvail=true;
				listAllPossibleCityCountryFromTimezone(strTZAvail);
				//simdi butun olasi sehirleri dbye at
				String strCities="";
				for (String str : listAllCitiesFromTimezone)
				{
					strCities+=str+",";
				}
				if(!strCities.equals(""))
				{
					DBConnect.insertFieldToTweetGivenID("timezoneAvailable","1", tweetid);
					DBConnect.insertFieldToTweetGivenID("timezoneCities",strCities, tweetid);
				}
				else		
				{
					DBConnect.insertFieldToTweetGivenID("timezoneAvailable","0", tweetid);

				}
				listAllCitiesFromTimezone.clear();
				strCities=null;
				valState=5;
				break;
			case 5: //profil check
				System.out.println("prepareAdditionalLocationInfoFromTweet5strProfileLoc:"+strProfileLocation);
				if(strProfileLocation==null||strProfileLocation.equals("")) //profil loc yok
				{
					valState=6;
				}
				else //profil loc var
				{
					valState=7;
				}
				break;			
			case 6: //profil loc yok
				//db update
				boolProfileLocationAvail=false;
				DBConnect.insertFieldToTweetGivenID("profileLocationAvailable","0", tweetid);
				boolEnd=true;
				boolNext=true;
				break;
			case 7: //profil loc var profil lok cikarttiktan sonra update ediorm profileLocationAvail
				//profil loc dbye obur fonksyionda atiorm
				hashmapTemp.clear();
				hashmapTemp=null;
				boolProfileLocationAvail=true;
				System.out.println("prepareAdditionalLocationInfoFromTweet7strProfileLocation:"+strProfileLocation);
				extractProfileLocation(hashmapTemp,false,tweetid,  null,0);
				System.out.println("tekrar profile locta");
				boolEnd=true;
				boolNext=true;
				break;
			case 8:
				boolEnd=true;
				boolNext=true;
				break;

			}
		}
		//System.gc();
		clearVars();

	}
	//timezonedan olasi butun ulke ve sehirleri cikartir
	//yani adam london dediyse onunla ayni timezoneda olan diger sehirlerde alinir
	public void listAllPossibleCityCountryFromTimezone(String strTimezone)
	{
		//simdilik sadece sehirlerde aricam. bu su sekilde bir ise yarayabilir. 
		//Adam St.Petersburg demistir sehir. ama kontinent asia seciliyse russia diger turlu america olur
		//yada adam timezone olarak amsterdam secmistir fakat farkli bir sehirde yasiyordur ancak o timezone ile 
		//yasadigi sehir ayni oldugu icin onu secmistir

		//dbden query yap icinde bahsi gecen timezonu tasiyan sehir var ise onunla
		//ayni gmt offsete sahipo diger sehirleri listeye al

		//ilk query bahsi gecen timezone varmi var ise onun gmt sini al
		DBObject query=null;
		String strGmt="";
		query = QueryBuilder.start().and(
				QueryBuilder.start("city").is(strTimezone).get()
				).get();


		DBObject o=null;
		o = DBConnect.getCollTimezones().findOne(query);

		if(o!=null)
		{
			strGmt=(String)o.get("gmtoffset");
		}

		//ikinci query gmtsi yukaridaki ile ayni olan cityleri cek
		query = QueryBuilder.start().and(
				QueryBuilder.start("gmtoffset").is(strGmt).get()
				).get();

		DBCursor cursor=DBConnect.getCollTimezones().find(query);
		while(cursor.hasNext())
		{
			o=cursor.next();
			//System.out.println(o.get("city"));
			listAllCitiesFromTimezone.add(o.get("city").toString());
		}
		cursor.close();

	}

	//hashmapin value ve key fieldlarini switch eder
	private HashMap<String, String> hashMapKeyValSwitch(HashMap<String, String> convert)
	{
		HashMap<String, String> temp=new HashMap<String, String>();
		Set set = convert.entrySet();
		Iterator i = set.iterator();
		while(i.hasNext())
		{
			Map.Entry me = (Map.Entry)i.next();
			temp.put(me.getValue().toString(), me.getKey().toString());
		} 
		//buna gelen detectten olan
		convert.clear();
		convert=null;
		return temp;
	}
	//extract profile lokation geneli
	//profilindeki lokasyonun neresi oldugunu 
	public void extractProfileLocation(HashMap< String, String> hashmapTemp,Boolean boolRequest,ObjectId tweetid, GeoLocation geoLoc,Integer valState) throws IOException, AnnotationException, JSONException
	{
		HashMap<String,String> hashmapEntityType=null;

		//queue ya ekleyince bu fonksiyonda kaldigi yerden devam edecek
		String strFunctionName="extractProfileLocation";
		Boolean boolEnd=false,boolNext=false;
		//once profilindeki lokasyonu alalim text
		String strProfileLocation=(String)DBConnect.fetchTweetFieldFromDBGivenID("location", tweetid);
		String strTemp=(String)DBConnect.fetchFieldFromDBGivenID("gpsLocationAvailable", tweetid);
		System.out.println("extractProfileLocationgpsLocationAvailable"+strTemp);
		if(strTemp.equals("0"))
			boolGPSAttachedToTweet=false;
		else
		{
			boolGPSAttachedToTweet=true;

			strGPSCountry=(String)DBConnect.fetchFieldFromDBGivenID("GPSCountry", tweetid);
			strGPSCity=(String)DBConnect.fetchFieldFromDBGivenID("GPSCity", tweetid);
			strGPSState=(String)DBConnect.fetchFieldFromDBGivenID("GPSState", tweetid);
			strGPSTown=(String)DBConnect.fetchFieldFromDBGivenID("GPSTown", tweetid);
			strGPSStreet=(String)DBConnect.fetchFieldFromDBGivenID("GPSStreet", tweetid);
			strGPSLat=(String)DBConnect.fetchFieldFromDBGivenID("GPSLat", tweetid); 
			strGPSLong=(String)DBConnect.fetchFieldFromDBGivenID("GPSLong", tweetid);
		}

		strTemp=(String)DBConnect.fetchFieldFromDBGivenID("timezoneAvailable", tweetid);
		if(strTemp.equals("0"))
			boolTimezoneAvail=false;
		else
			boolTimezoneAvail=true;

		String[] arrStrProfilelocation=strProfileLocation.split(" ");
		Boolean boolReq1=false,boolReq2=false;
		Span[] spanPositionLocations;
		String strLocationText="";
		Set set;
		Iterator it;
		while(true)
		{
			if(boolEnd)
			{
				if(hashmapTemp!=null)
				{
					hashmapTemp.clear();
				}
				hashmapTemp=null;
				if(boolNext)
				{
					prepareAdditionalLocationInfoFromTweet(hashmapTemp, tweetid, 8, false);
				}
				break;
			}
			switch (valState) 
			{
			case 0:	
				System.out.println("extractProfileLocation0strProfileLocation:"+strProfileLocation);

				//profil location nerler detect edilecek
				spanPositionLocations=textAnalytics.detectLocationEntitiesFromText(strProfileLocation).clone();
				//profil lokasyonunda duzgun texti olustur
				for (Span spn:spanPositionLocations)
				{
					strLocationText+=arrStrProfilelocation[spn.getStart()];

				}
				System.out.println("extractProfileLocation0strLocationText:"+strLocationText);
				System.out.println("extractProfileLocation0strProfileLocation:"+strProfileLocation);
				//detect ve recognize et 1) ya hicbisi dondurmez. 2)ya en az ulke ve sehir 3)tek sehir tek state tek ulke dondurur
				hashmapEntityType=detectAndRecognizeLocationEntitiesFromText(strProfileLocation);
				valState=1;
				break;

			case 1:
				if(hashmapEntityType.isEmpty())
				{
					valState=2;
					hashmapEntityType.clear();
					hashmapEntityType=null;
				}
				else
				{
					//2 ve 3 secenekler
					set = hashmapEntityType.entrySet();
					it = set.iterator();
					while(it.hasNext())
					{
						Map.Entry me = (Map.Entry)it.next();
						String strLocationType=me.getValue().toString();
						//strval setlerde varmi bunu kontrol edicez. eger sadece biri icin ok donuyorsa ambiguation
						if(boolReq1.equals(false))
							boolReq1=checkIfItemExistsInStringList(listLocationRequest1, strLocationType);
						if(boolReq2.equals(false))
							boolReq2=checkIfItemExistsInStringList(listLocationRequest2, strLocationType);

					} 
					//her iki listede de var yani verilen textin icinde hem profile country vede ona destek olacak state vs var

					if(boolReq1&&boolReq2||boolReq1)
					{
						valState=3;
					}
					else //sadece 1 listede uyusma var
					{
						valState=4;
					}
				}

				break;
			case 2: //dbpediadan sonuc donmedi

				hashmapEntityType=highlyPossibleLocationFromText(strProfileLocation);
				HashMap<String, String> temp=hashMapKeyValSwitch(hashmapEntityType);
				strProfileCity=temp.get("city");
				strProfileCountry=temp.get("country");
				strProfileTown=temp.get("town");
				strProfileState=temp.get("state");
				strProfileStreet=temp.get("street");


				//hihlgy possible bile birsey bulunamadi sacma bisiy yazmis

				if(temp.isEmpty())
					valState=12;
				else if(!temp.isEmpty()&&!boolRequest)
				{
					osmRequest(false,tweetid, strFunctionName, strProfileCountry,strProfileCity, strProfileTown, strProfileState,strProfileStreet, "","", "2");
					boolEnd=true;
				}
				else
				{
					//hashmapLoc=addressToGPSConverter(strProfileTown+","+strProfileState+","+strProfileCity+","+strProfileCountry);
					//ustteki bazi fieldlar bos olabilirdi ama addressToGPS bisiler dondurecek
					if(!hashmapTemp.isEmpty())
					{
						System.out.println("hashmap dolu 2");
						strProfileCity=hashmapTemp.get("city");
						strProfileCountry=hashmapTemp.get("country");
						strProfileTown=hashmapTemp.get("county");
						strProfileState=hashmapTemp.get("state");
						strProfileStreet=hashmapTemp.get("street");
						if(!hashmapTemp.get("lat").equals("")&&!hashmapTemp.get("long").equals(""))
						{
							geoProfileLoc=new GeoLocation(Double.parseDouble(hashmapTemp.get("lat")), Double.parseDouble(hashmapTemp.get("long")));
						}
						else
						{
							geoProfileLoc=null;
						}
						valState=10;

					}
					else //address cevrilemedi
					{
						valState=12;

					}

				}
				hashmapEntityType.clear();
				hashmapEntityType=null;
				temp.clear();
				temp=null;
				//System.gc();
				break;
			case 3:
				//dbpediadan geleni duzgun formata sok cevir.

				if(!boolRequest)
				{
					set = hashmapEntityType.entrySet();
					it = set.iterator();
					while(it.hasNext())
					{
						Map.Entry me = (Map.Entry)it.next();
						if(me.getValue().toString().equals("city"))
						{
							strProfileCity=me.getKey().toString();
						}
						else if(me.getValue().toString().equals("country"))
						{
							strProfileCountry=me.getKey().toString();
						}
						else if(me.getValue().toString().equals("state"))
						{
							strProfileState=me.getKey().toString();
						}
						else if(me.getValue().toString().equals("town"))
						{
							strProfileTown=me.getKey().toString();
						}

					} 
					hashmapEntityType.clear();
					hashmapEntityType=null;
					osmRequest(false,tweetid, strFunctionName, strProfileCountry,strProfileCity, strProfileTown, strProfileState,strProfileStreet, "","", "3");
					boolEnd=true;
					//hashmapLoc=addressToGPSConverter(strProfileTown+","+strProfileState+","+strProfileCity+","+strProfileCountry);


				}
				else
				{
					//hashmaptempe zaten fonksi.yon yoluyla geldi
					//verilen fieldlari guncelle openstreet dondukten sonra
					if(!hashmapTemp.isEmpty())
					{
						System.out.println("hashmap dolu 3");

						strProfileCity=hashmapTemp.get("city");
						strProfileCountry=hashmapTemp.get("country");
						strProfileTown=hashmapTemp.get("county");
						strProfileState=hashmapTemp.get("state");
						strProfileStreet=hashmapTemp.get("street");
						if((hashmapTemp.get("lat")!=null&&hashmapTemp.get("long")!=null)||(!hashmapTemp.get("lat").equals("")&&!hashmapTemp.get("long").equals("")))
						{
							geoProfileLoc=new GeoLocation(Double.parseDouble(hashmapTemp.get("lat")), Double.parseDouble(hashmapTemp.get("long")));
						}
						else
						{
							geoProfileLoc=null;
						}


						valState=10;
					}
					else
					{
						System.out.println("hashmap bos 3");

						valState=11;
					}
				}
				break;
			case 4:
				//sadece 1 bilgi var. ya sadece country yada city state vs.
				//once onlari alalim
				set = hashmapEntityType.entrySet();
				it = set.iterator();
				while(it.hasNext())
				{
					Map.Entry me = (Map.Entry)it.next();
					if(me.getValue().toString().equals("city"))
					{
						strProfileCity=me.getKey().toString();
					}
					else if(me.getValue().toString().equals("country"))
					{
						strProfileCountry=me.getKey().toString();
					}
					else if(me.getValue().toString().equals("state"))
					{
						strProfileState=me.getKey().toString();
					}
					else if(me.getValue().toString().equals("town"))
					{
						strProfileTown=me.getKey().toString();
					}

				} 
				hashmapEntityType.clear();
				hashmapEntityType=null;

				if(boolGPSAttachedToTweet)
				{
					valState=5;
				}
				else
				{
					valState=6;
				}
				break;
			case 5: //gps eklenmis 
				if(compareProfileWithGPS())
				{
					valState=7;
				}
				else
				{
					valState=6;
				}
				break;
			case 6: //gps eklenmemis yada /gps profil loc uyusmadi timezone a bak
				if(boolTimezoneAvail)
				{
					valState=8;
				}
				else
				{
					valState=2;
				}

				break;
			case 7: //gps ile profil loc uyusmus
				//gpsten cikartilan daha detayli olacagi icin onu gonderelim nasilsa uyusma var profile ile
				//zaten cikarildigi icin tekrar gondermeye gerek yok
				//hashmapLoc=addressToGPSConverter(strGPSTown+", "+strGPSState+", "+strGPSCity+", "+strGPSCountry);
				//verilen fieldlari guncelle openstreet dondukten sonra
				strProfileCity=strGPSCity;
				strProfileCountry=strGPSCountry;
				strProfileTown=strGPSTown;
				strProfileState=strGPSState;
				strProfileStreet=strGPSStreet;
				if(!strGPSLat.equals("")&&!strGPSLong.equals(""))
				{
					geoProfileLoc=new GeoLocation(Double.parseDouble(strGPSLat),Double.parseDouble(strGPSLong));
				}
				else
				{
					geoProfileLoc=null;
				}
				valState=10;

				break;
			case 8://timezone bilgisi var
				if(compareTimezoneWithText(tweetid,strProfileLocation)) //profil text ile tz arasinda match varmi
				{
					valState=2;
				}
				else
				{
					valState=2;
				}
				break;
			case 10:
				//dbupdate et profilden cikartilan bilgiler icin
				DBConnect.insertFieldToTweetGivenID("profileLocationAvailable","1", tweetid);
				DBConnect.insertFieldToTweetGivenID("ProfileCity",strProfileCity, tweetid);
				DBConnect.insertFieldToTweetGivenID("ProfileCountry",strProfileCountry, tweetid);
				DBConnect.insertFieldToTweetGivenID("ProfileState",strProfileState, tweetid);
				DBConnect.insertFieldToTweetGivenID("ProfileStreet",strProfileStreet, tweetid);
				DBConnect.insertFieldToTweetGivenID("ProfileTown",strProfileTown, tweetid);
				if(geoProfileLoc!=null)
				{
					DBConnect.insertFieldToTweetGivenID("ProfileLat",String.valueOf(geoProfileLoc.getLatitude()), tweetid);
					DBConnect.insertFieldToTweetGivenID("ProfileLong",String.valueOf(geoProfileLoc.getLongitude()), tweetid);
				}
				boolEnd=true;
				boolNext=true;
				break;
			case 11: //address cevrilemedi
				System.out.println("adres cevirilemedi");
				DBConnect.insertFieldToTweetGivenID("profileLocationAvailable","0", tweetid);
				boolEnd=true;
				if(boolRequest)
					boolNext=true;

				break;
			case 12:
				System.out.println("hashmap bos 2");
				//son care direk osm ye sormak AMA 3RD planet from sun gibi sacma ifadeler gelirse OSM patlar
				//				hashmapLoc=addressToGPSConverter(strProfileLocation);
				//ondan iptal bu kisim direk yolla bitsin
				valState=11;
				//				if(!boolRequest)
				//				{
				//					osmRequest(false,tweetid, strFunctionName, strProfileLocation,"", "", "","", "","", "12");
				//					boolEnd=true;
				//				}
				//				else
				//				{
				//					if(!hashmapTemp.isEmpty())
				//					{
				//						System.out.println("hashmap doldu 2");
				//						strProfileCity=hashmapTemp.get("city");
				//						strProfileCountry=hashmapTemp.get("country");
				//						strProfileTown=hashmapTemp.get("county");
				//						strProfileState=hashmapTemp.get("state");
				//						strProfileStreet=hashmapTemp.get("street");
				//						geoProfileLoc=new GeoLocation(Double.parseDouble(hashmapTemp.get("lat")), Double.parseDouble(hashmapTemp.get("long")));
				//						valState=10;
				//					}
				//					else
				//					{
				//						valState=11;
				//					}
				//				}
				break;
			}
		}
		clearVars();

	}
	//Profil lokasyon ile attachenmisse GPS lokasyonunu karsilastirir ulke sehir olarak
	public Boolean compareProfileWithGPS()
	{
		Boolean boolMatch=false;

		//GPS ulke veya sehir ile profile country veya sehir karsilastirirli
		if(strGPSCountry.equals(strProfileCountry)
				||strGPSCity.equals(strProfileCity)||
				strGPSState.equals(strProfileState))
		{
			boolMatch=true;
		}
		return boolMatch;
	}
	//profile lokasyon verilen text ile timezone olasi degerlerde eslesme varmi. yani profilde lokasyon paris
	//ama timezone Us ise texas paris cikartmayi bekleriz fransa yerine.
	public Boolean compareTimezoneWithText(ObjectId tweetid, String text)
	{
		Boolean flag=false;
		String strPossibleTimezones=(String)DBConnect.fetchTweetFieldFromDBGivenID("timezoneCities", tweetid);
		//verilen text muhtemelen city bizin timezonedan cikarttigimiz olasi timezonelarda varmi
		if(strPossibleTimezones==null)
			return false;
		if(strPossibleTimezones!=null||!strPossibleTimezones.equals(""))
			flag=strPossibleTimezones.contains(text);
		return flag;
	}
	//text verdin en popular sehiri ne ise DBPediadan o cekilir. misal sadece paris ise fransa paris
	public HashMap<String,String> highlyPossibleLocationFromText(String text) throws AnnotationException
	{
		//hashMapEntityType.clear();
		return detectAndRecognizeLocationEntitiesFromText(text);
	}

	//daha detayli veri cekmek istersen kullanabilirsin SPARQL
	//http://dbpedia.org/snorql/?query=PREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0D%0APREFIX+dbpprop%3A+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2F%3E%0D%0APREFIX+owl%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0D%0APREFIX+src%3A+%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2F%3E%0D%0APREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0D%0A%0D%0ASELECT+%3Fcountry_name%0D%0AWHERE%0D%0A{%0D%0A++++src%3AWallisellen+rdf%3Atype+%3Fcountry_name+.%0D%0A}
	//PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
	//PREFIX dbpprop: <http://dbpedia.org/property/>
	//PREFIX owl: <http://www.w3.org/2002/07/owl#>
	//PREFIX src: <http://dbpedia.org/resource/>
	//PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
	//
	//SELECT ?country_name
	//WHERE
	//{
	//	src:Istanbul rdf:type ?country_name .
	//}
	public void Sparql()
	{

	}

	//kullanmiyorum bunu
	@Override
	public List<DBpediaResource> extract(Text text) throws AnnotationException
	{


		return null;
	}

	//DBPedia bazen tam tipini donemiyor ornek Settlement aslinda sehir bunu degistir mapte
	private void typeConversionInHashMap(HashMap<String,String> hashmapEntityType)
	{
		Set set = hashmapEntityType.entrySet();
		Iterator i = set.iterator();
		// mapteki butun elemanlara bak ve gerekli degisiklikleri yap
		while(i.hasNext())
		{
			Map.Entry me = (Map.Entry)i.next();
			String strVal=me.getValue().toString();
			if(strVal.toLowerCase().equals("settlement"))
			{
				me.setValue("city");
			}
		} 
	}

	//resource objesini ve entitiy name gonder hashmap'e eklesin
	private void surfaceFormTextToHashMap(HashMap<String,String> hashmapEntityType,String name,JSONObject resourceObj) throws JSONException
	{
		String typesStr=resourceObj.getString("@types");
		String[] typesArr=typesStr.split(", ");
		boolean found=false;
		for (String str : typesArr)
		{
			if(found)
				break;
			//System.out.println(name+str);
			for (locationTypes locType:locationTypes.values())
			{
				//eger listemdeki location turunden biri uyuyorsa DBPediadan gelen tiplerden biriyle
				if(str.toLowerCase().contains(locType.toString()))
				{
					hashmapEntityType.put(name, locType.toString());
					found=true;
					break;
				}
			}
		}
		typeConversionInHashMap(hashmapEntityType);

	}

	//map icin sort comparator
	private static Map sortByComparator(Map unsortMap)
	{

		List list = new LinkedList(unsortMap.entrySet());

		//sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		//put sorted list into map again
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}    

	private Boolean checkIfItemExistsInStringList(List<String> list,String item)
	{
		Boolean found=false;
		for (String it:list)
		{

			if(item.toLowerCase().contains(it))
			{

				found=true;
				break;
			}
		}
		return found;
	}

	//DBPediadan resource gelince onu support valuesuna gore sort et en buyuk olani kullan
	private JSONObject highestSupportedObject(JSONArray resourceArr)
	{
		Map<String, Integer> unsortMap = new HashMap<String,Integer>();

		JSONObject resourceObj=null;

		for(int i=0;i<resourceArr.length();i++)
		{
			//objeleri sirasiyla al
			try 
			{
				JSONObject obj=resourceArr.getJSONObject(i);
				int valSupport=obj.getInt("@support");
				unsortMap.put(obj.getString("@uri"), Integer.valueOf(valSupport));
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//onceden nasildi sort edilmeden

		//		Iterator iterator=unsortMap.entrySet().iterator();
		//	
		//		for (Map.Entry entry : unsortMap.entrySet())
		//		{
		//			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
		//		}

		//sort ettikten sonra en buyuk olan en sonda olacak
		//System.out.println("Sorted Map......");
		Map<String,String> sortedMap =  sortByComparator(unsortMap);

		int i=0;
		int sortMapSize=sortedMap.entrySet().size()-1;
		String strUriName=null;

		for (Map.Entry entry : sortedMap.entrySet())
		{
			//son eleman en buyuk score u olan eleman
			if(i==sortMapSize)
			{
				strUriName=entry.getKey().toString();
				break;
			}
			i++;
			//System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
		}
		//uri name var elimizde gidip direk o resource u alioruz
		for (i=0;i<resourceArr.length();i++)
		{
			//objeleri sirasiyla al
			try 
			{
				JSONObject obj=resourceArr.getJSONObject(i);
				String strUri=obj.getString("@uri");
				if(strUri.equals(strUriName))
				{
					resourceObj=obj;
					break;
				}
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resourceObj;
	}

	//verilen textteki lokasyonlari bulur ve recognize eder
	private HashMap<String,String> detectAndRecognizeLocationEntitiesFromText(String text) throws AnnotationException
	{
		String spotlightResponse=null;
		LinkedList<DBpediaResource> resourceSurfaceForm;
		HashMap<String,String> hashmapEntityType=new HashMap<String,String>();
		JSONObject resultJSON = null;

		LOG.info("Querying API.");
		try 
		{
			GetMethod getMethod = new GetMethod(API_URL_DBPEDIA + "rest/candidates?" +
					"confidence=" + CONFIDENCE
					+ "&support=" + SUPPORT
					+ "&text=" + URLEncoder.encode(text, "utf-8")
					+ "&disambiguator=Document"
					+ "&types=Place"
					+ "&spotter=" + "NESpotter"
					+ "&coreferenceResolution=" + "false"
					); 
			synchronized (getMethod) {
				getMethod.addRequestHeader(new Header("Accept", "application/json"));

				spotlightResponse = request(getMethod);

			}
		} 
		catch (UnsupportedEncodingException e)
		{
			//throw new AnnotationException("Could not encode text.", e);
		}

		assert spotlightResponse != null;

		try
		{
			System.out.println("spot response:"+spotlightResponse);

			resultJSON = new JSONObject(spotlightResponse);

			System.out.println(resultJSON.toString());

			//candidates Array olarak donmuyor annotate gibi ondan
			//entities = resultJSON.getJSONArray("Resources"); 
			JSONObject annotationObj=resultJSON.getJSONObject("annotation");
			JSONObject surfaceFormObj=null;
			JSONObject resourceObj=null;
			JSONArray surfaceFormArr=null;
			JSONArray resourceArr=null;
			boolean boolSurfaceFormArray=false;
			//bir entity icin birden fazla match olabilir
			boolean boolResourceArray=false; 
			boolean boolExistsSurfaceForm=false;
			try
			{				
				//surfaceForm eger multiple cevap varsa array olarak donuyor
				if(annotationObj.has("surfaceForm"))
				{
					boolExistsSurfaceForm=true;
					surfaceFormObj=annotationObj.getJSONObject("surfaceForm"); 
				}
			} 
			catch (Exception e)
			{
				boolSurfaceFormArray=true;
				System.out.println("surfaceForm array");
				surfaceFormArr=annotationObj.getJSONArray("surfaceForm"); 
			}

			//birkac tane entity bulunmus olabilir yada bir tane
			if(boolSurfaceFormArray&&boolExistsSurfaceForm) 
			{					

				resourceSurfaceForm= new LinkedList<DBpediaResource>();
				for(int i=0;i<surfaceFormArr.length();i++)
				{
					//objeleri sirasiyla al
					JSONObject obj=surfaceFormArr.getJSONObject(i);
					if(obj.has("resource"))
					{
						//resource array ise support u en buyuk olani al
						try
						{	
							resourceObj=obj.getJSONObject("resource"); 
						} 
						catch (Exception e)
						{
							boolResourceArray=true;
							System.out.println("resource array");
							resourceArr=obj.getJSONArray("resource"); 
							//resource arraydan supportu en buyuk olani al
							resourceObj=highestSupportedObject(resourceArr);
						}

						String strName=obj.getString("@name");
						surfaceFormTextToHashMap(hashmapEntityType,strName,resourceObj);
					}
				}
			}
			else if(boolExistsSurfaceForm)
			{
				if(surfaceFormObj.has("resource"))
				{
					//resource array ise support u en buyuk olani al
					try
					{	
						resourceObj=surfaceFormObj.getJSONObject("resource"); 
					} 
					catch (Exception e)
					{
						boolResourceArray=true;
						System.out.println("resource array");
						resourceArr=surfaceFormObj.getJSONArray("resource"); 
						//resource arraydan supportu en buyuk olani al
						resourceObj=highestSupportedObject(resourceArr);
					}

					String strName=surfaceFormObj.getString("@name");
					surfaceFormTextToHashMap(hashmapEntityType,strName,resourceObj);
				}
			}

			System.out.println(hashmapEntityType.toString());
		} 
		catch (JSONException e)
		{
			//throw new AnnotationException("Received invalid response from DBpedia Spotlight API.");

		}
		//array olmadigi icin gereksiz
		//		resources = new LinkedList<DBpediaResource>();
		//		System.out.println(entities.length());
		//		for(int i = 0; i < entities.length(); i++) {
		//			try {
		//				JSONObject entity = entities.getJSONObject(i);
		//				resources.add(
		//						new DBpediaResource(entity.getString("@URI"),
		//								Integer.parseInt(entity.getString("@support"))));
		//
		//
		//				System.out.println(entity.getString("@types"));
		//				//tipin icinde town country yada city varsa ona gore ayir simdilik
		//
		//
		//
		//			} catch (JSONException e) {
		//				LOG.error("JSON exception "+e);
		//			}
		//
		//		}

		return hashmapEntityType;

	}


}
