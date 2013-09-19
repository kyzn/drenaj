package twitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import network.Edge;

import org.apache.commons.collections15.Bag;
import org.apache.commons.collections15.bag.TreeBag;

import search.Search;
import tokenizer.Token;
import twitter4j.User;
import util.MathUtil;
import util.Util;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.graph.DirectedGraph;

/**
 * This class represents a Twitterer.
 */
public class MyUser implements Comparable<MyUser>{
	
	private long id;
	
	private String screenName,
				   name;
	
	private int statusesCount,
				followersCount,
				friendsCount;
	
	private String profileImageUrl,
	               location,
	               description;
	
	private List<MyTweet> tweets;

	private int inDegree, outDegree;
	private Double betweenness, closeness;
	
	/**
	 * CAUTION:
	 * DO NOT USE THEM DIRECTLY EXCEPT initOwnAndRetweets()
	 * USE getOwnTweets() and getReTweets() INSTEAD
	 */
	private List<MyTweet> ownTweets, reTweets, relatedTweets;
	
	private List<MyTweet> relatedTweetsOwn, relatedTweetsRts;

	private Bag<String> termBag;
	private Bag<Token> urlBag;
	
	private boolean isExtended;

	private String rawJson;
	
	private double socialnessOwn, socialnessRts,  
				   feednessOwn, feednessRts,
				   ratingOwn, ratingRts, 
				   termVariationOwn, termVariationRts,
				   hashtagsOwn, hashtagsRts,
				   retweetRatioNew,
				   searchRelatenessNew;

	public double getRetweetRatioNew() {
		return retweetRatioNew;
	}

	public void setRetweetRatioNew(double retweetRatioNew) {
		this.retweetRatioNew = retweetRatioNew;
	}

	public double getSearchRelatenessNew() {
		return searchRelatenessNew;
	}

	public void setSearchRelatenessNew(double searchRelatenessNew) {
		this.searchRelatenessNew = searchRelatenessNew;
	}

	/**
	 * Dao methods using BeanPropertyRowMapper for constructing MyUser instances requires this. 
	 */
	public MyUser(){
	}
	
	// FIXME: ratelimit-showUser
	public MyUser(String screenName, List<MyTweet> tweets, Search search) {
		this.screenName = screenName;
		
		this.tweets = tweets;
		
		initBags();
		
		computeCharacteristics(search);
	}
	
	public MyUser(User user, List<MyTweet> tweets, Search search) {
		
		setUserFields(user);
		
		/* FIXME
		rawJson = DataObjectFactory.getRawJSON(user);

		System.out.println("rawJson=" + rawJson);
		*/
		
		this.tweets = tweets;
		
		initBags();
		
		computeCharacteristics(search);
	}

	/**
	 * 
	 * @param user
	 */
	private void setUserFields(User user) {
		this.screenName = user.getScreenName();
		this.id = user.getId();
		this.name = user.getName();
		this.statusesCount = user.getStatusesCount();
		this.followersCount = user.getFollowersCount();
		this.friendsCount = user.getFriendsCount();
		this.location = user.getLocation();
		this.profileImageUrl = user.getProfileImageURL().toString();
		this.description = user.getDescription();
	}

	public List<MyTweet> getTweets() {
		return tweets;
	}
	
	public void setTweets(List<MyTweet> tweets) {
		
		this.tweets = tweets;
		
		initBags();
	}

	/**
	 * 
	 */
	private void initBags() {
		
		initTermBag();
		initUrlBag();
	}
	
	@Override
	public int compareTo(MyUser otherUser) {
		boolean sameUser = id==otherUser.getId();
		return sameUser ? 0 : 1;
	}
	
	public String generateUserStr() {
		
		StringBuilder builder = new StringBuilder();
		
		for (MyTweet tweet : tweets) {
			
			// builder.append(tweet.getText());
			
			String hashtagsSeparated = tweet.getHashtagsSeparated();
			if (hashtagsSeparated!=null) {
				builder.append(hashtagsSeparated);
				builder.append(Util.DELIMITER_SPACE);
			}
			
			String wordsSeparated = tweet.getWordsSeparated();
			if (wordsSeparated!=null) {
				builder.append(wordsSeparated);
				builder.append(Util.DELIMITER_SPACE);
			}

			// builder.append('\n');
		}
		
		return builder.toString();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatusesCount() {
		return statusesCount;
	}

	public void setStatusesCount(int statusesCount) {
		this.statusesCount = statusesCount;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(int friendsCoun) {
		this.friendsCount = friendsCoun;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format("[ screen_name=%20s, extended=%5b, statusesCount=%7s, followersCount=%7s, friendsCount=%7s, location=%30s, name=%30s]", 
				screenName, isExtended, statusesCount, followersCount, friendsCount, Util.escapePercentage(location), Util.escapePercentage(name));
	}
	
	public static Set<String> findUserNames(Collection<MyUser> users) {
		
		Set<String> userNames = new TreeSet<String>();
		
		for (MyUser myUser : users) {
			userNames.add(myUser.getScreenName());
		}
		
		return userNames;
	}
	
	/**
	 * 
	 */
	private void initRelatedTweets(Search search) {
		
		if (relatedTweets == null) {
			
			relatedTweets = new ArrayList<MyTweet>(); 
			relatedTweetsRts = new ArrayList<MyTweet>(); 
			relatedTweetsOwn = new ArrayList<MyTweet>(); 
			
			for (MyTweet myTweet : tweets) {
				if ( myTweet.containsRelatedTerms(search) ) {
					
					myTweet.setRelated(true);
					
					relatedTweets.add(myTweet);
					
					if (myTweet.isRetweet()) {
						relatedTweetsRts.add(myTweet);
					} else {
						relatedTweetsOwn.add(myTweet);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<MyTweet> getOwnTweets() {
		
		initOwnAndRetweets();
		return ownTweets;
	}

	/**
	 * 
	 * @return
	 */
	public List<MyTweet> getReTweets() {

		initOwnAndRetweets();
		return reTweets;
	}

	/**
	 * 
	 * @return
	 */
	public List<MyTweet> getRelatedTweets(Search search) {

		initRelatedTweets(search);
		return relatedTweets;
	}
	
	public List<MyTweet> getRelatedTweetsOwn(Search search) {

		initRelatedTweets(search);
		return relatedTweetsOwn;
	}
	
	public List<MyTweet> getRelatedTweetsRts(Search search) {

		initRelatedTweets(search);
		return relatedTweetsRts;
	}
	
	/**
	 * 
	 */
	private void initOwnAndRetweets() {
		
		if (ownTweets == null) {
			
			ownTweets = new ArrayList<MyTweet>();
			reTweets = new ArrayList<MyTweet>();
			
			for (MyTweet myTweet : tweets) {
				
				if ( myTweet.isRetweet() ) {
					reTweets.add(myTweet);
				} else {
					ownTweets.add(myTweet);
				}
			}
		}
	}

	/**
	 * farkli term sayisi ile carparsak daha dogru sonuc verir mi? tek bir term ile "yuksek relateness" elde etmenin onune geceriz.. "google"
	 * @return searchRelateness = #relatedTweets/#tweets
	 * 
	 */
	private double searchRelateness(Search search) {
		return MathUtil.divideZeroSafe(getRelatedTweets(search).size(), tweets.size());		
	}
	
	/**
	 * 
	 */
	private void computeCharacteristics(Search search) {
		
		List<MyTweet> own = getRelatedTweetsOwn(search);
		List<MyTweet> rts = getRelatedTweetsRts(search);
		
		socialnessOwn = socialness(search, own);
		socialnessRts = socialness(search, rts);
	
		feednessOwn = feedness(search, own);
		feednessRts = feedness(search, rts);
		
		ratingOwn = rating(search, own);
		ratingRts = rating(search, rts);
		
		termVariationOwn = termVariation(search, own);
		termVariationRts = termVariation(search, rts);
		
		hashtagsOwn = hashtags(search, own);
		hashtagsRts = hashtags(search, rts);
		
		retweetRatioNew = retweetRatio(search);
		
		searchRelatenessNew = searchRelateness(search);
	}

	private double retweetRatio(Search search) {
		return MathUtil.divideZeroSafe(getRelatedTweetsRts(search).size(), getRelatedTweets(search).size());
	}

	private double hashtags(Search search, List<MyTweet> tweets) {
		
		Set<Token> hashtags = new TreeSet<Token>();
		
		for (MyTweet myTweet : tweets) {
			hashtags.addAll(myTweet.getHashtags());
		}
		
		return MathUtil.divideZeroSafe(hashtags.size(), tweets.size());
	}

	private double termVariation(Search search, List<MyTweet> tweets) {
		
		// return MathUtil.divideZeroSafe(termBag.uniqueSet().size(), termBag.size());
		
		Set<String> terms = new TreeSet<String>();
		
		for (MyTweet myTweet : tweets) {
			terms.addAll(myTweet.getTermBag());
		}
		
		return MathUtil.divideZeroSafe(terms.size(), tweets.size());
	}

	private double rating(Search search, List<MyTweet> tweets) {
		
		int retweets = 0;
		
		for (MyTweet myTweet : tweets) {
			retweets += myTweet.getRetweetCount();
		}
		
		return MathUtil.divideZeroSafe(retweets, tweets.size());
	}

	private double feedness(Search search, List<MyTweet> tweets) {

		Set<Token> urlSet = new TreeSet<Token>();
		
		for (MyTweet myTweet : tweets) {
			urlSet.addAll(myTweet.getUrls());
		}
		
		return MathUtil.divideZeroSafe(urlSet.size(), tweets.size());
	}

	/**
	 * 
	 * @return
	 */
	private double socialness(Search search, List<MyTweet> tweets) {
		
		int mentionsOut = 0;
		
		for (MyTweet myTweet : tweets) {
			mentionsOut += myTweet.getMentions().size();
		}
		
		return MathUtil.divideZeroSafe(mentionsOut, tweets.size());
	}

	public void initNetworkProperties(DirectedGraph<String, Edge> network, BetweennessCentrality<String, Edge> betweennessCentrality, ClosenessCentrality<String, Edge> closenessCentrality) {

		inDegree = network.inDegree(screenName);
		outDegree = network.outDegree(screenName);
		betweenness = MathUtil.nullAndNanSafeDouble(betweennessCentrality.getVertexScore(screenName));
		closeness = MathUtil.nullAndNanSafeDouble(closenessCentrality.getVertexScore(screenName));
	}
	
	/**
	 * 
	 */
	private void initTermBag(){
		
		termBag = new TreeBag<String>();
		
		for (MyTweet myTweet : tweets) {
			termBag.addAll(myTweet.getTermBag());
		}
	}
	
	public Bag<String> getTermBag() {
		return termBag;
	}
	
	/**
	 * 
	 */
	private void initUrlBag(){
		
		urlBag = new TreeBag<Token>();
		
		for (MyTweet myTweet : tweets) {
			urlBag.addAll(myTweet.getUrls());
		}
	}
	
	public Bag<Token> getUrlBag() {
		return urlBag;
	}
	
	public boolean isExtended() {
		return isExtended;
	}

	public void setExtended(boolean isExtended) {
		this.isExtended = isExtended;
	}

	public String getRawJson() {
		return rawJson;
	}

	public void setRawJson(String rawJson) {
		this.rawJson = rawJson;
	}

	public int getInDegree() {
		return inDegree;
	}

	public void setInDegree(int inDegree) {
		this.inDegree = inDegree;
	}

	public int getOutDegree() {
		return outDegree;
	}

	public void setOutDegree(int outDegree) {
		this.outDegree = outDegree;
	}

	public Double getBetweenness() {
		return betweenness;
	}

	public void setBetweenness(Double betweenness) {
		this.betweenness = betweenness;
	}

	public Double getCloseness() {
		return closeness;
	}

	public void setCloseness(Double closeness) {
		this.closeness = closeness;
	}
	
	public double getSocialnessOwn() {
		return socialnessOwn;
	}

	public void setSocialnessOwn(double socialnessOwn) {
		this.socialnessOwn = socialnessOwn;
	}

	public double getSocialnessRts() {
		return socialnessRts;
	}

	public void setSocialnessRts(double socialnessRts) {
		this.socialnessRts = socialnessRts;
	}

	public double getFeednessOwn() {
		return feednessOwn;
	}

	public void setFeednessOwn(double feednessOwn) {
		this.feednessOwn = feednessOwn;
	}

	public double getFeednessRts() {
		return feednessRts;
	}

	public void setFeednessRts(double feednessRts) {
		this.feednessRts = feednessRts;
	}

	public double getRatingOwn() {
		return ratingOwn;
	}

	public void setRatingOwn(double ratingOwn) {
		this.ratingOwn = ratingOwn;
	}

	public double getRatingRts() {
		return ratingRts;
	}

	public void setRatingRts(double ratingRts) {
		this.ratingRts = ratingRts;
	}

	public double getTermVariationOwn() {
		return termVariationOwn;
	}

	public void setTermVariationOwn(double termVariationOwn) {
		this.termVariationOwn = termVariationOwn;
	}

	public double getTermVariationRts() {
		return termVariationRts;
	}

	public void setTermVariationRts(double termVariationRts) {
		this.termVariationRts = termVariationRts;
	}

	public double getHashtagsOwn() {
		return hashtagsOwn;
	}

	public void setHashtagsOwn(double hashtagsOwn) {
		this.hashtagsOwn = hashtagsOwn;
	}

	public double getHashtagsRts() {
		return hashtagsRts;
	}

	public void setHashtagsRts(double hashtagsRts) {
		this.hashtagsRts = hashtagsRts;
	}

	/**
	 * 
	 * @param search
	 * @return
	 */
	public String printNetworkProperties () {
		return String.format("%22s[inDegree=%d, outDegree=%d, betweenness=%f, closeness=%f]", "", inDegree, outDegree, betweenness, closeness); 
	}
	
	/**
	 * 
	 * @return
	 */
	public String printCharacteristics (Search search) {
		return null;// String.format("%22s[chattiness=%5f, feedness=%f, retweetedMean=%f, retweetRatio=%f, termVariation=%f, searchRelateness=%f, ratingOfRelatedTweets=%f]\n", "", getChattiness(), getFeedness(), getRetweetedMean(), getRetweetRatio(), getTermVariation(), getSearchRelateness(), getRatingOfRelatedTweets()); 
	}
}
