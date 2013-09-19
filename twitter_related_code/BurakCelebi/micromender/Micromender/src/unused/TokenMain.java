package unused;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.collections15.Bag;
import org.apache.commons.collections15.bag.TreeBag;

import labeler.HashTagLabeler;
import labeler.Labeler;
import labeler.MentionLabeler;
import labeler.MultiLabeler;
import labeler.StopwordLabeler;
import labeler.UrlLabeler;
import tokenizer.Token;
import tokenizer.Tokenizer;
import twitter.TwitterClient;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.Paging;
import twitter4j.Place;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.TwitterException;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;
import twitter4j.json.DataObjectFactory;
import util.Util;

/**
 * @deprecated Unused Class
 */
public class TokenMain {

	public static void main(String[] args) throws TwitterException {
		//fetchTimeLine("BurakCelebi");
		// printStatus(null, 135666646265249792L);
		

		// System.out.println("RemainingHits:" + status.getRateLimitStatus().getRemainingHits());
		
		/*
		User showUser = TwitterClient.api().showUser("burakcelebi");

		System.out.println("RemainingHits:" + showUser.getRateLimitStatus().getRemainingHits());
		*/

		/*
		Scanner scanner = new Scanner(System.in);
		System.out.println(scanner.nextInt());
		*/
		/*
		String[] searchTxtArr = {"Semantic Web", "Linked Data", "Occupy Wall Street", "termination of employment", "Every government degenerates when trusted to the rulers of the people alone", "The Craft of Text Editing -or- A Cookbook for an Emacs", "Click here to see the algorithms"};

		for (String searchTxt : searchTxtArr) {
			searchQuote(searchTxt);
		}
		*/
		Query query = new Query("entropy energy");
		query.setLang(Util.LANG_ISO_639_1_ENGLISH);

		List<Tweet> tweets = TwitterClient.apiLocal().search(query).getTweets();
		
		for (Tweet tweet : tweets) {
			System.out.println(tweet.getText());
		}

		/*
		int retweeted = 1;
		int users = 5;
		int page = 1;
		while (users > 0 || page <10) {
			
			
			List<Tweet> tweets = TwitterClient.apiLocal().search(query).getTweets();

			for (Tweet tweet : tweets) {
				
				try {
					long retweetCount = TwitterClient.api().showStatus(tweet.getId()).getRetweetCount();
					if ( retweetCount >= retweeted ) {
						System.out.println(retweetCount + ": " + tweet);
						users--;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			
		}
		*/
		
	}

	private static void searchQuote(String searchTxt) throws TwitterException {
		
		System.out.println(searchTxt);
		
		searchTxt = searchTxt.toLowerCase();
		String searchTxtWithQ = ("\"" + searchTxt + "\"").toLowerCase();
		
		Query query = new Query(searchTxtWithQ);
		query.setLang(Util.LANG_ISO_639_1_ENGLISH);
		query.setRpp(100);
		query.setPage(15);
		query.setResultType(Query.MIXED);

		Bag<Tweet> tweetBag = new TreeBag<Tweet>();

		for (int i = 1; i <= 10; i++) {
			query.setPage(i);
			tweetBag.addAll(TwitterClient.apiLocal().search(query).getTweets());
		}
		
		System.out.println(tweetBag.size() + ", " + tweetBag.uniqueSet().size());
		
		Bag<Tweet> tweetBagWithOutQ = new TreeBag<Tweet>();
		Bag<Tweet> tweetBagWithQ = new TreeBag<Tweet>();
		
		for (Tweet tweet : tweetBag.uniqueSet()) {
			
			String text = tweet.getText().toLowerCase();
			
			if (text.contains(searchTxt)) {
				
				if (text.contains(searchTxtWithQ)) {
					tweetBagWithQ.add(tweet);
				} else {
					tweetBagWithOutQ.add(tweet);
				}
				
			}
		}
		
		System.out.println(tweetBagWithQ.uniqueSet().size());
		for (Tweet tweet : tweetBagWithQ.uniqueSet()) {
			System.out.println(tweet.getId() + ": " + tweet.getText());
		}
		
		System.out.println("----------------------------");
		
		System.out.println(tweetBagWithOutQ.uniqueSet().size());
		for (Tweet tweet : tweetBagWithOutQ.uniqueSet()) {
			System.out.println(tweet.getId() + ": " + tweet.getText());
		}
		
		System.out.println("--------------------------------------------------------------------------");
	}
	
	private static void fetchTimeLine(String userName) throws TwitterException {
		
		for (Status status : fetchStatus(userName)) {
			printStatus(status, null);
		}
	}
	
	/**
	 * 
	 * @param userName
	 * @return
	 * @throws TwitterException
	 */
	private static List<Status> fetchStatus(String userName) throws TwitterException {

		List<Status> result = new ArrayList<Status>();
		
		int pages = TwitterClient.api().showUser(userName).getStatusesCount() / 20;
		for (int i = 0; i <= Math.min(pages, 1); i++) {
			result.addAll(TwitterClient.api().getUserTimeline(userName, new Paging(i+1)));
		}
		return result;
	}
	
	public static void printStatus(Status status, Long id) throws TwitterException {
		
		if (id != null) {
			status = TwitterClient.api().showStatus(id);
		}
		
		System.out.println( DataObjectFactory.getRawJSON(status) );
		 
		System.out.println("_______");
		
		System.out.println(status);

		System.out.println(status.getUser().getScreenName());
		
		// System.out.println("RemainingHits:" + status.getRateLimitStatus().getRemainingHits());
		
		// isRetweet?
		boolean isRetweet = status.isRetweet();
		if (isRetweet) {
			System.out.println("isRetweet=" + isRetweet);
			Status retweetedStatus = status.getRetweetedStatus();
			System.out.println("retweetedStatus.getId()=" + retweetedStatus.getId());
			System.out.println("retweetedStatus.getUser().getScreenName()=" + retweetedStatus.getUser().getScreenName());
			
			status = retweetedStatus;
			
		}
		
		// System.out.println("RemainingHits:" + status.getRateLimitStatus().getRemainingHits());

		// isReply?
		long inReplyToStatusId = status.getInReplyToStatusId();
		if (inReplyToStatusId != -1) {
			System.out.println("inReplyToStatusId=" + inReplyToStatusId);
			String inReplyToScreenName = status.getInReplyToScreenName();
			System.out.println("inReplyToScreenName=" + inReplyToScreenName);
		}
		
		HashtagEntity[] hashtagEntities = status.getHashtagEntities();
		System.out.println("\nhashtags:");
		for (HashtagEntity entity : hashtagEntities) {
			System.out.println(entity.getText());
		}

		System.out.println("\nurls:");
		URLEntity[] urlEntities = status.getURLEntities();
		for (URLEntity entity : urlEntities) {
			
			URL url = entity.getExpandedURL();
			if(url==null) {
				url = entity.getURL();
			}
			
			System.out.println(url.toString());
		}
		
		System.out.println("\nmentions:");
		UserMentionEntity[] userMentionEntities = status.getUserMentionEntities();
		for (UserMentionEntity entity : userMentionEntities ) {
			System.out.println(entity.getScreenName());
		}
		
		// System.out.println("RemainingHits:" + status.getRateLimitStatus().getRemainingHits());
		
		GeoLocation geoLocation = status.getGeoLocation();
		System.out.println("geoLocation=" + geoLocation);
		
		Place place = status.getPlace();
		if (place!=null){
			System.out.println("place=" + place.getFullName());
		}
		
		long retweetCount = status.getRetweetCount();
		System.out.println("retweetCount=" + retweetCount);
		
		// System.out.println("RemainingHits=" + status.getRateLimitStatus().getRemainingHits());

		System.out.println("-------------------------------------------------------------------------");
	}
	
	public static void main1(String[] args) throws Exception {

		String text = "@burakcelebi about Four killed coming ComIng comiNg in East #Java landslide http://t.co/yWV https://t.co/yWV http://google.com/hello http://t.co/y'WV";
		text = "RT @cnnbrk: Blackout brings #Giants-Cowboys game to temporary halt http://on.cnn.com/dlAdJQ";
		
		List<Token> tokens = Tokenizer.tokenize(text);

		MultiLabeler labelers = new MultiLabeler(Arrays.asList(new Labeler[] {
				  new StopwordLabeler(),
			      new HashTagLabeler(),
			      new MentionLabeler(),
			      new UrlLabeler(),
			    }));
		labelers.init();
		
		List<Token> labeled = labelers.label(tokens);
		
		for (Token token : labeled) {
			System.out.println(token.getValue() + ": " + token.getLabel());
		}
		
	}
	
}
