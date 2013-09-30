import java.io.Console;
import java.net.UnknownHostException;

import org.apache.lucene.document.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.QueryBuilder;
import com.mongodb.ReadPreference;


public class DBConnect
{
	private static DB db;
	private static Mongo m;


	public static DBCollection collSituation,collTimezones,collEvent,collAnalysis,collWordId,collAnalysisStartEndTime,collAnalysisIntervalReadyRepords,collTweetWaiting,
	collRequestOSM,collUserTrust,collMobileUser;


	public static DBCollection getCollMobileUser() {
		return collMobileUser;
	}
	public static void setCollMobileUser(DBCollection collMobileUser) {
		DBConnect.collMobileUser = collMobileUser;
	}
	public static DBCollection getCollUserTrust() {
		return collUserTrust;
	}
	public static void setCollUserTrust(DBCollection collUserTrust) {
		DBConnect.collUserTrust = collUserTrust;
	}
	public static DBCollection getCollRequestOSM() {
		return collRequestOSM;
	}
	public static void setCollRequestOSM(DBCollection collRequestOSM) {
		DBConnect.collRequestOSM = collRequestOSM;
	}
	public static DBCollection getCollTweetWaiting() {
		return collTweetWaiting;
	}
	public static void setCollTweetWaiting(DBCollection collTweetWaiting) {
		DBConnect.collTweetWaiting = collTweetWaiting;
	}
	public static DBCollection getCollSituation() {
		return collSituation;
	}
	public static void setCollSituation(DBCollection collSituation) {
		DBConnect.collSituation = collSituation;
	}
	public static DBCollection getCollTimezones() {
		return collTimezones;
	}
	public static void setCollTimezones(DBCollection collTimezones) {
		DBConnect.collTimezones = collTimezones;
	}
	public static DBCollection getCollWordId() {
		return collWordId;
	}

	public static int queryCount; //query sonuc donerse count buraya
	
	public static void createDBIndexes()
	{
		DBObject obj = BasicDBObjectBuilder.start().add("eventType", 1).add("tweet.createdAt", 1).get(); //compound index
		collEvent.createIndex(obj);
		collEvent.createIndex(new BasicDBObject("_id",1));
		collTweetWaiting.createIndex(new BasicDBObject("tweetid",1));
		collTweetWaiting.createIndex(new BasicDBObject("_id",1));
		obj = BasicDBObjectBuilder.start().add("situationid", 1).add("main", 1).get(); //compound index
		collSituation.createIndex(obj);
		collSituation.createIndex(new BasicDBObject("situationid",1));
		collTimezones.createIndex(new BasicDBObject("timezoneid",1));
		collWordId.createIndex(new BasicDBObject("wordid",1));
		collEvent.createIndex(new BasicDBObject("status",1)); 

		collEvent.createIndex(new BasicDBObject("eventType",1)); //guya bunun uzerinden indexlemis oldum
		collRequestOSM.createIndex(new BasicDBObject("tweetid",1));
		//	dbConnect.getCollEvent().createIndex(new BasicDBObject("createdAt",1)); //guya bunun uzerinden indexlemis oldum
		//	dbConnect.getCollEvent().createIndex(new BasicDBObject("eventType",1),new BasicDBObject("tweet.createdAt",1));
		//https://groups.google.com/forum/?fromgroups=#!topic/mongodb-user/KN-RUGW0B1A
		collEvent.createIndex(new BasicDBObject("tweet.createdAt",1)); //guya bunun uzerinden indexlemis oldum
//		collEvent.createIndex(new BasicDBObject("tweet.isGeoAttached",1)); //guya bunun uzerinden indexlemis oldum SILDIM
//		collEvent.createIndex(new BasicDBObject("annotated",1)); //guya bunun uzerinden indexlemis oldum SILDIM
		obj = BasicDBObjectBuilder.start().add("eventType", 1).add("annotated", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("status", 1).add("inSet", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("eventType", 1).add("annotated", 1).add("status", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("eventType", 1).add("annotated", 1).add("tweet.createdAt", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("eventType", 1).add("hasSet", 1).add("inSet", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("eventType", 1).add("hasSet", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("eventType", 1).add("status", 1).add("hasSet", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("eventType", 1).add("inSet", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("eventType", 1).add("inSet", 1).add("tweet.createdAt", 1).get(); //compound index
		collEvent.createIndex(obj);
//		obj = BasicDBObjectBuilder.start().add("inSet", 1).get(); //compound index SILDIM bu indexi
//		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("city", 1).get(); //compound index
		collTimezones.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("gmtoffset", 1).get(); //compound index
		collTimezones.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("tweetLocationAvailable", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("tweetTextLocationAvailable", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("gpsLocationAvailable", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("timezoneAvailable", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("eventType", 1).add("status", 1).add("tweet.createdAt", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("eventType", 1).add("status", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("profileLocationAvailable", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("classifiedAs", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("tweet.geo", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("situationid", 1).add("tweetid", 1).get(); //compound index
		collUserTrust.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("userName", 1).add("score", 1).get(); //compound index
		collUserTrust.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("userName", 1).get(); //compound index
		collUserTrust.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("score", 1).get(); //compound index
		collUserTrust.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("situationid", 1).get(); //compound index
		collUserTrust.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("situationid", 1).add("main", 1).add("score", 1).get(); //compound index
		collSituation.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("tweet.originalTweet", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("tweet.timezone", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("tweet.location", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("tweet.userName", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("main", 1).add("officialArrived", 1).get(); //compound index
		collSituation.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("GPSCountry", 1).get(); //compound index
		collEvent.createIndex(obj);	
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("GPSCity", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("GPSState", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("GPSTown", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("GPSStreet", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("GPSLat", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("GPSLong", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("ProfileCountry", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("ProfileCity", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("ProfileState", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("ProfileTown", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("ProfileStreet", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("ProfileLat", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("ProfileLong", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("timezoneCities", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("TweetTextCountry", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("TweetTextCity", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("TweetTextState", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("TweetTextTown", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("TweetTextStreet", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("TweetTextLat", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("TweetTextLong", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("TweetCountry", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("TweetCity", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("TweetCities", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("TweetState", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("TweetTown", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("TweetStreet", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("TweetLat", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("TweetLong", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("situationid", 1).add("main", 1).get(); //compound index
		collSituation.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("main", 1).add("score", 1).get(); //compound index
		collSituation.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("main", 1).add("notified", 1).get(); //compound index
		collSituation.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("main", 1).add("notified", 1).add("timestamp", 1).get(); //compound index
		collSituation.createIndex(obj);
		collMobileUser.createIndex(new BasicDBObject("_id",1));
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("GPSLat", 1).get(); //compound index
		collMobileUser.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("GPSLong", 1).get(); //compound index
		collMobileUser.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("_id", 1).add("deviceToken", 1).get(); //compound index
		collMobileUser.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("eventType", 1).add("inTest", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("inSet", 1).add("inTest", 1).get(); //compound index SILDIM
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("eventType", 1).add("inSet", 1).add("inTest", 1).get(); //compound index
		collEvent.createIndex(obj);
		obj = BasicDBObjectBuilder.start().add("eventType", 1).add("inTest", 1).add("inSet", 1).add("classifiedAs", 1).get(); //compound index
		collEvent.createIndex(obj);
		
		//daha yapmadim
		obj = BasicDBObjectBuilder.start().add("inSet", 1).add("inTest", 1).add("tweet.createdAt", 1).get(); //compound index SILDIM
		collEvent.createIndex(obj);
		
		//situatiodnda main situationid
		//situation main notified
		//
		//tweet waitin indexleri yazmadim, mobile user interestlerin yok
	}
	public static void connect() throws UnknownHostException
	{
		//		Console consol=System.console();

		//String s=consol.readLine("DBConnect IP:");
		m=new Mongo("10.0.0.5"); //configserver IPsi 
		db=m.getDB("twitterDB");

		collSituation=db.getCollection("situation");

		collEvent=db.getCollection("event");
		collAnalysis=db.getCollection("analysis");
		collAnalysisStartEndTime=db.getCollection("analysisStartEndTime");
		collAnalysisIntervalReadyRepords=db.getCollection("analysisInvervalReadyReports");
		collWordId=db.getCollection("wordid");
		collTimezones=db.getCollection("timezones");
		collTweetWaiting=db.getCollection("tweetWaiting");
		collRequestOSM=db.getCollection("requestOSM");
		collUserTrust=db.getCollection("userTrust");
		collMobileUser=db.getCollection("mobileUser");


		db.setReadPreference(ReadPreference.primaryPreferred());

	}
	public static DB getDB()
	{
		return db;
	}
	public static DBCollection getCollanalysisIntervalReadyReports()
	{
		return collAnalysisIntervalReadyRepords;
	}
	public static DBCollection getCollanalysisStartEndTime()
	{
		return collAnalysisStartEndTime;
	}
	public static DBCollection getCollEvent()
	{
		return collEvent;
	}

	public static DBCollection getCollAnalysis()
	{
		return collAnalysis;
	}
	public static void insertToExistingSituation(Integer valSituationId,ObjectId tweetid,String strTweetCountry, String strTweetCity,String strTweetState, String strTweetTown,
			String strTweetStreet,String strTweetLat, String strTweetLong, Integer valTimestamp,String strEventType)
	{
		//olay icin yeni id cekilecek sitatation id olacak

		BasicDBObject doc=new BasicDBObject();

		doc.put("tweetid", tweetid);
		doc.put("situationid", valSituationId.intValue());
		doc.put("TweetCountry", strTweetCountry);
		doc.put("TweetCity", strTweetCity);
		doc.put("TweetState", strTweetState);
		doc.put("TweetTown", strTweetTown);
		doc.put("TweetStreet", strTweetStreet);
		doc.put("TweetLat", strTweetLat);
		doc.put("TweetLong", strTweetLong);
		doc.put("timestamp", valTimestamp.intValue());
		doc.put("main", "0");
		doc.put("eventType", strEventType);
		getCollSituation().insert(doc);

	}
	public static void insertNewSituation(ObjectId tweetid,String strTweetCountry, String strTweetCity,String strTweetState, String strTweetTown,
			String strTweetStreet,String strTweetLat, String strTweetLong, Integer valTimestamp,String strEventType)
	{
		//olay icin yeni id cekilecek sitatation id olacak
		DBObject querySituationid=null;
		BasicDBObject doc=new BasicDBObject();
		Integer valSitId=0;

		
		DBObject o;
		DBCursor cur = getCollSituation().find().sort(new BasicDBObject("situationid", -1)).limit(1);

		if (cur.hasNext())
		{
			o=cur.next();
			valSitId=(Integer)o.get("situationid");
			valSitId++;
		}
		else
		{ 
			valSitId=0;
		}

		doc.put("tweetid", tweetid);
		doc.put("situationid", valSitId.intValue());
		doc.put("TweetCountry", strTweetCountry);
		doc.put("TweetCity", strTweetCity);
		doc.put("TweetState", strTweetState);
		doc.put("TweetTown", strTweetTown);
		doc.put("TweetStreet", strTweetStreet);
		doc.put("TweetLat", strTweetLat);
		doc.put("TweetLong", strTweetLong);
		doc.put("timestamp", valTimestamp.intValue());
		doc.put("main", "1");
		doc.put("eventType",strEventType);
		getCollSituation().insert(doc);
		
		if(((String)DBConnect.fetchTweetFieldFromDBGivenID("classifiedAs",tweetid)).equals("official positive"))
		{
			DBObject query1 = new BasicDBObject();
			query1 = QueryBuilder.start().and(
					QueryBuilder.start("situationid").is(valSitId).get(),
					QueryBuilder.start("main").is("1").get()
					).get();
			
			//shard icin gerekli shardKey
			DBObject dbObj =  DBConnect.getCollSituation().findOne(query1);
			query1 = QueryBuilder.start().and(
					QueryBuilder.start("_id").is((ObjectId)dbObj.get("_id")).get(),
					QueryBuilder.start("situationid").is(valSitId).get(),
					QueryBuilder.start("main").is("1").get()
					).get();
			
			DBObject newObject =  DBConnect.getCollSituation().find(query1).toArray().get(0);
			newObject.put("officialArrived","1");
			DBConnect.getCollSituation().findAndModify(query1, newObject);
		}
		

	}
	public static void insertToUserTrust(ObjectId tweetid, String strUserName,Integer valSitid,Integer valScore)
	{
		//olay icin yeni id cekilecek sitatation id olacak
		BasicDBObject doc=new BasicDBObject();
		doc.put("userName", strUserName);
		doc.put("tweetid",tweetid);
		doc.put("situationid", valSitid.intValue());
		doc.put("score",valScore.intValue());
		getCollUserTrust().insert(doc);
	}
	public static void insertToRequestOSM(Boolean boolGPSToAddress,Integer valTimeStamp,ObjectId tweetid, String strFnName,String strTweetCountry,String strTweetCity,String strTweetTown,String strTweetState,String strTweetStreet,String strTweetLat,String strTweetLong,String strStatus)
	{
		//olay icin yeni id cekilecek sitatation id olacak

		BasicDBObject doc=new BasicDBObject();
		doc.put("toaddress", (boolGPSToAddress)?("1"):("0"));
		doc.put("timestamp",valTimeStamp.intValue());
		doc.put("tweetid", tweetid);
		doc.put("functionName",strFnName);
		doc.put("country",strTweetCountry);
		doc.put("city",strTweetCity);
		doc.put("town",strTweetTown);
		doc.put("state",strTweetState);
		doc.put("street",strTweetStreet);
		doc.put("lat",strTweetLat);
		doc.put("long",strTweetLong);
		doc.put("status", strStatus);
		getCollRequestOSM().insert(doc);

	}
	public static void insertToTweetWaiting(ObjectId tweetid,String strTweetText, Integer valTimestamp, String strFunctionName,String strTweetCountry,String strTweetCity,String strTweetTown,String strTweetState,String strTweetStreet,String strTweetLat,String strTweetLong
			,String strStatus,Boolean boolRequest)
	{
		//olay icin yeni id cekilecek sitatation id olacak
		BasicDBObject doc=new BasicDBObject();
		doc.put("tweetid", tweetid);
		doc.put("tweetText", strTweetText);
		doc.put("timestamp", valTimestamp.intValue());
		doc.put("functionName",strFunctionName);
		doc.put("status", strStatus);
		doc.put("country", strTweetCountry);
		doc.put("city", strTweetCity);
		doc.put("town", strTweetTown);
		doc.put("state", strTweetState);
		doc.put("street", strTweetStreet);
		doc.put("lat", strTweetLat);
		doc.put("long", strTweetLong);
		doc.put("request",boolRequest?"1":"0");
		getCollTweetWaiting().insert(doc);	
	}

	//belirlenen tweete yeni filed ekler ornek cikartilan lokasyon bilgisi gibi
	public static void insertFieldToTweetGivenID(String key,String val,ObjectId tweetid)
	{
		BasicDBObject query = new BasicDBObject();
		query.put("_id", tweetid);
		DBObject newObject =  DBConnect.getCollEvent().find(query).toArray().get(0);
		newObject.put(key,val);
		DBConnect.getCollEvent().findAndModify(query, newObject);
		newObject=null;
		query=null;
		//System.gc();
	}
	public static Object fetchFieldFromRequestOSMGivenID(String fieldname,ObjectId tweetid)
	{
		long start = System.nanoTime();

		BasicDBObject fields=new BasicDBObject();
		fields.put(fieldname, 1);
		Object fetched=null;
		DBObject query=null;
		query = QueryBuilder.start().and(
				QueryBuilder.start("tweetid").is(tweetid).get()
				).get();

		DBObject tweetobj = DBConnect.getCollRequestOSM().findOne(query, fields);

		fetched= tweetobj.get(fieldname);
		System.out.println("OSRequestfetched:"+fetched);
		System.out.println("OSMfetched:"+fieldname);
		if(fetched!=fetched)
		{
			fetched="";
			System.out.println("fetchedNULL");
		}
		tweetobj=null;
		query=null;
	//	System.gc();
		long end = System.nanoTime();
		long nanoseconds = (end - start);
		System.out.println("fetchOSMField took msec:"+((double)nanoseconds / 1000000.0));
		return fetched;

	}
	public static Object fetchFieldFromDBGivenID(String fieldname,ObjectId tweetid)
	{
		long start = System.nanoTime();

		Object fetched=null;
		DBObject query=null;
		BasicDBObject fields=new BasicDBObject();
		fields.put(fieldname, 1);
		query = QueryBuilder.start().and(
				QueryBuilder.start("_id").is(tweetid).get()
				).get();
		 
		DBObject tweetobj = DBConnect.getCollEvent().findOne(query,fields);
		fetched= tweetobj.get(fieldname);
		System.out.println("DBGivenIDfetched:"+fetched);
		System.out.println("DBfetched:"+fieldname);

		tweetobj=null;
		query=null;
		if(fetched!=fetched)
		{
			fetched="";
			System.out.println("fetchedNULL");
		}
		//System.gc();
		long end = System.nanoTime();
		long nanoseconds = (end - start);
		System.out.println("fetchDBField took msec:"+((double)nanoseconds / 1000000.0));
		return fetched;

	}

	public static String fetchValueFromWordIDGivenID(Integer value)
	{
		long start = System.nanoTime();

		String fetched=null;
		DBObject query=null;
		
		query = QueryBuilder.start().and(
				QueryBuilder.start("value").is(value).get()
				).get();
		 
		DBObject tweetobj = DBConnect.getCollWordId().findOne(query);
		fetched= (String)tweetobj.get("wordid");
		System.out.println("DBGivenIDfetched:"+fetched);
		System.out.println("DBfetched:"+"wordid");

		tweetobj=null;
		query=null;
		if(fetched!=fetched)
		{
			fetched="";
			System.out.println("fetchedNULL");
		}
		//System.gc();
		long end = System.nanoTime();
		long nanoseconds = (end - start);
		System.out.println("fetchDBField took msec:"+((double)nanoseconds / 1000000.0));
		return fetched;

	}
	//dbden istedigin tweet fielda ait bilgiyi ceker dondurur
	public static Object fetchTweetFieldFromDBGivenID(String fieldname,ObjectId tweetid)
	{
		long start = System.nanoTime();
		BasicDBList tweetlist;
		Object fetched=null;
		DBObject query=null;
		BasicDBObject fields=new BasicDBObject();
		fields.put("tweet."+fieldname, 1);
		System.out.println("Tweetfetched:tweet."+fieldname);

		query = QueryBuilder.start().and(
				QueryBuilder.start("_id").is(tweetid).get()
				).get();

		DBObject tweetobj = DBConnect.getCollEvent().findOne(query,fields);

		tweetlist=(BasicDBList)tweetobj.get("tweet");
		System.out.println("TweetFieldfetched:"+tweetobj.toString());

		if(!tweetlist.equals(null))
		{

			fetched=((BasicDBObject)tweetlist.get(0)).get(fieldname);

		}

		tweetlist.clear();
		tweetlist=null;
		query=null;
		tweetobj=null;
		if(fetched!=fetched)
		{
			fetched="";
			System.out.println("fetchedNULL:"+fetched);
		}
		//System.gc();
		long end = System.nanoTime();
		long nanoseconds = (end - start);
		System.out.println("fetchTweetField took msec:"+((double)nanoseconds / 1000000.0));
		return fetched;

	}


}
