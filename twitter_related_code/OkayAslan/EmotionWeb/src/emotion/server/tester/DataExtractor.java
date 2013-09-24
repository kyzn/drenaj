package emotion.server.tester;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import emotion.server.analyzer.AnalysisData;
import emotion.server.analyzer.Analyzer;
import emotion.server.analyzer.BaseResultAnalysisData;
import emotion.server.analyzer.EmotionAnalyzer;
import emotion.server.analyzer.ResultAnalysisData;
import emotion.server.analyzer.TweetAnalyzer;
import emotion.server.analyzer.WikiAnalyzer;
import emotion.server.analyzer.WordAnalyzer;
import emotion.server.fileCreator.FileCreator;
import emotion.server.pajek.PajekFileCreator;
import emotion.server.twitter.TwitterDBImpl;
import emotion.server.twitter.TwitterImpl;
import emotion.server.util.HashMapUtil;
import emotion.server.util.WordUtil;
import emotion.shared.EmoResultDO;
import emotion.shared.EmoTweet;

public class DataExtractor
{
   private static TwitterImpl twitterImpl = TwitterImpl.getInstance();
   private static TwitterDBImpl twitterDBImpl = TwitterDBImpl.getInstance();
   private static String DATE_FORMAT_FOR_DISPLAY   = "dd-MMMMM-yyyy";
   private static String DATE_FORMAT_FOR_SORT      = "yyyyMMdd";
   private static SimpleDateFormat simpleDataFormatterForDisplay = new SimpleDateFormat(DATE_FORMAT_FOR_DISPLAY,Locale.US);
   private SimpleDateFormat simpleDataFormatterForSort = new SimpleDateFormat(DATE_FORMAT_FOR_SORT,Locale.US);
   private static HashMapUtil hmapUtil = new HashMapUtil();
   private static WordUtil wordUtil = new WordUtil();
   private static Analyzer analyzer = new Analyzer();
   
   /**
    * @param args
    */
   public static void main(String[] args)
   {
  //    System.out.println("word;date;start time;end time;number of all tweets;number of all words;number of all Nwords;number of all verbs;number of all RTs;" + "number of all NVNtweets;number of all NVN Nwords;number of all NVN verbs;number of all NVN RTs");
      fireAll();
    //  fireSpecial();
   //   fireSpecial2();
      
   //   fireByHour("germany","tw_germany_tb");
   //   fireEveSpecial();
   //   processAll();
   }

   private static void fireEveSpecial()
   {
      fireEverything("germany","tw_germany_tb");
      fireEverything("argentina","tw_argentina_tb");
      fireEverything("messi","tw_messi_tb");
   }

   public static void processAll()
   {
      HashMap<String, String> tableMap = twitterImpl.getTableHashMapByWord();
      
      if(tableMap!=null && tableMap.size()>0)
      {
         Set<String> tableMapSet=tableMap.keySet();  
         Iterator<String> tableMapIterator=tableMapSet.iterator();
         while(tableMapIterator.hasNext())
         {
            String key = tableMapIterator.next(); 
            String val = tableMap.get(key);
            processTweetsByDay(key,val);
         }
      }
   }   
   
   private static void fireEverything(String word,String tableName)
   {
      ArrayList<EmoTweet> allTweetArrList = twitterDBImpl.getAllTweetsFromTables(tableName);
      analyzer.analyze(word,allTweetArrList,false);
  
   }    
   
   public static void fireAll()
   {
      HashMap<String, String> tableMap = twitterImpl.getTableHashMapByWord();
      
      if(tableMap!=null && tableMap.size()>0)
      {
         Set<String> tableMapSet=tableMap.keySet();  
         Iterator<String> tableMapIterator=tableMapSet.iterator();
         while(tableMapIterator.hasNext())
         {
            String key = tableMapIterator.next(); 
            String val = tableMap.get(key);
            fireEverything(key,val);
         }
      }
   }    
   
   
   public static void fireAllByHalfDay()
   {
      HashMap<String, String> tableMap = twitterImpl.getTableHashMapByWord();
      
      if(tableMap!=null && tableMap.size()>0)
      {
         Set<String> tableMapSet=tableMap.keySet();  
         Iterator<String> tableMapIterator=tableMapSet.iterator();
         while(tableMapIterator.hasNext())
         {
            String key = tableMapIterator.next(); 
            String val = tableMap.get(key);
            fireByHalfDay(key,val);
         }
      }
   } 
   
   private static void fireSpecial()
   {
      fireByHour("argentina","tw_argentina_tb");
      fireByHour("messi","tw_messi_tb");
   }
   
   private static void fireSpecial2()
   {
      fireByHalfDay("eduWeb","tw_eduweb_tb");
      fireByHalfDay("WINSYS","tw_winsys_tb");
      fireByHalfDay("Balisage","tw_balisage_tb");
      fireByHalfDay("The Dead Weather","tw_thedeadweather_tb");
      fireByHalfDay("Jazz Festival","tw_jazzfestival_tb");
      fireByHalfDay("New York","tw_newyork_tb");
      fireByHalfDay("San Fransisco","tw_sanfransisco_tb");
      fireByHalfDay("palo alto","tw_paloalto_tb");
      fireByHalfDay("unesco","tw_unesco_tb");
      fireByHalfDay("berkeley","tw_berkeley_tb");
      fireByHalfDay("second life","tw_secondlife_tb");
   }
   
   private static void fireByHour(String word,String tableName)
   {
      ArrayList<EmoTweet> allTweetArrList = twitterDBImpl.getAllTweetsFromTables(tableName);
      HashMap<String, ArrayList<EmoTweet>> dayTweetsHmap = getTweetsByDay(allTweetArrList);
      
      Iterator<String> hashmapIterator = dayTweetsHmap.keySet().iterator();
      while(hashmapIterator.hasNext()) 
      {
         String date = hashmapIterator.next();
         ArrayList<EmoTweet> currentTweets = dayTweetsHmap.get(date);
         ArrayList<EmoTweet> firstQuarter1 = new ArrayList<EmoTweet>(); 
         ArrayList<EmoTweet> firstQuarter2 = new ArrayList<EmoTweet>(); 
         ArrayList<EmoTweet> firstQuarter3 = new ArrayList<EmoTweet>(); 
         ArrayList<EmoTweet> firstQuarter4 = new ArrayList<EmoTweet>();
         
         ArrayList<EmoTweet> secondQuarter1 = new ArrayList<EmoTweet>();
         ArrayList<EmoTweet> secondQuarter2 = new ArrayList<EmoTweet>();
         ArrayList<EmoTweet> secondQuarter3 = new ArrayList<EmoTweet>();
         ArrayList<EmoTweet> secondQuarter4 = new ArrayList<EmoTweet>();
         
         for (Iterator<EmoTweet> iterator = currentTweets.iterator(); iterator.hasNext();)
         {
            EmoTweet emoTweet = (EmoTweet) iterator.next();
            Calendar cal=Calendar.getInstance(Locale.US);
            cal.setTime(emoTweet.getTweetTime());
            if(cal.get(Calendar.HOUR_OF_DAY)>=0 && 3>cal.get(Calendar.HOUR_OF_DAY))
            {
               firstQuarter1.add(emoTweet);
            }
            else if(cal.get(Calendar.HOUR_OF_DAY)>=3 && 6>cal.get(Calendar.HOUR_OF_DAY))
            {
               firstQuarter2.add(emoTweet);
            }
            else if(cal.get(Calendar.HOUR_OF_DAY)>=6 && 9>cal.get(Calendar.HOUR_OF_DAY))
            {
               firstQuarter3.add(emoTweet);
            } 
            else if(cal.get(Calendar.HOUR_OF_DAY)>=9 && 12>cal.get(Calendar.HOUR_OF_DAY))
            {
               firstQuarter4.add(emoTweet);
            }
            else if(cal.get(Calendar.HOUR_OF_DAY)>=12 && 15>cal.get(Calendar.HOUR_OF_DAY))
            {
               secondQuarter1.add(emoTweet);
            }
            else if(cal.get(Calendar.HOUR_OF_DAY)>=15 && 18>cal.get(Calendar.HOUR_OF_DAY))
            {
               secondQuarter2.add(emoTweet);
            } 
            else if(cal.get(Calendar.HOUR_OF_DAY)>=18 && 21>cal.get(Calendar.HOUR_OF_DAY))
            {
               secondQuarter3.add(emoTweet);
            }
            else if(cal.get(Calendar.HOUR_OF_DAY)>=21 && 24>cal.get(Calendar.HOUR_OF_DAY))
            {
               secondQuarter4.add(emoTweet);
            }
            else 
            {
            }  
         }
         analyze(word,date,"00:00","03:00","00","03",firstQuarter1);
         analyze(word,date,"03:00","06:00","03","06",firstQuarter2);
         analyze(word,date,"06:00","09:00","06","09",firstQuarter3);
         analyze(word,date,"09:00","12:00","09","12",firstQuarter4);
         
         analyze(word,date,"12:00","15:00","12","15",secondQuarter1);
         analyze(word,date,"15:00","18:00","15","18",secondQuarter2);
         analyze(word,date,"18:00","21:00","18","21",secondQuarter3);
         analyze(word,date,"21:00","24:00","21","24",secondQuarter4);
      }  
   }   
   
   
   private static void analyze(String word, String date, String startingTime, String endingTime, String fstartingTime, String fendingTime, ArrayList<EmoTweet> allTweetsFromTable)
   {
      AnalysisData analysisData = new AnalysisData();
      analysisData.setSearchedWord(word);
      analysisData.setAllTweetArrayList(allTweetsFromTable);
      TweetAnalyzer tweetAnalyzer = new TweetAnalyzer(analysisData);
      tweetAnalyzer.analyzeTweets();
      
      printertime(word, date, startingTime, endingTime, analysisData);
   //   FileCreator fileCreator = new FileCreator(word+"_"+date);
   //   filer(fileCreator,word+"_"+fstartingTime+"_"+fendingTime,analysisData);
     /* 
      if(analyzeEmotions(analysisData,resultAnalysisData))
      {
         FileCreator fileCreator = new FileCreator(word+"_"+date);
         fileCreator.writeSingleEmotionDataAndOccurence(word+"_"+fstartingTime+"_"+fendingTime, resultAnalysisData); 
      }
      */
   }
   
   private static void filer(FileCreator fileCreator,String word, AnalysisData analysisData)
   {
      fileCreator.writeOccurenceHashMap(word+"_All_Word_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllSignificantWordOccurrenceHashMap()));
      fileCreator.writeOccurenceHashMap(word+"_NWord_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllnWordOccurrenceHashMap()));
      fileCreator.writeOccurenceHashMap(word+"_Verb_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllVerbOccurrenceHashMap()));
      fileCreator.writeOccurenceHashMap(word+"_NVN_NWord_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllNVNWordOccurrenceHashMap()));
      fileCreator.writeOccurenceHashMap(word+"_NWord_Verb_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllNVNVerbOccurrenceHashMap()));
   }

   private static void printertime(String word, String date, String startingTime, String endingTime, AnalysisData analysisData)
   {
      System.out.print(word);
      System.out.print(";");
      System.out.print(date);
      System.out.print(";");
      System.out.print(startingTime);
      System.out.print(";");
      System.out.print(endingTime);
      System.out.print(";");
      System.out.print(analysisData.getAllTweetArrayList().size());
      System.out.print(";");
      System.out.print(analysisData.getAllSignificantWordOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getAllnWordOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getAllVerbOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getCountRTs());
      System.out.print(";");
      System.out.print(analysisData.getAllNVNTweetsHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getAllNVNWordOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getAllNVNVerbOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getCountNVNRTs());
      System.out.print("\n");
   }
   
  
   
   private static void processTweetsByDay(String word,String tableName)
   {
      ArrayList<EmoTweet> allTweetArrList = twitterDBImpl.getAllTweetsFromTables(tableName);
      HashMap<String, ArrayList<EmoTweet>> dayTweetsHmap = getTweetsByDay(allTweetArrList);
      
      Iterator<String> hashmapIterator = dayTweetsHmap.keySet().iterator();
      while(hashmapIterator.hasNext()) 
      {
         String date = hashmapIterator.next();
         ArrayList<EmoTweet> currentTweets = dayTweetsHmap.get(date);
         analyze(word,date,"00:00","24:00","00","24",currentTweets);
      }  
   }    
   

   private static void fireByHalfDay(String word,String tableName)
   {
      ArrayList<EmoTweet> allTweetArrList = twitterDBImpl.getAllTweetsFromTables(tableName);
      HashMap<String, ArrayList<EmoTweet>> dayTweetsHmap = getTweetsByDay(allTweetArrList);
      
      Iterator<String> hashmapIterator = dayTweetsHmap.keySet().iterator();
      while(hashmapIterator.hasNext()) 
      {
         String date = hashmapIterator.next();
         ArrayList<EmoTweet> currentTweets = dayTweetsHmap.get(date);
         ArrayList<EmoTweet> firstHalfTweets = new ArrayList<EmoTweet>(); 
         ArrayList<EmoTweet> secondHalfTweets = new ArrayList<EmoTweet>();
         
         for (Iterator<EmoTweet> iterator = currentTweets.iterator(); iterator.hasNext();)
         {
            EmoTweet emoTweet = (EmoTweet) iterator.next();
            Calendar cal=Calendar.getInstance(Locale.US);
            cal.setTime(emoTweet.getTweetTime());
            if(cal.get(Calendar.HOUR_OF_DAY)>=0 && 12>cal.get(Calendar.HOUR_OF_DAY))
            {
               firstHalfTweets.add(emoTweet);
            }
            else 
            {
               secondHalfTweets.add(emoTweet);
            }  
         }
         analyze(word,date,"00:00","11:59","00","12",firstHalfTweets);
         analyze(word,date,"12:00","23:59","12","24",secondHalfTweets);
      }  
   }      
      
   private static HashMap<String, ArrayList<EmoTweet>> getTweetsByDay(ArrayList<EmoTweet> allTweetArrList)
   {
      HashMap<String, ArrayList<EmoTweet>> dayTweetsHmap = new HashMap<String, ArrayList<EmoTweet>>();
      for (Iterator<EmoTweet> iterator = allTweetArrList.iterator(); iterator.hasNext();)
      {
         EmoTweet emoTweet = (EmoTweet) iterator.next();
         Calendar cal=Calendar.getInstance(Locale.US);
         cal.setTime(emoTweet.getTweetDate());
         String date = simpleDataFormatterForDisplay.format(cal.getTime());
         updateDayHashMap(date,dayTweetsHmap,emoTweet);  
      }
      return dayTweetsHmap;
   }

   private static void updateDayHashMap(String date, HashMap<String, ArrayList<EmoTweet>> dayEmotweetHashmap,EmoTweet emoTweet)
   {
      if(dayEmotweetHashmap.containsKey(date))
      {
         ArrayList<EmoTweet> emoTweetArr = dayEmotweetHashmap.get(date);
         emoTweetArr.add(emoTweet);
         dayEmotweetHashmap.put(date, emoTweetArr);
      }
      else
      {
         ArrayList<EmoTweet> emoTweetArr = new ArrayList<EmoTweet>();
         emoTweetArr.add(emoTweet);
         dayEmotweetHashmap.put(date, emoTweetArr);
      }
   }    
   
   public static void analyze(String word, String date, String time, ArrayList<EmoTweet> allTweetsFromTable)
   {
      AnalysisData analysisData = new AnalysisData();
      analysisData.setSearchedWord(word);
      analysisData.setAllTweetArrayList(allTweetsFromTable);
      TweetAnalyzer tweetAnalyzer = new TweetAnalyzer(analysisData);
      tweetAnalyzer.analyzeTweets();
      printer(word, date, time, analysisData);
      
   }

   public static boolean analyzeEmotions(AnalysisData analysisData,ResultAnalysisData resultAnalysisData )
   {
      Iterator<String> distinctEmoTweetHashMapIterator = analysisData.getAllDistinctEmoTweetsHashMap().keySet().iterator();
      HashMap<String, String> wordsToPlainHashMap = analysisData.getWordsToPlainHashMap();
      boolean found = false;
      
      while(distinctEmoTweetHashMapIterator.hasNext()) 
      {
         String tweetText = distinctEmoTweetHashMapIterator.next();
         Integer occurence = analysisData.getAllDistinctEmoTweetsHashMap().get(tweetText);
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
                        found = true;
                        hmapUtil.checkOccurenceHashMapByValue("Anger", resultAnalysisData.getEmotionOccHashMap(),occurence);
                        hmapUtil.checkOccurenceWordHashMapByValue("Anger",plainCurrentWord, resultAnalysisData.getEmotionWordsOccurenceHashMap(), occurence);   
                     }
                     else if(wordUtil.isParrotFearWord(plainCurrentWord))
                     {
                        found = true;
                        hmapUtil.checkOccurenceHashMapByValue("Fear", resultAnalysisData.getEmotionOccHashMap(),occurence);
                        hmapUtil.checkOccurenceWordHashMapByValue("Fear",plainCurrentWord, resultAnalysisData.getEmotionWordsOccurenceHashMap(), occurence); 
                     }
                     else if(wordUtil.isParrotSadnessWord(plainCurrentWord))
                     {
                        found = true;
                        hmapUtil.checkOccurenceHashMapByValue("Sadness", resultAnalysisData.getEmotionOccHashMap(),occurence);
                        hmapUtil.checkOccurenceWordHashMapByValue("Sadness",plainCurrentWord, resultAnalysisData.getEmotionWordsOccurenceHashMap(), occurence); 
                     }
                     else if(wordUtil.isParrotSurpriseWord(plainCurrentWord))
                     {
                        found = true;
                        hmapUtil.checkOccurenceHashMapByValue("Surprise", resultAnalysisData.getEmotionOccHashMap(),occurence);
                        hmapUtil.checkOccurenceWordHashMapByValue("Surprise",plainCurrentWord, resultAnalysisData.getEmotionWordsOccurenceHashMap(), occurence); 
                     }
                     else if(wordUtil.isParrotLoveWord(plainCurrentWord))
                     {
                        found = true;
                        hmapUtil.checkOccurenceHashMapByValue("Love", resultAnalysisData.getEmotionOccHashMap(),occurence);
                        hmapUtil.checkOccurenceWordHashMapByValue("Love",plainCurrentWord, resultAnalysisData.getEmotionWordsOccurenceHashMap(), occurence); 
                     }
                     else if(wordUtil.isParrotJoyWord(plainCurrentWord))
                     {
                        found = true;
                        hmapUtil.checkOccurenceHashMapByValue("Joy", resultAnalysisData.getEmotionOccHashMap(),occurence);
                        hmapUtil.checkOccurenceWordHashMapByValue("Joy",plainCurrentWord, resultAnalysisData.getEmotionWordsOccurenceHashMap(), occurence); 
                     }
                  }
               }
            }
         }
      }
      return found;
   }   
   
   
   /**
    * @param word
    * @param date
    * @param time
    * @param analysisData
    */
   private static void printer(String word, String date, String time, AnalysisData analysisData)
   {
      System.out.print(word);
      System.out.print(";");
      System.out.print(date);
      System.out.print(";");
      System.out.print(time);
      System.out.print(";");
      System.out.print(analysisData.getAllTweetArrayList().size());
      System.out.print(";");
      System.out.print(analysisData.getAllSignificantWordOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getAllnWordOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getAllVerbOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getCountRTs());
      System.out.print(";");
      System.out.print(analysisData.getAllNVNTweetsHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getAllNVNWordOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getAllNVNVerbOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getCountNVNRTs());
      System.out.print("\n");
   } 
   
}
