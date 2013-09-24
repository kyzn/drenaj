package emotion.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class EmoResultDO implements Serializable
{
   private static final long serialVersionUID = 1L;
   
   private LinkedHashMap<String,ArrayList<EmoResultValue>> emoResultValues = null;
   private ArrayList<EmoResultValue> emoResultValueArrayList = new ArrayList<EmoResultValue>();
   private ArrayList<String> hashTags = null;
   
   private boolean semantic  = false;
   private HashMap<Long,EmoTweet> allNVNTweetsHashMap = new HashMap<Long,EmoTweet>();
   private HashMap<Long,ArrayList<EmoNvNTriplets>> tweetIdToNvNTripletsArrListHashMap = new HashMap<Long,ArrayList<EmoNvNTriplets>>();
   
   
   /**
    * @return the emoResultValues
    */
   public LinkedHashMap<String, ArrayList<EmoResultValue>> getEmoResultValues()
   {
      return emoResultValues;
   }
   /**
    * @return the hashTags
    */
   public ArrayList<String> getHashTags()
   {
      return hashTags;
   }
   /**
    * @param emoResultValues the emoResultValues to set
    */
   public void setEmoResultValues(LinkedHashMap<String, ArrayList<EmoResultValue>> emoResultValues)
   {
      this.emoResultValues = emoResultValues;
   }
   /**
    * @param hashTags the hashTags to set
    */
   public void setHashTags(ArrayList<String> hashTags)
   {
      this.hashTags = hashTags;
   }
   /**
    * @param emoResultValueArrayList the emoResultValueArrayList to set
    */
   public void setEmoResultValueArrayList(ArrayList<EmoResultValue> emoResultValueArrayList)
   {
      this.emoResultValueArrayList = emoResultValueArrayList;
   }
   /**
    * @return the emoResultValueArrayList
    */
   public ArrayList<EmoResultValue> getEmoResultValueArrayList()
   {
      return emoResultValueArrayList;
   }
   /**
    * @param semantic the semantic to set
    */
   public void setSemantic(boolean semantic)
   {
      this.semantic = semantic;
   }
   /**
    * @return the semantic
    */
   public boolean isSemantic()
   {
      return semantic;
   }
   /**
    * @param allNVNTweetsHashMap the allNVNTweetsHashMap to set
    */
   public void setAllNVNTweetsHashMap(HashMap<Long,EmoTweet> allNVNTweetsHashMap)
   {
      this.allNVNTweetsHashMap = allNVNTweetsHashMap;
   }
   /**
    * @return the allNVNTweetsHashMap
    */
   public HashMap<Long,EmoTweet> getAllNVNTweetsHashMap()
   {
      return allNVNTweetsHashMap;
   }
   /**
    * @param tweetIdToNvNTripletsArrListHashMap the tweetIdToNvNTripletsArrListHashMap to set
    */
   public void setTweetIdToNvNTripletsArrListHashMap(HashMap<Long,ArrayList<EmoNvNTriplets>> tweetIdToNvNTripletsArrListHashMap)
   {
      this.tweetIdToNvNTripletsArrListHashMap = tweetIdToNvNTripletsArrListHashMap;
   }
   /**
    * @return the tweetIdToNvNTripletsArrListHashMap
    */
   public HashMap<Long,ArrayList<EmoNvNTriplets>> getTweetIdToNvNTripletsArrListHashMap()
   {
      return tweetIdToNvNTripletsArrListHashMap;
   }

}
