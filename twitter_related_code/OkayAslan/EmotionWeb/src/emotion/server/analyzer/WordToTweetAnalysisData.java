package emotion.server.analyzer;

import java.util.ArrayList;
import java.util.HashMap;

import emotion.shared.EmoTweet;

public class WordToTweetAnalysisData
{
   private String plainWord;
   private ArrayList<EmoTweet> foundTweetsArrayList = new ArrayList<EmoTweet>();
   private ArrayList<EmoTweet> foundNvNTweetsArrayList = new ArrayList<EmoTweet>();
   private HashMap<String,Integer> foundTweetOccurenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,Integer> foundNWordOccurenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,ArrayList<EmoTweet>> firstWordToNvNHashMap = new HashMap<String,ArrayList<EmoTweet>>();
   private HashMap<String,ArrayList<EmoTweet>> secondWordToNvNHashMap = new HashMap<String,ArrayList<EmoTweet>>();
   private HashMap<String,Integer> foundNvNOccurenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,Integer> adjectivesOccurenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,HashMap<String,String>> allWordPairToNvNHashMap = new HashMap<String,HashMap<String,String>>();
   
   /**
    * @return the plainWord
    */
   public String getPlainWord()
   {
      return plainWord;
   }
   /**
    * @param plainWord the plainWord to set
    */
   public void setPlainWord(String plainWord)
   {
      this.plainWord = plainWord;
   }
   /**
    * @return the foundTweetsArrayList
    */
   public ArrayList<EmoTweet> getFoundTweetsArrayList()
   {
      return foundTweetsArrayList;
   }
   /**
    * @param foundTweetsArrayList the foundTweetsArrayList to set
    */
   public void setFoundTweetsArrayList(ArrayList<EmoTweet> foundTweetsArrayList)
   {
      this.foundTweetsArrayList = foundTweetsArrayList;
   }
   /**
    * @return the foundPossibleNvNTweetsArrayList
    */
   public ArrayList<EmoTweet> getFoundPossibleNvNTweetsArrayList()
   {
      return foundNvNTweetsArrayList;
   }
   /**
    * @param foundPossibleNvNTweetsArrayList the foundPossibleNvNTweetsArrayList to set
    */
   public void setFoundPossibleNvNTweetsArrayList(ArrayList<EmoTweet> foundPossibleNvNTweetsArrayList)
   {
      this.foundNvNTweetsArrayList = foundPossibleNvNTweetsArrayList;
   }
   /**
    * @return the foundTweetOccurenceHashMap
    */
   public HashMap<String, Integer> getFoundTweetOccurenceHashMap()
   {
      return foundTweetOccurenceHashMap;
   }
   /**
    * @param foundTweetOccurenceHashMap the foundTweetOccurenceHashMap to set
    */
   public void setFoundTweetOccurenceHashMap(HashMap<String, Integer> foundTweetOccurenceHashMap)
   {
      this.foundTweetOccurenceHashMap = foundTweetOccurenceHashMap;
   }
   /**
    * @return the foundNWordOccurenceHashMap
    */
   public HashMap<String, Integer> getFoundNWordOccurenceHashMap()
   {
      return foundNWordOccurenceHashMap;
   }
   /**
    * @param foundNWordOccurenceHashMap the foundNWordOccurenceHashMap to set
    */
   public void setFoundNWordOccurenceHashMap(HashMap<String, Integer> foundNWordOccurenceHashMap)
   {
      this.foundNWordOccurenceHashMap = foundNWordOccurenceHashMap;
   }
   /**
    * @return the foundNvNTweetsArrayList
    */
   public ArrayList<EmoTweet> getFoundNvNTweetsArrayList()
   {
      return foundNvNTweetsArrayList;
   }
   /**
    * @param foundNvNTweetsArrayList the foundNvNTweetsArrayList to set
    */
   public void setFoundNvNTweetsArrayList(ArrayList<EmoTweet> foundNvNTweetsArrayList)
   {
      this.foundNvNTweetsArrayList = foundNvNTweetsArrayList;
   }
   /**
    * @return the firstWordToNvNHashMap
    */
   public HashMap<String, ArrayList<EmoTweet>> getFirstWordToNvNHashMap()
   {
      return firstWordToNvNHashMap;
   }
   /**
    * @param firstWordToNvNHashMap the firstWordToNvNHashMap to set
    */
   public void setFirstWordToNvNHashMap(HashMap<String, ArrayList<EmoTweet>> firstWordToNvNHashMap)
   {
      this.firstWordToNvNHashMap = firstWordToNvNHashMap;
   }
   /**
    * @return the secondWordToNvNHashMap
    */
   public HashMap<String, ArrayList<EmoTweet>> getSecondWordToNvNHashMap()
   {
      return secondWordToNvNHashMap;
   }
   /**
    * @param secondWordToNvNHashMap the secondWordToNvNHashMap to set
    */
   public void setSecondWordToNvNHashMap(HashMap<String, ArrayList<EmoTweet>> secondWordToNvNHashMap)
   {
      this.secondWordToNvNHashMap = secondWordToNvNHashMap;
   }
   /**
    * @return the foundNvNOccurenceHashMap
    */
   public HashMap<String, Integer> getFoundNvNOccurenceHashMap()
   {
      return foundNvNOccurenceHashMap;
   }
   /**
    * @param foundNvNOccurenceHashMap the foundNvNOccurenceHashMap to set
    */
   public void setFoundNvNOccurenceHashMap(HashMap<String, Integer> foundNvNOccurenceHashMap)
   {
      this.foundNvNOccurenceHashMap = foundNvNOccurenceHashMap;
   }
   /**
    * @param adjectivesOccurenceHashMap the adjectivesOccurenceHashMap to set
    */
   public void setAdjectivesOccurenceHashMap(HashMap<String,Integer> adjectivesOccurenceHashMap)
   {
      this.adjectivesOccurenceHashMap = adjectivesOccurenceHashMap;
   }
   /**
    * @return the adjectivesOccurenceHashMap
    */
   public HashMap<String,Integer> getAdjectivesOccurenceHashMap()
   {
      return adjectivesOccurenceHashMap;
   }
   /**
    * @param allWordPairToNvNHashMap the allWordPairToNvNHashMap to set
    */
   public void setAllWordPairToNvNHashMap(HashMap<String,HashMap<String,String>> allWordPairToNvNHashMap)
   {
      this.allWordPairToNvNHashMap = allWordPairToNvNHashMap;
   }
   /**
    * @return the allWordPairToNvNHashMap
    */
   public HashMap<String,HashMap<String,String>> getAllWordPairToNvNHashMap()
   {
      return allWordPairToNvNHashMap;
   }
    
}
