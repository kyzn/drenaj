SET GLOBAL log_bin_trust_function_creators = ON;

delimiter //

/* ------------------------------------------------------------------------------------------ */

drop function recommicro.split_str//

-- http://blog.fedecarg.com/2009/02/22/mysql-split-string-function/
CREATE FUNCTION recommicro.split_str(
  x TEXT,
  delim VARCHAR(12),
  pos INT
)
RETURNS VARCHAR(255)
RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(x, delim, pos),
       LENGTH(SUBSTRING_INDEX(x, delim, pos -1)) + 1),
       delim, '');
//

/* ------------------------------------------------------------------------------------------ */

drop function recommicro.num_of_occurrences//

-- http://www.bookofzeus.com/articles/count-the-number-of-occurrences-of-a-char-in-mysql/
CREATE FUNCTION recommicro.num_of_occurrences(str VARCHAR(2000), char_to_find VARCHAR(1))
RETURNS INT
DETERMINISTIC
	return LENGTH(str) - LENGTH(REPLACE(str, char_to_find, ''));
//

/* ------------------------------------------------------------------------------------------ */

drop procedure recommicro.insert_tokens//

CREATE PROCEDURE recommicro.insert_tokens (tweet_id VARCHAR(50), hashTagsSeparated VARCHAR(140), mentionsSeparated VARCHAR(140), urlsSeparated VARCHAR(2000), wordsSeparated VARCHAR(140))
BEGIN
    DECLARE token_i INT DEFAULT 0;
	DECLARE token_id INT DEFAULT 0;
	DECLARE token VARCHAR(140);

	IF hashTagsSeparated <> '' THEN
		-- handle hashtags
		SET token_i = recommicro.num_of_occurrences(hashTagsSeparated,' ') + 1;
		WHILE (token_i>0) DO
			SET token = recommicro.split_str(hashTagsSeparated, ' ', token_i);
			INSERT IGNORE INTO recommicro.t_token_hashtag(hashtag) VALUES ( token );
			
			SELECT id INTO token_id FROM recommicro.t_token_hashtag WHERE hashtag = token;
			INSERT IGNORE INTO recommicro.t_tweet_hashtag VALUES(tweet_id,token_id);
			
			SET token_i = token_i - 1;
		END WHILE;
	END IF;
	
	IF mentionsSeparated <> '' THEN
		-- handle mentions
		SET token_i = recommicro.num_of_occurrences(mentionsSeparated,' ') + 1;
		WHILE (token_i>0) DO
			SET token = recommicro.SPLIT_STR(mentionsSeparated, ' ', token_i);
			INSERT IGNORE INTO recommicro.t_tweet_mention VALUES(tweet_id,token);
			
			SET token_i = token_i - 1;
		END WHILE;
	END IF;
	
	IF urlsSeparated <> '' THEN	
		-- handle urls
		SET token_i = recommicro.num_of_occurrences(urlsSeparated,' ') + 1;
		WHILE (token_i>0) DO
			SET token = recommicro.SPLIT_STR(urlsSeparated, ' ', token_i);
			INSERT IGNORE INTO recommicro.t_token_url(url) VALUES ( token );
			
			SELECT id INTO token_id FROM recommicro.t_token_url WHERE url = token;
			INSERT IGNORE INTO recommicro.t_tweet_url VALUES(tweet_id,token_id);
			
			SET token_i = token_i - 1;
		END WHILE;
	END IF;
	
	IF wordsSeparated <> '' THEN	
		-- handle words
		SET token_i = recommicro.num_of_occurrences(wordsSeparated,' ') + 1;
		WHILE (token_i>0) DO
			SET token = recommicro.SPLIT_STR(wordsSeparated, ' ', token_i);
			INSERT IGNORE INTO recommicro.t_token_word(word) VALUES ( token );
			
			SELECT id INTO token_id FROM recommicro.t_token_word WHERE word = token;
			INSERT IGNORE INTO recommicro.t_tweet_word VALUES(tweet_id,token_id);
			
			SET token_i = token_i - 1;
		END WHILE;
	END IF;
	
END//

/* ------------------------------------------------------------------------------------------ */

CREATE OR REPLACE VIEW recommicro.v_hashtag_tweet AS
select t_tweet_hashtag.tweet_id, group_concat(t_token_hashtag.hashtag SEPARATOR ' ')  hashtags
from recommicro.t_token_hashtag, recommicro.t_tweet_hashtag
where t_token_hashtag.id = t_tweet_hashtag.hashtag_id
group by  t_tweet_hashtag.tweet_id
//

/* ------------------------------------------------------------------------------------------ */

CREATE OR REPLACE VIEW recommicro.v_mention_tweet AS
select t_tweet_mention.tweet_id, group_concat(t_tweet_mention.mention SEPARATOR ' ')  mentions
from recommicro.t_tweet_mention
group by t_tweet_mention.tweet_id
//

/* ------------------------------------------------------------------------------------------ */

CREATE OR REPLACE VIEW recommicro.v_url_tweet AS
select t_tweet_url.tweet_id, group_concat(t_token_url.url SEPARATOR ' ')  urls
from recommicro.t_token_url, recommicro.t_tweet_url
where t_token_url.id = t_tweet_url.url_id
group by  t_tweet_url.tweet_id
//

/* ------------------------------------------------------------------------------------------ */

CREATE OR REPLACE VIEW recommicro.v_word_tweet AS
select t_tweet_word.tweet_id, group_concat(recommicro.t_token_word.word SEPARATOR ' ')  words
from recommicro.t_token_word, recommicro.t_tweet_word
where t_token_word.id = t_tweet_word.word_id
group by  t_tweet_word.tweet_id
//

/* ------------------------------------------------------------------------------------------ */

CREATE OR REPLACE VIEW recommicro.v_tweet_tokens AS
SELECT t.*, h.hashtags, m.mentions, u.urls, w.words
FROM recommicro.t_tweet t 
	 LEFT JOIN recommicro.v_hashtag_tweet h ON t.id=h.tweet_id
	 LEFT JOIN recommicro.v_mention_tweet m ON t.id=m.tweet_id
	 LEFT JOIN recommicro.v_url_tweet u ON t.id=u.tweet_id
	 LEFT JOIN recommicro.v_word_tweet w ON t.id=w.tweet_id
//

CREATE OR REPLACE VIEW recommicro.v_tweet_search_tokens AS
SELECT search.id search_id, result.type, search.query, tokens.*
FROM recommicro.v_tweet_tokens tokens, recommicro.t_search search, recommicro.t_search_result result
WHERE tokens.id = result.tweet_id
AND result.search_id = search.id	
//

CREATE OR REPLACE VIEW recommicro.v_search_result AS
SELECT s.*, sr.tweet_id, t.tweet, t.screen_name
FROM t_search s, t_search_result sr, t_tweet t
WHERE s.id = sr.search_id
AND sr.type = 1
AND t.id = sr.tweet_id
//

CREATE OR REPLACE VIEW recommicro.v_search_result_user_tweets AS
SELECT sr.search_id, sr.type, t.*
FROM t_search_result sr, t_tweet t
WHERE sr.tweet_id = t.id
AND sr.type IN (2,3)
//

CREATE OR REPLACE VIEW recommicro.v_kw_tags  AS
SELECT query_id, tag, weight, resource, (select v_val from t_ref where id='kw_tags__resource' and i_key=resource) resource_desc
FROM t_kw_tags
//

/* ------------------------------------------------------------------------------------------ */
