package similarity;

import java.util.ArrayList;
import java.util.List;

import search.Search;
import twitter.MyUser;

public class SearchResult {

	private MyUser user;
	private double score;

	public SearchResult(MyUser user, double score) {
		this.user = user;
		this.score = score;
	}

	public MyUser getMyUser(){
		return user;
	}
	
	public double getScore() {
		return score;
	}

	public String toString(Search search) {
		return String.format("%20.6f: " + user, score) + "\n" + user.printCharacteristics (search) + "\n" + user.printNetworkProperties(); 
	}
	
	/**
	 * 
	 * @param searchResults
	 * @param topUsers
	 * @return
	 */
	public static List<MyUser> findTopUsers(List<SearchResult> searchResults, int topUsers) {
		
		List<MyUser> users = new ArrayList<MyUser>(topUsers);
		
		for (int i = 0; i < topUsers; i++) {
			users.add(searchResults.get(i).getMyUser());
		}
		
		return users;
	}
}