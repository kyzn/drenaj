package emotion.server.tester;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;

import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import emotion.server.affectiveText.AffectiveData;
import emotion.server.affectiveText.AffectiveFileParser;
import emotion.server.analyzer.Analyzer;
import emotion.server.dbPedia.DBPediaImpl;
import emotion.server.libstemmer.snowball.ext.englishStemmer;
import emotion.server.mediaWiki.MediaWikiImpl;
import emotion.server.twitter.TwitterDBImpl;
import emotion.server.twitter.TwitterImpl;
import emotion.server.util.HashMapUtil;
import emotion.server.util.TweetUtil;
import emotion.server.util.WordUtil;
import emotion.server.wordNet.WordNetDBImpl;
import emotion.server.wordNet.WordNetData;
import emotion.shared.EmoTweet;
import emotion.shared.EmoTweetResultData;
import emotion.shared.EmoTweetWordData;

public class TesterAffect
{

   private static DBPediaImpl dbpediaImpl = new DBPediaImpl();
   private static TwitterDBImpl twitterDBImpl = TwitterDBImpl.getInstance(); 
   private static TweetUtil tweetUtil = new TweetUtil();
   private static HashMapUtil hMapUtil = new HashMapUtil();
   private static MediaWikiImpl mediaWikiImpl = new MediaWikiImpl();
   private static HashMap<String,EmoTweetResultData> resultMap = new HashMap<String,EmoTweetResultData>();
   private static HashMap<String, Integer> sortedEmotionHashMapByWord = new HashMap<String, Integer>();
   private static WordNetDBImpl wordNetdbImpl = WordNetDBImpl.getInstance();
   private static TesterImpl testerImpl = new TesterImpl();
   private static WordUtil wordUtil = new WordUtil();
   private static HashMap<String,EmoTweetWordData> wordDataMap = new HashMap<String,EmoTweetWordData>();
   private static HashMap<String, Integer> emotionHashMapByWord = new HashMap<String, Integer>();
   private static englishStemmer stemmer = new englishStemmer();
   private static TesterHelper testerHelper = new TesterHelper();
   private static TwitterImpl twitterImpl = TwitterImpl.getInstance();
   private static Twitter twitter = null;
   private static HashMap<String,WordNetData> wordNetMap = new HashMap<String,WordNetData>();
   private static Analyzer analyzer = new Analyzer();
   private static AffectiveFileParser affFileParser = new AffectiveFileParser();
   private static int human =1;

   private static void analyzeTweets(ArrayList<EmoTweet> tweetList)
   {
      boolean firstConceptFound = false;
      boolean verbFound = false;
      int count = 1;
      int totalTweet = 1;

      for (Iterator<EmoTweet> iterator = tweetList.iterator(); iterator.hasNext();)
      {
         EmoTweet emoTweet = (EmoTweet) iterator.next();
         
         System.out.println(totalTweet +" *** " +emoTweet.getTweetText());
         totalTweet++;
         firstConceptFound = false;
         verbFound = false;
         
         StringTokenizer st = new StringTokenizer(emoTweet.getTweetText());
         while (st.hasMoreTokens()) 
         {
            String currentWord = st.nextToken().toLowerCase(Locale.ENGLISH);
            currentWord = wordUtil.clearPunctuation(currentWord);
            if(currentWord!=null)
            { 
               if(!wordUtil.isSimplifiedStopWord(currentWord))
               {
                  WordNetData wNData = getWordNetData(currentWord);               
                  if(wNData!=null)
                  {
                     if(wNData.isVerb() && firstConceptFound && !verbFound)
                     {
                        verbFound = true;
                     }
                     else if(wNData.isNoun())
                     {
                        if(firstConceptFound)
                        {
                           if(verbFound)
                           {
                              System.out.println("                       "+count +" : http://twitter.com/"+emoTweet.getUserName()+"/");
                              analyzeUser(emoTweet);
                              count++;
                              break;
                           }   
                        }
                        else
                        {
                     //      System.out.println("XXXX");
                           firstConceptFound = true; 
                        }   
                     }
                  } 
                  else
                  {
                     if(firstConceptFound)
                     {
                        if(verbFound)
                        {
                           System.out.println("                       "+count +" : http://twitter.com/"+emoTweet.getUserName()+"/");
                           analyzeUser(emoTweet);
                           count++;
                           break;
                        }   
                     }
                     else
                     {
                        firstConceptFound = true; 
                     }  
                  }     
               }  
            }
         }
      }
   }


   private static void analyzeUser(EmoTweet emoTweet)
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
         pagUserList = twitter.getFollowersStatuses(emoTweet.getUserID());
         if(pagUserList!=null && !pagUserList.isEmpty())
         {
            user = pagUserList.get(0);

            createMS = user.getCreatedAt().getTime();
            intervalMS = currentMS - createMS;
            long diffDays = intervalMS / (24 * 60 * 60 * 1000);
            int day =(int) (user.getStatusesCount()/diffDays);
            if(day >80)
            {
               System.out.println("                               automated with UF : "+day);
            }
            else
            {
               System.out.println("                               human "+human+" with UF : "+ day);
               human++;
            } 
         }
         else
         {
            
         }   
      }
      catch (TwitterException e)
      {
         System.out.println("                               Access denied");
      }
   }


   private static void getTweets(String word)
   {
      int count = 1;
      ArrayList<EmoTweet> tweetList = null;
      try
      {
         tweetList = tweetUtil.convertTweetToEmotweet(twitterImpl.searchWord(word));
         
         for (Iterator<EmoTweet> iterator = tweetList.iterator(); iterator.hasNext();)
         {
            EmoTweet emoTweet = (EmoTweet) iterator.next();
            System.out.println(count+" "+emoTweet.getTweetText());
            count++;
         }
         
      }
      catch (TwitterException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   
   private static void startSingle(String string)
   {
      boolean firstConceptFound = false;
      boolean verbFound = false;
      boolean secondConceptFound = false;
      int count = 1;
      
      ArrayList<AffectiveData> affectiveDataArrList = affFileParser.readStatements();
      for (Iterator<AffectiveData> iterator = affectiveDataArrList.iterator(); iterator.hasNext();)
      {
         AffectiveData affectiveData = (AffectiveData) iterator.next();
         System.out.println("*** " +affectiveData.getStatement());
         
         firstConceptFound = false;
         verbFound = false;
         secondConceptFound = false;
         
         StringTokenizer st = new StringTokenizer(affectiveData.getStatement());
         while (st.hasMoreTokens()) 
         {
            String currentWord = st.nextToken().toLowerCase(Locale.ENGLISH);
            currentWord = wordUtil.clearPunctuation(currentWord);
            if(currentWord!=null)
            { 
               if(!wordUtil.isSimplifiedStopWord(currentWord))
               {
                  WordNetData wNData = getWordNetData(currentWord);               
                  if(wNData!=null)
                  {
                     if(wNData.isVerb() && firstConceptFound && !verbFound)
                     {
                        verbFound = true;
                     }
                     else if(wNData.isNoun())
                     {
                        if(firstConceptFound)
                        {
                           if(verbFound)
                           {
                              System.out.println(count +" :  "+ affectiveData.getStatement());
                              count++;
                              break;
                           }   
                        }
                        else
                        {
                           System.out.println("XXXX");
                           firstConceptFound = true; 
                        }   
                     }
                  } 
                  else
                  {
                     if(firstConceptFound)
                     {
                        if(verbFound)
                        {
                           System.out.println(count +" : PR - "+ affectiveData.getStatement());
                           count++;
                           break;
                        }   
                     }
                     else
                     {
                        firstConceptFound = true; 
                     }  
                  }     
               }  
            }
         }
      }
   }

   public static void startAffective()
   {
      boolean firstConceptFound = false;
      boolean verbFound = false;
      boolean secondConceptFound = false;
      int count = 1;
      int ProperCount = 1;
      int allCount = 1;
      boolean ProperNounFound = false;
      
      ArrayList<AffectiveData> affectiveDataArrList = affFileParser.readStatements();
      for (Iterator<AffectiveData> iterator = affectiveDataArrList.iterator(); iterator.hasNext();)
      {
         AffectiveData affectiveData = (AffectiveData) iterator.next();
         System.out.println(allCount +affectiveData.getStatement());
         allCount++;
         firstConceptFound = false;
         verbFound = false;
         secondConceptFound = false;
         ProperNounFound = false;
         
         StringTokenizer st = new StringTokenizer(affectiveData.getStatement());
         while (st.hasMoreTokens()) 
         {
            String currentWord = st.nextToken().toLowerCase(Locale.ENGLISH);
            currentWord = wordUtil.clearPunctuation(currentWord);
            if(currentWord!=null)
            { 
               if(!wordUtil.isSimplifiedStopWord(currentWord))
               {
                  WordNetData wNData = getWordNetData(currentWord);               
                  if(wNData!=null)
                  {
                     if(wNData.isVerb() && firstConceptFound && !verbFound)
                     {
                        verbFound = true;
                     }
                     else if(wNData.isNoun())
                     {
                        if(firstConceptFound)
                        {
                           if(verbFound)
                           {
                              if(ProperNounFound)
                              {
                                 System.out.println("      "+count +" : PR - "+ProperCount+" "+affectiveData.getStatement());
                                 ProperCount++;
                              }                              
                              else
                              {
                                 System.out.println("      "+count +" :  "+ affectiveData.getStatement());
                              }
                              
                              count++;
                              break;
                           }   
                        }
                        else
                        {
                           firstConceptFound = true; 
                        }   
                     }
                  } 
                  else
                  {
                     if(firstConceptFound)
                     {
                        if(verbFound)
                        {
                           System.out.println("      "+count +" : PR - "+ProperCount+" "+affectiveData.getStatement());
                           count++;
                           ProperCount++;
                           break;
                        }   
                     }
                     else
                     {
                        ProperNounFound=true;
                   //     System.out.println("XXXX");
                        firstConceptFound = true; 
                     }  
                  }     
               }  
            }
         }
      }
   }

   private static WordNetData getWordNetData(String currentWord)
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
            if(wordNetMap.containsKey(currentWord))
            {
               return wordNetMap.get(currentWord);
            }
            else
            {
               wNData = wordNetdbImpl.getWordProperties(stemmedWord);
               if(wNData!=null)
               {
                  wordNetMap.put(currentWord, wNData);
                  return wNData;
               }
            }   
         }
         else
         {
            wordNetMap.put(currentWord, wNData);
            return wNData;
         }   
      }
      return null;
   }   
   
   
   private static void fire()
   {
      ArrayList<EmoTweet> allBarcelonaTweets = twitterDBImpl.getAllTweetsFromTables("tw_barcelona_tb");
      ArrayList<EmoTweet> day30BarcelonaTweets = new ArrayList<EmoTweet>();
      for (Iterator<EmoTweet> iterator = allBarcelonaTweets.iterator(); iterator.hasNext();)
      {
         EmoTweet emoTweet = (EmoTweet) iterator.next();
         Calendar cal=Calendar.getInstance(Locale.US);
         cal.setTime(emoTweet.getTweetDate());
         if(cal.get(Calendar.DAY_OF_MONTH)==30)
         {
            day30BarcelonaTweets.add(emoTweet);
         }   
      }
      analyzer.analyze("barcelona",day30BarcelonaTweets,true);
   }   
   

   private static void start()
   {
      testerHelper.proo();
      testerImpl.setSortedEmotionHashMapByWord(testerHelper.getSortedEmotionHashMapByWord());
      sortedEmotionHashMapByWord=testerHelper.getSortedEmotionHashMapByWord();
      wordDataMap = testerHelper.getWordDataMap();
      wordNetMap = testerHelper.getWordNetMap();
      go();
      resultMap=testerImpl.getResultMap();
   }

   private static void go()
   {
      Iterator<String> HashmapIterator = sortedEmotionHashMapByWord.keySet().iterator();
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next();
         String plForm = getPlainWord(key);
         if(wordDataMap.containsKey(key))
         {
            testerImpl.searchByTitle(key);
         //   testerHelper.printEmoTweet(wordDataMap.get(key));
         }       
      }
   }
   
   private static String getPlainWord(String incomingWord)
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
         return wNData.getPlainForm();
      }
      else
      {
         return null;
      }   
      
   }
}
