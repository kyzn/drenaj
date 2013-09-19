import java.util.*;
import java.sql.*;

import twitter4j.*;

class Main 
{
	public static void main(String[] args) 
	{
		try 
        {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/toptwitterusers" , "root" ,"db_sifresi");
			
			String username = "twitter_kullanýcý_adý";
			String password = "twitter_sifresi";

			Twitter twitter = new Twitter(username,password);
				
			Statement stmt = conn.createStatement();
			
			Statement selectDataToProcess = conn.createStatement( );
			selectDataToProcess.executeQuery("select * from users_to_analyse");
			ResultSet rsDataToProcess = selectDataToProcess.getResultSet( );
			
			while (rsDataToProcess.next ( ))  //user loop
			{
				String usernameToCollect = rsDataToProcess.getString("user_name");
				for(int pageCount = 1; pageCount <= 32; pageCount++)  //page loop
				{
					Paging page = new Paging(pageCount,100); //3200 is the pagination limit of statuses
					List<Status> userStatuses = twitter.getUserTimeline(usernameToCollect, page);
					for (Status userStatus : userStatuses)    //status loop
					{
						try 
						{
							java.sql.Timestamp tweetTimestamp = new java.sql.Timestamp(userStatus.getCreatedAt().getTime());
							String tweetText = userStatus.getText();
							tweetText = tweetText.replace("'", "\\'");
							tweetText = tweetText.replace("&quot;", "\"");
							tweetText = tweetText.replace("&amp;", "&");
							tweetText = tweetText.replace("&gt;", ">");
							tweetText = tweetText.replace("&lt;", "<");
		   
		    				stmt.executeUpdate("INSERT INTO link_tweet_raw (tweet_id,user_id,user_name,text,time_stamp) VALUES ('"+String.valueOf(userStatus.getId())+"','"+String.valueOf(userStatus.getUser().getId())+"','"+String.valueOf(userStatus.getUser().getScreenName())+"','"+tweetText+"','"+tweetTimestamp+"')");
							System.out.println("Row inserted\n");
						}
						catch (Exception ex) 
					    { 
					    	System.out.println("Exception: " + ex.getMessage()); 
					    }
						
					}
				}
			}
			rsDataToProcess.close();
			selectDataToProcess.close();
			stmt.close();
			conn.close();
        } 
		catch (Exception ex) 
		{
            System.out.println("Exception: " + ex.getMessage());
        }      
	


		
		/*Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
		
		try
		{
			String username = "twitter_kullanýcý_adý";
			String password = "twitter_sifresi";

			Twitter twitter = new Twitter(username,password);
			
			String searchQuery = "http:// OR https:// OR www.";
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			PrintWriter printWriter = null;

			
			try
			{
	            conn = DriverManager.getConnection("jdbc:mysql://localhost/twitter" , "root" ,"db_sifre");
	            Calendar cal = new GregorianCalendar();
	            String logFileName = "logFile"+cal.get(Calendar.DAY_OF_MONTH)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.YEAR)+".dat";
	            printWriter = new PrintWriter(new FileWriter(new File(logFileName), true));

	           
	            try 
	            {
	            	Query linkQuery = new Query();
	    			linkQuery.setLang("EN");
	    			linkQuery.setQuery(searchQuery);
	    			
	    			int limit = twitter.rateLimitStatus().getRemainingHits();
	    			
	    			Statement selectLastTweetId = conn.createStatement( );
	    			selectLastTweetId.executeQuery("select tweet_id from link_tweet_raw order by tweet_id desc LIMIT 1");
	    			ResultSet rsLastTweetId = selectLastTweetId.getResultSet( );
	    			long lastTweetId = 0;
	    			while (rsLastTweetId.next ( ))
	    			{
	    				lastTweetId = rsLastTweetId.getLong("tweet_id");
	    			}
	    			rsLastTweetId.close ( ); 
	    			selectLastTweetId.close ( ); 
	    			
	    			
	    			while(limit>0)
	    			{
	    				linkQuery.setSinceId(lastTweetId);
	    				QueryResult linkResult = twitter.search(linkQuery);
	    				
	    				for (Tweet linkTweet : linkResult.getTweets()) 
	    				{
	    					try 
	    					{ 
	    						stmt = conn.createStatement();
	    						
	    						java.sql.Timestamp tweetTimestamp = new java.sql.Timestamp(linkTweet.getCreatedAt().getTime());
	    						String tweetText = linkTweet.getText();
	    						tweetText = tweetText.replace("'", "\\'");
	    						tweetText = tweetText.replace("&quot;", "\"");
	    						tweetText = tweetText.replace("&amp;", "&");
	    						tweetText = tweetText.replace("&gt;", ">");
	    						tweetText = tweetText.replace("&lt;", "<");
	   
	    						stmt.executeUpdate("INSERT INTO link_tweet_raw (tweet_id,user_id,user_name,text,time_stamp,search_query) VALUES ("+linkTweet.getId()+","+linkTweet.getFromUserId()+",'"+linkTweet.getFromUser()+"','"+tweetText+"','"+tweetTimestamp+"','"+searchQuery+"')");
	    						System.out.println("Row inserted\n");
	    					}
	    				    catch (Exception ex) 
	    				    { 
	    				    	printWriter.println("Exception: " + ex.getMessage()); 
	    				    }
	    				}
	    				limit = twitter.rateLimitStatus().getRemainingHits();
	    				
	    				Statement selectLastTweetIdLoop = conn.createStatement( );
		    			selectLastTweetIdLoop.executeQuery("select tweet_id from link_tweet_raw order by tweet_id desc LIMIT 1");
		    			ResultSet rsLastTweetIdLoop = selectLastTweetIdLoop.getResultSet( );
		    			while (rsLastTweetIdLoop.next ( ))
		    			{
		    				lastTweetId = rsLastTweetIdLoop.getLong("tweet_id");
		    			}
		    			rsLastTweetIdLoop.close ( ); 
		    			selectLastTweetIdLoop.close ( ); 
	    				
	    				Thread.sleep(300000);  //wait for 5 min before next api request
	    				
	    				System.out.println("Remaining limit.."+limit+"\n");
	    				
	    			}
		
	            } 
	            finally 
	            {
	                if (rs != null) 
	                {
	                    try 
	                    {
	                        rs.close();
	                    } 
	                    catch (SQLException sqlEx) 
	                    {
	                    	printWriter.println("SQLException: " + sqlEx.getMessage());
	                    }
	                    rs = null;
	                }
	
	                if (stmt != null) 
	                {
	                    try 
	                    {
	                        stmt.close();
	                    } 
	                    catch (SQLException sqlEx) 
	                    {
	                    	printWriter.println("SQLException: " + sqlEx.getMessage());
	                    }
	
	                    stmt = null;
	                }
	
	                if (conn != null) 
	                {
	                    try 
	                    {
	                        conn.close();
	                    } 
	                    catch (SQLException sqlEx) 
	                    {
	                    }
	
	                    conn = null;
	                }
	            }
			}
            catch (SQLException ex) 
            {
            	printWriter.println("SQLException: " + ex.getMessage());
            	printWriter.println("SQLState: " + ex.getSQLState());
            	printWriter.println("VendorError: " + ex.getErrorCode());
            }
            finally
            {
              printWriter.close();
            }

            
		}
		catch (Exception ex) 
		{
            System.out.println("Exception: " + ex.getMessage());
        }*/

		
	}
}