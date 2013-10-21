package direnajDriver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import direnajAdapter.DirenajDataHandler;
import direnajAdapter.DirenajInvalidJSONException;

public class DirenajDriver {
	
	private String userID; 
	private String password;

	public DirenajDriver(String uID, String pass) {
		this.userID = uID;
		this.password = pass;
	}
	
	/* METHODS SUMMARY
	 * 
	 * public collectTweetTexts
	 * public countHashtags 
	 * public collectHashTags
	 * public getSingleTweeInfo
	 * 
	 * private collectHashtagsBIG
	 * private collecTweetTextsBIG
	 * 
	 * */
	
	

	/* collectTweetTexts
	 * 
	 * */
	public ArrayList<String> collectTweetTexts(String campaignID, int skip, int limit) 
			throws Exception, DirenajInvalidJSONException {
		
		if(limit>1000) {
			return collectTweetTextsBIG(campaignID, skip, limit);
		}
		
		// get data from dataHandler
		JSONObject obj = DirenajDataHandler.getData(this.userID, this.password, campaignID, skip, limit);
		
		JSONArray results = DirenajDriverUtils.getResults(obj);
		
		int num = results.length();
		
		ArrayList<String> tweets = new ArrayList<String>();
		
		
		for (int i = 0; i < num; i++) {
			try {
				tweets.add(DirenajDriverUtils.getSingleTweetText(DirenajDriverUtils.getTweetData(results, i)));
				
			} catch(JSONException e) {
				// we don't throw exception here because we want the tweets[] array 
				// to be populated in any case, to catch the 'bad tweets' of direnaj
				tweets.add("bad tweet : " + DirenajDriverUtils.getTweetData(results, i).toString());
				// TODO : what if the line above throws an exception?
				continue;
			}
		}
		return tweets;
	}
	
	/* collecTweetTextsBIG
	 * 
	 * See collectHastagsBIG
	 * */
	private ArrayList<String> collectTweetTextsBIG(String campaignID, int skip, int limit) 
			throws DirenajInvalidJSONException, Exception {
		
		ArrayList<String> first1000 = collectTweetTexts(campaignID, skip, 1000);
		
		ArrayList<String> theRest = collectTweetTexts(campaignID, skip+1000, limit-1000);
		
		first1000.addAll(theRest);
		
		return first1000;
		
	}
	
	
	/* countHashtags
	 * 
	 * */
	
	public ArrayList<Map.Entry<String,Integer>> countHastags(String campaignID, int skip, int limit) 
			throws Exception{
		
		Hashtable<String,Integer> counts = new Hashtable<String,Integer>();
		
		// debugCount is for checking this limit/skip mechanism
		//int debugCount = 0;
		
		// we take 500 objects at a time, and just store their counts
		int currentTotalLimit = limit;
		int currentSkip = skip; 
		int currentLimit = 500;
		
		if(currentTotalLimit < 500) {
			currentLimit = currentTotalLimit;
		}
			
		while(currentTotalLimit > 0) {
			
			ArrayList<ArrayList<String>> tags = collectHashtags(campaignID, currentSkip, currentLimit);
			
			for (ArrayList<String> singleTweetTags: tags) {
				//debugCount++;
		    	for(String tag : singleTweetTags) {
		    		// if our table contains the tag
		    		if(counts.containsKey(tag)) {
		    			// increment its count value
		    			counts.put(tag, counts.get(tag) + 1);
		    		} else {
		    			// else, just put the tag into the table with value 1
		    			counts.put(tag, 1);
		    		}
		    	}
		    }
			
			currentTotalLimit -= 500;
			
				
			currentLimit = Math.min(currentLimit, currentTotalLimit);
			
			// we don't need to update skip with a value other than 500,
			// since if currentLimit < 500, then we are at the last step,
			// loop will be end
			currentSkip += 500;
		}
		
		//System.out.println("Debug Count : " + debugCount);
		
		
		return DirenajDriverUtils.sortCounts(counts);
	}
	
	
	/* collectHashTags
	 * 
	 * */
	
	public ArrayList<ArrayList<String>> collectHashtags(String campaignID, int skip, int limit) 
			throws Exception, DirenajInvalidJSONException {
		
		if(limit>1000) {
			return collectHashtagsBIG(campaignID, skip, limit);
		}
		
		// get data from dataHandler
		JSONObject obj = DirenajDataHandler.getData(this.userID, this.password, campaignID, skip, limit);
		
		
		// prepare containers
		ArrayList<ArrayList<String>> tag_collection = new ArrayList<ArrayList<String>>();
		JSONArray hashtags = null;
		
		// getting "results" field from response object
		JSONArray results = DirenajDriverUtils.getResults(obj);
		
			
		for (int i = 0; i < results.length(); i++) {
			
			// hashtags of a single result
			hashtags = DirenajDriverUtils.getHashTags(
						DirenajDriverUtils.getEntities(
							DirenajDriverUtils.getTweet(
								DirenajDriverUtils.getTweetData(results, i))));
			
			// temporary list for hashtags of a single tweet 
			ArrayList<String> tmp = new ArrayList<String>();
			
			// populate the temporary list
			for (int j = 0; j < hashtags.length(); j++) {
				tmp.add(hashtags.getJSONObject(j).get("text").toString());
			}
			
			// add the temporary list to the collection
			tag_collection.add(tmp);
		}
		
		return tag_collection;
	}
	
	/* collectHashtagsBIG
	 * 
	 * This method is called (by collectHastags) if the limit is above 1000
	 * 
	 *  That's because we may have a large 'limit', and we care about;
	 *  1 - Direnaj Server
	 *  2 - our heap space 
	 *  (the response obj with all the tweets are MUCH bigger than all hashtags)
	 *  
	 *  With this way, we never have the whole data in our heap,
	 *  just blocks of hashtags (each having tags from 1000 tweets) 
	 * */
	private ArrayList<ArrayList<String>> collectHashtagsBIG(String campaignID, int skip, int limit) 
			throws Exception, DirenajInvalidJSONException {
		
		ArrayList<ArrayList<String>> first1000 = collectHashtags(campaignID,skip,1000);
		
		
		ArrayList<ArrayList<String>> theRest = collectHashtags(campaignID,skip+1000,limit-1000); 
		
		first1000.addAll(theRest);
		
		return first1000;
	}



	
	/* getSingleTweeInfo
	 * 
	 * */
	public String getSingleTweetInfo(String campaignID, int skip, int limit) throws Exception {
		
		// get data from dataHandler
		JSONObject obj = DirenajDataHandler.getData(this.userID, this.password, campaignID, skip, limit);
		
		String mName = "getSingleTweetInfo : ";
		String retVal = "";
		
		// ["limit","direnaj_service_version","results","requested_by","skip",
				// "served_at","campaign_id"] 
		JSONArray arr = null;
		JSONObject tw = null;
		JSONObject tweet = null;
		JSONObject entities = null;
		JSONArray urls = null;
		JSONArray tags = null;
		
		try {
		
			arr = obj.getJSONArray("results");
			
			tw = (JSONObject) arr.get(0);
			
			tweet = (JSONObject) tw.get("tweet");
			
			retVal += "Tweet ID : " + tweet.get("id").toString();
			
			retVal += "<br><br><hr>";
			
			
			retVal += "results<br><br>";
			
			
			retVal += "<b>names</b> --> " + tw.names().toString();
			
			retVal += "<br><br>";
			
			retVal += "<b>text</b> --> " + tweet.get("text").toString();
			
			retVal += "<br><br>";
			
			retVal += "ENTITIES:<br>";
			
			entities = (JSONObject) tweet.get("entities");
			
			retVal += "<b>symbols</b> --> " + entities.get("symbols").toString() + "<br><br>";
			
			retVal += "<b>urls</b> : <br>"; 
			
			urls = (JSONArray) entities.get("urls"); 
			
			for (int i = 0; i < urls.length(); i++) {
				retVal += "--- " + urls.getJSONObject(i).get("url").toString() + "<br>";
			}
			
			retVal += "<br>";
			
			retVal += "<b>hashtags</b> : <br>";
			
			tags = (JSONArray) entities.get("hashtags");
			
			for (int i = 0; i < tags.length(); i++) {
				retVal += "--- " + tags.getJSONObject(i).get("text").toString() + "<br>";
			}
		} catch(JSONException e) {
			throw new DirenajInvalidJSONException(mName + e.getMessage());
		}
		retVal += "<br><br><hr>";
		
		retVal += "FULL DUMP<br><br>";
		
		retVal += tweet.toString();
		

		
		return retVal;
	}
	

}
