package emotion.server.twitter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import twitter4j.Tweet;
import emotion.shared.EmoTweet;

public class TwitterDBImpl
{

   private static TwitterDBImpl instance = new TwitterDBImpl();
   private Connection conn = null;
   private String userName = "root";
   private String password = "root";
   private String url = "jdbc:mysql://localhost/twemotion";
 //  private HashMap<String, String> tableHashMapByWord = new HashMap<String, String>();

   public static TwitterDBImpl getInstance()
   {
      return instance;
   }

   private TwitterDBImpl()
   {
      try
      {
         Class.forName("com.mysql.jdbc.Driver").newInstance();
         conn = DriverManager.getConnection(url, userName, password);
      }
      catch (InstantiationException e)
      {
         e.printStackTrace();
      }
      catch (IllegalAccessException e)
      {
         e.printStackTrace();
      }
      catch (ClassNotFoundException e)
      {
         e.printStackTrace();
      }
      catch (SQLException e)
      {
         e.printStackTrace();
      } 
   }

   public Connection getConn()
   {
      return conn;
   }

   public void CloseConnnection()
   {
      if (conn != null)
      {
         try
         {
            conn.close();
         }
         catch (SQLException e)
         {
            e.printStackTrace();
         }
      }
   }

   public void createTweetTable(String searchedWord, String tableName)
   {
      try
      {
         PreparedStatement s;
         s = getConn().prepareStatement(
               "CREATE TABLE `twemotion`.`" + tableName + "` ( `tweet_id` DOUBLE NOT NULL, `fromuser_id` DOUBLE NOT NULL, `touser_id` DOUBLE, "
                     + "`tweet_text` VARCHAR(250) NOT NULL, `fromuser_name` VARCHAR(100) NOT NULL, `touser_name` VARCHAR(100), `imageurl` VARCHAR(250), `date` DATETIME NOT NULL, "
                     + "`source` VARCHAR(200), PRIMARY KEY (`tweet_id`) ) ENGINE = InnoDB;");
         int count = s.executeUpdate ();
         s.close();
         System.out.println("table" + tableName + " is created.");
      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }
      insertToTableMap(searchedWord, tableName);
   }
   
   
   public void createUserTweetTable(String userName, String tableName)
   {
      try
      {
         PreparedStatement s;
         s = getConn().prepareStatement(
               "CREATE TABLE `twemotion`.`" + tableName + "` ( `tweet_id` DOUBLE NOT NULL, `fromuser_id` DOUBLE NOT NULL, `touser_id` DOUBLE, "
                     + "`tweet_text` VARCHAR(250) NOT NULL, `fromuser_name` VARCHAR(100) NOT NULL, `touser_name` VARCHAR(100), `imageurl` VARCHAR(250), `date` DATETIME NOT NULL, "
                     + "`source` VARCHAR(200), PRIMARY KEY (`tweet_id`) ) ENGINE = InnoDB;");
         int count = s.executeUpdate ();
         s.close();
         System.out.println("table " + tableName + " is created.");
      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }
      insertToUserTableMap(userName, tableName);
   }   
   

   public void insertTweet(String tableName, Tweet tweet)
   {
      try
      {
            PreparedStatement s;
            s = getConn()
                  .prepareStatement("INSERT INTO " + tableName + " " + "(tweet_id, fromuser_id,touser_id,tweet_text,fromuser_name,touser_name,imageurl,source,date) " + "VALUES(?,?,?,?,?,?,?,?,?)");
            s.setLong(1, tweet.getId());
            s.setLong(2, tweet.getFromUserId());
            s.setLong(3, tweet.getToUserId());
            s.setString(4, tweet.getText());
            s.setString(5, tweet.getFromUser());
            s.setString(6, tweet.getToUser());
            s.setString(7, tweet.getProfileImageUrl());
            s.setString(8, tweet.getSource());
            s.setTimestamp(9, new java.sql.Timestamp(tweet.getCreatedAt().getTime()));
            int count = s.executeUpdate();
            s.close();
            System.out.println(count + " rows were inserted to:" +tableName);
      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }
   }
   
   
   public void insertTweet(String tableName, EmoTweet emoTweet)
   {
      try
      {
            PreparedStatement s;
            s = getConn()
                  .prepareStatement("INSERT INTO " + tableName + " " + "(tweet_id, fromuser_id,touser_id,tweet_text,fromuser_name,touser_name,imageurl,source,date) " + "VALUES(?,?,?,?,?,?,?,?,?)");
            s.setLong(1, emoTweet.getTweetID());
            s.setLong(2, emoTweet.getUserFollowers());
            s.setLong(3, emoTweet.getToUserID());
            s.setString(4, emoTweet.getTweetText());
            s.setString(5, emoTweet.getUserName());
            s.setString(6, emoTweet.getToUserName());
            s.setString(7, emoTweet.getImageUrl());
            s.setString(8, emoTweet.getSource());
            s.setTimestamp(9, new java.sql.Timestamp(emoTweet.getTweetDate().getTime()));
            int count = s.executeUpdate();
            s.close();
          //  System.out.println(count + " rows were inserted to:" +tableName);
      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }
   }   
   
   

   public boolean tweetExist(String tableName, long id)
   {
      ResultSet rs = null;
      try
      {
         Statement stmt = conn.createStatement();
         rs = stmt.executeQuery("SELECT tweet_id FROM "+tableName +" where tweet_id="+id);

         if (rs.next())
         {
            return true;
         }
      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }
      finally
      {
         try
         {
            if(rs!=null)
            {
               rs.close();
            }
         }
         catch (SQLException e)
         {
            e.printStackTrace();
         }
      }
      
      return false;
   }

   public void updateTableHashMap(HashMap<String,String> tableHashMapByWord)
   {
      ResultSet rs = null;
      try
      {
         Statement stmt = conn.createStatement();
         rs = stmt.executeQuery("SELECT * FROM wordtablemap");

         while (rs.next())
         {
            String searchedword = rs.getString("searchedword");
            String tablename = rs.getString("tablename");
            if(!tableHashMapByWord.containsKey(searchedword))
            {
               tableHashMapByWord.put(searchedword, tablename);
            }
         }

      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }
      finally
      {
         try
         {
            if(rs!=null)
            {
               rs.close();
            }
         }
         catch (SQLException e)
         {
            e.printStackTrace();
         }
      }
   }
   
   
   public void updateUserTableHashMap(HashMap<String,String> tableHashMapByWord)
   {
      ResultSet rs = null;
      try
      {
         Statement stmt = conn.createStatement();
         rs = stmt.executeQuery("SELECT * FROM usertablemap");

         while (rs.next())
         {
            String userName = rs.getString("username");
            String tablename = rs.getString("tablename");
            if(!tableHashMapByWord.containsKey(userName))
            {
               tableHashMapByWord.put(userName, tablename);
            }
         }

      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }
      finally
      {
         try
         {
            if(rs!=null)
            {
               rs.close();
            }
         }
         catch (SQLException e)
         {
            e.printStackTrace();
         }
      }
   }   
   
   

   public void insertToTableMap(String searchedWord, String tableName)
   {
      try
      {
         PreparedStatement s;
         s = getConn().prepareStatement("INSERT INTO wordtablemap (searchedword, tablename) VALUES(?,?)");
         s.setString(1, searchedWord);
         s.setString(2, tableName);
         int count = s.executeUpdate();
         s.close();
         System.out.println(count + " rows were inserted to wordtablemap table.");

      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }
   }

   public void insertToUserTableMap(String userName, String tableName)
   {
      try
      {
         PreparedStatement s;
         s = getConn().prepareStatement("INSERT INTO usertablemap (username, tablename) VALUES(?,?)");
         s.setString(1, userName);
         s.setString(2, tableName);
         int count = s.executeUpdate();
         s.close();
         System.out.println(count + " rows were inserted to usertablemap table.");

      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }
   }   
   
   
   public void insertTweet(EmoTweet myTweet)
   {
      try
      {
         PreparedStatement s;
         s = getConn().prepareStatement(
               "INSERT INTO tweets_public_timeline " + "(tweet_id, user_name,user_id,user_followers,user_friends,user_location,user_description,tweet_text,tweet_retweeted) "
                     + "VALUES(?,?,?,?,?,?,?,?,?)");
         s.setLong(1, myTweet.getTweetID());
         s.setString(2, myTweet.getUserName());
         s.setLong(3, myTweet.getUserID());
         s.setLong(4, myTweet.getUserFollowers());
         s.setLong(5, myTweet.getUserFriends());
         s.setString(6, myTweet.getUserLocation());
         s.setString(7, myTweet.getDescription());
         s.setString(8, myTweet.getTweetText());
         s.setString(9, myTweet.isReTweet() ? "Y" : "N");
         int count = s.executeUpdate();
         s.close();
         System.out.println(count + " rows were inserted");

      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }
   }
   
   public ArrayList<EmoTweet> getAllTweetsFromTables(String tableName)
   {
      ArrayList<EmoTweet> tweetArrList = new ArrayList<EmoTweet>();
      ResultSet rs = null;
      try
      {
         Statement stmt = conn.createStatement();
         rs = stmt.executeQuery("SELECT * FROM "+tableName);
         while (rs.next())
         {
            EmoTweet rowEmoTweet = new EmoTweet();
            rowEmoTweet.setTweetID(Long.parseLong(rs.getString("tweet_id")));
            rowEmoTweet.setUserID(Integer.parseInt(rs.getString("fromuser_id")));
            rowEmoTweet.setToUserID(Integer.parseInt(rs.getString("touser_id")));
            rowEmoTweet.setUserName(rs.getString("fromuser_name"));
            rowEmoTweet.setTweetText(rs.getString("tweet_text"));
            rowEmoTweet.setToUserName(rs.getString("touser_name"));
            rowEmoTweet.setSource(rs.getString("source"));
            rowEmoTweet.setTweetDate(rs.getDate("date"));
            rowEmoTweet.setTweetTime(rs.getTime("date"));
            rowEmoTweet.setImageUrl(rs.getString("imageurl"));
            tweetArrList.add(rowEmoTweet);
         }

      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }
      finally
      {
         try
         {
            if(rs!=null)
            {
               rs.close();
            }
         }
         catch (SQLException e)
         {
            e.printStackTrace();
         }
      }
      return tweetArrList;
   }   
}
