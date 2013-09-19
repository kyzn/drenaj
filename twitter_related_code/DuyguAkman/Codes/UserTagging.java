import java.util.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import twitter4j.*;
import java.util.Collections;
import java.util.Calendar;
import java.text.SimpleDateFormat;

class Main 
{
	public static String dbName = "jdbc:mysql://localhost/toptwitterusers";
	public static String dbUser = "root";
	public static String dbPass = "db_sifresi";
	public static String user_id = "";
	public static String twitterUsername = "twitter_kullan�c�_ad�";
	public static String twitterPassword = "twitter_sifresi";
	public static Twitter twitter = new Twitter(twitterUsername,twitterPassword);
	public static ArrayList<String> stopwordsList = new ArrayList<String> ();
	public static ArrayList<String> stopwordsTwitterList = new ArrayList<String> ();
	public static FileOutputStream outErrorLog;
	public static PrintStream pErrorLog;
	
	public static void main(String[] args) 
	{
		try
		{
		    initialize();
			
			//user input from keyboard
			//String userId;
		    //System.out.println("Write the id of the user:");
		    //Scanner in = new Scanner(System.in);
		    //userId = in.nextLine();
		    //Main.user_id = userId; 
		    //System.out.println("Start "+Calendar.getInstance().getTime());
		    //copyRawData();
		    //System.out.println("copyRawData finished "+Calendar.getInstance().getTime());
		    //splitAnalysisTable();
		    //System.out.println("splitAnalysisTable finished "+Calendar.getInstance().getTime());
		    //gatherLinkDataAboutUser();
		    //System.out.println("gatherLinkDataAboutUser finished "+Calendar.getInstance().getTime());
		    //gatherDataAboutUser();
		    //System.out.println("gatherDataAboutUser finished "+Calendar.getInstance().getTime());
		    //categorizeUsers();
		    //System.out.println("categorizeUsers finished "+Calendar.getInstance().getTime());
		    //twitterStopwords();
		    //System.out.println("twitterStopwords finished "+Calendar.getInstance().getTime());
		    //tagUsers();
		    //System.out.println("tagUsers finished "+Calendar.getInstance().getTime());
			
			Connection conn = DriverManager.getConnection(dbName , dbUser ,dbPass);
			
			Statement selectDataToProcess = conn.createStatement( );
			selectDataToProcess.executeQuery("select user_id, count(1) as xcount from link_tweet_raw group by user_id order by xcount desc");
			ResultSet rsDataToProcess = selectDataToProcess.getResultSet( );
			
			while (rsDataToProcess.next ( ))
			{
				String userId = rsDataToProcess.getString("user_id"); 
				Main.user_id = userId; 
				System.out.println("\nUser: "+user_id);
				pErrorLog.println("\nUser: "+user_id);
			    System.out.println("Start "+Calendar.getInstance().getTime());
				//copyRawData();
			    //System.out.println("copyRawData finished "+Calendar.getInstance().getTime());
				//splitAnalysisTable();
			    //System.out.println("splitAnalysisTable finished "+Calendar.getInstance().getTime());
				//gatherLinkDataAboutUser();
			    //System.out.println("gatherLinkDataAboutUser finished "+Calendar.getInstance().getTime());
			    //gatherDataAboutUser();
			    //System.out.println("gatherDataAboutUser finished "+Calendar.getInstance().getTime());
			    //categorizeUsers();
			    //System.out.println("categorizeUsers finished "+Calendar.getInstance().getTime());
			    twitterStopwords();
			    System.out.println("twitterStopwords finished "+Calendar.getInstance().getTime());
			    tagUsers();
			    System.out.println("tagUsers finished "+Calendar.getInstance().getTime());
			} 
			
			rsDataToProcess.close();
			selectDataToProcess.close();
			conn.close();
		}
		catch (Exception ex) 
		{
			pErrorLog.println("Exception in Main: " + ex.getMessage());
        }
	}
	
	static void copyRawData()
	{
		try
		{
			Connection conn = DriverManager.getConnection(dbName , dbUser ,dbPass);
			
			Statement selectDataToProcess = conn.createStatement( );
			selectDataToProcess.executeQuery("select * from link_tweet_raw where user_id = \'"+user_id+"\'");
			ResultSet rsDataToProcess = selectDataToProcess.getResultSet( );
			
			Statement clearAnalysisTable = conn.createStatement( );
			clearAnalysisTable.executeUpdate("truncate table link_tweet_analysis");
			clearAnalysisTable.close();
			
			//Statement selectUserName = conn.createStatement( );
			//selectUserName.executeQuery("select distinct user_name from link_tweet_raw where user_id = \'"+user_id+"\'");
			//ResultSet rsUserNameToProcess = selectUserName.getResultSet( );
			//String userName = "";
			//while (rsUserNameToProcess.next ( ))
			//{
				//userName = rsUserNameToProcess.getString("user_name");
			//}
			//ExtendedUser twitterUser = twitter.getUserDetail(userName);
			//int userId = twitterUser.getId();
			//userId = Integer.parseInt(Main.user_id);  //i add this line because in topusers scenario, both ids are from rest api, therefore search user id - rest user id conversion is unnecessary. delete this line when the conversion is necessary
			int userId = Integer.parseInt(Main.user_id);
			
			Statement clearUserCross = conn.createStatement( );
			clearUserCross.executeUpdate("delete from user_id_cross where user_id_search = \'"+user_id+"\'");
			clearUserCross.close();

			Statement insertUserCross = conn.createStatement( );
			insertUserCross.executeUpdate("insert into user_id_cross(user_id_search, user_id_rest) values (\'"+user_id+"\',\'"+userId+"\')");
			insertUserCross.close();
			
			Main.user_id = String.valueOf(userId); 
			
			while (rsDataToProcess.next ( ))
			{
				String tweetId = rsDataToProcess.getString("tweet_id"); 
				String text = rsDataToProcess.getString("text"); 
				text = text.replace("'", "\\'");
				Statement copyStatement = conn.createStatement( );
				copyStatement.executeUpdate("INSERT INTO link_tweet_analysis(tweet_id,user_id,text) VALUES('"+tweetId+"','"+userId+"','"+text+"')");
				copyStatement.close();
			}
			rsDataToProcess.close();
			selectDataToProcess.close();
			conn.close();
			System.out.println("Raw Data Copied");
		}
		catch (Exception ex) 
		{
			pErrorLog.println("Exception in copyRawData: " + ex.getMessage());
        }
		
	}
	
	static void splitAnalysisTable()
	{
		try
		{
			Connection conn = DriverManager.getConnection(dbName , dbUser ,dbPass);
			
			Statement clearTagsTable = conn.createStatement( );
			clearTagsTable.executeUpdate("delete from users_hashtags where user_id = \'"+user_id+"\'");
			clearTagsTable.executeUpdate("delete from users_plain_texts where user_id = \'"+user_id+"\'");
			clearTagsTable.executeUpdate("delete from users_link_texts where user_id = \'"+user_id+"\'");
			clearTagsTable.close();
			
			Statement selectDataToProcess = conn.createStatement( );
			selectDataToProcess.executeQuery("select * from link_tweet_analysis");
			ResultSet rsDataToProcess = selectDataToProcess.getResultSet( );
			
			while (rsDataToProcess.next ( ))
			{
				String tweetText = rsDataToProcess.getString("text"); 
				String tweetId = rsDataToProcess.getString("tweet_id");
				String links = "";
				String nonlinks = "";
				
				String[] words = tweetText.trim().split("\\s+");
				
				for(int i=0;i<words.length;i++)
				{
					if(words[i].startsWith("http") || words[i].startsWith("www") || words[i].startsWith("HTTP") || words[i].startsWith("WWW"))
					{
						links = links + words[i] + " ";
					}
					else
					{
						nonlinks = nonlinks + words[i] + " ";
					}
				}
				
				nonlinks = nonlinks.trim();
				nonlinks = stripPunctuation(nonlinks);
				String[] nonlinkswords = nonlinks.split("\\s+");
				String mentions = "";
				String hashtags = "";
				String retweets = "";
				nonlinks = "";
				Integer nonlinksStopwordsCount = 0;
				
				for(int i=0;i<nonlinkswords.length;i++)
				{
					if(nonlinkswords[i].startsWith("@"))
					{
						mentions = mentions + nonlinkswords[i] + " ";
					}
					else if(nonlinkswords[i].startsWith("#"))
					{
						hashtags = hashtags + nonlinkswords[i] + " ";
					}
					else if(nonlinkswords[i].toUpperCase().equals("RT") || nonlinkswords[i].toUpperCase().equals("RETWEET"))
					{
						retweets = retweets + nonlinkswords[i] + " ";
					}
					else
					{
						nonlinks = nonlinks + nonlinkswords[i] + " ";
						nonlinksStopwordsCount++;
					}
				}
				
				links = links.trim();
				mentions = mentions.trim();
				hashtags = hashtags.trim();
				nonlinks = nonlinks.trim();
				retweets = retweets.trim();
				links = links.replace("'", "\\'");
				nonlinks = nonlinks.replace("'", "\\'");
				mentions = mentions.replace("'", "\\'");
				hashtags = hashtags.replace("'", "\\'");
				retweets = retweets.replace("'", "\\'");
				
				String[] hashtagsList = hashtags.split("\\s+");			
				Statement insertHashtagsStatement = conn.createStatement( );
				
				for(int j=0; j<hashtagsList.length;j++)
				{
					Thread.sleep(1);
					String hashtagToInsert = hashtagsList[j];
					hashtagToInsert = hashtagToInsert.trim();	
					if(hashtagToInsert.length() == 0)
					{
						continue;
					}
					
					insertHashtagsStatement.executeUpdate("INSERT INTO users_hashtags(user_id,hashtag) VALUES('"+Main.user_id+"','"+hashtagToInsert.toUpperCase(Locale.US)+"')");
				}
				
				insertHashtagsStatement.close();
				
				String[] nonlinksList = nonlinks.split("\\s+");	
				Statement insertNonlinkStatement = conn.createStatement( );
				
				for(int j=0; j<nonlinksList.length;j++)
				{
					Thread.sleep(1);
					String nonlinkToInsert = nonlinksList[j];
					nonlinkToInsert = nonlinkToInsert.trim();
					if(nonlinkToInsert.length() == 0)
					{
						continue;
					}
					int index = Collections.<String>binarySearch(stopwordsList, nonlinkToInsert.toUpperCase(Locale.US));
					if(index >= 0)
					{
						continue;
					}
					
					if(links.length() > 0)
					{
						insertNonlinkStatement.executeUpdate("INSERT INTO users_link_texts(user_id,text) VALUES('"+Main.user_id+"','"+nonlinkToInsert.toUpperCase(Locale.US)+"')");
					}
					else
					{
						insertNonlinkStatement.executeUpdate("INSERT INTO users_plain_texts(user_id,text) VALUES('"+Main.user_id+"','"+nonlinkToInsert.toUpperCase(Locale.US)+"')");
					}
				}
				
				insertNonlinkStatement.close();
				
				Statement updateProcessedData = conn.createStatement( );
				updateProcessedData.executeUpdate("UPDATE link_tweet_analysis SET links = '" + links + "', nonlinks = '" + nonlinks + "', mentions = '" + mentions + "', hashtags = '" + hashtags + "', retweets = '" + retweets + "', nonlinks_count = "+nonlinksStopwordsCount+" where tweet_id = '"+tweetId+"'");
				updateProcessedData.close();
			}
			rsDataToProcess.close();
			selectDataToProcess.close();
			conn.close();
			
			System.out.println("Analsis Table Splitted");
		}
		catch (Exception ex) 
		{
			pErrorLog.println("Exception in splitAnalysisTable: " + ex.getMessage());
        }
	}
	
	static void gatherDataAboutUser()
	{
		try
		{
			int numberOfDistinctLinks = 0;
			int numberOfDistinctDomains = 0;
			int numberOfTotalTweets = 0;
			
			Connection conn = DriverManager.getConnection(dbName , dbUser , dbPass);
			
			Statement clearUsersTable = conn.createStatement( );
			clearUsersTable.executeUpdate("delete from users where user_id = \'"+user_id+"\'");
			clearUsersTable.executeUpdate("delete from users_friends where user_id = \'"+user_id+"\'");
			clearUsersTable.close();
			
			
			String screenName = "";
			int followerCount = 0;
			int friendCount = 0;
			double followerFollowingRatio = 0.0;
			int statusesCount = 0;
			java.sql.Timestamp userCreateDate = new Timestamp((new java.util.Date()).getTime());
			java.sql.Timestamp lastStatusDate = new Timestamp((new java.util.Date()).getTime());
			long deltaT = 0;
			double avgUpdates = 0.0;
			
			
			try
			{
				User user = twitter.showUser(user_id);
				screenName = user.getScreenName();
				followerCount = user.getFollowersCount();
				friendCount = user.getFriendsCount();
				followerFollowingRatio = ((double)followerCount / (double)friendCount);
				statusesCount = user.getStatusesCount();
				userCreateDate = new java.sql.Timestamp(user.getCreatedAt().getTime());
				lastStatusDate = new java.sql.Timestamp(user.getStatusCreatedAt().getTime());
				deltaT = lastStatusDate.getTime() - userCreateDate.getTime();
				deltaT = deltaT / 86400000; //daily frequency
				avgUpdates = ((double)statusesCount / (double)deltaT);
			}
			catch (Exception ex) 
			{
				pErrorLog.println("Exception in gatherDataAboutUser1: " + ex.getMessage());
	        }
			
			
			Statement selectDataToProcess = conn.createStatement( );
			selectDataToProcess.executeQuery("select count(1) from link_tweet_analysis");
			ResultSet rsDataToProcess = selectDataToProcess.getResultSet( );
			while (rsDataToProcess.next ( ))
			{
    			try
    			{
    				numberOfTotalTweets = rsDataToProcess.getInt(1);
    			}
    			catch (Exception e) 
				{
    				pErrorLog.println("Exception in gatherDataAboutUser2: " + e.getMessage());
	    	    }
			}
			
			
			selectDataToProcess.executeQuery("select count(distinct links) from link_tweet_analysis where length(links) > 0");
			rsDataToProcess = selectDataToProcess.getResultSet( );
			while (rsDataToProcess.next ( ))
			{
    			try
    			{
    				numberOfDistinctLinks = rsDataToProcess.getInt(1);
    			}
    			catch (Exception e) 
				{
    				pErrorLog.println("Exception in gatherDataAboutUser3: " + e.getMessage());
	    	    }
			}
			
			selectDataToProcess.executeQuery("select count(distinct domains) from users_links where user_id ='"+user_id+"' and length(domains) > 0");
			rsDataToProcess = selectDataToProcess.getResultSet( );
			while (rsDataToProcess.next ( ))
			{
    			try
    			{
    				numberOfDistinctDomains = rsDataToProcess.getInt(1);
    			}
    			catch (Exception e) 
				{
    				pErrorLog.println("Exception in gatherDataAboutUser4: " + e.getMessage());
	    	    }
			}
			
			Statement insertUsers = conn.createStatement( );
			insertUsers.executeUpdate("insert into users(user_id, total_tweet_count,distinct_links_count,distinct_domains_count,number_of_followers,number_of_following,follower_following_ratio,created_at,statuses_count,last_status_created_at,update_frequency,screen_name) values ("+user_id+","+numberOfTotalTweets+","+numberOfDistinctLinks+","+numberOfDistinctDomains+","+followerCount+","+friendCount+","+followerFollowingRatio+",'"+userCreateDate+"',"+statusesCount+",'"+lastStatusDate+"',"+avgUpdates+",'"+screenName+"')");
			insertUsers.close();
			
			
			IDs friendIDs = twitter.getFriendsIDs(screenName);
			int[] friendIDList = friendIDs.getIDs();
			Statement insertFriendIDs = conn.createStatement( );
			for(int i=0;i<friendIDList.length;i++)
			{
				int friend_id = friendIDList[i];
				insertFriendIDs.executeUpdate("insert into users_friends(user_id, friend_id) values ("+user_id+",\'"+friend_id+"\')");
			}
			insertFriendIDs.close();
			
			
			System.out.println("User Data Gathered");
			
			rsDataToProcess.close();
			selectDataToProcess.close();
			conn.close();		
		}
		catch (Exception ex) 
		{
			pErrorLog.println("Exception in gatherDataAboutUser5: " + ex.getMessage());
        }
	}
	
	static void gatherLinkDataAboutUser()
	{
		try
		{
			Connection conn = DriverManager.getConnection(dbName , dbUser , dbPass);
			
			Statement clearUsersTable = conn.createStatement( );
			clearUsersTable.executeUpdate("delete from users_links where user_id = \'"+user_id+"\'");
			clearUsersTable.executeUpdate("delete from users_links_titles where user_id = \'"+user_id+"\'");
			clearUsersTable.close();
			
			int numberOfTotalTweets = 0;
			Statement selectDataToProcess = conn.createStatement( );
			selectDataToProcess.executeQuery("select count(1) from link_tweet_analysis");
			ResultSet rsDataToProcess = selectDataToProcess.getResultSet( );
			while (rsDataToProcess.next ( ))
			{
    			try
    			{
    				numberOfTotalTweets = rsDataToProcess.getInt(1);
    			}
    			catch (Exception e) 
				{
    				pErrorLog.println("Exception in gatherLinkDataAboutUser1: " + e.getMessage());
	    	    }
			}
			
			selectDataToProcess.executeQuery("select links, count(1) as xcount from link_tweet_analysis where length(links) > 0 group by links order by xcount desc");
			rsDataToProcess = selectDataToProcess.getResultSet( );
			while (rsDataToProcess.next ( ))
			{
    			
				String distinctLink = rsDataToProcess.getString("links");
				int distinctLinkCount = Integer.parseInt(rsDataToProcess.getString("xcount"));
				double distinctLinkRatio = ((double)distinctLinkCount / (double)numberOfTotalTweets) * 100;
				
				String[] longUrlResult = getLongURL(distinctLink);
				String longUrl = longUrlResult[0];
				String title = longUrlResult[1];
				String domain = "";
				title = title.replace("&#038;", "&");
				title = title.replace("&#38;", "&");
				title = title.replace("&#039;", "'");
				title = title.replace("&#39;", "'");
				title = title.replace("&#62;", ">");
				title = title.replace("&#124;", "|");
				title = title.replace("&#8211;", "�");
				title = title.replace("&#8212;", "�");
				title = title.replace("&#8216;", "�");
				title = title.replace("&#8217;", "'");
				title = title.replace("&#8220;", "�");
				title = title.replace("&#8221;", "�");
				title = title.replace("&#8230;", "�");
				
				String linkTitle = stripPunctuation(title);
				String[] tags = linkTitle.trim().split("\\s+");
				Statement insertTitleStatement = conn.createStatement( );
				
				for(int j=0; j<tags.length;j++)
				{
					Thread.sleep(1);
					String tagToInsert = tags[j];
					tagToInsert = tagToInsert.trim();
					if(tagToInsert.length() == 0)
					{
						continue;
					}
					
					int index = Collections.<String>binarySearch(stopwordsList, tagToInsert.toUpperCase(Locale.US));
					if(index >= 0)
					{
						continue;
					}
					
					tagToInsert = tagToInsert.replace("'", "\\'");
					insertTitleStatement.executeUpdate("INSERT INTO users_links_titles(user_id,tag,tag_count) VALUES('"+Main.user_id+"','"+tagToInsert.toUpperCase(Locale.US)+"',"+distinctLinkCount+")");
				}
				
				insertTitleStatement.close();
			
				longUrl = longUrl.trim();
				title = title.trim();
				
				
				String[] domains = longUrl.split("/");
				if(longUrl.startsWith("http") || longUrl.startsWith("HTTP"))
				{
					domain = domains[2].toString();
				}
				else if(longUrl.startsWith("www") || longUrl.startsWith("WWW"))
				{
					domain = domains[0].toString();
				}
							
				longUrl = longUrl.replace("'", "\\'");
				title = title.replace("'", "\\'");
				domain = domain.replace("'", "\\'");
				
				Statement insertUsersLinks = conn.createStatement( );
				try
				{
					insertUsersLinks.executeUpdate("insert into users_links(user_id, distinct_links, percentage, domains, long_url, title) values ("+user_id+",\'"+distinctLink+"\',"+distinctLinkRatio+",\'"+domain+"\',\'"+longUrl+"\',\'"+title+"')");
				}
				catch (Exception ex) 
				{
					pErrorLog.println("Exception in gatherLinkDataAboutUser2: " + ex.getMessage());
					insertUsersLinks.close();
		        }
				insertUsersLinks.close();
			}
			
			System.out.println("User Link Data Gathered");
			
			rsDataToProcess.close();
			selectDataToProcess.close();
			conn.close();
		}
		catch (Exception ex) 
		{
			pErrorLog.println("Exception in gatherLinkDataAboutUser3: " + ex.getMessage());
        }
		
	}
	
	static void categorizeUsers()
	{
		double automatedThreshold = 80.0;
		double distinct_domain_threshold = 50.0;
		double followerFollowingRatioThreshold = 100.0;
		try
		{
			Connection conn = DriverManager.getConnection(dbName , dbUser , dbPass);
			
			Statement selectUpdateFrequency = conn.createStatement( );
			selectUpdateFrequency.executeQuery("select * from users where user_id = '"+user_id+"'");
			ResultSet rsUpdateFrequency = selectUpdateFrequency.getResultSet( );
			
			double update_frequency = 0.0;
			double follower_following_ratio = 0.0;
			while (rsUpdateFrequency.next ( ))
			{
    			try
    			{
    				update_frequency = Double.parseDouble(rsUpdateFrequency.getString("update_frequency"));
    				follower_following_ratio = Double.parseDouble(rsUpdateFrequency.getString("follower_following_ratio"));
    			}
    			catch (Exception e) 
				{
    				pErrorLog.println("Exception in categorizeUsers1: " + e.getMessage());
	    	    }
			}
			
			rsUpdateFrequency.close();
			selectUpdateFrequency.close();
			
			String category = "";
			String category_method = "";
			
			Statement updateUserData = conn.createStatement( );
			
			if(update_frequency > automatedThreshold)  //categorize acc to update_frequency
			{	
				category = "automated";
				category_method += "update frequency "; 		
			}
			
			Statement selectUsersDomainsToProcess = conn.createStatement( );
			selectUsersDomainsToProcess.executeQuery("select domains, sum(percentage) as percentage from users_links where user_id = '"+user_id+"' group by domains having percentage > "+distinct_domain_threshold);
			ResultSet rsUsersDomainsToProcess = selectUsersDomainsToProcess.getResultSet( );
			int countUsersDomainsToProcess = 0;

			while (rsUsersDomainsToProcess.next ( ))
			{
				countUsersDomainsToProcess++;
			}
			rsUsersDomainsToProcess.close();
			selectUsersDomainsToProcess.close();
			
			if(countUsersDomainsToProcess > 0)  //categorize acc to distinct domain frequency
			{	
				category = "automated";
				category_method += "distinct domain frequency";		
			}
			
			if(category.equals("automated"))
			{
				if(follower_following_ratio > followerFollowingRatioThreshold)
				{
					category = "automated - bot";
				}
				else if(follower_following_ratio < (1 / followerFollowingRatioThreshold))
				{
					category = "automated - spam";
				}
			}
			
			updateUserData.executeUpdate("UPDATE users SET category = '"+category+"', category_method = '"+category_method+"' where user_id = '"+user_id+"'");
			updateUserData.close();
		    
			conn.close();
			
    		System.out.println("User Categorized");
		}
		catch (Exception ex) 
		{
			pErrorLog.println("Exception in categorizeUsers: " + ex.getMessage());
		}
	}
	
	static void twitterStopwords()
	{
		try
		{
			int twitterStopwordsThreshold = 50;
			
			Connection conn = DriverManager.getConnection(dbName , dbUser , dbPass);
			
			Statement clearUsersTable = conn.createStatement( );
			clearUsersTable.executeUpdate("truncate table stopwords_twitter");
			clearUsersTable.close();
			
			Statement insertStopwordsStatement = conn.createStatement( );
			insertStopwordsStatement.executeUpdate("insert into stopwords_twitter(words) select text from (select text, sum(count) as count from (select text, count(distinct user_id) as count from users_link_texts group by text union select tag as text, count(distinct user_id) as count from users_links_titles group by tag union select text, count(distinct user_id) as count from users_plain_texts group by text) as frequent group by text order by 2 desc limit "+twitterStopwordsThreshold+") as frequentTable");
			insertStopwordsStatement.close();
			System.out.println("Twitter Stopwords table constructed.");
			
			
			Statement selectDataToProcess = conn.createStatement( );
			selectDataToProcess.executeQuery("select * from stopwords_twitter");
			ResultSet rsDataToProcess = selectDataToProcess.getResultSet( );
			
			
			while (rsDataToProcess.next ( ))
			{
				String stopword = rsDataToProcess.getString("words");
				stopwordsTwitterList.add(stopword);
			}
			
			Collections.sort(stopwordsTwitterList);

			selectDataToProcess.close();
			rsDataToProcess.close();
			
			conn.close();
		}
		catch (Exception ex) 
		{
			pErrorLog.println("twitterStopwords: " + ex.getMessage());
        }
		
	}
	
	static void tagUsers()
	{
		try
		{
			int tagCloudThreshold = 50;
			double hashtagWeightMultiplier = 1.0;
			
			Connection conn = DriverManager.getConnection(dbName , dbUser , dbPass);
			
			FileOutputStream out;
			PrintStream p;
			String inputFileName = Main.user_id + ".txt";
			String outputFileName = Main.user_id + ".png";
			File file = new File(inputFileName);
			file.delete();
			file = new File(outputFileName);
			file.delete();
			out = new FileOutputStream(inputFileName); 
			p = new PrintStream( out ); 
			
			Statement clearUsersTable = conn.createStatement( );
			clearUsersTable.executeUpdate("delete from users_tag_clouds where user_id = \'"+user_id+"\'");
			clearUsersTable.close();
			
			Statement insertCloudStatement = conn.createStatement( );
			insertCloudStatement.executeUpdate("insert into users_tag_clouds(user_id, tag, weight) (select user_id,text as tag, count(1) as weight from users_link_texts where user_id = '"+Main.user_id+"' group by text)");
			insertCloudStatement.executeUpdate("insert into users_tag_clouds(user_id, tag, weight) (select user_id, tag, sum(tag_count) as weight from users_links_titles where user_id = '"+Main.user_id+"' group by tag)");
			insertCloudStatement.executeUpdate("insert into users_tag_clouds(user_id, tag, weight) (select user_id, text as tag, count(1) as weight from users_plain_texts where user_id = '"+Main.user_id+"' group by text)");
			insertCloudStatement.close();
			
			Statement selectDataToProcess = conn.createStatement( );
			selectDataToProcess.executeQuery("select tag, sum(weight) as weight from users_tag_clouds where user_id = '"+Main.user_id+"' group by tag order by 2 desc limit "+tagCloudThreshold);
			ResultSet rsDataToProcess = selectDataToProcess.getResultSet( );
			
			String tag = "";
			double weight = 0.0;
			String color = "660066"; 
			while (rsDataToProcess.next ( ))
			{
    			try
    			{
    				tag = rsDataToProcess.getString("tag");
    				weight = Double.parseDouble(rsDataToProcess.getString("weight"));
    				
    				int index = Collections.<String>binarySearch(stopwordsTwitterList, tag.toUpperCase(Locale.US));
					if(index >= 0)
					{
						continue;
					}
					
					
    				p.println (tag+"\t"+weight+"\t"+color);
    			}
    			catch (Exception e) 
				{
    				pErrorLog.println("Exception in tagUsers1: " + e.getMessage());
	    	    }
			}
			
			selectDataToProcess.executeQuery("select hashtag as tag, count(1) as weight from users_hashtags where user_id = '"+Main.user_id+"' group by hashtag");
			rsDataToProcess = selectDataToProcess.getResultSet( );
			
			color = "CC0033"; 
			while (rsDataToProcess.next ( ))
			{
    			try
    			{
    				tag = rsDataToProcess.getString("tag");
    				weight = Double.parseDouble(rsDataToProcess.getString("weight"));
    				weight = weight * hashtagWeightMultiplier;
    				p.println (tag+"\t"+weight+"\t"+color);
    			}
    			catch (Exception e) 
				{
    				pErrorLog.println("Exception in tagUsers1: " + e.getMessage());
	    	    }
			}
			
			Runtime rt = Runtime.getRuntime();
			rt.exec("cmd.exe /k java -jar ibm-word-cloud.jar -c configuration.txt -w 1280 -h 800 < "+inputFileName+" > "+outputFileName);
			
			rsDataToProcess.close();
			selectDataToProcess.close();
			p.close();
			out.close();
			
		}
		catch (Exception ex) 
		{
			pErrorLog.println("Exception in tagUsers2: " + ex.getMessage());
        }
	}

	static void initialize()
	{
		try
		{
			//load stopwords into memory
			Connection conn = DriverManager.getConnection(dbName , dbUser ,dbPass);
			
			Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		    String outputFileName =  sdf.format(cal.getTime())+".txt";
			outErrorLog = new FileOutputStream(outputFileName); 
			pErrorLog = new PrintStream( outErrorLog ); 
			
			Statement selectDataToProcess = conn.createStatement( );
			selectDataToProcess.executeQuery("select * from stopwords");
			ResultSet rsDataToProcess = selectDataToProcess.getResultSet( );
			
			
			while (rsDataToProcess.next ( ))
			{
				String stopword = rsDataToProcess.getString("words");
				stopwordsList.add(stopword);
			}
			
			stopwordsList.add("ERROR");
			stopwordsList.add("ERRORERROR");
			
			Collections.sort(stopwordsList);

			selectDataToProcess.close();
			rsDataToProcess.close();
			
			conn.close();
		}
		catch (Exception ex) 
		{
			pErrorLog.println("Exception in initialize: " + ex.getMessage());
        }
	}
	
	static String stripPunctuation(String s) 
	{
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < s.length(); i++) 
		{
			if ((s.charAt(i) >= 0 && s.charAt(i) <= 32) || (s.charAt(i) == 35) || (s.charAt(i) == 38) || (s.charAt(i) == 39) || (s.charAt(i) >= 48 && s.charAt(i) <= 57) ||  (s.charAt(i) >= 64 && s.charAt(i) <= 90) || (s.charAt(i) == 95) || (s.charAt(i) >= 97 &&
			s.charAt(i) <= 122)) 
			{
				sb = sb.append(s.charAt(i));
			}
			else
			{
				sb = sb.append(' ');
			}
		}

		return sb.toString();
	}
	
	static String[] getLongURL(String shortUrl)
	{
		String[] result = new String[2];
		try
		{
			if(shortUrl.indexOf(" ")>-1)
			{
				result[0] = "ERRORERROR";
				result[1] = "ERRORERROR";
				return result;
			}
			URL url;
			
			String longUrl = "";
			String title = "";
	        while(longUrl == "")
	        {
				String line; 
	    	    String respond = ""; 
	    	    HttpURLConnection httpConn;
				url = new URL("http://api.longurl.org/v1/expand?url="+shortUrl);
	    	    httpConn = (HttpURLConnection) url.openConnection();
	    	    httpConn.setRequestMethod("GET");
	    	    BufferedReader rd = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
	    	    while ((line = rd.readLine()) != null) {
	    	    	respond += line;
	    	    }
	    	    
	    	    int longUrlStartIndex = respond.indexOf("<long_url>");
	    	    int longUrlFinishIndex = respond.indexOf("</long_url>");
	    	    if(longUrlStartIndex > 0 && longUrlFinishIndex > 0 && longUrlFinishIndex > longUrlStartIndex)
	    	    {
	    	       longUrl += respond.substring(longUrlStartIndex+19,longUrlFinishIndex-3);
	    	    }
	    	    else
	    	    {
	    	       longUrl += "ERRORERROR"; //if longUrl cannot parse the short url, ERROR is returned   
	    	    }
	    	    
	    	    int titleStartIndex = respond.indexOf("<title>");
	    	    int titleFinishIndex = respond.indexOf("</title>");
	    	    if(titleStartIndex > 0 && titleFinishIndex > 0 && titleFinishIndex > titleStartIndex)
	    	    {
	    	       title += respond.substring(titleStartIndex+16,titleFinishIndex-3);
	    	    }
	    	    else
	    	    {
	    	       title += "ERRORERROR"; //if longUrl cannot parse the title, ERROR is returned   
	    	    }
	    	    
	    	    httpConn.disconnect();
				rd.close();
	        }
			result[0] = longUrl;
			title = title.replace("&quot;", "\"");
			title = title.replace("&amp;", "&");
			title = title.replace("&gt;", ">");
			title = title.replace("&lt;", "<");
			result[1] = title;
			return result;
		}
		catch (Exception ex) 
		{
			pErrorLog.println("Exception in getLongURL: " + ex.getMessage());
            result[0] = "ERRORERROR";
			result[1] = "ERRORERROR";
			return result;
        }
	}


		
}