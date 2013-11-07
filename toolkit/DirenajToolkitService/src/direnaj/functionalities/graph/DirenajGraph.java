package direnaj.functionalities.graph;

import java.awt.Dimension;
import java.util.Collection;

import javax.swing.JFrame;

import direnaj.domain.User;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class DirenajGraph<T> {

    private Graph<T, String> graph;

    /**
     * FIXME istenilen Graph yapýsýna göre, burada parametrik olarak farklý graphlar yaratýlabilir
     */
    public DirenajGraph() {
        graph = new SparseMultigraph<T, String>();
    }

    public void addEdge2Graph(T node, T node2, Relations relation) {
        if (node != null && node2 != null) {
            graph.addEdge(relation + "-" + node.toString() + "-" + node2.toString(), node, node2, EdgeType.DIRECTED);
        }
    }

    public void add2Graph(T node) {
        // add user to graph
        if (node != null && !graph.containsVertex(node)) {
            graph.addVertex(node);
        }
    }

    public Graph<T, String> getJungGraph() {
        return graph;
    }

    public Collection<T> getVertices() {
        return graph.getVertices();
    }

    public Double getInDegree(T node) {
        return (double) graph.inDegree(node);
    }

    public void visualizeGraph() {
        // The Layout<V, E> is parameterized by the vertex and edge types
        Layout<User, String> layout = new KKLayout(graph);

        layout.setSize(new Dimension(1100, 600)); // sets the initial size of the space
        // The BasicVisualizationServer<V,E> is parameterized by the edge types
        BasicVisualizationServer<User, String> vv = new BasicVisualizationServer<User, String>(layout);
        vv.setPreferredSize(new Dimension(1100, 600)); //Sets the viewing area size
        
        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }

}
