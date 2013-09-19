package twitter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections15.Bag;
import org.apache.commons.collections15.bag.TreeBag;

import search.Search;
import tokenizer.Label;
import tokenizer.Token;
import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.URLEntity;
import twitter4j.json.DataObjectFactory;
import util.Util;

/*
 * 
 * This class represents a Tweet.
 * 
 */
public class MyTweet {

	private static final int IS_RELATED_TRUE  = 1,
							 IS_RELATED_FALSE = 0;
	
	private Bag<Token> hashtags,
                       mentions,
                       urls,
                       words,
                       stopWords;

	private Bag<Token> terms;
	
	// private Set<Token> tweetUrls;

	private String id;
	private String fromUser;
	private String text;
	private Date createdAt;
	
	private boolean isRetweet;
	private String retweetedId;
	private String retweetedScreenName;
	
	private String replyToId;
	private String replyToScreenName;
	
	private long retweetCount;
	
	private Double latitude, longitude;

	private String rawJson;

	private static final int IS_RETWEET_TRUE = 1;

	private static final String NO_REPLY_ID = "-1";

	private boolean isRelated;
	
	public boolean isRelated() {
		return isRelated;
	}

	public int isRelatedDbVal() {
		return isRelated ? IS_RELATED_TRUE : IS_RELATED_FALSE;
	}

	public void setRelated(boolean isRelated) {
		this.isRelated = isRelated;
	}


	/**
	 * 
	 * @param statuses
	 * @return
	 * @throws Exception
	 */
	public static List<MyTweet> toMyTweetsFromStatuses(List<Status> statuses) throws Exception {
		
		List<MyTweet> myTweets = new ArrayList<MyTweet>(statuses.size());
		
		for (Status status : statuses) {
			
			/*
			if (! NonAsciiLabeler.isPureAscii(status.getText())) {
				System.out.println("!!!! Not pure ascii: " + status.getId());
			}
			*/
			
			/*
			if (NonAsciiLabeler.isPureAscii(status.getText())) {
				myTweets.add(new MyTweet(status));
			} else {
				System.out.println("!!!! Not pure ascii: " + status.getId());
			}
			*/
			myTweets.add(new MyTweet(status));
		}
		
		return myTweets;
	}

	
	/**
	 * 
	 * @param tweets
	 * @return
	 * @throws Exception
	 */
	public static List<MyTweet> toMyTweetsFromTweets(List<Tweet> tweets) throws Exception {
		
		List<MyTweet> myTweets = new ArrayList<MyTweet>(tweets.size());
		
		for (Tweet tweet : tweets) {
			
			/*
			if (! NonAsciiLabeler.isPureAscii(tweet.getText())) {
				System.out.println("!!!! Not pure ascii: " + tweet.getId());
			}
			*/
			
			/*
			if (NonAsciiLabeler.isPureAscii(tweet.getText())) {
				myTweets.add(new MyTweet(tweet));
			} else {
				System.out.println("!!!! Not pure ascii: " + tweet.getId());
			}
			*/
			myTweets.add(new MyTweet(tweet));
		}
		
		return myTweets;
	}

	private MyTweet() {
	}
	
	/**
	 * For search results.
	 * 
	 * @param id
	 * @param fromUser
	 */
	private MyTweet(String id, String fromUser) {
		this.id = id;
		this.fromUser = fromUser;
	}

	/**
	 * User's tweets fetch from Twitter
	 * 
	 * @param status
	 * @throws Exception
	 */
	private MyTweet(Status status) throws Exception {
		
		rawJson = DataObjectFactory.getRawJSON(status);
		fromUser = status.getUser().getScreenName();
		id = String.valueOf(status.getId());
		
		// System.out.println("MyTweet for id=" + id + ", screen_name=" + fromUser);
		
		isRetweet = status.isRetweet();
		
		if (isRetweet) {

			Status retweetedStatus = status.getRetweetedStatus();
			
			retweetedId = String.valueOf(retweetedStatus.getId());
			retweetedScreenName = retweetedStatus.getUser().getScreenName();
			
			status = retweetedStatus;
		}
		
		text = status.getText();
		createdAt = status.getCreatedAt();
		
		replyToId = String.valueOf(status.getInReplyToStatusId());
		replyToScreenName = status.getInReplyToScreenName();
		
		GeoLocation geoLocation = status.getGeoLocation();
		if (geoLocation!=null) {
			latitude = geoLocation.getLatitude();
			longitude = geoLocation.getLongitude();
		}
		
		retweetCount = status.getRetweetCount();
		
		tokenize(status);

	}

	/**
	 * 
	 * @param tweet
	 * @throws Exception
	 */
	private MyTweet(Tweet tweet) throws Exception {

		this.id = String.valueOf(tweet.getId());
		this.fromUser = tweet.getFromUser();
	}
	
	public boolean isRetweet() {
		return isRetweet;
	}


	public void setRetweet(boolean isRetweet) {
		this.isRetweet = isRetweet;
	}

	/**
	 * 
	 * @param isRetweet
	 * @return
	 */
	private static boolean toRetweetBool(int isRetweet) {
		return IS_RETWEET_TRUE==isRetweet;
	}

	public String getRetweetedId() {
		return retweetedId;
	}


	public void setRetweetedId(String retweetedId) {
		this.retweetedId = retweetedId;
	}


	public String getRetweetedScreenName() {
		return retweetedScreenName;
	}


	public void setRetweetedScreenName(String retweetedScreenName) {
		this.retweetedScreenName = retweetedScreenName;
	}


	public String getReplyToId() {
		return replyToId;
	}


	public void setReplyToId(String replyToId) {
		this.replyToId = replyToId;
	}


	public String getReplyToScreenName() {
		return replyToScreenName;
	}


	public void setReplyToScreenName(String replyToScreenName) {
		this.replyToScreenName = replyToScreenName;
	}


	public long getRetweetCount() {
		return retweetCount;
	}


	public void setRetweetCount(long retweetCount) {
		this.retweetCount = retweetCount;
	}


	public Double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}


	public Double getLongitude() {
		return longitude;
	}


	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	public String getRawJson() {
		return rawJson;
	}


	public void setRawJson(String rawJson) {
		this.rawJson = rawJson;
	}


	/**
	 * 
	 * @param id
	 * @param fromUser
	 * @return
	 */
	public static MyTweet toSearchResult(String id, String fromUser) {
		return new MyTweet(id, fromUser);
	}
	
	public static MyTweet createFromDB(
			
			String id, String fromUser, String tweet, Date createdAt, int retweetCount,
			Double latitude, Double longitude,
			String replyId, String replyScreenName, 
			int isRetweet, String retweetedId, String retweetedScreenName, 
			String hashtags, String mentions, String urls, String words
			
			) {
		
		MyTweet myTweet = new MyTweet();
		
		myTweet.setId(id);
		myTweet.setFromUser(fromUser);
		myTweet.setText(tweet);
		myTweet.setCreatedAt(createdAt);
		myTweet.setRetweetCount(retweetCount);

		myTweet.setLatitude(latitude);
		myTweet.setLongitude(longitude);

		myTweet.setReplyToId(replyId);
		myTweet.setReplyToScreenName(replyScreenName);
		
		myTweet.setRetweet( toRetweetBool(isRetweet) );
		myTweet.setRetweetedId(retweetedId);
		myTweet.setRetweetedScreenName(retweetedScreenName);
		
		myTweet.setHashtags ( Token.toTokens(hashtags, tokenizer.Label.HASHTAG) );
		myTweet.setMentions ( Token.toTokens(mentions, tokenizer.Label.MENTION) );
		myTweet.setUrls     ( Token.toTokens(urls,     tokenizer.Label.URL    ) );
		myTweet.setWords    ( Token.toTokens(words,    tokenizer.Label.WORD   ) );
		
		myTweet.initTerms();
		
		return myTweet;
		
	}

	private void initTokenSets() {
		
		hashtags = new TreeBag<Token>();
		mentions = new TreeBag<Token>();
		urls = new TreeBag<Token>();
		words = new TreeBag<Token>();
		stopWords = new TreeBag<Token>();
		terms = new TreeBag<Token>();
		// tweetUrls = new TreeBag<Token>();
	}
	
	private void tokenize(Status status) throws Exception {
		
		initTokenSets();
		
		/*
		entitiesToTokens(status);
		
		List<Token> tokens = Util.toLabeledTokens(findTextWithoutEntities());
		*/
		
		List<Token> tokens = Util.toLabeledTokens(Util.lowerCase(text));
		
		urlEntitesToTokens(status);
		
		for (Token token : tokens) {
			switch (token.getLabel()) {
			case HASHTAG:
				hashtags.add(token);
				break;
			case MENTION:
				mentions.add(token);
				break;
			case URL:
				// urls.add(token); we already init urls form twitter entites from status object via urlEntitesToTokens method. Because we want to get expanded urls not shortened ones.
				break;
			case WORD:
				words.add(token);
				break;
			case STOP_WORD:
				stopWords.add(token);
				break;
			case NON_ASCII:
				//FIXME:
				break;
			default:
				boolean isSpecial = token.getValue().equals(Util.TWITTER_SIGN_MENTION) || token.getValue().equals(Util.TWITTER_SIGN_HASHTAG);
				if (!isSpecial) {
					; //System.out.println("Unhandled token: " + token);
				}
			}
		}
		
		initTerms();
		
	}

	/**
	 * 
	 */
	private void initTerms() {
		
		terms = new TreeBag<Token>(words);
		terms.addAll(hashtags);
	}

	/*
	private String findTextWithoutEntities() {
	
		System.out.println("Text1: " + text);
		
		String extracted = new String(text);
		
		for (Token token : hashtags) {
			extracted = extracted.replace("#"+token.getValue(), " ");
		}
		
		for (Token token : mentions) {
			extracted = extracted.replace("@"+token.getValue(), " ");
		}
		
		for (Token token : urls) {
			extracted = extracted.replace(forRegex(token.getValue()), " ");
		}
		
		for (Token token : tweetUrls) {
			extracted = extracted.replace(forRegex(token.getValue()), " ");
		}
		
		System.out.println("Text2: " + extracted);
		System.out.println("----------------------------");
		
		return extracted;
	}
	*/
	
	 /**
	   Replace characters having special meaning in regular expressions
	   with their escaped equivalents, preceded by a '\' character.
	  
	   <P>The escaped characters include :
	  <ul>
	  <li>.
	  <li>\
	  <li>?, * , and +
	  <li>&
	  <li>:
	  <li>{ and }
	  <li>[ and ]
	  <li>( and )
	  <li>^ and $
	  </ul>
	  */
	
		/*
	  public static String forRegex(String aRegexFragment){
	    final StringBuilder result = new StringBuilder();

	    final StringCharacterIterator iterator = 
	      new StringCharacterIterator(aRegexFragment)
	    ;
	    char character =  iterator.current();
	    while (character != CharacterIterator.DONE ){
	      
	      // All literals need to have backslashes doubled.
	    
	      if (character == '.') {
	        result.append("\\.");
	      }
	      else if (character == '\\') {
	        result.append("\\\\");
	      }
	      else if (character == '?') {
	        result.append("\\?");
	      }
	      else if (character == '*') {
	        result.append("\\*");
	      }
	      else if (character == '+') {
	        result.append("\\+");
	      }
	      else if (character == '&') {
	        result.append("\\&");
	      }
	      else if (character == ':') {
	        result.append("\\:");
	      }
	      else if (character == '{') {
	        result.append("\\{");
	      }
	      else if (character == '}') {
	        result.append("\\}");
	      }
	      else if (character == '[') {
	        result.append("\\[");
	      }
	      else if (character == ']') {
	        result.append("\\]");
	      }
	      else if (character == '(') {
	        result.append("\\(");
	      }
	      else if (character == ')') {
	        result.append("\\)");
	      }
	      else if (character == '^') {
	        result.append("\\^");
	      }
	      else if (character == '$') {
	        result.append("\\$");
	      }
	      else {
	        //the char is not a special one
	        //add it to the result as is
	        result.append(character);
	      }
	      character = iterator.next();
	    }
	    return result.toString();
	  }
	  */
	
	/*
	  public static String escapeURL(String aURLFragment){
		     String result = null;
		     try {
		       result = URLEncoder.encode(aURLFragment, "UTF-8");
		     }
		     catch (UnsupportedEncodingException ex){
		       throw new RuntimeException("UTF-8 not supported", ex);
		     }
		     return result;
		   }

	 */

	/*
	private void entitiesToTokens(Status status) {
		
		HashtagEntity[] hashtagEntities = status.getHashtagEntities();
		for (HashtagEntity entity : hashtagEntities) {
			hashtags.add(new Token(entity.getText(), Label.HASHTAG));
		}
		
		URLEntity[] urlEntities = status.getURLEntities();
		for (URLEntity entity : urlEntities) {

			URL url = entity.getExpandedURL();
			if(url==null) {
				url = entity.getURL();
			}
			urls.add(new Token(url.toString(), Label.URL));
			
			tweetUrls.add(new Token(entity.getURL().toString(), Label.URL));
		}
		
		UserMentionEntity[] userMentionEntities = status.getUserMentionEntities();
		for (UserMentionEntity entity : userMentionEntities ) {
			mentions.add(new Token(entity.getScreenName(), Label.MENTION));
		}
	}
	 */	
	
	/**
	 * 
	 * @param status
	 */
	private void urlEntitesToTokens(Status status) {

		URLEntity[] urlEntities = status.getURLEntities();
		
		for (URLEntity entity : urlEntities) {

			URL url = entity.getExpandedURL();
			if(url==null) {
				url = entity.getURL();
			}
			
			if(url!=null) {
				urls.add(new Token(url.toString(), Label.URL));
			}
			
		}
	}


	/**
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	public Bag<Token> getHashtags() {
		return hashtags;
	}
	
	public String getHashtagsSeparated() {
		return Util.toSpaceSeparated(hashtags);
	}
	
	public Bag<Token> getMentions() {
		return mentions;
	}

	public String getMentionsSeparated() {
		return Util.toSpaceSeparated(mentions);
	}
	
	public Bag<Token> getUrls() {
		return urls;
	}

	public String getUrlsSeparated() {
		return Util.toSpaceSeparated(urls);
	}
	
	public Bag<Token> getWords() {
		return words;
	}
	
	public String getWordsSeparated() {
		return Util.toSpaceSeparated(words);
	}
	
	public Bag<Token> getStopWords() {
		return stopWords;
	}
	
	public String getStopWordsSeparated() {
		return Util.toSpaceSeparated(stopWords);
	}
	
	public String getFromUser() {
		return fromUser;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	/*
	public Set<Token> getTweetUrls() {
		return tweetUrls;
	}

	public void setTweetUrls(Set<Token> tweetUrls) {
		this.tweetUrls = tweetUrls;
	}
	*/

	public void setHashtags(Bag<Token> hashtags) {
		this.hashtags = hashtags;
	}


	public void setMentions(Bag<Token> mentions) {
		this.mentions = mentions;
	}


	public void setUrls(Bag<Token> urls) {
		this.urls = urls;
	}


	public void setWords(Bag<Token> words) {
		this.words = words;
	}


	public void setStopWords(Bag<Token> stopWords) {
		this.stopWords = stopWords;
	}


	public void setId(String id) {
		this.id = id;
	}


	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isReply() {
		return replyToId.equals(NO_REPLY_ID);
	}

	public Bag<Token> getTerms() {
		return terms;
	}

	public Bag<String> getTermBag() {
		
		Bag<String> termBag = new TreeBag<String>();
		
		for (Token token : terms) {
			termBag.add(token.getValue());
		}
		
		return termBag;
	}

	/**
	 * @return
	 */
	boolean containsRelatedTerms (Search search) {
	
		return Token.isRelatedWith(terms, search) /* || Token.isRelatedWith(urls, search) */  ;  // FIXME: url

		/*
		// try terms of the tweet (ie words and hashtags) first.
		for (Token token : terms) {
			if ( token.isRelatedWith(search) ) {
				return true;
			}
		}
		
		// try urls of the tweet then.
		for (Token urlToken : urls) {
			if ( urlToken.isRelatedWith(search) ) {
				return true;
			}
		}
		
		return false;
		*/
	}
}
