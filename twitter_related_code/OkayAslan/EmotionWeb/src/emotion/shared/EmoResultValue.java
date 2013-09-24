package emotion.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class EmoResultValue implements Serializable
{
   private static final long serialVersionUID = 1L;
   
   private String key;
   private String name;
   private String alias;
   private String abstractText;
   private String imageUrl;
   private String wikiUrl;
   private ArrayList<String> adjectives;
   private ArrayList<String> verbs;
   private ArrayList<String> tweets = new ArrayList<String>();
   private HashMap<String, String> tweetHashMap = new HashMap<String, String>();
   private HashMap<String, Integer> emotionOccHashMap = new HashMap<String, Integer>();   
   private ArrayList<EmoTweet> emoTweetArrayList = new ArrayList<EmoTweet>();
   private HashMap<String, Integer> emoTweetHashMap = new HashMap<String, Integer>();
   
   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }
   /**
    * @return the alias
    */
   public String getAlias()
   {
      return alias;
   }
   /**
    * @return the abstractText
    */
   public String getAbstractText()
   {
      return abstractText;
   }
   /**
    * @return the imageUrl
    */
   public String getImageUrl()
   {
      return imageUrl;
   }
   /**
    * @return the adjectives
    */
   public ArrayList<String> getAdjectives()
   {
      return adjectives;
   }
   /**
    * @return the verbs
    */
   public ArrayList<String> getVerbs()
   {
      return verbs;
   }
   /**
    * @param name the name to set
    */
   public void setName(String name)
   {
      this.name = name;
   }
   /**
    * @param alias the alias to set
    */
   public void setAlias(String alias)
   {
      this.alias = alias;
   }
   /**
    * @param abstractText the abstractText to set
    */
   public void setAbstractText(String abstractText)
   {
      this.abstractText = abstractText;
   }
   /**
    * @param imageUrl the imageUrl to set
    */
   public void setImageUrl(String imageUrl)
   {
      this.imageUrl = imageUrl;
   }
   /**
    * @param adjectives the adjectives to set
    */
   public void setAdjectives(ArrayList<String> adjectives)
   {
      this.adjectives = adjectives;
   }
   /**
    * @param verbs the verbs to set
    */
   public void setVerbs(ArrayList<String> verbs)
   {
      this.verbs = verbs;
   }
   /**
    * @param wikiUrl the wikiUrl to set
    */
   public void setWikiUrl(String wikiUrl)
   {
      this.wikiUrl = wikiUrl;
   }
   /**
    * @return the wikiUrl
    */
   public String getWikiUrl()
   {
      return wikiUrl;
   }
   /**
    * @param tweets the tweets to set
    */
   public void setTweets(ArrayList<String> tweets)
   {
      this.tweets = tweets;
   }
   /**
    * @return the tweets
    */
   public ArrayList<String> getTweets()
   {
      return tweets;
   }
   /**
    * @param key the key to set
    */
   public void setKey(String key)
   {
      this.key = key;
   }
   /**
    * @return the key
    */
   public String getKey()
   {
      return key;
   }
   /**
    * @param tweetHashMap the tweetHashMap to set
    */
   public void setTweetHashMap(HashMap<String, String> tweetHashMap)
   {
      this.tweetHashMap = tweetHashMap;
   }
   /**
    * @return the tweetHashMap
    */
   public HashMap<String, String> getTweetHashMap()
   {
      return tweetHashMap;
   }
   /**
    * @return the emoTweetArrayList
    */
   public ArrayList<EmoTweet> getEmoTweetArrayList()
   {
      return emoTweetArrayList;
   }
   /**
    * @param emoTweetArrayList the emoTweetArrayList to set
    */
   public void setEmoTweetArrayList(ArrayList<EmoTweet> emoTweetArrayList)
   {
      this.emoTweetArrayList = emoTweetArrayList;
   }
   /**
    * @param emoTweetHashMap the emoTweetHashMap to set
    */
   public void setEmoTweetHashMap(HashMap<String, Integer> emoTweetHashMap)
   {
      this.emoTweetHashMap = emoTweetHashMap;
   }
   /**
    * @return the emoTweetHashMap
    */
   public HashMap<String, Integer> getEmoTweetHashMap()
   {
      return emoTweetHashMap;
   }
   /**
    * @param emotionOccHashMap the emotionOccHashMap to set
    */
   public void setEmotionOccHashMap(HashMap<String, Integer> emotionOccHashMap)
   {
      this.emotionOccHashMap = emotionOccHashMap;
   }
   /**
    * @return the emotionOccHashMap
    */
   public HashMap<String, Integer> getEmotionOccHashMap()
   {
      return emotionOccHashMap;
   }


   
}
