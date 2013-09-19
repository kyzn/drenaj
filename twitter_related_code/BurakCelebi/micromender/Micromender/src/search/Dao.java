package search;

import java.util.List;
import java.util.Set;

import network.Edge;
import similarity.SearchResult;
import twitter.MyTweet;
import twitter.MyUser;

public interface Dao {

	public void updateNewScore(int searchId);
	
	public void insertBatch(List<String> list);
	
	public void insertSearch(Search search, List<MyTweet> myTweets) throws Exception;
	
	public List<MyTweet> querySearchResult(Search search) throws Exception;
	
	public MyUser queryUser(String screenName, Search search) throws Exception;
	
	public void insertTweets(MyUser myUser, Search search, int searchType);
	
	/**
	 * @deprecated use {@link #insertTweets(MyUser, Search, int)} 
	 * 
	 */
	public void insertTweet(MyTweet myTweet, Search search, int searchType) throws Exception;
	
	public void insertTweetTokens(MyTweet myTweet) throws Exception;

	public List<MyTweet> queryTweets(String screenName, Search search) throws Exception;

	public void insertUser(MyUser myUser, Search search);

	public void querySearchQuery(Search search);
	
	public List<String> findRelatedUserNames(Search search, Set<String> referenceUserNames);

	public void insertSearchResult(List<SearchResult> searchResults, Search search);

	public List<SearchResult> querySearchResults(Search search);
	
	public List<Search> findWaitingSearchOrders();

	public void updateSearchOrderStatus(Search search, int status);

	public List<Edge> findEdges(Search search);
	
	public void insertNetworkProperties(MyUser myUser, Search search);
	
	public void insertNetworkProperties(Search search);
	
	public List<semantic.Query> findWaitingKeywordOrders();

	public semantic.Query findKeywordOrder(int id);
	
	public void updateKeywordOrder(semantic.Query query);
	
	public void insertRelatedTags(semantic.Query query, String searchTxt, String tag, int weight, int resourceId);
}
