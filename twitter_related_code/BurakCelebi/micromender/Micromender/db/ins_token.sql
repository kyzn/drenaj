SET GLOBAL log_bin_trust_function_creators = ON;

delimiter //


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