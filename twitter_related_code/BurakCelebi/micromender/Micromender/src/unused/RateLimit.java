package unused;

import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.apache.commons.collections15.Bag;
import org.apache.commons.collections15.bag.TreeBag;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import search.Conf;
import twitter.TwitterClient;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationBuilder;
import util.Util;

/**
 * @deprecated Unused Class
 */
public class RateLimit {

	private final static Logger logger = LoggerFactory.getLogger("micromender");

	public static void main(String[] args) {
		List<Tweet> tweetsFromDistinctUsers = new ArrayList<Tweet>(Conf.TWITTER_SEARCH_PAGE_SIZE);
		System.out.println(tweetsFromDistinctUsers.size());
		
	}
	
	public static void main1(String[] args) throws TwitterException {
		Twitter twitter = TwitterClient.apiLocal();
		
		String q = "big-data";
		
		Query query = new Query(q);
		query.setLang(Util.LANG_ISO_639_1_ENGLISH);
		query.setRpp(Conf.TWITTER_SEARCH_PAGE_SIZE);
		
		for (Tweet tweet : twitter.search(query).getTweets()) {
			System.out.println(tweet.getFromUser() + ": " + tweet.getText());
		}
		
		System.out.println("-------------");
		
		query.setQuery("\"" + q + "\"");
		
		for (Tweet tweet : twitter.search(query).getTweets()) {
			System.out.println(tweet.getFromUser() + ": " + tweet.getText());
		}
	}
	
	public static void main2(String[] args) throws Exception {

		/*
		logger.debug("hello from debug");		
		logger.info("hello from info");
		logger.error("hello from error");
		logger.warn("hello from warn");
		*/
		
		
		//StopWatch stopWatchDead = new LoggingStopWatch("stopWatchDead");
		
		
		/*
		stopWatchDead.stop();
		
		for (int i = 0; i < 4; i++) {
			StopWatch stopWatch = new LoggingStopWatch("firstBlock");
	
			// Execute the code block - this is just a dummy call to pause execution for
			// 0-1 second.
			Thread.sleep((long)(Math.random() * 1000L));
	
			// Stop the StopWatch and log it. LoggingStopWatches automatically log their
			// timing statements when one of the stop() or lap() methods are called.
			stopWatch.stop();
		}
		*/
		
 		Twitter twitter = TwitterClient.apiLocal();
		
		// ConfigurationBuilder cb = TwitterClient.createConfBuilder();
		//cb.setHttpProxyHost("201.36.126.132").setHttpProxyPort(80);
		// Twitter twitter = TwitterClient.toTwitterInstance(cb);
		
		//logger.info("RemainingHits/HourlyLimit: " + twitter.getRateLimitStatus().getRemainingHits() + "/" + twitter.getRateLimitStatus().getHourlyLimit());
		//List<Tweet> tweets = twitter.search(new Query("#java")).getTweets();

 		List<Tweet> tweetsFromDistinctUsers = new ArrayList<Tweet>(Conf.TWITTER_SEARCH_PAGE_SIZE);
 		
		Query query = new Query("flash");
		query.setLang(Util.LANG_ISO_639_1_ENGLISH);
		query.setRpp(Conf.TWITTER_SEARCH_PAGE_SIZE);
		
		// List<Tweet> tweets = twitter.search(query).getTweets();
		//System.out.println(tweets.size());
		
		int page = 1;
		
		while (tweetsFromDistinctUsers.size() < Conf.TWITTER_SEARCH_PAGE_SIZE) {
			System.out.println("page=" + page);
			if (page == 5) {
				break;
			}
			query.setPage(page++);
			addNewUserTweets(tweetsFromDistinctUsers, twitter.search(query).getTweets());
		}
		
		if (tweetsFromDistinctUsers.size() > Conf.TWITTER_SEARCH_PAGE_SIZE) {
			tweetsFromDistinctUsers = tweetsFromDistinctUsers.subList(0, Conf.TWITTER_SEARCH_PAGE_SIZE);
		}
		System.out.println(tweetsFromDistinctUsers.size());
		
		Bag<String> bag = new TreeBag<String>();
		for (Tweet tweet : tweetsFromDistinctUsers) {
			bag.add(tweet.getFromUser());
		}
		
		System.out.println(bag.uniqueSet().size());
		
		//Twitter twitter = new TwitterFactory().getInstance();
		
		//System.out.println("RemainingHits/HourlyLimit: " + twitter.getRateLimitStatus().getRemainingHits() + "/" + twitter.getRateLimitStatus().getHourlyLimit());
		//logger.info("HourlyLimit: " + twitter.getRateLimitStatus().getHourlyLimit());
		//logger.info("RemainingHits={}", twitter.getRateLimitStatus().getRemainingHits());
		
		/*
		logger.debug("hello from debug");		
		logger.info("hello from info");
		logger.error("hello from error");
		logger.warn("hello from warn");
		*/
		
		/*
		Query query = new Query("java");
		query.setLang(Util.LANG_ISO_639_1_ENGLISH);
		query.setRpp(100);
		
		
		for (int i = 1; i < 20; i++) {
			
			System.out.println("page=" + i);
			
			query.setPage(i);

			for (Tweet tweet : twitter.search(query).getTweets()) {
				System.out.println(tweet.getText());
			}
			
			
		} 
		*/
		
		/*
		ResponseList<Status> userTimeline = twitter.getUserTimeline("habersiz", new Paging(1,100));
		
		for (Status status : userTimeline) {
			System.out.println(status.getText());
		}
		Util.logger.info("info");
		Util.logger.warn("warn");
		Util.logger.error("error");
		*/
	}
	
	private static void addNewUserTweets(List<Tweet> tweetsFromDistinctUsers, List<Tweet> tweets) {
		
		for (Tweet tweet : tweets) {
			System.out.println(tweet.getFromUser() + ": " + tweet.getText());
			if (includesFromUser(tweetsFromDistinctUsers, tweet.getFromUser())) {
				continue;
			} else {
				tweetsFromDistinctUsers.add(tweet);
			}
		}
		
	}

	private static boolean includesFromUser(List<Tweet> tweetsFromDistinctUsers, String fromUser) {
		
		for (Tweet tweet : tweetsFromDistinctUsers) {
			if (fromUser.equals(tweet.getFromUser())) {
				return true;
			}
		}
		return false;
	}

	public static void main3(String[] args) throws TwitterException {
		
		String user = "burakcelebi";
		
		Twitter twitter = TwitterClient.apiLocal();
		Twitter twitterHabersiz = TwitterClient.toTwitterInstance(TwitterClient.createConfBuilderHabersiz());
		
		ConfigurationBuilder cb = new ConfigurationBuilder(); //TwitterClient.createConfBuilder();
		TwitterClient.injectProxy(cb);
		Twitter twitterProxy = TwitterClient.toTwitterInstance(cb);

		ConfigurationBuilder cb2 = new ConfigurationBuilder(); //TwitterClient.createConfBuilder();
		TwitterClient.injectProxy(cb2);
		Twitter twitterProxy2 = TwitterClient.toTwitterInstance(cb2);
		
		printRemaining(twitter, "twitter");
		printRemaining(twitterProxy, "twitterProxy");
		// printRemaining(twitterProxy2, "twitterProxy2");
		printRemaining(twitterHabersiz, "twitterHabersiz");
		
		
		
		/*
		System.out.println("local calling..");
		twitter.getUserTimeline(user, new Paging(1));
		
		
		printRemaining(twitter, "twitter");
		printRemaining(twitterProxy, "twitterProxy");
		// printRemaining(twitterProxy2, "twitterProxy2");
		*/
		
		
		System.out.println("twitterProxy calling..");
		twitterProxy.getUserTimeline(user, new Paging(1));
		
		printRemaining(twitter, "twitter");
		printRemaining(twitterProxy, "twitterProxy");
		//printRemaining(twitterProxy2, "twitterProxy2");
		printRemaining(twitterHabersiz, "twitterHabersiz");
		
		/*
		ResponseList<Status> userTimelineProxy = twitterProxy.getUserTimeline(user, new Paging(2));
		System.out.println("twitter: " + twitter.getRateLimitStatus().getRemainingHits());
		System.out.println("twitterProxy: " + twitterProxy.getRateLimitStatus().getRemainingHits());
		*/
		
		/*
		twitter.getUserTimeline(user, new Paging(2));
		System.out.println(twitter.getRateLimitStatus().getRemainingHits());
		*/
	}

	private static void printRemaining(Twitter twitter, String name) throws TwitterException {
		System.out.println(name + ": " + twitter.getRateLimitStatus().getRemainingHits());
	}
}