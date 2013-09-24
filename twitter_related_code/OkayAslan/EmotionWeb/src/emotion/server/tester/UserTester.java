package emotion.server.tester;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import emotion.server.EmotionWebServiceImpl;
import emotion.server.analyzer.AnalysisData;
import emotion.server.analyzer.Analyzer;
import emotion.server.analyzer.TweetAnalyzer;
import emotion.server.twitter.TwitterDBImpl;
import emotion.server.twitter.TwitterImpl;
import emotion.server.util.TweetUtil;
import emotion.shared.EmoTweet;

public class UserTester
{

   private static EmotionWebServiceImpl emoWebSvcImpl = new EmotionWebServiceImpl();
   private static TwitterImpl twitterImpl = TwitterImpl.getInstance();
   private static Twitter twitter = null;
   private static TweetUtil tweetUtil = new TweetUtil();
   private static Analyzer analyzer= new Analyzer();
   private static TwitterDBImpl twitterDBImpl = TwitterDBImpl.getInstance();
   private static LatexWriter latexWriter = new LatexWriter(); 
   
   private static String DATE_FORMAT_FOR_SORT      = "yyyyMMdd";
   private static String DATE_FORMAT_FOR_DISPLAY   = "dd-MMM-yyyy";
   private static SimpleDateFormat simpleDataFormatterForDisplay = new SimpleDateFormat(DATE_FORMAT_FOR_DISPLAY,Locale.US);
   private static SimpleDateFormat simpleDataFormatterForSort = new SimpleDateFormat(DATE_FORMAT_FOR_SORT,Locale.US);
   
   private static final String NL = System.getProperty("line.separator");
   
   private static NumberFormat numberFormatter = new DecimalFormat("#,###,###.##"); 
   
  // "aryvee", 
   //"cnnbrk", "bbcnews", "aplusk", "TechCrunch", "Oprah", "zef", "BarackObama", "BryanAlexander", "EelcoVisser", "GuyKawasaki", "hrheingold", "dsearls", "johnbreslin", "TheEconomist", "davewiner", "TheEllenShow", "nytimes", "BreakingNews", "TIME", "mashable", "TheOnion", "andersoncooper", "CBSNews", "britneyspears", "THE_REAL_SHAQ", "MariahCarey", "50cent", "snoopdogg", "PerezHilton", "kevinrose", "google", "leolaporte", "Veronica", "zappos", "iamdiddy", "ijustine", "postsecret", "smashingmag", "shoemoney", "Jason", "Scobleizer","NiemanLab", "jayrosen_nyu", "Poynter", "journalismnews", "Mediabistro", "Newsweek"  
   public  static String[] USERS_TWEETS_LIST={"johnschuhmann"};
 //  public  static String[] RANDOM_USERS_TWEETS_LIST={"TeamMalachiae", "mathrabbit1", "greatspeaking", "stjohnk5", "orangeunicorns", "anna0974" ,"iTauqeer","wsh66","LUKIKA","Alyssafelldown", "Hajji_love", "CallaLove"};
   public  static String[] RANDOM_USERS_TWEETS_LIST={"big_picture", "bbcbreaking", "espn", "harvardbiz", "gizmodo", "wired" ,"wsj","pitchforkmedia","rollingstone","whitehouse", "cnn", "tweetmeme", "peoplemag"};

   public static void main(String[] args)
   {
    //  searchUser();  
    //  saveUserTweets();
    //  fireAllUsers();
      fireAllUsersForSelectedUsers();
  //    printLatexAllUsers();
   //   writeLatexTablesForSelectedUsers();
  //    writeNvNTweetsToAllUsers();
  //    writeNvNTweetsforSelectedUsers();
  //    printHeader();
  //    fireEverything("uskudarli", "twu_uskudarli_tb");
//      System.out.println(simpleDataFormatterForDisplay.format(Calendar.getInstance().getTime()));
  //    latexWriter.printUserLatexTable(null);
   }


   private static void writeNvNTweetsforSelectedUsers()
   {
     HashMap<String, String> tableMap = twitterImpl.getUserTableHashMapByWord();
      
      if(tableMap!=null && tableMap.size()>0)
      {
         for (int i = 0; i < RANDOM_USERS_TWEETS_LIST.length; i++)
         {
            String userName = RANDOM_USERS_TWEETS_LIST[i];
            String tableName = tableMap.get(userName);

            ArrayList<EmoTweet> allTweetArrList = twitterDBImpl.getAllTweetsFromTables(tableName);
            
            AnalysisData analysisData = new AnalysisData();
            analysisData.setSearchedWord(userName);
            analysisData.setAllTweetArrayList(allTweetArrList);
            TweetAnalyzer tweetAnalyzer = new TweetAnalyzer(analysisData);
            tweetAnalyzer.analyzeTweets();
            
            writeUserNvN(userName, analysisData);
         }
      }
   }


   /**
    * @param userName
    * @param analysisData
    */
   private static void writeUserNvN(String userName, AnalysisData analysisData)
   {
      HashMap<String,ArrayList<EmoTweet>> nvnToEmoTweetsHashMap = analysisData.getNvnToEmoTweetsHashMap();
      
      System.out.println("writing user : " + userName);
      
      FileWriter fstream;
      try
      {
         fstream = new FileWriter("userData//"+userName+"_NvNtoTweets.txt");
         BufferedWriter out = new BufferedWriter(fstream);
         
         Iterator<String> hashmapIterator = nvnToEmoTweetsHashMap.keySet().iterator();
         while(hashmapIterator.hasNext()) 
         {
            String nvnString = hashmapIterator.next(); 
            ArrayList<EmoTweet> emoTweetArrList = nvnToEmoTweetsHashMap.get(nvnString);

            out.write(nvnString+NL);

            for (Iterator<EmoTweet> iterator = emoTweetArrList.iterator(); iterator.hasNext();)
            {
               EmoTweet emoTweet = (EmoTweet) iterator.next();
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
      System.out.println("user : " + userName+" was written.");
   }


   private static void fireAllUsersForSelectedUsers()
   {
      HashMap<String, String> tableMap = twitterImpl.getUserTableHashMapByWord();
      
      if(tableMap!=null && tableMap.size()>0)
      {
         for (int i = 0; i < RANDOM_USERS_TWEETS_LIST.length; i++)
         {
            String userName = RANDOM_USERS_TWEETS_LIST[i];
            String tableName = tableMap.get(userName);
            fireEverything(userName,tableName);
         }
      }
   }


   private static void writeLatexTablesForSelectedUsers()
   {
      HashMap<String, String> tableMap = twitterImpl.getUserTableHashMapByWord();
      
      if(tableMap!=null && tableMap.size()>0)
      {
         FileWriter fstream;
         try
         {
            fstream = new FileWriter("OverallUserLatexTable_3.txt");
            BufferedWriter out = new BufferedWriter(fstream);
            
            for (int i = 0; i < RANDOM_USERS_TWEETS_LIST.length; i++)
            {
               String userName = RANDOM_USERS_TWEETS_LIST[i];
               String tableName = tableMap.get(userName);
               
               ArrayList<EmoTweet> allTweetArrList = twitterDBImpl.getAllTweetsFromTables(tableName);
               
               AnalysisData analysisData = new AnalysisData();
               analysisData.setSearchedWord(userName);
               analysisData.setAllTweetArrayList(allTweetArrList);
               TweetAnalyzer tweetAnalyzer = new TweetAnalyzer(analysisData);
               tweetAnalyzer.analyzeTweets();
              
               writeUserNvN(userName, analysisData);
               
               Integer countAllTweets                             = analysisData.getAllTweetArrayList().size();
               Integer countDistinctAllTweets                     = analysisData.getAllWordOccurrenceHashMap().size();
               Integer countAllWords                              = analysisData.getCountAllWords();
               Integer countDistinctAllWords                      = analysisData.getAllWordOccurrenceHashMap().size();
               Integer countAllSignificantWords                   = analysisData.getCountAllSignificantWords();
               Integer countDistinctAllSignificantWords           = analysisData.getAllSignificantWordOccurrenceHashMap().size();
               Integer countAllnWords                             = analysisData.getCountAllnWords();
               Integer countDistinctAllnWords                     = analysisData.getAllnWordOccurrenceHashMap().size();
               Integer countAllVerbs                              = analysisData.getCountAllVerbs();
               Integer countDistinctAllVerbs                      = analysisData.getAllVerbOccurrenceHashMap().size();
               Integer countAllRT                                 = analysisData.getCountRTs();                  
               
               String countAllTweetsStr                           = numberFormatter.format(countAllTweets);
               String countDistinctAllTweetsStr                   = numberFormatter.format(countDistinctAllTweets);
               String countAllWordsStr                            = numberFormatter.format(countAllWords);
               String countDistinctAllWordsStr                    = numberFormatter.format(countDistinctAllWords);
               String countAllSignificantWordsStr                 = numberFormatter.format(countAllSignificantWords);
               String countDistinctAllSignificantWordsStr         = numberFormatter.format(countDistinctAllSignificantWords);
               String countAllnWordsStr                           = numberFormatter.format(countAllnWords);
               String countDistinctAllnWordsStr                   = numberFormatter.format(countDistinctAllnWords);
               String countAllVerbsStr                            = numberFormatter.format(countAllVerbs);
               String countDistinctAllVerbsStr                    = numberFormatter.format(countDistinctAllVerbs);
               String countAllRTStr                               = numberFormatter.format(countAllRT);
               
               
               Integer countAllNVNTweets                          = analysisData.getAllNVNTweetsHashMap().size();
               Integer countDistinctAllNVNTweets                  = analysisData.getAllNVNWordOccurrenceHashMap().size();
               Integer countAllNVNWords                           = analysisData.getCountAllNVNWords();
               Integer countDistinctAllNVNWords                   = analysisData.getAllNVNWordOccurrenceHashMap().size();
               Integer countAllNVNSignificantWords                = analysisData.getCountAllNVNSignificantWords();
               Integer countDistinctAllNVNSignificantWords        = analysisData.getAllNVNSignificantWordOccurrenceHashMap().size();
               Integer countAllNVNnWords                          = analysisData.getCountAllNVNnWords();
               Integer countDistinctAllNVNnWords                  = analysisData.getAllNVNnWordOccurrenceHashMap().size();
               Integer countAllNVNVerbs                           = analysisData.getCountAllNVNVerbs();
               Integer countDistinctAllNVNVerbs                   = analysisData.getAllNVNVerbOccurrenceHashMap().size();
               Integer countAllNVNRT                              = analysisData.getCountNVNRTs();                  
               
               String countAllNVNTweetsStr                        = numberFormatter.format(countAllNVNTweets);
               String countDistinctAllNVNTweetsStr                = numberFormatter.format(countDistinctAllNVNTweets);
               String countAllNVNWordsStr                         = numberFormatter.format(countAllNVNWords);
               String countDistinctAllNVNWordsStr                 = numberFormatter.format(countDistinctAllNVNWords);
               String countAllNVNSignificantWordsStr              = numberFormatter.format(countAllNVNSignificantWords);
               String countDistinctAllNVNSignificantWordsStr      = numberFormatter.format(countDistinctAllNVNSignificantWords);
               String countAllNVNnWordsStr                        = numberFormatter.format(countAllNVNnWords);
               String countDistinctAllNVNnWordsStr                = numberFormatter.format(countDistinctAllNVNnWords);
               String countAllNVNVerbsStr                         = numberFormatter.format(countAllNVNVerbs);
               String countDistinctAllNVNVerbsStr                 = numberFormatter.format(countDistinctAllNVNVerbs);
               String countAllNVNRTStr                            = numberFormatter.format(countAllNVNRT);
               
           
               double countTweetsRatio                             = (double) countAllNVNTweets / countAllTweets;
               double countDistinctTweetsRatio                     = (double) countDistinctAllNVNTweets / countDistinctAllTweets;
               double countWordsRatio                              = (double) countAllNVNWords / countAllWords;
               double countDistinctWordsRatio                      = (double) countDistinctAllNVNWords / countDistinctAllWords;
               double countSignificantWordsRatio                   = (double) countAllNVNSignificantWords / countAllSignificantWords;
               double countDistinctSignificantWordsRatio           = (double) countDistinctAllNVNSignificantWords /countDistinctAllSignificantWords;
               double countnWordsRatio                             = (double) countAllNVNnWords / countAllnWords;
               double countDistinctnWordsRatio                     = (double) countDistinctAllNVNnWords / countDistinctAllnWords;
               double countVerbsRatio                              = (double) countAllNVNVerbs / countAllVerbs ;
               double countDistinctVerbsRatio                      = (double) countDistinctAllNVNVerbs / countDistinctAllVerbs ;
               double countRTRatio                                 = (double) countAllNVNRT /countAllRT;                  
               
               String countTweetsRatioStr                         = numberFormatter.format(countTweetsRatio);
               String countDistinctTweetsRatioStr                 = numberFormatter.format(countDistinctTweetsRatio);
               String countWordsRatioStr                          = numberFormatter.format(countWordsRatio);
               String countDistinctWordsRatioStr                  = numberFormatter.format(countDistinctWordsRatio);
               String countSignificantWordsRatioStr               = numberFormatter.format(countSignificantWordsRatio);
               String countDistinctSignificantWordsRatioStr       = numberFormatter.format(countDistinctSignificantWordsRatio);
               String countnWordsRatioStr                         = numberFormatter.format(countnWordsRatio);
               String countDistinctnWordsRatioStr                 = numberFormatter.format(countDistinctnWordsRatio);
               String countVerbsRatioStr                          = numberFormatter.format(countVerbsRatio);
               String countDistinctVerbsRatioStr                  = numberFormatter.format(countDistinctVerbsRatio);
               String countRTRatioStr                             = numberFormatter.format(countRTRatio);
            

               out.write("\\begin{table}[H]");
               out.write("\n");
               out.write("    \\begin{center}");
               out.write("\n");
               out.write("          \\begin{tabular}{|r|r|r|r|}");
               out.write("\n");
               out.write("                \\cline{1-2} " );
               out.write("\n");
               out.write("                      \\textbf{UserName}                     & "+analysisData.getSearchedWord()+"       \\\\");
               out.write("\n");
               out.write("                \\cline{1-2}");
               out.write("\n");
               out.write("                      \\textbf{Start Date}                  & "+analysisData.getMinDate()+"   \\\\");
               out.write("\n");
               out.write("                \\cline{1-2}");
               out.write("\n");
               out.write("                      \\textbf{End Date}                    & "+analysisData.getMaxDate()+"   \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                                                            & \\textbf{All}   & \\textbf{{\\nvnB}}        &  \\textbf{{\\nvnB}/\\textbf{All}} \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of tweets}               & "+countAllTweetsStr+"                       &     "+countAllNVNTweetsStr+"                        &  "+ countTweetsRatioStr+"  \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of dis. tweets}          & "+countDistinctAllTweetsStr+"               &     "+countDistinctAllNVNTweetsStr+"                &  "+ countDistinctTweetsRatioStr+"  \\\\");      
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of words}                & "+countAllWordsStr+"                        &     "+countAllNVNWordsStr+"                         &  "+countWordsRatioStr+"   \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of dis. words}           & "+countDistinctAllWordsStr+"                &     "+countDistinctAllNVNWordsStr+"                 &  "+countDistinctWordsRatioStr+"  \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of sign. words}          & "+countAllSignificantWordsStr+"             &     "+countAllNVNSignificantWordsStr+"              &  "+countSignificantWordsRatioStr+" \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of dis. sign. words}     & "+countDistinctAllSignificantWordsStr+"     &     "+countDistinctAllNVNSignificantWordsStr+"      &  "+countDistinctSignificantWordsRatioStr+" \\\\");
               out.write("\n");
               out.write("                \\hline");                                                  
               out.write("\n");
               out.write("                      \\textbf{\\# of {\\nword}}            & "+countAllnWordsStr+"                       &     "+countAllNVNnWordsStr+"                        &  "+countnWordsRatioStr+" \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of dist. {\\nword}}      & "+countDistinctAllnWordsStr+"               &     "+countDistinctAllNVNnWordsStr+"                &  "+countDistinctnWordsRatioStr+" \\\\");   
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of verbs}                & "+countAllVerbsStr+"                        &     "+countAllNVNVerbsStr+"                         &  "+countVerbsRatioStr+" \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of dist. verbs}          & "+countDistinctAllVerbsStr+"                &     "+countDistinctAllNVNVerbsStr+"                 &  "+countDistinctVerbsRatioStr+"\\\\");  
               out.write("\n");
               out.write("                \\hline");      
               out.write("\n");
               out.write("                      \\textbf{\\# of RTs}                  & "+countAllRTStr+"                           &     "+countAllNVNRTStr+"                            &  "+countRTRatioStr+"\\\\"); 
               out.write("\n");
               out.write("                \\hline");   
               out.write("\n");
               out.write("          \\end{tabular}");
               out.write("\n");
               out.write("    \\end{center}");
               out.write("\n");
               out.write("\\caption{\\label{tab:summary"+analysisData.getSearchedWord()+"} The summary of the collected data for user "+analysisData.getSearchedWord()+"}");
               out.write("\n");
               out.write("\\end{table}");        
               
               
            
               out.write("\n");
               out.write("\n");
               
               System.out.println(" written : "+analysisData.getSearchedWord());
         }
         
         out.close();
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
         
      }
   }


   private static void writeNvNTweetsToAllUsers()
   {
     HashMap<String, String> tableMap = twitterImpl.getUserTableHashMapByWord();
      
      if(tableMap!=null && tableMap.size()>0)
      {
         Set<String> tableMapSet=tableMap.keySet();  
         Iterator<String> tableMapIterator=tableMapSet.iterator();
         while(tableMapIterator.hasNext())
         {
            String user = tableMapIterator.next(); 
            String tableName = tableMap.get(user);
            ArrayList<EmoTweet> allTweetArrList = twitterDBImpl.getAllTweetsFromTables(tableName);
            
            AnalysisData analysisData = new AnalysisData();
            analysisData.setSearchedWord(user);
            analysisData.setAllTweetArrayList(allTweetArrList);
            TweetAnalyzer tweetAnalyzer = new TweetAnalyzer(analysisData);
            tweetAnalyzer.analyzeTweets();
            
            writeUserNvN(user, analysisData);
         }
      }
   }


   private static void fireAllUsers()
   {
  //    System.out.println("number of all tweets ; number of all distinct tweets ; number of all words ; number of all distinct words ; number of all significant words ; number of all distinct significant words ; number of all nWord words ; number of all distinct nWord words ; number of all verbs ; number of all verbs ; number of all distinct verbs ; number of all RTs");
      HashMap<String, String> tableMap = twitterImpl.getUserTableHashMapByWord();
      
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

   
   private static void printLatexAllUsers()
   {
      HashMap<String, String> tableMap = twitterImpl.getUserTableHashMapByWord();
      
      if(tableMap!=null && tableMap.size()>0)
      {
         Set<String> tableMapSet=tableMap.keySet();  
         Iterator<String> tableMapIterator=tableMapSet.iterator();
         
         FileWriter fstream;
         try
         {
            fstream = new FileWriter("OverallUserLatexTable.txt");
            BufferedWriter out = new BufferedWriter(fstream);
         
         while(tableMapIterator.hasNext())
         {
            String word = tableMapIterator.next(); 
            String tableName = tableMap.get(word);
            
            ArrayList<EmoTweet> allTweetArrList = twitterDBImpl.getAllTweetsFromTables(tableName);
            
            AnalysisData analysisData = new AnalysisData();
            analysisData.setSearchedWord(word);
            analysisData.setAllTweetArrayList(allTweetArrList);
            TweetAnalyzer tweetAnalyzer = new TweetAnalyzer(analysisData);
            tweetAnalyzer.analyzeTweets();
           
            Integer countAllTweets                             = analysisData.getAllTweetArrayList().size();
            Integer countDistinctAllTweets                     = analysisData.getAllWordOccurrenceHashMap().size();
            Integer countAllWords                              = analysisData.getCountAllWords();
            Integer countDistinctAllWords                      = analysisData.getAllWordOccurrenceHashMap().size();
            Integer countAllSignificantWords                   = analysisData.getCountAllSignificantWords();
            Integer countDistinctAllSignificantWords           = analysisData.getAllSignificantWordOccurrenceHashMap().size();
            Integer countAllnWords                             = analysisData.getCountAllnWords();
            Integer countDistinctAllnWords                     = analysisData.getAllnWordOccurrenceHashMap().size();
            Integer countAllVerbs                              = analysisData.getCountAllVerbs();
            Integer countDistinctAllVerbs                      = analysisData.getAllVerbOccurrenceHashMap().size();
            Integer countAllRT                                 = analysisData.getCountRTs();                  
            
            String countAllTweetsStr                           = numberFormatter.format(countAllTweets);
            String countDistinctAllTweetsStr                   = numberFormatter.format(countDistinctAllTweets);
            String countAllWordsStr                            = numberFormatter.format(countAllWords);
            String countDistinctAllWordsStr                    = numberFormatter.format(countDistinctAllWords);
            String countAllSignificantWordsStr                 = numberFormatter.format(countAllSignificantWords);
            String countDistinctAllSignificantWordsStr         = numberFormatter.format(countDistinctAllSignificantWords);
            String countAllnWordsStr                           = numberFormatter.format(countAllnWords);
            String countDistinctAllnWordsStr                   = numberFormatter.format(countDistinctAllnWords);
            String countAllVerbsStr                            = numberFormatter.format(countAllVerbs);
            String countDistinctAllVerbsStr                    = numberFormatter.format(countDistinctAllVerbs);
            String countAllRTStr                               = numberFormatter.format(countAllRT);
            
            
            Integer countAllNVNTweets                          = analysisData.getAllNVNTweetsHashMap().size();
            Integer countDistinctAllNVNTweets                  = analysisData.getAllNVNWordOccurrenceHashMap().size();
            Integer countAllNVNWords                           = analysisData.getCountAllNVNWords();
            Integer countDistinctAllNVNWords                   = analysisData.getAllNVNWordOccurrenceHashMap().size();
            Integer countAllNVNSignificantWords                = analysisData.getCountAllNVNSignificantWords();
            Integer countDistinctAllNVNSignificantWords        = analysisData.getAllNVNSignificantWordOccurrenceHashMap().size();
            Integer countAllNVNnWords                          = analysisData.getCountAllNVNnWords();
            Integer countDistinctAllNVNnWords                  = analysisData.getAllNVNnWordOccurrenceHashMap().size();
            Integer countAllNVNVerbs                           = analysisData.getCountAllNVNVerbs();
            Integer countDistinctAllNVNVerbs                   = analysisData.getAllNVNVerbOccurrenceHashMap().size();
            Integer countAllNVNRT                              = analysisData.getCountNVNRTs();                  
            
            String countAllNVNTweetsStr                        = numberFormatter.format(countAllNVNTweets);
            String countDistinctAllNVNTweetsStr                = numberFormatter.format(countDistinctAllNVNTweets);
            String countAllNVNWordsStr                         = numberFormatter.format(countAllNVNWords);
            String countDistinctAllNVNWordsStr                 = numberFormatter.format(countDistinctAllNVNWords);
            String countAllNVNSignificantWordsStr              = numberFormatter.format(countAllNVNSignificantWords);
            String countDistinctAllNVNSignificantWordsStr      = numberFormatter.format(countDistinctAllNVNSignificantWords);
            String countAllNVNnWordsStr                        = numberFormatter.format(countAllNVNnWords);
            String countDistinctAllNVNnWordsStr                = numberFormatter.format(countDistinctAllNVNnWords);
            String countAllNVNVerbsStr                         = numberFormatter.format(countAllNVNVerbs);
            String countDistinctAllNVNVerbsStr                 = numberFormatter.format(countDistinctAllNVNVerbs);
            String countAllNVNRTStr                            = numberFormatter.format(countAllNVNRT);
            
        
            double countTweetsRatio                             = (double) countAllNVNTweets / countAllTweets;
            double countDistinctTweetsRatio                     = (double) countDistinctAllNVNTweets / countDistinctAllTweets;
            double countWordsRatio                              = (double) countAllNVNWords / countAllWords;
            double countDistinctWordsRatio                      = (double) countDistinctAllNVNWords / countDistinctAllWords;
            double countSignificantWordsRatio                   = (double) countAllNVNSignificantWords / countAllSignificantWords;
            double countDistinctSignificantWordsRatio           = (double) countDistinctAllNVNSignificantWords /countDistinctAllSignificantWords;
            double countnWordsRatio                             = (double) countAllNVNnWords / countAllnWords;
            double countDistinctnWordsRatio                     = (double) countDistinctAllNVNnWords / countDistinctAllnWords;
            double countVerbsRatio                              = (double) countAllNVNVerbs / countAllVerbs ;
            double countDistinctVerbsRatio                      = (double) countDistinctAllNVNVerbs / countDistinctAllVerbs ;
            double countRTRatio                                 = (double) countAllNVNRT /countAllRT;                  
            
            String countTweetsRatioStr                         = numberFormatter.format(countTweetsRatio);
            String countDistinctTweetsRatioStr                 = numberFormatter.format(countDistinctTweetsRatio);
            String countWordsRatioStr                          = numberFormatter.format(countWordsRatio);
            String countDistinctWordsRatioStr                  = numberFormatter.format(countDistinctWordsRatio);
            String countSignificantWordsRatioStr               = numberFormatter.format(countSignificantWordsRatio);
            String countDistinctSignificantWordsRatioStr       = numberFormatter.format(countDistinctSignificantWordsRatio);
            String countnWordsRatioStr                         = numberFormatter.format(countnWordsRatio);
            String countDistinctnWordsRatioStr                 = numberFormatter.format(countDistinctnWordsRatio);
            String countVerbsRatioStr                          = numberFormatter.format(countVerbsRatio);
            String countDistinctVerbsRatioStr                  = numberFormatter.format(countDistinctVerbsRatio);
            String countRTRatioStr                             = numberFormatter.format(countRTRatio);
            
            
            
               
               out.write("\\begin{table}[H]");
               out.write("\n");
               out.write("    \\begin{center}");
               out.write("\n");
               out.write("          \\begin{tabular}{|r|r|r|r|}");
               out.write("\n");
               out.write("                \\cline{1-2} " );
               out.write("\n");
               out.write("                      \\textbf{UserName}                     & "+analysisData.getSearchedWord()+"       \\\\");
               out.write("\n");
               out.write("                \\cline{1-2}");
               out.write("\n");
               out.write("                      \\textbf{Start Date}                  & "+analysisData.getMinDate()+"   \\\\");
               out.write("\n");
               out.write("                \\cline{1-2}");
               out.write("\n");
               out.write("                      \\textbf{End Date}                    & "+analysisData.getMaxDate()+"   \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                                                            & \\textbf{All}   & \\textbf{{\\nvnB}}        &  \\textbf{{\\nvnB}/\\textbf{All}} \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of tweets}               & "+countAllTweetsStr+"                       &     "+countAllNVNTweetsStr+"                        &  "+ countTweetsRatioStr+"  \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of dis. tweets}          & "+countDistinctAllTweetsStr+"               &     "+countDistinctAllNVNTweetsStr+"                &  "+ countDistinctTweetsRatioStr+"  \\\\");      
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of words}                & "+countAllWordsStr+"                        &     "+countAllNVNWordsStr+"                         &  "+countWordsRatioStr+"   \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of dis. words}           & "+countDistinctAllWordsStr+"                &     "+countDistinctAllNVNWordsStr+"                 &  "+countDistinctWordsRatioStr+"  \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of sign. words}          & "+countAllSignificantWordsStr+"             &     "+countAllNVNSignificantWordsStr+"              &  "+countSignificantWordsRatioStr+" \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of dis. sign. words}     & "+countDistinctAllSignificantWordsStr+"     &     "+countDistinctAllNVNSignificantWordsStr+"      &  "+countDistinctSignificantWordsRatioStr+" \\\\");
               out.write("\n");
               out.write("                \\hline");                                                  
               out.write("\n");
               out.write("                      \\textbf{\\# of {\\nword}}            & "+countAllnWordsStr+"                       &     "+countAllNVNnWordsStr+"                        &  "+countnWordsRatioStr+" \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of dist. {\\nword}}      & "+countDistinctAllnWordsStr+"               &     "+countDistinctAllNVNnWordsStr+"                &  "+countDistinctnWordsRatioStr+" \\\\");   
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of verbs}                & "+countAllVerbsStr+"                        &     "+countAllNVNVerbsStr+"                         &  "+countVerbsRatioStr+" \\\\");
               out.write("\n");
               out.write("                \\hline");
               out.write("\n");
               out.write("                      \\textbf{\\# of dist. verbs}          & "+countDistinctAllVerbsStr+"                &     "+countDistinctAllNVNVerbsStr+"                 &  "+countDistinctVerbsRatioStr+"\\\\");  
               out.write("\n");
               out.write("                \\hline");      
               out.write("\n");
               out.write("                      \\textbf{\\# of RTs}                  & "+countAllRTStr+"                           &     "+countAllNVNRTStr+"                            &  "+countRTRatioStr+"\\\\"); 
               out.write("\n");
               out.write("                \\hline");   
               out.write("\n");
               out.write("          \\end{tabular}");
               out.write("\n");
               out.write("    \\end{center}");
               out.write("\n");
               out.write("\\caption{\\label{tab:summary"+analysisData.getSearchedWord()+"} The summary of the collected data for user "+analysisData.getSearchedWord()+"}");
               out.write("\n");
               out.write("\\end{table}");        
               
               
            
               out.write("\n");
               out.write("\n");
               
               System.out.println(" written : "+analysisData.getSearchedWord());
         }
         
         out.close();
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
         
      }
   }   
   
   
   public void writeOverallData(String fileName, AnalysisData analysisData)
   {
      Integer countAllTweets                             = analysisData.getAllTweetArrayList().size();
      Integer countDistinctAllTweets                     = analysisData.getAllWordOccurrenceHashMap().size();
      Integer countAllWords                              = analysisData.getCountAllWords();
      Integer countDistinctAllWords                      = analysisData.getAllWordOccurrenceHashMap().size();
      Integer countAllSignificantWords                   = analysisData.getCountAllSignificantWords();
      Integer countDistinctAllSignificantWords           = analysisData.getAllSignificantWordOccurrenceHashMap().size();
      Integer countAllnWords                             = analysisData.getCountAllnWords();
      Integer countDistinctAllnWords                     = analysisData.getAllnWordOccurrenceHashMap().size();
      Integer countAllVerbs                              = analysisData.getCountAllVerbs();
      Integer countDistinctAllVerbs                      = analysisData.getAllVerbOccurrenceHashMap().size();
      Integer countAllRT                                 = analysisData.getCountRTs();                  
      
      String countAllTweetsStr                           = numberFormatter.format(countAllTweets);
      String countDistinctAllTweetsStr                   = numberFormatter.format(countDistinctAllTweets);
      String countAllWordsStr                            = numberFormatter.format(countAllWords);
      String countDistinctAllWordsStr                    = numberFormatter.format(countDistinctAllWords);
      String countAllSignificantWordsStr                 = numberFormatter.format(countAllSignificantWords);
      String countDistinctAllSignificantWordsStr         = numberFormatter.format(countDistinctAllSignificantWords);
      String countAllnWordsStr                           = numberFormatter.format(countAllnWords);
      String countDistinctAllnWordsStr                   = numberFormatter.format(countDistinctAllnWords);
      String countAllVerbsStr                            = numberFormatter.format(countAllVerbs);
      String countDistinctAllVerbsStr                    = numberFormatter.format(countDistinctAllVerbs);
      String countAllRTStr                               = numberFormatter.format(countAllRT);
      
      
      Integer countAllNVNTweets                          = analysisData.getAllNVNTweetsHashMap().size();
      Integer countDistinctAllNVNTweets                  = analysisData.getAllNVNWordOccurrenceHashMap().size();
      Integer countAllNVNWords                           = analysisData.getCountAllNVNWords();
      Integer countDistinctAllNVNWords                   = analysisData.getAllNVNWordOccurrenceHashMap().size();
      Integer countAllNVNSignificantWords                = analysisData.getCountAllNVNSignificantWords();
      Integer countDistinctAllNVNSignificantWords        = analysisData.getAllNVNSignificantWordOccurrenceHashMap().size();
      Integer countAllNVNnWords                          = analysisData.getCountAllNVNnWords();
      Integer countDistinctAllNVNnWords                  = analysisData.getAllNVNnWordOccurrenceHashMap().size();
      Integer countAllNVNVerbs                           = analysisData.getCountAllNVNVerbs();
      Integer countDistinctAllNVNVerbs                   = analysisData.getAllNVNVerbOccurrenceHashMap().size();
      Integer countAllNVNRT                              = analysisData.getCountNVNRTs();                  
      
      String countAllNVNTweetsStr                        = numberFormatter.format(countAllNVNTweets);
      String countDistinctAllNVNTweetsStr                = numberFormatter.format(countDistinctAllNVNTweets);
      String countAllNVNWordsStr                         = numberFormatter.format(countAllNVNWords);
      String countDistinctAllNVNWordsStr                 = numberFormatter.format(countDistinctAllNVNWords);
      String countAllNVNSignificantWordsStr              = numberFormatter.format(countAllNVNSignificantWords);
      String countDistinctAllNVNSignificantWordsStr      = numberFormatter.format(countDistinctAllNVNSignificantWords);
      String countAllNVNnWordsStr                        = numberFormatter.format(countAllNVNnWords);
      String countDistinctAllNVNnWordsStr                = numberFormatter.format(countDistinctAllNVNnWords);
      String countAllNVNVerbsStr                         = numberFormatter.format(countAllNVNVerbs);
      String countDistinctAllNVNVerbsStr                 = numberFormatter.format(countDistinctAllNVNVerbs);
      String countAllNVNRTStr                            = numberFormatter.format(countAllNVNRT);
      
  
      double countTweetsRatio                             = (double) countAllNVNTweets / countAllTweets;
      double countDistinctTweetsRatio                     = (double) countDistinctAllNVNTweets / countDistinctAllTweets;
      double countWordsRatio                              = (double) countAllNVNWords / countAllWords;
      double countDistinctWordsRatio                      = (double) countDistinctAllNVNWords / countDistinctAllWords;
      double countSignificantWordsRatio                   = (double) countAllNVNSignificantWords / countAllSignificantWords;
      double countDistinctSignificantWordsRatio           = (double) countDistinctAllNVNSignificantWords /countDistinctAllSignificantWords;
      double countnWordsRatio                             = (double) countAllNVNnWords / countAllnWords;
      double countDistinctnWordsRatio                     = (double) countDistinctAllNVNnWords / countDistinctAllnWords;
      double countVerbsRatio                              = (double) countAllNVNVerbs / countAllVerbs ;
      double countDistinctVerbsRatio                      = (double) countDistinctAllNVNVerbs / countDistinctAllVerbs ;
      double countRTRatio                                 = (double) countAllNVNRT /countAllRT;                  
      
      String countTweetsRatioStr                         = numberFormatter.format(countTweetsRatio);
      String countDistinctTweetsRatioStr                 = numberFormatter.format(countDistinctTweetsRatio);
      String countWordsRatioStr                          = numberFormatter.format(countWordsRatio);
      String countDistinctWordsRatioStr                  = numberFormatter.format(countDistinctWordsRatio);
      String countSignificantWordsRatioStr               = numberFormatter.format(countSignificantWordsRatio);
      String countDistinctSignificantWordsRatioStr       = numberFormatter.format(countDistinctSignificantWordsRatio);
      String countnWordsRatioStr                         = numberFormatter.format(countnWordsRatio);
      String countDistinctnWordsRatioStr                 = numberFormatter.format(countDistinctnWordsRatio);
      String countVerbsRatioStr                          = numberFormatter.format(countVerbsRatio);
      String countDistinctVerbsRatioStr                  = numberFormatter.format(countDistinctVerbsRatio);
      String countRTRatioStr                             = numberFormatter.format(countRTRatio);
      
      
      FileWriter fstream;
      try
      {
         fstream = new FileWriter("OverallUserLatexTable.txt");
         BufferedWriter out = new BufferedWriter(fstream);
         out.write(fileName+"\n");
         
         out.write("\\begin{table}[H]");
         out.write("    \\begin{center}");
         out.write("          \\begin{tabular}{|r|r|r|r|}");
         out.write("                \\cline{1-2} " );
         out.write("                      \\textbf{Keyword}                     & "+analysisData.getSearchedWord()+"       \\\\");
         out.write("                \\cline{1-2}");
         out.write("                      \\textbf{Start Date}                  & "+analysisData.getMinDate()+"   \\\\");
         out.write("                \\cline{1-2}");
         out.write("                      \\textbf{End Date}                    & "+analysisData.getMaxDate()+"   \\\\");
         out.write("                \\hline");
         out.write("                                                            & \\textbf{All}   & \\textbf{{\\nvnB}}        &  \\textbf{{\\nvnB}/\\textbf{All}} \\\\");
         out.write("                \\hline");
         out.write("                      \\textbf{\\# of tweets}               & "+countAllTweetsStr+"                       &     "+countAllNVNTweetsStr+"                        &  "+ countTweetsRatioStr+"  \\\\");
         out.write("                \\hline");
         out.write("                      \\textbf{\\# of dis. tweets}          & "+countDistinctAllTweetsStr+"               &     "+countDistinctAllNVNTweetsStr+"                &  "+ countDistinctTweetsRatioStr+"  \\\\");      
         out.write("                \\hline");
         out.write("                      \\textbf{\\# of words}                & "+countAllWordsStr+"                        &     "+countAllNVNWordsStr+"                         &  "+countWordsRatioStr+"   \\\\");
         out.write("                \\hline");
         out.write("                      \\textbf{\\# of dis. words}           & "+countDistinctAllWordsStr+"                &     "+countDistinctAllNVNWordsStr+"                 &  "+countDistinctWordsRatioStr+"  \\\\");
         out.write("                \\hline");
         out.write("                      \\textbf{\\# of sign. words}          & "+countAllSignificantWordsStr+"             &     "+countAllNVNSignificantWordsStr+"              &  "+countSignificantWordsRatioStr+" \\\\");
         out.write("                \\hline");
         out.write("                      \\textbf{\\# of dis. sign. words}     & "+countDistinctAllSignificantWordsStr+"     &     "+countDistinctAllNVNSignificantWordsStr+"      &  "+countDistinctSignificantWordsRatioStr+" \\\\");
         out.write("                \\hline");                                                  
         out.write("                      \\textbf{\\# of {\\nword}}            & "+countAllnWordsStr+"                       &     "+countAllNVNnWordsStr+"                        &  "+countnWordsRatioStr+" \\\\");
         out.write("                \\hline");
         out.write("                      \\textbf{\\# of dist. {\\nword}}      & "+countDistinctAllnWordsStr+"               &     "+countDistinctAllNVNnWordsStr+"                &  "+countDistinctnWordsRatioStr+" \\\\");   
         out.write("                \\hline");
         out.write("                      \\textbf{\\# of verbs}                & "+countAllVerbsStr+"                        &     "+countAllNVNVerbsStr+"                         &  "+countVerbsRatioStr+" \\\\");
         out.write("                \\hline");
         out.write("                      \\textbf{\\# of dist. verbs}          & "+countDistinctAllVerbsStr+"                &     "+countDistinctAllNVNVerbsStr+"                 &  "+countDistinctVerbsRatioStr+"\\\\");  
         out.write("                \\hline");      
         out.write("                      \\textbf{\\# of RTs}                  & "+countAllRTStr+"                           &     "+countAllNVNRTStr+"                            &  "+countRTRatioStr+"\\\\"); 
         out.write("                \\hline");   
         out.write("          \\end{tabular}");
         out.write("    \\end{center}");
         out.write("\\caption{\\label{tab:summary"+analysisData.getSearchedWord()+"} The summary of the collected data for user "+analysisData.getSearchedWord()+"}");
         out.write("\\end{table}");        
         
         out.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }   
   
   
   private static void fireEverything(String word, String tableName)
   {
      ArrayList<EmoTweet> allTweetArrList = twitterDBImpl.getAllTweetsFromTables(tableName);
      
      AnalysisData analysisData = new AnalysisData();
      analysisData.setSearchedWord(word);
      analysisData.setAllTweetArrayList(allTweetArrList);
      TweetAnalyzer tweetAnalyzer = new TweetAnalyzer(analysisData);
      tweetAnalyzer.analyzeTweets();
      printOverallData(analysisData);
   }

   public static void printHeader()
   {
      System.out.print("UserName");
      System.out.print(";");
      System.out.print("number of all tweets");
      System.out.print(";");
      System.out.print("number of all distinct tweets");
      System.out.print(";");
      System.out.print("number of all words");
      System.out.print(";");
      System.out.print("number of all distinct words");
      System.out.print(";");
      System.out.print("number of all significant words");
      System.out.print(";");
      System.out.print("number of all distinct significant words");
      System.out.print(";");
      System.out.print("number of all nWord words");
      System.out.print(";");
      System.out.print("number of all distinct nWord words");
      System.out.print(";");
      System.out.print("number of all verbs");
      System.out.print(";");
      System.out.print("number of all distinct verbs");
      System.out.print(";");
      System.out.print("number of all RTs");
      System.out.print(";");
      System.out.print("number of all NVN tweets");
      System.out.print(";");
      System.out.print("number of all NVN distinct tweets");
      System.out.print(";");
      System.out.print("number of all NVN words");
      System.out.print(";");
      System.out.print("number of all NVN distinct words");
      System.out.print(";");
      System.out.print("number of all NVN significant words");
      System.out.print(";");
      System.out.print("number of all NVN distinct significant words");
      System.out.print(";");
      System.out.print("number of all NVN nWord words");
      System.out.print(";");
      System.out.print("number of all NVN distinct nWord words");
      System.out.print(";");
      System.out.print("number of all NVN verbs");
      System.out.print(";");
      System.out.print("number of all NVN distinct verbs");
      System.out.print(";");
      System.out.print("number of all NVN RTs");
      System.out.print(";");
      System.out.print("NVN / All ratio : tweets");
      System.out.print(";");
      System.out.print("NVN / All ratio : distinct tweets");
      System.out.print(";");
      System.out.print("NVN / All ratio : words");
      System.out.print(";");
      System.out.print("NVN / All ratio : distinct words");
      System.out.print(";");
      System.out.print("NVN / All ratio : significant words");
      System.out.print(";");
      System.out.print("NVN / All ratio : distinct significant words");
      System.out.print(";");
      System.out.print("NVN / All ratio : nWord words");
      System.out.print(";");
      System.out.print("NVN / All ratio : distinct nWord words");
      System.out.print(";");
      System.out.print("NVN / All ratio : verbs");
      System.out.print(";");
      System.out.print("NVN / All ratio : distinct verbs");
      System.out.print(";");
      System.out.print("NVN / All ratio : RTs");
      System.out.print("\n");
   }
   
   
   public static void printOverallData(AnalysisData analysisData)
   { 
      System.out.print(analysisData.getSearchedWord());
      System.out.print(";");
      System.out.print(analysisData.getAllTweetArrayList().size());
      System.out.print(";");
      System.out.print(analysisData.getAllDistinctEmoTweetsHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getCountAllWords());
      System.out.print(";");
      System.out.print(analysisData.getAllWordOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getCountAllSignificantWords());
      System.out.print(";");
      System.out.print(analysisData.getAllSignificantWordOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getCountAllnWords());
      System.out.print(";");
      System.out.print(analysisData.getAllnWordOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getCountAllVerbs() );
      System.out.print(";");
      System.out.print(analysisData.getAllVerbOccurrenceHashMap().size() );
      System.out.print(";");
      System.out.print(analysisData.getCountRTs());
      System.out.print(";");
      System.out.print(analysisData.getAllNVNTweetsHashMap().size() );
      System.out.print(";");
      System.out.print(analysisData.getAllDistinctNVNEmoTweetsHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getCountAllNVNWords());
      System.out.print(";");
      System.out.print(analysisData.getAllNVNWordOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getCountAllNVNSignificantWords()); 
      System.out.print(";");
      System.out.print(analysisData.getAllNVNSignificantWordOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getCountAllNVNnWords());
      System.out.print(";");
      System.out.print(analysisData.getAllNVNnWordOccurrenceHashMap().size());
      System.out.print(";");
      System.out.print(analysisData.getCountAllNVNVerbs() );
      System.out.print(";");
      System.out.print(analysisData.getAllNVNVerbOccurrenceHashMap().size() );
      System.out.print(";");
      System.out.print(analysisData.getCountNVNRTs());
      System.out.print(";");
      System.out.print((float) analysisData.getAllNVNTweetsHashMap().size() / analysisData.getAllTweetArrayList().size() );
      System.out.print(";");
      System.out.print((float) analysisData.getAllDistinctNVNEmoTweetsHashMap().size() / analysisData.getAllDistinctEmoTweetsHashMap().size() );
      System.out.print(";");
      System.out.print((float) analysisData.getCountAllNVNWords() / analysisData.getCountAllWords() );
      System.out.print(";");
      System.out.print((float) analysisData.getAllNVNWordOccurrenceHashMap().size() / analysisData.getAllWordOccurrenceHashMap().size() );
      System.out.print(";");
      System.out.print((float) analysisData.getCountAllNVNSignificantWords() / analysisData.getCountAllSignificantWords() ); 
      System.out.print(";");
      System.out.print((float) analysisData.getAllNVNSignificantWordOccurrenceHashMap().size() / analysisData.getAllSignificantWordOccurrenceHashMap().size() );
      System.out.print(";");
      System.out.print((float) analysisData.getCountAllNVNnWords() / analysisData.getCountAllnWords() ); 
      System.out.print(";");
      System.out.print((float) analysisData.getAllNVNnWordOccurrenceHashMap().size() / analysisData.getAllnWordOccurrenceHashMap().size() );
      System.out.print(";");
      System.out.print((float) analysisData.getCountAllNVNVerbs() / analysisData.getCountAllVerbs() );
      System.out.print(";");
      System.out.print((float) analysisData.getAllNVNVerbOccurrenceHashMap().size() / analysisData.getAllVerbOccurrenceHashMap().size() );
      System.out.print(";");
      System.out.print((float) analysisData.getCountNVNRTs() / analysisData.getCountRTs() );
      System.out.print("\n");

   }   
   
   

   /**
    * 
    */
   private static void saveUserTweets()
   {
      try
      {
         for (int i = 0; i < RANDOM_USERS_TWEETS_LIST.length; i++)
         {
            String userName = RANDOM_USERS_TWEETS_LIST[i];
            System.out.println("getting tweets from user : "+userName); 
            twitterImpl.insertUserTweetstoDB(tweetUtil.convertStatusToEmoTweet(twitterImpl.getUserTweets(userName)), userName);
            System.out.println("finishing tweets from user : "+userName); 
         }
         
      }
      catch (TwitterException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }


   /**
    * 
    */
   private static void searchUser()
   {
      twitter = twitterImpl.getTwitter();
      
      ResponseList<Status> userStatuses = null;
      Paging paging = new Paging(32, 100);
      int i =1;
      int k =1;
     // for (int pageCount = 32; pageCount >= 1; pageCount--) 
      for (int pageCount = 1; pageCount <= 32; pageCount++) 
      {   
         try
         {
             paging.setPage(pageCount);
             userStatuses = twitter.getUserTimeline("armoleon", paging);
         }
         catch (TwitterException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         if(userStatuses==null || userStatuses.isEmpty())
         {
            break;
         }   
         for (Status status : userStatuses)
         {
            System.out.println(pageCount+"_"+i+"_"+k  +": "+status.getText()); 
            i++;
            k++;
         }
         i =0;
      }
      
      twitter = twitterImpl.getTwitter();
   }

}
