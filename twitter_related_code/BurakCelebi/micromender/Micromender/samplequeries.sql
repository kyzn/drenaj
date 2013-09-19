SELECT * FROM t_search WHERE id=554 ORDER BY id;

select * FROM t_search_extended WHERE search_id=554;

select * FROM t_search_result WHERE search_id=554 order by type;

select * FROM t_user WHERE search_id=554;

select * FROM t_tweet WHERE id in (select tweet_id FROM t_search_result WHERE search_id=554 and type>1);

select * from raw_tweet group by screen_name;

