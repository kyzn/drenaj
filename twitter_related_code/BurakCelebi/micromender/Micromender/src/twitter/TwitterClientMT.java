package twitter;

import static util.MicromenderConfigurator.DELIMETER_COMMA;
import static util.MicromenderConfigurator.OAUTH_ACCESS_TOKEN;
import static util.MicromenderConfigurator.OAUTH_ACCESS_TOKEN_SECRET;
import static util.MicromenderConfigurator.OAUTH_CONSUMER_KEY;
import static util.MicromenderConfigurator.OAUTH_CONSUMER_SECRET;
import static util.MicromenderConfigurator.PROXIES_CVS;
import static util.MicromenderConfigurator.getProperty;
import static util.MicromenderConfigurator.getPropertyWithDelimeterAsStringList;

import java.util.List;
import java.util.Random;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @deprecated Unused class
 */
public class TwitterClientMT {

	private static List<String> proxies;
	private static final int MAX_RETRY_FOR_PROXY = 3;

	private final static Random GENERATOR = new Random(19580427);
	
	/**
	 * 
	 * @param cb
	 */
	private static void injectProxy(ConfigurationBuilder cb, int proxyIndex) {

		initProxies();
		
		System.out.println("NewProxy=" + proxies.get(proxyIndex) + " at " + proxyIndex);
		
		String[] proxy = proxies.get(proxyIndex).split(":");
		cb.setHttpProxyHost(proxy[0]).setHttpProxyPort(Integer.parseInt(proxy[1]));
	}
	
	private static Twitter api(int proxyIndex) {
		
		Twitter twitter = null;
		
		ConfigurationBuilder cb = createConfBuilder();
		
		injectProxy(cb, proxyIndex);
		
		twitter = toTwitterInstance(cb);
		
		/* FIXME
		while (findRemainingHits(twitter) == 0 ) {
			twitter = api(proxyIndex + 50);
		}
		*/
		
		return twitter;
	}
	
	public static User showUser(String screenName, int proxyIndex) {
		
		User user = null;

		int retryCount = 0;
		int indexShift = -1;
		while (user == null && retryCount<MAX_RETRY_FOR_PROXY)  {
			
			proxyIndex = findNewProxyIndex(proxyIndex, retryCount, indexShift);  
			
			
			/**
			 * showUser: screenName=0wasp, proxyIndex=-1, retryCount=1
				java.lang.ArrayIndexOutOfBoundsException: -1
				shift stratejisi ne olsun?
			 */
			
			String log = null;
			try {
				log = "showUser: screenName=" + screenName + ", proxyIndex=" + proxyIndex + ", retryCount=" + retryCount;
				System.out.println(log);
				user = api(proxyIndex).showUser(screenName);
			} catch (Exception e) {
				System.out.println("ex for " + log);
				e.printStackTrace();
			}
			retryCount++;
		}
		
		return user;
	}

	private static int findNewProxyIndex(int proxyIndex, int retryCount, int indexShift) {
		proxyIndex += retryCount * indexShift;
		proxyIndex = (proxyIndex<0) ? proxyIndex+proxies.size() : proxyIndex;
		
		if (proxyIndex>proxies.size()-1) {
			proxyIndex = GENERATOR.nextInt(proxies.size());
		}
		
		return proxyIndex;
	}

	public static ResponseList<Status> getUserTimeline(String screenName, Paging paging, int proxyIndex) {
		
		ResponseList<Status> responseList = null;

		int retryCount = 0;
		int indexShift = -1;
		while (responseList == null && retryCount<MAX_RETRY_FOR_PROXY)  {
			
			proxyIndex = findNewProxyIndex(proxyIndex, retryCount, indexShift);
			
			String log = null;
			try {
				log = "getUserTimeline: screenName=" + screenName + ", proxyIndex=" + proxyIndex + ", retryCount=" + retryCount;
				System.out.println(log);
				
				responseList  = api(proxyIndex).getUserTimeline(screenName, paging);
			} catch (Exception e) {
				System.out.println("ex for " + log);
				e.printStackTrace();
			}
			retryCount++;
		}
		return responseList;
	}
	
	private static int findRemainingHits(Twitter twitter) {
		
		int remainingHits=0;
		try {
			remainingHits = twitter.getRateLimitStatus().getRemainingHits();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return remainingHits;
	}
	
	/**
	 * 
	 */
	private static void initProxies() {
		
		if (proxies == null) {
			proxies = getPropertyWithDelimeterAsStringList(PROXIES_CVS, DELIMETER_COMMA);
		}
	}

	private static Twitter toTwitterInstance(ConfigurationBuilder cb) {
		return new TwitterFactory(cb.build()).getInstance();
	}

	public static ConfigurationBuilder createConfBuilder() {
			
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(getProperty(OAUTH_CONSUMER_KEY))
		.setOAuthConsumerSecret(getProperty(OAUTH_CONSUMER_SECRET))
		.setOAuthAccessToken(getProperty(OAUTH_ACCESS_TOKEN))
		.setOAuthAccessTokenSecret(getProperty(OAUTH_ACCESS_TOKEN_SECRET))
		// .setUser(getProperty(USER))
		// .setPassword(getProperty(PASSWORD))
		.setJSONStoreEnabled(true)
		;
		
		return cb;
	}
}
