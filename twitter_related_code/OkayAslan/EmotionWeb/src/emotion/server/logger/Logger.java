package emotion.server.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import emotion.shared.EmoNvNTriplets;
import emotion.shared.EmoTweet;

public class Logger
{
   private static String DATE_FORMAT_FOR_DISPLAY   = "dd-MMMMM-yyyy_hh-mm-ss";
   private static SimpleDateFormat simpleDataFormatterForDisplay = new SimpleDateFormat(DATE_FORMAT_FOR_DISPLAY,Locale.US);
   private Calendar cal=Calendar.getInstance(Locale.US);
   private static final String NL = System.getProperty("line.separator");
   private boolean logOn = false;
   
   public void writeConsoleWithTime(String logParam)
   {
      cal=Calendar.getInstance(Locale.US);
      if(logOn)
      {
         System.out.println(logParam+" : "+simpleDataFormatterForDisplay.format(cal.getTime()));
      }
   }
   
   public void writeDoubleHasHMapToFile(String word, HashMap<String,HashMap<String, String>> writerHashMap)
   {
      try
      {
         FileWriter fstream = new FileWriter(word+"_Tweets.txt");
         BufferedWriter out = new BufferedWriter(fstream);
         
         Iterator<String> HashmapIterator = writerHashMap.keySet().iterator();
         while(HashmapIterator.hasNext()) 
         {
            String key = HashmapIterator.next();
            out.write(key+NL+NL);
            
            HashMap<String, String> val = writerHashMap.get(key);
            Iterator<String> tweetHashmapIterator = val.keySet().iterator();
            while(tweetHashmapIterator.hasNext()) 
            {
               String tweet = tweetHashmapIterator.next(); 
               out.write(tweet+NL);
            }
            out.write("-------------------------------------------"+NL);
         }
         out.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   /**
    * @return the logOn
    */
   public boolean isLogOn()
   {
      return logOn;
   }

   /**
    * @param logOn the logOn to set
    */
   public void setLogOn(boolean logOn)
   {
      this.logOn = logOn;
   }

   public void writeNvNArrayListToFile(String word, ArrayList<EmoNvNTriplets> overallEmoNvNTripletsArrList)
   {
      try
      {
         FileWriter fstream = new FileWriter(word+"_Tweets.txt");
         BufferedWriter out = new BufferedWriter(fstream);
         for (Iterator<EmoNvNTriplets> iterator = overallEmoNvNTripletsArrList.iterator(); iterator.hasNext();)
         {
            EmoNvNTriplets emoNvNTriplets = (EmoNvNTriplets) iterator.next();
            out.write(emoNvNTriplets.toString()+NL+NL);
            ArrayList<EmoTweet> emoTweetArrList = emoNvNTriplets.getTweets();
            for (Iterator<EmoTweet> iterator2 = emoTweetArrList.iterator(); iterator2.hasNext();)
            {
               EmoTweet emoTweet = (EmoTweet) iterator2.next();
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
   }
}
