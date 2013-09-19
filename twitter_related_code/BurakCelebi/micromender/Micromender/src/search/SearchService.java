package search;

import java.util.Collection;
import java.util.List;

import similarity.SearchResult;
import twitter.MyTweet;
import twitter.MyUser;

public interface SearchService {
	
	public List<Search> findWaitingSearchOrders();
	
	public List<MyTweet> search(Search search) throws Exception;
	
	public List<MyUser> queryUsers(List<MyTweet> myTweets, Search search) throws Exception;
	
	/**
	 * @deprecated 
	 */
	public List<MyUser> queryUsersMT(List<MyTweet> myTweets, Search search) throws Exception;
	
	public List<MyUser> queryExtendedUsers(List<MyUser> users, Search search, int extendedUserIteration) throws Exception;
	
	/**
	 * @deprecated 
	 */
	public List<MyUser> queryExtendedUsersMT(List<MyUser> referenceUsers, Search search, int extendedUserIteration) throws Exception;

	public MyUser fetchUserFromDb(String userName, Search search) throws Exception;
	
	public void insertResults(List<SearchResult> searchResults, Search search);
	
	public List<SearchResult> queryResults(Search search);
	
	public void computeNetworkProperties(Collection<MyUser> users, Search search);
	
	public void failed (Search search);
	
	public void finish(Search search);
	
	public List<semantic.Query> findWaitingKeywordOrders();

	public semantic.Query findKeywordOrder(int id);
	
	public void insertRelatedTags(semantic.Query query, String searchTxt, String tag, int weight, int resourceId);
	
	public void updateKeywordOrder(semantic.Query query);
	
	public void updateNewScore(Search search);
	
}