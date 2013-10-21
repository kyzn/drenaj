package direnajDriver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import direnajAdapter.DirenajInvalidJSONException;

public class DirenajDriverUtils {
	
	/* METHODS SUMMARY
	 * 
	 * 
	 * public static JSONObject getEntities(JSONObject tweet)
	 * 
	 * public static JSONArray getHashTags(JSONObject entities)
	 * 
	 * public static JSONArray getUrls(JSONObject entities)
	 * 
	 * public static JSONArray getResults(JSONObject obj)
	 * 
	 * public static JSONObject getTweet(JSONObject tweetData)
	 * 
	 * public static JSONObject getTweetData(JSONArray results, int index)
	 * 
	 * public static String getSingleTweetText(JSONObject tweetData)
	 * 
	 * public static ArrayList<Map.Entry<String, Integer>> sortCounts(Hashtable<String, Integer> t)
	 * 
	 * 
	 * */

	
	public static JSONObject getEntities(JSONObject tweet) throws DirenajInvalidJSONException {
		try {
			return (JSONObject) tweet.get("entities");
		} catch (JSONException je) {
			throw new DirenajInvalidJSONException("getEntities : " + je.getMessage() + tweet.toString());
		}
	}
	
	public static JSONArray getHashTags(JSONObject entities) throws DirenajInvalidJSONException {
		try {
			return (JSONArray) entities.get("hashtags");
		} catch(JSONException je) {
			throw new DirenajInvalidJSONException("getHashTags : " + je.getMessage());
		}
	}
	
	public static JSONArray getUrls(JSONObject entities) throws DirenajInvalidJSONException {
		try {
			return (JSONArray) entities.get("urls");
		} catch(JSONException je) {
			throw new DirenajInvalidJSONException("getUrls : " + je.getMessage());
		}
	}
	
	public static JSONArray getResults(JSONObject obj) throws DirenajInvalidJSONException {
		try {
			return obj.getJSONArray("results");
		} catch (JSONException e) {
			throw new DirenajInvalidJSONException("getReults : " + e.getMessage());
		}
	}
	
	public static JSONObject getTweet(JSONObject tweetData) throws DirenajInvalidJSONException {
		try {
			return (JSONObject) tweetData.get("tweet");
		} catch (JSONException e) {
			throw new DirenajInvalidJSONException("getTweet : " + e.getMessage());
		}
	}
	
	public static JSONObject getTweetData(JSONArray results, int index) throws DirenajInvalidJSONException {
		try {
			return (JSONObject) results.get(index);
		} catch (JSONException e) {
			throw new DirenajInvalidJSONException("getTweetData : " + e.getMessage());
		}
	}
	
	public static String getSingleTweetText(JSONObject tweetData) throws DirenajInvalidJSONException {
		try {
			return tweetData.getJSONObject("tweet").get("text").toString();
		} catch (Exception e) {
			throw new DirenajInvalidJSONException("getTweetText : " + e.getMessage());
		}
	}
	
	
    public static ArrayList<Map.Entry<String, Integer>> sortCounts(Hashtable<String, Integer> t) {

        //Transfer as List and sort it
        ArrayList<Map.Entry<String, Integer>> l = new ArrayList<Map.Entry<String, Integer>>(t.entrySet());
        
        Collections.sort(l, new Comparator<Map.Entry<String, Integer>>(){

          public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
        	  
        	  return o2.getValue().compareTo(o1.getValue());
         }});
        
        return l;
     }
}
