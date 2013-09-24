package emotion.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import twitter4j.TwitterException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import emotion.client.EmotionWebService;
import emotion.server.twitter.TwitterImpl;
import emotion.server.util.HashMapUtil;
import emotion.server.util.TweetUtil;
import emotion.shared.EmoAnalysisDO;
import emotion.shared.EmoDO;
import emotion.shared.EmoResultDO;
import emotion.shared.EmoTweet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class EmotionWebServiceImpl extends RemoteServiceServlet implements EmotionWebService
{

   private TweetUtil tweetUtil = new TweetUtil();
   private TwitterImpl twitterImpl = TwitterImpl.getInstance();
   private HashMapUtil hMapUtil = new HashMapUtil();
   private HashMap<String,String> jobMap = new HashMap<String,String>();

   
   @Override
   public EmoDO searchWordOnTwitter(String word) throws Exception
   {
      ArrayList<EmoTweet> tweetList = null;
      tweetList = tweetUtil.convertTweetToEmotweet(twitterImpl.searchWord(word));
      EmoDO emoDo = new EmoDO();
      emoDo.setEmoTweets(tweetList);
      emoDo.setSortedEmotionHashMapByWord(hMapUtil.sortEmotionHashMapByValue(tweetUtil.fillAndGetEmotionHashMap(tweetList)));
   /*   
      ConceptNetImpl conceptNetImpl = new ConceptNetImpl();
      conceptNetImpl.getConcept(word);
      MediaWikiImpl mediaWikiImpl = new MediaWikiImpl();
      mediaWikiImpl.queryWikiPedia(word);
      
      */
      return emoDo;
   }

   @Override
   public void saveJob(final String word) throws Exception
   {
      if(!jobMap.containsKey(word))
      {
         jobMap.put(word, word);
         Thread thread = new Thread(new Runnable()
         {
            @Override
            public void run()
            {
               int delay = 1000; 
               int period = 300000; 
               Timer timer = new Timer(); 
               timer.scheduleAtFixedRate(new TimerTask() 
               { 
                  public void run() 
                  { 
                     try
                     {
                        twitterImpl.insertTweetstoDB(twitterImpl.searchWord(word), word);
                     }
                     catch (TwitterException e)
                     {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                     }
                  } 
               }, delay, period);    
            }
         });
         thread.run();
      }
   }

   @Override
   public String[] getAllJobs() throws Exception
   {
      String[] allArcJob=null;
      HashMap<String, String> tableMap = twitterImpl.getTableHashMapByWord();
      if(tableMap!=null && tableMap.size()>0)
      {
         allArcJob = new String[tableMap.size()];
         Set<String> tableMapSet=tableMap.keySet();  
         Iterator<String> tableMapIterator=tableMapSet.iterator();
         int count = 0;
         while(tableMapIterator.hasNext())
         {
            allArcJob[count]=tableMapIterator.next();
            count++;
         }
      }
      return allArcJob;
   }

   @Override
   public EmoResultDO getJob(String word) throws Exception
   {
      EmoResultDO emoResultDO = twitterImpl.getResults(word);
      return emoResultDO;
   }

   @Override
   public EmoResultDO searchAndReturnResults(String word, boolean semanticAnalysis) throws Exception
   {
      ArrayList<EmoTweet> tweetList = null;
      tweetList = tweetUtil.convertTweetToEmotweet(twitterImpl.searchWord(word));
      return twitterImpl.searchAndReturnResults(word,semanticAnalysis,tweetList);
   }

   @Override
   public EmoResultDO performAnalysis(EmoAnalysisDO emoAnalysisDO) throws Exception
   {
      EmoResultDO emoResultDO = twitterImpl.getAnalysisResults(emoAnalysisDO);
      return emoResultDO;
   }

   @Override
   public EmoResultDO searchUserAndReturnResults(String word, boolean semanticAnalysis) throws Exception
   {
      ArrayList<EmoTweet> tweetList = null;
      tweetList = tweetUtil.convertStatusToEmoTweet(twitterImpl.getUserTweets(word));
      return twitterImpl.searchAndReturnResults(word,semanticAnalysis,tweetList);
   }

   @Override
   public String[] getAllAnalyzedUsers() throws Exception
   {
      String[] allAnalyzedUsers=null;
      HashMap<String, String> tableMap = twitterImpl.getUserTableHashMapByWord();
      if(tableMap!=null && tableMap.size()>0)
      {
         allAnalyzedUsers = new String[tableMap.size()];
         Set<String> tableMapSet=tableMap.keySet();  
         Iterator<String> tableMapIterator=tableMapSet.iterator();
         int count = 0;
         while(tableMapIterator.hasNext())
         {
            allAnalyzedUsers[count]=tableMapIterator.next();
            count++;
         }
      }
      return allAnalyzedUsers;
   }
}
