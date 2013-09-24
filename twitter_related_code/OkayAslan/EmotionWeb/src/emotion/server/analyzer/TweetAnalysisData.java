package emotion.server.analyzer;

import java.util.HashMap;

import emotion.shared.EmoNvNTriplets;
import emotion.shared.EmoTweet;

public class TweetAnalysisData
{
   private boolean nvn              = false;
   private EmoTweet emoTweet        = null;
   
   private HashMap<String,Integer> wordOccurenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,Integer> significantWordOccurenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,Integer> nWordOccurenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,Integer> namedEntityCandidateOccurenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,Integer> nounOccurenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,Integer> verbOccurenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,Integer> adjectiveOccurenceHashMap = new HashMap<String,Integer>();

   
   private HashMap<String,EmoNvNTriplets> nvnTripletsHashMap = new HashMap<String,EmoNvNTriplets>(); 
   
   private HashMap<String,EmoNvNTriplets> firstWordToNvNHashMap = new HashMap<String,EmoNvNTriplets>();
   private HashMap<String,EmoNvNTriplets> secondWordToNvNHashMap = new HashMap<String,EmoNvNTriplets>();
   
   /**
    * @param nvn the nvn to set
    */
   public void setNVN(boolean nvn)
   {
      this.nvn = nvn;
   }

   /**
    * @return the nvn
    */
   public boolean isNVN()
   {
      return nvn;
   }

   /**
    * @return the wordOccurenceHashMap
    */
   public HashMap<String, Integer> getWordOccurenceHashMap()
   {
      return wordOccurenceHashMap;
   }

   /**
    * @param wordOccurenceHashMap the wordOccurenceHashMap to set
    */
   public void setWordOccurenceHashMap(HashMap<String, Integer> wordOccurenceHashMap)
   {
      this.wordOccurenceHashMap = wordOccurenceHashMap;
   }

   /**
    * @return the significantWordOccurenceHashMap
    */
   public HashMap<String, Integer> getSignificantWordOccurenceHashMap()
   {
      return significantWordOccurenceHashMap;
   }

   /**
    * @param significantWordOccurenceHashMap the significantWordOccurenceHashMap to set
    */
   public void setSignificantWordOccurenceHashMap(HashMap<String, Integer> significantWordOccurenceHashMap)
   {
      this.significantWordOccurenceHashMap = significantWordOccurenceHashMap;
   }

   /**
    * @return the nWordOccurenceHashMap
    */
   public HashMap<String, Integer> getnWordOccurenceHashMap()
   {
      return nWordOccurenceHashMap;
   }

   /**
    * @param nWordOccurenceHashMap the nWordOccurenceHashMap to set
    */
   public void setnWordOccurenceHashMap(HashMap<String, Integer> nWordOccurenceHashMap)
   {
      this.nWordOccurenceHashMap = nWordOccurenceHashMap;
   }

   /**
    * @return the namedEntityCandidateOccurenceHashMap
    */
   public HashMap<String, Integer> getNamedEntityCandidateOccurenceHashMap()
   {
      return namedEntityCandidateOccurenceHashMap;
   }

   /**
    * @param namedEntityCandidateOccurenceHashMap the namedEntityCandidateOccurenceHashMap to set
    */
   public void setNamedCandidateEntityOccurenceHashMap(HashMap<String, Integer> namedEntityCandidateOccurenceHashMap)
   {
      this.namedEntityCandidateOccurenceHashMap = namedEntityCandidateOccurenceHashMap;
   }

   /**
    * @return the nounOccurenceHashMap
    */
   public HashMap<String, Integer> getNounOccurenceHashMap()
   {
      return nounOccurenceHashMap;
   }

   /**
    * @param nounOccurenceHashMap the nounOccurenceHashMap to set
    */
   public void setNounOccurenceHashMap(HashMap<String, Integer> nounOccurenceHashMap)
   {
      this.nounOccurenceHashMap = nounOccurenceHashMap;
   }

   /**
    * @return the verbOccurenceHashMap
    */
   public HashMap<String, Integer> getVerbOccurenceHashMap()
   {
      return verbOccurenceHashMap;
   }

   /**
    * @param verbOccurenceHashMap the verbOccurenceHashMap to set
    */
   public void setVerbOccurenceHashMap(HashMap<String, Integer> verbOccurenceHashMap)
   {
      this.verbOccurenceHashMap = verbOccurenceHashMap;
   }

   /**
    * @return the adjectiveOccurenceHashMap
    */
   public HashMap<String, Integer> getAdjectiveOccurenceHashMap()
   {
      return adjectiveOccurenceHashMap;
   }

   /**
    * @param adjectiveOccurenceHashMap the adjectiveOccurenceHashMap to set
    */
   public void setAdjectiveOccurenceHashMap(HashMap<String, Integer> adjectiveOccurenceHashMap)
   {
      this.adjectiveOccurenceHashMap = adjectiveOccurenceHashMap;
   }

   /**
    * @return the nvnTripletsHashMap
    */
   public HashMap<String, EmoNvNTriplets> getNVNTripletsHashMap()
   {
      return nvnTripletsHashMap;
   }

   /**
    * @param nvnTripletsHashMap the nvnTripletsHashMap to set
    */
   public void setNVNTripletsHashMap(HashMap<String, EmoNvNTriplets> nvnTripletsHashMap)
   {
      this.nvnTripletsHashMap = nvnTripletsHashMap;
   }

   /**
    * @param emoTweet the emoTweet to set
    */
   public void setEmoTweet(EmoTweet emoTweet)
   {
      this.emoTweet = emoTweet;
   }

   /**
    * @return the emoTweet
    */
   public EmoTweet getEmoTweet()
   {
      return emoTweet;
   }

   /**
    * @return the firstWordToNvNHashMap
    */
   public HashMap<String, EmoNvNTriplets> getFirstWordToNvNHashMap()
   {
      return firstWordToNvNHashMap;
   }

   /**
    * @param firstWordToNvNHashMap the firstWordToNvNHashMap to set
    */
   public void setFirstWordToNvNHashMap(HashMap<String, EmoNvNTriplets> firstWordToNvNHashMap)
   {
      this.firstWordToNvNHashMap = firstWordToNvNHashMap;
   }

   /**
    * @return the secondWordToNvNHashMap
    */
   public HashMap<String, EmoNvNTriplets> getSecondWordToNvNHashMap()
   {
      return secondWordToNvNHashMap;
   }

   /**
    * @param secondWordToNvNHashMap the secondWordToNvNHashMap to set
    */
   public void setSecondWordToNvNHashMap(HashMap<String, EmoNvNTriplets> secondWordToNvNHashMap)
   {
      this.secondWordToNvNHashMap = secondWordToNvNHashMap;
   }
   
}
