import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import org.bson.types.ObjectId;

import cmu.arktweetnlp.Tagger.TaggedToken;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

//statusu 1 olanlari filea yaz
//26 marttan dahil onceki statusu 1 olanlari cektim

public class TweetToFile
{
	public TweetToFile() throws IOException
	{
		try 
		{
			DBConnect.connect();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		DBObject query=null;


		query = QueryBuilder.start().and(
				QueryBuilder.start("status").is(1).get()

				).get();


		int tweetBuffer=30;
		DBCursor cursor = null;
		int size=0;

		size= DBConnect.getCollEvent().find(query).count();
		System.out.println(size);
		int skip;
		DBObject o;
		BasicDBList tweet1;
		ObjectId eventID;
		DBConnect.queryCount=size;

		FileWriter outFile = new FileWriter(new File("tweets.txt"));
		PrintWriter out = new PrintWriter(outFile);
		//hepsini cekmiyoruz kademe kademe
		for(int j=0;j<Math.ceil(size/tweetBuffer)+1;j++)
		{
			skip=tweetBuffer*j;

			cursor=null;
			System.gc();


			cursor = DBConnect.getCollEvent().find(query).limit(tweetBuffer).skip(skip);



			while(cursor.hasNext())
			{
				TweetStat temp=new TweetStat();
				o=cursor.next();
				tweet1=(BasicDBList)o.get("tweet");
				eventID=(ObjectId)o.get("_id");
				if(!tweet1.equals(null))
				{

					//bunu git dosyaya yaz

					//
					System.out.println(((BasicDBObject)tweet1.get(0)).get("originalTweet").toString());

					out.println(((BasicDBObject)tweet1.get(0)).get("originalTweet").toString());
					out.println();



					tweet1.clear();
					tweet1=null;
					o=null;

					eventID=null;


				}
			}


		}	
		out.close();
		cursor.close();



	}
}
