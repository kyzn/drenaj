package direnaj.functionalities.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import direnaj.adapter.DirenajInvalidJSONException;
import direnaj.domain.User;
import direnaj.driver.DirenajDriver;
import direnaj.driver.DirenajDriverUtils;

public class GraphUtil {

    public static DirenajGraph<User> formUserRelationsGraph(String direnajId, String pass, String campaignID, int skip,
            int limit) {
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
                // add user to graph
                direnajGraph.add2Graph(postedUser);
                // add mentioned users to graph
                List<User> userMentionsList = DirenajDriverUtils.getUserMentionsList(DirenajDriverUtils
                        .getEntities(tweet));
                for (Iterator<User> mentionedUsers = userMentionsList.iterator(); mentionedUsers.hasNext();) {
                    User mentionedUser = (User) mentionedUsers.next();
                    direnajGraph.add2Graph(mentionedUser);
                    direnajGraph.addEdge2Graph(postedUser, mentionedUser, Relations.Mentions);

                }
                // add reply and retweet users to graph
                User replyUser = DirenajDriverUtils.getRepliedUser(tweet);
                User retweetedUser = DirenajDriverUtils.getRetweetedUser(tweet);
                direnajGraph.add2Graph(retweetedUser);
                direnajGraph.add2Graph(replyUser);
                direnajGraph.addEdge2Graph(postedUser, retweetedUser, Relations.Retweets);
                direnajGraph.addEdge2Graph(postedUser, retweetedUser, Relations.Replies);
            }
            return direnajGraph;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}