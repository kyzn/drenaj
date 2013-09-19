package search;

import java.util.ArrayList;
import java.util.List;

import twitter.MyTweet;
import twitter.MyUser;
import twitter.TwitterClient;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;
import util.LogUtil;

/**
 * @deprecated Unused class
 */
public class UserThread implements Runnable {

	// private Thread thread;
	
	private String userName; 

	public String getUserName() {
		return userName;
	}

	private Search search;
	private boolean isExtended;
	
	private int index;
	
	private MyUser myUser;
	private SearchServiceImpl searchService;
	private int searchTypeExtended;
	
	public UserThread(String userName, Search search, boolean isExtended, int index, SearchServiceImpl searchService, int searchTypeExtended) {
		
		this.userName = userName;
		this.search = search;
		this.isExtended = isExtended;
		
		this.index = index;
		
		this.searchService = searchService;
		
		this.searchTypeExtended = searchTypeExtended;
		
		// thread = new Thread (this);
	}
	
	@Override
	public void run() {
		
		try {
			
			myUser = buildMyUser();
			
			LogUtil.logger.info("start of inserting tweets for {}", userName);
			searchService.insertUserWithTweets(search, myUser, searchTypeExtended);
			LogUtil.logger.info("end of inserting tweets for {}", userName);
		        
		} catch (Exception e) {
			LogUtil.logger.info("ex for user={}, ex={}", userName, e.toString());
			// throw new RuntimeException(e);
		}
	}
	
	/*
	
	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}
	*/
	public MyUser getMyUser() {
		return myUser;
	}

	public void setMyUser(MyUser myUser) {
		this.myUser = myUser;
	}

	private MyUser buildMyUser() throws Exception {
		
		MyUser myUser = null;
		
		if (search.fromTwitter()) {
			
			/**
			User user = TwitterClientMT.showUser(userName, index);
			 **/
			
			User user = TwitterClient.apiLocal().showUser(userName);
			
			if (user != null) { 
				
				LogUtil.logger.info("getting tweets of {}. index={}", userName, index);
				
				int numOfPages = 1; //Math.min(user.getStatusesCount() / SearchServiceImpl.TWITTER_TIMELINE_PAGE_SIZE, SearchServiceImpl.TWITTER_TIMELINE_MAX_PAGES); //FIXME
				
				List<MyTweet> userTweets = fetchMyTweetsMT(userName, numOfPages, index);
				// List<MyTweet> userTweets = SearchServiceImpl.fetchMyTweets(userName, numOfPages);
				
				myUser = new MyUser(user, userTweets, search);
				// myUser = new MyUser(userName, userTweets, search); //ratelimit-showUser
				
				myUser.setExtended(isExtended);
				
			}
		} else {
			myUser = searchService.fetchUserFromDb(userName, search);
		}
			
		
		return myUser;
		
	}

	public static List<MyTweet> fetchMyTweetsMT(String userName, int numOfPages, int index) throws Exception {
		return MyTweet.toMyTweetsFromStatuses(fetchStatusListMT(userName, numOfPages, index));
	}

	/**
	 * 
	 * @param userName
	 * @return
	 * @throws TwitterException
	 */
	private static List<Status> fetchStatusListMT(String userName, int numOfPages, int index) throws TwitterException {

		List<Status> result = new ArrayList<Status>();
		
		for (int i = 0; i < numOfPages; i++) {
			/**
			 * FIXME
			result.addAll(TwitterClientMT.getUserTimeline(userName, new Paging(i+1), index));
			 */
			
			result.addAll(TwitterClient.apiLocal().getUserTimeline(userName, new Paging(i+1, Conf.USER_MAX_TWEETS)));
			
		}
		return result;
	}
}
