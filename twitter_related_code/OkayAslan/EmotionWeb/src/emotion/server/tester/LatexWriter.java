package emotion.server.tester;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import emotion.server.analyzer.AnalysisData;
import emotion.shared.EmoTweet;

public class LatexWriter
{
   private String path = null;
   private static final String NL = System.getProperty("line.separator");
   private static NumberFormat numberFormatter = new DecimalFormat("#,###,###.##"); 
   
   public LatexWriter(String fileName)
   {
      this.path="LatexData//"+fileName;
      boolean success = (new File(path)).mkdir();
      if (success) {
        System.out.println("Directory: " + path + " created");
      }
   }
   
   public LatexWriter()
   {}
   
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
         fstream = new FileWriter(path+"//"+fileName+"_OverallUserLatexTable.txt");
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
   
   public void printUserLatexTable(AnalysisData analysisData)
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
      
      
      
      System.out.println("\\begin{table}[H]");
      System.out.println("    \\begin{center}");
      System.out.println("          \\begin{tabular}{|r|r|r|r|}");
      System.out.println("                \\cline{1-2} " );
      System.out.println("                      \\textbf{Keyword}                     & "+analysisData.getSearchedWord()+"       \\\\");
      System.out.println("                \\cline{1-2}");
      System.out.println("                      \\textbf{Start Date}                  & "+analysisData.getMinDate()+"   \\\\");
      System.out.println("                \\cline{1-2}");
      System.out.println("                      \\textbf{End Date}                    & "+analysisData.getMaxDate()+"   \\\\");
      System.out.println("                \\hline");
      System.out.println("                                                            & \\textbf{All}   & \\textbf{{\\nvnB}}        &  \\textbf{{\\nvnB}/\\textbf{All}} \\\\");
      System.out.println("                \\hline");
      System.out.println("                      \\textbf{\\# of tweets}               & "+countAllTweetsStr+"                       &     "+countAllNVNTweetsStr+"                        &  "+ countTweetsRatioStr+"  \\\\");
      System.out.println("                \\hline");
      System.out.println("                      \\textbf{\\# of dis. tweets}          & "+countDistinctAllTweetsStr+"               &     "+countDistinctAllNVNTweetsStr+"                &  "+ countDistinctTweetsRatioStr+"  \\\\");      
      System.out.println("                \\hline");
      System.out.println("                      \\textbf{\\# of words}                & "+countAllWordsStr+"                        &     "+countAllNVNWordsStr+"                         &  "+countWordsRatioStr+"   \\\\");
      System.out.println("                \\hline");
      System.out.println("                      \\textbf{\\# of dis. words}           & "+countDistinctAllWordsStr+"                &     "+countDistinctAllNVNWordsStr+"                 &  "+countDistinctWordsRatioStr+"  \\\\");
      System.out.println("                \\hline");
      System.out.println("                      \\textbf{\\# of sign. words}          & "+countAllSignificantWordsStr+"             &     "+countAllNVNSignificantWordsStr+"              &  "+countSignificantWordsRatioStr+" \\\\");
      System.out.println("                \\hline");
      System.out.println("                      \\textbf{\\# of dis. sign. words}     & "+countDistinctAllSignificantWordsStr+"     &     "+countDistinctAllNVNSignificantWordsStr+"      &  "+countDistinctSignificantWordsRatioStr+" \\\\");
      System.out.println("                \\hline");                                                  
      System.out.println("                      \\textbf{\\# of {\\nword}}            & "+countAllnWordsStr+"                       &     "+countAllNVNnWordsStr+"                        &  "+countnWordsRatioStr+" \\\\");
      System.out.println("                \\hline");
      System.out.println("                      \\textbf{\\# of dist. {\\nword}}      & "+countDistinctAllnWordsStr+"               &     "+countDistinctAllNVNnWordsStr+"                &  "+countDistinctnWordsRatioStr+" \\\\");   
      System.out.println("                \\hline");
      System.out.println("                      \\textbf{\\# of verbs}                & "+countAllVerbsStr+"                        &     "+countAllNVNVerbsStr+"                         &  "+countVerbsRatioStr+" \\\\");
      System.out.println("                \\hline");
      System.out.println("                      \\textbf{\\# of dist. verbs}          & "+countDistinctAllVerbsStr+"                &     "+countDistinctAllNVNVerbsStr+"                 &  "+countDistinctVerbsRatioStr+"\\\\");  
      System.out.println("                \\hline");      
      System.out.println("                      \\textbf{\\# of RTs}                  & "+countAllRTStr+"                           &     "+countAllNVNRTStr+"                            &  "+countRTRatioStr+"\\\\"); 
      System.out.println("                \\hline");   
      System.out.println("          \\end{tabular}");
      System.out.println("    \\end{center}");
      System.out.println("\\caption{\\label{tab:summary"+analysisData.getSearchedWord()+"} The summary of the collected data for user "+analysisData.getSearchedWord()+"}");
      System.out.println("\\end{table}");
   }
}
