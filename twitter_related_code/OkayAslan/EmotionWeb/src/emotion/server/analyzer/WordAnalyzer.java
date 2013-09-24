package emotion.server.analyzer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import emotion.server.util.HashMapUtil;
import emotion.server.util.WordUtil;
import emotion.shared.EmoTweetWordData;

public class WordAnalyzer
{
  
   private HashMap<String,HashMap<String, Integer>> dateSearchWordsOccurenceHashMap = null;  
   private HashMap<String,HashMap<String, Integer>> dateCheckWordsOccurenceHashMap = null;
   private HashMap<String,HashMap<String, Integer>> dateAllWordsOccurenceHashMap = null;
   private HashMap<String,HashMap<String, EmoTweetWordData>> dateNounWordHashMap = null;
   private LinkedHashMap<String, String> dateHashMap = new LinkedHashMap<String, String>();
   private HashMapUtil hMapUtil = new HashMapUtil();
   private AnalysisData analysisData = null;
   private WordUtil wordUtil = new WordUtil();
 
   public WordAnalyzer(LinkedHashMap<String, String> dateHashMap, HashMap<String, HashMap<String, Integer>> dateSearchWordsOccurenceHashMap,HashMap<String, HashMap<String, Integer>> dateCheckWordsOccurenceHashMap, HashMap<String,HashMap<String,Integer>> dateAllWordsOccurenceHashMap, HashMap<String,HashMap<String,EmoTweetWordData>> dateNounWordHashMap)
   {
      this.dateHashMap = dateHashMap;
      this.dateSearchWordsOccurenceHashMap = dateSearchWordsOccurenceHashMap;
      this.dateCheckWordsOccurenceHashMap = dateCheckWordsOccurenceHashMap;
      this.dateAllWordsOccurenceHashMap = dateAllWordsOccurenceHashMap;
      this.dateNounWordHashMap = dateNounWordHashMap;
   }  
   
   public WordAnalyzer(AnalysisData analysisData)
   {
      this.analysisData = analysisData;
   }
   
   public void analyzeWords()
   {
      LinkedHashMap<String, Integer> allNwordsOccurenceHashMap = hMapUtil.sortEmotionHashMapByValue(analysisData.getAllnWordOccurrenceHashMap());
      int maxVal = hMapUtil.getFirstValue(allNwordsOccurenceHashMap);
      Iterator<String> allNwordsOccurenceHashMapIterator = allNwordsOccurenceHashMap.keySet().iterator();
      while(allNwordsOccurenceHashMapIterator.hasNext()) 
      {
         String key = allNwordsOccurenceHashMapIterator.next();
         Integer val = allNwordsOccurenceHashMap.get(key);
         if(!wordUtil.isTimeWord(key) && wordUtil.isAvailable(key))
         {
            if(val >= (maxVal/250) )
            {
               if(val >= (maxVal/88))
               {
                  analysisData.getSearchWordsOccurrenceHashMap().put(key, val);  
               }
               analysisData.getCheckWordsOccurrenceHashMap().put(key, val);
            }
         }   
      }
   }   

   public void analyzeWordsByData()
   {
      Iterator<String> HashmapIterator = dateHashMap.keySet().iterator();
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next(); 
         prepareWords(key,hMapUtil.sortEmotionHashMapByValue(dateAllWordsOccurenceHashMap.get(key)),dateNounWordHashMap.get(key));
      } 
   }

   /**
    * @param dateKey 
    * @param wordsOccurenceHashMap
    * @param nounWordHashMap
    */
   private void prepareWords(String dateKey, HashMap<String, Integer> wordsOccurenceHashMap, HashMap<String, EmoTweetWordData> nounWordHashMap)
   {
      int maxVal = hMapUtil.getFirstValue(wordsOccurenceHashMap);
      HashMap<String, Integer> searchMap = new HashMap<String, Integer>();
      HashMap<String, Integer> checkMap = new HashMap<String, Integer>();
      Iterator<String> HashmapIterator = wordsOccurenceHashMap.keySet().iterator();
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next();
         Integer val = wordsOccurenceHashMap.get(key);
         if(nounWordHashMap.containsKey(key) && val >= (maxVal/250) )
         {
            if(val >= (maxVal/88))
            {
               searchMap.put(key, val);
            }
            checkMap.put(key, val);
         }    
      }
      dateSearchWordsOccurenceHashMap.put(dateKey, searchMap);
      dateCheckWordsOccurenceHashMap.put(dateKey, checkMap);
   }




}
