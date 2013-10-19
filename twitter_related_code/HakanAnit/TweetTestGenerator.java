import java.io.IOException;
import java.net.UnknownHostException;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

//user pos, neg, official pos olarak ayiklanmis mesajlari inTest e atar ynai test set olusturur.

public class TweetTestGenerator
{
	private String strEventType;

	public TweetTestGenerator(String strEventType) throws UnknownHostException
	{
		DBConnect.connect();
		this.strEventType=strEventType;
		startGeneration();
	}
	
	
	private void startGeneration()
	{
		//eventtypi verilen olup userposta olan ve inTest exist olmayan in sayisi
		int valUserPos,valOfficialPos,valNeg,valUserPosTest,valOfficialPosTest,valNegTest;

		DBObject query = QueryBuilder.start().and(
				QueryBuilder.start("eventType").is(strEventType).get(),
				QueryBuilder.start("inSet").is("userPosSet").get(),
				QueryBuilder.start("inTest").exists(false).get()
				).get();
		valUserPos= DBConnect.getCollEvent().find(query).count();

		query = QueryBuilder.start().and(
				QueryBuilder.start("eventType").is(strEventType).get(),
				QueryBuilder.start("inSet").is("officialPosSet").get(),
				QueryBuilder.start("inTest").exists(false).get()
				).get();
		valOfficialPos= DBConnect.getCollEvent().find(query).count();

		query = QueryBuilder.start().and(
				QueryBuilder.start("inSet").is("negSet").get(),
				QueryBuilder.start("inTest").exists(false).get()
				).get();
		valNeg= DBConnect.getCollEvent().find(query).count();

		//eventtypi verilen olup user pos ta olan ve inTest is 1 olanin sayisi
		query = QueryBuilder.start().and(
				QueryBuilder.start("eventType").is(strEventType).get(),
				QueryBuilder.start("inSet").is("userPosSet").get(),
				QueryBuilder.start("inTest").is(1).get()
				).get();
		valUserPosTest= DBConnect.getCollEvent().find(query).count();

		query = QueryBuilder.start().and(
				QueryBuilder.start("eventType").is(strEventType).get(),
				QueryBuilder.start("inSet").is("officialPosSet").get(),
				QueryBuilder.start("inTest").is(1).get()
				).get();
		valOfficialPosTest= DBConnect.getCollEvent().find(query).count();

		query = QueryBuilder.start().and(
				QueryBuilder.start("inSet").is("negSet").get(),
				QueryBuilder.start("inTest").is(1).get()
				).get();
		valNegTest= DBConnect.getCollEvent().find(query).count();
		//test ve train in sayisi toplamin yarisi kadar olsun
		//yani if testset sayisi esitse sadece userpos/2 okey
		//onlari update et

		
		if((valUserPos+valUserPosTest)/2!=valUserPosTest&&valUserPosTest<valUserPos)
		{
			//ama degilse userpos/2-testset kadar fetch et

			query = QueryBuilder.start().and(
					QueryBuilder.start("eventType").is(strEventType).get(),
					QueryBuilder.start("inSet").is("userPosSet").get(),
					QueryBuilder.start("inTest").exists(false).get()
					).get();

			int tweetBuffer=30;
			DBCursor cursor;
			int size=0;
			int valCnt=0;
			size= DBConnect.getCollEvent().find(query).count();

			System.out.println("query:"+size);

			int skip;
			DBObject o;
			BasicDBList tweet1;
			ObjectId eventid;
			DBConnect.queryCount=size;
			Boolean boolEnd=false;
			//hepsini cekmiyoruz kademe kademe
			for(int j=0;j<Math.ceil(size/tweetBuffer)+1;j++)
			{
				if(boolEnd)
					break;
				skip=tweetBuffer*j;
				cursor=null;
				cursor = DBConnect.getCollEvent().find(query).sort(new BasicDBObject("tweet.createdAt", 1)).limit(tweetBuffer).skip(skip);
				try 
				{
					while(cursor.hasNext())
					{

						if(valCnt==(valUserPos-valUserPosTest)/2)
						{
							boolEnd=true;
							break;
						}
						valCnt++;
						o=cursor.next();
						tweet1=(BasicDBList)o.get("tweet");
						eventid=(ObjectId)o.get("_id");
						if(!tweet1.equals(null))
						{

							BasicDBObject query1 = new BasicDBObject();
							query1.put("_id", eventid);

							DBObject newObject =  DBConnect.getCollEvent().find(query1).toArray().get(0);
							newObject.put("inTest", 1);
							DBConnect.getCollEvent().findAndModify(query1, newObject);

							tweet1.clear();
							tweet1=null;
							o=null;

							eventid=null;
						}

					}

				} 
				finally 
				{
					cursor.close();
				}

			}

		}
		
		//official Pos
		
		if((valOfficialPos+valOfficialPosTest)/2!=valOfficialPosTest&&valOfficialPosTest<valOfficialPos)
		{
			//ama degilse userpos/2-testset kadar fetch et

			query = QueryBuilder.start().and(
					QueryBuilder.start("eventType").is(strEventType).get(),
					QueryBuilder.start("inSet").is("officialPosSet").get(),
					QueryBuilder.start("inTest").exists(false).get()
					).get();

			int tweetBuffer=30;
			DBCursor cursor;
			int size=0;
			int valCnt=0;
			size= DBConnect.getCollEvent().find(query).count();

			System.out.println("query:"+size);

			int skip;
			DBObject o;
			BasicDBList tweet1;
			ObjectId eventid;
			DBConnect.queryCount=size;
			Boolean boolEnd=false;
			//hepsini cekmiyoruz kademe kademe
			for(int j=0;j<Math.ceil(size/tweetBuffer)+1;j++)
			{
				if(boolEnd)
					break;
				skip=tweetBuffer*j;
				cursor=null;
				cursor = DBConnect.getCollEvent().find(query).sort(new BasicDBObject("tweet.createdAt", 1)).limit(tweetBuffer).skip(skip);
				try 
				{
					while(cursor.hasNext())
					{

						if(valCnt==(valOfficialPos-valOfficialPosTest)/2)
						{
							boolEnd=true;
							break;
						}
						valCnt++;
						o=cursor.next();
						tweet1=(BasicDBList)o.get("tweet");
						eventid=(ObjectId)o.get("_id");
						if(!tweet1.equals(null))
						{

							BasicDBObject query1 = new BasicDBObject();
							query1.put("_id", eventid);

							DBObject newObject =  DBConnect.getCollEvent().find(query1).toArray().get(0);
							newObject.put("inTest", 1);
							DBConnect.getCollEvent().findAndModify(query1, newObject);

							tweet1.clear();
							tweet1=null;
							o=null;

							eventid=null;
						}

					}

				} 
				finally 
				{
					cursor.close();
				}

			}

		}
		
		//neg
//		System.out.println(valNeg+valNegTest);
//		System.out.println(valNeg);
//		System.out.println(valNegTest);

		
		if(((valNeg+valNegTest)/2!=valNegTest)&&(valNegTest<valNeg))
		{
			//ama degilse userpos/2-testset kadar fetch et

			query = QueryBuilder.start().and(
					QueryBuilder.start("inSet").is("negSet").get(),
					QueryBuilder.start("inTest").exists(false).get()
					).get();

			int tweetBuffer=30;
			DBCursor cursor;
			int size=0;
			int valCnt=0;
			size= DBConnect.getCollEvent().find(query).count();

			System.out.println("query:"+size);

			int skip;
			DBObject o;
			BasicDBList tweet1;
			ObjectId eventid;
			DBConnect.queryCount=size;
			Boolean boolEnd=false;
			//hepsini cekmiyoruz kademe kademe
			for(int j=0;j<Math.ceil(size/tweetBuffer)+1;j++)
			{
				if(boolEnd)
					break;
				skip=tweetBuffer*j;
				cursor=null;
				cursor = DBConnect.getCollEvent().find(query).sort(new BasicDBObject("tweet.createdAt", 1)).limit(tweetBuffer).skip(skip);
				try 
				{
					while(cursor.hasNext())
					{

						if(valCnt==(valNeg-valNegTest)/2)
						{
							boolEnd=true;
							break;
						}
						valCnt++;
						o=cursor.next();
						tweet1=(BasicDBList)o.get("tweet");
						eventid=(ObjectId)o.get("_id");
						if(!tweet1.equals(null))
						{

							BasicDBObject query1 = new BasicDBObject();
							query1.put("_id", eventid);

							DBObject newObject =  DBConnect.getCollEvent().find(query1).toArray().get(0);
							newObject.put("inTest", 1);
							DBConnect.getCollEvent().findAndModify(query1, newObject);

							tweet1.clear();
							tweet1=null;
							o=null;

							eventid=null;
						}

					}

				} 
				finally 
				{
					cursor.close();
				}

			}

		}


	}
}
