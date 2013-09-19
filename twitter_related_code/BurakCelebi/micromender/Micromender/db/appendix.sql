select id, query, start_time, finish_time, 
format(socialness_own_avg,2) socialness_own_avg, format(socialness_rts_avg,2) socialness_rts_avg, 
format(feedness_own_avg,2) feedness_own_avg, format(feedness_rts_avg,2) feedness_rts_avg, 
format(hashtags_own_avg,2) hashtags_own_avg, format(hashtags_rts_avg,2) hashtags_rts_avg, 
format(rating_own_avg,2) rating_own_avg, format(rating_rts_avg,2) rating_rts_avg, 
format(term_variation_own_avg,2) term_variation_own_avg, format(term_variation_rts_avg,2) term_variation_rts_avg
from t_search
where id = 491;

select tag, weight
from t_search_extended
where search_id = 491
and type = 2
order by weight desc, tag;


select GROUP_CONCAT(CONCAT(tag, ':', weight) ORDER BY WEIGHT DESC, TAG SEPARATOR ', ')
from t_search_extended
where search_id = 491
and type = 2
order by weight desc;


select
    ( select count(*) from t_user where search_id = 491) twitterers,
    ( select sum(num_of_tweets_fetched) from t_user where search_id = 491 ) tweets_fetched
;


select *
from t_eval_user_query
where search_id = 491;


select *
from t_eval_user
where id = 43;

