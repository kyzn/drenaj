package emotion.server.analyzer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.StringTokenizer;

import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.TwitterException;
import twitter4j.User;

import emotion.server.fileCreator.FileCreator;
import emotion.server.logger.Logger;
import emotion.server.mediaWiki.Item;
import emotion.server.mediaWiki.MediaWikiData;
import emotion.server.pajek.PajekFileCreator;
import emotion.server.twitter.TwitterImpl;
import emotion.server.util.HashMapUtil;
import emotion.server.util.WordUtil;
import emotion.server.wordNet.WordNetData;
import emotion.shared.EmoNvNTriplets;
import emotion.shared.EmoResultDO;
import emotion.shared.EmoResultValue;
import emotion.shared.EmoTweet;
import emotion.shared.EmoTweetWordData;

public class Analyzer
{
   private LinkedHashMap<String, String> dateHashMap = new LinkedHashMap<String, String>();
   private HashMap<String,ArrayList<EmoTweet>> dateAllEmoTweetsHashMap = new HashMap<String,ArrayList<EmoTweet>>();
   private HashMap<String,HashMap<String, Integer>> dateAllWordsOccurenceHashMap = new HashMap<String,HashMap<String, Integer>>();
   private HashMap<String,HashMap<String, EmoTweetWordData>> dateNounWordHashMap = new HashMap<String,HashMap<String, EmoTweetWordData>>();
   private HashMap<String,WordNetData> wordNetMap = new HashMap<String,WordNetData>();

   private HashMap<String,HashMap<String, Integer>> dateSearchWordsOccurenceHashMap = new HashMap<String,HashMap<String, Integer>>();  
   private HashMap<String,HashMap<String, Integer>> dateCheckWordsOccurenceHashMap = new HashMap<String,HashMap<String, Integer>>(); 
   
   private HashMap<String,LinkedHashMap<String, String>> dateResultPairHashMap = new HashMap<String,LinkedHashMap<String, String>>();
   
   private static String DATE_FORMAT_FOR_DISPLAY   = "dd-MMMMM-yyyy_hh-mm-ss";
   private static SimpleDateFormat simpleDataFormatterForDisplay = new SimpleDateFormat(DATE_FORMAT_FOR_DISPLAY,Locale.US);
   private static final String NL = System.getProperty("line.separator");    
   private LinkedHashMap<String, String> keyValHashMap = new LinkedHashMap<String, String>();

   private HashMap<Integer, Integer> uidTypeHasMap = new HashMap<Integer, Integer>();
   
   
   private HashMap<String, String> foundWords = new HashMap<String, String>();
   private HashMap<String, String> foundAfterVerbWords = new HashMap<String, String>();
   private HashMap<String, String> foundBeforeVerbWords = new HashMap<String, String>();
   
   private HashMap<String, String> alreadyFoundWords = new HashMap<String, String>();
   
   private WordUtil wordUtil = new WordUtil();
   
   private WikiAnalyzer wikiAnalyzer = null;
   private EmotionAnalyzer emotionAnalyzer = null;
   private EmoResultDO emoResultDO = new EmoResultDO();
   private ArrayList<EmoTweet> allTweetsArrList = null;
   
   
   private ArrayList<EmoNvNTriplets> overallEmoNvNTripletsArrList = null;
   private ArrayList<EmoNvNTriplets> humanEmoNvNTripletsArrList = null;
   private ArrayList<EmoNvNTriplets> automatedEmoNvNTripletsArrList = null;
   private ArrayList<EmoNvNTriplets> unknownEmoNvNTripletsArrList = null;
   
   private HashMap<String, Integer> overallVerbStreghtHasMap = new HashMap<String, Integer>();
   private HashMap<String, Integer> humanVerbStreghtHasMap = new HashMap<String, Integer>();
   private HashMap<String, Integer> automatedVerbStreghtHasMap = new HashMap<String, Integer>();
   private HashMap<String, Integer> unknownVerbStreghtHasMap = new HashMap<String, Integer>();
   
   private HashMap<String, Integer> overallKeyOccHashMap = new HashMap<String, Integer>();
   private HashMap<String, Integer> humanKeyOccHashMap = new HashMap<String, Integer>();
   private HashMap<String, Integer> automatedKeyOccHashMap = new HashMap<String, Integer>();
   private HashMap<String, Integer> unknownKeyOccHashMap = new HashMap<String, Integer>();
   
   private HashMap<String, Integer> overallVerbOccHashMap = new HashMap<String, Integer>();
   private HashMap<String, Integer> humanVerbOccHashMap = new HashMap<String, Integer>();
   private HashMap<String, Integer> automatedVerbOccHashMap = new HashMap<String, Integer>();
   private HashMap<String, Integer> unknownVerbOccHashMap = new HashMap<String, Integer>();
   
   private BaseResultAnalysisData overallBaseResultAnalysisData = new BaseResultAnalysisData();
   private HashMapUtil hmapUtil = new HashMapUtil();
   
   private HashMap<String,HashMap<String, String>> writerHashMap = new HashMap<String,HashMap<String, String>>();
   
   private Logger logger = new Logger();
   
   private AnalysisData analysisData = null;
   private PajekFileCreator pajekFileCreator = null;
   private FileCreator fileCreator = null;;
   
   public EmoResultDO analyze(String word, ArrayList<EmoTweet> allTweetsFromTable,boolean semantic)
   {
      analysisData = new AnalysisData();
      analysisData.setSearchedWord(word);
      analysisData.setAllTweetArrayList(allTweetsFromTable);
      allTweetsArrList = allTweetsFromTable;
      TweetAnalyzer tweetAnalyzer = new TweetAnalyzer(analysisData);
      WordAnalyzer wordAnalyzer = new WordAnalyzer(analysisData);
      wikiAnalyzer = new WikiAnalyzer(analysisData);
      emotionAnalyzer = new EmotionAnalyzer(analysisData);
      
  //    clearHashMaps();
      
      logger.setLogOn(true);
      logger.writeConsoleWithTime("Analysis started for : " +word);
            
     // TweetAnalyzer tweetAnalyzer = new TweetAnalyzer(dateHashMap,dateAllEmoTweetsHashMap,dateAllWordsOccurenceHashMap,dateNounWordHashMap,wordNetMap);
     // WordAnalyzer wordAnalyzer = new WordAnalyzer(dateHashMap,dateSearchWordsOccurenceHashMap,dateCheckWordsOccurenceHashMap,dateAllWordsOccurenceHashMap,dateNounWordHashMap);
      
    //  wikiAnalyzer = new WikiAnalyzer(dateHashMap,dateSearchWordsOccurenceHashMap,dateCheckWordsOccurenceHashMap,dateResultPairHashMap);
      
      logger.writeConsoleWithTime("Tweet analysis has been started...");
      tweetAnalyzer.analyzeTweets();
      logger.writeConsoleWithTime("Tweet analysis has been finished...");

      if(semantic)
      {
         logger.writeConsoleWithTime("Word analysis has been started...");
         wordAnalyzer.analyzeWords();
         logger.writeConsoleWithTime("Word analysis has been finished...");
         
         logger.writeConsoleWithTime("WikiPedia analysis has been started...");
         wikiAnalyzer.analyzeWiki();
         logger.writeConsoleWithTime("WikiPedia analysis has been finished...");
         
         processResultPairs();
                 
         logger.writeConsoleWithTime("NvNTriplet analysis has been started...");
         findNvNTriplets();
         logger.writeConsoleWithTime("NvNTriplet analysis has been finished...");
         
         logger.writeConsoleWithTime("Emotion analysis has been started...");
         emotionAnalyzer.analyzeEmotions(overallBaseResultAnalysisData);
         logger.writeConsoleWithTime("Emotion analysis has been finished...");      
         
         logger.writeConsoleWithTime("Data is being written");
         fileWriter(word);
         logger.writeConsoleWithTime("Data was written");      
         
         logger.writeConsoleWithTime("Triplets Pajek data is being written");
         pajekWriter(word);
         logger.writeConsoleWithTime("Triplets Pajek data was written");

         retrieveWikiInfo();
      }
      else
      {
         emoResultDO.setTweetIdToNvNTripletsArrListHashMap(analysisData.getTweetIdToNvNTripletsArrListHashMap());
         emoResultDO.setAllNVNTweetsHashMap(analysisData.getAllNVNTweetsHashMap());
         fileWriterWithOutSemantic(word);
      }   
      
      logger.writeConsoleWithTime("Analysis have been finished for : " +word);
      
      emoResultDO.setSemantic(semantic);
      
      return emoResultDO;
   }

   private void fileWriterWithOutSemantic(String word)
   {
      fileCreator = new FileCreator(word);
      fileCreator.writeOverallData(word, analysisData);
      fileCreator.writeOccurenceHashMap(word+"_All_Word_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllWordOccurrenceHashMap()));
      fileCreator.writeOccurenceHashMap(word+"_All_Sig_Word_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllSignificantWordOccurrenceHashMap()));
      fileCreator.writeOccurenceHashMap(word+"_All_NWord_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllnWordOccurrenceHashMap()));
      fileCreator.writeOccurenceHashMap(word+"_All_Verb_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllVerbOccurrenceHashMap()));
      
      fileCreator.writeOccurenceHashMap(word+"_NVN_Word_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllNVNWordOccurrenceHashMap()));
      fileCreator.writeOccurenceHashMap(word+"_NVN_Sig_Word_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllNVNSignificantWordOccurrenceHashMap()));
      fileCreator.writeOccurenceHashMap(word+"_NVN_NWord_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllNVNnWordOccurrenceHashMap()));
      fileCreator.writeOccurenceHashMap(word+"_NVN_Verb_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllNVNVerbOccurrenceHashMap()));
      
   }

   private void fileWriter(String word)
   {
      fileCreator = new FileCreator(word);
      fileCreator.writeOverallData(word, analysisData);
      fileCreator.writeEmotionData(word, overallBaseResultAnalysisData);
      fileCreator.writeOccurenceHashMap(word+"_All_Word_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllSignificantWordOccurrenceHashMap()));
      fileCreator.writeOccurenceHashMap(word+"_NWord_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllnWordOccurrenceHashMap()));
      fileCreator.writeOccurenceHashMap(word+"_Verb_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllVerbOccurrenceHashMap()));
      fileCreator.writeOccurenceHashMap(word+"_NVN_NWord_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllNVNWordOccurrenceHashMap()));
      fileCreator.writeOccurenceHashMap(word+"_NWord_Verb_", hmapUtil.sortEmotionHashMapByValue(analysisData.getAllNVNVerbOccurrenceHashMap()));
      fileCreator.writeNvNArrayListToFile(word, overallEmoNvNTripletsArrList);
      fileCreator.writeNvNArrayTimeListToFile(word, overallEmoNvNTripletsArrList);
      fileCreator.writeRecognitionsToFile(word, analysisData);
   }

   private void pajekWriter(String word)
   {
      
      ArrayList<EmoNvNTriplets> emoNvNTripletsArrList = new ArrayList<EmoNvNTriplets>();;
      HashMap<String, Integer> verbOccHashMap = new HashMap<String, Integer>();
      HashMap<String, Integer> nounOccHashMap = new HashMap<String, Integer>();
      for (Iterator<EmoNvNTriplets> iterator = overallEmoNvNTripletsArrList.iterator(); iterator.hasNext();)
      {
         EmoNvNTriplets emoNvNTriplets = (EmoNvNTriplets) iterator.next();
         if(emoNvNTriplets.getStrength()>1)
         {
            hmapUtil.checkOccurenceHashMapByValue(emoNvNTriplets.getFirstNoun(),nounOccHashMap,emoNvNTriplets.getStrength());
            hmapUtil.checkOccurenceHashMapByValue(emoNvNTriplets.getSecondNoun(),nounOccHashMap,emoNvNTriplets.getStrength());
            hmapUtil.checkOccurenceHashMapByValue(emoNvNTriplets.getVerb(),verbOccHashMap,emoNvNTriplets.getStrength());
            emoNvNTripletsArrList.add(emoNvNTriplets);
         }
         
      }
      pajekFileCreator = new PajekFileCreator(word,hmapUtil.getFirstValue(hmapUtil.sortEmotionHashMapByValue(nounOccHashMap)),hmapUtil.getFirstValue(hmapUtil.sortEmotionHashMapByValue(verbOccHashMap)));
      pajekFileCreator.writeToFileWithEdges(word, nounOccHashMap, verbOccHashMap, emoNvNTripletsArrList);
   }

   private void findNvNTriplets()
   {
      overallEmoNvNTripletsArrList = new ArrayList<EmoNvNTriplets>();
      ResultAnalysisData resAnalysisData = null ;  
      LinkedHashMap<String, ArrayList<String>> tempActualValueResultPairHashMap = analysisData.getActualValueResultPairHashMap();
      Iterator<String> tempActualValueResultPairHashMapIterator = tempActualValueResultPairHashMap.keySet().iterator();
      ArrayList<String> valueResultPairArrayList = new ArrayList<String>();;
      while(tempActualValueResultPairHashMapIterator.hasNext()) 
      {
         String key = tempActualValueResultPairHashMapIterator.next(); 
         resAnalysisData = new ResultAnalysisData();
         resAnalysisData.setNamedEntity(key);
         valueResultPairArrayList = tempActualValueResultPairHashMap.get(key);
         for (Iterator<String> iterator = valueResultPairArrayList.iterator(); iterator.hasNext();)
         {
            String valueResult = (String) iterator.next();
     //       WordToTweetAnalysisData tweetAnalysisData = analysisData.getWordtoTweetDataHashMap().get(valueResult);
            Iterator<String> tempActualValueResultPairHashMapIter = tempActualValueResultPairHashMap.keySet().iterator();
            ArrayList<String> valueResultPairArrList = new ArrayList<String>();;
            while(tempActualValueResultPairHashMapIter.hasNext()) 
            {
               String keyo = tempActualValueResultPairHashMapIter.next(); 
               if(!key.equalsIgnoreCase(keyo))
               {
                  valueResultPairArrList = analysisData.getActualValueResultPairHashMap().get(keyo);
                  for (Iterator<String> iter = valueResultPairArrList.iterator(); iter.hasNext();)
                  {
                     String valueRes = (String) iter.next();
               //      if(tweetAnalysisData.getAllWordPairToNvNHashMap().containsKey(valueResult+"_"+valueRes))
                     if(analysisData.getKeyToNvNTripletsArrListHashMap().containsKey(valueResult+"_"+valueRes))   
                     {
                        ArrayList<EmoNvNTriplets> nvnTripletsArrListHashMap = analysisData.getKeyToNvNTripletsArrListHashMap().get(valueResult+"_"+valueRes);
                        for (EmoNvNTriplets emoNvNTriplets : nvnTripletsArrListHashMap)
                        {
                           emoNvNTriplets.setStrength(analysisData.getAllDistinctNVNEmoTweetsHashMap().get(emoNvNTriplets.getTweet().getTweetText()));
                           emoNvNTriplets.setTweets(analysisData.getNvnToEmoTweetsHashMap().get(emoNvNTriplets.toString()));
                           overallEmoNvNTripletsArrList.add(emoNvNTriplets);
                           updateResultAnalysisData(resAnalysisData,emoNvNTriplets);
                           
                        } 
                     }
                  }
               }
            }
         }
         overallBaseResultAnalysisData.getResultAnalysisDataHashMap().put(resAnalysisData.getNamedEntity(), resAnalysisData);
       //  overallBaseResultAnalysisData.getResultAnalysisDataArraylist().add(resAnalysisData);
         overallBaseResultAnalysisData.getAllNounOccHashMap().put(resAnalysisData.getNamedEntity(), resAnalysisData.getEmoTweetArrayList().size());
      }
   }

   private void updateResultAnalysisData(ResultAnalysisData resAnalysisData, EmoNvNTriplets emoNvNTriplets)
   {
      ArrayList<EmoTweet> emoTweetArrList = emoNvNTriplets.getTweets();
      for (Iterator<EmoTweet> iterator = emoTweetArrList.iterator(); iterator.hasNext();)
      {
         EmoTweet emoTweet = (EmoTweet) iterator.next();
         resAnalysisData.getEmoTweetArrayList().add(emoTweet);
         hmapUtil.checkOccurenceHashMapByOne(emoTweet.getTweetText()+" ---- "+emoNvNTriplets.toString(), resAnalysisData.getDistinctEmoTweets());
     //    resAnalysisData.getTweetIDHashMap().put(emoTweet.getTweetID(), emoTweet.getTweetText());
     //    resAnalysisData.getNVNHashMap().put(emoTweet.getTweetID(), emoNvNTriplets.toString());
      }
      hmapUtil.checkOccurenceHashMapByValue(emoNvNTriplets.getVerb(), resAnalysisData.getVerbOccHashMap(), emoNvNTriplets.getStrength());
      hmapUtil.checkOccurenceHashMapByValue(emoNvNTriplets.getVerb(), overallBaseResultAnalysisData.getAllVerbOccHashMap(), emoNvNTriplets.getStrength());
      hmapUtil.checkOccurenceHashMapByValue(emoNvNTriplets.getSecondNoun(), overallBaseResultAnalysisData.getAllNounOccHashMap(), emoNvNTriplets.getStrength());
   }

   private void retrieveWikiInfo()
   {
      EmoResultValue emoResValue = null;
      MediaWikiData mediaWikiData = null;
      Iterator<String> actualValueResultPairHashmapIterator = analysisData.getActualValueResultPairHashMap().keySet().iterator();
      ArrayList<EmoResultValue> emoResValArrList = new ArrayList<EmoResultValue>();
      while(actualValueResultPairHashmapIterator.hasNext()) 
      {
         String key = actualValueResultPairHashmapIterator.next();         
         emoResValue = new EmoResultValue();
         mediaWikiData = wikiAnalyzer.getSingleValueInformation(wordUtil.converToUnderscored(key));
         Item item = mediaWikiData.getSection().getItems().get(0);
         emoResValue.setKey(key);
         emoResValue.setName(item.getText());
         if(item.getImage()!=null)
         {
            emoResValue.setImageUrl(item.getImage().getSource());
         }
         emoResValue.setAbstractText(item.getDescription());
         emoResValue.setWikiUrl(item.getWikiUrl());
         emoResValue.setEmoTweetHashMap(overallBaseResultAnalysisData.getResultAnalysisDataHashMap().get(key).getDistinctEmoTweets());
         emoResValue.setEmoTweetArrayList(overallBaseResultAnalysisData.getResultAnalysisDataHashMap().get(key).getEmoTweetArrayList());
         emoResValue.setEmotionOccHashMap(overallBaseResultAnalysisData.getResultAnalysisDataHashMap().get(key).getEmotionOccHashMap());
         emoResValArrList.add(emoResValue);

      }
      emoResultDO.setEmoResultValueArrayList(emoResValArrList);
   }

   private void processResultPairs()
   {
      ArrayList<String> foundWords = null;
      LinkedHashMap<String, String> tempResPairHashMap = analysisData.getResultPairHashMap();
      LinkedHashMap<String, ArrayList<String>> actualValueResPair = new LinkedHashMap<String, ArrayList<String>>();
      Iterator<String> resultPairHashmapIterator = tempResPairHashMap.keySet().iterator();
      while(resultPairHashmapIterator.hasNext()) 
      {
         String key = resultPairHashmapIterator.next();
         String val = tempResPairHashMap.get(key);
         if(actualValueResPair.containsKey(val))
         {
            foundWords = actualValueResPair.get(val);
         }
         else
         {
            foundWords = new ArrayList<String>();
         }
         foundWords.add(key);
         actualValueResPair.put(val, foundWords);
      }
      analysisData.setActualValueResultPairHashMap(actualValueResPair);
   }

   private void logTriplets(String word)
   {
      writeToFileWithEdges(word+"_Overall",overallKeyOccHashMap,overallEmoNvNTripletsArrList,overallVerbStreghtHasMap,overallVerbOccHashMap);
      writeToFileWithEdges(word+"_Human",humanKeyOccHashMap,humanEmoNvNTripletsArrList,humanVerbStreghtHasMap,humanVerbOccHashMap);
      writeToFileWithEdges(word+"_Automated",automatedKeyOccHashMap,automatedEmoNvNTripletsArrList,automatedVerbStreghtHasMap,automatedVerbOccHashMap); 
      writeToFileWithEdges(word+"_Unknown",unknownKeyOccHashMap,unknownEmoNvNTripletsArrList,unknownVerbStreghtHasMap,unknownVerbOccHashMap); 
   }
   
   private void writeToFileWithEdges(String keyName, HashMap<String,Integer> verticeHashMap, ArrayList<EmoNvNTriplets> emoNvNTripletsArrList, HashMap<String, Integer> verbStreghtHasMap, HashMap<String, Integer> verbOccHashMap)
   {
      System.out.println("Pajek writing....");
      Calendar cal=Calendar.getInstance(Locale.US);
      HashMap<String, Integer> verticeToKey = new HashMap<String, Integer>();
      String displayDate = simpleDataFormatterForDisplay.format(cal.getTime());
      FileWriter fstream;
      try
      {
         fstream = new FileWriter(keyName+"_"+displayDate+".net");
         BufferedWriter out = new BufferedWriter(fstream);
         out.write("*Vertices "+ getVerticeSize(verticeHashMap,alreadyFoundWords,emoNvNTripletsArrList)+NL);
         Iterator<String> HashmapIterator = verticeHashMap.keySet().iterator();
         int verticeCount=1;
         while(HashmapIterator.hasNext()) 
         {
            String key = HashmapIterator.next(); 
            Integer val = verticeHashMap.get(key);
            if(!alreadyFoundWords.containsKey(key))
            {
               out.write(verticeCount+" \""+keyValHashMap.get(key)+"/"+val+"\" x_fact "+((float)val)/10+" y_fact "+((float)val)/10+" ic Blue bc Black"+NL);
               verticeToKey.put(keyValHashMap.get(key), verticeCount);
               verticeCount++;
            }
         }
         Iterator<String> verbHashmapIterator = verbOccHashMap.keySet().iterator();
         while(verbHashmapIterator.hasNext()) 
         {
            String key = verbHashmapIterator.next(); 
            Integer val = verbOccHashMap.get(key);
            if(!alreadyFoundWords.containsKey(key))
            {
               out.write(verticeCount+" \""+key+"/"+val+"\" diamond x_fact "+((float)val)/500+" y_fact "+((float)val)/500+" ic Green bc Black"+NL);
               verticeToKey.put(key, verticeCount);
               verticeCount++;
            }
         }
         out.write("*Arcs"+NL);
         for (Iterator<EmoNvNTriplets> iterator = emoNvNTripletsArrList.iterator(); iterator.hasNext();)
         {
            EmoNvNTriplets emoNvNTriplets = (EmoNvNTriplets) iterator.next();
            out.write(verticeToKey.get(keyValHashMap.get(emoNvNTriplets.getFirstNoun()))+" "+verticeToKey.get(emoNvNTriplets.getVerb())+" "+((float)verbStreghtHasMap.get(emoNvNTriplets.toString()))/10+" l \""+(float)verbStreghtHasMap.get(emoNvNTriplets.toString())+"\" c Red lc Black"+NL);
            out.write(verticeToKey.get(emoNvNTriplets.getVerb())+" "+verticeToKey.get(keyValHashMap.get(emoNvNTriplets.getSecondNoun()))+" "+((float)verbStreghtHasMap.get(emoNvNTriplets.toString()))/10+" l \""+(float)verbStreghtHasMap.get(emoNvNTriplets.toString())+"\" c Red lc Black"+NL);
         }
         out.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }    

   private int getVerticeSize(HashMap<String, Integer> verticeHashMap, HashMap<String, String> alreadyFoundWords, ArrayList<EmoNvNTriplets> emoNvNTripletsArrList)
   {
      Iterator<String> HashmapIterator = verticeHashMap.keySet().iterator();
      int verticeCount=0;
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next(); 
         if(!alreadyFoundWords.containsKey(key))
         {
            verticeCount++;
         }
      }
      return verticeCount+emoNvNTripletsArrList.size();
   }

   private void analyzeUser(EmoTweet emoTweet, EmoNvNTriplets emoNvNTriplets)
   {
      int uid = emoTweet.getUserID();
      if(!uidTypeHasMap.containsKey(uid))
      {
         Calendar cal=Calendar.getInstance();
         Paging paging = new Paging();
         paging.setCount(81);
         paging.setPage(1);
         User user =null;
         PagableResponseList<User> pagUserList;
         long createMS = 0;
         long intervalMS =0;
         long currentMS = cal.getTimeInMillis();
         try
         {
            pagUserList = TwitterImpl.getInstance().getTwitter().getFollowersStatuses(emoTweet.getUserID());
            if(pagUserList!=null && !pagUserList.isEmpty())
            {
               user = pagUserList.get(0);

               createMS = user.getCreatedAt().getTime();
               intervalMS = currentMS - createMS;
               long diffDays = intervalMS / (24 * 60 * 60 * 1000);
               if(diffDays!=0)
               {
                  int day =(int) (user.getStatusesCount()/diffDays);
                  if(80 >day && day>40)
                  {
                     System.out.println("---------------------------------------------------------");
                     System.out.println("-----------------yees there is 40----------------");
                     System.out.println("---------------------------------------------------------");
                  }
                  
                  if(day >80)
                  {
                     updateNvNTripletArrList(automatedEmoNvNTripletsArrList,emoNvNTriplets,automatedVerbStreghtHasMap,automatedKeyOccHashMap,automatedVerbOccHashMap);
                     uidTypeHasMap.put(uid, 80);
                  }
                  else
                  {
                     updateNvNTripletArrList(humanEmoNvNTripletsArrList,emoNvNTriplets,humanVerbStreghtHasMap,humanKeyOccHashMap,humanVerbOccHashMap);
                     uidTypeHasMap.put(uid, 40);
                  } 
               }
               else
               {
                  updateNvNTripletArrList(humanEmoNvNTripletsArrList,emoNvNTriplets,humanVerbStreghtHasMap,humanKeyOccHashMap,humanVerbOccHashMap);
                  uidTypeHasMap.put(uid, 40);
               }   
                
            }
            else
            {
               updateNvNTripletArrList(unknownEmoNvNTripletsArrList,emoNvNTriplets,unknownVerbStreghtHasMap,unknownKeyOccHashMap,unknownVerbOccHashMap);
               uidTypeHasMap.put(uid, 0);
            }   
         }
         catch (TwitterException e)
         {
            updateNvNTripletArrList(unknownEmoNvNTripletsArrList,emoNvNTriplets,unknownVerbStreghtHasMap,unknownKeyOccHashMap,unknownVerbOccHashMap);
            uidTypeHasMap.put(uid, 0);
         }
      }   
      else
      {
         int type = uidTypeHasMap.get(uid);
         if(type == 80)
         {
            updateNvNTripletArrList(automatedEmoNvNTripletsArrList,emoNvNTriplets,automatedVerbStreghtHasMap,automatedKeyOccHashMap,automatedVerbOccHashMap);
         }
         else if(type == 40)
         {
            updateNvNTripletArrList(humanEmoNvNTripletsArrList,emoNvNTriplets,humanVerbStreghtHasMap,humanKeyOccHashMap,humanVerbOccHashMap);
         }
         else if(type == 0)
         {
            updateNvNTripletArrList(unknownEmoNvNTripletsArrList,emoNvNTriplets,unknownVerbStreghtHasMap,unknownKeyOccHashMap,unknownVerbOccHashMap);
         }
      }   
   }

   private void updateNvNTripletArrList(ArrayList<EmoNvNTriplets> emoNvNTripletsArrList, EmoNvNTriplets emoNvNTriplets, HashMap<String, Integer> verbStreghtHasMap, HashMap<String, Integer> keyOccHashMap, HashMap<String, Integer> verbOccHashMap)
   {
      if(verbStreghtHasMap.containsKey(emoNvNTriplets.toString()))
      {
         Integer val = verbStreghtHasMap.get(emoNvNTriplets.toString());
         val =val+1;
         verbStreghtHasMap.put(emoNvNTriplets.toString(), val);
      }
      else
      {
         verbStreghtHasMap.put(emoNvNTriplets.toString(), 1);
         emoNvNTripletsArrList.add(emoNvNTriplets);
      }   
      updateKeyOccMap(emoNvNTriplets,keyOccHashMap);
      updateVerbOccMap(emoNvNTriplets.getVerb(),verbOccHashMap);
   }

   private void clearHashMaps()
   {
      emoResultDO = new EmoResultDO();
      uidTypeHasMap = new HashMap<Integer, Integer>();
      alreadyFoundWords = new HashMap<String, String>();
      
      overallEmoNvNTripletsArrList = new ArrayList<EmoNvNTriplets>();
      humanEmoNvNTripletsArrList = new ArrayList<EmoNvNTriplets>();
      automatedEmoNvNTripletsArrList = new ArrayList<EmoNvNTriplets>();
      unknownEmoNvNTripletsArrList = new ArrayList<EmoNvNTriplets>();
      
      overallVerbStreghtHasMap = new HashMap<String, Integer>();
      humanVerbStreghtHasMap = new HashMap<String, Integer>();
      automatedVerbStreghtHasMap = new HashMap<String, Integer>();
      unknownVerbStreghtHasMap = new HashMap<String, Integer>();
      
      overallKeyOccHashMap = new HashMap<String, Integer>();
      humanKeyOccHashMap = new HashMap<String, Integer>();
      automatedKeyOccHashMap = new HashMap<String, Integer>();
      unknownKeyOccHashMap = new HashMap<String, Integer>();
      
      overallVerbOccHashMap = new HashMap<String, Integer>();
      humanVerbOccHashMap = new HashMap<String, Integer>();
      automatedVerbOccHashMap = new HashMap<String, Integer>();
      unknownVerbOccHashMap = new HashMap<String, Integer>();
      writerHashMap = new HashMap<String,HashMap<String, String>>();
      
      
      foundAfterVerbWords = new HashMap<String, String>();
      foundBeforeVerbWords = new HashMap<String, String>();
      dateHashMap = new LinkedHashMap<String, String>();
      dateAllEmoTweetsHashMap = new HashMap<String,ArrayList<EmoTweet>>();
      dateAllWordsOccurenceHashMap = new HashMap<String,HashMap<String, Integer>>();
      dateNounWordHashMap = new HashMap<String,HashMap<String, EmoTweetWordData>>();

      dateSearchWordsOccurenceHashMap = new HashMap<String,HashMap<String, Integer>>();  
      dateCheckWordsOccurenceHashMap = new HashMap<String,HashMap<String, Integer>>(); 
      
      dateResultPairHashMap = new HashMap<String,LinkedHashMap<String, String>>();
      foundWords = new HashMap<String, String>();
   }

   private void createResultDO()
   {
      LinkedHashMap<String,ArrayList<EmoResultValue>> emoResultValues = new LinkedHashMap<String,ArrayList<EmoResultValue>>();
      Iterator<String> HashmapIterator = dateHashMap.keySet().iterator();
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next();
         keyValHashMap = dateResultPairHashMap.get(key);
         emoResultValues.put(key, processResultPairs(dateResultPairHashMap.get(key),dateNounWordHashMap.get(key)) );
      } 
      emoResultDO.setEmoResultValues(emoResultValues);
   }

   private ArrayList<EmoResultValue> processResultPairs(HashMap<String, String> resultPairHashMap, HashMap<String,EmoTweetWordData> nounWordHashMap)
   {
      EmoResultValue emoResValue = null;
      MediaWikiData mediaWikiData = null;
      WordUtil wUtil = new WordUtil(); 
      Iterator<String> HashmapIterator = resultPairHashMap.keySet().iterator();
      ArrayList<EmoResultValue> emoResValArrList = new ArrayList<EmoResultValue>();;
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next(); 
         String val = resultPairHashMap.get(key);        
         if(!foundWords.containsKey(key))
         {
            updateFoundWordsMap(val,key);
            emoResValue = new EmoResultValue();
            updateEmoResValue(key,emoResValArrList,emoResValue,nounWordHashMap);
            mediaWikiData = wikiAnalyzer.getSingleValueInformation(wUtil.converToUnderscored(val));
            Item item = mediaWikiData.getSection().getItems().get(0);
            emoResValue.setKey(key);
            emoResValue.setName(item.getText());
            if(item.getImage()!=null)
            {
               emoResValue.setImageUrl(item.getImage().getSource());
            }
            emoResValue.setAbstractText(item.getDescription());
            emoResValue.setWikiUrl(item.getWikiUrl());
            emoResValArrList.add(emoResValue);
         }
         else
         {
            emoResValue = new EmoResultValue();
            updateEmoResValue(key,emoResValArrList,emoResValue,nounWordHashMap);
            updateResultValues(key,emoResValArrList,emoResValue);
         }
      }
      return emoResValArrList;  
   }
   
   private void updateEmoResValue(String key, ArrayList<EmoResultValue> emoResValArrList, EmoResultValue emoResValue, HashMap<String, EmoTweetWordData> nounWordHashMap)
   {
      EmoTweetWordData emoTweWordData = nounWordHashMap.get(key);
      HashMap<String, Integer> afterVerbs = emoTweWordData.getAfterVerbs();
      Iterator<String> HashmapIterator = afterVerbs.keySet().iterator();
      String tweet;
      ArrayList<String> tweetArrList = null;
      while(HashmapIterator.hasNext()) 
      {
         String hkey = HashmapIterator.next();
         if(foundBeforeVerbWords.containsKey(hkey) && !foundBeforeVerbWords.get(hkey).equalsIgnoreCase(key))
         {
           // System.out.println("#######    "+key+" "+ hkey +" "+foundBeforeVerbWords.get(hkey));
            
            tweetArrList = searchTriplets(key,hkey,foundBeforeVerbWords.get(hkey));
            if(!tweetArrList.isEmpty())
            {
               for (Iterator<String> iterator = tweetArrList.iterator(); iterator.hasNext();)
               {
                  tweet = (String) iterator.next();
                  emoResValue.getTweetHashMap().put(tweet,tweet);
               }
               writerHashMap.put(key+" "+ hkey +" "+foundBeforeVerbWords.get(hkey), emoResValue.getTweetHashMap());
            }
         }
         foundAfterVerbWords.put(hkey, key);
    //     System.out.println("----------"+key+" ::::  "+hkey+" ; "+ val);
      }
      
      HashMap<String, Integer> beforeVerbs = emoTweWordData.getBeforeVerbs();
      Iterator<String> hashmapIterator = beforeVerbs.keySet().iterator();
      while(hashmapIterator.hasNext()) 
      {
         String hkey = hashmapIterator.next();
         if(foundAfterVerbWords.containsKey(hkey) && !foundAfterVerbWords.get(hkey).equalsIgnoreCase(key))
         {
        //    System.out.println("#######    "+foundAfterVerbWords.get(hkey)+" "+ hkey +" "+key);
            String kKey = foundAfterVerbWords.get(hkey);
            tweetArrList =  searchTriplets(kKey,hkey,key);
            if(!tweetArrList.isEmpty())
            {
               for (Iterator<EmoResultValue> iterator = emoResValArrList.iterator(); iterator.hasNext();)
               {
                  EmoResultValue emoResultValue = (EmoResultValue) iterator.next();
                  if(emoResultValue.getKey().equalsIgnoreCase(kKey))
                  {
                     for (Iterator<String> iterator1 = tweetArrList.iterator(); iterator1.hasNext();)
                     {
                        tweet = (String) iterator1.next();
                        emoResValue.getTweetHashMap().put(tweet,tweet);
                     }
                     writerHashMap.put(foundAfterVerbWords.get(hkey)+" "+ hkey +" "+key, emoResValue.getTweetHashMap());
                  }
               }  
            }
         }
         foundBeforeVerbWords.put(hkey, key);
      }
   }

   private ArrayList<String> searchTriplets(String firstWord, String verbWord, String secondWord)
   {
      EmoNvNTriplets emoNvNTriplets = new EmoNvNTriplets();
      emoNvNTriplets.setFirstNoun(firstWord);
      emoNvNTriplets.setVerb(verbWord);
      emoNvNTriplets.setSecondNoun(secondWord);
      boolean found = false;
      
      ArrayList<String> tweetArrList = new ArrayList<String>();
      WordNetData wnData = null;
      for (Iterator<EmoTweet> iterator = allTweetsArrList.iterator(); iterator.hasNext();)
      {
         EmoTweet emoTweet = (EmoTweet) iterator.next();
         String tweetText = emoTweet.getTweetText().toLowerCase(Locale.ENGLISH);
         boolean firstFound=false;
         boolean verbFound=false;
         StringTokenizer st = new StringTokenizer(tweetText);
         while (st.hasMoreTokens()) 
         {
            String currentWord = st.nextToken().toLowerCase(Locale.ENGLISH);
            if(!wordUtil.isSimplifiedStopWord(currentWord))
            {
               if(wordNetMap.containsKey(currentWord))
               {
                  wnData = wordNetMap.get(currentWord);
               }
               else
               {
                  wnData = null;
               }   
               if(firstFound ||  currentWord.contains(firstWord))
               {
                  if(firstFound)
                  {
                     if(!verbFound)
                     {
                        if(wnData!=null)
                        {
                           if(!wnData.isVerb() && wnData.isNoun())
                           {
                              break;
                           }
                        }
                        else
                        {
                           break;
                        }
                     }
                     
                     if(verbFound ||  currentWord.contains(verbWord))
                     {
                        if(verbFound)
                        {
                           if(currentWord.contains(secondWord))
                           {
                           //   System.out.println(tweetText);
                              tweetArrList.add(tweetText);
                              emoNvNTriplets.getTweets().add(emoTweet);
                              if(!found)
                              {
                                 overallEmoNvNTripletsArrList.add(emoNvNTriplets);
                              }
                              found=true;
                              updateKeyOccMap(emoNvNTriplets,overallKeyOccHashMap);
                              analyzeUser(emoTweet,emoNvNTriplets);
                           //   return tweetText;
                           }
                           else
                           {
                              if(wnData!=null)
                              {
                                 if(!wnData.isVerb() && wnData.isNoun())
                                 {
                                    break;
                                 }
                              }
                              else
                              {
                                 break;
                              } 
                           }
                        }
                        verbFound =true;  
                     }
                  }
                  firstFound =true;
               }
            }
            
         }
         if(found)
         {
            updateVerbStreghtHasMap(emoNvNTriplets,tweetArrList.size());
         }  
      }
      return tweetArrList;
   }

   private void updateVerbStreghtHasMap(EmoNvNTriplets emoNvNTriplets, int size)
   {
      overallVerbStreghtHasMap.put(emoNvNTriplets.toString(), size);
      updateVerbOccMap(emoNvNTriplets.getVerb(),overallVerbOccHashMap);
   }

   private void updateKeyOccMap(EmoNvNTriplets emoNvNTriplets,HashMap<String, Integer> hashMap)
   {
      updateforSingleKeyOccMap(emoNvNTriplets.getFirstNoun(),hashMap);
      updateforSingleKeyOccMap(emoNvNTriplets.getSecondNoun(),hashMap);
   }
   
   private void updateVerbOccMap(String key, HashMap<String,Integer> hashMap)
   {
      if(hashMap.containsKey(key))
      {
         Integer val = hashMap.get(key);
         val =val+1;
         hashMap.put(key, val);
      }
      else
      {
         hashMap.put(key, 1);
      }     
   }
   

   private void updateforSingleKeyOccMap(String key, HashMap<String,Integer> hashMap)
   {
      if(!alreadyFoundWords.containsKey(key))
      {
         if(hashMap.containsKey(key))
         {
            Integer val = hashMap.get(key);
            val =val+1;
            hashMap.put(key, val);
         }
         else
         {
            hashMap.put(key, 1);
         }
      }      
   }

   private void updateResultValues(String key, ArrayList<EmoResultValue> emoResValArrList, EmoResultValue aliasEmoResValue)
   {
      String fKey = foundWords.get(key);
      for (Iterator<EmoResultValue> iterator = emoResValArrList.iterator(); iterator.hasNext();)
      {
         EmoResultValue emoResultValue = (EmoResultValue) iterator.next();
         
         if(emoResultValue.getKey().equalsIgnoreCase(fKey))
         {
            System.out.println("          /////"+key+" to "+emoResultValue.getName());
            alreadyFoundWords.put(key, emoResultValue.getName());
            emoResultValue.setAlias(key);
            HashMap<String,String> aliasTweets = aliasEmoResValue.getTweetHashMap();
            if(aliasTweets!=null && !aliasTweets.isEmpty())
            {
               Iterator<String> hashmapIterator = aliasTweets.keySet().iterator();
               while(hashmapIterator.hasNext()) 
               {
                  String tweet = hashmapIterator.next(); 
                  emoResultValue.getTweetHashMap().put(tweet,tweet);
               }
            }
            
         } 
/*
         if(emoResultValue.getName().toLowerCase().contains(key))
         {
            emoResultValue.setAlias(key); 
         }   
*/            
            
      }
   }

   private void updateFoundWordsMap(String found, String key)
   {
      String meanWord[] = found.split(" ");
      for (int i = 0; i < meanWord.length; i++)
      {
         foundWords.put(wordUtil.covertToPlain(meanWord[i].toLowerCase(Locale.ENGLISH)), key);
      }
   }
   

}
