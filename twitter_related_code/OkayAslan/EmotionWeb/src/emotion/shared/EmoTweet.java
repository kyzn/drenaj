package emotion.shared;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class EmoTweet implements Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = 6797271885286734962L;
   
   private Long tweetID             = null;
   private int userID               = 0;
   private String userName          = null;
   private int userFollowers        = 0;
   private int userFriends          = 0;   
   private String userLocation      = null;
   private String description       = null;
   private String Source            = null;
   private String tweetText         = null;
   private boolean reTweet          = false;
   private Date tweetDate           = null;
   private Time tweetTime           = null;
   private String imageUrl          = null;
   private int toUserID             = 0;
   private String toUserName        = null;
   
   /**
    * @return the tweetID
    */
   public Long getTweetID()
   {
      return tweetID;
   }
   /**
    * @return the userID
    */
   public int getUserID()
   {
      return userID;
   }
   /**
    * @return the userName
    */
   public String getUserName()
   {
      return userName;
   }
   /**
    * @return the userFollowers
    */
   public int getUserFollowers()
   {
      return userFollowers;
   }
   /**
    * @return the userFriends
    */
   public int getUserFriends()
   {
      return userFriends;
   }
   /**
    * @return the userLocation
    */
   public String getUserLocation()
   {
      return userLocation;
   }
   /**
    * @return the description
    */
   public String getDescription()
   {
      return description;
   }
   /**
    * @param source the source to set
    */
   public void setSource(String source)
   {
      Source = source;
   }
   /**
    * @return the source
    */
   public String getSource()
   {
      return Source;
   }
   /**
    * @return the tweetText
    */
   public String getTweetText()
   {
      return tweetText;
   }

   /**
    * @return the tweetDate
    */
   public Date getTweetDate()
   {
      return tweetDate;
   }
   /**
    * @param tweetID the tweetID to set
    */
   public void setTweetID(Long tweetID)
   {
      this.tweetID = tweetID;
   }
   /**
    * @param userID the userID to set
    */
   public void setUserID(int userID)
   {
      this.userID = userID;
   }
   /**
    * @param userName the userName to set
    */
   public void setUserName(String userName)
   {
      this.userName = userName;
   }
   /**
    * @param userFollowers the userFollowers to set
    */
   public void setUserFollowers(int userFollowers)
   {
      this.userFollowers = userFollowers;
   }
   /**
    * @param userFriends the userFriends to set
    */
   public void setUserFriends(int userFriends)
   {
      this.userFriends = userFriends;
   }
   /**
    * @param userLocation the userLocation to set
    */
   public void setUserLocation(String userLocation)
   {
      this.userLocation = userLocation;
   }
   /**
    * @param description the description to set
    */
   public void setDescription(String description)
   {
      this.description = description;
   }
   /**
    * @param tweetText the tweetText to set
    */
   public void setTweetText(String tweetText)
   {
      this.tweetText = tweetText;
   }

   /**
    * @param tweetDate the tweetDate to set
    */
   public void setTweetDate(Date tweetDate)
   {
      this.tweetDate = tweetDate;
   }
   /**
    * @param imageUrl the imageUrl to set
    */
   public void setImageUrl(String imageUrl)
   {
      this.imageUrl = imageUrl;
   }
   /**
    * @return the imageUrl
    */
   public String getImageUrl()
   {
      return imageUrl;
   }
   /**
    * @return the toUserID
    */
   public int getToUserID()
   {
      return toUserID;
   }
   /**
    * @param toUserID the toUserID to set
    */
   public void setToUserID(int toUserID)
   {
      this.toUserID = toUserID;
   }
   /**
    * @return the toUserName
    */
   public String getToUserName()
   {
      return toUserName;
   }
   /**
    * @param toUserName the toUserName to set
    */
   public void setToUserName(String toUserName)
   {
      this.toUserName = toUserName;
   }
   /**
    * @param tweetTime the tweetTime to set
    */
   public void setTweetTime(Time tweetTime)
   {
      this.tweetTime = tweetTime;
   }
   /**
    * @return the tweetTime
    */
   public Time getTweetTime()
   {
      return tweetTime;
   }
   /**
    * @param reTweet the reTweet to set
    */
   public void setReTweet(boolean reTweet)
   {
      this.reTweet = reTweet;
   }
   /**
    * @return the reTweet
    */
   public boolean isReTweet()
   {
      return reTweet;
   }
}
