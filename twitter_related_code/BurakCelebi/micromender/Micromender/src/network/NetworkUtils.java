package network;

import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

/**
 * 
 */
public final class NetworkUtils {
	
	public static DirectedGraph<String, Edge> toDirectedGraph(List<Edge> edges) {
		
		DirectedGraph<String, Edge> network = new DirectedSparseGraph<String, Edge>();
		
		for (Edge edge : edges) {
			network.addEdge(edge, edge.getFromUser(), edge.getToUser());
		}
		
		return network;
	}

	/**
	 * Preventing construction
	 */
	private NetworkUtils (){
		throw new AssertionError();	
	}
}
