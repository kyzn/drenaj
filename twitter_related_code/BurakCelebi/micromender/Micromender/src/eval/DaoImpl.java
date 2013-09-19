package eval;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import util.Util;

/**
 * 
 * Implementation of Data Access Object.
 * 
 */
public class DaoImpl implements Dao {

	private static final String RETWEET_RATIO_THRESHOLD = "0.7";

	private static final String SQL_TOP_REL_CONCEPTS =
			" select tag, weight " +
			" from t_search_extended " +
			" where search_id = ? " +
			" and type = 1 " +
			" order by weight desc, tag";
	
	private static final String SQL_REL_CONCEPTS =
	" select GROUP_CONCAT(CONCAT(tag, ':', weight) ORDER BY WEIGHT DESC, TAG SEPARATOR ', ') " +
	" from t_search_extended " +
	" where search_id = ? " +
	" and type = 2 " +
	" order by weight desc";
	
	private static final String SQL_RECOM_RESULT =
	"	select r.screen_name, " + 
	"	    if (tu.new_score is null, '\\\\resultNwrC', " +
	"	        if ( ((tu.term_variation_own >= 3 and tu.retweet_ratio_new<0.6) OR (tu.retweet_ratio_new>=0.6 AND tu.term_variation_rts >= 3 ) ), " + 
	"	        '\\\\resultR',  " +
	"	        '\\\\resultNwrTv') " +
	"	    ) r, " +
	"       if(r.eval=2,'\\\\evalS', if(r.eval=1,'\\\\evalPs', if(r.eval=0,'\\\\evalNs', if(r.eval=-2,'\\\\evalNa', '')))) eval, " +
	"	    format(tu.new_score,1) new_score,  " +
	"	    format(tu.score,2) score,  " +
	"	    format(socialness_own_norm,1) socialness_own_norm, " + 
	"	    format(socialness_rts_norm,1) socialness_rts_norm, " +
	"	    format(feedness_own_norm,1) feedness_own_norm,  " +
	"	    format(feedness_rts_norm,1) feedness_rts_norm, " +
	"	    format(hashtags_own_norm,1) hashtags_own_norm,  " +
	"	    format(hashtags_rts_norm,1) hashtags_rts_norm, " +
	"	    format(rating_own_norm,1) rating_own_norm,  " +
	"	    format(rating_rts_norm,1) rating_rts_norm, " +
	"	    format(term_variation_own_norm,1) term_variation_own_norm, " + 
	"	    format(term_variation_rts_norm,1) term_variation_rts_norm, " +
	"	    format(retweet_ratio_new,1) retweet_ratio_new " +
	"	from t_eval_recom r, t_eval_user_query uq, t_eval_user eu, t_user tu, t_user_recom ur " +
	"	where uq.id = ? " +
	"	and uq.id = eval_user_query_id " + 
	"   and uq.user_id = eu.id " +
	"	and tu.search_id = uq.search_id " +
	"	and tu.screen_name = r.screen_name " +
	"	and tu.search_id = ur.search_id " +
	"	and tu.screen_name = ur.screen_name " +
	"	order by r.eval desc, recom, email, uq.id, new_score desc, score desc "
	;
	
	
	private static final String SQL_BROADER =
					" select broad_category, query_count, format((100*query_count/ (select count(*) from t_eval_user_query_detail)),1) pct from ( " +
					"  select broad_category , count(query_id) query_count " +
					" from t_eval_user_query_detail " +
					" group by broad_category) t order by query_count desc";

	private static final String SQL_BROADER_RESULT = 
			
					" select broad_category, " +
					"        query_count,  " +
					"        format( (100*query_count/ (select count(*) from t_eval_user_query_detail)) ,1) pct, " +
					"        sum_satisfied, " +
					"        sum_partially_satisfied, " +
					"        sum_not_satisfied, " +
					"        sum_not_applicable, " +
					"        sum_rec, " +
					"        sum_rec_satisfied, " +
					"        sum_rec_partially_satisfied, " +
					"        sum_rec_not_satisfied, " +
					"        sum_rec_not_applicable, " +
					"        format( (100 * sum_rec_satisfied / sum_rec) ,1) pct_rec_satisfied, " +
					"        format( (100 * sum_rec_partially_satisfied / sum_rec) ,1) pct_rec_partially_satisfied, " +
					"        format( (100 * (sum_rec_satisfied + sum_rec_partially_satisfied) / sum_rec) ,1) pct_rec_satisfied_partially_satisfied, " +
					"        format( (100 * sum_rec_not_satisfied / sum_rec) ,1) pct_rec_not_satisfied, " +
					"        format( (100 * sum_rec_not_applicable / sum_rec) ,1) pct_rec_not_applicable " +
					" from( " +
					"     select broad_category,  " +
					" 	       count(broad_category) query_count,  " +
					"            sum(not_applicable) sum_not_applicable,  " +
					"            sum(not_satisfied) sum_not_satisfied,  " +
					"            sum(partially_satisfied) sum_partially_satisfied,  " +
					"            sum(satisfied) sum_satisfied, " +
					"            sum(rec) sum_rec, " +
					"            sum(rec_satisfied) sum_rec_satisfied, " +
					"            sum(rec_partially_satisfied) sum_rec_partially_satisfied, " +
					"            sum(rec_not_satisfied) sum_rec_not_satisfied, " +
					"            sum(rec_not_applicable) sum_rec_not_applicable " +
					"     from( " +
					"         select uq.query, uq_det.*,  " +
					"             (select count(*) from t_eval_recom where eval_user_query_id = uq.id and eval = -2) not_applicable, " +
					"             (select count(*) from t_eval_recom where eval_user_query_id = uq.id and eval = 0) not_satisfied, " +
					"             (select count(*) from t_eval_recom where eval_user_query_id = uq.id and eval = 1) partially_satisfied, " +
					"             (select count(*) from t_eval_recom where eval_user_query_id = uq.id and eval = 2) satisfied, " +
					"             (select count(u.screen_name) " +
					"                                                                 from t_user u, t_eval_recom r " +
					"                                                                where r.eval_user_query_id = uq.id " +
					"                                                                  and u.search_id = uq.search_id " +
					"                                                                  and u.screen_name = r.screen_name " +
					"                                                                  and u.num_of_tweets_fetched >= 50  " +
					" 																   and ( (u.new_score is not null and u.retweet_ratio_new< " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_own >= 3) OR (u.new_score is not null and u.retweet_ratio_new>= " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_rts >= 3) ) "+ 
					"                                                                  and u.new_score is not null " +
					"                                                                  ) rec, " +
					"             (select count(u.screen_name) " +
					"                                                                 from t_user u, t_eval_recom r " +
					"                                                                where r.eval_user_query_id = uq.id " +
					"                                                                  and r.eval = 2 " +
					"                                                                  and u.search_id = uq.search_id " +
					"                                                                  and u.screen_name = r.screen_name " +
					"                                                                  and u.num_of_tweets_fetched >= 50  " +
					" 																   and ( (u.new_score is not null and u.retweet_ratio_new< " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_own >= 3) OR (u.new_score is not null and u.retweet_ratio_new>= " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_rts >= 3) ) "+
					"                                                                  and u.new_score is not null " +
					"                                                                  ) rec_satisfied, " +
					"             (select count(u.screen_name) " +
					"                                                                 from t_user u, t_eval_recom r " +
					"                                                                where r.eval_user_query_id = uq.id " +
					"                                                                  and r.eval = 1 " +
					"                                                                  and u.search_id = uq.search_id " +
					"                                                                  and u.screen_name = r.screen_name " +
					"                                                                  and u.num_of_tweets_fetched >= 50  " +
					" 																   and ( (u.new_score is not null and u.retweet_ratio_new< " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_own >= 3) OR (u.new_score is not null and u.retweet_ratio_new>= " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_rts >= 3) ) "+
					"                                                                  and u.new_score is not null " +
					"                                                                  ) rec_partially_satisfied, " +
					"             (select count(u.screen_name) " +
					"                                                                 from t_user u, t_eval_recom r " +
					"                                                                where r.eval_user_query_id = uq.id " +
					"                                                                  and r.eval = 0 " +
					"                                                                  and u.search_id = uq.search_id " +
					"                                                                  and u.screen_name = r.screen_name " +
					"                                                                  and u.num_of_tweets_fetched >= 50  " +
					" 																   and ( (u.new_score is not null and u.retweet_ratio_new< " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_own >= 3) OR (u.new_score is not null and u.retweet_ratio_new>= " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_rts >= 3) ) "+
					"                                                                  and u.new_score is not null " +
					"                                                                  ) rec_not_satisfied, " +
					"             (select count(u.screen_name) " +
					"                                                                 from t_user u, t_eval_recom r " +
					"                                                                where r.eval_user_query_id = uq.id " +
					"                                                                  and r.eval = -2 " +
					"                                                                  and u.search_id = uq.search_id " +
					"                                                                  and u.screen_name = r.screen_name " +
					"                                                                  and u.num_of_tweets_fetched >= 50  " +
					" 																   and ( (u.new_score is not null and u.retweet_ratio_new< " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_own >= 3) OR (u.new_score is not null and u.retweet_ratio_new>= " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_rts >= 3) ) "+
					"                                                                  and u.new_score is not null " +
					"                                                                  ) rec_not_applicable " +
					"         from t_eval_user_query_detail uq_det, t_eval_user_query uq " +
					"         where uq.id = uq_det.query_id " +
					"     ) t1 " +
					"     group by broad_category " +
					" ) t2 " +
					" order by query_count desc";

	private static final String TESTER_AGES = 
			" select age_interval, count, format(100*count/(select count(*) from t_eval_user where status=0),1) pct from ( " +
			" select  " +
			    " age_interval as age_interval_id, " +
			    " (select v_val from t_ref where id='t_eval_user__age_interval' and i_key=age_interval) age_interval, " +
			    " count(*) count " +
			" from t_eval_user " +
			" where status = 0 " +
			" group by age_interval " +
			" ) t order by age_interval_id "
	;
	
	private static final String TESTER_GENDER =
			" select gender, count, format(100*count/(select count(*) from t_eval_user where status=0),1) pct from ( " + 
			" select gender, count(*) count " +
			" from t_eval_user " +
			" where status=0 " +
			" group by gender " +
			" ) t order by count desc"
	;
	

	private static final String TESTER_EDU = 
			" select edu_level, count, format(100*count/(select count(*) from t_eval_user where status=0),1) pct from ( " +
					" select  " +
					    " edu_level as edu_level_id, " +
					    " (select v_val from t_ref where id='t_eval_user__edu_level' and i_key=edu_level) edu_level, " +
					    " count(*) count " +
					" from t_eval_user " +
					" where status=0 " +
					" group by edu_level " +
					" ) t order by edu_level_id "
	;
	
	private static final String TESTER_STU = 
			" select is_student, count, format(100*count/(select count(*) from t_eval_user where status=0),1) pct from ( " +
					" select (select v_val from t_ref where id='t_eval_user__is_student' and i_key=is_student) is_student, count(*) count " +
					" from t_eval_user " +
					" where status=0 " +
					" group by is_student " +
					" ) t1 order by count desc "
	;
	
	private static final String TESTER_PROF = 
			" select profession, count, format(100*count/(select count(*) from t_eval_user where status=0),2) pct from ( " +
					" select  " +
					       " (select v_val from t_ref where id='t_eval_user__profession' and i_key=profession) profession, " +
					    " count(*) count " +
					" from t_eval_user " +
					" where status=0 " +
					" group by profession " +
					" ) t order by count desc "
	;

	private static final String TEST_USER_QUERY_COUNTS = 
			" select * from ( " + 
			" 		select user_id,  " + 
			" 		       count(id) query_count, " + 
			" 		       (select count(distinct broad_category) from t_eval_user_query_detail where query_id in (select id from t_eval_user_query where user_id = uq.user_id)) query_category_count " + 
			" 		from t_eval_user_query uq " + 
			" 		where status = 2 " + 
			" 		group by user_id " + 
			" 		) t order by query_count desc"
	;
	
	private static final String QUERY_EXP =
			" select *, (satisfied+partially_satisfied+not_satisfied+not_applicable) total from ( " +
			" select expansion_grade,  " +
			" format(100 * sum_satisfied/sum_all,1) satisfied, " +
			" format(100 * sum_partially_satisfied/sum_all,1) partially_satisfied, " +
			" format(100 * sum_not_satisfied/sum_all,1) not_satisfied, " +
			" format(100 * sum_not_applicable/sum_all,1) not_applicable " +
			" from ( " +
			" select expansion_grade, " +
			" sum(satisfied) sum_satisfied, " +
			"      sum(partially_satisfied) sum_partially_satisfied, " +
			"      sum(not_satisfied) sum_not_satisfied, " +
			"      sum(not_applicable) sum_not_applicable, " +
			"      sum(satisfied)+sum(partially_satisfied)+sum(not_satisfied)+sum(not_applicable) sum_all from ( " +
			" select uq.query, uq_det.*,  " +
			" (select GROUP_CONCAT(tag ORDER BY weight DESC SEPARATOR ' | ') from t_search_extended where search_id = (select search_id from t_eval_user_query where id=uq.id) and type = 1) keywords, " +
			"   (select count(*) from t_eval_recom where eval_user_query_id = uq.id and eval = -2) not_applicable, " +
			"   (select count(*) from t_eval_recom where eval_user_query_id = uq.id and eval = 0) not_satisfied, " +
			"   (select count(*) from t_eval_recom where eval_user_query_id = uq.id and eval = 1) partially_satisfied, " +
			"   (select count(*) from t_eval_recom where eval_user_query_id = uq.id and eval = 2) satisfied " +
			" from t_eval_user_query_detail uq_det, t_eval_user_query uq " +
			" where uq.id = uq_det.query_id " +
			" and uq.status = 2 " +
			" and expansion_grade = '%s' " +
			" ) t1 ) t2 ) t3";
	
	private static final String 
			QUERY_EXP_PS = QUERY_EXP.replaceAll("%s", "partially satisfied"),
			QUERY_EXP_S  = QUERY_EXP.replaceAll("%s", "satisfied"),
			QUERY_EXP_NS = QUERY_EXP.replaceAll("%s", "not satisfied"),
			QUERY_EXP_NA = QUERY_EXP.replaceAll("%s", "n/a");

	private static final String SQL_ITER_IMPACT = 
			" select iter, count(iter) count from ( " +
			" 	select (select min(type) from t_search_result where search_id = tu.search_id and screen_name = tu.screen_name and type>1)-1 iter " +
			" 		from t_eval_recom r, t_eval_user_query uq, t_user tu " +
			" 		where r.eval_user_query_id = uq.id " +
			" 		and tu.search_id = uq.search_id " +
			" 		and tu.screen_name = r.screen_name " +
			" 		and r.eval = 2 " +
			" 		and uq.status = 2 " +
			" 		and uq.user_id in (select id from t_eval_user where status=0) " +
			" 	) t group by iter"
			;

	private static final String SQL_SPECIFIC_QUERIES = 
			" select uq.id, uq.query, uqd.broad_category, uqd.expansion_grade " +
			" from t_eval_user_query_detail uqd, t_eval_user_query uq " +
			" where uqd.query_type_info like '%specific%' " +
			" and uqd.query_id = uq.id " +
			" order by broad_category "
			;

	private static final String SQL_NO_QUERY_EXP = 
			" select uq.query, uqd.broad_category " + 
			" from t_eval_user_query_detail uqd, t_eval_user_query uq " +  
			" where expansion_grade = 'n/a' " + 
			" and uqd.query_id = uq.id " + 
			" order by broad_category "
	;

	private static final String SQL_EVAL_USER_QUERIES = 
			" select uq.user_id, uq.id, uq.query " +
			" from t_eval_user_query uq, t_eval_user u " +
			" where u.id = ? " +
			" and u.id = uq.user_id " +
			" and u.status = 0 " +
			" and uq.status = 2 " +
			" order by user_id, id "
	;

	private static final String SQL_EVAL_USERS = 
			" select distinct u.id " +
			" from t_eval_user_query uq, t_eval_user u " +
			" where u.id = uq.user_id " +
			" and u.status = 0 " +
			" and uq.status = 2 " +
			" order by user_id, id "
	;
			
	private JdbcTemplate jdbcTemplate;
	
	/*
	public static final String EVAL_KEYWORD_NA = "n/a";
	public static final String EVAL_KEYWORD_NS = "not satisfied";
	public static final String EVAL_KEYWORD_PS = "partially satisfied";
	public static final String EVAL_KEYWORD_S = "satisfied";
	*/
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void insertEvalDetail(int id, String types, String typeDetails, String expansionGrade) {
		jdbcTemplate.update("INSERT INTO t_eval_user_query_detail (query_id, query_type, query_type_info, expansion_grade) VALUES (?,?,?,?)", new Object[] {id,types,typeDetails,expansionGrade});
	}
	
	public void updateEvalDetail(int id, String broadCategory) {
		jdbcTemplate.update("UPDATE t_eval_user_query_detail set broad_category=? where query_id=?", new Object[] {broadCategory, id});
	}
	
	public String findTopRelatedKeywords(int evalId) {
		String keywords =  jdbcTemplate.queryForObject("select GROUP_CONCAT(tag ORDER BY weight DESC SEPARATOR ' | ') from t_search_extended where search_id = (select search_id from t_eval_user_query where id=? ) and type = 1;", new Object[]{evalId}, String.class);
		return keywords==null ? "" : keywords;
	}
	
	public List<Map<String, Object>> findTestUserQueryCounts() {
		return jdbcTemplate.queryForList(TEST_USER_QUERY_COUNTS);
	}
	
	public List<Map<String, Object>> findTestUserAges() {
		return jdbcTemplate.queryForList(TESTER_AGES);
	}
	
	public List<Map<String, Object>> findTestUserGender() {
		return jdbcTemplate.queryForList(TESTER_GENDER);
	}
	
	public List<Map<String, Object>> findTestUserEdu() {
		return jdbcTemplate.queryForList(TESTER_EDU);
	}
	
	public List<Map<String, Object>> findTestUserIsStudent() {
		return jdbcTemplate.queryForList(TESTER_STU);
	}
	
	public List<Map<String, Object>> findTestUserProf() {
		return jdbcTemplate.queryForList(TESTER_PROF);
	}
	
	public List<Map<String, Object>> findBroadCategories() {
		return jdbcTemplate.queryForList(SQL_BROADER);
	}
	
	public List<Map<String, Object>> findBroadCategoryResults() {
		return jdbcTemplate.queryForList(SQL_BROADER_RESULT);
	}
	
	public  List<Map<String, Object>>  findQueryExpansionImpact() {
		
		List<Map<String, Object>> result = jdbcTemplate.queryForList(QUERY_EXP_S);
		
		result.addAll(jdbcTemplate.queryForList(QUERY_EXP_PS));
		result.addAll(jdbcTemplate.queryForList(QUERY_EXP_NS));
		result.addAll(jdbcTemplate.queryForList(QUERY_EXP_NA));
		
		return result;
	}
	
	public List<Map<String, Object>> findNoQueryExpansions() {
		return jdbcTemplate.queryForList(SQL_NO_QUERY_EXP);
	}
	
	public  List<Map<String, Object>> findIterationImpact() {
		return jdbcTemplate.queryForList(SQL_ITER_IMPACT);
	}
	
	public List<Map<String, Object>> findSpecificQueries() {
		return jdbcTemplate.queryForList(SQL_SPECIFIC_QUERIES);
	}
	
	
	@Override
	public int countRecommended(Eval eval, List<Integer> evalQueryIdList) {
		return jdbcTemplate.queryForInt(buildCountSqlForRecommended(eval, evalQueryIdList));
	}

	@Override
	public int countNotRecommended(Eval eval, NotRecommendedReason reason, List<Integer> evalQueryIdList) {
		return jdbcTemplate.queryForInt(buildCountSqlForNotRecommended(eval, reason, evalQueryIdList));
	}

	@Override
	public int count(Eval eval, List<Integer> evalQueryIdList) {
		return jdbcTemplate.queryForInt(buildCountSql(eval, false, null, null, evalQueryIdList));
	}
	
	/*
	@Override
	public int countRecommended(Eval eval) {
		return jdbcTemplate.queryForInt(buildCountSqlForRecommended(eval, null));
	}

	@Override
	public int countNotRecommended(Eval eval, NotRecommendedReason reason) {
		return jdbcTemplate.queryForInt(buildCountSqlForNotRecommended(eval, reason, null));
	}

	@Override
	public int count(Eval eval) {
		return jdbcTemplate.queryForInt(buildCountSql(eval, false, null, null, null));
	}
	*/
	
	private static String buildCountSqlForRecommended(Eval eval, List<Integer> evalQueryIdList) {
		return buildCountSql(eval, true, true, null, evalQueryIdList);
	}
	
	private static String buildCountSqlForNotRecommended(Eval eval, NotRecommendedReason notRecommendedReason, List<Integer> evalQueryIdList) {
		return buildCountSql(eval, true, false, notRecommendedReason, evalQueryIdList);
	}
	
	private static String buildCountSql(Eval eval, boolean withMicromender, Boolean isRecommended, NotRecommendedReason notRecommendedReason, List<Integer> evalQueryIdList) {
		
		String sql = 
				"  select count(r.id) "
				+ "  from t_eval_recom r, t_eval_user_query uq, t_search s, t_user u ";

		if (Eval.ANY==eval) {
			sql += " where r.eval = r.eval ";
		} else {
			sql += " where r.eval = " + eval.getVal();
		}
				
		sql +=	
			  "   and r.eval_user_query_id = uq.id "
			+ "   and uq.search_id = s.id "
			+ "   and r.screen_name = u.screen_name "
			+ "   and u.search_id = s.id "
			+ "   and u.search_id = u.search_id ";
		
		if(withMicromender) {
			if(isRecommended) {
				sql += "   and ( (u.new_score is not null and u.retweet_ratio_new< " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_own >= 3) OR (u.new_score is not null and u.retweet_ratio_new>= " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_rts >= 3) ) ";
			} else {
				switch (notRecommendedReason) {
				case ALL:
					sql += "   and ((u.new_score is null) OR (u.new_score is not null and u.retweet_ratio_new< " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_own < 3) OR (u.new_score is not null and u.retweet_ratio_new>= " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_rts < 3)) ";
					break;
				case CONTENT_BASED_SCORE:
					sql += "   and u.new_score is null";
					break;
				case TERM_VARIATION:
					sql += "   and ((u.new_score is not null and u.retweet_ratio_new< " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_own < 3) OR (u.new_score is not null and u.retweet_ratio_new>= " + RETWEET_RATIO_THRESHOLD + " and u.term_variation_rts < 3)) ";
					break;
				default:
					break;
				}
			}
		} 
 
		if (evalQueryIdList!=null) {
			sql += " and uq.id in ( " + Util.joinWithComma(evalQueryIdList) + " ) ";
		}
		
		System.out.println(sql);
		System.out.println("------------------------");
		
		return sql;
	}

	public List<Map<String, Object>> findUserQueryResult(int evalUserQueryId) {
		return jdbcTemplate.queryForList(SQL_RECOM_RESULT, new Object[]{evalUserQueryId});
	}
	
	public List<Map<String, Object>> findEvalUserQueries(int userId) {
		return jdbcTemplate.queryForList(SQL_EVAL_USER_QUERIES, new Object[]{userId});
	}
	
	public List<Integer> findEvalUsers() {
		return jdbcTemplate.query(SQL_EVAL_USERS, new RowMapper<Integer>() {
			      public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
			        return resultSet.getInt(1);
			      }
		});
	}
	
	public List<Map<String, Object>> findTopRelatedConcepts(int searchId) {
		return jdbcTemplate.queryForList(SQL_TOP_REL_CONCEPTS, new Object[]{searchId});
	}
	
	public String findRelatedConcepts(int searchId) {
		return jdbcTemplate.queryForObject(SQL_REL_CONCEPTS, new Object[]{searchId}, String.class);
	}
}
