select ut.screen_name from_user, tm.mention to_user, count(*)
from v_search_result_user_tweets ut, t_tweet_mention tm
where ut.search_id = 38
and ut.screen_name = 'chelseaingrey' -- new
and tm.tweet_id = ut.id
and ut.is_retweet = 0
-- and mention in (select screen_name from t_user where search_id = 38)
group by to_user
union all
select screen_name from_user, retweeted_screen_name to_user, count(*)
from v_search_result_user_tweets ut 
where ut.search_id = 38
and ut.screen_name = 'chelseaingrey' -- new
and ut.is_retweet = 1
-- and ut.retweeted_screen_name in (select screen_name from t_user where search_id = 38);
group by to_user;


            select user, sum(weight) weight 
					from 
					( 
            select tm.mention user, count(*) weight 
					 from v_search_result_user_tweets ut, t_tweet_mention tm 
					 where ut.search_id = 38 
					 and ut.screen_name = 'Nigel_Shadbolt' 
					 and tm.tweet_id = ut.id 
					 and ut.is_retweet = 0
					 group by user 
					 union all
					 select retweeted_screen_name user, count(*) weight 
					 from v_search_result_user_tweets ut 
					 where ut.search_id = 38
					 and ut.screen_name = 'Nigel_Shadbolt'
					 and ut.is_retweet = 1 
            group by user 
					 ) as users 
					 group by user 
					 order by weight desc;

select from_user, to_user, sum(weight) 
from (
    select ut.screen_name from_user, tm.mention to_user, count(*) weight
    from v_search_result_user_tweets ut, t_tweet_mention tm 
    where ut.search_id = 40
    and tm.tweet_id = ut.id 
    and ut.is_retweet = 0 
    and mention in (select screen_name from t_user where search_id = 40)
    group by from_user, to_user
    union all
    select screen_name from_user, retweeted_screen_name to_user, count(*) weight
    from v_search_result_user_tweets ut 
    where ut.search_id = 40
    and ut.is_retweet = 1 
    and ut.retweeted_screen_name in (select screen_name from t_user where search_id =  40 )
    group by from_user, to_user
) from_to_users
group by from_user, to_user;



select h.hashtag, ut.id, ut.screen_name, ut.tweet, is_retweet, retweeted_screen_name, retweet_count
from v_search_result_user_tweets ut, t_tweet_hashtag th, t_token_hashtag h
where ut.search_id = 30
and th.tweet_id = ut.id
and th.hashtag_id = h.id
and h.hashtag in (select tag from t_search_extended where search_id = 30 and type = 2)
and ut.screen_name in ('kerfors')
order by hashtag, id;

select *
from v_search_result_user_tweets ut
where ut.search_id = 30
and ut.screen_name in ('kerfors')
order by date desc;

select count(distinct screen_name)
from t_search_result
where search_id = 30
and type = 1;

-- -----------------------
select term, sum(weight) weight
from 
(
select h.hashtag term, count(*) weight
from v_search_result_user_tweets ut, t_token_hashtag h, t_tweet_hashtag th
where search_id = 28
and screen_name = 'ivan_herman'
and ut.id = th.tweet_id
and h.id = th.hashtag_id
group by h.id
union all
select w.word term, count(*) weight
from v_search_result_user_tweets ut, t_token_word w, t_tweet_word tw
where search_id = 28
and screen_name = 'ivan_herman'
and ut.id = tw.tweet_id
and w.id = tw.word_id
group by w.id
) as terms
group by term
order by weight desc;

select w.word, ut.tweet, ut.id
from v_search_result_user_tweets ut, t_token_word w, t_tweet_word tw
where search_id = 30
and screen_name = 'semwebcompany'
and ut.id = tw.tweet_id
and w.id = tw.word_id;





/*
4 rdfa 
2 semweb
1 class
1 linkeddata
1 opendata
*/

select *
from t_tweet_word
where tweet_id = 134216073812774912; -- fixme! sadece ilk hashtag, ilk word atılıyor!

select type, count(distinct screen_name) count
from t_search_result
where search_id = 12
group by type;

select concat('\'', replace(twitter_query, ' ', '\', \''), '\'') from t_search where id = 5;

select twitter_query from t_search where id = 5;

select count(*)
from t_user
where search_id = 28;

select *
from t_search_extended
where search_id = 28
and type = 2
order by weight desc;

SELECT id, screen_name, name, statuses_count, followers_count, friends_count, location, score, chattiness, feedness, retweeted_mean, retweet_ratio, term_variation 
FROM t_user 
WHERE search_id = 5 
ORDER BY score DESC;


select *
from t_user
where search_id = 5
order by score desc
limit 15;

-- hashgtags --------------------------------

select score
from t_user
where search_id = 28
order by score desc
limit 10;

-- most retweeted tweets having specified hashtags. search_id=5'te 1 tweet disinda hepsi extended user'lardan :)
select ut.id, ut.screen_name, ut.tweet, is_retweet, retweeted_screen_name, retweet_count
from v_search_result_user_tweets ut, t_tweet_hashtag th, t_token_hashtag h
where ut.search_id = 28
and ut.retweet_count > 1
and th.tweet_id = ut.id
and th.hashtag_id = h.id
and h.hashtag in (select tag from t_search_extended where search_id = 28 and type = 2)
/*
and (
    ut.screen_name in ( select screen_name from t_user where search_id = 5 and score > 0.074 order by score desc)
 or ut.retweeted_screen_name in (select screen_name from t_user where search_id = 5 and score > 0.07 order by score desc)
)
*/
order by ut.retweet_count desc, date desc;




-- desc
select ut.screen_name, h.hashtag, count(*) weight
from v_search_result_user_tweets ut, t_tweet_hashtag th, t_token_hashtag h
where ut.search_id = 5
and th.tweet_id = ut.id
and th.hashtag_id = h.id
and ut.screen_name in (select screen_name from t_user where search_id = 5 and score > 0.1 order by score desc) -- a retweet or a self-tweet
group by ut.screen_name, h.hashtag
order by screen_name, weight desc;

-- g.addNode("ivan_herman", {label:"ivan_herman", fill:"#56f"});
-- select concat('g.addNode("', screen_name, '", {label:"', screen_name, '", fill:"#33f", weight="50", "stroke-width"});') user_node
select concat('g.addNode("', screen_name, '", {label:"', screen_name, '", render:render});') user_node
from t_user
where screen_name in ('roylac','ivan_herman','faviki','manusporny','SemanticBlogs','planetrdf');

-- g.addEdge("ivan_herman","linkeddata", {label : "10" });
select concat('g.addEdge("', ut.screen_name, '","#', h.hashtag, '", {label:"', count(*), '"});') js
from v_search_result_user_tweets ut, t_tweet_hashtag th, t_token_hashtag h
where ut.search_id = 5
and th.tweet_id = ut.id
and th.hashtag_id = h.id
and ut.screen_name in ('roylac','ivan_herman','faviki','manusporny','SemanticBlogs','planetrdf') -- a retweet or a self-tweet
group by ut.screen_name, h.hashtag
order by screen_name;


-- burakcelebi -> "#linkeddata" [label="3"];
select concat(ut.screen_name, ' -> "#', h.hashtag, '" [label="', count(*), '"];') js
from v_search_result_user_tweets ut, t_tweet_hashtag th, t_token_hashtag h
where ut.search_id = 5
and th.tweet_id = ut.id
and th.hashtag_id = h.id
and ut.screen_name in ('roylac','ivan_herman','faviki','manusporny','SemanticBlogs','planetrdf') -- a retweet or a self-tweet
group by ut.screen_name, h.hashtag
order by screen_name;


select ut.screen_name, h.hashtag, count(*) weight
from v_search_result_user_tweets ut, t_tweet_hashtag th, t_token_hashtag h
where ut.search_id = 5
and th.tweet_id = ut.id
and th.hashtag_id = h.id
and ut.screen_name in ('roylac','ivan_herman','faviki','manusporny','SemanticBlogs','planetrdf')
-- and h.hashtag in ('semantic','web','rdf','api','data','database','interesante','java','linkeddata','metadata','microformats','ontology','opensource','owl','programming','rdfa','reference','semanticweb','standards','tools','uri','validator','w3c','web2.0','wiki','wikipedia','xml')
group by ut.screen_name, h.hashtag
order by screen_name, hashtag;


-- mentions --------------------------------

-- how many times people mentions others 

select user_from, user_to, sum(weight) from (
select ut.screen_name user_from, tm.mention user_to, count(*) weight
from v_search_result_user_tweets ut, t_tweet_mention tm
where ut.search_id = 28
and tm.tweet_id = ut.id
and ut.is_retweet = 0
and mention in (select screen_name from t_user where search_id = 28)
group by screen_name, mention
-- order by ut.screen_name, weight desc
union all
(select screen_name user_from, retweeted_screen_name user_to, count(*) weight
from v_search_result_user_tweets ut
where ut.search_id = 28
and ut.is_retweet = 1
and ut.retweeted_screen_name in (select screen_name from t_user where search_id = 28)
group by screen_name, retweeted_screen_name)
) as from_to
group by user_from, user_to;



-- order by ut.screen_name, weight desc;


-- http://www.infochimps.com/datasets/twitter-census-influence-metrics#api-docs_tab --------------

-- chattiness = (@mentions_out / tweets_out) 
select ut.screen_name, count(tm.mention) / count(ut.id) chattiness
from v_search_result_user_tweets ut
LEFT JOIN t_tweet_mention tm ON ut.id = tm.tweet_id
where ut.search_id = 5
and ut.type in (2,3)
and ut.is_retweet = 0
and ut.screen_name in ('roylac','ivan_herman','faviki','manusporny','SemanticBlogs','planetrdf') 
group by screen_name
order by chattiness desc, screen_name;

-- feedness = (total_links_seen / tweets_out)
select ut.screen_name, count(distinct tu.url_id) / count(ut.id) feedness 
from v_search_result_user_tweets ut
LEFT JOIN t_tweet_url tu ON ut.id = tu.tweet_id
where ut.search_id = 5
and ut.type in (2,3)
and ut.screen_name in ('roylac','ivan_herman','faviki','manusporny','SemanticBlogs','planetrdf') 
group by screen_name
order by feedness desc, screen_name;

-- retweetedMean = (retweets_in / tweets_out)
select ut.screen_name, sum(ut.retweet_count) / count(ut.id) sway 
from v_search_result_user_tweets ut
where ut.search_id = 5
and ut.type in (2,3)
and ut.is_retweet = 0 -- tweets_out
and ut.screen_name in ('roylac','ivan_herman','faviki','manusporny','SemanticBlogs','planetrdf') 
group by ut.screen_name
order by sway desc, screen_name;

-- tweet tarihleri. toplam kac gunde kac tweet? son tweet tarihi


-- test 34/40=0.85
select urls, id, tweet
from v_tweet_search_tokens
where search_id = 5
and screen_name = 'roylac'
and type in (2,3);


select *
from t_search;