package test;

import java.util.List;

import org.apache.commons.collections15.Bag;

import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

import network.Edge;
import scala.actors.threadpool.Arrays;
import search.Dao;
import search.Search;
import semantic.SemanticDao;
import twitter.MyUser;
import util.AppContextUtil;

public class TestDao {

	private static Dao dao = AppContextUtil.getDao();
	private static SemanticDao semanticDao = AppContextUtil.getSemanticDao();
	
	
	public static void main(String[] args) {
		// findEdges();
		// insertNetworkProperties_Search();
		// insertNetworkProperties_User();
		// findRedirects();
		//  insertBatch();
		
		int[] ids = {321,322};

		for (int id : ids) {
			updateNewScore(id);
		}
	}
	
	public static void updateNewScore(int searchId) {
		System.out.println("calculating for id=" + searchId);
		dao.updateNewScore(searchId);
	}
	
	@SuppressWarnings("unchecked")
	public static void insertBatch() {
		dao.insertBatch(Arrays.asList(new String[]{"burak", "lorin"}));  
	}
	
	public static void findRedirects() {

		Bag<String> bag = semanticDao.findRedirects("<http://dbpedia.org/resource/Constructed_language>");
		
		for (String string : bag.uniqueSet())  {
			System.out.println(string + ": " + bag.getCount(string));
		}
	}
	
	public static Search buildSearch (int searchId) {

		Search search = new Search(searchId);

		search.setAvgInDegree(3.1);
		search.setAvgOutDegree(3.2);
		search.setAvgBetweenness(3.3);
		search.setAvgCloseness(3.4);
		
		return search;
	}


	public static MyUser buildUser(String screenName) {
		
		MyUser myUser = new MyUser();
		myUser.setScreenName(screenName); 
		
		myUser.setInDegree(1);
        myUser.setOutDegree(2); 
        myUser.setBetweenness(3.3); 
        myUser.setCloseness(4.4);
        
        return myUser;
	}
	
	public static void insertNetworkProperties_Search () {
		dao.insertNetworkProperties(buildSearch(40));
	}
	
	public static void insertNetworkProperties_User () {
		dao.insertNetworkProperties(buildUser("danbri"), buildSearch(40));
	}
	
	public static void findEdges() {
		
		List<Edge> edges = dao.findEdges(buildSearch(40));
		
		System.out.println(edges.size());
		
		for (Edge edge : edges) {
			System.out.println(edge);
		}
		
		
		DirectedGraph<String, Edge> network = new DirectedSparseGraph<String, Edge>();
		
		for (Edge edge : edges) {
			network.addEdge(edge, edge.getFromUser(), edge.getToUser());
		}
		
		System.out.println(network.toString());
		
		BetweennessCentrality<String, Edge> betweennessCentrality = new BetweennessCentrality<String, Edge>(network);
		ClosenessCentrality<String, Edge> closenessCentrality = new ClosenessCentrality<String, Edge>(network);
		EigenvectorCentrality<String, Edge> eigenvectorCentrality = new EigenvectorCentrality<String, Edge>(network);
		
		for (String screenName : network.getVertices()) {
			
			System.out.println("inDegree: " + network.inDegree(screenName));
			System.out.println("outDegree: " + network.outDegree(screenName));
			System.out.println("BetweennessCentrality: " + betweennessCentrality.getVertexScore(screenName));
			System.out.println("ClosenessCentrality: " + closenessCentrality.getVertexScore(screenName));
			System.out.println("EigenvectorCentrality: " + eigenvectorCentrality.getVertexScore(screenName));

		}
		
	}
}
