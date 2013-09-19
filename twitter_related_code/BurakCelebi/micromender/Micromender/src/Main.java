import indexer.IndexerUtil;
import indexer.UserVectors;

import java.util.List;

import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import search.Conf;
import search.Search;
import search.SearchService;
import semantic.DeliciousClient;
import semantic.Query;
import semantic.RelatedKeywordsFinder;
import similarity.SearchResult;
import twitter.MyTweet;
import twitter.MyUser;
import twitter.TwitterClient;
import util.AppContextUtil;
import util.LogUtil;

public class Main {
	
	private static final String LOG_ORDERS_FINISHED = "All search orders processed... Waiting for new ones..";
	
	/**
	 * 1. Search'te buldugun kullanicilarin mention ettigi, RT ettigi kullanicilari da al. Set'i genişlet! t_extented_users (maybe v_?).
	 * 2. Hashtag'leri nasıl degerlendirirsin, onu dusun.
	 * 3. Spammer'ları nasıl tespit ederiz?
	 * 4. How to deal with disambiguation?
	 * 
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		// Thread.sleep(3 * 60 * 60 * 1000);
		
		Search.RELATED_TAGS_FINDER = DeliciousClient.INSTANCE;
		// Conf.LOG_ENRICHMENT = true;
		
		SearchService searchService = AppContextUtil.getSearchService();
		
		// while(true) {
			
			for (Search searchOrder : searchService.findWaitingSearchOrders()) {

				int remainingHits = TwitterClient.apiLocal().getRateLimitStatus().getRemainingHits();
				if (remainingHits <= 1) { //if (remainingHits < 350) {
					long milisecondsUntilReset = TwitterClient.apiLocal().getRateLimitStatus().getSecondsUntilReset()*1000;
					LogUtil.logger.info("remainingHits={}, waiting {} ms for reset..", remainingHits, milisecondsUntilReset );
					Thread.sleep(milisecondsUntilReset);
				}
				
				try {
					handleSemanticSearch(searchOrder, searchService);
					search(searchOrder, searchService);
				} catch (Exception e) {
					searchService.failed(searchOrder);
					LogUtil.logger.error("recommendation failed", e);
					throw e;
				}
			}
			
			System.out.println(LOG_ORDERS_FINISHED);
			LogUtil.logger.info(LOG_ORDERS_FINISHED);
			
			// Thread.sleep(10000);
		// }
		
		
		/*
		Search search = new Search(26);
		search.setReCalculate(true);
		search(search, searchService);
		*/
	}

	private static void handleSemanticSearch(Search searchOrder, SearchService searchService) throws Exception {
		
		Query semanticQuery = searchOrder.getSemanticQuery();
		
		if (semanticQuery != null) {
			
			searchOrder.setSemanticQuery( searchService.findKeywordOrder(semanticQuery.getId()) );
			
 			RelatedKeywordsFinder.process(searchOrder.getSemanticQuery());
			searchOrder.initRelatedTags();
		}
	}
	
	private static void search(Search search, SearchService searchService) throws Exception {

		List<SearchResult> searchResults;
		
		if (search.fromTwitter() || search.isReCalculate()) {
			
			// LogUtil.logger.info("conf=[{}], search={}", Conf.fetchInfo(), search.getId());
			
			// search tweets
			List<MyTweet> myTweets = searchService.search(search);
			
			// fetch user tweets and characterize
			// long startMs = System.currentTimeMillis();
			List<MyUser> users = searchService.queryUsers(myTweets, search);
			// LogUtil.logger.info("search={}, queryUsers={} min", search.getId(), String.valueOf( (System.currentTimeMillis()-startMs)/60000 ));
			
			boolean noUsersFound = (users.size() == 0);
			if (noUsersFound) {
				// search.setFinishTime(new Date());
				searchService.finish(search);
				LogUtil.logger.info("Skipping search={}, no users found via search.", search.getId());
				return; 
			}
			
			// LogUtil.logger.info("<rank-initial(search={})>", search.getId());
			searchResults = rank(users, new UserVectors(users), search);
			// LogUtil.logger.info("</rank-initial(search={})>", search.getId());
			// IndexerUtil.prettyPrintResults(search, searchResults);
			
			// startMs = System.currentTimeMillis();
			
			for (int i = 0; i < Conf.EXTENDED_USERS_ITER; i++) {
				
				LogUtil.logger.info("search={}, start of iteration={}", search.getId(), (i+2));
				
				List<MyUser> extendedUsers = null;
				
				int topUserIter = 1; boolean lastTry = false;
				while ((extendedUsers == null || extendedUsers.size()==0) && !lastTry) {
					
					int topUsers = Conf.EXTENDED_USERS_REF_USERS * (topUserIter++);
					if (topUsers>searchResults.size()) {
						topUsers = searchResults.size();
						lastTry = true;
					}
					
					extendedUsers = searchService.queryExtendedUsers(SearchResult.findTopUsers(searchResults,  topUsers), search, i+3);
					users.addAll(extendedUsers);
				}
				
				if (extendedUsers == null || extendedUsers.size()==0 ) {
					LogUtil.logger.info("search={}, no reference user found!", search.getId());
					break;
				} else {
					/*
					// rank users
					searchResults = rank(extendedUsers, new UserVectors(extendedUsers), search);
					IndexerUtil.prettyPrintResults(search, searchResults);
					*/
				}
				
				LogUtil.logger.info("search={}, end of iteration={}", search.getId(), (i+2));
			}
			
			// LogUtil.logger.info("<new UserVectors(search={})>", search.getId());
			UserVectors userVectors = new UserVectors(users);
			// LogUtil.logger.info("<new UserVectors(search={})>", search.getId());
			
			// LogUtil.logger.info("<rank(search={})>", search.getId());
			searchResults = rank(users, userVectors, search);
			// LogUtil.logger.info("</rank(search={})>", search.getId());
			
			// LogUtil.logger.info("<insertResults(search={})>", search.getId());
			searchService.insertResults(searchResults, search);
			// LogUtil.logger.info("</insertResults(search={})>", search.getId());
			
			searchService.updateNewScore(search);
			
			// LogUtil.logger.info("<computeNetworkProperties(search={})>", search.getId());			
			searchService.computeNetworkProperties(users, search);
			// LogUtil.logger.info("</computeNetworkProperties(search={})>", search.getId());
			
			// LogUtil.logger.info("<cosineSimilarityWithTfIdfVector(search={})>", search.getId());
			IndexerUtil.cosineSimilarityWithTfIdfVector(userVectors, search);
			// LogUtil.logger.info("</cosineSimilarityWithTfIdfVector(search={})>", search.getId());
			
			// LogUtil.logger.info("TF-IDF Result for search={}", search.getId());
			// IndexerUtil.prettyPrintResults(search, searchResults);
			
			// ======================================================== //
			
			/*
			List<SearchResult> searchResultsIdf = IndexerUtil.searchTfVectorWithCosine(users, userVectors, search);
			LogUtil.logger.info("TF Result for search={}", search.getId());
			IndexerUtil.prettyPrintResults(search, searchResultsIdf);
			*/
			
			
		} else {
			searchResults = searchService.queryResults(search);
		}
		
		// IndexerUtil.prettyPrintResults(search, searchResults);
	}

	private static List<SearchResult> rank(List<MyUser> users, UserVectors userVectors, Search search) throws Exception {
		
		StopWatch stopWatch_rank = new Log4JStopWatch("rank");
		List<SearchResult> result = IndexerUtil.searchTfIdfVectorWithCosine(users, userVectors, search);
		stopWatch_rank.stop();
		
		return result;
	}
}
