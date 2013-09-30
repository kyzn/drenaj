//bu sadece DB den okuyup verilen buffera yazsin. baska birsey yapmasin query degisebilir 1) query interval aralik querysi yapacak 2) hepsnini cekecek
//daha sonra indexer verilen bufferdan okur vs onu yapmak lazim


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
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

public class ReadGPSFromEventWriteToFile implements Runnable
{
	private String eventType,official; //kisa kisa earthquake, fire, tsunami etc.
	private ArrayList<TweetStat> tweetsBuffer;
	private Integer bufferLimit=100; //100 den fazla obje varsa streami islemicek buffer tekrar 0 laninca devam eder
	private Tagger tagger;
	private String modelFilename,analysisType,tweetCreatedAt1,tweetCreatedAt2; //tweetcreatedlar intervaller icin
	private List<TaggedToken> taggedTokens;
	//	private TweetStat temp;
	private DBConnect dbConnect;
	private BasicDBObject doc,hashtags,urls,mentions,punctuations,emoticons,tempObj;
	private BasicDBList tweet;
	private String[] filterWords;
	private Boolean boolQueryAll,boolQueryInterval,boolReadCompleted; //hangi query kullanilsin
	private StandardAnalyzer standardAnalyzer;
	private Long annotationTime;

	public ReadGPSFromEventWriteToFile(String eventType,String tweetCreatedAt1,String tweetCreatedAt2)
	{


		this.eventType=eventType;
		this.tweetCreatedAt1=tweetCreatedAt1;
		this.tweetCreatedAt2=tweetCreatedAt2;
		run();


	}
	@Override
	public void run() 
	{
		//iste burdan surekli dbden okumasi gerekli



		//eger hepsini okuyorsan. ilk tweetin zamanini al. endtime olarak ona logTime ekle ve sorguyu calistir
		try {
			DBConnect.connect();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		DBObject queryInterval=null,queryAllBasedOnEventType=null,queryAnnotation=null;

		queryInterval = QueryBuilder.start().and(
				QueryBuilder.start("eventType").is(eventType).get(),
				QueryBuilder.start("tweet.isGeoAttached").is("1").get(),
				QueryBuilder.start("tweet.createdAt").greaterThanEquals(Integer.valueOf(tweetCreatedAt1).intValue()).get(),
				QueryBuilder.start("tweet.createdAt").lessThan(Integer.valueOf(tweetCreatedAt2).intValue()).get()

				).get();

		int tweetBuffer=30;
		DBCursor cursor;
		int size=0;


		size= DBConnect.getCollEvent().find(queryInterval).count();

		System.out.println("query interval:"+size);

		PrintWriter writer;
		try 
		{
			writer = new PrintWriter("toplanangeolar.txt", "UTF-8");


			int skip;
			DBObject o;
			BasicDBList tweet1;
			ObjectId eventID;
			DBConnect.queryCount=size;


			//hepsini cekmiyoruz kademe kademe
			for(int j=0;j<Math.ceil(size/tweetBuffer)+1;j++)
			{
				skip=tweetBuffer*j;

				cursor=null;


				cursor = DBConnect.getCollEvent().find(queryInterval).limit(tweetBuffer).skip(skip);

				try 
				{
					while(cursor.hasNext())
					{
						TweetStat temp=new TweetStat();
						o=cursor.next();
						tweet1=(BasicDBList)o.get("tweet");
						eventID=(ObjectId)o.get("_id");
						if(!tweet1.equals(null))
						{


							String strGeo= ((BasicDBObject)tweet1.get(0)).get("geo").toString();
							writer.print(strGeo+";");
							String strText= ((BasicDBObject)tweet1.get(0)).get("originalTweet").toString();
							writer.print(strText+";");
							String strTimestamp= ((BasicDBObject)tweet1.get(0)).get("tweet.createdAt").toString();
							writer.print(strText+";");


						}

						//	temp.clear();
						//temp=null;
						tweet1.clear();
						tweet1=null;
						o=null;

						eventID=null;
					}

				}




				finally 
				{

					cursor.close();

				}

			}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	

		System.out.println("read db completed");
		boolReadCompleted=true;




	}


}
