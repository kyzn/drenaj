package similarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.Bag;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import search.Search;
import twitter.MyUser;
import util.LogUtil;
import util.Util;

/**
 * Based on net.sf.jtmt.similarity.matrix.Searcher of Sujit Pal
 */
public class Searcher {

	private RealMatrix termDocumentMatrix;
	private List<String> documents;
	private List<String> terms;
	private AbstractSimilarity similarity;

	private List<MyUser> users;
	
	public Searcher(List<MyUser> users) {
		this.users = users;
	}
	
	public void setTermDocumentMatrix(RealMatrix termDocumentMatrix) {
		this.termDocumentMatrix = termDocumentMatrix;
	}

	public void setDocuments(String[] documents) {
		this.documents = Arrays.asList(documents);
	}

	public void setTerms(String[] terms) {
		this.terms = Arrays.asList(terms);
	}

	public void setSimilarity(AbstractSimilarity similarity) {
		this.similarity = similarity;
	}

	/**
	 * 
	 * @param query
	 * @return
	 * @throws Exception 
	 */
	public List<SearchResult> search(Search search) throws Exception {

		Bag<String> queryBag = search.getCooccuringTags();
		
		if (queryBag.size() == 0) {
			
			List<String> queryTerms = search.getQueryTerms();
			
			for (String elem: queryTerms) {
				queryBag.add(elem, Search.RELATED_TAGS_FINDER.maxItems());
			}
			
			if (queryTerms.size()>1) {
				queryBag.add( Util.asHashtag(search.getQuery()).substring(1), Search.RELATED_TAGS_FINDER.maxItems());
			}
			
			StringBuilder sb = new StringBuilder(); 
			sb.append("No related keywords for search=").append(search.getId()).append(". Constructing query terms vector with: "); 
			for (String string : queryBag.uniqueSet()) {
				sb.append(string).append(": ").append(queryBag.getCount(string)).append(", ");
			}
			LogUtil.logger.info(sb.toString());
			
		}
		
		// LogUtil.logger.info("<search(search={}, {})>", search.getId(), StringUtils.join(queryBag, Util.DELIMITER_SPACE_STR));
		
		// build up query matrix
		

		String jointQueryTerms = Util.asHashtag(search.getQuery()).substring(1);
		if (queryBag.getCount(jointQueryTerms) != Search.RELATED_TAGS_FINDER.maxItems()) {
			queryBag.remove(jointQueryTerms);
			queryBag.add(jointQueryTerms, queryBag.getCount(search.getQueryTerms().get(0)));
		}
		
		RealMatrix queryMatrix = constructQueryMatrix(queryBag, search);

		final Map<String, Double> similarityMap = new HashMap<String, Double>();
		
		// LogUtil.logger.info("<computeSimilaritiesForQueryMatrix(search={})>", search.getId());
		StopWatch stopWatch_computeSimilaritiesForQueryMatrix = new Log4JStopWatch("rank.computeSimilaritiesForQueryMatrix", String.valueOf(search.getId()));
		
		for (int docId = 0; docId < termDocumentMatrix.getColumnDimension(); docId++) {

			double sim = similarity.computeSimilarity(queryMatrix, termDocumentMatrix.getSubMatrix(0, termDocumentMatrix.getRowDimension() - 1, docId, docId));

			if (sim > 0.0D) {
				similarityMap.put(documents.get(docId), sim);
			} else {
				// System.out.println("document similarity is negative for docId " + docId + ", " + sim);
			}
		}
		stopWatch_computeSimilaritiesForQueryMatrix.stop();
		// LogUtil.logger.info("</computeSimilaritiesForQueryMatrix(search={})>", search.getId());
		
		// LogUtil.logger.info("<sortByScore(search={})>", search.getId());
		StopWatch stopWatch_sortByScore = new Log4JStopWatch("rank.sortByScore", String.valueOf(search.getId()));
		List<SearchResult> sortedResults = sortByScore(similarityMap);
		stopWatch_sortByScore.stop();
		// LogUtil.logger.info("</sortByScore(search={})>", search.getId());

		// LogUtil.logger.info("</search(search={})>", search.getId());
		
		return sortedResults;
	}

	/**
	 * 
	 * @param search 
	 * @param query
	 * @return
	 */
	private RealMatrix constructQueryMatrix(Bag<String> queryBag, Search search) {

		// LogUtil.logger.info("<constructQueryMatrix(search={})>", search.getId());
		
		
		StringBuilder sb = new StringBuilder("queryMatrixBag={");
		for (String item : queryBag.uniqueSet()) {
			sb.append(item).append(":").append(queryBag.getCount(item)).append(", ");
		}
		sb.append("}");
		LogUtil.logger.info("search={}, {}", search.getId(), sb.toString());
		
		StopWatch stopWatch = new Log4JStopWatch("rank.constructQueryMatrix", String.valueOf(search.getId()));
		
		RealMatrix queryMatrix = new OpenMapRealMatrix(terms.size(), 1);

		//insertQueryTerms(queryMatrix, query.split("\\s+"));
		insertQueryTerms(queryMatrix, queryBag);
		
		queryMatrix = queryMatrix.scalarMultiply(1 / queryMatrix.getNorm()); // http://mathworld.wolfram.com/MaximumAbsoluteRowSumNorm.html
		
		stopWatch.stop();
		// LogUtil.logger.info("</constructQueryMatrix(search={})>", search.getId());
		
		/*
		sb = new StringBuilder("queryMatrix={");
		for (int i = 0; i < queryMatrix.getRowDimension(); i++) {
		   .getEntry(i, j);
		}
		*/
		
		return queryMatrix;
	}

	/**
	 * 
	 * @param queryMatrix
	 * @param queryTerms
	 */
	private void insertQueryTerms(RealMatrix queryMatrix, Bag<String> queryBag) {

		for (String queryTerm : queryBag) {
			
			int termIdx = 0;
			
			for (String term : terms) {
				if (queryTerm.equalsIgnoreCase(term)) {
					queryMatrix.setEntry(termIdx, 0, queryMatrix.getEntry(termIdx, 0) + 1.0D);
				}
				termIdx++;
			}
		}
	}

	/**
	 * 
	 * @param similarityMap
	 * @return
	 * @throws Exception 
	 */
	private List<SearchResult> sortByScore(final Map<String, Double> similarityMap) throws Exception {
		
		List<SearchResult> results = new ArrayList<SearchResult>();
		
		List<String> docNames = new ArrayList<String>();
		docNames.addAll(similarityMap.keySet());
		
		Collections.sort(docNames, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return similarityMap.get(s2).compareTo(similarityMap.get(s1));
			}
		});
		
		for (String docName : docNames) {
			double score = similarityMap.get(docName);
			if (score < 0.00001D) {
				continue;
			}
			
			MyUser myUser = null;
			for (MyUser user : users) {
				if (user.getScreenName().equals(docName)) {
					myUser = user;
					break;
				}
			}
			
			results.add(new SearchResult(myUser, score));
		}
		
		return results;
	}
}
