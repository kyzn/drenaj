package unused;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import util.JsonUtil;

/**
 * @deprecated Unused Class
 */
public class Twittomender {

	public static void main(String[] args) throws Exception {
		
		String[] queries = {"spring framework"};
		
		for (String q : queries) {
			printQuery(q);
		}
	}
	
	public static void printQuery(String q) throws Exception {
		
		q = q.replace(" ", "%20");
		
		List<JSONObject> recommendations = query(q);
		for (JSONObject recommendation : recommendations) {
			System.out.println( "http://twitter.com/"+recommendation.get("screenname") + "\n" + recommendation.get("desc"));
			printInsertStatements(000, (String)recommendation.get("screenname"));
			System.out.println("------------------------------------------------");
		}
	}
	
	public static List<JSONObject> query (String query) throws Exception {
		
		List<JSONObject> recommendations = new ArrayList<JSONObject>();
		
		JSONObject root = (JSONObject) JSONValue.parse(buildReader(query));
		JSONArray entries = JsonUtil.getJsonArray("results", root);
		
		if (entries!=null) {
			
			for (int i = 0; i < entries.size(); i++) {
				
				recommendations.add((JSONObject)entries.get(i));
				// System.out.println("http://twitter.com/"+entry.get("screenname") + ": " + entry.get("desc") );
			}
		}
		
		return recommendations;
	}
	
	public static void printInsertStatements(int eval_user_query_id, String screenName) {
		// for (String screenName : screenNames) {
			System.out.println("INSERT INTO t_eval_recom ('eval_user_query_id', 'rec_source', 'screen_name') VALUES ('" + eval_user_query_id + "', '1', '" + screenName + "');");
		//}
	}

	private static Reader buildReader(String query) throws Exception {

		String url = "http://twittomender.ucd.ie/SerServlet?searchquery=" + query;
		System.out.println(url);
		return new BufferedReader(new InputStreamReader(new URL(url).openStream()));
	}
}
