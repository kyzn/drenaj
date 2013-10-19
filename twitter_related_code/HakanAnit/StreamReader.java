//Bu Thread Twitterdan Stream okuyor hem buffera hem db ye yaziyor

import java.awt.PageAttributes.OriginType;
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
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;
import org.bson.types.ObjectId;
import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.json.JSONException;

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
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

public class StreamReader implements Runnable {

	private String eventType; // kisa kisa earthquake, fire, tsunami etc.
	private ArrayList<TweetStat> tweetsBuffer;
	private Integer averageTweetSize = 0, bufferLimit = 100; // 100 den fazla
	private Long totalNegTweets,totalUserPosTweets;
	private BasicDBObject doc, hashtags, urls, mentions, punctuations,
	emoticons, tempObj;
	private BasicDBList tweet;
	private String[] filterWords;
	private Boolean flagIndex;
	private TextAnalytics textAnalytics;
	private List<TaggedToken> taggedTokens;
	private Double valClassificationThreshold;
	private Classification classification;


	public StreamReader(ArrayList<TweetStat> tweetsBuffer, String eventType,
			List<String> filterWords, Boolean flagIndex,Classification classification,Double valClassificationThreshold) throws IOException {
		this.flagIndex = flagIndex;
		this.eventType = eventType;
		this.tweetsBuffer = tweetsBuffer;
		this.filterWords = filterWords.toArray(new String[filterWords.size()]);

		this.classification=classification;
		this.textAnalytics=classification.getTextAnalytics();
		classification.prepareTraining(true);

		//		new Thread(new Trust()).start();
		this.totalNegTweets=(long)0.0;
		this.totalUserPosTweets=(long)0.0;
		this.valClassificationThreshold=valClassificationThreshold;
	}

	@Override
	public void run()
	{
		//ayni zamanda bir key beklesin onu alinca durdursun islemleri


		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {

				TweetStat temp=new TweetStat();

				// taglemek icin streami
				// System.out.println("BUFFERSIZE: "+tweetsBuffer.size());
				if (tweetsBuffer.size() < bufferLimit)
				{
					tweet = new BasicDBList();
					doc = new BasicDBObject();
					hashtags = new BasicDBObject();
					urls = new BasicDBObject();
					mentions = new BasicDBObject();
					punctuations = new BasicDBObject();
					emoticons = new BasicDBObject();
					tempObj = new BasicDBObject();
					// TweetStat olustur ve onu buffer'a ekle
					taggedTokens = textAnalytics.tagTweetText(status.getText());

					if (status.getGeoLocation() == null) {
						temp.SetGpsAttached(false);
					} else
						temp.SetGpsAttached(true);

					temp.SetOriginalTweet(status.getText());

					Long lx = status.getCreatedAt().getTime() / 1000; // zaman
					// milisaniye
					// olarka
					// geliyor
					int t1 = lx.intValue();
					temp.SetTweetTime(t1);
					// System.out.println("geken:"+String.valueOf(t));
					// System.out.println("cevir:"+String.valueOf(t1));
					// System.out.println("cevir arti:"+String.valueOf(t2));

					temp.SetRT(status.isRetweet());
					temp.SetGeoLocation(status.getGeoLocation());
					temp.SetUserName(status.getUser().getScreenName());
					temp.SetTweetLength(taggedTokens.size());

					tempObj.put("location", status.getUser().getLocation());
					tempObj.put("followerCount", status.getUser()
							.getFollowersCount());
					tempObj.put("followingCount", status.getUser()
							.getFriendsCount());
					tempObj.put("timezone", status.getUser().getTimeZone());
					tempObj.put("userName", status.getUser().getName());

					Long tweetid = status.getId();
					tempObj.put("tweetID", tweetid.toString()); // tweet idi
					if (temp.IsGpsAttached())
						tempObj.put("geo", status.getGeoLocation().toString());
					tempObj.put("isRT", temp.GetIsRT());

					if (status.getSource().contains("iphone")
							|| status.getSource().contains("android")
							|| status.getSource().contains("ipad"))
						tempObj.put("isMobile", "1"); // mobile or web
					else
						tempObj.put("isMobile", "0"); // mobile or web
					tempObj.put("source", status.getSource()); // genede koyalim
					// mobil mi
					// degilmi
					// anlamak icin
					tempObj.put("createdAt", t1); // gmt +2 3 yapmisim onceden
					tempObj.put("tweetLength", taggedTokens.size());
					tempObj.put("isGeoAttached", temp.GetGpsAttached());
					tempObj.put("originalTweet", temp.GetOriginalTweet());

					// arindirilmis tweetti yani stopwordlerden burda string
					// cumle olarak dbye at. sonra indexerda onu parcalayip
					// indexlet
					// tweet.put("stemmedTweet",);

					// asagidakindan sonra bir string olustur onu tokenize et.
					// try {
					// System.out.println("1):"+temp.GetOriginalTweet());
					// System.out.println("2):"+removeStopWordsAndStem(temp.GetOriginalTweet()));
					// } catch (IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }

					// user locationuda alabiliyorsun profili nereye kayitliysa
					// hash, punctuation, emoticon vs bunlar onemli olabilir. O
					// yuzden TweetStatin icinde hepsi icin ayri Arrayler var.
					Integer cnt = 1;

					for (TaggedToken token : taggedTokens) {

						if (token.tag.equals("#")) {
							temp.AddToken("Hashtag");
							temp.AddHashQueue(token.token);
							hashtags.put(cnt.toString(), token.token);

						} else if (token.tag.equals("U")) {
							temp.AddToken("URL");

							urls.put(cnt.toString(), token.token);

						} else if (token.tag.equals("E")) {
							temp.AddToken("Emoticon");
							temp.AddEmoticonQueue(token.token);
							emoticons.put(cnt.toString(), token.token);

						} else if (token.tag.equals(",")) {
							temp.AddToken("Punctuation");
							temp.AddPunctuationQueue(token.token);
							punctuations.put(cnt.toString(), token.token);

						} else if (token.tag.equals("@")) {
							temp.AddToken("Mention");
							mentions.put(cnt.toString(), token.token);

						} else
							temp.AddToken(token.token);

						tempObj.put(cnt.toString(), token.token); // /su
						// lokasyondaki
						// veri su
						// fieldlari
						cnt++;
						// System.out.printf("%s(%s) ", token.token, token.tag);

					}

					try {
						String s = textAnalytics.removeStopWords(temp.GetCombinedTokens());

						// gelen stringi bosluklara ayir
						String[] words = s.split(" ");
						temp.AddStringToToken(words);
						tempObj.put("tweetLengthSemiFiltered", words.length);
						temp.SetSemiFilteredTweetLength(words.length);

						s="";
						s=textAnalytics.removeStopWords(temp.GetOriginalTweet());
						tempObj.put("filteredTweet",s);
						words = s.split(" ");
						tempObj.put("tweetLengthFiltered", words.length);
						temp.SetFilteredTweet(s);

						temp.SetFilteredTweetLength(words.length);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					tempObj.put("hashtags", hashtags); // key lokasyon value
					// hashtag
					tempObj.put("emoticons", emoticons);
					tempObj.put("mentions", mentions);
					tempObj.put("urls", urls);
					tempObj.put("punctuations", punctuations);

					doc.put("annotated", 0);

					doc.put("eventType", eventType);
					if (flagIndex) // streamden okuyorsan eger anabilgisayarda
						// tekrar indexlenmesin zaman kazanalim diye
						doc.put("indexed", 1);
					else
						doc.put("indexed", -1);


					synchronized (tweetsBuffer)
					{

						// dbConnect.getCollEvent().insert(doc,WriteConcern.SAFE);
						// //yazana kadar beklesin
						// http://whyjava.wordpress.com/2011/12/08/how-mongodb-different-write-concern-values-affect-performance-on-a-single-node/
						// https://groups.google.com/forum/?fromgroups=#!topic/mongodb-user/znJjUPmag08

						//dbye tweetten cikarilan lokasyonu vs de koymak istiorm sonra
						//tweeti burda islettirelim lokasyon cikartsin vs neyse tweet le ilgili. ama georeverse code yapamiabilir yada
						//lokasyonu cevirme 

						//streamden sadece okuma amacli calismasi lazim

						try
						{
							//classify et
							//commentilerin hepsi var aslinda

							String classifiedAs="";
							switch (classification.getValClassifier()) {
							case 0:
								classifiedAs=classification.classifyTweetBySVM(temp.GetOriginalTweet());

								break;

							case 1:
								classifiedAs=classification.classifyTweetByNaiveBayes(temp.GetOriginalTweet());							

								break;
							case 2:
								classifiedAs=classification.classifyTweetBySelfOpSVM(temp.GetOriginalTweet());							

								break;
							}

							temp.setClassifiedAs(classifiedAs);
							tempObj.put("classifiedAs", classifiedAs);
							tweet.add(tempObj); // bide bunu ekledim
							doc.put("tweet", tweet);

							if(classification.getinstanceClassValuePositive()>=valClassificationThreshold) //sadece positiveleri ekle dbye artik
							{
								DBConnect.getCollEvent().insert(doc);


								ObjectId id = (ObjectId)doc.get( "_id" ); //son eklediginin id sini aliorm boylece sonra o tweetle ugras ne yaparsan yap
								temp.setDbTweetID(id);
								tweetsBuffer.add(temp);
								totalUserPosTweets++;

								if(classifiedAs.equals("user positive"))

								{
									//tweetwaiting adli tabloya tweetid createdat ilk algonun adi ve status 0 olarak atilacak
									DBConnect.insertToTweetWaiting(id,temp.GetOriginalTweet(),Long.valueOf(temp.GetTweetTime()).intValue(), "prepareAdditionalLocationInfoFromTweet", "","","","","","","","0",false);

								}
							}
							else
							{
								totalNegTweets++;
							}
							System.out.println("TOTALneg:"+totalNegTweets+" TOTALpos:"+totalUserPosTweets);

							//							olay  hakkinda lokassyon vs
							//							if(classifiedAs.equals("user positive")||classifiedAs.equals("official positive")) //simdilik sadece

							//							System.out.println("TWEET_PROCESS_OLCUM_GIRIS:"+id.toString()+"-"+System.currentTimeMillis());

							classification.getTextAnalytics().clear();


						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}


				}

			}

			@Override
			public void onDeletionNotice(
					StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:"
						+ statusDeletionNotice.getStatusId());
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:"
						+ numberOfLimitedStatuses);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId
						+ " upToStatusId:" + upToStatusId);
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				System.out.println("Got stall warning:" + warning);
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};

		twitterStream.addListener(listener);
		FilterQuery query = new FilterQuery();
		query.count(0);
		query.track(filterWords);
		twitterStream.filter(query);

	}

}
