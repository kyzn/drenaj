package direnaj.functionalities.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import org.json.JSONObject;

import direnaj.domain.User;
import direnaj.driver.DirenajDriver;
import direnaj.driver.DirenajDriverUtils;

public class GraphUtil {

    public static DirenajGraph<User> formUserRelationsGraph(String direnajId, String pass, String campaignID, int skip,
            int limit, List<Relations> relationsDemanded4Graph, TreeMap<User, Vector<String>> vertexObjectMapping) {
        try {
            // form graph
            DirenajGraph<User> direnajGraph = new DirenajGraph<User>();
            // get tweet data
            DirenajDriver direnajDriver = new DirenajDriver(direnajId, pass);
            ArrayList<JSONObject> allTweets = direnajDriver.collectTweets(campaignID, skip, limit);
            for (Iterator<JSONObject> iterator = allTweets.iterator(); iterator.hasNext();) {
                JSONObject tweetData = (JSONObject) iterator.next();
                JSONObject tweet = DirenajDriverUtils.getTweet(tweetData);
                User postedUser = DirenajDriverUtils.parseUser(tweet);
                String userPost = DirenajDriverUtils.getSingleTweetText(tweetData);
                addPost4User(vertexObjectMapping, postedUser, userPost);
                // add user to graph
                direnajGraph.add2Graph(postedUser);
                if (relationsDemanded4Graph.isEmpty() || relationsDemanded4Graph.contains(Relations.Mentions)) {
                    // add mentioned users to graph
                    List<User> userMentionsList = DirenajDriverUtils.getUserMentionsList(DirenajDriverUtils
                            .getEntities(tweet));
                    for (Iterator<User> mentionedUsers = userMentionsList.iterator(); mentionedUsers.hasNext();) {
                        User mentionedUser = (User) mentionedUsers.next();
                        direnajGraph.add2Graph(mentionedUser);
                        direnajGraph.addEdge2Graph(postedUser, mentionedUser, Relations.Mentions);

                    }
                }
                // retweet relation & vector is added to Graph
                if (relationsDemanded4Graph.isEmpty() || relationsDemanded4Graph.contains(Relations.Retweets)) {
                    User retweetedUser = DirenajDriverUtils.getRetweetedUser(tweet);
                    addPost4User(vertexObjectMapping, retweetedUser, userPost);
                    direnajGraph.add2Graph(retweetedUser);
                    direnajGraph.addEdge2Graph(postedUser, retweetedUser, Relations.Retweets);
                }
                // reply relation & vector is added to Graph
                if (relationsDemanded4Graph.isEmpty() || relationsDemanded4Graph.contains(Relations.Replies)) {
                    User replyUser = DirenajDriverUtils.getRepliedUser(tweet);
                    direnajGraph.add2Graph(replyUser);
                    direnajGraph.addEdge2Graph(postedUser, replyUser, Relations.Replies);
                }
            }
            return direnajGraph;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void addPost4User(TreeMap<User, Vector<String>> vertexObjectMapping, User postedUser, String userPost) {
        if (vertexObjectMapping != null && postedUser != null) {
            Vector<String> posts;
            if (vertexObjectMapping.containsKey(postedUser)) {
                posts = vertexObjectMapping.get(postedUser);
            } else {
                posts = new Vector<String>();
            }
            posts.add(userPost);
            vertexObjectMapping.put(postedUser, posts);
        }
    }
}