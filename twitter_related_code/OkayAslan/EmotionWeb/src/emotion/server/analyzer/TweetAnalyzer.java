package emotion.server.analyzer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.StringTokenizer;

import emotion.server.libstemmer.snowball.ext.englishStemmer;
import emotion.server.util.HashMapUtil;
import emotion.server.util.WordUtil;
import emotion.server.wordNet.WordNetDBImpl;
import emotion.server.wordNet.WordNetData;
import emotion.shared.EmoNvNTriplets;
import emotion.shared.EmoTweet;
import emotion.shared.EmoTweetWordData;

public class TweetAnalyzer
{
   private LinkedHashMap<String, String> dateHashMap = new LinkedHashMap<String, String>();
   private HashMap<String,ArrayList<EmoTweet>> dateAllEmoTweetsHashMap = new HashMap<String,ArrayList<EmoTweet>>();
   private HashMap<String,HashMap<String, Integer>> dateAllWordsOccurenceHashMap = new HashMap<String,HashMap<String, Integer>>();
   private HashMap<String,HashMap<String, EmoTweetWordData>> dateNounWordHashMap = new HashMap<String,HashMap<String, EmoTweetWordData>>();
   private HashMap<String,WordNetData> wordNetMap = new HashMap<String,WordNetData>();
   private englishStemmer stemmer = new englishStemmer();
   private static WordNetDBImpl wordNetdbImpl = WordNetDBImpl.getInstance();
   private WordUtil wordUtil = new WordUtil();
   private static String DATE_FORMAT_FOR_SORT      = "yyyyMMdd";
   private static String DATE_FORMAT_FOR_DISPLAY   = "dd-MMMMM-yyyy";
   private SimpleDateFormat simpleDataFormatterForDisplay = new SimpleDateFormat(DATE_FORMAT_FOR_DISPLAY,Locale.US);
   private SimpleDateFormat simpleDataFormatterForSort = new SimpleDateFormat(DATE_FORMAT_FOR_SORT,Locale.US);

   private HashMapUtil hMapUtil = new HashMapUtil();
   private AnalysisData analysisData = null;
   
   public TweetAnalyzer(LinkedHashMap<String, String> dateHashMap, HashMap<String, ArrayList<EmoTweet>> dateAllEmoTweetsHashMap,HashMap<String, HashMap<String, Integer>> dateAllWordsOccurenceHashMap, HashMap<String, HashMap<String, EmoTweetWordData>> dateNounWordHashMap, HashMap<String,WordNetData> wordNetMap)
   {
      this.dateHashMap = dateHashMap;
      this.dateAllEmoTweetsHashMap = dateAllEmoTweetsHashMap;
      this.dateAllWordsOccurenceHashMap = dateAllWordsOccurenceHashMap;
      this.dateNounWordHashMap = dateNounWordHashMap;
      this.wordNetMap = wordNetMap;
   } 
   
   public TweetAnalyzer(AnalysisData analysisData)
   {
      this.analysisData = analysisData;
   }

   public void analyzeTweets()
   {
      for (Iterator<EmoTweet> iterator = analysisData.getAllTweetArrayList().iterator(); iterator.hasNext();)
      {
         EmoTweet emoTweet = (EmoTweet) iterator.next();
         hMapUtil.checkOccurenceHashMapByOne(emoTweet.getTweetText(), analysisData.getAllDistinctEmoTweetsHashMap());
         emoTweet.setReTweet(emoTweet.getTweetText().startsWith("RT"));
   //      processTweet(emoTweet);
         if(emoTweet.getTweetDate().after(analysisData.getMaxDate()))
         {
            analysisData.setMaxDate(emoTweet.getTweetDate());
         }
         if(emoTweet.getTweetDate().before(analysisData.getMinDate()))
         {
            analysisData.setMinDate(emoTweet.getTweetDate());
         }  
            
         TweetAnalysisData tweAnalysisData = preProcessTweet(emoTweet);
         postProcessTweet(tweAnalysisData);
      }
   }
   
   private void postProcessTweet(TweetAnalysisData tweAnalysisData)
   {
      EmoTweet emoTweet = tweAnalysisData.getEmoTweet();
      
      int count = 0;
      
      if(emoTweet.isReTweet())
      {
         int rtCount = analysisData.getCountRTs();
         rtCount = rtCount+1;
         analysisData.setCountRTs(rtCount);
         if(tweAnalysisData.isNVN())
         {
            rtCount = analysisData.getCountNVNRTs();
            rtCount = rtCount+1;
            analysisData.setCountNVNRTs(rtCount);
         }
      }
      
      if(!tweAnalysisData.getWordOccurenceHashMap().isEmpty())
      {
         count = analysisData.getCountAllWords();
         count = count + hMapUtil.copyToHashMapAndGetTotalValue(tweAnalysisData.getWordOccurenceHashMap(),analysisData.getAllWordOccurrenceHashMap());
         analysisData.setCountAllWords(count);   
      }
      
      if(!tweAnalysisData.getSignificantWordOccurenceHashMap().isEmpty())
      {
         count = analysisData.getCountAllSignificantWords();
         count = count + hMapUtil.copyToHashMapAndGetTotalValue(tweAnalysisData.getSignificantWordOccurenceHashMap(),analysisData.getAllSignificantWordOccurrenceHashMap());
         analysisData.setCountAllSignificantWords(count); 
      }
     
      if(!tweAnalysisData.getnWordOccurenceHashMap().isEmpty())
      {
         count = analysisData.getCountAllnWords();
         count = count + hMapUtil.copyToHashMapAndGetTotalValue(tweAnalysisData.getnWordOccurenceHashMap(),analysisData.getAllnWordOccurrenceHashMap());
         analysisData.setCountAllnWords(count); 
      } 
      
      if(!tweAnalysisData.getVerbOccurenceHashMap().isEmpty())
      {
         count = analysisData.getCountAllVerbs();
         count = count + hMapUtil.copyToHashMapAndGetTotalValue(tweAnalysisData.getVerbOccurenceHashMap(),analysisData.getAllVerbOccurrenceHashMap());
         analysisData.setCountAllVerbs(count); 
      }
      

      if(tweAnalysisData.isNVN())
      {
         
         if(!tweAnalysisData.getWordOccurenceHashMap().isEmpty())
         {
            count = analysisData.getCountAllNVNWords();
            count = count + hMapUtil.copyToHashMapAndGetTotalValue(tweAnalysisData.getWordOccurenceHashMap(),analysisData.getAllNVNWordOccurrenceHashMap());
            analysisData.setCountAllNVNWords(count);   
         }
         
         if(!tweAnalysisData.getSignificantWordOccurenceHashMap().isEmpty())
         {
            count = analysisData.getCountAllNVNSignificantWords();
            count = count + hMapUtil.copyToHashMapAndGetTotalValue(tweAnalysisData.getSignificantWordOccurenceHashMap(),analysisData.getAllNVNSignificantWordOccurrenceHashMap());
            analysisData.setCountAllNVNSignificantWords(count); 
         }
        
         if(!tweAnalysisData.getnWordOccurenceHashMap().isEmpty())
         {
            count = analysisData.getCountAllNVNnWords();
            count = count + hMapUtil.copyToHashMapAndGetTotalValue(tweAnalysisData.getnWordOccurenceHashMap(),analysisData.getAllNVNnWordOccurrenceHashMap());
            analysisData.setCountAllNVNNwords(count); 
         } 
         
         if(!tweAnalysisData.getVerbOccurenceHashMap().isEmpty())
         {
            count = analysisData.getCountAllNVNVerbs();
            count = count + hMapUtil.copyToHashMapAndGetTotalValue(tweAnalysisData.getVerbOccurenceHashMap(),analysisData.getAllNVNVerbOccurrenceHashMap());
            analysisData.setCountAllNVNVerbs(count); 
         }

         hMapUtil.checkOccurenceHashMapByOne(emoTweet.getTweetText(), analysisData.getAllDistinctNVNEmoTweetsHashMap());
         analysisData.getAllNVNTweetsHashMap().put(emoTweet.getTweetID(), emoTweet);
         
         HashMap<String, EmoNvNTriplets> nvnTripletsHMap= tweAnalysisData.getNVNTripletsHashMap();
         Iterator<String> hashmapIterator = nvnTripletsHMap.keySet().iterator();
         ArrayList<EmoNvNTriplets> emoNvNTripletsArr = new ArrayList<EmoNvNTriplets>();
         while(hashmapIterator.hasNext()) 
         {
            String key = hashmapIterator.next(); 
            EmoNvNTriplets emoNvNTriplets = nvnTripletsHMap.get(key);
            emoNvNTripletsArr.add(emoNvNTriplets);
            
            ArrayList<EmoNvNTriplets> tempEmoNvNTripletsArr = null;
            if(analysisData.getKeyToNvNTripletsArrListHashMap().containsKey(key))
            {
               tempEmoNvNTripletsArr =analysisData.getKeyToNvNTripletsArrListHashMap().get(key);
            }
            else
            {
               tempEmoNvNTripletsArr = new ArrayList<EmoNvNTriplets>();
            }
            tempEmoNvNTripletsArr.add(emoNvNTriplets);
            analysisData.getKeyToNvNTripletsArrListHashMap().put(key,emoNvNTripletsArr); 
            
            ArrayList<EmoTweet> emoTweetArrList = null;
            if(analysisData.getNvnToEmoTweetsHashMap().containsKey(emoNvNTriplets.toString()))
            {
               emoTweetArrList =analysisData.getNvnToEmoTweetsHashMap().get(emoNvNTriplets.toString());
            }
            else
            {
               emoTweetArrList = new ArrayList<EmoTweet>();
               
            }
            emoTweetArrList.add(emoTweet);
            analysisData.getNvnToEmoTweetsHashMap().put(emoNvNTriplets.toString(),emoTweetArrList); 
         }
         analysisData.getTweetIdToNvNTripletsArrListHashMap().put(emoTweet.getTweetID(), emoNvNTripletsArr);
      }      
   }

   private TweetAnalysisData preProcessTweet(EmoTweet emoTweet)
   {
      TweetAnalysisData tweAnalysisData = new TweetAnalysisData();
      tweAnalysisData.setEmoTweet(emoTweet);
      
      EmoNvNTriplets emoNvNTriplet = new EmoNvNTriplets();
      
      StringTokenizer st = new StringTokenizer(emoTweet.getTweetText());
      while (st.hasMoreTokens()) 
      {
         String currentWord = st.nextToken().toLowerCase(Locale.ENGLISH);
         currentWord = wordUtil.clearPunctuation(currentWord);
         if(currentWord!=null)
         { 
            hMapUtil.checkOccurenceHashMapByOne(currentWord, tweAnalysisData.getWordOccurenceHashMap());
            hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllWordOccurrenceHashMap());
            
            if(!wordUtil.isSimplifiedStopWord(currentWord) && wordUtil.isAvailable(currentWord) )
            {
               hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllSignificantWordOccurrenceHashMap());
               hMapUtil.checkOccurenceHashMapByOne(currentWord, tweAnalysisData.getSignificantWordOccurenceHashMap());
               WordNetData wNData = getWordNetData(currentWord);               
               if(!wordUtil.isExceptionalWord(currentWord) && wNData!=null)
               {
                  String plainForm = wNData.getPlainForm();
                  analysisData.getWordsToPlainHashMap().put(currentWord, plainForm);
                  if(wNData.isJustNoun())
                  {
                     hMapUtil.checkOccurenceHashMapByOne(currentWord, tweAnalysisData.getNounOccurenceHashMap());
                     hMapUtil.checkOccurenceHashMapByOne(currentWord, tweAnalysisData.getnWordOccurenceHashMap());
                     
                     updateAnalysisDataAsNounfound(plainForm,false);
                     if(emoNvNTriplet.getFirstNoun()!=null)
                     {
                        if(emoNvNTriplet.getVerb()!=null)
                        {
                           tweAnalysisData.setNVN(true);
                           emoNvNTriplet.setSecondNoun(currentWord);
                           emoNvNTriplet.setTweet(emoTweet);
                           tweAnalysisData.getNVNTripletsHashMap().put(emoNvNTriplet.getFirstNoun()+"_"+emoNvNTriplet.getSecondNoun(), emoNvNTriplet);
                           tweAnalysisData.getFirstWordToNvNHashMap().put(emoNvNTriplet.getFirstNoun(), emoNvNTriplet);
                           tweAnalysisData.getSecondWordToNvNHashMap().put(emoNvNTriplet.getSecondNoun(), emoNvNTriplet);
                           emoNvNTriplet = new EmoNvNTriplets();
                        }
                        else
                        {
                           emoNvNTriplet.setFirstNoun(currentWord);
                        }   
                     }
                     else
                     {
                        emoNvNTriplet.setFirstNoun(currentWord);
                     }  
                  }   
                  else if(wNData.isVerb())
                  {
                     hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllVerbOccurrenceHashMap());
                     hMapUtil.checkOccurenceHashMapByOne(currentWord, tweAnalysisData.getVerbOccurenceHashMap());
                     hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllPlainWordOccurrenceHashMap());
                     
                     if(emoNvNTriplet.getFirstNoun()!=null)
                     {
                       emoNvNTriplet.setVerb(plainForm);
                     }
                  }
                  else if(wNData.isAdjective())
                  {
                     hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllPlainWordOccurrenceHashMap());
                     hMapUtil.checkOccurenceHashMapByOne(currentWord, tweAnalysisData.getAdjectiveOccurenceHashMap());
                  }                     
               }
               else
               {
                  analysisData.getWordsToPlainHashMap().put(currentWord, currentWord);
                  hMapUtil.checkOccurenceHashMapByOne(currentWord, tweAnalysisData.getNamedEntityCandidateOccurenceHashMap());
                  hMapUtil.checkOccurenceHashMapByOne(currentWord, tweAnalysisData.getnWordOccurenceHashMap());
                  updateAnalysisDataAsNounfound(currentWord,true);
                  if(emoNvNTriplet.getFirstNoun()!=null)
                  {
                     if(emoNvNTriplet.getVerb()!=null)
                     {
                        tweAnalysisData.setNVN(true);          
                        emoNvNTriplet.setSecondNoun(currentWord);
                        emoNvNTriplet.setTweet(emoTweet);
                        
                        tweAnalysisData.getNVNTripletsHashMap().put(emoNvNTriplet.getFirstNoun()+"_"+emoNvNTriplet.getSecondNoun(), emoNvNTriplet);
                        tweAnalysisData.getFirstWordToNvNHashMap().put(emoNvNTriplet.getFirstNoun(), emoNvNTriplet);
                        tweAnalysisData.getSecondWordToNvNHashMap().put(emoNvNTriplet.getSecondNoun(), emoNvNTriplet);
                        emoNvNTriplet = new EmoNvNTriplets();
                     }
                     else
                     {
                        emoNvNTriplet.setFirstNoun(currentWord);
                     }   
                  }
                  else
                  {
                     emoNvNTriplet.setFirstNoun(currentWord);
                  }
               } 
            } 
         }
      }
      return tweAnalysisData;
   }

   private void processTweet(EmoTweet emoTweet)
   {
      HashMap<String,String> nearestWordFoundHashMap = new HashMap<String,String>();
      HashMap<String,String> nWordFoundHashMap = new HashMap<String,String>();
      HashMap<String,String> adjectivesHashMap = new HashMap<String,String>();
      EmoNvNTriplets emoNvNTriplet = new EmoNvNTriplets();
      HashMap<String,EmoNvNTriplets> firstWordToNvNHashMap = new HashMap<String,EmoNvNTriplets>();
      HashMap<String,EmoNvNTriplets> secondWordToNvNHashMap = new HashMap<String,EmoNvNTriplets>();
      
      StringTokenizer st = new StringTokenizer(emoTweet.getTweetText());
      while (st.hasMoreTokens()) 
      {
         String currentWord = st.nextToken().toLowerCase(Locale.ENGLISH);
         currentWord = wordUtil.clearPunctuation(currentWord);
         if(currentWord!=null)
         { 
            hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllWordOccurrenceHashMap());
            if(!wordUtil.isSimplifiedStopWord(currentWord) && wordUtil.isAvailable(currentWord) )
            {
               hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllSignificantWordOccurrenceHashMap());
               WordNetData wNData = getWordNetData(currentWord);               
               if(!wordUtil.isExceptionalWord(currentWord) && wNData!=null)
               {
                  String plainForm = wNData.getPlainForm();
                  analysisData.getWordsToPlainHashMap().put(currentWord, plainForm);
                  if(wNData.isJustNoun())
                  {
                     nWordFoundHashMap.put(plainForm, plainForm);
                     updateAnalysisDataAsNounfound(plainForm,false);
                     if(emoNvNTriplet.getFirstNoun()!=null)
                     {
                        if(emoNvNTriplet.getVerb()!=null)
                        {
                           hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllNVNWordOccurrenceHashMap());
                           hMapUtil.checkOccurenceHashMapByOne(emoTweet.getTweetText(), analysisData.getAllDistinctNVNEmoTweetsHashMap());
                           analysisData.getAllNVNTweetsHashMap().put(emoTweet.getTweetID(), emoTweet);
                           emoNvNTriplet.setSecondNoun(currentWord);
                           emoNvNTriplet.setTweet(emoTweet);
                           firstWordToNvNHashMap.put(emoNvNTriplet.getFirstNoun(), emoNvNTriplet);
                           secondWordToNvNHashMap.put(emoNvNTriplet.getSecondNoun(), emoNvNTriplet);
                           emoNvNTriplet = new EmoNvNTriplets();
                        }
                        else
                        {
                           nearestWordFoundHashMap.put(emoNvNTriplet.getFirstNoun(),emoNvNTriplet.getSecondNoun());
                           emoNvNTriplet.setFirstNoun(currentWord);
                        }   
                     }
                     else
                     {
                        emoNvNTriplet.setFirstNoun(currentWord);
                     }  
                  }   
                  else if(wNData.isVerb())
                  {
                     hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllVerbOccurrenceHashMap());
                     if(emoNvNTriplet.getFirstNoun()!=null)
                     {
                        hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllNVNVerbOccurrenceHashMap());
                        hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllPlainWordOccurrenceHashMap());
                        emoNvNTriplet.setVerb(plainForm);
                     }
                  }
                  else if(wNData.isAdjective())
                  {
                     hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllPlainWordOccurrenceHashMap());
                     adjectivesHashMap.put(plainForm, plainForm);
                  }                     
               }
               else
               {
                  analysisData.getWordsToPlainHashMap().put(currentWord, currentWord);
                  nWordFoundHashMap.put(currentWord, currentWord);
                  updateAnalysisDataAsNounfound(currentWord,true);
                  if(emoNvNTriplet.getFirstNoun()!=null)
                  {
                     if(emoNvNTriplet.getVerb()!=null)
                     {
                        hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllNVNWordOccurrenceHashMap());
                        hMapUtil.checkOccurenceHashMapByOne(emoTweet.getTweetText(), analysisData.getAllDistinctNVNEmoTweetsHashMap());
                        analysisData.getAllNVNTweetsHashMap().put(emoTweet.getTweetID(), emoTweet);
                        emoNvNTriplet.setSecondNoun(currentWord);
                        emoNvNTriplet.setTweet(emoTweet);
                        firstWordToNvNHashMap.put(emoNvNTriplet.getFirstNoun(), emoNvNTriplet);
                        secondWordToNvNHashMap.put(emoNvNTriplet.getSecondNoun(), emoNvNTriplet);
                        emoNvNTriplet = new EmoNvNTriplets();
                     }
                     else
                     {
                        nearestWordFoundHashMap.put(emoNvNTriplet.getFirstNoun(),emoNvNTriplet.getSecondNoun());
                        emoNvNTriplet.setFirstNoun(currentWord);
                     }   
                  }
                  else
                  {
                     emoNvNTriplet.setFirstNoun(currentWord);
                  }
               } 
            } 
         }
      } 
      updateAnalysisDataAsTweetData(nWordFoundHashMap,nearestWordFoundHashMap,adjectivesHashMap,firstWordToNvNHashMap,secondWordToNvNHashMap,emoTweet);
   }

   private void updateAnalysisDataAsTweetData(HashMap<String, String> nWordFoundHashMap, HashMap<String, String> nearestWordFoundHashMap, HashMap<String, String> adjectivesHashMap,HashMap<String, EmoNvNTriplets> firstWordToNvNHashMap, HashMap<String, EmoNvNTriplets> secondWordToNvNHashMap, EmoTweet emoTweet)
   {
      WordToTweetAnalysisData wordToTweetAnalysisData = null;
      EmoNvNTriplets emoNvNTriplet = null;
      Iterator<String> nWordFoundHashMapIterator = nWordFoundHashMap.keySet().iterator();
      if(emoTweet.isReTweet())
      {
         int rtCount = analysisData.getCountRTs();
         rtCount = rtCount+1;
         analysisData.setCountRTs(rtCount);
         if(!firstWordToNvNHashMap.isEmpty())
         {
            rtCount = analysisData.getCountNVNRTs();
            rtCount = rtCount+1;
            analysisData.setCountNVNRTs(rtCount++);
         }
      }
      
      while(nWordFoundHashMapIterator.hasNext()) 
      {
         String key = nWordFoundHashMapIterator.next(); 
         if(analysisData.getWordtoTweetDataHashMap().containsKey(key))
         {
            wordToTweetAnalysisData = analysisData.getWordtoTweetDataHashMap().get(key);
         }
         else
         {
            wordToTweetAnalysisData = new WordToTweetAnalysisData();
            wordToTweetAnalysisData.setPlainWord(key);
         }
         wordToTweetAnalysisData.getFoundTweetsArrayList().add(emoTweet);
         hMapUtil.checkOccurenceHashMapByOne(key, wordToTweetAnalysisData.getFoundTweetOccurenceHashMap());
         
         Iterator<String> nWordFoundHashMapIter = nWordFoundHashMap.keySet().iterator();
         while(nWordFoundHashMapIter.hasNext()) 
         {
            String keyo = nWordFoundHashMapIter.next();
            if(!keyo.equals(key))
            {
               hMapUtil.checkOccurenceHashMapByOne(keyo, wordToTweetAnalysisData.getFoundNWordOccurenceHashMap());
            }
         }
         boolean alreadyfound = false;
         if(firstWordToNvNHashMap.containsKey(key))
         {
            emoNvNTriplet = firstWordToNvNHashMap.get(key);
            ArrayList<EmoTweet> emoTweetArrList = null;
            if(wordToTweetAnalysisData.getFirstWordToNvNHashMap().containsKey(emoNvNTriplet.toString()))
            {
              emoTweetArrList = wordToTweetAnalysisData.getFirstWordToNvNHashMap().get(emoNvNTriplet.toString()); 
            }
            else
            {
               emoTweetArrList = new ArrayList<EmoTweet>();
            }
            emoTweetArrList.add(emoTweet);
            wordToTweetAnalysisData.getFirstWordToNvNHashMap().put(emoNvNTriplet.toString(), emoTweetArrList);
            wordToTweetAnalysisData.getFoundPossibleNvNTweetsArrayList().add(emoTweet);
            hMapUtil.checkOccurenceHashMapByOne(emoNvNTriplet.toString(),wordToTweetAnalysisData.getFoundNvNOccurenceHashMap());
            updateTweAnalyWordPairHashMap(emoNvNTriplet,wordToTweetAnalysisData,true);
            alreadyfound = true;
         }
         if(secondWordToNvNHashMap.containsKey(key))
         {
            emoNvNTriplet = secondWordToNvNHashMap.get(key);
            ArrayList<EmoTweet> emoTweetArrList = null;
            if(wordToTweetAnalysisData.getSecondWordToNvNHashMap().containsKey(emoNvNTriplet.toString()))
            {
              emoTweetArrList = wordToTweetAnalysisData.getSecondWordToNvNHashMap().get(emoNvNTriplet.toString()); 
            }
            else
            {
               emoTweetArrList = new ArrayList<EmoTweet>();
            }
            emoTweetArrList.add(emoTweet);
            wordToTweetAnalysisData.getSecondWordToNvNHashMap().put(emoNvNTriplet.toString(), emoTweetArrList);
            updateTweAnalyWordPairHashMap(emoNvNTriplet,wordToTweetAnalysisData,false);
            if(!alreadyfound)
            {
               hMapUtil.checkOccurenceHashMapByOne(emoNvNTriplet.toString(),wordToTweetAnalysisData.getFoundNvNOccurenceHashMap()); 
               wordToTweetAnalysisData.getFoundPossibleNvNTweetsArrayList().add(emoTweet);
            }  
         }
         
         Iterator<String> adjectivesHashMapIter = adjectivesHashMap.keySet().iterator();
         while(adjectivesHashMapIter.hasNext()) 
         {
            String keyo = adjectivesHashMapIter.next();
            if(!keyo.equals(key))
            {
               hMapUtil.checkOccurenceHashMapByOne(keyo, wordToTweetAnalysisData.getAdjectivesOccurenceHashMap());
            }
         }
         analysisData.getWordtoTweetDataHashMap().put(key, wordToTweetAnalysisData);
      }
   }

   private void updateTweAnalyWordPairHashMap(EmoNvNTriplets emoNvNTriplet, WordToTweetAnalysisData tweetAnalysisData, boolean first)
   {
      HashMap<String, String> wordPairHashMap = null;
      if(first)
      {
         String firstNvNString = emoNvNTriplet.getFirstNoun()+"_"+emoNvNTriplet.getSecondNoun();
         if(tweetAnalysisData.getAllWordPairToNvNHashMap().containsKey(firstNvNString))
         {
            wordPairHashMap = tweetAnalysisData.getAllWordPairToNvNHashMap().get(firstNvNString); 
         }
         else
         {
            wordPairHashMap = new HashMap<String,String>();
         }
         wordPairHashMap.put(emoNvNTriplet.toString(), emoNvNTriplet.getVerb());
         tweetAnalysisData.getAllWordPairToNvNHashMap().put(firstNvNString, wordPairHashMap);
      }
      else
      {
         String secondNvNString = emoNvNTriplet.getFirstNoun()+"_"+emoNvNTriplet.getSecondNoun();
         if(tweetAnalysisData.getAllWordPairToNvNHashMap().containsKey(secondNvNString))
         {
            wordPairHashMap = tweetAnalysisData.getAllWordPairToNvNHashMap().get(secondNvNString); 
         }
         else
         {
            wordPairHashMap = new HashMap<String,String>();
         }
         wordPairHashMap.put(emoNvNTriplet.toString(), emoNvNTriplet.getVerb());
         tweetAnalysisData.getAllWordPairToNvNHashMap().put(secondNvNString, wordPairHashMap);
      }
   }

   private void updateAnalysisDataAsNounfound(String currentWord,boolean posNamedEntity)
   {
      if(posNamedEntity)
      {
         hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getNamedEntityCandidateOccurrenceHashMap());
      }
      else
      {
         hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getNounWordOccurrenceHashMap());
      }
   //   analysisData.getWordsToPlainHashMap().put(currentWord, currentWord);
      hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllnWordOccurrenceHashMap());
  //    hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllSignificantWordOccurrenceHashMap());
      hMapUtil.checkOccurenceHashMapByOne(currentWord, analysisData.getAllPlainWordOccurrenceHashMap());
   }

   public void analyzeTweetsByDate(String word, ArrayList<EmoTweet> allTweetsFromTable)
   {
      Calendar cal=Calendar.getInstance();
      for (Iterator<EmoTweet> iterator = allTweetsFromTable.iterator(); iterator.hasNext();)
      {
         EmoTweet emoTweet = (EmoTweet) iterator.next();
         cal.setTime(emoTweet.getTweetDate());
         String displayDate = simpleDataFormatterForDisplay.format(cal.getTime());
         if(dateAllEmoTweetsHashMap.containsKey(displayDate))
         {
            dateAllEmoTweetsHashMap.get(displayDate).add(emoTweet);
         }  
         else
         {
            ArrayList<EmoTweet> tempEmoTweetArrList = new ArrayList<EmoTweet>();
            tempEmoTweetArrList.add(emoTweet);
            dateAllEmoTweetsHashMap.put(displayDate, tempEmoTweetArrList);
            dateHashMap.put(displayDate, simpleDataFormatterForSort.format(cal.getTime()));
         }
      }
      analyzeWords(word);
   }   
   
   private void analyzeWords(String word)
   {
      Iterator<String> HashmapIterator = dateAllEmoTweetsHashMap.keySet().iterator();
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next(); 
         ArrayList<EmoTweet> tempEmoTweetArrList = dateAllEmoTweetsHashMap.get(key);
         HashMap<String, Integer> emotionHashMapByWord = new HashMap<String, Integer>();
         HashMap<String,EmoTweetWordData> wordDataMap = new HashMap<String,EmoTweetWordData>();
         processWords(emotionHashMapByWord,tempEmoTweetArrList,wordDataMap);
         dateAllWordsOccurenceHashMap.put(key, emotionHashMapByWord);
         dateNounWordHashMap.put(key, wordDataMap);
      }
   }

   private void processWords(HashMap<String,Integer> emotionHashMapByWord, ArrayList<EmoTweet> tempEmoTweetArrList, HashMap<String, EmoTweetWordData> wordDataMap)
   {
      for (Iterator<EmoTweet> iterator = tempEmoTweetArrList.iterator(); iterator.hasNext();)
      {
         EmoTweet emoTweet = (EmoTweet) iterator.next();
         EmoTweetWordData emoTweetWordData = new EmoTweetWordData();
         
         StringTokenizer st = new StringTokenizer(emoTweet.getTweetText());
         while (st.hasMoreTokens()) 
         {
            String currentWord = st.nextToken().toLowerCase(Locale.ENGLISH);
            currentWord = wordUtil.clearPunctuation(currentWord);
            if(currentWord!=null)
            { 
               if(!wordUtil.isSimplifiedStopWord(currentWord))
               {
                  checkEmotionHashMapByWord(emotionHashMapByWord,currentWord);
                  WordNetData wNData = getWordNetData(currentWord);               
                  if(wNData!=null)
                  {
                     String plainForm = wNData.getPlainForm();
                    
                     if(wNData.isNoun() && !wNData.isVerb() && !wNData.isAdjective())
                     {
                        if(emoTweetWordData.getNoun()!= null)
                        {
                           EmoTweetWordData tempEmoTweetWordData = new EmoTweetWordData();
                           tempEmoTweetWordData.setBeforeAdjectives(emoTweetWordData.getAfterAdjectives());
                           tempEmoTweetWordData.setBeforeVerbs(emoTweetWordData.getAfterVerbs());
                           tempEmoTweetWordData.setNoun(currentWord);
                           emoTweetWordData.getAfterNouns().put(currentWord, 1);
                           if(wordDataMap.containsKey(emoTweetWordData.getNoun()))
                           {
                              updapteWordDataMap(emoTweetWordData.getNoun(),emoTweetWordData, wordDataMap);
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
                     else if(wNData.isVerb())
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
                     else if(wNData.isAdjective())
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
                           updapteWordDataMap(emoTweetWordData.getNoun(),emoTweetWordData, wordDataMap);  
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
               updapteWordDataMap(emoTweetWordData.getNoun(),emoTweetWordData, wordDataMap);  
            }
            else
            {   
               wordDataMap.put(emoTweetWordData.getNoun(), emoTweetWordData);
            }
         }
      }  
   }

   private WordNetData getWordNetData(String currentWord)
   {
      if(wordNetMap.containsKey(currentWord))
      {
         return wordNetMap.get(currentWord);
      }
      else
      {
         WordNetData wNData = wordNetdbImpl.getWordProperties(currentWord);
         if(wNData==null)
         {
            stemmer.setCurrent(currentWord);
            stemmer.stem();
            String stemmedWord = stemmer.getCurrent();
            if(wordNetMap.containsKey(stemmedWord))
            {
               return wordNetMap.get(stemmedWord);
            }
            else
            {
               wNData = wordNetdbImpl.getWordProperties(stemmedWord);
               if(wNData!=null)
               {
                  wNData.setWord(currentWord);
                  wordNetMap.put(stemmedWord, wNData);
                  return wNData;
               }
            }   
         }
         else
         {
            wNData.setWord(currentWord);
            wordNetMap.put(currentWord, wNData);
            return wNData;
         }   
      }
      return null;
   }

   private void checkEmotionHashMapByWord(HashMap<String, Integer> emotionHashMapByWord, String currentWord)
   {
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
   }
   
   private void updapteWordDataMap(String plainForm, EmoTweetWordData emoTweetWordData,HashMap<String,EmoTweetWordData> wordDataMap)
   {
      EmoTweetWordData tempEmoTweetWordData = wordDataMap.get(plainForm);
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
   
   private void updateWordMap(HashMap<String, Integer> toWordHashMap, HashMap<String, Integer> fromWordHashMap)
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
      }
   }
         
}
