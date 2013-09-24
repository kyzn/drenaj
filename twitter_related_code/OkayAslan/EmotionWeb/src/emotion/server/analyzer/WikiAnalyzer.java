package emotion.server.analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import emotion.server.dbPedia.DBPediaData;
import emotion.server.dbPedia.DBPediaImpl;
import emotion.server.mediaWiki.MWApi;
import emotion.server.mediaWiki.MWTitle;
import emotion.server.mediaWiki.MediaWikiData;
import emotion.server.mediaWiki.MediaWikiImpl;
import emotion.server.util.HashMapUtil;
import emotion.server.util.WordUtil;
import emotion.shared.EmoTweetResultData;

public class WikiAnalyzer
{
   private static MediaWikiImpl mediaWikiImpl = new MediaWikiImpl();
   private static MWApi mediaWikiApi = null;
   private WordUtil wordUtil = new WordUtil();
   private static DBPediaImpl dbpediaImpl = new DBPediaImpl();
   private static HashMapUtil hMapUtil = new HashMapUtil();
   private static HashMap<String,EmoTweetResultData> resultMap = new HashMap<String,EmoTweetResultData>();
   private LinkedHashMap<String, String> dateHashMap = new LinkedHashMap<String, String>();
   private HashMap<String,HashMap<String, Integer>> dateSearchWordsOccurenceHashMap = new HashMap<String,HashMap<String, Integer>>();  
   private HashMap<String,HashMap<String, Integer>> dateCheckWordsOccurenceHashMap = new HashMap<String,HashMap<String, Integer>>();
   private HashMap<String,LinkedHashMap<String, String>> dateResultPairHashMap = new HashMap<String,LinkedHashMap<String, String>>();
   private HashMap<String, Integer> checkHashMapByWord = null;
   private HashMap<String, Integer> searchHashMapByWord = null;
   private HashMap<String, Integer> resultHashMap = new HashMap<String, Integer>();
   private LinkedHashMap<String, String> resultPairHashMap = new LinkedHashMap<String, String>();
   
   private Map<String, String> threadResultPairHashMap = null; 
   
   private HashMap<String, String> foundWords = new HashMap<String, String>();
   
   private HashMap<String, String> originalToModifiedTitleHashMap = new HashMap<String, String>();
   private AnalysisData analysisData = null;
   
   public WikiAnalyzer(LinkedHashMap<String, String> dateHashMap, HashMap<String, HashMap<String, Integer>> dateSearchWordsOccurenceHashMap,HashMap<String, HashMap<String, Integer>> dateCheckWordsOccurenceHashMap, HashMap<String,LinkedHashMap<String,String>> dateResultPairHashMap)
   {
      this.dateHashMap = dateHashMap;
      this.dateSearchWordsOccurenceHashMap = dateSearchWordsOccurenceHashMap;
      this.dateCheckWordsOccurenceHashMap = dateCheckWordsOccurenceHashMap;
      this.dateResultPairHashMap = dateResultPairHashMap;
   }
   
   public WikiAnalyzer(AnalysisData analysisData)
   {
      threadResultPairHashMap  = Collections.synchronizedMap(new HashMap<String, String>());
      this.analysisData = analysisData;
   }
   
   public void analyzeWiki()
   {
      int count = 0;
      if(analysisData.getSearchWordsOccurrenceHashMap().size()<20)
      {
         count = analysisData.getSearchWordsOccurrenceHashMap().size();
      }
      else
      {
         count = 20;
      }   
      int num=0;
      ArrayList<String> wordList = new ArrayList<String>();
      Thread threadList[] = new Thread[count];
      Iterator<String> HashmapIterator = analysisData.getSearchWordsOccurrenceHashMap().keySet().iterator();
      while(HashmapIterator.hasNext()) 
      {
         final String key = HashmapIterator.next();
         wordList.add(key);
         System.out.println(key + " is inserted");
         threadList[num] = new Thread(new Runnable()
         {
            public void run()
            {
               processWikiTitle(key);    
            }
         });
         if(num==count-1)
         {
            break;
         }
         num++;
      }
      
      for (int j = 0; j < threadList.length; j++)
      {
         Thread thread = threadList[j];
         thread.start();
         try
         {
            synchronized (this) {
               this.wait(500);
           }
         }
         catch (InterruptedException e)
         {
            e.printStackTrace();
         }
      }
      
      for (int j = 0; j < threadList.length; j++)
      {
         Thread thread = threadList[j];
         try
         {
            thread.join();
         }
         catch (InterruptedException e)
         {
            e.printStackTrace();
         }
      }
      prepareResPairHmap(wordList);
   } 
   
   public void processWikiTitle(String key)
   {
      if(wordUtil.isExceptionalWord(key))
      {
         threadResultPairHashMap.put(key, key);
         return;
      }
      else if(wordUtil.isProhibited(key))
      {
         return;
      }
      
      ArrayList<String> titleArrayList = new ArrayList<String>();
      HashMap<String, Integer> nWordOccHashMap = analysisData.getWordtoTweetDataHashMap().get(key).getFoundNWordOccurenceHashMap();
      HashMap<String, Integer> coOccurenceHashMap = new HashMap<String, Integer>();
      Integer originalOccurence = analysisData.getAllnWordOccurrenceHashMap().get(key);
      boolean sameFound = false;
      String sameTitle = null; 
      String[] titleArr = getWikiTitleList(key);
      
      if(titleArr!=null)
      {
         for (int i = 0; i < titleArr.length; i++)
         {
            String originalTitle = titleArr[i];
            String modifiedTitle = wordUtil.deAccent(originalTitle.toLowerCase());
            String[] titleTokenArr = modifiedTitle.split(" ");
            ArrayList<String> tokenArrayList = new ArrayList<String>();
            for (int j = 0; j < titleTokenArr.length; j++)
            {
               String token = wordUtil.clearPunctuation(titleTokenArr[j]);
               if(token!=null)
               {
                  tokenArrayList.add(token); 
               }  
            }
            titleTokenArr = (String []) tokenArrayList.toArray (new String [tokenArrayList.size()]);
            if(titleTokenArr.length==1)
            {
               String token = titleTokenArr[0];
               if(token.equalsIgnoreCase(key))
               {
                  titleArrayList.add(originalTitle);
                  sameFound = true;
                  sameTitle = originalTitle;
               }
               else
               {
                  if(nWordOccHashMap.containsKey(token))
                  {
                     if(!sameFound)
                     {
                        coOccurenceHashMap.put(originalTitle, nWordOccHashMap.get(token));
                     //   titleArrayList.add(originalTitle);
                    //    threadResultPairHashMap.put(key, originalTitle);
                    //    return;
                     }
                     else
                     {
                        titleArrayList.add(originalTitle);
                     }                       
                  }
                  else if(analysisData.getSearchWordsOccurrenceHashMap().containsKey(token))
                  {
                     coOccurenceHashMap.put(originalTitle, analysisData.getSearchWordsOccurrenceHashMap().get(token));
                //     titleArrayList.add(originalTitle);
                //     System.out.println(key + " is recognized as " + originalTitle);
                //     threadResultPairHashMap.put(key, originalTitle);
               //      return;
                  }
                  else if(analysisData.getCheckWordsOccurrenceHashMap().containsKey(token))
                  {
                     titleArrayList.add(originalTitle);
                  }
               }   
            }
            else if (titleTokenArr.length>1)
            {
               int otherFound = 0;
               int found = 0;
               int lowest = Integer.MAX_VALUE;
               for (int j = 0; j < titleTokenArr.length; j++)
               {
                  String titleToken = titleTokenArr[j];
                  if(!titleToken.equalsIgnoreCase(key) && !wordUtil.isSimplifiedStopWord(titleToken))
                  {
                     if(nWordOccHashMap.containsKey(titleToken))
                     {
                        if(nWordOccHashMap.get(titleToken)<lowest)
                        {
                           lowest = nWordOccHashMap.get(titleToken);
                        }
                        found++;
                     }
                  }
                  else
                  {
                     otherFound++;
                  }   
               }
               if((found+otherFound)==titleTokenArr.length )
               {
                  coOccurenceHashMap.put(originalTitle, lowest);
              //    titleArrayList.add(originalTitle);
           //       System.out.println(key + " is recognized as " + originalTitle);
               //   threadResultPairHashMap.put(key, originalTitle);
               //   return;
               }
               else 
               {
                  if(found!=0)
                  {
                     int checkOtherFound = 0;
                     int checFound = 0;
                     
                     for (int j = 0; j < titleTokenArr.length; j++)
                     {
                        String titleToken = titleTokenArr[j];
                        if(!titleToken.equalsIgnoreCase(key) && !wordUtil.isSimplifiedStopWord(titleToken))
                        {
                           if(analysisData.getCheckWordsOccurrenceHashMap().containsKey(titleToken))
                           {
                              checFound++;
                           }
                        }
                        else
                        {
                           checkOtherFound++;
                        }   
                     }
                     if((found+otherFound)==titleTokenArr.length )
                     {
                        titleArrayList.add(originalTitle); 
                     }
                  }  
               }    
            }
         }
         if(!coOccurenceHashMap.isEmpty())
         {
            LinkedHashMap<String, Integer>  resLinkedHashMap= hMapUtil.sortEmotionHashMapByValue(coOccurenceHashMap);
            Iterator<String> resLinkedHashMapIterator = resLinkedHashMap.keySet().iterator();
            ArrayList<String> tempTitleArrayList = new ArrayList<String>();
            while(resLinkedHashMapIterator.hasNext()) 
            {
               String keyo = resLinkedHashMapIterator.next(); 
               Integer val = resLinkedHashMap.get(keyo);
               if(val>((float)originalOccurence*0.1) && wordUtil.checkContains(keyo, key))
               {
                  String keyoArr[] = keyo.split(" ");
                  if(keyoArr.length==1)
                  {
                     if(sameFound)
                     {
                        threadResultPairHashMap.put(key, sameTitle);
                        return;
                     }
                  }
                  
                  if(resLinkedHashMapIterator.hasNext())
                  {
                     String keyo2 = resLinkedHashMapIterator.next(); 
                     Integer val2 = resLinkedHashMap.get(keyo2);
                     String keyo2Arr[] = keyo2.split(" ");
                     if(val == val2 && keyoArr.length < keyo2Arr.length)
                     {
                        threadResultPairHashMap.put(key, keyo2);
                        return;
                     }
                  }
                  
                  threadResultPairHashMap.put(key, keyo);
                  return;
               }
               tempTitleArrayList.add(keyo);
            }
            if(!titleArrayList.isEmpty())
            {
               for (Iterator<String> iterator = tempTitleArrayList.iterator(); iterator.hasNext();)
               {
                  titleArrayList.add((String) iterator.next());  
               }
            }
         }
         if(titleArrayList.size()==1)
         {
            threadResultPairHashMap.put(key, titleArrayList.get(0));
            return;
         }
         else if(sameFound)
         {
            threadResultPairHashMap.put(key, sameTitle);
            return;
         }
         
         processWikiData(key,titleArrayList); 
      }
      hMapUtil.sortEmotionHashMapByValue(nWordOccHashMap);
   }
   
   private void processWikiData(String key, ArrayList<String> titleArrayList)
   {
      int numofOCC = 0;
      float rate = 0;
      int resultSize = 0;
      
      for (Iterator<String> iterator = titleArrayList.iterator(); iterator.hasNext();)
      {
         String title = (String) iterator.next();
         numofOCC = checkDBPediaValues(title);
         if(numofOCC >0)
         {
            resultSize = resultHashMap.keySet().size();
            if(resultSize!=0)
            {
               rate = numofOCC/resultSize;
            }
            else
            {
               rate =0;
            }   
            if(resultMap.containsKey(key))
            {
               if(numofOCC>resultMap.get(key).getNumberOfOccurence())
               {
                  EmoTweetResultData emoTweeResData = new EmoTweetResultData();
                  emoTweeResData.setTitle(title);
                  emoTweeResData.setNumberOfOccurence(numofOCC);
                  emoTweeResData.setRatio(rate);
                  emoTweeResData.setDistinc(resultSize);
                  resultMap.put(key, emoTweeResData);
       //           System.out.println("        "+mwTitle.getTitle() + " is re-put with " + numofOCC + " distinc words # : " +resultSize);
               }
            }
            else
            {
               EmoTweetResultData emoTweeResData = new EmoTweetResultData();
               emoTweeResData.setTitle(title);
               emoTweeResData.setNumberOfOccurence(numofOCC);
               emoTweeResData.setRatio(rate);
               emoTweeResData.setDistinc(resultSize);
               resultMap.put(key, emoTweeResData);
         //      System.out.println("        "+mwTitle.getTitle() + " is put with " + numofOCC + " distinc words # : " +resultSize);
            }
         }
         resultHashMap = new HashMap<String, Integer>();
      }
      if(resultMap.containsKey(key))
      {
         if(resultMap.get(key).getDistinc()>2)
         {
            String found=resultMap.get(key).getTitle();
            threadResultPairHashMap.put(key,found);
      //      System.out.println(key + " is recognized as " + found);
         }
      } 
   }

   private String[] getWikiTitleList(String key)
   {
      String[] titleArr = null;
      mediaWikiApi = mediaWikiImpl.getWikiPediaTitleInformation(key);
      
      if(mediaWikiApi != null)
      {
         if(mediaWikiApi.getMwQuery().getMwSearch().getMwTitles()!=null)
         {
            titleArr = new String[mediaWikiApi.getMwQuery().getMwSearch().getMwTitles().size()];
            for (int i = 0; i < titleArr.length; i++)
            {
               titleArr[i]=mediaWikiApi.getMwQuery().getMwSearch().getMwTitles().get(i).getTitle();
            }
         }
      }
      return titleArr;
   }

   public void analyzeWikibyDate()
   {
      Iterator<String> HashmapIterator = dateHashMap.keySet().iterator();
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next(); 
         checkHashMapByWord  = dateCheckWordsOccurenceHashMap.get(key);
         searchHashMapByWord = dateSearchWordsOccurenceHashMap.get(key);
         prepareWords(key);
         dateResultPairHashMap.put(key, resultPairHashMap);
         resultPairHashMap = new LinkedHashMap<String, String>();
      }
   }
   

   private void prepareWords(String hashMapkey)
   {
      int count = 20;
      int num=0;
      ArrayList<String> wordList = new ArrayList<String>();
      Thread threadList[] = new Thread[count];
      Iterator<String> HashmapIterator = hMapUtil.sortEmotionHashMapByValue(searchHashMapByWord).keySet().iterator();
      while(HashmapIterator.hasNext()) 
      {
         final String key = HashmapIterator.next();
         if(!wordUtil.isTimeWord(key) && !key.startsWith("http") && !key.startsWith("www") && !key.contains("dotcom"))
         {
            wordList.add(key);
            System.out.println(key + " is inserted");
            threadList[num] = new Thread(new Runnable()
            {
               public void run()
               {
                  searchByTitle(key);    
               }
            });
            if(num==count-1)
            {
               break;
            }
            num++;
         }
      }
      for (int j = 0; j < threadList.length; j++)
      {
         Thread thread = threadList[j];
         thread.start();
         try
         {
            synchronized (this) {
               this.wait(500);
           }
         }
         catch (InterruptedException e)
         {
            e.printStackTrace();
         }
      }
      
      for (int j = 0; j < threadList.length; j++)
      {
         Thread thread = threadList[j];
         try
         {
            thread.join();
         }
         catch (InterruptedException e)
         {
            e.printStackTrace();
         }
      }
  //    prepareResPairHmap(wordList);
   }   

   private void prepareResPairHmap(ArrayList<String> wordList)
   {
      String key;
      for (Iterator<String> iterator = wordList.iterator(); iterator.hasNext();)
      {
         key = (String) iterator.next();
         if(threadResultPairHashMap.containsKey(key))
         {
            analysisData.getResultPairHashMap().put(key, threadResultPairHashMap.get(key));
            System.out.println(key + " is recognized as " + threadResultPairHashMap.get(key));
         }
      }
   }

   public void searchByTitle(String key)
   {
      mediaWikiApi = mediaWikiImpl.getWikiPediaTitleInformation(key);
      
      if(mediaWikiApi != null)
      {
         int numofOCC = 0;
         float rate = 0;
         int resultSize = 0;
         ArrayList<MWTitle> itemArrList = (ArrayList<MWTitle>) mediaWikiApi.getMwQuery().getMwSearch().getMwTitles();
         if(itemArrList!= null)
         {
            for (Iterator<MWTitle> iterator = itemArrList.iterator(); iterator.hasNext();)
            {
               MWTitle mwTitle = (MWTitle) iterator.next();
               if((wordUtil.covertToPlain(mwTitle.getTitle().toLowerCase())).contains(key.toLowerCase()))
               {
                  numofOCC = checkDBPediaValues(mwTitle.getTitle());
                  if(numofOCC >0)
                  {
                     resultSize = resultHashMap.keySet().size();
                     if(resultSize!=0)
                     {
                        rate = numofOCC/resultSize;
                     }
                     else
                     {
                        rate =0;
                     }   
                     
                     if(resultMap.containsKey(key))
                     {
                        if(numofOCC>resultMap.get(key).getNumberOfOccurence())
                        {
                           EmoTweetResultData emoTweeResData = new EmoTweetResultData();
                           emoTweeResData.setMwTitle(mwTitle);
                           emoTweeResData.setNumberOfOccurence(numofOCC);
                           emoTweeResData.setRatio(rate);
                           emoTweeResData.setDistinc(resultSize);
                           resultMap.put(key, emoTweeResData);
                //           System.out.println("        "+mwTitle.getTitle() + " is re-put with " + numofOCC + " distinc words # : " +resultSize);
                        }
                     }
                     else
                     {
                        EmoTweetResultData emoTweeResData = new EmoTweetResultData();
                        emoTweeResData.setMwTitle(mwTitle);
                        emoTweeResData.setNumberOfOccurence(numofOCC);
                        emoTweeResData.setRatio(rate);
                        emoTweeResData.setDistinc(resultSize);
                        resultMap.put(key, emoTweeResData);
                  //      System.out.println("        "+mwTitle.getTitle() + " is put with " + numofOCC + " distinc words # : " +resultSize);
                     }
                  }
                  resultHashMap = new HashMap<String, Integer>();
               }
               
            }
         }
         if(resultMap.containsKey(key))
         {
            if(resultMap.get(key).getDistinc()>2)
            {
               String found=resultMap.get(key).getMwTitle().getTitle();
          //     updateFoundWordsMap(found);
               threadResultPairHashMap.put(key,found );
          //     System.out.println(key + " is recognized as " + resultMap.get(key).getMwTitle().getTitle());
            }
         }       
      }  
      
   }
   
   private void updateFoundWordsMap(String found)
   {
      String meanWord[] = found.split(" ");
      if(meanWord.length>0)
      {
         for (int i = 1; i < meanWord.length; i++)
         {
            foundWords.put(meanWord[i], meanWord[i]);
         }
      }
   }

   public MediaWikiData getSingleValueInformation(String value)
   {
      MediaWikiData mediaWikiData = mediaWikiImpl.getSingleWikiInformation(value);
      return mediaWikiData;
      
   }
   
   private int checkDBPediaValues(String key)
   {
      String meanWord[] = key.split(" ");
      if(meanWord.length>0)
      {
         StringBuffer strBuffer = new StringBuffer();
         strBuffer.append(meanWord[0]);
         for (int i = 1; i < meanWord.length; i++)
         {
            strBuffer.append("_" + meanWord[i]);
         }
         key = strBuffer.toString();
      }
      key = key.replaceAll("\\(", "%28");
      key = key.replaceAll("\\)", "%29");
      key = key.replaceAll("\"", "%22");
      key = key.replaceAll("`", "");
      ArrayList<DBPediaData> dbPediaDataArrList = dbpediaImpl.getDBPediaContent(key);
      return checkAllValues(key, dbPediaDataArrList);
   }
   
   private int checkAllValues(String word, ArrayList<DBPediaData> dbPediaDataArrList)
   {
      Iterator<String> HashmapIterator = analysisData.getCheckWordsOccurrenceHashMap().keySet().iterator();
      int numOfOccurence=0;
      int occurence=0;
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next(); 
         if(!key.equalsIgnoreCase(word))
         {
            occurence = checkValues(dbPediaDataArrList, key);
            if(occurence!=0)
            {
               resultHashMap.put(key, occurence);
            }   
            numOfOccurence=numOfOccurence+occurence;
         }
      }
      return numOfOccurence;
   }

   public int checkValues(ArrayList<DBPediaData> dbPediaDataArrList, String word)
   {
      int numofOcc=0;
      if(word.length()>3 && dbPediaDataArrList!=null)
      {
         for (Iterator<DBPediaData> iterator = dbPediaDataArrList.iterator(); iterator.hasNext();)
         {
            DBPediaData dbPediaData = (DBPediaData) iterator.next();
            if (dbPediaData.getHasValue() != null && dbPediaData.getHasValueWordHashMap().containsKey(word.toLowerCase()))
            {
     //          System.out.println("  ----"+word+"----");
               numofOcc++;
            }
            if (dbPediaData.getIsValueOf() != null && dbPediaData.getIsValueOWordfHashMap().containsKey(word.toLowerCase()))
            {
       //        System.out.println("  ----"+word+"----");
               numofOcc++;
            }
         }
      }
      return numofOcc;
   }
}
