package emotion.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public class EmoTweetWordData
{
   private String noun;
   private HashMap<String,Integer> afterNouns = new HashMap<String,Integer>();
   private ArrayList<String> tweetText = new ArrayList<String>();
   private HashMap<String,Integer> beforeAdjectives = new HashMap<String,Integer>();
   private HashMap<String,Integer> afterAdjectives = new HashMap<String,Integer>();
   private HashMap<String,Integer> beforeVerbs = new HashMap<String,Integer>();
   private HashMap<String,Integer> afterVerbs = new HashMap<String,Integer>();
   
   /**
    * @return the noun
    */
   public String getNoun()
   {
      return noun;
   }
   /**
    * @return the tweetText
    */
   public ArrayList<String> getTweetText()
   {
      return tweetText;
   }
   /**
    * @return the beforeAdjectives
    */
   public HashMap<String, Integer> getBeforeAdjectives()
   {
      return beforeAdjectives;
   }
   /**
    * @return the afterAdjectives
    */
   public HashMap<String, Integer> getAfterAdjectives()
   {
      return afterAdjectives;
   }
   /**
    * @return the beforeVerbs
    */
   public HashMap<String, Integer> getBeforeVerbs()
   {
      return beforeVerbs;
   }
   /**
    * @return the afterVerbs
    */
   public HashMap<String, Integer> getAfterVerbs()
   {
      return afterVerbs;
   }
   /**
    * @param noun the noun to set
    */
   public void setNoun(String noun)
   {
      this.noun = noun;
   }
   /**
    * @param tweetText the tweetText to set
    */
   public void setTweetText(ArrayList<String> tweetText)
   {
      this.tweetText = tweetText;
   }
   /**
    * @param beforeAdjectives the beforeAdjectives to set
    */
   public void setBeforeAdjectives(HashMap<String, Integer> beforeAdjectives)
   {
      this.beforeAdjectives = beforeAdjectives;
   }
   /**
    * @param afterAdjectives the afterAdjectives to set
    */
   public void setAfterAdjectives(HashMap<String, Integer> afterAdjectives)
   {
      this.afterAdjectives = afterAdjectives;
   }
   /**
    * @param beforeVerbs the beforeVerbs to set
    */
   public void setBeforeVerbs(HashMap<String, Integer> beforeVerbs)
   {
      this.beforeVerbs = beforeVerbs;
   }
   /**
    * @param afterVerbs the afterVerbs to set
    */
   public void setAfterVerbs(HashMap<String, Integer> afterVerbs)
   {
      this.afterVerbs = afterVerbs;
   }

   /**
    * @param afterVerbs the afterVerbs to set
    */
   public boolean containsPair(String word1, String word2)
   {
      for (Iterator<String> iterator = tweetText.iterator(); iterator.hasNext();)
      {
         String tweetText = (String) iterator.next();
         if(containWords(tweetText,word1))
         {
            if(containWords(tweetText,word2))
            {
               return true;
            }
         }
      }
      return false;
   }
   
   
   private boolean containWords(String tweetText, String word)
   {
      StringTokenizer st = new StringTokenizer(tweetText);
      while (st.hasMoreTokens()) 
      {
         if(st.nextToken().equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }
   /**
    * @param afterNouns the afterNouns to set
    */
   public void setAfterNouns(HashMap<String,Integer> afterNouns)
   {
      this.afterNouns = afterNouns;
   }
   /**
    * @return the afterNouns
    */
   public HashMap<String,Integer> getAfterNouns()
   {
      return afterNouns;
   }
   
   
   
}
