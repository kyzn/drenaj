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

public class ReadFromDB implements Runnable
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

	public String removeStopWordsAndStem(String input) throws IOException {

		List<String> stopWords = new ArrayList<String>();
		CharArraySet stopSet = new CharArraySet(Version.LUCENE_36,
				stopWords.size(), false);

		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream("./stopwords.txt");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				stopWords.add(strLine);
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		stopSet.addAll(stopWords);

		TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_36,
				new StringReader(input));

		standardAnalyzer = new StandardAnalyzer(Version.LUCENE_36, stopSet);
		// tokenStream =new StopFilter(Version.LUCENE_36,tokenStream, stopSet);
		tokenStream = standardAnalyzer.tokenStream("", new StringReader(input));

		OffsetAttribute offsetAttribute = tokenStream
				.addAttribute(OffsetAttribute.class);
		CharTermAttribute charTermAttribute = tokenStream
				.addAttribute(CharTermAttribute.class);
		StringBuilder sb = new StringBuilder();

		while (tokenStream.incrementToken()) {
			// int startOffset = offsetAttribute.startOffset();
			// int endOffset = offsetAttribute.endOffset();
			String term = charTermAttribute.toString();
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(term);
		}

		return sb.toString();
	}
	public Boolean GetBoolReadCompleted()
	{
		return boolReadCompleted;
	}
	public ReadFromDB(ArrayList<TweetStat> tweetsBuffer, String eventType,String analysisType,Boolean boolQueryAll,Boolean boolQueryInterval,
			String tweetCreatedAt1,String tweetCreatedAt2,Long annotationTime,String official)
	{

		this.official=official;
		this.annotationTime=annotationTime;
		this.eventType=eventType;
		this.tweetsBuffer=tweetsBuffer;
		this.analysisType=analysisType;
		this.tweetCreatedAt1=tweetCreatedAt1;
		this.tweetCreatedAt2=tweetCreatedAt2;
		this.boolQueryAll=boolQueryAll;
		this.boolQueryInterval=boolQueryInterval;
		boolReadCompleted=false;

		tagger = new Tagger();
		modelFilename = "/cmu/arktweetnlp/model.20120919";
		try {
			tagger.loadModel(modelFilename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//temp=new TweetStat();

	}
	@Override
	public void run() 
	{
		//iste burdan surekli dbden okumasi gerekli



		System.out.println(tweetCreatedAt1+" "+tweetCreatedAt2);
		//eger hepsini okuyorsan. ilk tweetin zamanini al. endtime olarak ona logTime ekle ve sorguyu calistir
		try {
			DBConnect.connect();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		DBObject queryInterval=null,queryAllBasedOnEventType=null,queryAnnotation=null;

		if(boolQueryInterval&&analysisType.equals(""))
		{
			queryInterval = QueryBuilder.start().and(
					QueryBuilder.start("eventType").is(eventType).get(),
					QueryBuilder.start("tweet.createdAt").greaterThanEquals(Integer.valueOf(tweetCreatedAt1).intValue()).get(),
					QueryBuilder.start("tweet.createdAt").lessThan(Integer.valueOf(tweetCreatedAt2).intValue()).get()

					).get();
		}
		else if(boolQueryAll)
		{
			queryAllBasedOnEventType = QueryBuilder.start().and(
					QueryBuilder.start("eventType").is(eventType).get()

					).get();
		}
		else
		{

			if(annotationTime==null)
			{
				if(official.equals("0"))
				{
					queryAnnotation = QueryBuilder.start().and(
							QueryBuilder.start("eventType").is(eventType).get(),
							QueryBuilder.start("annotated").is(1).get(),
							QueryBuilder.start("official").exists(false).get(),
							QueryBuilder.start("status").is(Integer.parseInt(analysisType)).get()
							).get();
				}
				else
				{
					queryAnnotation = QueryBuilder.start().and(
							QueryBuilder.start("eventType").is(eventType).get(),
							QueryBuilder.start("annotated").is(1).get(),
							QueryBuilder.start("official").is(1).get(),
							QueryBuilder.start("status").is(Integer.parseInt(analysisType)).get()
							).get();
				}
			
			}
			else if(official.equals("0"))
			{
				queryAnnotation = QueryBuilder.start().and(
						QueryBuilder.start("eventType").is(eventType).get(),
						QueryBuilder.start("annotated").is(1).get(),
						QueryBuilder.start("official").exists(false).get(),
						QueryBuilder.start("status").is(Integer.parseInt(analysisType)).get(),
						QueryBuilder.start("tweet.createdAt").greaterThanEquals(annotationTime-60).get(),
						QueryBuilder.start("tweet.createdAt").lessThan(annotationTime+3*60).get()
						).get();
			}
			else
			{
				queryAnnotation = QueryBuilder.start().and(
						QueryBuilder.start("eventType").is(eventType).get(),
						QueryBuilder.start("annotated").is(1).get(),
						QueryBuilder.start("official").is(1).get(),
						QueryBuilder.start("status").is(Integer.parseInt(analysisType)).get(),
						QueryBuilder.start("tweet.createdAt").greaterThanEquals(annotationTime-60).get(),
						QueryBuilder.start("tweet.createdAt").lessThan(annotationTime+3*60).get()
						).get();

			}


		}

		int tweetBuffer=30;
		DBCursor cursor;
		int size=0;


		if(boolQueryInterval&&analysisType.equals(""))
		{


			size= DBConnect.getCollEvent().find(queryInterval).count();

			System.out.println("query interval:"+size);



		}
		else if(boolQueryAll)
		{

			size= DBConnect.getCollEvent().find(queryAllBasedOnEventType).count();
			System.out.println("direct index all:"+size);


		}
		else if(analysisType.equals("2"))
		{

			size= DBConnect.getCollEvent().find(queryAnnotation).count();
			System.out.println(size);

		}
		else if(analysisType.equals("1"))
		{


			size= DBConnect.getCollEvent().find(queryAnnotation).count();
			System.out.println(size);


		}
		else if(analysisType.equals("-1"))
		{


			size= DBConnect.getCollEvent().find(queryAnnotation).count();
			System.out.println(size);



		}

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
			System.gc();

			if(boolQueryAll)
			{

				cursor = DBConnect.getCollEvent().find(queryAllBasedOnEventType).sort(new BasicDBObject("tweet.createdAt", 1)).limit(tweetBuffer).skip(skip);


			}
			else if(boolQueryInterval&&analysisType.equals(""))
			{

				cursor = DBConnect.getCollEvent().find(queryInterval).limit(tweetBuffer).skip(skip);

			}
			else
			{

				cursor = DBConnect.getCollEvent().find(queryAnnotation).limit(tweetBuffer).skip(skip);
			}

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

						if(tweetsBuffer.size()<bufferLimit)
						{
							Integer i=Integer.parseInt(((BasicDBObject)tweet1.get(0)).get("createdAt").toString());//+3*60*6/1000
							if(j==0)
								IndexAnalyzer.tweetTime=i.longValue();

							//TweetStat olustur ve onu buffer'a ekle
							taggedTokens = tagger.tokenizeAndTag(((BasicDBObject)tweet1.get(0)).get("originalTweet").toString());
							if(((BasicDBObject)tweet1.get(0)).get("isGeoAttached")!=null) //geoAttached idi adini degistirdim ondan eldekiler gitmesin
							{
								if(((BasicDBObject)tweet1.get(0)).get("isGeoAttached").toString().equals("0"))
								{
									temp.SetGpsAttached(false);
								}
								else
									temp.SetGpsAttached(true);
							}
							else
								temp.SetGpsAttached(false);


							temp.SetOriginalTweet(((BasicDBObject)tweet1.get(0)).get("originalTweet").toString());
							temp.SetTweetTime(i.intValue());
							temp.SetUserName(((BasicDBObject)tweet1.get(0)).get("userName").toString());
							temp.SetTweetLength(taggedTokens.size());
							if(((BasicDBObject)tweet1.get(0)).get("isRT").toString().equals("1"))
								temp.SetRT(true);
							else
								temp.SetRT(false);

							//user locationuda alabiliyorsun profili nereye kayitliysa
							//hash, punctuation, emoticon vs bunlar onemli olabilir. O yuzden TweetStatin icinde hepsi icin ayri Arrayler var.
							//Integer cnt=1;

							for (TaggedToken token : taggedTokens) 
							{

								if(token.tag.equals("#"))
								{
									temp.AddToken("Hashtag");
									temp.AddHashQueue(token.token);
								}
								else if(token.tag.equals("U"))
								{
									temp.AddToken("URL");
								}
								else if(token.tag.equals("E"))
								{
									temp.AddToken("Emoticon");
									temp.AddEmoticonQueue(token.token);

								}
								else if(token.tag.equals(","))
								{
									temp.AddToken("Punctuation");
									temp.AddPunctuationQueue(token.token);

								}
								else if(token.tag.equals("@"))
								{
									temp.AddToken("Mention");
								}
								else
									temp.AddToken(token.token);

								//cnt++;

							}

							if(((BasicDBObject)o).get("outGPS")!=null)
								temp.setOutGPS(((BasicDBObject)o).get("outGPS").toString());
							if(((BasicDBObject)o).get("outAbb")!=null)
								temp.setOutAbb(((BasicDBObject)o).get("outAbb").toString());
							if(((BasicDBObject)o).get("outAddress")!=null)
								temp.setOutAddress(((BasicDBObject)o).get("outAddress").toString());
							if(((BasicDBObject)o).get("outCity")!=null)
								temp.setOutCity(((BasicDBObject)o).get("outCity").toString());
							if(((BasicDBObject)o).get("outTown")!=null)
								temp.setOutTown(((BasicDBObject)o).get("outTown").toString());
							if(((BasicDBObject)o).get("outCountry")!=null)
								temp.setOutCountry(((BasicDBObject)o).get("outCountry").toString());
							if(((BasicDBObject)o).get("outSarcasticLocation")!=null)
								temp.setOutSarcasticLocation(((BasicDBObject)o).get("outSarcasticLocation").toString());
							if(((BasicDBObject)o).get("outFakeLocation")!=null)
								temp.setOutFakeLocation(((BasicDBObject)o).get("outFakeLocation").toString());
							if(((BasicDBObject)o).get("inGPS")!=null)
								temp.setInGPS(((BasicDBObject)o).get("inGPS").toString());
							if(((BasicDBObject)o).get("inAbb")!=null)
								temp.setInAbb(((BasicDBObject)o).get("inAbb").toString());
							if(((BasicDBObject)o).get("inAddress")!=null)
								temp.setInAddress(((BasicDBObject)o).get("inAddress").toString());
							if(((BasicDBObject)o).get("inCity")!=null)
								temp.setInCity(((BasicDBObject)o).get("inCity").toString());
							if(((BasicDBObject)o).get("inTown")!=null)
								temp.setInTown(((BasicDBObject)o).get("inTown").toString());
							if(((BasicDBObject)o).get("inCountry")!=null)
								temp.setInCountry(((BasicDBObject)o).get("inCountry").toString());
							if(((BasicDBObject)o).get("inSarcasticLocation")!=null)
								temp.setInSarcasticLocation(((BasicDBObject)o).get("inSarcasticLocation").toString());
							if(((BasicDBObject)o).get("inFakeLocation")!=null)
								temp.setInFakeLocation(((BasicDBObject)o).get("inFakeLocation").toString());
							if(((BasicDBObject)o).get("organization")!=null)
								temp.setOrganization(((BasicDBObject)o).get("organization").toString());
							if(((BasicDBObject)o).get("dueToAnotherEvent")!=null)
								temp.setDueToAnotherEvent(((BasicDBObject)o).get("dueToAnotherEvent").toString());
							if(((BasicDBObject)o).get("timeEndInfo")!=null)
								temp.setTimeEndInfo(((BasicDBObject)o).get("timeEndInfo").toString());
							if(((BasicDBObject)o).get("timeStartInfo")!=null)
								temp.setTimeStartInfo(((BasicDBObject)o).get("timeStartInfo").toString());


							try {
								String s = removeStopWordsAndStem(temp.GetCombinedTokens());

								// gelen stringi bosluklara ayir
								String[] words = s.split(" ");
								temp.AddStringToToken(words);
								temp.SetSemiFilteredTweetLength(words.length);

								s="";
								s=removeStopWordsAndStem(temp.GetOriginalTweet());
								words = s.split(" ");
								temp.SetFilteredTweet(s);

								temp.SetFilteredTweetLength(words.length);

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}



							taggedTokens.clear();
							taggedTokens=null;

							synchronized (tweetsBuffer)
							{
								tweetsBuffer.add(temp);
							}

						}

						//	temp.clear();
						//temp=null;
						tweet1.clear();
						tweet1=null;
						o=null;

						eventID=null;
						System.gc();
					}

				}



			} 
			finally 
			{

				cursor.close();

			}

		}


		System.out.println("read db completed");
		boolReadCompleted=true;




	}


}
