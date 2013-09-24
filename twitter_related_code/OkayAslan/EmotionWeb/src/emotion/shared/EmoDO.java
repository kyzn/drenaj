package emotion.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class EmoDO implements Serializable
{
   private static final long serialVersionUID = -1304537242013245285L;
   
   private ArrayList<EmoTweet> emoTweets = null;
   private LinkedHashMap<String,Integer> sortedEmotionHashMapByWord = null;
   
   /**
    * @param emoTweets the emoTweets to set
    */
   public void setEmoTweets(ArrayList<EmoTweet> emoTweets)
   {
      this.emoTweets = emoTweets;
   }

   /**
    * @return the emoTweets
    */
   public ArrayList<EmoTweet> getEmoTweets()
   {
      return emoTweets;
   }

   /**
    * @param sortedEmotionHashMapByWord the sortedEmotionHashMapByWord to set
    */
   public void setSortedEmotionHashMapByWord(LinkedHashMap<String,Integer> sortedEmotionHashMapByWord)
   {
      this.sortedEmotionHashMapByWord = sortedEmotionHashMapByWord;
   }

   /**
    * @return the sortedEmotionHashMapByWord
    */
   public LinkedHashMap<String,Integer> getSortedEmotionHashMapByWord()
   {
      return sortedEmotionHashMapByWord;
   }

}
