package emotion.server.analyzer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.tools.ant.util.DateUtils;
import org.joda.time.DateTime;

import emotion.shared.EmoNvNTriplets;
import emotion.shared.EmoTweet;

public class AnalysisData
{
   private String searchedWord;
   private int countRTs=0;
   private int countNVNRTs=0;
   private int chosenNVNword=0;
   private int chosenNVNverb=0;
   
//   
   private int countAllWords=0;
   private int countAllSignificantWords=0;
   private int countAllnWords=0;
   private int countAllVerbs=0;
   
   private int countAllNVNWords=0;
   private int countAllNVNSignificantWords=0;
   private int countAllNVNnWords=0;
   private int countAllNVNVerbs=0;     
   
  //
   
   private Date maxDate = new Date(Long.MIN_VALUE);
   private Date minDate = new Date(Long.MAX_VALUE);   
   
   private ArrayList<EmoTweet> allTweetArrayList = new ArrayList<EmoTweet>();
   private HashMap<String,Integer> allDistinctEmoTweetsHashMap = new HashMap<String,Integer>();
   
   private HashMap<Long,EmoTweet> allNVNTweetsHashMap = new HashMap<Long,EmoTweet>();
   private HashMap<String,Integer> allDistinctNVNEmoTweetsHashMap = new HashMap<String,Integer>(); 
   
   private HashMap<String,Integer> allWordOccurrenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,Integer> allNVNWordOccurrenceHashMap = new HashMap<String,Integer>();
   
   private HashMap<String,Integer> allSignificantWordOccurrenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,Integer> allNVNSignificantWordOccurrenceHashMap = new HashMap<String,Integer>();   ///
   
   private HashMap<String,Integer> allnWordOccurrenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,Integer> allNVNnWordOccurrenceHashMap = new HashMap<String,Integer>();  ///
   
   private HashMap<String,Integer> allVerbOccurrenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,Integer> allNVNVerbOccurrenceHashMap = new HashMap<String,Integer>();
   
   private HashMap<String,String> wordsToPlainHashMap = new HashMap<String,String>();
   private HashMap<String,WordToTweetAnalysisData> wordtoTweetDataHashMap = new HashMap<String,WordToTweetAnalysisData>();
   private HashMap<String,Integer> allPlainWordOccurrenceHashMap = new HashMap<String,Integer>();
   
   private HashMap<String,ArrayList<EmoNvNTriplets>> keyToNvNTripletsArrListHashMap = new HashMap<String,ArrayList<EmoNvNTriplets>>();
   private HashMap<Long,ArrayList<EmoNvNTriplets>> tweetIdToNvNTripletsArrListHashMap = new HashMap<Long,ArrayList<EmoNvNTriplets>>();
   
   private HashMap<String,Integer> nounWordOccurrenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,Integer> namedEntityCandidateOccurrenceHashMap = new HashMap<String,Integer>();
   
   private HashMap<String,ArrayList<EmoTweet>> nvnToEmoTweetsHashMap = new HashMap<String,ArrayList<EmoTweet>>();
   
   private LinkedHashMap<String,Integer> searchWordsOccurrenceHashMap = new LinkedHashMap<String,Integer>();
   private LinkedHashMap<String,Integer> checkWordsOccurrenceHashMap = new LinkedHashMap<String,Integer>();
   
   private LinkedHashMap<String, String> resultPairHashMap = new LinkedHashMap<String, String>();
   private LinkedHashMap<String, ArrayList<String>> actualValueResultPairHashMap = new LinkedHashMap<String, ArrayList<String>>();
   
   /**
    * @return the searchedWord
    */
   public String getSearchedWord()
   {
      return searchedWord;
   }
   /**
    * @param searchedWord the searchedWord to set
    */
   public void setSearchedWord(String searchedWord)
   {
      this.searchedWord = searchedWord;
   }
   /**
    * @return the allTweetArrayList
    */
   public ArrayList<EmoTweet> getAllTweetArrayList()
   {
      return allTweetArrayList;
   }
   /**
    * @param allTweetArrayList the allTweetArrayList to set
    */
   public void setAllTweetArrayList(ArrayList<EmoTweet> allTweetArrayList)
   {
      this.allTweetArrayList = allTweetArrayList;
   }
   /**
    * @return the wordsToPlainHashMap
    */
   public HashMap<String, String> getWordsToPlainHashMap()
   {
      return wordsToPlainHashMap;
   }
   /**
    * @param wordsToPlainHashMap the wordsToPlainHashMap to set
    */
   public void setWordsToPlainHashMap(HashMap<String, String> wordsToPlainHashMap)
   {
      this.wordsToPlainHashMap = wordsToPlainHashMap;
   }
   /**
    * @return the wordtoTweetDataHashMap
    */
   public HashMap<String, WordToTweetAnalysisData> getWordtoTweetDataHashMap()
   {
      return wordtoTweetDataHashMap;
   }
   /**
    * @param wordtoTweetDataHashMap the wordtoTweetDataHashMap to set
    */
   public void setWordtoTweetDataHashMap(HashMap<String, WordToTweetAnalysisData> wordtoTweetDataHashMap)
   {
      this.wordtoTweetDataHashMap = wordtoTweetDataHashMap;
   }
   /**
    * @return the allSignificantWordOccurrenceHashMap
    */
   public HashMap<String, Integer> getAllSignificantWordOccurrenceHashMap()
   {
      return allSignificantWordOccurrenceHashMap;
   }
   /**
    * @param allSignificantWordOccurrenceHashMap the allSignificantWordOccurrenceHashMap to set
    */
   public void setAllSignificantWordOccurrenceHashMap(HashMap<String, Integer> allSignificantWordOccurrenceHashMap)
   {
      this.allSignificantWordOccurrenceHashMap = allSignificantWordOccurrenceHashMap;
   }
   /**
    * @return the nounWordOccurrenceHashMap
    */
   public HashMap<String, Integer> getNounWordOccurrenceHashMap()
   {
      return nounWordOccurrenceHashMap;
   }
   /**
    * @param nounWordOccurrenceHashMap the nounWordOccurrenceHashMap to set
    */
   public void setNounWordOccurrenceHashMap(HashMap<String, Integer> nounWordOccurrenceHashMap)
   {
      this.nounWordOccurrenceHashMap = nounWordOccurrenceHashMap;
   }
   /**
    * @return the namedEntityCandidateOccurrenceHashMap
    */
   public HashMap<String, Integer> getNamedEntityCandidateOccurrenceHashMap()
   {
      return namedEntityCandidateOccurrenceHashMap;
   }
   /**
    * @param namedEntityCandidateOccurrenceHashMap the namedEntityCandidateOccurrenceHashMap to set
    */
   public void setNamedEntityCandidateOccurrenceHashMap(HashMap<String, Integer> namedEntityCandidateOccurrenceHashMap)
   {
      this.namedEntityCandidateOccurrenceHashMap = namedEntityCandidateOccurrenceHashMap;
   }
   /**
    * @param allPlainWordOccurrenceHashMap the allPlainWordOccurrenceHashMap to set
    */
   public void setAllPlainWordOccurrenceHashMap(HashMap<String,Integer> allPlainWordOccurrenceHashMap)
   {
      this.allPlainWordOccurrenceHashMap = allPlainWordOccurrenceHashMap;
   }
   /**
    * @return the allPlainWordOccurrenceHashMap
    */
   public HashMap<String,Integer> getAllPlainWordOccurrenceHashMap()
   {
      return allPlainWordOccurrenceHashMap;
   }
   /**
    * @param allnWordOccurrenceHashMap the allNWordOccurrenceHashMap to set
    */
   public void setAllnWordOccurrenceHashMap(HashMap<String,Integer> allnWordOccurrenceHashMap)
   {
      this.allnWordOccurrenceHashMap = allnWordOccurrenceHashMap;
   }
   /**
    * @return the allnWordOccurrenceHashMap
    */
   public HashMap<String,Integer> getAllnWordOccurrenceHashMap()
   {
      return allnWordOccurrenceHashMap;
   }
   /**
    * @return the searchWordsOccurrenceHashMap
    */
   public LinkedHashMap<String, Integer> getSearchWordsOccurrenceHashMap()
   {
      return searchWordsOccurrenceHashMap;
   }
   /**
    * @param searchWordsOccurrenceHashMap the searchWordsOccurrenceHashMap to set
    */
   public void setSearchWordsOccurrenceHashMap(LinkedHashMap<String, Integer> searchWordsOccurrenceHashMap)
   {
      this.searchWordsOccurrenceHashMap = searchWordsOccurrenceHashMap;
   }
   /**
    * @return the checkWordsOccurrenceHashMap
    */
   public LinkedHashMap<String, Integer> getCheckWordsOccurrenceHashMap()
   {
      return checkWordsOccurrenceHashMap;
   }
   /**
    * @param checkWordsOccurrenceHashMap the checkWordsOccurrenceHashMap to set
    */
   public void setCheckWordsOccurrenceHashMap(LinkedHashMap<String, Integer> checkWordsOccurrenceHashMap)
   {
      this.checkWordsOccurrenceHashMap = checkWordsOccurrenceHashMap;
   }
   /**
    * @param resultPairHashMap the resultPairHashMap to set
    */
   public void setResultPairHashMap(LinkedHashMap<String, String> resultPairHashMap)
   {
      this.resultPairHashMap = resultPairHashMap;
   }
   /**
    * @return the resultPairHashMap
    */
   public LinkedHashMap<String, String> getResultPairHashMap()
   {
      return resultPairHashMap;
   }
   /**
    * @param actualValueResultPairHashMap the actualValueResultPairHashMap to set
    */
   public void setActualValueResultPairHashMap(LinkedHashMap<String, ArrayList<String>> actualValueResultPairHashMap)
   {
      this.actualValueResultPairHashMap = actualValueResultPairHashMap;
   }
   /**
    * @return the actualValueResultPairHashMap
    */
   public LinkedHashMap<String, ArrayList<String>> getActualValueResultPairHashMap()
   {
      return actualValueResultPairHashMap;
   }
   /**
    * @param allVerbOccurrenceHashMap the allVerbOccurrenceHashMap to set
    */
   public void setAllVerbOccurrenceHashMap(HashMap<String,Integer> allVerbOccurrenceHashMap)
   {
      this.allVerbOccurrenceHashMap = allVerbOccurrenceHashMap;
   }
   /**
    * @return the allVerbOccurrenceHashMap
    */
   public HashMap<String,Integer> getAllVerbOccurrenceHashMap()
   {
      return allVerbOccurrenceHashMap;
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
    * @return the allNVNWordOccurrenceHashMap
    */
   public HashMap<String, Integer> getAllNVNWordOccurrenceHashMap()
   {
      return allNVNWordOccurrenceHashMap;
   }
   /**
    * @param allNVNWordOccurrenceHashMap the allNVNWordOccurrenceHashMap to set
    */
   public void setAllNVNWordOccurrenceHashMap(HashMap<String, Integer> allNVNWordOccurrenceHashMap)
   {
      this.allNVNWordOccurrenceHashMap = allNVNWordOccurrenceHashMap;
   }
   /**
    * @return the allNVNVerbOccurrenceHashMap
    */
   public HashMap<String, Integer> getAllNVNVerbOccurrenceHashMap()
   {
      return allNVNVerbOccurrenceHashMap;
   }
   /**
    * @param allNVNVerbOccurrenceHashMap the allNVNVerbOccurrenceHashMap to set
    */
   public void setAllNVNVerbOccurrenceHashMap(HashMap<String, Integer> allNVNVerbOccurrenceHashMap)
   {
      this.allNVNVerbOccurrenceHashMap = allNVNVerbOccurrenceHashMap;
   }
   /**
    * @return the countRTs
    */
   public int getCountRTs()
   {
      return countRTs;
   }
   /**
    * @param countRTs the countRTs to set
    */
   public void setCountRTs(int countRTs)
   {
      this.countRTs = countRTs;
   }
   /**
    * @return the countNVNRTs
    */
   public int getCountNVNRTs()
   {
      return countNVNRTs;
   }
   /**
    * @param countNVNRTs the countNVNRTs to set
    */
   public void setCountNVNRTs(int countNVNRTs)
   {
      this.countNVNRTs = countNVNRTs;
   }
   /**
    * @return the chosenNVNword
    */
   public int getChosenNVNword()
   {
      return chosenNVNword;
   }
   /**
    * @param chosenNVNword the chosenNVNword to set
    */
   public void setChosenNVNword(int chosenNVNword)
   {
      this.chosenNVNword = chosenNVNword;
   }
   /**
    * @return the chosenNVNverb
    */
   public int getChosenNVNverb()
   {
      return chosenNVNverb;
   }
   /**
    * @param chosenNVNverb the chosenNVNverb to set
    */
   public void setChosenNVNverb(int chosenNVNverb)
   {
      this.chosenNVNverb = chosenNVNverb;
   }
   /**
    * @param allDistinctEmoTweets the allDistinctEmoTweets to set
    */
   public void setAllDistinctEmoTweetsHashMap(HashMap<String,Integer> allDistinctEmoTweetsHashMap)
   {
      this.allDistinctEmoTweetsHashMap = allDistinctEmoTweetsHashMap;
   }
   /**
    * @return the allDistinctEmoTweets
    */
   public HashMap<String,Integer> getAllDistinctEmoTweetsHashMap()
   {
      return allDistinctEmoTweetsHashMap;
   }
   /**
    * @param allWordOccurrenceHashMap the allWordOccurrenceHashMap to set
    */
   public void setAllWordOccurrenceHashMap(HashMap<String,Integer> allWordOccurrenceHashMap)
   {
      this.allWordOccurrenceHashMap = allWordOccurrenceHashMap;
   }
   /**
    * @return the allWordOccurrenceHashMap
    */
   public HashMap<String,Integer> getAllWordOccurrenceHashMap()
   {
      return allWordOccurrenceHashMap;
   }
   /**
    * @param allNVNnWordOccurrenceHashMap the allNVNnWordOccurrenceHashMap to set
    */
   public void setAllNVNnWordOccurrenceHashMap(HashMap<String,Integer> allNVNnWordOccurrenceHashMap)
   {
      this.allNVNnWordOccurrenceHashMap = allNVNnWordOccurrenceHashMap;
   }
   /**
    * @return the allNVNnWordOccurrenceHashMap
    */
   public HashMap<String,Integer> getAllNVNnWordOccurrenceHashMap()
   {
      return allNVNnWordOccurrenceHashMap;
   }
   /**
    * @param allNVNSignificantWordOccurrenceHashMap the allNVNSignificantWordOccurrenceHashMap to set
    */
   public void setAllNVNSignificantWordOccurrenceHashMap(HashMap<String,Integer> allNVNSignificantWordOccurrenceHashMap)
   {
      this.allNVNSignificantWordOccurrenceHashMap = allNVNSignificantWordOccurrenceHashMap;
   }
   /**
    * @return the allNVNSignificantWordOccurrenceHashMap
    */
   public HashMap<String,Integer> getAllNVNSignificantWordOccurrenceHashMap()
   {
      return allNVNSignificantWordOccurrenceHashMap;
   }
   /**
    * @param allDistinctNVNEmoTweetsHashMap the allDistinctNVNEmoTweetsHashMap to set
    */
   public void setAllDistinctNVNEmoTweetsHashMap(HashMap<String,Integer> allDistinctNVNEmoTweetsHashMap)
   {
      this.allDistinctNVNEmoTweetsHashMap = allDistinctNVNEmoTweetsHashMap;
   }
   /**
    * @return the allDistinctNVNEmoTweetsHashMap
    */
   public HashMap<String,Integer> getAllDistinctNVNEmoTweetsHashMap()
   {
      return allDistinctNVNEmoTweetsHashMap;
   }
   /**
    * @return the countAllWords
    */
   public int getCountAllWords()
   {
      return countAllWords;
   }
   /**
    * @param countAllWords the countAllWords to set
    */
   public void setCountAllWords(int countAllWords)
   {
      this.countAllWords = countAllWords;
   }
   /**
    * @return the countAllSignificantWords
    */
   public int getCountAllSignificantWords()
   {
      return countAllSignificantWords;
   }
   /**
    * @param countAllSignificantWords the countAllSignificantWords to set
    */
   public void setCountAllSignificantWords(int countAllSignificantWords)
   {
      this.countAllSignificantWords = countAllSignificantWords;
   }
   /**
    * @return the countAllNwords
    */
   public int getCountAllnWords()
   {
      return countAllnWords;
   }
   /**
    * @param countAllNwords the countAllNwords to set
    */
   public void setCountAllnWords(int countAllnWords)
   {
      this.countAllnWords = countAllnWords;
   }
   /**
    * @return the countAllVerbs
    */
   public int getCountAllVerbs()
   {
      return countAllVerbs;
   }
   /**
    * @param countAllVerbs the countAllVerbs to set
    */
   public void setCountAllVerbs(int countAllVerbs)
   {
      this.countAllVerbs = countAllVerbs;
   }
   /**
    * @return the countAllNVNWords
    */
   public int getCountAllNVNWords()
   {
      return countAllNVNWords;
   }
   /**
    * @param countAllNVNWords the countAllNVNWords to set
    */
   public void setCountAllNVNWords(int countAllNVNWords)
   {
      this.countAllNVNWords = countAllNVNWords;
   }
   /**
    * @return the countAllNVNSignificantWords
    */
   public int getCountAllNVNSignificantWords()
   {
      return countAllNVNSignificantWords;
   }
   /**
    * @param countAllNVNSignificantWords the countAllNVNSignificantWords to set
    */
   public void setCountAllNVNSignificantWords(int countAllNVNSignificantWords)
   {
      this.countAllNVNSignificantWords = countAllNVNSignificantWords;
   }
   /**
    * @return the countAllNVNnWords
    */
   public int getCountAllNVNnWords()
   {
      return countAllNVNnWords;
   }
   /**
    * @param countAllNVNnWords the countAllNVNnWords to set
    */
   public void setCountAllNVNNwords(int countAllNVNnWords)
   {
      this.countAllNVNnWords = countAllNVNnWords;
   }
   /**
    * @return the countAllNVNVerbs
    */
   public int getCountAllNVNVerbs()
   {
      return countAllNVNVerbs;
   }
   /**
    * @param countAllNVNVerbs the countAllNVNVerbs to set
    */
   public void setCountAllNVNVerbs(int countAllNVNVerbs)
   {
      this.countAllNVNVerbs = countAllNVNVerbs;
   }
   /**
    * @param keyToNvNTripletsArrListHashMap the keyToNvNTripletsArrListHashMap to set
    */
   public void setKeyToNvNTripletsArrListHashMap(HashMap<String,ArrayList<EmoNvNTriplets>> keyToNvNTripletsArrListHashMap)
   {
      this.keyToNvNTripletsArrListHashMap = keyToNvNTripletsArrListHashMap;
   }
   /**
    * @return the keyToNvNTripletsArrListHashMap
    */
   public HashMap<String,ArrayList<EmoNvNTriplets>> getKeyToNvNTripletsArrListHashMap()
   {
      return keyToNvNTripletsArrListHashMap;
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
   /**
    * @param nvnToEmoTweetsHashMap the nvnToEmoTweetsHashMap to set
    */
   public void setNvnToEmoTweetsHashMap(HashMap<String,ArrayList<EmoTweet>> nvnToEmoTweetsHashMap)
   {
      this.nvnToEmoTweetsHashMap = nvnToEmoTweetsHashMap;
   }
   /**
    * @return the nvnToEmoTweetsHashMap
    */
   public HashMap<String,ArrayList<EmoTweet>> getNvnToEmoTweetsHashMap()
   {
      return nvnToEmoTweetsHashMap;
   }
   /**
    * @return the maxDate
    */
   public Date getMaxDate()
   {
      return maxDate;
   }
   /**
    * @param maxDate the maxDate to set
    */
   public void setMaxDate(Date maxDate)
   {
      this.maxDate = maxDate;
   }
   /**
    * @return the minDate
    */
   public Date getMinDate()
   {
      return minDate;
   }
   /**
    * @param minDate the minDate to set
    */
   public void setMinDate(Date minDate)
   {
      this.minDate = minDate;
   }
}
