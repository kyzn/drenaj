<?php include_once 'preprocess.php'; ?>

<?php include 'show_search_common.php';?>

<!----------------------------------------- <terms> -------------------------------------------------------------------------------->


<div id="terms_chart" align="center"></div>
 
<script type="text/javascript">

var terms = {
	    "name": "terms",
	    "children": [
		<?php 
		$userTermsSql =
		' select term, sum(weight) weight ' .
		' from ' .
		' ( ' .
		' select h.hashtag term, count(*) weight ' .
		' from v_search_result_user_tweets ut, t_token_hashtag h, t_tweet_hashtag th ' .
		' where search_id = ' . $searchId .  
		' and ut.id = th.tweet_id ' .
		' and h.id = th.hashtag_id ' .
		' and length(h.hashtag) > 2 ' .
		' group by h.id ' .
		' union all ' .
		' select w.word term, count(*) weight ' .
		' from v_search_result_user_tweets ut, t_token_word w, t_tweet_word tw ' .
		' where search_id = ' . $searchId . 
		' and ut.id = tw.tweet_id ' .
		' and w.id = tw.word_id ' .
		' and length(w.word) > 2 ' .
		' group by w.id ' .
		' ) as terms ' .
		' where weight > 40 ' .
		' group by term ' .
		' order by weight desc';
		
		$bubleStr = '';
		$result = mysql_query($userTermsSql);
		while($row = mysql_fetch_array($result)) {
			$bubleStr .= '{"name": "' .  $row['term'] . '",' . "\n";
			$bubleStr .= '"children": [' . "\n";
			$bubleStr .= '{"name": "' .  $row['term'] . '","size": ' . $row['weight'] . '},' . "\n"; 
			$bubleStr .= ']' . "\n";
			$bubleStr .= '},' . "\n";
		}
		echo substr( $bubleStr, 0, strlen($bubleStr) );
		?>
	    ]
};

bubleChart(terms, 'terms_chart');

</script>


<!----------------------------------------- </terms> ------------------------------------------------------------------------------>

<?php include_once 'postprocess.php'; ?>
