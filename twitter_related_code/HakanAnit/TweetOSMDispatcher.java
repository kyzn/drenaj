import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;
import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.json.JSONException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

//requestOSM adli tabloya atilan tweetid, location, fn, state infosu, timestamp vs var.
//ama ci location ile ilgili olanlari alip batch halinde OSM ye sormak cevap geldiginde 
public class TweetOSMDispatcher implements Runnable
{
	private DBObject query;
	private BasicDBObject doc;
	private DBObject o;
	private DBCursor cur;
	private Long valCountRequest;
	private LocationManager locMan;
	private HashMap<String, String> hashmapLoc;
	private String strTemp,strAddress;
	private Integer valBatchSize=2;
	private Set set;
	private IndexManager im;
	private TweetWaitingDispatcher twd;

	public TweetOSMDispatcher(LocationManager locMan,IndexManager im,TweetWaitingDispatcher twd) throws UnknownHostException, FileNotFoundException
	{
		this.twd=twd;
		DBConnect.connect();
		query=null;
		doc=new BasicDBObject();
		this.locMan=locMan;
		strTemp="";
		strAddress="";

		this.im=im;
	}

	public void updateDB() throws InterruptedException
	{
		String strPrevId="";
		for (String entry : im.listBatch) 
		{

			//System.out.println("request O4444444444 ");
			//: dan bol ikinci parcada lokation ilk parcayi , le bol ilk parcasi id ikinci parca tipi
			String[] strTemp=entry.split(":");
			String strLocation="";
			if(strTemp.length==2)
				strLocation=strTemp[1];

			String[] strIDAndType=strTemp[0].split(",");
			System.out.println("strIDAndType[0]"+strIDAndType[0]);
			System.out.println("strIDAndType[1]"+strIDAndType[1]);

			String strValId=strIDAndType[0];

			System.out.println("vaID"+strValId);
			System.out.println("prevID"+strPrevId);

			if(!strValId.equals(strPrevId))
			{
				//once insert yap bu tweetwaitinge
				strPrevId=strValId;
				ObjectId objId=new ObjectId(strValId);
				System.out.println("OBJID"+objId.toString());
				//tweet ile ilgili bilgilerin request osmden alinmasi gerekli
				Integer valTimestamp=(Integer)DBConnect.fetchFieldFromRequestOSMGivenID("timestamp", objId);
				String strFunctionName=(String)DBConnect.fetchFieldFromRequestOSMGivenID("functionName", objId);
				String strTweetCountry=(String)DBConnect.fetchFieldFromRequestOSMGivenID("country", objId);
				String strTweetCity=(String)DBConnect.fetchFieldFromRequestOSMGivenID("city", objId);
				String strTweetTown=(String)DBConnect.fetchFieldFromRequestOSMGivenID("town", objId);
				String strTweetState=(String)DBConnect.fetchFieldFromRequestOSMGivenID("state", objId);
				String strTweetStreet=(String)DBConnect.fetchFieldFromRequestOSMGivenID("street", objId);
				String strTweetLat=(String)DBConnect.fetchFieldFromRequestOSMGivenID("lat", objId);
				String strTweetLong=(String)DBConnect.fetchFieldFromRequestOSMGivenID("long", objId);
				String strStatus=(String)DBConnect.fetchFieldFromRequestOSMGivenID("status", objId);
				String strTweetText=(String)DBConnect.fetchFieldFromRequestOSMGivenID("tweetText", objId);
				DBConnect.insertToTweetWaiting(objId, strTweetText,valTimestamp, strFunctionName, strTweetCountry, strTweetCity, strTweetTown, strTweetState, strTweetStreet, strTweetLat, strTweetLong, strStatus,true);							
				//dbde hemen hazir olmayip else e girebiliyor.
				Thread.sleep(100);
				DBConnect.getCollRequestOSM().remove(new BasicDBObject("tweetid",objId));

				System.out.println("request OSM REMOVED bunda:"+objId.toString());

			}
			else
			{
				BasicDBObject query = new BasicDBObject();
				query.put("tweetid", new ObjectId(strIDAndType[0]));
				//shard icin _id gerekli
				DBObject dbObj = DBConnect.getCollTweetWaiting().findOne(query);
				//BUrasi biraz supheli
				while(dbObj==null)
				{
					dbObj =DBConnect.getCollTweetWaiting().findOne(query);
					Thread.sleep(1000);

				}
				//data hemen available olamiyabvilir ve hata verebilir alttaki null diye
				query.put("_id",(ObjectId)dbObj.get("_id"));	
				DBObject newObject = DBConnect.getCollTweetWaiting().find(query).toArray().get(0);
				newObject.put(strIDAndType[1],strLocation);
				DBConnect.getCollTweetWaiting().findAndModify(query, newObject);
				newObject=null;
				query=null;
			}
		} 

	}
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//eger request sayisi 100 ise OSMye batch sorgu gonderilecek
			valCountRequest=DBConnect.getCollRequestOSM().count();
			System.out.println("request OSM size:"+valCountRequest);


			if(im.listBatch!=null)
			{
				//birşeyler var ve daha TweetWaiting tarafından boşşaltılmamış
				System.out.println("List BATCH size:"+im.listBatch.size());

			}
			else
			{
				if(im.listIds!=null)
					im.listIds.clear();
				if(im.listLocation!=null)
					im.listLocation.clear();
			}

			if(valCountRequest>=valBatchSize&&im.listBatch==null)
			{
				cur =DBConnect.getCollRequestOSM().find().sort(new BasicDBObject("timestamp", 1)).limit(valBatchSize);
				while (cur.hasNext())
				{
					strTemp="";
					o=cur.next();
					//gps alicaksin
					if(((String)o.get("toaddress")).equals("1"))
					{
						strTemp+=(String)o.get("lat")+" "+(String)o.get("long");
					}
					else
					{
						//geri kalan her lokasyonu al
						strAddress=(String)o.get("town")+","+(String)o.get("state")+","+(String)o.get("city")+","+(String)o.get("country");
						strAddress=strAddress.replaceAll("null", "");
						char chPrev = 0;
						for (char ch: strAddress.toCharArray())
						{
							if(chPrev==','&&ch!=',')
							{
								strTemp+=chPrev;
								strTemp+=ch;
							}
							else if(chPrev!=','&&ch!=',')
							{
								strTemp+=ch;
							}

							chPrev=ch;
						}
					}
					if(strTemp.charAt(0)==',')
					{
						strTemp=strTemp.replaceFirst(",", "");
					}
					//System.out.println("request OSMMMMMMMMM LOC"+strTemp);

					im.listLocation.add(strTemp);
					ObjectId objTemp=(ObjectId)o.get("tweetid");
					im.listIds.add(objTemp);
					try
					{
						Thread.sleep(100);
					} 
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//listeyi batch query icin gonder cevabi buna al dbyi update et

				//					System.out.println("request OSMMMMMMMMM ");

				//cevapta hersey var gidip tweetwaitingde yeni query gireceksin
				synchronized (twd.boolRequest)
				{
					twd.boolRequest=true;
				}
			}
		}
	}
}
