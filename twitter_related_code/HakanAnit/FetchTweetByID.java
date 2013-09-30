import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;

import cmu.arktweetnlp.Tagger.TaggedToken;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;


public class FetchTweetByID
{
	private String strConsumerKey,strConsumerKeySecret,strTwitterToken,strTwitterTokenSecret;
	private BasicDBObject doc, hashtags, urls, mentions, punctuations,
	emoticons, tempObj;
	private BasicDBList tweet;
	private String[] filterWords;
	private Boolean flagIndex;
	private TextAnalytics textAnalytics;
	private List<TaggedToken> taggedTokens;

	public FetchTweetByID()
	{
		strConsumerKey="6yjRQJKTHZgQsPEf7crlw";
		strConsumerKeySecret="H860cYorslljci1Cv597fvAIPIjENDyWXIp7DcWGA";
		strTwitterToken="588674231-iDPFI4q0dSX8P8sBea9Gr0BTzhvp44WKgmn7ZrST";
		strTwitterTokenSecret="UOPiZ13Ve2k6d9OVnRik5TmIjye2iuoIVsnoYzHI";
	}
	public void fetchTweetByIDAddToDB() throws IOException, NumberFormatException, TwitterException, InterruptedException
	{
		DBConnect.connect();
		final Twitter twitter = new TwitterFactory().getInstance();
		//		   twitter.setOAuthConsumer(strConsumerKey, strConsumerKeySecret);
		AccessToken accessToken = new AccessToken(strTwitterToken,
				strTwitterTokenSecret);
		//		 twitter.setOAuthAccessToken(accessToken);
		//dosyadan okucak stringden yani linkten cikarticak ve dbye eklicek
		////////////////
		TweetStat temp=new TweetStat();
		Classification classification=new Classification("",0);
		textAnalytics=classification.getTextAnalytics();
		BufferedReader br = new BufferedReader(new FileReader("tweetsManual.txt"));
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("BASLIOR");

		String line = br.readLine();
		int cnt1=0;
		while (line != null)
		{
			//saatte 350 limiti var
			if(cnt1%350==0&&cnt1!=0)
			{
				System.out.println("kacta: "+cnt1);
				//simdi burda oyle bisi ypaicaz ki ya 1 saat beklicek yada modemi elle restart ediceksin ve devama basicaksin
				//simdiki zamani al zaten entera basmasyan o kadar bekliceksin
				Long valTimeStop=System.currentTimeMillis();  
				Long valTimeTemp=System.currentTimeMillis();
				char c;
				System.out.println("modemi resetlediysen 'q' yaz");
				c = (char) br1.read();
				while(valTimeStop+3600*1000>valTimeTemp)
				{
					if(c == 'q')
						break;
					valTimeTemp=System.currentTimeMillis(); 
					c = (char) br1.read();
				}
			}



			cnt1++;
			Status status=null;
			Boolean boolFail=false;
			try
			{
				status = twitter.showStatus(Long.parseLong(line));

			}
			catch(Exception e)
			{
				System.out.println(e.toString());
				boolFail=true;
			}
			if(!boolFail)
			{
				if (status == null) { // 
					// don't know if needed - T4J docs are VERY BAD
				} else {
					System.out.println("@" + status.getUser().getScreenName()
							+ " - " + status.getUser().getLocation()+" "+ status.getText());
					
				}
				//////DBYE AT
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
				System.out.println("ID: " + tweetid.toString());
				
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


				tempObj.put("hashtags", hashtags); // key lokasyon value
				// hashtag
				tempObj.put("emoticons", emoticons);
				tempObj.put("mentions", mentions);
				tempObj.put("urls", urls);
				tempObj.put("punctuations", punctuations);

				doc.put("annotated", 0);

				doc.put("eventType", "manual");


				doc.put("indexed", 1);

				tweet.add(tempObj); // bide bunu ekledim
				doc.put("tweet", tweet);
				DBConnect.getCollEvent().insert(doc);
				temp.clear();
				////////////////
			}
			line=br.readLine();
		} 
	}
}

