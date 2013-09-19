package indexer;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.math.linear.RealMatrix;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import search.Search;
import similarity.AbstractSimilarity;
import similarity.CosineSimilarity;
import similarity.JaccardSimilarity;
import similarity.SearchResult;
import similarity.Searcher;
import twitter.MyUser;
import util.LogUtil;

public class IndexerUtil {

	/**
	 * 
	 * @param search
	 * @param users
	 * @return
	 * @throws Exception
	 */
	public static List<SearchResult> searchTfIdfVectorWithJaccard(List<MyUser> users, UserVectors userVectors, Search search) throws Exception {
		return search(users, userVectors, search, new IdfIndexer(), new JaccardSimilarity());
	}

	/**
	 * 
	 * @param search
	 * @param users
	 * @return
	 * @throws Exception
	 */
	public static List<SearchResult> searchTfIdfVectorWithCosine(List<MyUser> users, UserVectors userVectors, Search search) throws Exception {
		return search(users, userVectors, search, new IdfIndexer(), new CosineSimilarity());
	}
	
	/**
	 * 
	 * @param search
	 * @param users
	 * @return
	 * @throws Exception
	 */
	public static List<SearchResult> searchTfVectorWithCosine(List<MyUser> users, UserVectors userVectors, Search search) throws Exception {
		return search(users, userVectors, search, new TfIndexer(), new CosineSimilarity());
	}

	/**
	 * 
	 * @param users
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public static List<SearchResult> searchLsiVectorWithCosine(List<MyUser> users, UserVectors userVectors, Search search) throws Exception {
		return search(users, userVectors, search, new LsiIndexer(), new CosineSimilarity());
	}
	
	/**
	 * 
	 * @param vectorGenerator
	 * @param query
	 * @throws Exception
	 */
	private static List<SearchResult> search(List<MyUser> users, UserVectors userVectors, Search search, Transformer<RealMatrix,RealMatrix> indexer, AbstractSimilarity similarity) throws Exception {
		
		// generate the term document matrix via the appropriate indexer
		// IdfIndexer indexer = new IdfIndexer();
		StopWatch stopWatch_indexer = new Log4JStopWatch("rank.indexer");
		RealMatrix termDocMatrix = indexer.transform(userVectors.getMatrix());
		stopWatch_indexer.stop();
		
		// set up the query
		Searcher searcher = new Searcher(users);
		searcher.setDocuments(userVectors.getDocumentNames());
		searcher.setTerms(userVectors.getWords());
		searcher.setSimilarity(similarity);
		searcher.setTermDocumentMatrix(termDocMatrix);

		// run the query
		List<SearchResult> results = searcher.search(search); //FIXME query terms?.
		
		search.setFinishTime(new Date());
		
		return results;
	}

	public static void cosineSimilarityWithTfVector(UserVectors vectorGenerator) throws Exception {

		TfIndexer indexer = new TfIndexer();
		RealMatrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());

		CosineSimilarity cosineSimilarity = new CosineSimilarity();
		RealMatrix similarity = cosineSimilarity.transform(termDocMatrix);

		prettyPrintMatrix("Cosine Similarity (TF)", similarity, vectorGenerator.getDocumentNames(), new PrintWriter(System.out, true));
	}

	
	/**
	 * 
	 * @param vectorGenerator
	 * @param search 
	 * @throws Exception
	 */
	public static void cosineSimilarityWithTfIdfVector(UserVectors vectorGenerator, Search search) throws Exception {

		StopWatch stopWatch = new Log4JStopWatch("userSimilarities", String.valueOf(search.getId()));
		
		IdfIndexer indexer = new IdfIndexer();
		RealMatrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());

		CosineSimilarity cosineSimilarity = new CosineSimilarity();
		RealMatrix similarity = cosineSimilarity.transform(termDocMatrix);
		
		stopWatch.stop();

		StopWatch stopWatch_log = new Log4JStopWatch("userSimilarities-log", String.valueOf(search.getId()));
		
		String matrixStr = buildDelimitedMatrixStr(similarity, vectorGenerator.getDocumentNames(), "|");
		
		LogUtil.logger.info("Cosine Similarity (TF/IDF) for search={}\n{}", search.getId(), matrixStr);
		
		stopWatch_log.stop();
		
		// prettyPrintMatrix("Cosine Similarity (TF/IDF)", similarity, vectorGenerator.getDocumentNames(), new PrintWriter(System.out, true));
	}
	
	private static String buildDelimitedMatrixStr(RealMatrix matrix, String[] documentNames, String delimiter) {
		
		StringBuilder builder = new StringBuilder("users");
		
		for (int i = 0; i < documentNames.length; i++) {
			builder.append(delimiter).append(documentNames[i]);
		}
		builder.append("\n");
		
		for (int i = 0; i < documentNames.length; i++) {
			builder.append(documentNames[i]);
			for (int j = 0; j < documentNames.length; j++) {
				builder.append(delimiter).append(matrix.getEntry(i, j));
			}
			builder.append("\n");
		}
		
		return builder.toString();
	}

	/**
	 * 
	 * @param vectorGenerator
	 * @throws Exception
	 */
	public static void cosineSimilarityWithLsiVector(UserVectors vectorGenerator) throws Exception {

		LsiIndexer indexer = new LsiIndexer();
		RealMatrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());

		CosineSimilarity cosineSimilarity = new CosineSimilarity();
		RealMatrix similarity = cosineSimilarity.transform(termDocMatrix);

		prettyPrintMatrix("Cosine Similarity (LSI)", similarity, vectorGenerator.getDocumentNames(), new PrintWriter(System.out, true));
	}

	/**
	 * 
	 * @param vectorGenerator
	 * @throws Exception
	 */
	public static void tfIndexer(UserVectors vectorGenerator) throws Exception {

		TfIndexer indexer = new TfIndexer();
		RealMatrix tfMatrix = indexer.transform(vectorGenerator.getMatrix());

		prettyPrintMatrix("Term Frequency", tfMatrix, vectorGenerator.getDocumentNames(), vectorGenerator.getWords(), new PrintWriter(System.out, true));
	}

	/**
	 * 
	 * @param vectorGenerator
	 * @throws Exception
	 */
	public static void idfIndexer(UserVectors vectorGenerator) throws Exception {

		IdfIndexer indexer = new IdfIndexer();
		RealMatrix idfMatrix = indexer.transform(vectorGenerator.getMatrix());

		prettyPrintMatrix("Inverse Document Frequency", idfMatrix, vectorGenerator.getDocumentNames(), vectorGenerator.getWords(), new PrintWriter(System.out, true));
	}

	/**
	 * 
	 * @param vectorGenerator
	 * @throws Exception
	 */
	public static void lsiIndexer(UserVectors vectorGenerator) throws Exception {

		LsiIndexer indexer = new LsiIndexer();
		RealMatrix lsiMatrix = indexer.transform(vectorGenerator.getMatrix());

		prettyPrintMatrix("Latent Semantic (LSI)", lsiMatrix, vectorGenerator.getDocumentNames(), vectorGenerator.getWords(), new PrintWriter(System.out, true));
	}

	/**
	 * 
	 * @param legend
	 * @param matrix
	 * @param documentNames
	 * @param words
	 * @param writer
	 */
	private static void prettyPrintMatrix(String legend, RealMatrix matrix, String[] documentNames, String[] words, PrintWriter writer) {

		writer.printf("=== %s ===%n", legend);
		writer.printf("%30s", " ");
		for (int i = 0; i < documentNames.length; i++) {
			writer.printf("%20s", documentNames[i]);
		}
		writer.println();
		for (int i = 0; i < words.length; i++) {
			writer.printf("%30s", words[i]);
			for (int j = 0; j < documentNames.length; j++) {
				writer.printf("%20.4f", matrix.getEntry(i, j));
			}
			writer.println();
		}
		writer.flush();
	}

	/**
	 * 
	 * @param vectorGenerator
	 */
	public static void occurences(UserVectors vectorGenerator) {
		prettyPrintMatrix("Occurences", vectorGenerator.getMatrix(), vectorGenerator.getDocumentNames(), vectorGenerator.getWords(), new PrintWriter(System.out, true));
	}

	/**
	 * 
	 * @param legend
	 * @param matrix
	 * @param documentNames
	 * @param writer
	 */
	private static void prettyPrintMatrix(String legend, RealMatrix matrix, String[] documentNames, PrintWriter writer) {

		writer.printf("=== %s ===%n", legend);
		writer.printf("%30s", " ");
		for (int i = 0; i < documentNames.length; i++) {
		}
		writer.println();
		for (int i = 0; i < documentNames.length; i++) {
			writer.printf("%30s", documentNames[i]);
			for (int j = 0; j < documentNames.length; j++) {
				writer.printf("%20.4f", matrix.getEntry(i, j));
			}
			writer.println();
		}
		writer.flush();
	}

	/**
	 * 
	 * @param query
	 * @param results
	 */
	public static void prettyPrintResults(Search search, List<SearchResult> results) {

		LogUtil.logger.info("");

		for (SearchResult result : results) {
			LogUtil.logger.info(result.toString(search));
		}
	}

}
