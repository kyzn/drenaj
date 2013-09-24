package emotion.server.fileCreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;

import emotion.server.analyzer.AnalysisData;
import emotion.server.analyzer.BaseResultAnalysisData;
import emotion.server.analyzer.ResultAnalysisData;
import emotion.server.util.HashMapUtil;
import emotion.shared.EmoNvNTriplets;
import emotion.shared.EmoTweet;

public class FileCreator
{
   private String path = null;
   private static final String NL = System.getProperty("line.separator");
   private HashMapUtil hmapUtil = new HashMapUtil();
   
   public FileCreator(String fileName)
   {
      this.path="resultData//"+fileName;
      boolean success = (new File(path)).mkdir();
      if (success) {
        System.out.println("Directory: " + path + " created");
      }
   }
   
   public void writeOverallData(String fileName, AnalysisData analysisData)
   {
      FileWriter fstream;
      try
      {
         fstream = new FileWriter(path+"//"+fileName+"_OverallData.txt");
         BufferedWriter out = new BufferedWriter(fstream);
         out.write(fileName+"\n");
         out.write("-------------------\n");
         out.write("number of all tweets                          ; "+ analysisData.getAllTweetArrayList().size() +"\n");
         out.write("number of all distinct tweets                 ; "+ analysisData.getAllDistinctEmoTweetsHashMap().size()+"\n");
         out.write("number of all words                           ; "+ analysisData.getCountAllWords()+"\n");
         out.write("number of all distinct words                  ; "+ analysisData.getAllWordOccurrenceHashMap().size()+"\n");
         out.write("number of all significant words               ; "+ analysisData.getCountAllSignificantWords()+"\n"); 
         out.write("number of all distinct significant words      ; "+ analysisData.getAllSignificantWordOccurrenceHashMap().size()+"\n");
         out.write("number of all nWord words                     ; "+ analysisData.getCountAllnWords()+"\n"); 
         out.write("number of all distinct nWord words            ; "+ analysisData.getAllnWordOccurrenceHashMap().size()+"\n");
         out.write("number of all verbs                           ; "+ analysisData.getCountAllVerbs() +"\n");
         out.write("number of all distinct verbs                  ; "+ analysisData.getAllVerbOccurrenceHashMap().size() +"\n");
         out.write("number of all RTs                             ; "+ analysisData.getCountRTs()+"\n");
         out.write("-------------------\n");
         out.write("number of all NVN tweets                      ; "+ analysisData.getAllNVNTweetsHashMap().size() +"\n");
         out.write("number of all NVN distinct tweets             ; "+ analysisData.getAllDistinctNVNEmoTweetsHashMap().size()+"\n");
         out.write("number of all NVN words                       ; "+ analysisData.getCountAllNVNWords()+"\n");
         out.write("number of all NVN distinct words              ; "+ analysisData.getAllNVNWordOccurrenceHashMap().size()+"\n");
         out.write("number of all NVN significant words           ; "+ analysisData.getCountAllNVNSignificantWords()+"\n"); 
         out.write("number of all NVN distinct significant words  ; "+ analysisData.getAllNVNSignificantWordOccurrenceHashMap().size()+"\n");
         out.write("number of all NVN nWord words                 ; "+ analysisData.getCountAllNVNnWords()+"\n"); 
         out.write("number of all NVN distinct nWord words        ; "+ analysisData.getAllNVNnWordOccurrenceHashMap().size()+"\n");
         out.write("number of all NVN verbs                       ; "+ analysisData.getCountAllNVNVerbs() +"\n");
         out.write("number of all NVN distinct verbs              ; "+ analysisData.getAllNVNVerbOccurrenceHashMap().size() +"\n");
         out.write("number of all NVN RTs                         ; "+ analysisData.getCountNVNRTs()+"\n");
         out.write("-------------------\n");
         out.write("NVN / All ratio : tweets                      ; "+ (float) analysisData.getAllNVNTweetsHashMap().size() / analysisData.getAllTweetArrayList().size() +"\n");
         out.write("NVN / All ratio : distinct tweets             ; "+ (float) analysisData.getAllDistinctNVNEmoTweetsHashMap().size() / analysisData.getAllDistinctEmoTweetsHashMap().size() +"\n");
         out.write("NVN / All ratio : words                       ; "+ (float) analysisData.getCountAllNVNWords() / analysisData.getCountAllWords() +"\n");
         out.write("NVN / All ratio : distinct words              ; "+ (float) analysisData.getAllNVNWordOccurrenceHashMap().size() / analysisData.getAllWordOccurrenceHashMap().size() +"\n");
         out.write("NVN / All ratio : significant words           ; "+ (float) analysisData.getCountAllNVNSignificantWords() / analysisData.getCountAllSignificantWords() +"\n"); 
         out.write("NVN / All ratio : distinct significant words  ; "+ (float) analysisData.getAllNVNSignificantWordOccurrenceHashMap().size() / analysisData.getAllSignificantWordOccurrenceHashMap().size() +"\n");
         out.write("NVN / All ratio : nWord words                 ; "+ (float) analysisData.getCountAllNVNnWords() / analysisData.getCountAllnWords() +"\n"); 
         out.write("NVN / All ratio : distinct nWord words        ; "+ (float) analysisData.getAllNVNnWordOccurrenceHashMap().size() / analysisData.getAllnWordOccurrenceHashMap().size() +"\n");
         out.write("NVN / All ratio : verbs                       ; "+ (float) analysisData.getCountAllNVNVerbs() / analysisData.getCountAllVerbs() +"\n");
         out.write("NVN / All ratio : distinct verbs              ; "+ (float) analysisData.getAllNVNVerbOccurrenceHashMap().size() / analysisData.getAllVerbOccurrenceHashMap().size() +"\n");
         out.write("NVN / All ratio : RTs                         ; "+ (float) analysisData.getCountNVNRTs() / analysisData.getCountRTs() +"\n");
         out.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }


   public void writeOccurenceHashMap(String fileName, LinkedHashMap<String, Integer> occurenceHashMap )
   {
      FileWriter fstream;
      try
      {
         fstream = new FileWriter(path+"//"+fileName+"_Occurence.csv");
         BufferedWriter out = new BufferedWriter(fstream);
         
         Iterator<String> occurenceHashMapIterator = occurenceHashMap.keySet().iterator();
         while(occurenceHashMapIterator.hasNext()) 
         {
            String key = occurenceHashMapIterator.next(); 
            Integer val = occurenceHashMap.get(key);
            out.write(key+" ; "+ val+"\n");
         }
         out.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }   

   public void writeNvNArrayListToFile(String word, ArrayList<EmoNvNTriplets> overallEmoNvNTripletsArrList)
   {
      try
      {
         FileWriter fstream = new FileWriter(path+"//"+word+"_NvNtoTweets.txt");
         BufferedWriter out = new BufferedWriter(fstream);
         for (Iterator<EmoNvNTriplets> iterator = overallEmoNvNTripletsArrList.iterator(); iterator.hasNext();)
         {
            EmoNvNTriplets emoNvNTriplets = (EmoNvNTriplets) iterator.next();
            out.write(emoNvNTriplets.toString()+NL);
            out.write("RT : "+getNumofRT(emoNvNTriplets.getTweets())+NL+NL);
            ArrayList<EmoTweet> emoTweetArrList = emoNvNTriplets.getTweets();
            for (Iterator<EmoTweet> iterator2 = emoTweetArrList.iterator(); iterator2.hasNext();)
            {
               EmoTweet emoTweet = (EmoTweet) iterator2.next();
               out.write(emoTweet.getTweetText()+NL);
            }
            out.write("-------------------------------------------"+NL);
         }
         out.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public void writeNvNArrayTimeListToFile(String word, ArrayList<EmoNvNTriplets> overallEmoNvNTripletsArrList)
   {
      try
      {
         FileWriter fstream = new FileWriter(path+"//"+word+"_NvNtoTweetsTime.txt");
         BufferedWriter out = new BufferedWriter(fstream);
         for (Iterator<EmoNvNTriplets> iterator = overallEmoNvNTripletsArrList.iterator(); iterator.hasNext();)
         {
            EmoNvNTriplets emoNvNTriplets = (EmoNvNTriplets) iterator.next();
            out.write(emoNvNTriplets.toString()+NL);
            out.write("RT : "+getNumofRT(emoNvNTriplets.getTweets())+NL+NL);
            ArrayList<EmoTweet> emoTweetArrList = emoNvNTriplets.getTweets();
            for (Iterator<EmoTweet> iterator2 = emoTweetArrList.iterator(); iterator2.hasNext();)
            {
               EmoTweet emoTweet = (EmoTweet) iterator2.next();
               Calendar cal=Calendar.getInstance(Locale.US);
               cal.setTime(emoTweet.getTweetTime());
               out.write(cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+NL);
            }
            out.write("-------------------------------------------"+NL);
         }
         out.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }   
   

   private int getNumofRT(ArrayList<EmoTweet> tweets)
   {
      int rtCount =0;
      for (Iterator<EmoTweet> iterator2 = tweets.iterator(); iterator2.hasNext();)
      {
         EmoTweet emoTweet = (EmoTweet) iterator2.next();
         if(emoTweet.isReTweet())
         {
            rtCount++;
         }
      }
      return rtCount;
   }

   public void writeRecognitionsToFile(String word, AnalysisData analysisData)
   {
      try
      {
         FileWriter fstream = new FileWriter(path+"//"+word+"_Recognition.txt");
         BufferedWriter out = new BufferedWriter(fstream);
         LinkedHashMap<String, ArrayList<String>>  actualValResPairLinkedHashMap = analysisData.getActualValueResultPairHashMap();
         Iterator<String> actualValResPairLinkedHashMapIterator = actualValResPairLinkedHashMap.keySet().iterator();
         while(actualValResPairLinkedHashMapIterator.hasNext()) 
         {
            String key = actualValResPairLinkedHashMapIterator.next();
            out.write(key+ "  is recognized from :"+NL);
            ArrayList<String> regArrList = actualValResPairLinkedHashMap.get(key);
            for (Iterator<String> iterator = regArrList.iterator(); iterator.hasNext();)
            {
               String string = (String) iterator.next();
               out.write(string+NL);
            }
            out.write("-------------------------------------------"+NL);
         }
         out.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public void writeEmotionData(String word, BaseResultAnalysisData overallBaseResultAnalysisData)
   {
      HashMap<String, ResultAnalysisData> resAnalysisDataHashMap = overallBaseResultAnalysisData.getResultAnalysisDataHashMap();
      writeEmotionDataAndOccurence(word, resAnalysisDataHashMap);   
   }

   /**
    * @param word
    * @param resAnalysisDataHashMap
    */
   public void writeEmotionDataAndOccurence(String word, HashMap<String, ResultAnalysisData> resAnalysisDataHashMap)
   {
      Iterator<String> resAnalysisDataHashmapIterator = resAnalysisDataHashMap.keySet().iterator();
      while(resAnalysisDataHashmapIterator.hasNext()) 
      {
         String key = resAnalysisDataHashmapIterator.next(); 
         ResultAnalysisData resultAnalysisData = resAnalysisDataHashMap.get(key);
         if(!resultAnalysisData.getEmotionOccHashMap().isEmpty())
         {
            writeOccurenceHashMap(word+"_"+key+"_EmotionWord_",hmapUtil.sortEmotionHashMapByValue(resultAnalysisData.getEmotionOccHashMap()));
            HashMap<String, Integer> emoOccHMap = resultAnalysisData.getEmotionOccHashMap();
            Iterator<String> occurenceHashMapIterator = emoOccHMap.keySet().iterator();
            while(occurenceHashMapIterator.hasNext()) 
            {
               String emoKey = occurenceHashMapIterator.next(); 
               resultAnalysisData.getEmotionWordsOccurenceHashMap().get(emoKey);
               writeOccurenceHashMap(word+"_"+key+"_EmotionWordOccurence_",hmapUtil.sortEmotionHashMapByValue(resultAnalysisData.getEmotionWordsOccurenceHashMap().get(emoKey)));
            }
         }
      }
   }
   
   public void writeSingleEmotionDataAndOccurence(String word, ResultAnalysisData resultAnalysisData)
   {
      if(!resultAnalysisData.getEmotionOccHashMap().isEmpty())
      {
         writeOccurenceHashMap(word+"_EmotionWord_",hmapUtil.sortEmotionHashMapByValue(resultAnalysisData.getEmotionOccHashMap()));
         HashMap<String, Integer> emoOccHMap = resultAnalysisData.getEmotionOccHashMap();
         Iterator<String> occurenceHashMapIterator = emoOccHMap.keySet().iterator();
         while(occurenceHashMapIterator.hasNext()) 
         {
            String emoKey = occurenceHashMapIterator.next(); 
            resultAnalysisData.getEmotionWordsOccurenceHashMap().get(emoKey);
            writeOccurenceHashMap(word+"_"+emoKey+"_EmotionWordOccurence_",hmapUtil.sortEmotionHashMapByValue(resultAnalysisData.getEmotionWordsOccurenceHashMap().get(emoKey)));
         }
      }
   }   
   
}
