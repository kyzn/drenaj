package emotion.server.tester;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import emotion.server.analyzer.AnalysisData;
import emotion.server.analyzer.TweetAnalyzer;
import emotion.server.twitter.TwitterDBImpl;
import emotion.server.twitter.TwitterImpl;
import emotion.shared.EmoTweet;

public class DataTester
{
   private static NumberFormat numberFormatter = new DecimalFormat("#,###,###.##");   
   private static TwitterImpl twitterImpl = TwitterImpl.getInstance();
   private static TwitterDBImpl twitterDBImpl = TwitterDBImpl.getInstance();
   
   /**
    * @param args
    */
   public static void main(String[] args)
   {
      printLatexAllData();
   }

   
   private static void printLatexAllData()
   {
      HashMap<String, String> tableMap = twitterImpl.getTableHashMapByWord();
      
      if(tableMap!=null && tableMap.size()>0)
      {
         Set<String> tableMapSet=tableMap.keySet();  
         Iterator<String> tableMapIterator=tableMapSet.iterator();
         
         FileWriter fstream;
         try
         {
            fstream = new FileWriter("OverallDataLatexTable.txt");
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
               out.write("                      \\textbf{Keyword}                     & "+analysisData.getSearchedWord()+"       \\\\");
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
   
}
