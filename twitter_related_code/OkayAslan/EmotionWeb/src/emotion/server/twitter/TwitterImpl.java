package emotion.server.twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import emotion.server.analyzer.Analyzer;
import emotion.shared.EmoAnalysisDO;
import emotion.shared.EmoResultDO;
import emotion.shared.EmoTweet;

public class TwitterImpl
{
   private Twitter twitter = null;
   private static TwitterImpl instance = new TwitterImpl();
   private TwitterDBImpl twitterDBImpl = TwitterDBImpl.getInstance();
   private HashMap<String,String> tableHashMapByWord = new HashMap<String,String>();
   private HashMap<String,String> userTableHashMapByWord = new HashMap<String,String>();
   private HashMap<String,ArrayList<Long>> tweetIDsHashMapByWord = new HashMap<String,ArrayList<Long>>();
   private HashMap<String,List<Tweet>> tweetsHashMapByWord = new HashMap<String,List<Tweet>>();
   private Analyzer analyzer= new Analyzer();
   private int pageNum = 15;
   private int userPageNum = 32;
   
   private String consumerKey = "A6HUs49F040B86MtsewYig";
   private String consumerSecret= "n3O5gMfEjFVZPMoecpFxEn9t93gvRcZtzg8iKfI";
   
   private String twitterToken =  "82467122-QJhk1EHAV0zSuAxXHuRmxRjvwKvAfmJXwNIpZVE";
   private String twitterTokenSecret = "mIrIJ2Tjl4lLoQjv2G2G2gcO0kgf5WRyyDQLPKXwg";
   
   @SuppressWarnings("deprecation")
   private TwitterImpl()
   {
   
      twitter = new TwitterFactory().getInstance();
      twitter.setOAuthConsumer(consumerKey, consumerSecret);
      AccessToken accessToken = new AccessToken(twitterToken, twitterTokenSecret);
      twitter.setOAuthAccessToken(accessToken);      
      
      twitterDBImpl.updateTableHashMap(tableHashMapByWord);
      twitterDBImpl.updateUserTableHashMap(userTableHashMapByWord);
   }
   
   public static TwitterImpl getInstance()
   {
      return instance;
   }

   public List<?> getPublicTimeline() throws TwitterException
   {
      return twitter.getPublicTimeline();
   }
   

   public  List<Tweet> searchWord(String query) throws TwitterException
   {
      List<Tweet> alllistTweets = new ArrayList<Tweet>();
      Query tweetQuery = new Query(query);
      tweetQuery.setLang("en");
      tweetQuery.setPage(pageNum);
      tweetQuery.setRpp(100);
      QueryResult qres;
      List<Tweet> listTweets;
      for (int i = 0; i < pageNum; i++) 
      {
         tweetQuery.setPage(pageNum - i);
         listTweets = twitter.search(tweetQuery).getTweets();
         for (Iterator<Tweet> iterator = listTweets.iterator(); iterator.hasNext();) 
         {
            alllistTweets.add((Tweet) iterator.next());
         }
      }
      
/*    
      Query tweetQuery = new Query(query);
      tweetQuery.setLang("en"); 
      tweetQuery.setPage(1);
      tweetQuery.setRpp(100);
   //   tweetQuery.setSinceId(12165371449L);
      QueryResult qres = twitter.search(tweetQuery);
      return qres.getTweets();
 */
      return alllistTweets;
   }   

   
   public  ArrayList<Status> getUserTweets(String userName) throws TwitterException
   {
      ArrayList<Status> allStatusList = new ArrayList<Status>();
      
      
      ResponseList<Status> userStatuses = null;
      Paging paging = new Paging(userPageNum, 100);
      for (int pageCount = 1; pageCount <= userPageNum; pageCount++) 
      {
         System.out.println("getting page : " +pageCount);   
         paging.setPage(pageCount);
         try
         {
            userStatuses = twitter.getUserTimeline(userName, paging);
         }
         catch (TwitterException e1)
         {
            synchronized (this) 
            {
               try
               {
                  System.out.println("waiting for 10 minutes");
                  System.out.println(e1.getLocalizedMessage());
                  this.wait(600000);
               }
               catch (InterruptedException e)
               {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
           }
            userStatuses = twitter.getUserTimeline(userName, paging);
         }
         if(userStatuses==null || userStatuses.isEmpty())
         {
            break;
         }
         for (Status status : userStatuses)
         {
            allStatusList.add(status);
         }
         
         synchronized (this) 
         {
            try
            {
               this.wait(10000);
            }
            catch (InterruptedException e)
            {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
        }
      }
     
      return allStatusList;
   }   
   
   public void insertTweetstoDB(List<Tweet> tweets, String searchedWord)
   {
      String tableName=createTableName(searchedWord);
      if(!tableHashMapByWord.containsKey(searchedWord) )
      {
         twitterDBImpl.createTweetTable(searchedWord, tableName);
         twitterDBImpl.updateTableHashMap(tableHashMapByWord);
      }      
      for (Iterator<Tweet> iterator = tweets.iterator(); iterator.hasNext();)
      {
         Tweet tweet = (Tweet) iterator.next();
         if(!twitterDBImpl.tweetExist(tableName,tweet.getId()))
         {
            twitterDBImpl.insertTweet(tableName, tweet);
         }
      }  
   }   
   
   
   
   public void insertTweetstoDBByTableName(List<Tweet> tweets, String searchedWord,String tableName)
   {
      if(!tableHashMapByWord.containsKey(searchedWord) )
      {
         twitterDBImpl.createTweetTable(searchedWord, tableName);
         twitterDBImpl.updateTableHashMap(tableHashMapByWord);
      }      
      for (Iterator<Tweet> iterator = tweets.iterator(); iterator.hasNext();)
      {
         Tweet tweet = (Tweet) iterator.next();
         if(!twitterDBImpl.tweetExist(tableName,tweet.getId()))
         {
            twitterDBImpl.insertTweet(tableName, tweet);
         }
      }  
   }   
   
   public void insertUserTweetstoDB(List<Tweet> tweets, String userName)
   {
      String tableName=createTableName(userName);
      if(!userTableHashMapByWord.containsKey(userName) )
      {
         twitterDBImpl.createUserTweetTable(userName, tableName);
         twitterDBImpl.updateUserTableHashMap(userTableHashMapByWord);
      }      
      for (Iterator<Tweet> iterator = tweets.iterator(); iterator.hasNext();)
      {
         Tweet tweet = (Tweet) iterator.next();
         if(!twitterDBImpl.tweetExist(tableName,tweet.getId()))
         {
            twitterDBImpl.insertTweet(tableName, tweet);
         }
      }  
   }
   
   public void insertUserTweetstoDB(ArrayList<EmoTweet> emoTweets, String userName)
   {
      String tableName=createUserTableName(userName);
      if(!userTableHashMapByWord.containsKey(userName) )
      {
         twitterDBImpl.createUserTweetTable(userName, tableName);
         twitterDBImpl.updateUserTableHashMap(userTableHashMapByWord);
      }      
      for (Iterator<EmoTweet> iterator = emoTweets.iterator(); iterator.hasNext();)
      {
         EmoTweet emoTweet = (EmoTweet) iterator.next();
         if(!twitterDBImpl.tweetExist(tableName,emoTweet.getTweetID()))
         {
            twitterDBImpl.insertTweet(tableName, emoTweet);
         }
      }
      System.out.println(emoTweets.size() + " rows were inserted to : " +tableName);
   }   
   
   public void insertUserTweetstoDBByTableName(List<Tweet> tweets, String userName,String tableName)
   {
      if(!userTableHashMapByWord.containsKey(userName) )
      {
         twitterDBImpl.createUserTweetTable(userName, tableName);
         twitterDBImpl.updateUserTableHashMap(userTableHashMapByWord);
      }      
      for (Iterator<Tweet> iterator = tweets.iterator(); iterator.hasNext();)
      {
         Tweet tweet = (Tweet) iterator.next();
         if(!twitterDBImpl.tweetExist(tableName,tweet.getId()))
         {
            twitterDBImpl.insertTweet(tableName, tweet);
         }
      }  
   }   
   
   private String createTableName (String searchedWord)
   {
      String newWord = searchedWord.replaceAll(" ", "");
      String tableName = "tw_"+newWord+"_tb";
      return tableName; 
   }
   
   private String createUserTableName (String searchedWord)
   {
      String newWord = searchedWord.replaceAll(" ", "");
      String tableName = "twu_"+newWord+"_tb";
      return tableName; 
   }   
   
   public Twitter getTwitter()
   {
      return twitter;
   }

   public void setTwitter(Twitter twitter)
   {
      this.twitter = twitter;
   }

   /**
    * @return the tableHashMapByWord
    */
   public HashMap<String, String> getTableHashMapByWord()
   {
      return tableHashMapByWord;
   }

   /**
    * @param tableHashMapByWord the tableHashMapByWord to set
    */
   public void setTableHashMapByWord(HashMap<String, String> tableHashMapByWord)
   {
      this.tableHashMapByWord = tableHashMapByWord;
   }

   public EmoResultDO getResults(String word)
   {
      return analyzer.analyze(word,twitterDBImpl.getAllTweetsFromTables(getTableHashMapByWord().get(word)),true);
   }
   
   public EmoResultDO searchAndReturnResults(String word, boolean semanticAnalysis, ArrayList<EmoTweet> tweetList)
   {
      return analyzer.analyze(word,tweetList,semanticAnalysis);
   }

   public EmoResultDO getAnalysisResults(EmoAnalysisDO emoAnalysisDO)
   {
      ArrayList<EmoTweet> allTweetsFromTable = twitterDBImpl.getAllTweetsFromTables(getTableHashMapByWord().get(emoAnalysisDO.getKeyword()));
      ArrayList<EmoTweet> selectedTweets = new ArrayList<EmoTweet>();
      for (Iterator<EmoTweet> iterator = allTweetsFromTable.iterator(); iterator.hasNext();)
      {
         EmoTweet emoTweet = (EmoTweet) iterator.next();
         if(emoTweet.getTweetDate().after(emoAnalysisDO.startDate) && emoTweet.getTweetDate().before(emoAnalysisDO.endDate))
         {
            selectedTweets.add(emoTweet);
         } 
      }
      return analyzer.analyze(emoAnalysisDO.getKeyword(),selectedTweets,emoAnalysisDO.isSemanticAnalysis());
   }

   /**
    * @return the userTableHashMapByWord
    */
   public HashMap<String, String> getUserTableHashMapByWord()
   {
      return userTableHashMapByWord;
   }

   /**
    * @param userTableHashMapByWord the userTableHashMapByWord to set
    */
   public void setUserTableHashMapByWord(HashMap<String, String> userTableHashMapByWord)
   {
      this.userTableHashMapByWord = userTableHashMapByWord;
   }   

}
