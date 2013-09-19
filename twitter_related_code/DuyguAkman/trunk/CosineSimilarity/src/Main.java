import java.io.*;
import java.sql.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import uk.ac.shef.wit.simmetrics.similaritymetrics.*;
import java.util.*;


//java -jar CosineSimilarity.jar dbname dbuser dbpass cosinetype limittype countlimit
//cosinetype: 1-unweighted, 2-weighted
//limittype: 1-unlimited, 2-countlimited
class Main 
{
	
	public static String dbName = "jdbc:mysql://localhost/";
	public static String dbUser = "";
	public static String dbPass = "";
	public static String user_id = "";
	public static String hashtag_id = "";
	public static FileOutputStream outErrorLog;
	public static PrintStream pErrorLog;
	
	public static void main(String[] args) 
	{
		try
		{
			initialize(args);
			
			int cosinetype = Integer.parseInt(args[3].toString());
			int limittype = Integer.parseInt(args[4].toString());
			int countLimit = Integer.parseInt(args[5].toString());
		    
		    Connection conn = DriverManager.getConnection(dbName , dbUser ,dbPass);
				
			Statement selectDataToProcess = conn.createStatement( );
			selectDataToProcess.executeQuery("select * from users_to_compare");
			ResultSet rsDataToProcess = selectDataToProcess.getResultSet( );
			
			while (rsDataToProcess.next ( ))
			{
				String usernameToAnalyse = rsDataToProcess.getString("user_name");
				Main.user_id = getUserIdFromUsername(usernameToAnalyse);
				
				pErrorLog.println("\n-------------------------------------------------");
				pErrorLog.println("\nUSER: "+user_id);
				pErrorLog.println("\t\tStart "+Calendar.getInstance().getTime());
				if(cosinetype == 1)
				{
					unweightedCoSim(limittype, countLimit);
				}
				else if(cosinetype == 2)
				{
					weightedCoSim(countLimit);
				}
				pErrorLog.println("\t\tcoSim finished "+Calendar.getInstance().getTime());
			} 
			
			rsDataToProcess.close();
			selectDataToProcess.close();
		    
		    
		    conn.close();
		}
		catch (Exception ex) 
		{
			pErrorLog.println("\tException in Main: " + ex.getMessage());
        }
	}
	
	static void initialize(String[] args)
	{
		try
		{
			//Take the name of the database from command line
			dbName = dbName + args[0].toString();
		    
		    //Take database authentication information from command line
			dbUser = args[1].toString();
			dbPass = args[2].toString();
			
			Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		    String outputFileName =  "LOG_SIMILARITY"+sdf.format(cal.getTime())+"_"+args[0].toString()+".txt";
			outErrorLog = new FileOutputStream(outputFileName); 
			pErrorLog = new PrintStream( outErrorLog ); 
						
		}
		catch (Exception ex) 
		{
			pErrorLog.println("\tException in initialize: " + ex.getMessage());
        }
	}
	
	public static void unweightedCoSim(int type, int countLimit) 
	{
		//1-all, 2-count limited
		try 
        { 
			Connection conn = DriverManager.getConnection(dbName , dbUser ,dbPass);
			
			AbstractStringMetric metric = new CosineSimilarity();
			
			String user1 = Main.user_id;
			
			if(type == 1)
			{
				/********** UNWEIGHTED ALL **********/
				
				//Statement clearUserCross = conn.createStatement( );
				//clearUserCross.executeUpdate("delete from users_similarity where user_id1 = \'"+Main.user_id+"\' and type like \'UA%\'");
				//clearUserCross.close();
				
				String[] comparisonStrings = getComparisonStrings(user1,1,0);
				String plainText1 = comparisonStrings[0];
				String linkTitles1 = comparisonStrings[1];
				String hashtags1 = comparisonStrings[2];
				String tagCloud1 = comparisonStrings[3];
				String mentionCloud1 = comparisonStrings[4];
				
				
				Statement selectDataToProcess = conn.createStatement( );
				selectDataToProcess.executeQuery("select * from users_to_compare");
				ResultSet rsDataToProcess = selectDataToProcess.getResultSet( );
				
				while (rsDataToProcess.next ( ))
				{
					String user2 = rsDataToProcess.getString("user_name");
					user2= getUserIdFromUsername(user2);
					
					String[] comparisonStrings2 = getComparisonStrings(user2,1,0);
					String plainText2 = comparisonStrings2[0];
					String linkTitles2 = comparisonStrings2[1];
					String hashtags2 = comparisonStrings2[2];
					String tagCloud2 = comparisonStrings2[3];
					String mentionCloud2 = comparisonStrings2[4];
					
					float plainTextResult = metric.getSimilarity(plainText1, plainText2);
					float linkTitlesResult = metric.getSimilarity(linkTitles1, linkTitles2);
					float hashtagsResult = metric.getSimilarity(hashtags1, hashtags2);
					float tagCloudResult = metric.getSimilarity(tagCloud1, tagCloud2);
					float mentionCloudResult = metric.getSimilarity(mentionCloud1, mentionCloud2);
					
					Statement insertCosim = conn.createStatement( );
					insertCosim.executeUpdate("insert into users_similarity(user_id1, user_id2, type, cosim) values (\'"+user1+"\',\'"+user2+"\',\'UAP\',"+plainTextResult+")");
					insertCosim.executeUpdate("insert into users_similarity(user_id1, user_id2, type, cosim) values (\'"+user1+"\',\'"+user2+"\',\'UAL\',"+linkTitlesResult+")");
					insertCosim.executeUpdate("insert into users_similarity(user_id1, user_id2, type, cosim) values (\'"+user1+"\',\'"+user2+"\',\'UAH\',"+hashtagsResult+")");
					insertCosim.executeUpdate("insert into users_similarity(user_id1, user_id2, type, cosim) values (\'"+user1+"\',\'"+user2+"\',\'UAT\',"+tagCloudResult+")");
					insertCosim.executeUpdate("insert into users_similarity(user_id1, user_id2, type, cosim) values (\'"+user1+"\',\'"+user2+"\',\'UAM\',"+mentionCloudResult+")");
					insertCosim.close();
								
				} 
				
				rsDataToProcess.close();
				selectDataToProcess.close();
			}
			
			else if(type == 2)
			{
				/********** UNWEIGHTED COUNT LIMITED **********/
				
				//Statement clearUserCross = conn.createStatement( );
				//clearUserCross.executeUpdate("delete from users_similarity where user_id1 = \'"+Main.user_id+"\' and type like \'UC"+countLimit+"%\'");
				//clearUserCross.close();
				
				String[] comparisonStrings = getComparisonStrings(user1,2,countLimit);
				//String plainText1 = comparisonStrings[0];
				//String linkTitles1 = comparisonStrings[1];
				//String hashtags1 = comparisonStrings[2];
				String tagCloud1 = comparisonStrings[3];
				//String mentionCloud1 = comparisonStrings[4];
				
				
				Statement selectDataToProcess = conn.createStatement( );
				selectDataToProcess.executeQuery("select * from users_to_compare");
				ResultSet rsDataToProcess = selectDataToProcess.getResultSet( );
				
				while (rsDataToProcess.next ( ))
				{
					String user2 = rsDataToProcess.getString("user_name");
					user2= getUserIdFromUsername(user2);
					
					String[] comparisonStrings2 = getComparisonStrings(user2,2,countLimit);
					//String plainText2 = comparisonStrings2[0];
					//String linkTitles2 = comparisonStrings2[1];
					//String hashtags2 = comparisonStrings2[2];
					String tagCloud2 = comparisonStrings2[3];
					//String mentionCloud2 = comparisonStrings2[4];
					
					//float plainTextResult = metric.getSimilarity(plainText1, plainText2);
					//float linkTitlesResult = metric.getSimilarity(linkTitles1, linkTitles2);
					//float hashtagsResult = metric.getSimilarity(hashtags1, hashtags2);
					float tagCloudResult = metric.getSimilarity(tagCloud1, tagCloud2);
					//float mentionCloudResult = metric.getSimilarity(mentionCloud1, mentionCloud2);
					
					Statement insertCosim = conn.createStatement( );
					//insertCosim.executeUpdate("insert into users_similarity(user_id1, user_id2, type, cosim) values (\'"+user1+"\',\'"+user2+"\',\'UC"+countLimit+"P\',"+plainTextResult+")");
					//insertCosim.executeUpdate("insert into users_similarity(user_id1, user_id2, type, cosim) values (\'"+user1+"\',\'"+user2+"\',\'UC"+countLimit+"L\',"+linkTitlesResult+")");
					//insertCosim.executeUpdate("insert into users_similarity(user_id1, user_id2, type, cosim) values (\'"+user1+"\',\'"+user2+"\',\'UC"+countLimit+"H\',"+hashtagsResult+")");
					insertCosim.executeUpdate("insert into users_similarity(user_id1, user_id2, type, cosim) values (\'"+user1+"\',\'"+user2+"\',\'UC"+countLimit+"T\',"+tagCloudResult+")");
					//insertCosim.executeUpdate("insert into users_similarity(user_id1, user_id2, type, cosim) values (\'"+user1+"\',\'"+user2+"\',\'UC"+countLimit+"M\',"+mentionCloudResult+")");
					insertCosim.close();
								
				} 
				
				rsDataToProcess.close();
				selectDataToProcess.close();
			}
			
        } 
		catch (Exception ex) 
		{
			pErrorLog.println("\tException in unweightedCoSim: " + ex.getMessage());
        }      

		
	}
	
	public static void weightedCoSim(int countLimit) 
	{
		try 
        { 
			Connection conn = DriverManager.getConnection(dbName , dbUser ,dbPass);
			
			String user1 = Main.user_id;
			
			/********** WEIGHTED COUNT LIMITED **********/
			
			//Statement clearUserCross = conn.createStatement( );
			//clearUserCross.executeUpdate("delete from users_similarity where user_id1 = \'"+Main.user_id+"\' and type like \'WC"+countLimit+"%\'");
			//clearUserCross.close();
			
			HashMap<String, Double> tagCloud1 = getComparisonStringsWeighted(user1,2,countLimit);

			
			Statement selectDataToProcess = conn.createStatement( );
			selectDataToProcess.executeQuery("select * from users_to_compare");
			ResultSet rsDataToProcess = selectDataToProcess.getResultSet( );
			
			while (rsDataToProcess.next ( ))
			{
				String user2 = rsDataToProcess.getString("user_name");
				user2= getUserIdFromUsername(user2);
				
				HashMap<String, Double> tagCloud2 = getComparisonStringsWeighted(user2,2,countLimit);
				
				Double tagCloudResult = getWeightedSimilarity(tagCloud1, tagCloud2);
				
				Statement insertCosim = conn.createStatement( );
				insertCosim.executeUpdate("insert into users_similarity(user_id1, user_id2, type, cosim) values (\'"+user1+"\',\'"+user2+"\',\'WC"+countLimit+"T\',"+tagCloudResult+")");
				insertCosim.close();
							
			} 
			
			rsDataToProcess.close();
			selectDataToProcess.close();	
        } 
		catch (Exception ex) 
		{
			pErrorLog.println("\tException in unweightedCoSim: " + ex.getMessage());
        }      

		
	}
	
	public static Double getWeightedSimilarity(HashMap<String, Double> tagCloud1, HashMap<String, Double> tagCloud2)
	{
		try
		{
			Vector<Double> vector1 = new Vector<Double>();
			Vector<Double> vector2 = new Vector<Double>();
			
			Set<String> allTokens = new HashSet<String>();
			allTokens.addAll(tagCloud1.keySet());
			allTokens.addAll(tagCloud2.keySet());
			
			Iterator<String> i = allTokens.iterator();

		    while(i.hasNext())
		    {
		    	String token = (String)i.next();
		    	
		    	Double value1  = 0.0;
		    	Object obj1 = tagCloud1.get(token);
		    	if(obj1 == null)
		    	{
		    		value1 = 0.0;
		    	}
		    	else
		    	{
		    		value1 = (Double)obj1;
		    	}
		    	vector1.add(value1);
		    	
		    	Double value2  = 0.0;
		    	Object obj2 = tagCloud2.get(token);
		    	if(obj2 == null)
		    	{
		    		value2 = 0.0;
		    	}
		    	else
		    	{
		    		value2 = (Double)obj2;
		    	}
		    	vector2.add(value2);
		    	
		    }
		    
		    Double magnitude1 = getMagnitude(vector1);
	    	Double magnitude2 = getMagnitude(vector2);
	    	Double dotProduct = getDotProduct(vector1, vector2);
	    	return dotProduct / (magnitude1 * magnitude2); 
		    
		}
		catch(Exception ex)
		{
			pErrorLog.println("\tException in getWeightedSimilarity: " + ex.getMessage());
			return 0.0;
		}
	}
	
	public static String[] getComparisonStrings(String user, int type, int limit)
	{
		try
		{
			//1- all, 2- count limited
			Connection conn = DriverManager.getConnection(dbName , dbUser ,dbPass);
			
			Statement selectUser1Data = conn.createStatement( );
			ResultSet rsUser1Data;
			String plainText1 = "";
			String linkTitles1 = "";
			String hashtags1 = "";
			String mentionCloud1 = "";
			
			
			/*if(type == 1)
			{
				selectUser1Data.executeQuery("select user_id, tag, sum(weight) as weight, pos from (select user_id, text as tag, count(1) as weight, pos from users_plain_texts_stem where user_id = '"+user+"' and (pos = 'UNIDENTIFIED' or pos = 'NOUN') group by text union select user_id,text as tag, count(1) as weight, pos from users_link_texts_stem where user_id = '"+user+"' and (pos = 'UNIDENTIFIED' or pos = 'NOUN') group by text) as union_plain group by tag order by 3 desc");
			}
			else if(type == 2)
			{
				selectUser1Data.executeQuery("select user_id, tag, sum(weight) as weight, pos from (select user_id, text as tag, count(1) as weight, pos from users_plain_texts_stem where user_id = '"+user+"' and (pos = 'UNIDENTIFIED' or pos = 'NOUN') group by text union select user_id,text as tag, count(1) as weight, pos from users_link_texts_stem where user_id = '"+user+"' and (pos = 'UNIDENTIFIED' or pos = 'NOUN') group by text) as union_plain group by tag order by 3 desc limit "+limit);
			}

			rsUser1Data = selectUser1Data.getResultSet( );
			String plainText1 = "";
			while (rsUser1Data.next ( ))
			{
				plainText1 += rsUser1Data.getString("tag") + " ";
			} 
			plainText1 = plainText1.trim();*/
			
			/*selectUser1Data = conn.createStatement( );
			if(type == 1)
			{
				selectUser1Data.executeQuery("select user_id, tag, sum(tag_count) as weight, pos from users_links_titles_stem where user_id = '"+user+"' and (pos = 'UNIDENTIFIED' or pos = 'NOUN') group by tag order by 3 desc");
			}
			else if(type == 2)
			{
				selectUser1Data.executeQuery("select user_id, tag, sum(tag_count) as weight, pos from users_links_titles_stem where user_id = '"+user+"' and (pos = 'UNIDENTIFIED' or pos = 'NOUN') group by tag order by 3 desc limit "+limit);
			}
			
			rsUser1Data = selectUser1Data.getResultSet( );
			String linkTitles1 = "";
			while (rsUser1Data.next ( ))
			{
				linkTitles1 += rsUser1Data.getString("tag") + " ";
			} 
			linkTitles1 = linkTitles1.trim();*/
			
			/*selectUser1Data = conn.createStatement( );
			if(type == 1)
			{
				selectUser1Data.executeQuery("select user_id, hashtag as tag, count(1) as weight from users_hashtags where user_id = '"+user+"' group by hashtag order by 3 desc");
			}
			else if(type == 2)
			{
				selectUser1Data.executeQuery("select user_id, hashtag as tag, count(1) as weight from users_hashtags where user_id = '"+user+"' group by hashtag order by 3 desc limit "+limit);
			}
			
			rsUser1Data = selectUser1Data.getResultSet( );
			String hashtags1 = "";
			while (rsUser1Data.next ( ))
			{
				hashtags1 += rsUser1Data.getString("tag") + " ";
			} 
			hashtags1 = hashtags1.trim();*/
			
			selectUser1Data = conn.createStatement( );
			if(type == 1)
			{
				selectUser1Data.executeQuery("select user_id, tag, sum(weight) as weight, pos from users_tag_clouds_stem where user_id = '"+user+"' and (pos = 'UNIDENTIFIED' or pos = 'NOUN') group by tag order by 3 desc");
			}
			else if(type == 2)
			{
				selectUser1Data.executeQuery("select user_id, tag, sum(weight) as weight, pos from users_tag_clouds_stem where user_id = '"+user+"' and (pos = 'UNIDENTIFIED' or pos = 'NOUN') group by tag order by 3 desc limit "+limit);
			}
			
			rsUser1Data = selectUser1Data.getResultSet( );
			String tagCloud1 = "";
			while (rsUser1Data.next ( ))
			{
				tagCloud1 += rsUser1Data.getString("tag") + " ";
			} 
			tagCloud1 = tagCloud1.trim();
			
			/*selectUser1Data = conn.createStatement( );
			if(type == 1)
			{
				selectUser1Data.executeQuery("select user_id, mention as tag, count(1) as weight from users_mentions where user_id = '"+user+"' group by mention order by 3 desc");
			}
			else if(type == 2)
			{
				selectUser1Data.executeQuery("select user_id, mention as tag, count(1) as weight from users_mentions where user_id = '"+user+"' group by mention order by 3 desc limit "+limit);
			}
			
			rsUser1Data = selectUser1Data.getResultSet( );
			String mentionCloud1 = "";
			while (rsUser1Data.next ( ))
			{
				mentionCloud1 += rsUser1Data.getString("tag") + " ";
			} 
			mentionCloud1 = mentionCloud1.trim();*/
			
			rsUser1Data.close();
			selectUser1Data.close();
			
			String[] comparisonStrings = {plainText1,linkTitles1,hashtags1,tagCloud1,mentionCloud1};
			return comparisonStrings;
		}
		catch (Exception ex) 
		{
			pErrorLog.println("\tException in getComparisonStrings: " + ex.getMessage());
            String[] comparisonStrings = {"","","","",""};
			return comparisonStrings;
        }  
	}
	
	public static HashMap<String, Double> getComparisonStringsWeighted(String user, int type, int limit)
	{
		try
		{
			Connection conn = DriverManager.getConnection(dbName , dbUser ,dbPass);
			
			Statement selectUser1Data = conn.createStatement( );
			ResultSet rsUser1Data;
			
			
			selectUser1Data = conn.createStatement( );
			if(type == 1)
			{
				selectUser1Data.executeQuery("select user_id, tag, sum(weight) as weight, pos from users_tag_clouds_stem where user_id = '"+user+"' and (pos = 'UNIDENTIFIED' or pos = 'NOUN') group by tag order by 3 desc");
			}
			else if(type == 2)
			{
				selectUser1Data.executeQuery("select user_id, tag, sum(weight) as weight, pos from users_tag_clouds_stem where user_id = '"+user+"' and (pos = 'UNIDENTIFIED' or pos = 'NOUN') group by tag order by 3 desc limit "+limit);
			}
			
			HashMap<String, Double> hm = new HashMap<String, Double>();
			
			rsUser1Data = selectUser1Data.getResultSet( );
			while (rsUser1Data.next ( ))
			{
				hm.put(rsUser1Data.getString("tag"), Double.valueOf(rsUser1Data.getString("weight")));
			} 
			
			rsUser1Data.close();
			selectUser1Data.close();
			
			return hm;
		}
		catch (Exception ex) 
		{
			HashMap<String, Double> hm = new HashMap<String, Double>();
			pErrorLog.println("\tException in getComparisonStringsWeighted: " + ex.getMessage());
			return hm;
        }  
	}
	
	public static String getUserIdFromUsername(String username)
	{
		try
		{
			Connection conn = DriverManager.getConnection(dbName , dbUser ,dbPass);
			Statement select = conn.createStatement( );
			select.executeQuery("select user_id from users where screen_name = \'"+username+"'");
			ResultSet rs = select.getResultSet( );
			rs.next();
			String returnValue = rs.getString("user_id");
			rs.close();
			select.close();
			return returnValue;
		}
		catch(Exception ex)
		{
			pErrorLog.println("\tException in getUserIdFromUsername: " + ex.getMessage());
			return "";
		}
	}
	
	public static Double getMagnitude(Vector<Double> v)
	{
		Iterator<Double> i = v.iterator();
		Double sum = 0.0;
		while(i.hasNext())
	    {
			Double d = (Double) i.next();
			sum += d*d;
	    }
		return Math.sqrt(sum);
		
	}
	
	public static Double getDotProduct(Vector<Double> vector1, Vector<Double> vector2)
	{
		int sizeOfVectors = vector1.size();
		Double sum = 0.0; 
		for(int i=0; i< sizeOfVectors; i++)
		{
			Double d1 = (Double)vector1.get(i);
			Double d2 = (Double)vector2.get(i);
			sum += (d1 * d2);
		}
		return sum;
	}

}