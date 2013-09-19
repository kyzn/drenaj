package search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import labeler.StopwordLabeler;
import network.Edge;
import network.NetworkUtils;

import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import similarity.SearchResult;
import twitter.MyTweet;
import twitter.MyUser;
import twitter.TwitterClient;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.TwitterException;
import twitter4j.User;
import util.LogUtil;
import util.Util;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.graph.DirectedGraph;

public class SearchServiceImpl implements SearchService {

	private Dao dao;
	
	public SearchServiceImpl (Dao dao) {
		this.dao = dao;
	}
	
	/**
	 * 
	 */
	public List<MyTweet> search(Search search) throws Exception {

		StopWatch stopWatch = new Log4JStopWatch("twitterSearchAndInsert", String.valueOf(search.getId()));
		
		search.setStartTime(new Date());
		dao.updateSearchOrderStatus(search, Search.SEARCH_STATUS_PROCESSING);
		
		List<MyTweet> myTweets = searchMyTweets(search);
		
		insertSearch(myTweets, search);
		
		stopWatch.stop();
		
		return myTweets;
	}

	/**
	 * 
	 * @param query
	 * @return
	 * @throws TwitterException
	 * @throws Exception
	 */
	private List<MyTweet> searchMyTweets(Search search) throws Exception {

		List<MyTweet> myTweets;
		
		if (search.fromTwitter()) {
			myTweets = searchMyTweetsFromTwitter(search);
		} else {
			dao.querySearchQuery(search);
			myTweets = dao.querySearchResult(search);
		}
		
		return myTweets;
	}

	/**
	 * 
	 * @param search
	 * @return
	 * @throws Exception
	 */
	private List<MyTweet> searchMyTweetsFromTwitter(Search search) throws Exception {
		
		List<Tweet> tweets = searchQuery(search.getQuery());

		if (search.getQueryTerms().size() > 1) {
			tweets.addAll( searchQuery(new StringBuilder("\"").append(search.getQuery()).append("\"").toString()) );
		}
		
		tweets.addAll( searchQuery(Util.asHashtag(search.getQuery())) );
		
		String queryStopWordsExcluded = StopwordLabeler.excludeStopWords(search.getQuery());
		
		if (queryStopWordsExcluded != null) {
			tweets.addAll( searchQuery(queryStopWordsExcluded) ) ; 
			tweets.addAll( searchQuery(Util.asHashtag(queryStopWordsExcluded)) );
		}
		/*
		if (search.getQueryTerms().size()>1){
			
			for (String queryToken : search.getQueryTerms()) {
				tweets.addAll(searchQuery(queryToken));
			}
		}
		*/
		
		for (String tag : search.getTopCooccuringTags().uniqueSet()) {
			
			tag = Util.makeSearchable(tag);

			if (tag != null) {
				
				if (!search.getQueryTerms().contains(tag)) {
					tweets.addAll(searchQuery( tag ));
					tweets.addAll(searchQuery(tag.concat(Util.DELIMITER_SPACE_STR).concat(search.getQuery()))); //FIXME: hashtag olarak (da) mı olsa?
				}
			}
		}
		
		return MyTweet.toMyTweetsFromTweets(tweets);
	}
	
	

	/**
	 * 
	 * @param queryStr
	 * @return
	 * @throws TwitterException
	 */
	private List<Tweet> searchQuery(String queryStr) throws TwitterException {

		List<Tweet> tweetsFromDistinctUsers = new ArrayList<Tweet>(Conf.TWITTER_SEARCH_PAGE_SIZE);
		
		if (queryStr != null) {
			
			queryStr = queryStr.replace('_', ' ').replace('-', ' ');
			
			Query query = new Query(queryStr);
			query.setLang(Util.LANG_ISO_639_1_ENGLISH);
			query.setRpp(Conf.TWITTER_SEARCH_PAGE_SIZE);
			
			try {
				/*
				tweets = TwitterClient.apiLocal().search(query).getTweets();
				if (tweets.size() > Conf.TWITTER_SEARCH_PAGE_SIZE) {
					tweets = tweets.subList(0, Conf.TWITTER_SEARCH_PAGE_SIZE);
				}*/
				
				int page = 1;
				while (tweetsFromDistinctUsers.size() < Conf.TWITTER_SEARCH_PAGE_SIZE) {
					if (page == 5) {
						break;
					}
					query.setPage(page++);
					addNewUserTweets(tweetsFromDistinctUsers, TwitterClient.apiLocal().search(query).getTweets());
				}
				
				if (tweetsFromDistinctUsers.size() > Conf.TWITTER_SEARCH_PAGE_SIZE) {
					tweetsFromDistinctUsers = tweetsFromDistinctUsers.subList(0, Conf.TWITTER_SEARCH_PAGE_SIZE);
				}
				
			} catch (Exception e) {
				LogUtil.logger.error("failed", e);
				LogUtil.logger.error("discarding query search for query={} due to {}", queryStr, e.toString());
			}
			
			/*
			int retryCount = 0;
			while (retryCount >= 0 && retryCount < 3) {
				try {
					Thread.sleep(retryCount*5000);
					tweets = TwitterClient.apiLocal().search(query).getTweets();
					retryCount = -1;
				} catch (Exception e) {
					LogUtil.logger.error(e.toString());
					retryCount++;
				}
			}
			*/
		}
		
		LogUtil.logger.info("Results for query={}, size={}", queryStr, tweetsFromDistinctUsers.size());
		
		StringBuilder sb = new StringBuilder();
		for (Tweet tweet : tweetsFromDistinctUsers) {
			sb.append(tweet.getText()).append("\n");
		}
		LogUtil.logger.info(sb.toString());

		return tweetsFromDistinctUsers;
	}
	
	/**
	 * 
	 * @param tweetsFromDistinctUsers
	 * @param tweets
	 */
	private static void addNewUserTweets(List<Tweet> tweetsFromDistinctUsers, List<Tweet> tweets) {
		
		for (Tweet tweet : tweets) {
			if (includesFromUser(tweetsFromDistinctUsers, tweet.getFromUser())) {
				continue;
			} else {
				tweetsFromDistinctUsers.add(tweet);
			}
		}
		
	}

	/**
	 * 
	 * @param tweetsFromDistinctUsers
	 * @param fromUser
	 * @return
	 */
	private static boolean includesFromUser(List<Tweet> tweetsFromDistinctUsers, String fromUser) {
		
		for (Tweet tweet : tweetsFromDistinctUsers) {
			if (fromUser.equals(tweet.getFromUser())) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 
	 * @param myTweets
	 * @param search
	 * @throws Exception
	 */
	private void insertSearch(List<MyTweet> myTweets, Search search) throws Exception {
		if (search.insert()) dao.insertSearch(search, myTweets);
		dao.querySearchQuery(search);
	}
	
	
	private static String[] findSubSet(Set<String> set, int numOfElems) {

		String[] subArr = new String[numOfElems];
		
		int i = 0;
		for (String username : set) {
			
			if (i == numOfElems) {
				break;
			}
			subArr[i++] = username; 
		}
		return subArr; 
	}

	
	public List<MyUser> queryUsers(List<MyTweet> myTweets, Search search) throws Exception {

		StopWatch stopWatch_queryUsers = new Log4JStopWatch("queryUsers");
		
		// actual users
		Set<String> uniqeUserNames = getUniqueUserNames(myTweets);
		
		List<MyUser> myUsers = new ArrayList<MyUser>(uniqeUserNames.size());

		for (String userName : uniqeUserNames) {
			
			buildAndInsertUserAndTweets(userName, false, Search.SEARCH_TYPE_USER, search, myUsers);
		}
		
		stopWatch_queryUsers.stop();
		
		return myUsers;
	}

	/**
	 * @throws Exception 
	 * 
	 */
	public List<MyUser> queryExtendedUsers(List<MyUser> referenceUsers, Search search, int extendedUserIteration) throws Exception {

		StopWatch stopWatch_queryExtendedUsers = new Log4JStopWatch("queryExtendedUsers");
		
		List<MyUser> myUsers = new ArrayList<MyUser>();
		
		if (referenceUsers.size() > 0) {
			
			Set<String> relatedUserNames = findRelatedUserNames(referenceUsers, search);
			
			for (String userName: relatedUserNames ) {

				buildAndInsertUserAndTweets(userName, true, extendedUserIteration, search, myUsers);
			}
		}
		
		stopWatch_queryExtendedUsers.stop();
		
		// dao.insertUserCharacter(myUsers, search); //FIXME: comment
		
		return myUsers;
	}

	/**
	 * 
	 * @param userName
	 * @param isExtended
	 * @param extendedUserIteration
	 * @param search
	 * @param myUsers
	 */
	private void buildAndInsertUserAndTweets(String userName, boolean isExtended, int extendedUserIteration, Search search, List<MyUser> myUsers) {
		
		// LogUtil.logger.info("<buildAndInsertUserAndTweets({})> search={}", userName, search.getId());
		
		try {
			
			StopWatch stopWatch = new Log4JStopWatch("buildAndInsertUserAndTweets");
			MyUser myUser = buildMyUser(userName, search, isExtended);
			myUsers.add(myUser);
			insertUserWithTweets(search, myUser, extendedUserIteration);
			stopWatch.stop();
			
		} catch (Exception e) {
			LogUtil.logger.error("failed", e);
			LogUtil.logger.error("discarding user={} due to exception.", userName);
		}
		
		
		// LogUtil.logger.info("</buildAndInsertUserAndTweets({})> search={}", userName, search.getId());
	}

	/**
	 * 
	 * @param search
	 * @param userName
	 * @param isExtended
	 * @return
	 */
	/*
	private MyUser buildMyUserExcSafe(Search search, String userName, boolean isExtended) {
		
		MyUser myUser = null;
		
		try {
			myUser = buildMyUser(userName, search, isExtended);
		} catch (Exception e) {
			LogUtil.logger.info("ex for user={}, ex={}", userName, e.toString());
			e.printStackTrace();
		}
		
		return myUser;
	}
	*/
	
	/**
	 * 
	 * @param myUsers
	 * @param search
	 * @return
	 */
	private Set<String> findRelatedUserNames(List<MyUser> referenceUsers,  Search search) {
		
		Set<String> relatedUserNames = new TreeSet<String>();
		
		Set<String> referenceUserNames = MyUser.findUserNames(referenceUsers);
		
		List<String> findRelatedUserNames = dao.findRelatedUserNames(search, referenceUserNames);
		// findRelatedUserNames = Util.findTopElems(findRelatedUserNames, 3);
			
		relatedUserNames.addAll(findRelatedUserNames);
		
		relatedUserNames.removeAll(referenceUserNames);

		return relatedUserNames;
	}
	
	/**
	 * 
	 * @param search
	 * @param myUser
	 * @throws Exception
	 */
	public void insertUserWithTweets(Search search, MyUser myUser, int searchType) throws Exception {
		
		String screenName = myUser.getScreenName();
		int searchId = search.getId();
		
		String stopWatchMessage = new StringBuilder("screenName=").append(screenName).append(", searchId=").append(searchId).toString();
		
		// LogUtil.logger.info("<insertUserWithTweets({})> search={}", screenName, searchId);
		StopWatch stopWatch_insertUserWithTweets = new Log4JStopWatch("insertUserWithTweets", stopWatchMessage);
		
		if (search.fromDb() && search.insert()) {
			
			for (MyTweet myTweet : myUser.getTweets()) {
				dao.insertTweetTokens(myTweet);
			}
			
		} else if (search.insert()) {
			
			// LogUtil.logger.info("<insertUser({})> search={}", screenName, searchId);
			StopWatch stopWatch_insertUser = new Log4JStopWatch("insertUser", stopWatchMessage);
			dao.insertUser(myUser, search);
			stopWatch_insertUser.stop();
			// LogUtil.logger.info("</insertUser({})> search={}", screenName, searchId);
			
			// LogUtil.logger.info("<insertTweets({})> search={}", screenName, searchId);
			StopWatch stopWatch_insertTweets = new Log4JStopWatch("insertTweets", stopWatchMessage);
			dao.insertTweets(myUser, search, searchType);
			stopWatch_insertTweets.stop();
			// LogUtil.logger.info("</insertTweets({})> search={}", screenName, searchId);
			
			// LogUtil.logger.info("<insertTweetTokens({})> search={}", screenName, searchId);
			// StopWatch stopWatch_insertTweetTokens = new Log4JStopWatch("insertTweetTokens", stopWatchMessage);
			// dao.insertTweetTokens(myUser);
			/*
			for (MyTweet myTweet : myUser.getTweets()) {
				dao.insertTweetTokens(myTweet);
			}
			*/
			
			//stopWatch_insertTweetTokens.stop();
			// LogUtil.logger.info("</insertTweetTokens({})> search={}", screenName, searchId);
			
			/*
			for (MyTweet myTweet : myUser.getTweets()) {
			
				/// LogUtil.logger.info("<insertTweet({})> search={}", myTweet.getId(), search.getId());
				dao.insertTweet(myTweet, search, searchType);
				dao.insertTweetTokens(myTweet);
				// LogUtil.logger.info("</insertTweet({})> search={}", myTweet.getId(), search.getId());
				
			}
			*/
		}
		
		stopWatch_insertUserWithTweets.stop();
		// LogUtil.logger.info("</insertUserWithTweets({})> search={}", screenName, searchId);
	}

	/**
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	private MyUser buildMyUser(String userName, Search search, boolean isExtended) throws Exception {
		
		// LogUtil.logger.info("<buildMyUser({})>", userName);
		StopWatch stopWatch_buildMyUser = new Log4JStopWatch("buildMyUser", userName);
		
		MyUser myUser;
		
		if (search.fromTwitter()) {
			
			// LogUtil.logger.info("<showUser({})>", userName);
			StopWatch stopWatch_showUser = new Log4JStopWatch("showUser", userName);	
			User user = TwitterClient.apiLocal().showUser(userName); 
			stopWatch_showUser.stop();
			// LogUtil.logger.info("</showUser({})>", userName);
			
			// LogUtil.logger.info("<fetchMyTweets({})>", userName);
			int numOfPages = 1; //Math.min(user.getStatusesCount() / TWITTER_TIMELINE_PAGE_SIZE, TWITTER_TIMELINE_MAX_PAGES);
			StopWatch stopWatch_fetchMyTweets = new Log4JStopWatch("fetchMyTweets", userName);
			List<MyTweet> userTweets = fetchMyTweets(userName, numOfPages);
			stopWatch_fetchMyTweets.stop();
			// LogUtil.logger.info("</fetchMyTweets({})>", userName);
			
			// LogUtil.logger.info("<new MyUser({})>", userName);
			StopWatch stopWatch_newMyUser = new Log4JStopWatch("newMyUser", userName);
			myUser = new MyUser(user, userTweets, search);
			//myUser = new MyUser(userName, userTweets, search);//fixme:user
			stopWatch_newMyUser.stop();
			// LogUtil.logger.info("</new MyUser({})>", userName);
			
		} else {
			
			myUser = fetchUserFromDb(userName, search);
		}
		
		myUser.setExtended(isExtended);
		
		stopWatch_buildMyUser.stop();
		// LogUtil.logger.info("</buildMyUser({})>", userName);
		
		return myUser;
		
	}
	
	/**
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public MyUser fetchUserFromDb(String userName, Search search) throws Exception {
		
		MyUser myUser = dao.queryUser(userName, search);
		myUser.setTweets( fetchMyTweetsFromDb(userName, search) );
		// myUser.computeCharacteristics(); //FIXME: comment
		return myUser;
	}
	
	/**
	 * 
	 * @param userName
	 * @return
	 * @throws Exception 
	 */
	private List<MyTweet> fetchMyTweetsFromDb(String userName, Search search) throws Exception {
		return dao.queryTweets(userName, search);
	}

	/**
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public static List<MyTweet> fetchMyTweets(String userName, int numOfPages) throws Exception {
		return MyTweet.toMyTweetsFromStatuses(fetchStatusList(userName, numOfPages));
	}
	
	/**
	 * 
	 * @param userName
	 * @return
	 * @throws TwitterException
	 */
	private static List<Status> fetchStatusList(String userName, int numOfPages) throws TwitterException {

		List<Status> result = new ArrayList<Status>();
		
		for (int i = 0; i < numOfPages; i++) {
			result.addAll(TwitterClient.apiLocal().getUserTimeline(userName, new Paging(i+1, Conf.USER_MAX_TWEETS)));
		}
		return result;
	}
	
	/**
	 * 
	 * @param tweets
	 * @return
	 */
	private static Set<String> getUniqueUserNames(List<MyTweet> myTweets) {
		
		Set<String> users = new TreeSet<String>();
		
		for (MyTweet myTweet: myTweets) {
			users.add(myTweet.getFromUser());
		}
		
		return users;
	}
	
	public void insertResults(List<SearchResult> searchResults, Search search) {
		
		StopWatch stopWatch = new Log4JStopWatch("insertResults", String.valueOf(search.getId()));
		
		dao.insertSearchResult(searchResults, search);
		finish(search);
		
		stopWatch.stop();
	}

	public void finish(Search search) {
		dao.updateSearchOrderStatus(search, Search.SEARCH_STATUS_FINISHED);
	}
	
	public void failed (Search search) {
		dao.updateSearchOrderStatus(search, Search.SEARCH_STATUS_INVALID);
	}
	
	public List<SearchResult> queryResults(Search search) {
		return dao.querySearchResults(search);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Search> findWaitingSearchOrders() {
		return dao.findWaitingSearchOrders();
	}
	
	public semantic.Query findKeywordOrder(int id) {
		return dao.findKeywordOrder(id);
	}
	
	/**
	 * 
	 * @param users
	 */
	public void computeNetworkProperties(Collection<MyUser> users, Search search) {
		
		StopWatch stopWatch = new Log4JStopWatch("computeNetworkProperties", String.valueOf(search.getId()));
		
		DirectedGraph<String, Edge> network = NetworkUtils.toDirectedGraph( dao.findEdges(search) );

		BetweennessCentrality<String, Edge> betweennessCentrality = new BetweennessCentrality<String, Edge>(network);
		ClosenessCentrality<String, Edge> closenessCentrality = new ClosenessCentrality<String, Edge>(network);
		// EigenvectorCentrality<String, Edge> eigenvectorCentrality = new EigenvectorCentrality<String, Edge>(network);
		
		for (MyUser myUser : users) {
			if (network.containsVertex(myUser.getScreenName())) {
				myUser.initNetworkProperties(network, betweennessCentrality, closenessCentrality);
				dao.insertNetworkProperties(myUser, search);
			}
		}

		search.initNetworkProperties(users);
		dao.insertNetworkProperties(search);
		
		stopWatch.stop();
	}
	
	public List<semantic.Query> findWaitingKeywordOrders() {
		return dao.findWaitingKeywordOrders();
	}

	public void insertRelatedTags(semantic.Query query, String searchTxt, String tag, int weight, int resourceId) {

		if (tag.length()>100) {
			LogUtil.logger.info("Big word size!!!: tag={}, resource={}, searchTxt={}", new Object[] {tag, resourceId, searchTxt});
		} else {
			dao.insertRelatedTags(query, searchTxt, tag, weight, resourceId);
		}
	}
	
	public void updateKeywordOrder(semantic.Query query) {
		dao.updateKeywordOrder(query);
	}
	
	public void updateNewScore(Search search) {
		dao.updateNewScore(search.getId());
	}

	////////////////////////////// <UNUSED_METHODS> /////////////////////////////////////////////////////////
	
	/**
	 * @deprecated Unused method
	 * 
	 * @param myTweets
	 * @param insert
	 * @return
	 * @throws Exception
	 */
	public List<MyUser> queryUsersMT(List<MyTweet> myTweets, Search search) throws Exception {

		// actual users
		Set<String> uniqeUserNames = getUniqueUserNames(myTweets);
		
		int numOfTwitterers;
		//numOfTwitterers = uniqeUserNames.size();
		
		if (Conf.MAX_USERS_INITIAL_ITER==0) {
			numOfTwitterers = uniqeUserNames.size();
		} else {
			numOfTwitterers = Math.min(Conf.MAX_USERS_INITIAL_ITER, uniqeUserNames.size()); //uniqeUserNames.size(); //FIXME
		}
		String[] uniqeUserNamesArr = findSubSet(uniqeUserNames, numOfTwitterers); //FIXME: comment out! 
		
		List<MyUser> myUsers = new ArrayList<MyUser>(numOfTwitterers);

		Thread[] threads = new Thread[numOfTwitterers];
		
		List<UserThread> userThreads = new ArrayList<UserThread>(numOfTwitterers);

		// for (String userName : uniqeUserNames) {
		for (int i=0; i<numOfTwitterers; i++) {

			String name = uniqeUserNamesArr[i];
			LogUtil.logger.info("start of getting tweets for {}", name);
			
			try {
				
				 //threads.add(new UserThread(userName, search, false));
				
				UserThread userThread = new UserThread(name, search, false, i, this, Search.SEARCH_TYPE_USER);
				userThreads.add(userThread);
				
				Thread thread = new Thread(userThread);
				thread.start();
				threads[i] = thread;
				
			} catch (Exception e) {
				LogUtil.logger.error("failed", e);
			}
			
			LogUtil.logger.info("end of getting tweets for {}", name);
		}
		
		for (Thread thread : threads) {
			thread.join();
		}
		
		MyUser myUser;
		for (UserThread userThread : userThreads) {
			myUser = userThread.getMyUser();
			if (myUser == null) {
				LogUtil.logger.info("MyUser is null: {}", userThread.getUserName());
			} else {
				myUsers.add(myUser);
			}
		}

		return myUsers;
	}

	/**
	 * @deprecated Unused method
	 */
	public List<MyUser> queryExtendedUsersMT(List<MyUser> referenceUsers, Search search, int extendedUserIteration) throws Exception {

		List<MyUser> myUsers = new ArrayList<MyUser>();
		
		if (referenceUsers.size() > 0) {
			
			Set<String> relatedUserNames = findRelatedUserNames(referenceUsers, search);
			
			int numOfTwitterers;
			if (Conf.EXTENDED_USERS_USERS_PER_ITER==0) {
				numOfTwitterers = relatedUserNames.size();
			} else {
				numOfTwitterers = Math.min(Conf.EXTENDED_USERS_USERS_PER_ITER, relatedUserNames.size());
			}
			LogUtil.logger.info("iter={}, relatedUserNames={}, reducedTo={}", new Object[]{(extendedUserIteration-1), relatedUserNames.size(), numOfTwitterers});
			String[] uniqeUserNamesArr = findSubSet(relatedUserNames, numOfTwitterers); //FIXME: comment out!
			
			Thread[] threads = new Thread[numOfTwitterers];
			List<UserThread> userThreads = new ArrayList<UserThread>(numOfTwitterers );
			
			// int threadIndex = 0;
			// for (String userName: relatedUserNames ) {
			for (int i=0; i<numOfTwitterers; i++) {
				
				String name = uniqeUserNamesArr[i];
				LogUtil.logger.info("start of getting tweets for {}", name);
				
				try {
					
					 //threads.add(new UserThread(userName, search, false));
					
					UserThread userThread = new UserThread(name, search, true, i, this, extendedUserIteration);
					userThreads.add(userThread);
					
					Thread thread = new Thread(userThread);
					thread.start();
					threads[i] = thread;
					
				} catch (Exception e) {
					LogUtil.logger.error("failed", e);
					LogUtil.logger.error("ex for {}: {}", name, e.toString());
				}
				
				LogUtil.logger.info("end of getting tweets for {}", name);
			}
			
			for (Thread thread : threads) {
				thread.join();
			}
			
			MyUser myUser;
			for (UserThread userThread : userThreads) {
				myUser = userThread.getMyUser();
				
				if (myUser == null) {
					LogUtil.logger.info("MyUser is null: {}", userThread.getUserName());
				} else {
					myUsers.add(myUser);
				}
			}
		}
		
		return myUsers;
	}

	/**
	 * @deprecated Unused field
	 */
	public static final int TWITTER_TIMELINE_MAX_PAGES = 2;
	/**
	 * @deprecated Unused field
	 * 
	 * CAUTION: do not change, it is a twitter api standard.
	 */
	public static final int TWITTER_TIMELINE_PAGE_SIZE = 20;
	
	//////////////////////////////</UNUSED_METHODS> /////////////////////////////////////////////////////////
}
