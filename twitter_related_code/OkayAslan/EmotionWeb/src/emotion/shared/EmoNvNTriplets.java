package emotion.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class EmoNvNTriplets implements Serializable
{
   private static final long serialVersionUID = 7734597526948587825L;
   
   private String firstNoun;
   private String verb;
   private String secondNoun;
   private int strength = 0;
   private ArrayList<EmoTweet> tweets = new ArrayList<EmoTweet>();
   private EmoTweet tweet = new EmoTweet();
   
   /**
    * @return the firstNoun
    */
   public String getFirstNoun()
   {
      return firstNoun;
   }
   /**
    * @param firstNoun the firstNoun to set
    */
   public void setFirstNoun(String firstNoun)
   {
      this.firstNoun = firstNoun;
   }
   /**
    * @return the verb
    */
   public String getVerb()
   {
      return verb;
   }
   /**
    * @param verb the verb to set
    */
   public void setVerb(String verb)
   {
      this.verb = verb;
   }
   /**
    * @return the secondNoun
    */
   public String getSecondNoun()
   {
      return secondNoun;
   }
   /**
    * @param secondNoun the secondNoun to set
    */
   public void setSecondNoun(String secondNoun)
   {
      this.secondNoun = secondNoun;
   }
   /**
    * @return the strength
    */
   public int getStrength()
   {
      return strength;
   }
   /**
    * @param strength the strength to set
    */
   public void setStrength(int strength)
   {
      this.strength = strength;
   }
   /**
    * @return the tweets
    */
   public ArrayList<EmoTweet> getTweets()
   {
      return tweets;
   }
   /**
    * @param tweets the tweets to set
    */
   public void setTweets(ArrayList<EmoTweet> tweets)
   {
      this.tweets = tweets;
   }
   
   public String toString()
   {
      return getFirstNoun()+"_"+getVerb()+"_"+getSecondNoun();
   }
   /**
    * @param tweet the tweet to set
    */
   public void setTweet(EmoTweet tweet)
   {
      this.tweet = tweet;
   }
   /**
    * @return the tweet
    */
   public EmoTweet getTweet()
   {
      return tweet;
   }

}
