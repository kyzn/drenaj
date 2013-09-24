package emotion.server.analyzer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;

import emotion.server.util.HashMapUtil;
import emotion.server.util.WordUtil;

public class EmotionAnalyzer
{
   private WordUtil wordUtil = new WordUtil();
   private AnalysisData analysisData = null;
   private HashMapUtil hmapUtil = new HashMapUtil();

   public EmotionAnalyzer(AnalysisData analysisData)
   {
      this.analysisData = analysisData;
   }

/*   
   public void analyzeEmotions(BaseResultAnalysisData overallBaseResultAnalysisData)
   {
      HashMap<String, ResultAnalysisData> resAnalysisDataHashMap = overallBaseResultAnalysisData.getResultAnalysisDataHashMap();
      HashMap<String, String> wordsToPlainHashMap = analysisData.getWordsToPlainHashMap();
      HashMap<String,Integer> plainWordOccurrenceHashMap = analysisData.getAllPlainWordOccurrenceHashMap();
      HashMap<String,Integer> allWordOccurrenceHashMap = analysisData.getAllWordOccurrenceHashMap();
      Iterator<String> resAnalysisDataHashmapIterator = resAnalysisDataHashMap.keySet().iterator();
      while(resAnalysisDataHashmapIterator.hasNext()) 
      {
         String key = resAnalysisDataHashmapIterator.next(); 
         ResultAnalysisData resultAnalysisData = resAnalysisDataHashMap.get(key);
         HashMap<String, String> distinctEmoTweetHashMap =  resultAnalysisData.getDistinctEmoTweets();
         Iterator<String> distinctEmoTweetHashMapIterator = distinctEmoTweetHashMap.keySet().iterator();
         while(distinctEmoTweetHashMapIterator.hasNext()) 
         {
            String tweetText = distinctEmoTweetHashMapIterator.next();
            StringTokenizer st = new StringTokenizer(tweetText);
            while (st.hasMoreTokens()) 
            {
               String currentWord = st.nextToken().toLowerCase(Locale.ENGLISH);
               currentWord = wordUtil.clearPunctuation(currentWord);
               if(currentWord!=null)
               { 
                  if(!wordUtil.isSimplifiedStopWord(currentWord) && wordUtil.isAvailable(currentWord) )
                  {
                     if(wordsToPlainHashMap.containsKey(currentWord))
                     {
                        String plainCurrentWord = wordsToPlainHashMap.get(currentWord);
                        if(wordUtil.isAngerWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByOne("Anger", resultAnalysisData.getEmotionOccHashMap()); 
                        }
                        else if(wordUtil.isFearWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByOne("Fear", resultAnalysisData.getEmotionOccHashMap()); 
                        }
                        else if(wordUtil.isSadnessWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByOne("Sadness", resultAnalysisData.getEmotionOccHashMap()); 
                        }
                        else if(wordUtil.isSurpriseWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByOne("Surprise", resultAnalysisData.getEmotionOccHashMap()); 
                        }
                        else if(wordUtil.isDisgustWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByOne("Disgust", resultAnalysisData.getEmotionOccHashMap()); 
                        }
                        else if(wordUtil.isJoyWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByOne("Joy", resultAnalysisData.getEmotionOccHashMap()); 
                        }
                     }
                  }
               }
            }
         }   
      }
   }
*/   
   
   public void analyzeEmotions(BaseResultAnalysisData overallBaseResultAnalysisData)
   {
      HashMap<String, ResultAnalysisData> resAnalysisDataHashMap = overallBaseResultAnalysisData.getResultAnalysisDataHashMap();
      HashMap<String, String> wordsToPlainHashMap = analysisData.getWordsToPlainHashMap();
  //    HashMap<String,Integer> plainWordOccurrenceHashMap = analysisData.getAllPlainWordOccurrenceHashMap();
  //    HashMap<String,Integer> allWordOccurrenceHashMap = analysisData.getAllWordOccurrenceHashMap();
      Iterator<String> resAnalysisDataHashmapIterator = resAnalysisDataHashMap.keySet().iterator();
      while(resAnalysisDataHashmapIterator.hasNext()) 
      {
         String key = resAnalysisDataHashmapIterator.next(); 
         ResultAnalysisData resultAnalysisData = resAnalysisDataHashMap.get(key);
         HashMap<String, Integer> distinctEmoTweetHashMap =  resultAnalysisData.getDistinctEmoTweets();
         Iterator<String> distinctEmoTweetHashMapIterator = distinctEmoTweetHashMap.keySet().iterator();
         while(distinctEmoTweetHashMapIterator.hasNext()) 
         {
            String tweetText = distinctEmoTweetHashMapIterator.next();
            Integer occurence = distinctEmoTweetHashMap.get(tweetText);
            StringTokenizer st = new StringTokenizer(tweetText);
            while (st.hasMoreTokens()) 
            {
               String currentWord = st.nextToken().toLowerCase(Locale.ENGLISH);
               currentWord = wordUtil.clearPunctuation(currentWord);
               if(currentWord!=null)
               { 
                  if(!wordUtil.isSimplifiedStopWord(currentWord) && wordUtil.isAvailable(currentWord) )
                  {
                     if(wordsToPlainHashMap.containsKey(currentWord))
                     {
                        String plainCurrentWord = wordsToPlainHashMap.get(currentWord);
             /*           
                        if(wordUtil.isAngerWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByValue("Anger", resultAnalysisData.getEmotionOccHashMap(),occurence); 
                        }
                        else if(wordUtil.isFearWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByValue("Fear", resultAnalysisData.getEmotionOccHashMap(),occurence); 
                        }
                        else if(wordUtil.isSadnessWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByValue("Sadness", resultAnalysisData.getEmotionOccHashMap(),occurence); 
                        }
                        else if(wordUtil.isSurpriseWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByValue("Surprise", resultAnalysisData.getEmotionOccHashMap(),occurence); 
                        }
                        else if(wordUtil.isDisgustWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByValue("Disgust", resultAnalysisData.getEmotionOccHashMap(),occurence); 
                        }
                        else if(wordUtil.isJoyWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByValue("Joy", resultAnalysisData.getEmotionOccHashMap(),occurence); 
                        }
              */          
                        if(wordUtil.isParrotAngerWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByValue("Anger", resultAnalysisData.getEmotionOccHashMap(),occurence);
                           hmapUtil.checkOccurenceWordHashMapByValue("Anger",plainCurrentWord, resultAnalysisData.getEmotionWordsOccurenceHashMap(), occurence);   
                        }
                        else if(wordUtil.isParrotFearWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByValue("Fear", resultAnalysisData.getEmotionOccHashMap(),occurence);
                           hmapUtil.checkOccurenceWordHashMapByValue("Fear",plainCurrentWord, resultAnalysisData.getEmotionWordsOccurenceHashMap(), occurence); 
                        }
                        else if(wordUtil.isParrotSadnessWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByValue("Sadness", resultAnalysisData.getEmotionOccHashMap(),occurence);
                           hmapUtil.checkOccurenceWordHashMapByValue("Sadness",plainCurrentWord, resultAnalysisData.getEmotionWordsOccurenceHashMap(), occurence); 
                        }
                        else if(wordUtil.isParrotSurpriseWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByValue("Surprise", resultAnalysisData.getEmotionOccHashMap(),occurence);
                           hmapUtil.checkOccurenceWordHashMapByValue("Surprise",plainCurrentWord, resultAnalysisData.getEmotionWordsOccurenceHashMap(), occurence); 
                        }
                        else if(wordUtil.isParrotLoveWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByValue("Love", resultAnalysisData.getEmotionOccHashMap(),occurence);
                           hmapUtil.checkOccurenceWordHashMapByValue("Love",plainCurrentWord, resultAnalysisData.getEmotionWordsOccurenceHashMap(), occurence); 
                        }
                        else if(wordUtil.isParrotJoyWord(plainCurrentWord))
                        {
                           hmapUtil.checkOccurenceHashMapByValue("Joy", resultAnalysisData.getEmotionOccHashMap(),occurence);
                           hmapUtil.checkOccurenceWordHashMapByValue("Joy",plainCurrentWord, resultAnalysisData.getEmotionWordsOccurenceHashMap(), occurence); 
                        }
                     }
                  }
               }
            }
         }   
      }
   }

 
   
}
