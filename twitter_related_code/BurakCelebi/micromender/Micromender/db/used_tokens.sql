
-- hashtags
select srut.screen_name, twh.tweet_id, th.hashtag
from v_search_result_user_tweets srut, t_tweet_hashtag twh, t_token_hashtag th
where srut.type in (2,3)
and srut.search_id = 28
and twh.tweet_id = srut.id
and twh.hashtag_id = th.id
order by hashtag;

-- words
select srut.screen_name, tww.tweet_id, tw.word
from v_search_result_user_tweets srut, t_tweet_word tww, t_token_word tw
where srut.type in (2,3)
and srut.search_id = 15
and tww.tweet_id = srut.id
and tww.word_id = tw.id
order by word;

-- mention and retweets
select srut.screen_name, tm.tweet_id, tm.mention mention_retweet
from v_search_result_user_tweets srut, t_tweet_mention tm
where srut.type in (2,3)
and srut.search_id = 15
and tm.tweet_id = srut.id
order by mention, screen_name, tweet_id;

-- urls
select srut.screen_name, twu.tweet_id, tu.url
from v_search_result_user_tweets srut, t_tweet_url twu, t_token_url tu
where srut.search_id = 5
and twu.tweet_id = srut.id
and twu.url_id = tu.id
order by url;

-- ----------------------------------------------------------

-- hashtag weights
select th.hashtag, count(*) weight
from v_search_result_user_tweets srut, t_tweet_hashtag twh, t_token_hashtag th
where srut.type in (2,3)
and srut.search_id = 5
and twh.tweet_id = srut.id
and twh.hashtag_id = th.id
-- and srut.screen_name = 'marin_dimitrov'
group by hashtag
order by weight desc, hashtag;


-- word weights
select tw.word, count(*) weight
from v_search_result_user_tweets srut, t_tweet_word tww, t_token_word tw
where srut.type in (2,3)
and srut.search_id = 3
and tww.tweet_id = srut.id
and tww.word_id = tw.id
and word <> ''
group by word
order by weight desc, word;

-- url weights
select tu.url, count(*) weight, group_concat(distinct srut.screen_name separator ' ' ) screen_names
from v_search_result_user_tweets srut, t_tweet_url twu, t_token_url tu
where srut.type in (2,3)
and srut.search_id = 5
and twu.tweet_id = srut.id
and twu.url_id = tu.id
-- and tu.url = 'data.gov'
group by url
order by weight desc, url;

-- screen_name - url weight
select srut.screen_name, tu.url, count(*) weight
from v_search_result_user_tweets srut, t_tweet_url twu, t_token_url tu
where srut.type in (2,3)
and srut.search_id = 5
and twu.tweet_id = srut.id
and twu.url_id = tu.id
and tu.url = 'http://www.ecuadorinmediato.com'
group by screen_name, url
order by weight desc;



-- url'i kullanan kisiler
select t.screen_name, count(*) weight
from t_tweet t, t_token_url u, t_tweet_url t_u, v_search_result_user_tweets srut
where srut.type in (2,3)
and srut.search_id = 3
and t.id = srut.id
and t_u.tweet_id = t.id
and t_u.url_id = u.id
-- and u.url = 'http://t.co/oxYZG38V'
group by screen_name
order by weight desc, screen_name;

-- -------------------------------------------------

select th.tweet_id, t.tweet, group_concat(h.hashtag SEPARATOR ' ') hashtags, ut.urls
from t_tweet_hashtag th, t_token_hashtag h, t_tweet t, v_url_tweet ut
where th.tweet_id in(
        select twu.tweet_id
        from v_search_result_user_tweets srut, t_tweet_url twu, t_token_url tu
        where srut.search_id = 3
        and twu.tweet_id = srut.id
        and twu.url_id = tu.id
        order by url)
and th.hashtag_id = h.id
and th.tweet_id = t.id
and ut.tweet_id = t.id
-- and urls = 'http://www.facebook.com/photo.php?v=1862867806359'
group by th.tweet_id;


select t.tweet, u.url, h.hashtag
from t_search s, t_search_result sr, t_tweet t, t_tweet_url tu, t_token_url u, t_tweet_hashtag th, t_token_hashtag h
where s.id = 3
and s.id = sr.search_id
and sr.type in (2,3)
and sr.tweet_id = t.id
and t.id = tu.tweet_id
and tu.tweet_id = th.tweet_id;



select t.*
from t_token_word w, t_tweet_word tw, t_tweet t, t_search_result sr
where w.id = tw.word_id
and tw.tweet_id = t.id
and w.word = '2pm'
and t.screen_name = 'erik_oswald'
and t.id = sr.tweet_id
and sr.search_id = 3;

select distinct sr.screen_name, sr.type
from t_search_result sr, t_user u
where sr.screen_name = u.screen_name
and sr.type in (2,3)
and sr.search_id = 3
order by sr.screen_name;

select words,hashtags
from v_tweet_search_tokens
where search_id = 3
and screen_name = 'erik_oswald';