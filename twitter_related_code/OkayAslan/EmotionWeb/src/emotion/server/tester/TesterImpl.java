package emotion.server.tester;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import emotion.server.dbPedia.DBPediaData;
import emotion.server.dbPedia.DBPediaImpl;
import emotion.server.mediaWiki.Item;
import emotion.server.mediaWiki.MWApi;
import emotion.server.mediaWiki.MWTitle;
import emotion.server.mediaWiki.MediaWikiData;
import emotion.server.mediaWiki.MediaWikiImpl;
import emotion.server.twitter.TwitterDBImpl;
import emotion.server.util.HashMapUtil;
import emotion.server.util.TweetUtil;
import emotion.server.wordNet.WordNetDBImpl;
import emotion.shared.EmoDO;
import emotion.shared.EmoTweet;
import emotion.shared.EmoTweetResultData;

public class TesterImpl
{
   private static DBPediaImpl dbpediaImpl = new DBPediaImpl();
   private static TwitterDBImpl twitterDBImpl = TwitterDBImpl.getInstance(); 
   private static TweetUtil tweetUtil = new TweetUtil();
   private static HashMapUtil hMapUtil = new HashMapUtil();
   private static MediaWikiImpl mediaWikiImpl = new MediaWikiImpl();
   private static HashMap<String,EmoTweetResultData> resultMap = new HashMap<String,EmoTweetResultData>();
   private static HashMap<String, Integer> sortedEmotionHashMapByWord = new HashMap<String, Integer>();
   private static WordNetDBImpl wordNetdbImpl = WordNetDBImpl.getInstance();
   private static MWApi mediaWikiApi = null;

   private static String covertToPlain(String key)
   {
      key = key.replace('y', 'y');
      key = key.replaceAll("ù|ú|û|ü", "u");
      key = key.replaceAll("ò|ó|ô|õ|ö", "o");
      key = key.replaceAll("ì|í|î|ï|ý", "i");
      key = key.replaceAll("è|é|ê|ë", "e");
      key = key.replace('ç', 'c');
      key = key.replaceAll("à|á|â|ã|ä|å|æ", "a");
      return key;
   }   
   
   public void searchByTitle(String key)
   {
      mediaWikiApi = mediaWikiImpl.getWikiPediaTitleInformation(key);
      
      if(mediaWikiApi != null)
      {
         int numofOCC = 0;
         ArrayList<MWTitle> itemArrList = (ArrayList<MWTitle>) mediaWikiApi.getMwQuery().getMwSearch().getMwTitles();
         if(itemArrList!= null)
         {
            for (Iterator<MWTitle> iterator = itemArrList.iterator(); iterator.hasNext();)
            {
               MWTitle mwTitle = (MWTitle) iterator.next();
               if((covertToPlain(mwTitle.getTitle().toLowerCase())).contains(key.toLowerCase()))
               {
                  numofOCC = checkDBPediaValues(mwTitle.getTitle());
                  if(numofOCC >0)
                  {
                     if(resultMap.containsKey(key))
                     {
                        if(numofOCC>resultMap.get(key).getNumberOfOccurence())
                        {
                           EmoTweetResultData emoTweeResData = new EmoTweetResultData();
                           emoTweeResData.setMwTitle(mwTitle);
                           emoTweeResData.setNumberOfOccurence(numofOCC);
                           resultMap.put(key, emoTweeResData);
                      //     System.out.println("        "+mwTitle.getTitle() + " is re-put with " + numofOCC);
                        }
                     }
                     else
                     {
                        EmoTweetResultData emoTweeResData = new EmoTweetResultData();
                        emoTweeResData.setMwTitle(mwTitle);
                        emoTweeResData.setNumberOfOccurence(numofOCC);
                        resultMap.put(key, emoTweeResData);
                  //      System.out.println("        "+mwTitle.getTitle() + " is put with " + numofOCC);
                     }
                  }
               }
            }
         }
         if(resultMap.containsKey(key))
         {
            System.out.println(key + " is recognized as " + resultMap.get(key).getMwTitle().getTitle());
         }       
      }
   }
   
   
   public void startWithSuggestedTitle()
   {
      EmoDO emoDo = getEmoDO();
      sortedEmotionHashMapByWord = emoDo.getSortedEmotionHashMapByWord();
      Iterator<String> HashmapIterator = sortedEmotionHashMapByWord.keySet().iterator();
      
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next(); 
         searchByTitle( key);
      }
      System.out.println("finish");
   }
   
   public void StartWithSearchWord()
   {
      EmoDO emoDo = getEmoDO();
      sortedEmotionHashMapByWord = emoDo.getSortedEmotionHashMapByWord();
      Iterator<String> HashmapIterator = sortedEmotionHashMapByWord.keySet().iterator();
      MediaWikiData mediaWikiData = null;
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next(); 
         mediaWikiData = mediaWikiImpl.getWikiInformation(key);
         
         if(mediaWikiData != null)
         {
            int numofOCC = 0;
            ArrayList<Item> itemArrList = (ArrayList<Item>) mediaWikiData.getSection().getItems();
            if(itemArrList!= null)
            {
               for (Iterator<Item> iterator = itemArrList.iterator(); iterator.hasNext();)
               {
                  Item item = (Item) iterator.next();
                  numofOCC = checkDBPediaValues(item.getText());
                  if(numofOCC >0)
                  {
                     if(resultMap.containsKey(key))
                     {
                        if(numofOCC>resultMap.get(key).getNumberOfOccurence())
                        {
                           EmoTweetResultData emoTweeResData = new EmoTweetResultData();
                           emoTweeResData.setItem(item);
                           emoTweeResData.setNumberOfOccurence(numofOCC);
                           resultMap.put(key, emoTweeResData);
                        }
                     }
                     else
                     {
                        EmoTweetResultData emoTweeResData = new EmoTweetResultData();
                        emoTweeResData.setItem(item);
                        emoTweeResData.setNumberOfOccurence(numofOCC);
                        resultMap.put(key, emoTweeResData);
                     }
                  }
               }
            }
            if(resultMap.containsKey(key))
            {
               System.out.println(key + " is recognized as " + resultMap.get(key).getItem().getText() + " at " + resultMap.get(key).getItem().getWikiUrl() );
            }       
         }
      }
      System.out.println("finish");
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
      ArrayList<DBPediaData> dbPediaDataArrList = dbpediaImpl.getDBPediaContent(key);
      return checkAllValues(key, dbPediaDataArrList);
   }
   
   private int checkAllValues(String word, ArrayList<DBPediaData> dbPediaDataArrList)
   {
      Iterator<String> HashmapIterator = sortedEmotionHashMapByWord.keySet().iterator();
      int numOfOccurence=0;
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next(); 
         if(!key.equalsIgnoreCase(word))
         {
            numOfOccurence=numOfOccurence+checkValues(dbPediaDataArrList, key);
         }
      }
      return numOfOccurence;
   }

   public int checkValues(ArrayList<DBPediaData> dbPediaDataArrList, String word)
   {
      int numofOcc=0;
      if(word.length()>3)
      {
         for (Iterator<DBPediaData> iterator = dbPediaDataArrList.iterator(); iterator.hasNext();)
         {
            DBPediaData dbPediaData = (DBPediaData) iterator.next();
            if (dbPediaData.getHasValue() != null && dbPediaData.getHasValueWordHashMap().containsKey(word.toLowerCase()))
            {
               numofOcc++;
         //      System.out.println(dbPediaData.getProperty() + " has value " + dbPediaData.getHasValue());
            }
            if (dbPediaData.getIsValueOf() != null && dbPediaData.getIsValueOWordfHashMap().containsKey(word.toLowerCase()))
            {
               numofOcc++;
       //        System.out.println(dbPediaData.getProperty() + " is value of  " + dbPediaData.getIsValueOf());
            }
         }
      }
      return numofOcc;
   }   
   

   /**
    * @return
    */
   private EmoDO getEmoDO()
   {
      ArrayList<EmoTweet> allBarcelonaTweets = twitterDBImpl.getAllTweetsFromTables("tw_barcelona_tb");
      ArrayList<EmoTweet> day30BarcelonaTweets = new ArrayList<EmoTweet>();
      for (Iterator<EmoTweet> iterator = allBarcelonaTweets.iterator(); iterator.hasNext();)
      {
         EmoTweet emoTweet = (EmoTweet) iterator.next();
         Calendar cal=Calendar.getInstance();
         cal.setTime(emoTweet.getTweetDate());
         if(cal.get(Calendar.DAY_OF_MONTH)==30)
         {
            day30BarcelonaTweets.add(emoTweet);
         }   
      }
      EmoDO emoDo = new EmoDO();
      emoDo.setEmoTweets(day30BarcelonaTweets);
      emoDo.setSortedEmotionHashMapByWord(hMapUtil.sortEmotionHashMapByValue(tweetUtil.fillAndGetEmotionHashMap(day30BarcelonaTweets)));
      return emoDo;
   }

   public void askDBPedia(HashMap<String, Integer> sortedEmotionHashMapByWord)
   {
      Iterator<String> HashmapIterator = sortedEmotionHashMapByWord.keySet().iterator();
      ArrayList<DBPediaData> dbPediaDataArrList = dbpediaImpl.searchDBPedia();
      while(HashmapIterator.hasNext()) 
      {
         Object key = HashmapIterator.next(); 
         Object val = sortedEmotionHashMapByWord.get(key);
         System.out.println(key+" ; "+ val);
         dbpediaImpl.checkValue(dbPediaDataArrList, (String)key);
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
      TesterImpl.sortedEmotionHashMapByWord = sortedEmotionHashMapByWord;
   }

   /**
    * @return the resultMap
    */
   public static HashMap<String, EmoTweetResultData> getResultMap()
   {
      return resultMap;
   }

   /**
    * @param resultMap the resultMap to set
    */
   public static void setResultMap(HashMap<String, EmoTweetResultData> resultMap)
   {
      TesterImpl.resultMap = resultMap;
   }   
   
}
