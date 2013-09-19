-- phpMyAdmin SQL Dump
-- version 3.4.3.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Feb 26, 2012 at 07:51 PM
-- Server version: 5.5.15
-- PHP Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `recommicro`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_tokens`(tweet_id VARCHAR(50), hashTagsSeparated VARCHAR(140), mentionsSeparated VARCHAR(140), urlsSeparated VARCHAR(2000), wordsSeparated VARCHAR(140))
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
	
END$$

--
-- Functions
--
CREATE DEFINER=`root`@`localhost` FUNCTION `num_of_occurrences`(str VARCHAR(2000), char_to_find VARCHAR(1)) RETURNS int(11)
    DETERMINISTIC
return LENGTH(str) - LENGTH(REPLACE(str, char_to_find, ''))$$

CREATE DEFINER=`root`@`localhost` FUNCTION `split_str`(
  x TEXT,
  delim VARCHAR(12),
  pos INT
) RETURNS varchar(255) CHARSET latin1
RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(x, delim, pos),
       LENGTH(SUBSTRING_INDEX(x, delim, pos -1)) + 1),
       delim, '')$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `raw_tweet`
--

CREATE TABLE IF NOT EXISTS `raw_tweet` (
  `id` varchar(50) CHARACTER SET latin1 NOT NULL,
  `screen_name` varchar(20) CHARACTER SET latin1 NOT NULL,
  `query_date` text CHARACTER SET latin1,
  `raw_json` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `raw_user`
--

CREATE TABLE IF NOT EXISTS `raw_user` (
  `id` int(11) NOT NULL,
  `screen_name` varchar(20) NOT NULL,
  `query_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `raw_json` text NOT NULL,
  PRIMARY KEY (`id`,`query_date`),
  KEY `screen_name` (`screen_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `t_eval_recom`
--

CREATE TABLE IF NOT EXISTS `t_eval_recom` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `eval_user_query_id` int(10) NOT NULL,
  `rec_source` tinyint(1) NOT NULL,
  `screen_name` varchar(20) NOT NULL,
  `eval` int(1) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=134 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_eval_recom_eval`
--

CREATE TABLE IF NOT EXISTS `t_eval_recom_eval` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `eval_recom_id` int(10) NOT NULL,
  `screen_name` varchar(20) DEFAULT NULL,
  `eval_user_id` int(10) NOT NULL,
  `eval_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `eval` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_eval_user`
--

CREATE TABLE IF NOT EXISTS `t_eval_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(10) NOT NULL,
  `screen_name` varchar(20) DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_eval_user_query`
--

CREATE TABLE IF NOT EXISTS `t_eval_user_query` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `user_id` int(2) NOT NULL,
  `query` varchar(50) NOT NULL,
  `search_id` int(10) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `eval_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_kw_query`
--

CREATE TABLE IF NOT EXISTS `t_kw_query` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `query` varchar(50) NOT NULL,
  `freebase_id` varchar(255) NOT NULL,
  `freebase_type` varchar(255) NOT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  `type` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=55 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_kw_tags`
--

CREATE TABLE IF NOT EXISTS `t_kw_tags` (
  `query_id` int(10) NOT NULL,
  `search_txt` varchar(255) NOT NULL,
  `tag` varchar(100) CHARACTER SET utf8 NOT NULL,
  `weight` int(10) NOT NULL,
  `resource` int(1) NOT NULL,
  PRIMARY KEY (`query_id`,`search_txt`,`tag`,`weight`,`resource`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `t_ref`
--

CREATE TABLE IF NOT EXISTS `t_ref` (
  `id` varchar(50) NOT NULL,
  `i_key` int(2) NOT NULL,
  `v_val` varchar(50) NOT NULL,
  PRIMARY KEY (`id`,`i_key`,`v_val`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `t_search`
--

CREATE TABLE IF NOT EXISTS `t_search` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `start_time` timestamp NULL DEFAULT NULL,
  `query` varchar(100) NOT NULL,
  `semantic_search_id` varchar(10) NOT NULL,
  `finish_time` timestamp NULL DEFAULT NULL,
  `avg_in_degree` double DEFAULT NULL,
  `avg_out_degree` double DEFAULT NULL,
  `avg_betweenness` double DEFAULT NULL,
  `avg_closeness` double DEFAULT NULL,
  `twitter_query` text,
  `vector_query` text,
  `status` tinyint(1) DEFAULT '1' COMMENT '0=invalid\n1=entry\n2=processing\n3=finished',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=172 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_search_extended`
--

CREATE TABLE IF NOT EXISTS `t_search_extended` (
  `search_id` int(10) NOT NULL,
  `resource` varchar(1) NOT NULL,
  `search_txt` varchar(255) NOT NULL,
  `tag` varchar(100) NOT NULL,
  `weight` int(10) DEFAULT NULL,
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1=top tag\n2=tag',
  PRIMARY KEY (`search_id`,`tag`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `t_search_result`
--

CREATE TABLE IF NOT EXISTS `t_search_result` (
  `search_id` int(10) NOT NULL,
  `tweet_id` varchar(50) NOT NULL,
  `screen_name` varchar(20) DEFAULT NULL,
  `type` tinyint(1) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`search_id`,`tweet_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `t_tag`
--

CREATE TABLE IF NOT EXISTS `t_tag` (
  `id` int(10) NOT NULL,
  `tag` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `t_token_hashtag`
--

CREATE TABLE IF NOT EXISTS `t_token_hashtag` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `hashtag` varchar(140) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `hashtag` (`hashtag`),
  KEY `hashtag_2` (`hashtag`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=227095 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_token_url`
--

CREATE TABLE IF NOT EXISTS `t_token_url` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `link` (`url`),
  KEY `url` (`url`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=255510 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_token_word`
--

CREATE TABLE IF NOT EXISTS `t_token_word` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word` varchar(140) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `word` (`word`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4076596 ;

-- --------------------------------------------------------

--
-- Table structure for table `t_tweet`
--

CREATE TABLE IF NOT EXISTS `t_tweet` (
  `id` varchar(50) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `screen_name` varchar(20) NOT NULL,
  `tweet` varchar(200) CHARACTER SET latin5 NOT NULL,
  `date` timestamp NULL DEFAULT NULL,
  `query_date` text,
  `status` int(1) NOT NULL DEFAULT '0',
  `is_retweet` int(1) NOT NULL DEFAULT '0',
  `retweeted_id` varchar(50) DEFAULT NULL,
  `retweeted_screen_name` varchar(20) DEFAULT NULL,
  `reply_id` varchar(50) DEFAULT NULL,
  `reply_screen_name` varchar(20) DEFAULT NULL,
  `retweet_count` int(11) DEFAULT NULL,
  `latitude` double(9,6) DEFAULT NULL,
  `longitude` double(9,6) DEFAULT NULL,
  `raw_json` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `screen_name` (`screen_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `t_tweet_backup`
--

CREATE TABLE IF NOT EXISTS `t_tweet_backup` (
  `id` varchar(50) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `screen_name` varchar(20) NOT NULL,
  `tweet` varchar(200) CHARACTER SET latin5 NOT NULL,
  `date` timestamp NULL DEFAULT NULL,
  `query_date` text,
  `status` int(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `t_tweet_hashtag`
--

CREATE TABLE IF NOT EXISTS `t_tweet_hashtag` (
  `tweet_id` varchar(50) NOT NULL,
  `hashtag_id` int(10) NOT NULL,
  PRIMARY KEY (`tweet_id`,`hashtag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `t_tweet_mention`
--

CREATE TABLE IF NOT EXISTS `t_tweet_mention` (
  `tweet_id` varchar(50) NOT NULL,
  `mention` varchar(20) NOT NULL,
  PRIMARY KEY (`tweet_id`,`mention`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `t_tweet_url`
--

CREATE TABLE IF NOT EXISTS `t_tweet_url` (
  `tweet_id` varchar(50) NOT NULL,
  `url_id` int(10) NOT NULL,
  PRIMARY KEY (`tweet_id`,`url_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `t_tweet_word`
--

CREATE TABLE IF NOT EXISTS `t_tweet_word` (
  `tweet_id` varchar(50) NOT NULL,
  `word_id` int(11) NOT NULL,
  PRIMARY KEY (`tweet_id`,`word_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `t_user`
--

CREATE TABLE IF NOT EXISTS `t_user` (
  `id` int(11) NOT NULL DEFAULT '0',
  `screen_name` varchar(20) CHARACTER SET utf8 NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `statuses_count` int(11) NOT NULL DEFAULT '0',
  `followers_count` int(11) NOT NULL DEFAULT '0',
  `friends_count` int(11) NOT NULL DEFAULT '0',
  `location` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8 DEFAULT NULL,
  `profile_image_url` varchar(1000) DEFAULT NULL,
  `search_id` int(10) NOT NULL,
  `raw_json` text,
  `score` double DEFAULT NULL,
  `chattiness` double DEFAULT NULL,
  `feedness` double DEFAULT NULL,
  `retweeted_mean` double DEFAULT NULL,
  `retweet_ratio` double DEFAULT NULL,
  `term_variation` double DEFAULT NULL,
  `search_relateness` double DEFAULT NULL,
  `rating_of_related_tweets` double DEFAULT NULL,
  `in_degree` int(10) DEFAULT NULL,
  `out_degree` int(10) DEFAULT NULL,
  `betweenness` double DEFAULT NULL,
  `closeness` double DEFAULT NULL,
  PRIMARY KEY (`screen_name`,`search_id`),
  KEY `screen_name` (`screen_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `t_user_copy`
--

CREATE TABLE IF NOT EXISTS `t_user_copy` (
  `id` int(11) NOT NULL,
  `screen_name` varchar(20) CHARACTER SET utf8 NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `statuses_count` int(11) NOT NULL,
  `followers_count` int(11) NOT NULL,
  `friends_count` int(11) NOT NULL,
  `location` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8 DEFAULT NULL,
  `profile_image_url` varchar(1000) DEFAULT NULL,
  `search_id` int(10) NOT NULL,
  `raw_json` text,
  `score` double DEFAULT NULL,
  `chattiness` double DEFAULT NULL,
  `feedness` double DEFAULT NULL,
  `retweeted_mean` double DEFAULT NULL,
  `retweet_ratio` double DEFAULT NULL,
  `term_variation` double DEFAULT NULL,
  `search_relateness` double DEFAULT NULL,
  `rating_of_related_tweets` double DEFAULT NULL,
  `in_degree` int(10) DEFAULT NULL,
  `out_degree` int(10) DEFAULT NULL,
  `betweenness` double DEFAULT NULL,
  `closeness` double DEFAULT NULL,
  PRIMARY KEY (`id`,`search_id`),
  KEY `screen_name` (`screen_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_hashtag_tweet`
--
CREATE TABLE IF NOT EXISTS `v_hashtag_tweet` (
`tweet_id` varchar(50)
,`hashtags` text
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_kw_tags`
--
CREATE TABLE IF NOT EXISTS `v_kw_tags` (
`query_id` int(10)
,`search_txt` varchar(255)
,`tag` varchar(100)
,`weight` int(10)
,`resource` int(1)
,`resource_desc` varchar(50)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_mention_tweet`
--
CREATE TABLE IF NOT EXISTS `v_mention_tweet` (
`tweet_id` varchar(50)
,`mentions` text
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_search_result`
--
CREATE TABLE IF NOT EXISTS `v_search_result` (
`id` int(10)
,`start_time` timestamp
,`query` varchar(100)
,`finish_time` timestamp
,`twitter_query` text
,`vector_query` text
,`status` tinyint(1)
,`tweet_id` varchar(50)
,`tweet` varchar(200)
,`screen_name` varchar(20)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_search_result_user_tweets`
--
CREATE TABLE IF NOT EXISTS `v_search_result_user_tweets` (
`search_id` int(10)
,`type` tinyint(1)
,`id` varchar(50)
,`user_id` int(11)
,`screen_name` varchar(20)
,`tweet` varchar(200)
,`date` timestamp
,`query_date` text
,`status` int(1)
,`is_retweet` int(1)
,`retweeted_id` varchar(50)
,`retweeted_screen_name` varchar(20)
,`reply_id` varchar(50)
,`reply_screen_name` varchar(20)
,`retweet_count` int(11)
,`latitude` double(9,6)
,`longitude` double(9,6)
,`raw_json` text
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_tweet_search_tokens`
--
CREATE TABLE IF NOT EXISTS `v_tweet_search_tokens` (
`search_id` int(10)
,`type` tinyint(1)
,`query` varchar(100)
,`id` varchar(50)
,`user_id` int(11)
,`screen_name` varchar(20)
,`tweet` varchar(200)
,`date` timestamp
,`query_date` text
,`status` int(1)
,`is_retweet` int(1)
,`retweeted_id` varchar(50)
,`retweeted_screen_name` varchar(20)
,`reply_id` varchar(50)
,`reply_screen_name` varchar(20)
,`retweet_count` int(11)
,`latitude` double(9,6)
,`longitude` double(9,6)
,`raw_json` text
,`hashtags` text
,`mentions` text
,`urls` text
,`words` text
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_tweet_tokens`
--
CREATE TABLE IF NOT EXISTS `v_tweet_tokens` (
`id` varchar(50)
,`user_id` int(11)
,`screen_name` varchar(20)
,`tweet` varchar(200)
,`date` timestamp
,`query_date` text
,`status` int(1)
,`is_retweet` int(1)
,`retweeted_id` varchar(50)
,`retweeted_screen_name` varchar(20)
,`reply_id` varchar(50)
,`reply_screen_name` varchar(20)
,`retweet_count` int(11)
,`latitude` double(9,6)
,`longitude` double(9,6)
,`raw_json` text
,`hashtags` text
,`mentions` text
,`urls` text
,`words` text
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_url_tweet`
--
CREATE TABLE IF NOT EXISTS `v_url_tweet` (
`tweet_id` varchar(50)
,`urls` text
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `v_word_tweet`
--
CREATE TABLE IF NOT EXISTS `v_word_tweet` (
`tweet_id` varchar(50)
,`words` text
);
-- --------------------------------------------------------

--
-- Structure for view `v_hashtag_tweet`
--
DROP TABLE IF EXISTS `v_hashtag_tweet`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_hashtag_tweet` AS select `t_tweet_hashtag`.`tweet_id` AS `tweet_id`,group_concat(`t_token_hashtag`.`hashtag` separator ' ') AS `hashtags` from (`t_token_hashtag` join `t_tweet_hashtag`) where (`t_token_hashtag`.`id` = `t_tweet_hashtag`.`hashtag_id`) group by `t_tweet_hashtag`.`tweet_id`;

-- --------------------------------------------------------

--
-- Structure for view `v_kw_tags`
--
DROP TABLE IF EXISTS `v_kw_tags`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_kw_tags` AS select `t_kw_tags`.`query_id` AS `query_id`,`t_kw_tags`.`search_txt` AS `search_txt`,`t_kw_tags`.`tag` AS `tag`,`t_kw_tags`.`weight` AS `weight`,`t_kw_tags`.`resource` AS `resource`,(select `t_ref`.`v_val` from `t_ref` where ((`t_ref`.`id` = 'kw_tags__resource') and (`t_ref`.`i_key` = `t_kw_tags`.`resource`))) AS `resource_desc` from `t_kw_tags`;

-- --------------------------------------------------------

--
-- Structure for view `v_mention_tweet`
--
DROP TABLE IF EXISTS `v_mention_tweet`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_mention_tweet` AS select `t_tweet_mention`.`tweet_id` AS `tweet_id`,group_concat(`t_tweet_mention`.`mention` separator ' ') AS `mentions` from `t_tweet_mention` group by `t_tweet_mention`.`tweet_id`;

-- --------------------------------------------------------

--
-- Structure for view `v_search_result`
--
DROP TABLE IF EXISTS `v_search_result`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_search_result` AS select `s`.`id` AS `id`,`s`.`start_time` AS `start_time`,`s`.`query` AS `query`,`s`.`finish_time` AS `finish_time`,`s`.`twitter_query` AS `twitter_query`,`s`.`vector_query` AS `vector_query`,`s`.`status` AS `status`,`sr`.`tweet_id` AS `tweet_id`,`t`.`tweet` AS `tweet`,`t`.`screen_name` AS `screen_name` from ((`t_search` `s` join `t_search_result` `sr`) join `t_tweet` `t`) where ((`s`.`id` = `sr`.`search_id`) and (`sr`.`type` = 1) and (`t`.`id` = `sr`.`tweet_id`));

-- --------------------------------------------------------

--
-- Structure for view `v_search_result_user_tweets`
--
DROP TABLE IF EXISTS `v_search_result_user_tweets`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_search_result_user_tweets` AS select `sr`.`search_id` AS `search_id`,`sr`.`type` AS `type`,`t`.`id` AS `id`,`t`.`user_id` AS `user_id`,`t`.`screen_name` AS `screen_name`,`t`.`tweet` AS `tweet`,`t`.`date` AS `date`,`t`.`query_date` AS `query_date`,`t`.`status` AS `status`,`t`.`is_retweet` AS `is_retweet`,`t`.`retweeted_id` AS `retweeted_id`,`t`.`retweeted_screen_name` AS `retweeted_screen_name`,`t`.`reply_id` AS `reply_id`,`t`.`reply_screen_name` AS `reply_screen_name`,`t`.`retweet_count` AS `retweet_count`,`t`.`latitude` AS `latitude`,`t`.`longitude` AS `longitude`,`t`.`raw_json` AS `raw_json` from (`t_search_result` `sr` join `t_tweet` `t`) where ((`sr`.`tweet_id` = `t`.`id`) and (`sr`.`type` <> 1));

-- --------------------------------------------------------

--
-- Structure for view `v_tweet_search_tokens`
--
DROP TABLE IF EXISTS `v_tweet_search_tokens`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_tweet_search_tokens` AS select `search`.`id` AS `search_id`,`result`.`type` AS `type`,`search`.`query` AS `query`,`tokens`.`id` AS `id`,`tokens`.`user_id` AS `user_id`,`tokens`.`screen_name` AS `screen_name`,`tokens`.`tweet` AS `tweet`,`tokens`.`date` AS `date`,`tokens`.`query_date` AS `query_date`,`tokens`.`status` AS `status`,`tokens`.`is_retweet` AS `is_retweet`,`tokens`.`retweeted_id` AS `retweeted_id`,`tokens`.`retweeted_screen_name` AS `retweeted_screen_name`,`tokens`.`reply_id` AS `reply_id`,`tokens`.`reply_screen_name` AS `reply_screen_name`,`tokens`.`retweet_count` AS `retweet_count`,`tokens`.`latitude` AS `latitude`,`tokens`.`longitude` AS `longitude`,`tokens`.`raw_json` AS `raw_json`,`tokens`.`hashtags` AS `hashtags`,`tokens`.`mentions` AS `mentions`,`tokens`.`urls` AS `urls`,`tokens`.`words` AS `words` from ((`v_tweet_tokens` `tokens` join `t_search` `search`) join `t_search_result` `result`) where ((`tokens`.`id` = `result`.`tweet_id`) and (`result`.`search_id` = `search`.`id`));

-- --------------------------------------------------------

--
-- Structure for view `v_tweet_tokens`
--
DROP TABLE IF EXISTS `v_tweet_tokens`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_tweet_tokens` AS select `t`.`id` AS `id`,`t`.`user_id` AS `user_id`,`t`.`screen_name` AS `screen_name`,`t`.`tweet` AS `tweet`,`t`.`date` AS `date`,`t`.`query_date` AS `query_date`,`t`.`status` AS `status`,`t`.`is_retweet` AS `is_retweet`,`t`.`retweeted_id` AS `retweeted_id`,`t`.`retweeted_screen_name` AS `retweeted_screen_name`,`t`.`reply_id` AS `reply_id`,`t`.`reply_screen_name` AS `reply_screen_name`,`t`.`retweet_count` AS `retweet_count`,`t`.`latitude` AS `latitude`,`t`.`longitude` AS `longitude`,`t`.`raw_json` AS `raw_json`,`h`.`hashtags` AS `hashtags`,`m`.`mentions` AS `mentions`,`u`.`urls` AS `urls`,`w`.`words` AS `words` from ((((`t_tweet` `t` left join `v_hashtag_tweet` `h` on((`t`.`id` = `h`.`tweet_id`))) left join `v_mention_tweet` `m` on((`t`.`id` = `m`.`tweet_id`))) left join `v_url_tweet` `u` on((`t`.`id` = `u`.`tweet_id`))) left join `v_word_tweet` `w` on((`t`.`id` = `w`.`tweet_id`)));

-- --------------------------------------------------------

--
-- Structure for view `v_url_tweet`
--
DROP TABLE IF EXISTS `v_url_tweet`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_url_tweet` AS select `t_tweet_url`.`tweet_id` AS `tweet_id`,group_concat(`t_token_url`.`url` separator ' ') AS `urls` from (`t_token_url` join `t_tweet_url`) where (`t_token_url`.`id` = `t_tweet_url`.`url_id`) group by `t_tweet_url`.`tweet_id`;

-- --------------------------------------------------------

--
-- Structure for view `v_word_tweet`
--
DROP TABLE IF EXISTS `v_word_tweet`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_word_tweet` AS select `t_tweet_word`.`tweet_id` AS `tweet_id`,group_concat(`t_token_word`.`word` separator ' ') AS `words` from (`t_token_word` join `t_tweet_word`) where (`t_token_word`.`id` = `t_tweet_word`.`word_id`) group by `t_tweet_word`.`tweet_id`;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
