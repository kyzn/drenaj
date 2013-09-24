package emotion.server.tester;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;

import emotion.server.libstemmer.snowball.ext.englishStemmer;
import emotion.server.twitter.TwitterDBImpl;
import emotion.server.util.HashMapUtil;
import emotion.server.util.WordUtil;
import emotion.server.wordNet.WordNetDBImpl;
import emotion.server.wordNet.WordNetData;
import emotion.shared.EmoTweet;
import emotion.shared.EmoTweetWordData;

public class TesterHelper
{
   private static TwitterDBImpl twitterDBImpl = TwitterDBImpl.getInstance(); 
   private static HashMapUtil hMapUtil = new HashMapUtil();
   private static HashMap<String, Integer> sortedEmotionHashMapByWord = new HashMap<String, Integer>();
   private static WordNetDBImpl wordNetdbImpl = WordNetDBImpl.getInstance();
   private static WordUtil wordUtil = new WordUtil();
   private static HashMap<String,EmoTweetWordData> wordDataMap = new HashMap<String,EmoTweetWordData>();
   private static HashMap<String,WordNetData> wordNetMap = new HashMap<String,WordNetData>();
   private static HashMap<String, Integer> emotionHashMapByWord = new HashMap<String, Integer>();
   static englishStemmer stemmer = new englishStemmer();
   
   /**
    * 
    */
   public static void proo()
   {
      process();
      System.out.println("Process Finish...");
      
      sortedEmotionHashMapByWord = hMapUtil.sortEmotionHashMapByValue(emotionHashMapByWord);
   //   printMaps();
      
      System.out.println("Finito...");
   }
   
   public HashMap<String,EmoTweetWordData> getWordDataMap()
   {
      return wordDataMap; 
   }
   
  
   public static void printMaps()
   {
      Iterator<String> HashmapIterator = sortedEmotionHashMapByWord.keySet().iterator();
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next();
         System.out.println("---------------------------");
         System.out.println("    "+key+" : "+sortedEmotionHashMapByWord.get(key));
         if(wordDataMap.containsKey(key))
         {
            EmoTweetWordData emoTweWordData = wordDataMap.get(key);
            printEmoTweet(emoTweWordData);
         }
         else
         {
            String plForm = getPlainWord(key);
            if(wordDataMap.containsKey(plForm))
            {
               EmoTweetWordData emoTweWordData = wordDataMap.get(plForm);
               printEmoTweet(emoTweWordData);
            }
            else
            {     
               System.out.println("    "+key+" not found...");
            } 
         }   
      }
   }


   public static void printEmoTweet(EmoTweetWordData emoTweWordData)
   {
      if(!emoTweWordData.getBeforeAdjectives().isEmpty())
      {
         System.out.println("---Before adjectives");
         hMapUtil.printHashmap(hMapUtil.sortEmotionHashMapByValue(emoTweWordData.getBeforeAdjectives())); 
      }
      if(!emoTweWordData.getBeforeVerbs().isEmpty())
      {
         System.out.println("---Before verbs");
         hMapUtil.printHashmap(hMapUtil.sortEmotionHashMapByValue(emoTweWordData.getBeforeVerbs()));   
      }
      if(!emoTweWordData.getAfterAdjectives().isEmpty())
      {
         System.out.println("---After adjectives");
         hMapUtil.printHashmap(hMapUtil.sortEmotionHashMapByValue(emoTweWordData.getAfterAdjectives()));  
      }
      if(!emoTweWordData.getAfterVerbs().isEmpty())
      {
         System.out.println("---After verbs");
         hMapUtil.printHashmap(hMapUtil.sortEmotionHashMapByValue(emoTweWordData.getAfterVerbs()));  
      }
      if(!emoTweWordData.getAfterNouns().isEmpty())
      {
         System.out.println("---After nouns");
         hMapUtil.printHashmap(hMapUtil.sortEmotionHashMapByValue(emoTweWordData.getAfterNouns()));   
      }
   }


   /**
    * @return
    */
   private static void process()
   {
      ArrayList<EmoTweet> allBarcelonaTweets = twitterDBImpl.getAllTweetsFromTables("tw_barcelona_tb");
      ArrayList<EmoTweet> day30BarcelonaTweets = new ArrayList<EmoTweet>();
      for (Iterator<EmoTweet> iterator = allBarcelonaTweets.iterator(); iterator.hasNext();)
      {
         EmoTweet emoTweet = (EmoTweet) iterator.next();
         Calendar cal=Calendar.getInstance(Locale.US);
         cal.setTime(emoTweet.getTweetDate());
         if(cal.get(Calendar.DAY_OF_MONTH)==31)
         {
            day30BarcelonaTweets.add(emoTweet);
         }   
      }
      
      processTweets(day30BarcelonaTweets);
   }
   
   private static String getPlainWord(String incomingWord)
   {
      if(wordNetMap.containsKey(incomingWord))
      {
         return wordNetMap.get(incomingWord).getPlainForm();
      }
      else
      {
         WordNetData wNData = wordNetdbImpl.getWordProperties(incomingWord);
         if(wNData==null)
         {
            stemmer.setCurrent(incomingWord);
            stemmer.stem();
            String stemmedWord = stemmer.getCurrent();
            wNData = wordNetdbImpl.getWordProperties(stemmedWord);
         }
         if(wNData!=null)
         {
            wordNetMap.put(incomingWord, wNData);
            return wNData.getPlainForm();
         }
         else
         {
            return null;
         }
      }
      
   }
   
   private static void processTweets(ArrayList<EmoTweet> day30BarcelonaTweets)
   {
      int count=1;
      
      
      for (Iterator<EmoTweet> iterator = day30BarcelonaTweets.iterator(); iterator.hasNext();)
      {
         EmoTweet emoTweet = (EmoTweet) iterator.next();
         EmoTweetWordData emoTweetWordData = new EmoTweetWordData();
   //      emoTweetWordData.setTweetText(emoTweet.getTweetText());
      
         /*
         System.out.println("--------------------------------------------------------");
         System.out.println("Tweet #: "+ count);
         System.out.println("");
         System.out.println(emoTweet.getTweetText());
         System.out.println("");
         */
         StringTokenizer st = new StringTokenizer(emoTweet.getTweetText());
         while (st.hasMoreTokens()) 
         {
            String currentWord = wordUtil.clearPunctuation(st.nextToken().toLowerCase(Locale.ENGLISH));
            if(currentWord!=null)
            { 
               if(!wordUtil.isSimplifiedStopWord(currentWord))
               {
              //    System.out.println("    currentWord : "+ currentWord);
                  if( emotionHashMapByWord.containsKey(currentWord))
                  {
                     int frequency = emotionHashMapByWord.get(currentWord);
                     frequency=frequency+1;
                     emotionHashMapByWord.put(currentWord,frequency); 
                  }
                  else
                  {
                     emotionHashMapByWord.put(currentWord, 1);
                  }   
                  WordNetData wNData = wordNetdbImpl.getWordProperties(currentWord);
                  if(wNData==null)
                  {
                     stemmer.setCurrent(currentWord);
                     stemmer.stem();
                     String stemmedWord = stemmer.getCurrent();
               //      System.out.println("    stemmedWord : "+ stemmedWord);
                     wNData = wordNetdbImpl.getWordProperties(stemmedWord);
                  }
                  if(wNData!=null)
                  {
    //                 emotionHashMapByWord.put(wNData.getPlainForm(), 1);
                     String plainForm = wNData.getPlainForm();
                     if(wNData.isAdjective())
                     {
                        if(emoTweetWordData.getNoun()== null)
                        {
                           emoTweetWordData.getBeforeAdjectives().put(plainForm, 1);
                        }
                        else
                        {
                           emoTweetWordData.getAfterAdjectives().put(plainForm, 1);
                        }
                     }
                     if(wNData.isVerb())
                     {
                        if(emoTweetWordData.getNoun()== null)
                        {
                           emoTweetWordData.getBeforeVerbs().put(plainForm, 1);
                        }
                        else
                        {
                           emoTweetWordData.getAfterVerbs().put(plainForm, 1);
                        }
                     }
                     if(wNData.isNoun())
                     {
                        if(emoTweetWordData.getNoun()!= null)
                        {
                           EmoTweetWordData tempEmoTweetWordData = new EmoTweetWordData();
                           tempEmoTweetWordData.setBeforeAdjectives(emoTweetWordData.getAfterAdjectives());
                           tempEmoTweetWordData.setBeforeVerbs(emoTweetWordData.getAfterVerbs());
                           tempEmoTweetWordData.setNoun(plainForm);
                           emoTweetWordData.getAfterNouns().put(plainForm, 1);
                           if(wordDataMap.containsKey(emoTweetWordData.getNoun()))
                           {
                              updapteWordDataMap(emoTweetWordData.getNoun(),emoTweetWordData);
                           }
                           else
                           {
                              wordDataMap.put(emoTweetWordData.getNoun(), emoTweetWordData);
                           }
                           emoTweetWordData = tempEmoTweetWordData; 
                        }
                        else
                        {
                           emoTweetWordData.setNoun(plainForm);
                        }
                     }
                  } 
                  else
                  {
                     if(emoTweetWordData.getNoun()!=null)
                     {
                        EmoTweetWordData tempEmoTweetWordData = new EmoTweetWordData();
                        tempEmoTweetWordData.setBeforeAdjectives(emoTweetWordData.getAfterAdjectives());
                        tempEmoTweetWordData.setBeforeVerbs(emoTweetWordData.getAfterVerbs());
                        tempEmoTweetWordData.setNoun(currentWord);
                        if(wordDataMap.containsKey(emoTweetWordData.getNoun()))
                        {
                           updapteWordDataMap(emoTweetWordData.getNoun(),emoTweetWordData);  
                        }
                        else
                        {   
                           wordDataMap.put(emoTweetWordData.getNoun(), emoTweetWordData);
                        }
                        emoTweetWordData = tempEmoTweetWordData;
                     }
                     else
                     {
                        emoTweetWordData.setNoun(currentWord);
                     }   
                  }
                       
               }  
            }
         }
         if(emoTweetWordData.getNoun()!=null)
         {
            if(wordDataMap.containsKey(emoTweetWordData.getNoun()))
            {
               updapteWordDataMap(emoTweetWordData.getNoun(),emoTweetWordData);  
            }
            else
            {   
               wordDataMap.put(emoTweetWordData.getNoun(), emoTweetWordData);
            }
         }
         count++;
      }
   }

   private static void updapteWordDataMap(String plainForm, EmoTweetWordData emoTweetWordData)
   {
      EmoTweetWordData tempEmoTweetWordData = wordDataMap.get(plainForm);
 //     HashMap<String,Integer> tempHashMap;
      
      if(!emoTweetWordData.getBeforeAdjectives().isEmpty())
      {
         updateWordMap(tempEmoTweetWordData.getBeforeAdjectives(),emoTweetWordData.getBeforeAdjectives());  
      }
      if(!emoTweetWordData.getBeforeVerbs().isEmpty())
      {
         updateWordMap(tempEmoTweetWordData.getBeforeVerbs(),emoTweetWordData.getBeforeVerbs());  
      }
      if(!emoTweetWordData.getAfterAdjectives().isEmpty())
      {
         updateWordMap(tempEmoTweetWordData.getAfterAdjectives(),emoTweetWordData.getAfterAdjectives());  
      }
      if(!emoTweetWordData.getAfterVerbs().isEmpty())
      {
         updateWordMap(tempEmoTweetWordData.getAfterVerbs(),emoTweetWordData.getAfterVerbs());  
      }
      if(!emoTweetWordData.getAfterNouns().isEmpty())
      {
         updateWordMap(tempEmoTweetWordData.getAfterNouns(),emoTweetWordData.getAfterNouns());  
      }
      wordDataMap.put(plainForm, tempEmoTweetWordData);
   }

   private static void updateWordMap(HashMap<String, Integer> toWordHashMap, HashMap<String, Integer> fromWordHashMap)
   {
      Iterator<String> HashmapIterator = fromWordHashMap.keySet().iterator();
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next();
         if(toWordHashMap.containsKey(key))
         {
            Integer val = toWordHashMap.get(key);
            val =val+1;
            toWordHashMap.put(key, val);
         }
         else
         {
            toWordHashMap.put(key, 1);
         }   
        // System.out.println(key+" ; "+ val);
      }
    
   }

   /**
    * @return the sortedEmotionHashMapByWord
    */
   public static HashMap<String, Integer> getSortedEmotionHashMapByWord()
   {
      return sortedEmotionHashMapByWord;
   }

   /**
    * @param sortedEmotionHashMapByWord the sortedEmotionHashMapByWord to set
    */
   public static void setSortedEmotionHashMapByWord(HashMap<String, Integer> sortedEmotionHashMapByWord)
   {
      TesterHelper.sortedEmotionHashMapByWord = sortedEmotionHashMapByWord;
   }

   /**
    * @return the wordNetMap
    */
   public static HashMap<String, WordNetData> getWordNetMap()
   {
      return wordNetMap;
   }

   /**
    * @param wordNetMap the wordNetMap to set
    */
   public static void setWordNetMap(HashMap<String, WordNetData> wordNetMap)
   {
      TesterHelper.wordNetMap = wordNetMap;
   }
}
