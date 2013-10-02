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
import org.apache.lucene.util.Version;
import org.bson.types.ObjectId;

import cmu.arktweetnlp.Tagger;
import cmu.arktweetnlp.Tagger.TaggedToken;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


public class UpdateDB
{

	private static BasicDBObject tempObj,query,data,command;
	private static Tagger tagger;
	private static String modelFilename;
	private static List<TaggedToken> taggedTokens;
	private static TweetStat temp;
	private static BasicDBList tweet;
	private static StandardAnalyzer standardAnalyzer;
	private static BasicDBObject newDocument;

	public static String removeStopWordsAndStem(String input) throws IOException {

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



	public static void startUpdate()
	{
		try {
			DBConnect.connect();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int tweetBuffer=30;
		DBCursor cursor;
		int size=0;
		tagger = new Tagger();
		modelFilename = "/cmu/arktweetnlp/model.20120919";
		try {
			tagger.loadModel(modelFilename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp = new TweetStat();
		tweet = new BasicDBList();

		tempObj = new BasicDBObject();
		size= DBConnect.getCollEvent().find().count();
		System.out.println("direct index:"+size);


		int skip;
		DBObject o;
		BasicDBList tweet1;
		ObjectId eventID;
		int t=0;

		//hepsini cekmiyoruz kademe kademe
		for(int j=0;j<Math.ceil(size/tweetBuffer)+1;j++)
		{
			skip=tweetBuffer*j;

			cursor=null;
			System.gc();
			cursor = DBConnect.getCollEvent().find().sort(new BasicDBObject("tweet.createdAt", 1)).limit(tweetBuffer).skip(skip);



			while(cursor.hasNext())
			{

				t++;
				if(t%1000==0)
					System.out.println(t);
				o=cursor.next();
				tweet1=(BasicDBList)o.get("tweet");
				eventID=(ObjectId)o.get("_id");
				if(!tweet1.equals(null))
				{
					//TweetStat olustur ve onu buffer'a ekle
					taggedTokens = tagger.tokenizeAndTag(((BasicDBObject)tweet1.get(0)).get("originalTweet").toString());

					temp.SetOriginalTweet(((BasicDBObject)tweet1.get(0)).get("originalTweet").toString());
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



					try {
						String s = removeStopWordsAndStem(temp.GetCombinedTokens());

						// gelen stringi bosluklara ayir
						String[] words = s.split(" ");

						//						System.out.println(eventID);
						//						
						query = new BasicDBObject();
						query.put("_id", eventID);


						data = new BasicDBObject();
						data.put("tweet.0.tweetLengthSemiFiltered", words.length);

						command = new BasicDBObject();
						command.put("$set", data);

						DBConnect.getCollEvent().update(query, command);



						s="";
						s=removeStopWordsAndStem(temp.GetOriginalTweet());
						//						tempObj.put("filteredTweet",s);
						//						System.out.println("filtered:"+s);

						data = new BasicDBObject();
						data.put("tweet.0.filteredTweet", s);

						command = new BasicDBObject();
						command.put("$set", data);

						DBConnect.getCollEvent().update(query, command);


						words = s.split(" ");
						//						tempObj.put("tweetLengthFiltered", words.length);
						//						System.out.println("filtered length:"+words.length);


						data = new BasicDBObject();
						data.put("tweet.0.tweetLengthFiltered",words.length);

						command = new BasicDBObject();
						command.put("$set", data);

						DBConnect.getCollEvent().update(query, command);






					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


					taggedTokens.clear();
					taggedTokens=null;


				}

				temp.clear();
				tweet1.clear();
				tweet1=null;
				o=null;

				eventID=null;
				System.gc();
			}




		}
	}
}
