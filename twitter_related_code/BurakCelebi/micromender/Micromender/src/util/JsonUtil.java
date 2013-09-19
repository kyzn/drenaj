package util;

import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import semantic.DeliciousClient;

public final class JsonUtil {
	
	/**
	 * 
	 * @param jsonArray
	 * @return
	 */
	public static String[] toStringArr(JSONArray jsonArray) {

		Set<String> elems = new TreeSet<String>();
		
		for (int i = 0; i < jsonArray.size(); i++) {
			elems.add( DeliciousClient.unifiedTag((String)jsonArray.get(i)));
		}

		return elems.toArray(new String[0]);
	}
	
	/**
	 * 
	 * @param jsonObjectId
	 * @param ancestor
	 * @return
	 */
	public static JSONObject getJsonObject(String jsonObjectId, JSONObject ancestor) {
		return (JSONObject) getJsonObj(jsonObjectId, ancestor);
	}
	
	/**
	 * 
	 * @param jsonArrayId
	 * @param ancestor
	 * @return
	 */
	public static JSONArray getJsonArray(String jsonArrayId, JSONObject ancestor) {
		return (JSONArray) getJsonObj(jsonArrayId, ancestor);
	}

	/**
	 * 
	 * @param jsonObjectId
	 * @param ancestor
	 * @return
	 */
	private static Object getJsonObj (String jsonObjectId, JSONObject ancestor) {
		return ancestor.get(jsonObjectId);
	}
	
	/**
	 * Preventing construction
	 */
	private JsonUtil(){
		throw new AssertionError();
	}
}
