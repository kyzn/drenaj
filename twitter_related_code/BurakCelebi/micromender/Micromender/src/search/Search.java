package search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections15.Bag;
import org.apache.commons.collections15.bag.TreeBag;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import semantic.DbpediaResource;
import semantic.Query;
import semantic.RelatedTagsFinder;
import semantic.TagResourceQueryResult;
import twitter.MyUser;
import util.LogUtil;
import util.MathUtil;
import util.Util;

/**
 * Model corresponding to a recommendation request.    
 */
public class Search {

	public static final int SEARCH_TYPE_SEARCH        = 1,
							SEARCH_TYPE_USER          = 2,
							SEARCH_TYPE_USER_EXTENDED = 3;
	
	static final int SEARCH_STATUS_INVALID       = 0,
					 SEARCH_STATUS_NOT_PROCESSED = 1,
					 SEARCH_STATUS_PROCESSING    = 2,
					 SEARCH_STATUS_FINISHED      = 3;

	
	public static RelatedTagsFinder RELATED_TAGS_FINDER;
	
	private static final int MIN_OCCURRENCE = 3;
	
	private Integer id;
	private Query semanticQuery;

	private String query; //CAUTION: Always set through setQuery(), not by "this.query = ..."
	private List<String> queryTerms;

	private boolean insert;
	private boolean fromDb;
	private boolean reCalculate;
	private boolean insertOrder;
	
	private Date startTime, finishTime;
	
	private Bag<String> cooccuringTags;
	private Bag<String> topCooccuringTags;

	private double avgInDegree, avgOutDegree, avgBetweenness, avgCloseness;
	
	static Search createFromDB(Search search, String query, String topCooccuringTagsSpaceSep, String vectorSearchStr) {
		
		search.setQuery(query);
		
		search.setTopCooccuringTags( Util.toBagFromSpaceDelimeted(topCooccuringTagsSpaceSep) );

		search.setCooccuringTags( Util.toBagFromSpaceDelimeted(vectorSearchStr) );
		
		return search;
	}

	private Search (String query) throws Exception {
		this(query, true);
	}
	
	private Search (String query, boolean insert) throws Exception {
		
		setQuery(query);
		
		this.insert = insert;
		this.fromDb = false;

		enhanceQuery();
	}
	
	public Search (Integer id) {
		
		this(id, false);
	}
	
	private Search (Integer id, boolean insert) {
		
		this.id = id;
		this.insert = insert;
		this.fromDb = true;
	}

	/**
	 * 
	 * @param id
	 * @param query
	 * @throws Exception 
	 */
	Search (Integer id, String query) throws Exception  {
		this(query);
		this.id = id;
	}
	
	Search(int id, String query, String semanticSearchId) {
		
		this.id = id;
		setQuery(query);
		setSemanticQuery(new Query(Integer.valueOf(semanticSearchId)));
		
		this.insert = true;
		this.fromDb = false;
	}

	private void enhanceQuery() throws Exception {

		StopWatch stopWatch = new Log4JStopWatch("findKeywords", String.valueOf(id));
		cooccuringTags = RELATED_TAGS_FINDER.findCooccuringTags(query);
		stopWatch.stop();
		
		topCooccuringTags = findTopCooccuringTags(cooccuringTags);
		
		/*
		StringBuilder builder = new StringBuilder();
		for (String tag : cooccuringTags.uniqueSet()) {
			builder.append(tag).append(":").append(cooccuringTags.getCount(tag)).append(", ");
		}
		LogUtil.logger.info("CooccuringTags= {}", builder.toString());
		
		builder = new StringBuilder();
		for (String tag : topCooccuringTags.uniqueSet()) {
			builder.append(tag).append(":").append(topCooccuringTags.getCount(tag)).append(", ");
		}
		LogUtil.logger.info("TopCooccuringTags= {}", builder.toString());
		*/
		
		/*
		queryEnhanced = Util.joinWithOr(topCooccuringTags);
		vectorSearchStr = StringUtils.join( cooccuringTags, Util.DELIMITER_SPACE); // findRelatedWords(cooccuringTags);
		*/
	}

	public String getQuery() {
		return query;
	}
	
	public Integer getId() {
		return id;
	}
	
	boolean insert() {
		return insert;
	}
	
	boolean fromDb(){
		return fromDb;
	}

	public boolean fromTwitter(){
		return !fromDb;
	}

	public void setQuery(String query) {
		this.query = Util.lowerCase(query);
		this.queryTerms = Arrays.asList(this.query.split("\\s+"));
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Bag<String> getTopCooccuringTags() {
		return topCooccuringTags;
	}

	public Bag<String> getCooccuringTags() {
		return cooccuringTags;
	}

	public void setCooccuringTags(Bag<String> cooccuringTags) {
		this.cooccuringTags = cooccuringTags;
	}

	
	private void setTopCooccuringTags(Bag<String> topCooccuringTags) {
		this.topCooccuringTags = topCooccuringTags;
	}

	public List<String> getQueryTerms() {
		return queryTerms;
	}

	public boolean isInsertOrder() {
		return insertOrder;
	}

	public boolean isReCalculate() {
		return reCalculate;
	}

	public void setReCalculate(boolean reCalculate) {
		this.reCalculate = reCalculate;
	}
	
	
	
	/*
	public String getQueryEnhanced() {
		return queryEnhanced;
	}

	public void setQueryEnhanced(String queryEnhanced) {
		this.queryEnhanced = queryEnhanced;
	}

	public String getVectorSearchStr() {
		return vectorSearchStr;
	}

	public void setVectorSearchStr(String vectorSearchStr) {
		this.vectorSearchStr = vectorSearchStr;
	}
	*/

	
	/*
	public Bag<String> getVectorSearchStrAsBag() {
		
		Bag<String> bag = new TreeBag<String>();
		
		for (String elem: vectorSearchStr.split("\\s+")) {
			bag.add(elem);
		}
		return bag;
	}
	*/
	
	public Date getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public double getAvgInDegree() {
		return avgInDegree;
	}

	public void setAvgInDegree(double avgInDegree) {
		this.avgInDegree = avgInDegree;
	}

	public double getAvgOutDegree() {
		return avgOutDegree;
	}

	public void setAvgOutDegree(double avgOutDegree) {
		this.avgOutDegree = avgOutDegree;
	}

	public double getAvgBetweenness() {
		return avgBetweenness;
	}

	public void setAvgBetweenness(double avgBetweenness) {
		this.avgBetweenness = avgBetweenness;
	}

	public double getAvgCloseness() {
		return avgCloseness;
	}

	public void setAvgCloseness(double avgCloseness) {
		this.avgCloseness = avgCloseness;
	}
	
	public Query getSemanticQuery() {
		return semanticQuery;
	}

	public void setSemanticQuery(Query semanticQuery) {
		this.semanticQuery = semanticQuery;
	}
	
	/**
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	private Bag<String> findTopCooccuringTags(Bag<String> cooccuringTags) throws Exception {
				
		List<String> blackList = new ArrayList<String>(queryTerms); //FIXME
		blackList.add(query);
		blackList.add(Util.asHashtag(query).substring(1));
		
		return Util.findFirstElems(cooccuringTags, Conf.MAX_TOP_COOCCURING_TAGS, blackList);
				//Util.findTopElems(cooccuringTags, Math.max(queryTerms.size()-1, MIN_OCCURRENCE), queryTerms);
	}

	/**
	 * 
	 * @param users
	 */
	void initNetworkProperties(Collection<MyUser> users) {

		double totalInDegree=0.0, totalOutDegree=0.0, totalBetweenness=0.0, totalCloseness=0.0;

		int numberOfUsersInNetwork = 0;
		for (MyUser myUser : users) {
			
			if (myUser.getOutDegree()>0 || myUser.getOutDegree()>0 ) {
				
				totalInDegree += myUser.getInDegree();
				totalOutDegree += myUser.getOutDegree();  
				totalBetweenness += MathUtil.nullAndNanSafeDouble(myUser.getBetweenness());
				totalCloseness += MathUtil.nullAndNanSafeDouble(myUser.getCloseness());
				
				numberOfUsersInNetwork++;
			}
		}
		
		LogUtil.logger.info("numberOfUsersInNetwork={}, search={}", numberOfUsersInNetwork, id);
		
		avgInDegree =  MathUtil.divideZeroSafe(totalInDegree, users.size());
		avgOutDegree = MathUtil.divideZeroSafe(totalOutDegree, users.size());
		avgBetweenness =  MathUtil.divideZeroSafe(totalBetweenness, users.size());
		avgCloseness = MathUtil.divideZeroSafe(totalCloseness, users.size());
	}

	public void initRelatedTags() throws Exception {

		cooccuringTags = new TreeBag<String>();
		
		DbpediaResource dbpediaResource = semanticQuery.getDbpediaResource();
		
		cooccuringTags.addAll(dbpediaResource.getRedirects());
		cooccuringTags.addAll(dbpediaResource.getCategories());
		
		for (TagResourceQueryResult taggingResult : semanticQuery.getTaggingResults()) {

			if (taggingResult.getId() == 1) {
				cooccuringTags.addAll(taggingResult.getTags());
			}
			
			
			
		}
		
		setCooccuringTags(cooccuringTags);
		topCooccuringTags = findTopCooccuringTags(cooccuringTags);
	}
}
