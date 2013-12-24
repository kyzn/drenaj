package direnaj.domain;

import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import direnaj.functionalities.graph.DirenajGraph;

public class Community {

    private Vector<User> usersInCommunity;
    private double fractionOfEdges;
    private double allEdgeCountInNetwork;
    private DirenajGraph<User> userGraph;

    public Community(User user) {
        usersInCommunity = new Vector<User>();
        usersInCommunity.add(user);
    }

    public Community(User user, double communityEdgeFraction, double networkEdgeCount, DirenajGraph<User> userGraph) {
        usersInCommunity = new Vector<User>();
        usersInCommunity.add(user);
        fractionOfEdges = communityEdgeFraction;
        allEdgeCountInNetwork = networkEdgeCount;
        this.userGraph = userGraph;
    }

    public Vector<User> getUsersInCommunity() {
        return usersInCommunity;
    }

    public boolean addUsersToCommunity(List<User> users) {
        return usersInCommunity.addAll(users);
    }

    public double getFractionOfEdges() {
        return fractionOfEdges;
    }

    public void setFractionOfEdges(double fractionOfEdges) {
        this.fractionOfEdges = fractionOfEdges;
    }

    public void calculateNewFraction() {
        double degrees = 0d;
        for (User communityUser : usersInCommunity) {
            degrees += userGraph.getVertexDegree(communityUser);
        }
        double fraction = degrees / (2d * allEdgeCountInNetwork);
        fractionOfEdges = fraction;
    }

    @Override
    public String toString() {
        String users = "";
        for (User user : usersInCommunity) {
            users += user.getUserScreenName() + " , ";
        }
        return users;
    }

    public void retrivePostOfUsersInCommunity(TreeMap<User, Vector<String>> vertexObjectMapping) {
        for (User user : usersInCommunity) {
            Vector<String> posts = vertexObjectMapping.get(user);
            user.addPostsToUser(posts);
        }
    }
}
