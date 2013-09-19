package search;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import network.Edge;

import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import similarity.SearchResult;
import twitter.MyTweet;
import twitter.MyUser;
import util.JdbcUtil;
import util.LogUtil;
import util.TimeUtil;
import util.Util;

public class DaoImpl implements Dao {

	private static final String SQL_INSERT_TWEET = "INSERT IGNORE INTO T_TWEET (ID, SCREEN_NAME, TWEET, DATE, QUERY_DATE, IS_RETWEET, RETWEETED_ID, RETWEETED_SCREEN_NAME, REPLY_ID, REPLY_SCREEN_NAME, RETWEET_COUNT, LATITUDE, LONGITUDE, RAW_JSON, IS_RELATED) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String SQL_INSERT_RAW_TWEET = "INSERT IGNORE INTO RAW_TWEET (ID, SCREEN_NAME, QUERY_DATE, RAW_JSON) VALUES (?,?,?,?)";
	private static final String SQL_INSERT_SEARCH_RESULT = "INSERT IGNORE INTO T_SEARCH_RESULT (SEARCH_ID, TWEET_ID, SCREEN_NAME, TYPE) VALUES (?,?,?,?)";
	private static final String SQL_INSERT_TWEET_TOKENS = "call recommicro.insert_tokens (?,?,?,?,?)";
	
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void updateNewScore(int searchId) {
		
		calcAvg(searchId);

		calcNorm(searchId);
		
		jdbcTemplate.update(
				"update t_user tu set new_score = ( " + 
						    0.27 + " * ( if(socialness_own_norm is null,0,socialness_own_norm) + if(feedness_own_norm is null,0,feedness_own_norm) + if(term_variation_own_norm is null,0,term_variation_own_norm) ) + " +   
						    0.05 + " * if(hashtags_own_norm is null,0,hashtags_own_norm) + " + 
						    0.13 + " * ( if(socialness_rts_norm is null,0,socialness_rts_norm) + if(feedness_rts_norm is null,0,feedness_rts_norm ) + if(term_variation_rts_norm is null,0,term_variation_rts_norm) ) + " + 
						    0.03 + " * if(hashtags_rts_norm is null,0,hashtags_rts_norm) + " + 
						    0.30 + " * if(rating_own_norm is null,0,rating_own_norm) + " + 
						    0.22 + " * if(rating_rts_norm is null,0,rating_rts_norm) " + 
		" ) where search_id = ? and score > ?", 
		new Object[] { searchId, Conf.MIN_BEST_CONTENT_BASED_SCORE });
		
		/*
		List<SearchResult> users = jdbcTemplate.query(
				" select new_score_norm, u.screen_name from( " + 
				" select  IF(@score=score, @rownum, @rownum:=@rownum+1) rank_new,( " +  
				"   0.27 * (  if(socialness_own_norm is null,0,socialness_own_norm) + if(feedness_own_norm is null,0,feedness_own_norm) + if(term_variation_own_norm is null,0,term_variation_own_norm) ) " + 
				" + 0.05 * if(hashtags_own_norm is null,0,hashtags_own_norm) " + 
				" + 0.13 * ( if(socialness_rts_norm is null,0,socialness_rts_norm) + if(feedness_rts_norm is null,0,feedness_rts_norm ) + if(term_variation_rts_norm is null,0,term_variation_rts_norm) )" + 
				" + 0.03 * if(hashtags_rts_norm is null,0,hashtags_rts_norm) " + 
				" + 0.30 * if(rating_own_norm is null,0,rating_own_norm) " + 
				" + 0.22 * if(rating_rts_norm is null,0,rating_rts_norm) " + 
				" ) new_score_norm, " + 
				" t.* from ( " + 
				" select  " + 
				" (socialness_own+socialness_rts+feedness_own+feedness_rts+rating_own+rating_rts+term_variation_own+term_variation_rts) new_score, " + 
				" IF(@score=score, @rownum, @rownum:=@rownum+1) rank_old, " +    
				" (select min(type)-1 from t_search_result where search_id = tu.search_id and screen_name = tu.screen_name and type > 1) iter, " + 
				" screen_name, name, statuses_count, followers_count, friends_count, " + 
				" format(score, 3) score, " + 
				" ( socialness_own / " +  
				" (select ( sum(socialness_own * num_of_related_tweets_own) / sum(num_of_related_tweets_own) ) socialness_own_avg " + 
				" from t_user where search_id = tu.search_id and score > 0.1)) socialness_own_norm, " + 
				" ( socialness_rts / " +  
				" (select ( sum(socialness_rts * num_of_related_tweets_rts) / sum(num_of_related_tweets_rts) ) socialness_rts_avg " + 
				" from t_user where search_id = tu.search_id and score > 0.1)) socialness_rts_norm, " + 
				" ( feedness_own / " +  
				" (select ( sum(feedness_own * num_of_related_tweets_own) / sum(num_of_related_tweets_own) ) feedness_own_avg " + 
				" from t_user where search_id = tu.search_id and score > 0.1)) feedness_own_norm, " + 
				" ( feedness_rts / " +  
				" (select ( sum(feedness_rts * num_of_related_tweets_rts) / sum(num_of_related_tweets_rts) ) feedness_rts_avg " + 
				" from t_user where search_id = tu.search_id and score > 0.1)) feedness_rts_norm, " + 
				" ( hashtags_own / " +  
				" (select ( sum(hashtags_own * num_of_related_tweets_own) / sum(num_of_related_tweets_own) ) hashtags_own_avg " + 
				" from t_user where search_id = tu.search_id and score > 0.1)) hashtags_own_norm, " + 
				" ( hashtags_rts / " +  
				" (select ( sum(hashtags_rts * num_of_related_tweets_rts) / sum(num_of_related_tweets_rts) ) hashtags_rts_avg " + 
				" from t_user where search_id = tu.search_id and score > 0.1)) hashtags_rts_norm, " + 
				" ( rating_own / " +  
				" (select ( sum(rating_own * num_of_related_tweets_own) / sum(num_of_related_tweets_own) ) rating_own_avg " + 
				" from t_user where search_id = tu.search_id and score > 0.1)) rating_own_norm, " + 
				" ( rating_rts / " +  
				" (select ( sum(rating_rts * num_of_related_tweets_rts) / sum(num_of_related_tweets_rts) ) rating_rts_avg " + 
				" from t_user where search_id = tu.search_id and score > 0.1)) rating_rts_norm, " + 
				" ( term_variation_own / " +  
				" (select ( sum(term_variation_own * num_of_related_tweets_own) / sum(num_of_related_tweets_own) ) term_variation_own_avg " + 
				" from t_user where search_id = tu.search_id and score > 0.1)) term_variation_own_norm, " + 
				" term_variation_rts, " + 
				" ( term_variation_rts / " +  
				" (select ( sum(term_variation_rts * num_of_related_tweets_rts) / sum(num_of_related_tweets_rts) ) term_variation_rts_avg " + 
				" from t_user where search_id = tu.search_id and score > 0.1)) term_variation_rts_norm " + 
				" from t_user tu, (SELECT @rownum:=0) x, (SELECT @score:=0) y " +  
				" where search_id = ? " + 
				" and score > 0.1 and num_of_tweets_fetched >= 50 " + 
				" and (  " +
				"        (num_of_related_tweets_own > 0 and term_variation_own > 3) " + 
				"        OR " +
				"        (num_of_related_tweets_rts > 0 and term_variation_rts > 3) " + 
				"    ) " +
				" order by score desc " + 
				" ) t, (SELECT @rownum:=0) x, (SELECT @score:=0) y " +  
				" order by new_score_norm desc, score desc) u "
				, new Object[]{searchId}, 

				new RowMapper<SearchResult>() {
			 		public SearchResult mapRow(ResultSet rs, int rowNum) throws SQLException {
			 			
			 			MyUser user = new MyUser();
			 			user.setScreenName(rs.getString("screen_name"));
			 			
			 			SearchResult result = new SearchResult(user, rs.getDouble("new_score_norm"));
			 			
			 			return result;
			 		}
				}
		);
		
		for (SearchResult searchResult : users) {
			jdbcTemplate.update("update T_USER set new_score = ? where screen_name = ? and search_id = ?", 
					new Object[] { searchResult.getScore(), searchResult.getMyUser().getScreenName(), searchId });
		}
		
		*/
	}


	private void calcNorm(int searchId) {
		
		jdbcTemplate.update( " update t_user tu set socialness_own_norm = (socialness_own / (select socialness_own_avg from t_search where id=tu.search_id) ) where search_id = ? and score > ? ",
				new Object[] { searchId, Conf.MIN_BEST_CONTENT_BASED_SCORE });
		jdbcTemplate.update( " update t_user tu set socialness_rts_norm = (socialness_rts / (select socialness_rts_avg from t_search where id=tu.search_id) ) where search_id = ? and score > ? ",
				new Object[] { searchId, Conf.MIN_BEST_CONTENT_BASED_SCORE });
	
		jdbcTemplate.update( " update t_user tu set feedness_own_norm = (feedness_own / (select feedness_own_avg from t_search where id=tu.search_id) ) where search_id = ? and score > ? ",
				new Object[] { searchId, Conf.MIN_BEST_CONTENT_BASED_SCORE });
		jdbcTemplate.update( " update t_user tu set feedness_rts_norm = (feedness_rts / (select feedness_rts_avg from t_search where id=tu.search_id) ) where search_id = ? and score > ? ",
				new Object[] { searchId, Conf.MIN_BEST_CONTENT_BASED_SCORE });
		
		jdbcTemplate.update( " update t_user tu set hashtags_own_norm = (hashtags_own / (select hashtags_own_avg from t_search where id=tu.search_id) ) where search_id = ? and score > ? ",
				new Object[] { searchId, Conf.MIN_BEST_CONTENT_BASED_SCORE });
		jdbcTemplate.update( " update t_user tu set hashtags_rts_norm = (hashtags_rts / (select hashtags_rts_avg from t_search where id=tu.search_id) ) where search_id = ? and score > ? ",
				new Object[] { searchId, Conf.MIN_BEST_CONTENT_BASED_SCORE });

		jdbcTemplate.update( " update t_user tu set rating_own_norm = (rating_own / (select rating_own_avg from t_search where id=tu.search_id) ) where search_id = ? and score > ? ",
				new Object[] { searchId, Conf.MIN_BEST_CONTENT_BASED_SCORE });
		jdbcTemplate.update( " update t_user tu set rating_rts_norm = (rating_rts / (select rating_rts_avg from t_search where id=tu.search_id) ) where search_id = ? and score > ? ",
				new Object[] { searchId, Conf.MIN_BEST_CONTENT_BASED_SCORE });
		
		jdbcTemplate.update( " update t_user tu set term_variation_own_norm = (term_variation_own / (select term_variation_own_avg from t_search where id=tu.search_id) ) where search_id = ? and score > ? ",
				new Object[] { searchId, Conf.MIN_BEST_CONTENT_BASED_SCORE });
		jdbcTemplate.update( " update t_user tu set term_variation_rts_norm = (term_variation_rts / (select term_variation_rts_avg from t_search where id=tu.search_id) ) where search_id = ? and score > ? ",
				new Object[] { searchId, Conf.MIN_BEST_CONTENT_BASED_SCORE });
	}


	private void calcAvg(int searchId) {
		jdbcTemplate.update(
				" update t_search ts set socialness_own_avg = ( "
						  + " 		select (sum(socialness_own * num_of_related_tweets_own) / sum(num_of_related_tweets_own) ) from t_user where search_id = ts.id and score > ? "
						  + " ) where id = ? ",
				new Object[] { Conf.MIN_BEST_CONTENT_BASED_SCORE, searchId });
		
		jdbcTemplate.update(
				" update t_search ts set socialness_rts_avg = ( "
						  + " 		select (sum(socialness_rts * num_of_related_tweets_rts) / sum(num_of_related_tweets_rts) ) from t_user where search_id = ts.id and score > ? "
						  + " ) where id = ? ",
				new Object[] { Conf.MIN_BEST_CONTENT_BASED_SCORE, searchId });
		

		jdbcTemplate.update(
				" update t_search ts set feedness_own_avg = ( "
						  + " 		select (sum(feedness_own * num_of_related_tweets_own) / sum(num_of_related_tweets_own) ) from t_user where search_id = ts.id and score > ? "
						  + " ) where id = ? ",
				new Object[] { Conf.MIN_BEST_CONTENT_BASED_SCORE, searchId });
		
		
		jdbcTemplate.update(
				" update t_search ts set feedness_rts_avg = ( "
						  + " 		select (sum(feedness_rts * num_of_related_tweets_rts) / sum(num_of_related_tweets_rts) ) from t_user where search_id = ts.id and score > ? "
						  + " ) where id = ? ",
				new Object[] { Conf.MIN_BEST_CONTENT_BASED_SCORE, searchId });
		
		jdbcTemplate.update(
				" update t_search ts set hashtags_own_avg = ( "
						  + " 		select (sum(hashtags_own * num_of_related_tweets_own) / sum(num_of_related_tweets_own) ) from t_user where search_id = ts.id and score > ? "
						  + " ) where id = ? ",
				new Object[] { Conf.MIN_BEST_CONTENT_BASED_SCORE, searchId });
		
		jdbcTemplate.update(
				" update t_search ts set hashtags_rts_avg = ( "
						  + " 		select (sum(hashtags_rts * num_of_related_tweets_rts) / sum(num_of_related_tweets_rts) ) from t_user where search_id = ts.id and score > ? "
						  + " ) where id = ? ",
				new Object[] { Conf.MIN_BEST_CONTENT_BASED_SCORE, searchId });
		
		
		jdbcTemplate.update(
				" update t_search ts set rating_own_avg = ( "
						  + " 		select (sum(rating_own * num_of_related_tweets_own) / sum(num_of_related_tweets_own) ) from t_user where search_id = ts.id and score > ? "
						  + " ) where id = ? ",
				new Object[] { Conf.MIN_BEST_CONTENT_BASED_SCORE, searchId });
		
		
		jdbcTemplate.update(
				" update t_search ts set rating_rts_avg = ( "
						  + " 		select (sum(rating_rts * num_of_related_tweets_rts) / sum(num_of_related_tweets_rts) ) from t_user where search_id = ts.id and score > ? "
						  + " ) where id = ? ",
				new Object[] { Conf.MIN_BEST_CONTENT_BASED_SCORE, searchId });
		
		jdbcTemplate.update(
				" update t_search ts set term_variation_own_avg = ( "
						  + " 		select (sum(term_variation_own * num_of_related_tweets_own) / sum(num_of_related_tweets_own) ) from t_user where search_id = ts.id and score > ? "
						  + " ) where id = ? ",
				new Object[] { Conf.MIN_BEST_CONTENT_BASED_SCORE, searchId });
		
		jdbcTemplate.update(
				" update t_search ts set term_variation_rts_avg = ( "
						  + " 		select (sum(term_variation_rts * num_of_related_tweets_rts) / sum(num_of_related_tweets_rts) ) from t_user where search_id = ts.id and score > ? "
						  + " ) where id = ? ",
				new Object[] { Conf.MIN_BEST_CONTENT_BASED_SCORE, searchId });
	}
	
	/**
	 * 
	 * @param search
	 * @param myUser
	 * @return
	 */
	public List<String> findRelatedUserNames(Search search, Set<String> referenceUserNames) { 
		
		String sqlInScreenNames = Util.joinWithCommaForSqlIn(referenceUserNames);
		
		String sql = " select screen_name, count(screen_name) occurrences from ( " +
					 "SELECT tm.mention as screen_name " +
        			 "  FROM t_tweet t, t_search_result sr, t_tweet_mention tm " +
        			 " WHERE t.id = sr.tweet_id " +
        			 "   AND sr.search_id = ? " +
        			 "   AND sr.type <> " + Search.SEARCH_TYPE_SEARCH + 
        			 // "   AND sr.type in (2,3) " +
        			 "   AND sr.screen_name in ( " +  sqlInScreenNames  + ")" +
        			 "   AND tm.tweet_id = t.id " +
        			 " union all " +
        			 "SELECT t.retweeted_screen_name as screen_name " +
        			 "  FROM t_tweet t, t_search_result sr " +
        			 " WHERE t.id = sr.tweet_id " +
        			 "   AND sr.search_id = ? " +
        			 "   AND sr.type <> " + Search.SEARCH_TYPE_SEARCH + 
        			 // "   AND sr.type in (2,3) " +
        			 "   AND sr.screen_name in ( " +  sqlInScreenNames  + ")" +
        			 "   AND t.retweeted_screen_name != '' " +
        			 " ORDER BY screen_name desc" + 
        			 ") t where screen_name not in (select distinct t.screen_name from t_tweet t, t_search_result sr where t.id = sr.tweet_id and sr.type <> " + 
        			 Search.SEARCH_TYPE_SEARCH + " and search_id = ?) group by screen_name order by occurrences desc limit " + Conf.EXTENDED_USERS_USERS_PER_ITER + ";";
		
		/*
		return jdbcTemplate.query(sql, new Object[]{search.getSearchId(), screenName},
				new RowMapper<String>(){
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getString(1);
					}
				}
		);
		*/
		List<String> relatedUserNames = jdbcTemplate.query(sql, new Object[]{search.getId(),search.getId(),search.getId()}, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});
		
		
		LogUtil.logger.info("search={}, referenceUserNames={}, relatedUserNames={}", new Object[]{search.getId(), Util.joinWithComma(referenceUserNames), Util.joinWithComma(relatedUserNames)});
		
		return relatedUserNames;
		
	}

	@Override
	public void insertSearch(Search search, List<MyTweet> myTweets) throws Exception {
		
		if (search.isInsertOrder()) {
			insertSearch(search);
		}
		
		insertExtendedSearch(search);
		
		for (MyTweet myTweet : myTweets) {
			insertSearchResult(myTweet, search.getId(), Search.SEARCH_TYPE_SEARCH);
		}
	}

	/**
	 * 
	 * @param search
	 * @param searchId
	 */
	private void insertExtendedSearch(Search search) {
		
		for (String tag : search.getTopCooccuringTags().uniqueSet()) {
			jdbcTemplate.update("INSERT IGNORE INTO T_SEARCH_EXTENDED (SEARCH_ID, TAG, WEIGHT, TYPE) VALUES (?,?,?,?)", new Object[] { search.getId(), tag, search.getTopCooccuringTags().getCount(tag), 1});
		}
		
		for (String tag : search.getCooccuringTags().uniqueSet()) {
			jdbcTemplate.update("INSERT IGNORE INTO T_SEARCH_EXTENDED (SEARCH_ID, TAG, WEIGHT, TYPE) VALUES (?,?,?,?)", new Object[] { search.getId(), tag, search.getCooccuringTags().getCount(tag), 2});
		}
	}

	/**
	 * 
	 */
	public List<MyTweet> querySearchResult(Search search) throws Exception {
		
		List<MyTweet> myTweets = jdbcTemplate.query(
				
				"SELECT tweet_id, screen_name FROM t_search_result WHERE search_id = ? and type = " + Search.SEARCH_TYPE_SEARCH, new Object[]{search.getId()}, 

				new RowMapper<MyTweet>() {
			 		public MyTweet mapRow(ResultSet rs, int rowNum) throws SQLException {
			 			return MyTweet.toSearchResult(rs.getString(1), rs.getString(2));
			 		}
				}
		);
		
		return myTweets;
	}

	/**
	 * 
	 */
	public void querySearchQuery(Search search) {
		
		if (search.fromDb()) {
			querySearch(search);
		} else {
			search.setId( findLastSearchId(search.getQuery()) );
		}
	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	private int findLastSearchId(String query) {
		return jdbcTemplate.queryForInt("SELECT MAX(id) FROM t_search WHERE query = ?", new Object[]{query});
	}
	
	/**
	 * 
	 * @param searchId
	 * @return
	 */
	private void querySearch(final Search search) {

		String query = jdbcTemplate.queryForObject("SELECT query FROM t_search WHERE id = ?", new Object[]{search.getId()}, String.class);

		String tagsSql = "SELECT GROUP_CONCAT( REPEAT(CONCAT(tag,' '), weight)  SEPARATOR '') FROM t_search_extended WHERE search_id = ? and type = ";
		String topCooccuringTags = jdbcTemplate.queryForObject(tagsSql + 1, new Object[]{search.getId()}, String.class);
		String cooccuringTags = jdbcTemplate.queryForObject(tagsSql + 2, new Object[]{search.getId()}, String.class);		
		
		Search.createFromDB(search, query, topCooccuringTags, cooccuringTags);
		
		
		/*
		jdbcTemplate.queryForObject(
				
				"SELECT query, twitter_query, vector_query FROM t_search WHERE id = ?" , new Object[]{search.getId()}, 
				
				new RowMapper<Search>() {
			 		public Search mapRow(ResultSet rs, int rowNum) throws SQLException {
			 			return Search.createFromDB(search, rs.getString(1), rs.getString(2), rs.getString(3));
			 		}
				}
		);
		*/
	}
	
	/**
	 * 
	 */
	public void insertTweetTokens(MyTweet myTweet) throws Exception {
		
		/*
		String hashtags = myTweet.getHashtagsSeparated();
		String mentions = myTweet.getMentionsSeparated();
		String urls = myTweet.getUrlsSeparated();
		String words = myTweet.getWordsSeparated();
		
		LogUtil.logger.info("tweet={}, hashtags={}, mentions={}, urls={}, words={}", new Object[]{ myTweet.getId(), hashtags, mentions, urls, words });
		*/
		
		try {
			jdbcTemplate.update(SQL_INSERT_TWEET_TOKENS, new Object[] {
					myTweet.getId(),
					myTweet.getHashtagsSeparated(),
					myTweet.getMentionsSeparated(),
					myTweet.getUrlsSeparated(),
					myTweet.getWordsSeparated()
			});
		} catch (Exception e) {
			LogUtil.logger.error("failed", e);
			LogUtil.logger.error("exception while insert_tokens for tweet={}: {}", myTweet.getId(), e.toString());
		}
	}

	/**
	 * 
	 * @param searchQuery
	 * @return
	 */
	private int insertSearch(Search search) {
		/*
		jdbcTemplate.update("INSERT INTO T_SEARCH (QUERY, TWITTER_QUERY, VECTOR_QUERY) VALUES (?,?,?)", new Object[] { search.getQuery(), Util.joinWithSpace(search.getTopCooccuringTags()), Util.joinWithSpace(search.getCooccuringTags()) });
		int searchId = jdbcTemplate.queryForInt("select max(id) from T_SEARCH");
		return searchId;
		*/
		jdbcTemplate.update("INSERT INTO T_SEARCH (QUERY) VALUES (?)", new Object[] { search.getQuery()});
		int searchId = jdbcTemplate.queryForInt("select max(id) from T_SEARCH");
		return searchId;
	}
	
	/**
	 * 
	 * @param tweet
	 * @param searchId
	 */
	private void insertSearchResult(MyTweet myTweet, int searchId, int searchType) {
		jdbcTemplate.update("INSERT IGNORE INTO T_SEARCH_RESULT (SEARCH_ID, TWEET_ID, SCREEN_NAME, TYPE) VALUES (?,?,?,?)", new Object[] 
				{ searchId, myTweet.getId(), myTweet.getFromUser(), searchType});
	}

	/**
	 * @deprecated test batch
	 */
	public void insertBatch(List<String> list) {
		
		List<Object[]> myTweetsArgumentList = new ArrayList<Object[]>(list.size());

			for (String str : list) {
	        	
	            myTweetsArgumentList.add(new Object[] { 
	            		str, 1, str
	    		});
	        }
		
		jdbcTemplate.batchUpdate("insert into t_ref(id,i_key,v_val) values(?,?,?)", myTweetsArgumentList);
	}

	public void insertTweets(MyUser myUser, Search search, int searchType) {
		
		insertBatchTweets(myUser);
		insertBatchRawTweets(myUser);
		insertBatchSearchResults(myUser, search, searchType);
		insertBatchTweetTokens(myUser);
	}
	
	/**
	 * 
	 */
	private void insertBatchTweetTokens(MyUser myUser)  {
		
		StopWatch stopWatch_insertTweetTokens = new Log4JStopWatch("insertTweets.insertTweetTokens", myUser.getScreenName());
		
		List<MyTweet> myTweets = myUser.getTweets();
		
		List<Object[]> myTweetsArgumentList = new ArrayList<Object[]>(myTweets.size());

			for (MyTweet myTweet : myTweets) {
				/*
				String hashtags = myTweet.getHashtagsSeparated();
				String mentions = myTweet.getMentionsSeparated();
				String urls = myTweet.getUrlsSeparated();
				String words = myTweet.getWordsSeparated();
				
				LogUtil.logger.info("tweet={}, hashtags={}, mentions={}, urls={}, words={}", new Object[]{ myTweet.getId(), hashtags, mentions, urls, words });
	        	*/
	            myTweetsArgumentList.add(new Object[] { 
	            		myTweet.getId(),
	            		myTweet.getHashtagsSeparated(),
						myTweet.getMentionsSeparated(),
						myTweet.getUrlsSeparated(),
						myTweet.getWordsSeparated()
	    		});
	        }
		
		jdbcTemplate.batchUpdate(SQL_INSERT_TWEET_TOKENS, myTweetsArgumentList);

		stopWatch_insertTweetTokens.stop();
	}


	private void insertBatchSearchResults(MyUser myUser, Search search, int searchType) {

		// LogUtil.logger.info("<insertTweets.insertBatchSearchResults({})>", myUser.getScreenName());
		StopWatch stopWatch_insertBatchSearchResults = new Log4JStopWatch("insertTweets.insertBatchSearchResults", myUser.getScreenName());
		
		List<MyTweet> myTweets = myUser.getTweets();
		
		List<Object[]> myTweetsArgumentList = new ArrayList<Object[]>(myTweets.size());

			for (MyTweet myTweet : myTweets) {
	    
				if (myTweet.getRawJson() != null && !myTweet.getRawJson().equals("")) {
					
					myTweetsArgumentList.add(new Object[] { 
							search.getId(), myTweet.getId(), myTweet.getFromUser(), searchType
					});
				}
				
	        }
		
		jdbcTemplate.batchUpdate(SQL_INSERT_SEARCH_RESULT, myTweetsArgumentList);

		stopWatch_insertBatchSearchResults.stop();
		// LogUtil.logger.info("</insertTweets.insertBatchSearchResults({})>", myUser.getScreenName());
		
	}

	private void insertBatchRawTweets(MyUser myUser) {
		
		// LogUtil.logger.info("<insertTweets.insertBatchRawTweets({})>", myUser.getScreenName());
		
		StopWatch stopWatch_insertBatchRawTweets = new Log4JStopWatch("insertTweets.insertBatchRawTweets", myUser.getScreenName());
		
		List<MyTweet> myTweets = myUser.getTweets();
		
		List<Object[]> myTweetsArgumentList = new ArrayList<Object[]>(myTweets.size());

			for (MyTweet myTweet : myTweets) {
	    
				if (myTweet.getRawJson() != null && !myTweet.getRawJson().equals("")) {
					
					myTweetsArgumentList.add(new Object[] { 
							myTweet.getId(), myTweet.getFromUser(), TimeUtil.getCurrentTime(), myTweet.getRawJson()
					});
				}
				
	        }
		
		jdbcTemplate.batchUpdate(SQL_INSERT_RAW_TWEET, myTweetsArgumentList);

		stopWatch_insertBatchRawTweets.stop();
		// LogUtil.logger.info("</insertTweets.insertBatchRawTweets({})>", myUser.getScreenName());		
	}

	private void insertBatchTweets(MyUser myUser) {
		
		// LogUtil.logger.info("<insertTweets.insertBatchTweets({})>", myUser.getScreenName());
		StopWatch stopWatch_insertBatchTweets = new Log4JStopWatch("insertTweets.insertBatchTweets", myUser.getScreenName());
		
		List<MyTweet> myTweets = myUser.getTweets();
		
		List<Object[]> myTweetsArgumentList = new ArrayList<Object[]>(myTweets.size());

			for (MyTweet myTweet : myTweets) {
	        	
	            myTweetsArgumentList.add(new Object[] { 
	            		myTweet.getId(), myTweet.getFromUser(), myTweet.getText(), myTweet.getCreatedAt(), 
	            		TimeUtil.getCurrentTime(), myTweet.isRetweet(), myTweet.getRetweetedId(), myTweet.getRetweetedScreenName(),
	    				myTweet.getReplyToId(), myTweet.getReplyToScreenName(), myTweet.getRetweetCount(), myTweet.getLatitude(), myTweet.getLongitude(), "", myTweet.isRelatedDbVal()
	    		});
	        }
		
		jdbcTemplate.batchUpdate(SQL_INSERT_TWEET, myTweetsArgumentList);
		
		stopWatch_insertBatchTweets.stop();
		// LogUtil.logger.info("</insertTweets.insertBatchTweets({})>", myUser.getScreenName());
	}
	
	/**
	 * @deprecated use {@link #insertTweets(MyUser, Search, int)} 
	 * 
	 */
	public void insertTweet(MyTweet myTweet, Search search, int searchType) {
		
		jdbcTemplate.update(SQL_INSERT_TWEET, 
				new Object[] { myTweet.getId(), myTweet.getFromUser(), myTweet.getText(), myTweet.getCreatedAt(), TimeUtil.getCurrentTime(), myTweet.isRetweet(), myTweet.getRetweetedId(), myTweet.getRetweetedScreenName(),
				myTweet.getReplyToId(), myTweet.getReplyToScreenName(), myTweet.getRetweetCount(), myTweet.getLatitude(), myTweet.getLongitude(), ""});
		
		if (myTweet.getRawJson() != null && !myTweet.getRawJson().equals("")) {
			jdbcTemplate.update(SQL_INSERT_RAW_TWEET, 
					new Object[] { myTweet.getId(), myTweet.getFromUser(), TimeUtil.getCurrentTime(), myTweet.getRawJson() }
			);
		}
		
		insertSearchResult(myTweet, search.getId(), searchType);
	}
	
	/**
	 * 
	 */
	public void insertUser(MyUser myUser, Search search) {
		
		try {

			jdbcTemplate.update("INSERT IGNORE INTO T_USER (ID, SCREEN_NAME, NAME, STATUSES_COUNT, FOLLOWERS_COUNT, FRIENDS_COUNT, LOCATION, DESCRIPTION, PROFILE_IMAGE_URL, SEARCH_ID, SOCIALNESS_OWN, SOCIALNESS_RTS, FEEDNESS_OWN, FEEDNESS_RTS, RATING_OWN, RATING_RTS, TERM_VARIATION_OWN, TERM_VARIATION_RTS, HASHTAGS_OWN, HASHTAGS_RTS, RETWEET_RATIO_NEW, SEARCH_RELATENESS_NEW, NUM_OF_TWEETS_FETCHED, NUM_OF_RELATED_TWEETS, NUM_OF_RELATED_TWEETS_OWN, NUM_OF_RELATED_TWEETS_RTS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[] {
					
					myUser.getId(), myUser.getScreenName(), myUser.getName(), myUser.getStatusesCount(), myUser.getFollowersCount(), myUser.getFriendsCount(), 
					myUser.getLocation(), myUser.getDescription(), myUser.getProfileImageUrl(), 
					search.getId(), 
					myUser.getSocialnessOwn(), myUser.getSocialnessRts(), 
					myUser.getFeednessOwn(), myUser.getFeednessRts(), 
					myUser.getRatingOwn(), myUser.getRatingRts(),
					myUser.getTermVariationOwn(), myUser.getTermVariationRts(),
					myUser.getHashtagsOwn(), myUser.getHashtagsRts(),
					myUser.getRetweetRatioNew(), 
					myUser.getSearchRelatenessNew(),
					myUser.getTweets().size(),
					myUser.getRelatedTweets(search).size(),
					myUser.getRelatedTweetsOwn(search).size(),
					myUser.getRelatedTweetsRts(search).size()
					 });
			
			if (myUser.getRawJson() != null && !myUser.getRawJson().equals("")) {
				jdbcTemplate.update("INSERT IGNORE INTO RAW_USER (ID, SCREEN_NAME, RAW_JSON) VALUES (?,?,?)", 
						new Object[] { myUser.getId(), myUser.getScreenName(), myUser.getRawJson() }
						);
			}

		} catch (Exception e) {
			LogUtil.logger.error("failed", e);
			LogUtil.logger.error("exception while insert_user for tweet={}: {}", myUser.getScreenName(), e.toString());
		}
		
	}

	/*
	public void insertUser_(MyUser myUser, Search search) {
		
		try {

			jdbcTemplate.update("INSERT IGNORE INTO T_USER (ID, SCREEN_NAME, NAME, STATUSES_COUNT, FOLLOWERS_COUNT, FRIENDS_COUNT, LOCATION, DESCRIPTION, PROFILE_IMAGE_URL, SEARCH_ID, CHATTINESS, FEEDNESS, RETWEETED_MEAN, RETWEET_RATIO, TERM_VARIATION, SEARCH_RELATENESS, RATING_OF_RELATED_TWEETS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[] {
					
					myUser.getId(), myUser.getScreenName(), myUser.getName(), myUser.getStatusesCount(), myUser.getFollowersCount(), myUser.getFriendsCount(), 
					myUser.getLocation(), myUser.getDescription(), myUser.getProfileImageUrl(), 
					search.getId(), 
					myUser.getChattiness(), myUser.getFeedness(), myUser.getRetweetedMean(), 
					myUser.getRetweetRatio(), myUser.getTermVariation(), myUser.getSearchRelateness(), myUser.getRatingOfRelatedTweets()
					 });
			
			if (myUser.getRawJson() != null && !myUser.getRawJson().equals("")) {
				jdbcTemplate.update("INSERT IGNORE INTO RAW_USER (ID, SCREEN_NAME, RAW_JSON) VALUES (?,?,?)", 
						new Object[] { myUser.getId(), myUser.getScreenName(), myUser.getRawJson() }
						);
			}

		} catch (Exception e) {
			LogUtil.logger.error("failed", e);
			LogUtil.logger.error("exception while insert_tokens for tweet={}: {}", myUser.getScreenName(), e.toString());
		}
		
	}
	*/

	
	/**
	 * 
	 */
	public MyUser queryUser(String screenName, Search search) throws Exception {
		
		LogUtil.logger.info("dao.queryUser({})", screenName);
		
		return (jdbcTemplate.queryForObject(
				"SELECT id, screen_name, name, statuses_count, followers_count, friends_count, location, chattiness, feedness, retweeted_mean, retweet_ratio, term_variation FROM t_user WHERE screen_name = ? and search_id = ?" , new Object[]{ screenName, search.getId() }, 
				new BeanPropertyRowMapper<MyUser>(MyUser.class)) 
		);
	}
	
	/**
	 * 
	 */
	public List<MyTweet> queryTweets(String screenName, Search search) throws Exception {
		
		List<MyTweet> myTweets = jdbcTemplate.query(
				
				"SELECT type, id, screen_name, tweet, date, query_date, status, is_retweet, retweeted_id, retweeted_screen_name, reply_id, reply_screen_name, retweet_count, latitude, longitude, hashtags, mentions, urls, words" + 
				"  FROM v_tweet_search_tokens" + 
				" WHERE screen_name = ? and search_id = ? and status = 0 and type <> " + Search.SEARCH_TYPE_SEARCH, 
				new Object[]{screenName, search.getId()}, 
				
				new RowMapper<MyTweet>() {
			 		public MyTweet mapRow(ResultSet rs, int rowNum) throws SQLException {
			 			return MyTweet.createFromDB(rs.getString("id"), rs.getString("screen_name"), rs.getString("tweet"), rs.getTimestamp("date"), rs.getInt("retweet_count"), 
			 					                    JdbcUtil.getNullableDouble(rs,"latitude"), JdbcUtil.getNullableDouble(rs,"longitude"), 
			 					                    rs.getString("reply_id"), rs.getString("reply_screen_name"), 
			 					                    rs.getInt("is_retweet"), rs.getString("retweeted_id"), rs.getString("retweeted_screen_name"), 
			 					                    rs.getString("hashtags"), rs.getString("mentions"), rs.getString("urls"), rs.getString("words"));
			 		}
				}
		);
		
		return myTweets;
	}

	public void insertSearchResult(List<SearchResult> searchResults, Search search) {
		
		/*
		for (SearchResult searchResult : searchResults) {
			jdbcTemplate.update("UPDATE t_user SET score=? WHERE id=? AND search_id=?", 
					new Object[] {searchResult.getScore() , searchResult.getMyUser().getId(), search.getId()});
		}
		*/
		
		for (SearchResult searchResult : searchResults) {
			jdbcTemplate.update("UPDATE t_user SET score=? WHERE screen_name=? AND search_id=?", 
					new Object[] {searchResult.getScore() , searchResult.getMyUser().getScreenName(), search.getId()});
		}
		
		
	}

	public List<SearchResult> querySearchResults(Search search) {
		
		return jdbcTemplate.query(
				"SELECT id, screen_name, name, statuses_count, followers_count, friends_count, location, score, chattiness, feedness, retweeted_mean, retweet_ratio, term_variation FROM t_user WHERE search_id = ? ORDER BY score DESC" , 
				new Object[]{search.getId()},
				
				new RowMapper<SearchResult>() {
			 		public SearchResult mapRow(ResultSet rs, int rowNum) throws SQLException {
			 			
			 			MyUser user = new MyUser();
			 			user.setId(rs.getLong("id"));
			 			user.setScreenName(rs.getString("screen_name"));
			 			user.setName(rs.getString("name"));
			 			user.setStatusesCount(rs.getInt("statuses_count"));
			 			user.setFollowersCount(rs.getInt("followers_count"));
			 			user.setFriendsCount(rs.getInt("friends_count"));
			 			user.setLocation(rs.getString("location"));
			 			
			 			/*
			 			user.setChattiness(rs.getDouble("chattiness"));
			 			user.setFeedness(rs.getDouble("feedness"));
			 			user.setRetweetedMean(rs.getDouble("retweeted_mean"));
			 			user.setRetweetRatio(rs.getDouble("retweet_ratio"));
			 			user.setTermVariation(rs.getDouble("term_variation"));
			 			*/
			 			return new SearchResult(user, rs.getDouble("score"));
			 		}
				}
		);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Search> findWaitingSearchOrders() {
		
		return jdbcTemplate.query("SELECT id, query, semantic_search_id FROM t_search WHERE status = 1 ORDER BY id", new RowMapper<Search>() {
			@Override
			public Search mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				try {
					String semanticSearchId = rs.getString("semantic_search_id");
					
					Search search;
					if (semanticSearchId==null || semanticSearchId.equals("")) {
						search = new Search(rs.getInt(1), rs.getString(2));
					} else {
						search = new Search(rs.getInt(1), rs.getString(2), semanticSearchId);
					}
					return search;
					
				} catch (Exception e) {
					throw new SQLException(e);
				}
			}
		});
	}
	
	/**
	 * 
	 * @param search
	 * @param status
	 */
	public void updateSearchOrderStatus(Search search, int status) {
		
		switch (status) {
			case Search.SEARCH_STATUS_PROCESSING:
				jdbcTemplate.update("UPDATE t_search SET status=?, start_time=? WHERE id=?", new Object[] {status, search.getStartTime(), search.getId()});
				break;
			case Search.SEARCH_STATUS_FINISHED:
				jdbcTemplate.update("UPDATE t_search SET status=?, finish_time=? WHERE id=?", new Object[] {status, search.getFinishTime(), search.getId()});
				break;
			default:
				jdbcTemplate.update("UPDATE t_search SET status=? WHERE id=?", new Object[] {status, search.getId()});
				break;
		}
	}
	
	/**
	 * 
	 */
	public List<Edge> findEdges(Search search) {
		
		String sql = " select from_user, to_user, sum(weight) weight" + 
					 " from (" +
					   " select ut.screen_name from_user, tm.mention to_user, count(*) weight" +
					   " from v_search_result_user_tweets ut, t_tweet_mention tm " +
					   " where ut.search_id = ?" +
					   " and tm.tweet_id = ut.id " +
					   " and ut.is_retweet = 0" +
					   " and mention in (select screen_name from t_user where search_id = ?)" +
					   " group by from_user, to_user" +
					   " union all" +
					   " select screen_name from_user, retweeted_screen_name to_user, count(*) weight" +
					   " from v_search_result_user_tweets ut " +
					   " where ut.search_id = ?" +
					   " and ut.is_retweet = 1" +
					   " and ut.retweeted_screen_name in (select screen_name from t_user where search_id =  ? )" +
					   " group by from_user, to_user" +
					 " ) from_to_users" +
					 " group by from_user, to_user";
		
		return jdbcTemplate.query(
				sql , new Object[]{search.getId(), search.getId(), search.getId(), search.getId()},

				new RowMapper<Edge>() {
			 		public Edge mapRow(ResultSet rs, int rowNum) throws SQLException {
			 			return new Edge(rs.getString("from_user"), rs.getString("to_user"), rs.getInt("weight"));
			 		}
				}
		);		
	}

	/**
	 * 
	 */
	public void insertNetworkProperties(MyUser myUser, Search search) {
		jdbcTemplate.update("UPDATE t_user SET in_degree=?, out_degree=?, betweenness=?, closeness=? WHERE search_id=? AND screen_name=?", 
				new Object[] { 
				               myUser.getInDegree(), 
				               myUser.getOutDegree(), 
				               myUser.getBetweenness(), 
				               myUser.getCloseness(), 
				               search.getId(), myUser.getScreenName() 
				             }
		);
	}
	
	/**
	 * 
	 */
	public void insertNetworkProperties(Search search) {
		jdbcTemplate.update("UPDATE t_search SET avg_in_degree=?, avg_out_degree=?, avg_betweenness=?, avg_closeness=? WHERE id=?", 
				new Object[] { 
				               search.getAvgInDegree(), 
				               search.getAvgOutDegree(), 
				               search.getAvgBetweenness(),
				               search.getAvgCloseness(),
				               search.getId()
				             }
		);		
	}
	
	public List<semantic.Query> findWaitingKeywordOrders() {
		
		return jdbcTemplate.query("SELECT id, date, query, freebase_id, freebase_type, status FROM t_kw_query WHERE type=0 and status = " + semantic.Query.STATUS_WAITING + " ORDER BY id", new RowMapper<semantic.Query>() {
			public semantic.Query mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new semantic.Query(rs.getInt(1), rs.getDate(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
			}
		});
		
	}

	public semantic.Query findKeywordOrder(int id) {
		
		return jdbcTemplate.queryForObject("SELECT id, date, query, freebase_id, freebase_type, status FROM t_kw_query WHERE id = " + id , new RowMapper<semantic.Query>() {
			public semantic.Query mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new semantic.Query(rs.getInt(1), rs.getDate(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
			}
		});
	}
	
	public void insertRelatedTags(semantic.Query query, String searchTxt, String tag, int weight, int resourceId) {
		jdbcTemplate.update("INSERT INTO T_KW_TAGS (QUERY_ID, search_txt, TAG, WEIGHT, RESOURCE) VALUES (?,?,?,?,?)", new Object[] { query.getId(), searchTxt, tag, weight, resourceId});
	}
	
	public void updateKeywordOrder(semantic.Query query) {
		jdbcTemplate.update("UPDATE T_KW_QUERY SET status=? WHERE id=?", new Object[] {query.getStatus(), query.getId()});		
	}

}
