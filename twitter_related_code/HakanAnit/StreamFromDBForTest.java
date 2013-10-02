//bu sadece DB den okuyup verilen buffera yazsin. baska birsey yapmasin query degisebilir 1) query interval aralik querysi yapacak 2) hepsnini cekecek
//daha sonra indexer verilen bufferdan okur vs onu yapmak lazim


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.bson.BSONObject;
import org.bson.types.ObjectId;

import twitter4j.*;

import cmu.arktweetnlp.Tagger;
import cmu.arktweetnlp.Tagger.TaggedToken;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.QueryBuilder;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

public class StreamFromDBForTest implements Runnable
{
	private String strEventType; //kisa kisa earthquake, fire, tsunami etc.

	private Integer bufferLimit=100; //100 den fazla obje varsa streami islemicek buffer tekrar 0 laninca devam eder

	private DBConnect dbConnect;
	private BasicDBObject doc,hashtags,urls,mentions,punctuations,emoticons,tempObj;
	private BasicDBList tweet;
	private Classification classification;
	public StreamFromDBForTest(String strEventType,Integer valClassifier) throws IOException
	{

		try {
			DBConnect.connect();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.strEventType=strEventType;

		classification=new Classification(strEventType,valClassifier);
		classification.prepareTraining(true);
//		new Thread(new Trust()).start();

	}
	@Override
	public void run() 
	{



		DBObject query1,query2,query3;

		query1= QueryBuilder.start().and(
				QueryBuilder.start("eventType").is(strEventType).get(),
				QueryBuilder.start("inSet").is("userPosSet").get(),
				QueryBuilder.start("inTest").is(1).get()
				).get();
		query2= QueryBuilder.start().and(
				QueryBuilder.start("eventType").is(strEventType).get(),
				QueryBuilder.start("inSet").is("officialPosSet").get(),
				QueryBuilder.start("inTest").is(1).get()
				).get();
		query3= QueryBuilder.start().and(
				QueryBuilder.start("inSet").is("negSet").get(),
				QueryBuilder.start("inTest").is(1).get()
				).get();
		for(int k=0;k<3;k++)
		{
			int tweetBuffer=30;
			DBCursor cursor;
			int size=0;

			if(k==0)
				size= DBConnect.getCollEvent().find(query1).count();
			else if(k==1)
				size= DBConnect.getCollEvent().find(query2).count();
			else if(k==2)
				size= DBConnect.getCollEvent().find(query3).count();


			System.out.println("query:"+size);

			int skip;
			DBObject o;
			BasicDBList tweet1;
			ObjectId eventid;
			DBConnect.queryCount=size;


			//hepsini cekmiyoruz kademe kademe
			for(int j=0;j<Math.ceil(size/tweetBuffer)+1;j++)
			{
				skip=tweetBuffer*j;

				cursor=null;
				//		System.gc();
//				cursor = DBConnect.getCollEvent().find(query1).sort(new BasicDBObject("tweet.createdAt", 1)).limit(tweetBuffer).skip(skip);

				if(k==0)
					cursor = DBConnect.getCollEvent().find(query1).limit(tweetBuffer).skip(skip);
				else if(k==1)
					cursor = DBConnect.getCollEvent().find(query2).limit(tweetBuffer).skip(skip);
				else if(k==2)
					cursor = DBConnect.getCollEvent().find(query3).limit(tweetBuffer).skip(skip);
				
				try 
				{
					while(cursor.hasNext())
					{

						o=cursor.next();
						tweet1=(BasicDBList)o.get("tweet");
						eventid=(ObjectId)o.get("_id");
						if(!tweet1.equals(null))
						{

							Integer i=Integer.parseInt(((BasicDBObject)tweet1.get(0)).get("createdAt").toString());

							//TweetStat olustur ve onu buffer'a ekle
							String strOriginalTweetText=((BasicDBObject)tweet1.get(0)).get("originalTweet").toString();
							String classifiedAs="";
							
							switch (classification.getValClassifier()) {
							case 0:
								classifiedAs=classification.classifyTweetBySVM(strOriginalTweetText);

								break;

							case 1:
								classifiedAs=classification.classifyTweetByNaiveBayes(strOriginalTweetText);

								break;
							case 2:
								classifiedAs=classification.classifyTweetBySelfOpSVM(strOriginalTweetText);

								break;
							}
						
							BasicDBObject queryT = new BasicDBObject();
							queryT.put("_id", eventid);

							DBObject newObject =  DBConnect.getCollEvent().find(queryT).toArray().get(0);
							newObject.put("classifiedAs", classifiedAs);
							DBConnect.getCollEvent().findAndModify(queryT, newObject);

							if(classifiedAs.equals("user positive")||classifiedAs.equals("official positive"))
							{
								//								System.out.println("neymis id"+id.toString());

								//tweetwaiting adli tabloya tweetid createdat ilk algonun adi ve status 0 olarak atilacak
								DBConnect.insertToTweetWaiting(eventid,strOriginalTweetText,i, "prepareAdditionalLocationInfoFromTweet", "","","","","","","","0",false);								
							}

							tweet1.clear();
							tweet1=null;
							o=null;

							
							classification.getTextAnalytics().clear();

							eventid=null;
						}

					}



				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				finally 
				{

					cursor.close();

				}

			}

		}



		System.out.println("read db completed");



	}


}
