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

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Class to interact with Twitter via Twitter4J. Other classes should use this class when accessing Twitter.  
 */
public class TwitterClient {

	private static Twitter twitter;
	private static List<String> proxies;
	
	/**
	 * @deprecated Unused field
	 */
	public static int proxyIndex;
	
	public static Twitter apiLocal() {
		return newTwitter();
	}
	
	/**
	 * @deprecated Unused method
	 * 
	 * @param cb
	 */
	public static void injectProxy(ConfigurationBuilder cb, int proxyIndex) {

		initProxies();
		
		System.out.println("NewProxy=" + proxies.get(proxyIndex) );
		
		String[] proxy = proxies.get(proxyIndex).split(":");
		cb.setHttpProxyHost(proxy[0]).setHttpProxyPort(Integer.parseInt(proxy[1]));
	}
	
	public static Twitter api() {
		
		if (twitter == null) {
			twitter = newTwitter();
		} 
		
		while (findRemainingHits(twitter) == 0 ) {
			twitter = newTwitterViaProxy();
		}
		
		// System.out.println("Remaining=" + twitter.getRateLimitStatus().getRemainingHits() );
		
		return twitter;
	}

	/**
	 * @deprecated Unused method
	 * 
	 * @param screenName
	 * @return
	 */
	public static User showUser(String screenName) {
		
		User user = null;

		while (user == null)  {
			try {
				user = api().showUser(screenName);
			} catch (Exception e) {
				System.out.println("proxyIndex=" + proxyIndex);
				e.printStackTrace();
				// FIXME: user not found 404
			}
		}
		return user;
	}
	
	/**
	 * @deprecated Unused method
	 * 
	 * @param screenName
	 * @param paging
	 * @return
	 */
	public static ResponseList<Status> getUserTimeline(String screenName, Paging paging) {
		
		ResponseList<Status> responseList = null;

		while (responseList == null)  {
			try {
				responseList  = api().getUserTimeline(screenName, paging);
			} catch (Exception e) {
				System.out.println("proxyIndex=" + proxyIndex);
				e.printStackTrace();
			}
		}
		return responseList;
	}
	
	private static int findRemainingHits(Twitter twitter) {
		
		int remainingHits=0;
		try {
			remainingHits = twitter.getRateLimitStatus().getRemainingHits();
		} catch (Exception e) {
			System.out.println("proxyIndex=" + proxyIndex);
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

	/**
	 * 
	 * @return
	 */
	private static Twitter newTwitter() {
		return newTwitter(false);
	}
	
	/**
	 * 
	 * @return
	 */
	private static Twitter newTwitterViaProxy() {
		return newTwitter(true);
	}
	
	/**
	 * 
	 * @return
	 */
	private static Twitter newTwitter(boolean withProxy) {

		ConfigurationBuilder cb = createConfBuilder();
		
		if (withProxy) {
			injectProxy(cb);
		}
		
		return toTwitterInstance(cb);
	}

	public static Twitter toTwitterInstance(ConfigurationBuilder cb) {
		return new TwitterFactory(cb.build()).getInstance();
	}

	public static ConfigurationBuilder createConfBuilder() {
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
          .setOAuthConsumerKey(getProperty(OAUTH_CONSUMER_KEY))
          .setOAuthConsumerSecret(getProperty(OAUTH_CONSUMER_SECRET))
          .setOAuthAccessToken(getProperty(OAUTH_ACCESS_TOKEN))
          .setOAuthAccessTokenSecret(getProperty(OAUTH_ACCESS_TOKEN_SECRET))
          .setJSONStoreEnabled(true)
		;
		return cb;
	}
	
	public static ConfigurationBuilder createConfBuilderHabersiz() {
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
	      .setOAuthConsumerKey(getProperty("habersiz."+OAUTH_CONSUMER_KEY))
	      .setOAuthConsumerSecret(getProperty("habersiz."+OAUTH_CONSUMER_SECRET))
	      .setOAuthAccessToken(getProperty("habersiz."+OAUTH_ACCESS_TOKEN))
	      .setOAuthAccessTokenSecret(getProperty("habersiz."+OAUTH_ACCESS_TOKEN_SECRET))
	      .setJSONStoreEnabled(true)
		;
		return cb;
	}


	
	/**
	 * 
	 * @param cb
	 */
	public static void injectProxy(ConfigurationBuilder cb) {

		initProxies();
		
		if (proxies != null) {
			
			proxyIndex = (proxyIndex + 1) % proxies.size();

			System.out.println("NewProxy=" + proxies.get(proxyIndex) );
			
			String[] proxy = proxies.get(proxyIndex).split(":");
			cb.setHttpProxyHost(proxy[0]).setHttpProxyPort(Integer.parseInt(proxy[1]));

		} else {
			System.out.println("Rate limit exceeded. No proxy defined!");
		}
	}
}
