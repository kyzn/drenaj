package emotion.server.analyzer;

import java.util.ArrayList;
import java.util.HashMap;

import emotion.shared.EmoTweet;

public class ResultAnalysisData
{
   private String namedEntity;
   private ArrayList<EmoTweet> emoTweetArrayList = new ArrayList<EmoTweet>();
   private HashMap<String,Integer> distinctEmoTweets = new HashMap<String,Integer>();
   private HashMap<Long,String> tweetIDHashMap = new HashMap<Long,String>();
   private HashMap<Long,String> NVNHashMap = new HashMap<Long,String>();
   private HashMap<String, Integer> verbOccHashMap = new HashMap<String, Integer>();
   private HashMap<String, Integer> emotionOccHashMap = new HashMap<String, Integer>();
   private HashMap<String,HashMap<String, Integer>> emotionWordsOccurenceHashMap = new HashMap<String,HashMap<String, Integer>>();
   
   /**
    * @return the namedEntity
    */
   public String getNamedEntity()
   {
      return namedEntity;
   }
   /**
    * @param namedEntity the namedEntity to set
    */
   public void setNamedEntity(String namedEntity)
   {
      this.namedEntity = namedEntity;
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
    * @return the distinctEmoTweets
    */
   public HashMap<String, Integer> getDistinctEmoTweets()
   {
      return distinctEmoTweets;
   }
   /**
    * @param distinctEmoTweets the distinctEmoTweets to set
    */
   public void setDistinctEmoTweets(HashMap<String, Integer> distinctEmoTweets)
   {
      this.distinctEmoTweets = distinctEmoTweets;
   }
   /**
    * @return the verbOccHashMap
    */
   public HashMap<String, Integer> getVerbOccHashMap()
   {
      return verbOccHashMap;
   }
   /**
    * @param verbOccHashMap the verbOccHashMap to set
    */
   public void setVerbOccHashMap(HashMap<String, Integer> verbOccHashMap)
   {
      this.verbOccHashMap = verbOccHashMap;
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
   /**
    * @param emotionWordsOccurenceHashMap the emotionWordsOccurenceHashMap to set
    */
   public void setEmotionWordsOccurenceHashMap(HashMap<String,HashMap<String, Integer>> emotionWordsOccurenceHashMap)
   {
      this.emotionWordsOccurenceHashMap = emotionWordsOccurenceHashMap;
   }
   /**
    * @return the emotionWordsOccurenceHashMap
    */
   public HashMap<String,HashMap<String, Integer>> getEmotionWordsOccurenceHashMap()
   {
      return emotionWordsOccurenceHashMap;
   }
   /**
    * @param tweetIDHashMap the tweetIDHashMap to set
    */
   public void setTweetIDHashMap(HashMap<Long,String> tweetIDHashMap)
   {
      this.tweetIDHashMap = tweetIDHashMap;
   }
   /**
    * @return the tweetIDHashMap
    */
   public HashMap<Long,String> getTweetIDHashMap()
   {
      return tweetIDHashMap;
   }
   /**
    * @param nVNHashMap the nVNHashMap to set
    */
   public void setNVNHashMap(HashMap<Long,String> nVNHashMap)
   {
      NVNHashMap = nVNHashMap;
   }
   /**
    * @return the nVNHashMap
    */
   public HashMap<Long,String> getNVNHashMap()
   {
      return NVNHashMap;
   }
   
}
