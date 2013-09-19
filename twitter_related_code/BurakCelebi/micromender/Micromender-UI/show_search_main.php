<?php include_once 'preprocess.php'; ?>

<?php include 'show_search_common.php';?>

<!----------------------------------------- <user_rank_table> ------------------------------------------------------------------------------->
	
          <script>
            $(function () {
              $("a[rel=popover]")
                .popover({
                  offset: 10
                })
                .click(function(e) {
                  e.preventDefault()
                })
            })
          </script>

<a id="rankTableAnchor"></a><p>&nbsp;</p>
<script>
  		$(function() {
    		$("table#rankTable").tablesorter({ sortList: [[0,1]] });
  		});
</script>
<table id="rankTable" class="zebra-striped">
<thead><tr>
	<!-- 
	<th>Rank</th>
	 -->
	<th>Score</th>
	<th>CB Score</th>
	<th>Screen Name [Statuses]</th>
	<!--  
	 <th>Iteration</th>
	 -->
	<th>Followers / Friends</th>
	<th>Socialness</th>
	<th>Feedness</th>
	<th>Hashtags</th>
	<th>Retweeted</th>
	<th>TermVariation</th>
	<th>RetweetRatio</th>
	<th>Result</th>
</tr></thead>
<tbody>
<?php 

$usersSql = 'select screen_name, format(new_score,2)new_score, format(score,2)score, 
					statuses_count, followers_count, friends_count,
					format(socialness_own,2)socialness_own, format(socialness_rts,2)socialness_rts,
					format(feedness_own,2)feedness_own, format(feedness_rts,2)feedness_rts,
					format(hashtags_own,2)hashtags_own, format(hashtags_rts,2)hashtags_rts,
					format(rating_own,2)rating_own, format(rating_rts,2)rating_rts,
					format(term_variation_own,2)term_variation_own, format(term_variation_rts,2)term_variation_rts,
					format(search_relateness_new,2)search_relateness_new, format(retweet_ratio_new,2)retweet_ratio_new,
					if (new_score is null, \'NWR-C\', 
		        				if ( ((term_variation_own >= 3 and retweet_ratio_new<0.6) OR (retweet_ratio_new>=0.6 AND term_variation_rts >= 3 ) ), 
		        					\'R\',  
		        	\'NWR-TV\') ) r
			   from t_user 
              where search_id = ' . $searchId . ' and num_of_tweets_fetched>=50 order by new_score desc, score desc';

/*
$usersSql = '
select
  format( (
    	    0.27 * ( ' . printNullSafe('socialness_own_norm') . ' + ' . printNullSafe('feedness_own_norm') . ' + ' . printNullSafe('term_variation_own_norm') . ')'
      . ' + 0.05 * ' . printNullSafe('hashtags_own_norm')
      . ' + 0.13 * ( ' . printNullSafe('socialness_rts_norm') . ' + ' . printNullSafe('feedness_rts_norm') . ' + ' . printNullSafe('term_variation_rts_norm') . ')'
      . ' + 0.03 * ' . printNullSafe(addHashTagsRts($searchId)) 
      . ' + 0.30 * ' . printNullSafe('rating_own_norm')
      . ' + 0.22 * ' . printNullSafe('rating_rts_norm')
  . '), 2) new_score_norm,      
  t.* 
 from (
        select  
        screen_name, statuses_count, followers_count, friends_count,
        format(score, 3) score,
        format( ( socialness_own / 
                (select ( sum(socialness_own * num_of_related_tweets_own) / sum(num_of_related_tweets_own) ) socialness_own_avg
                from t_user where search_id = tu.search_id and score > 0.1)) ,2) socialness_own_norm,
        format( ( socialness_rts / 
                (select ( sum(socialness_rts * num_of_related_tweets_rts) / sum(num_of_related_tweets_rts) ) socialness_rts_avg
                from t_user where search_id = tu.search_id and score > 0.1)) ,2) socialness_rts_norm,
        format( ( feedness_own / 
                (select ( sum(feedness_own * num_of_related_tweets_own) / sum(num_of_related_tweets_own) ) feedness_own_avg
                from t_user where search_id = tu.search_id and score > 0.1)) ,2) feedness_own_norm,
        format( ( feedness_rts / 
                (select ( sum(feedness_rts * num_of_related_tweets_rts) / sum(num_of_related_tweets_rts) ) feedness_rts_avg
                from t_user where search_id = tu.search_id and score > 0.1)) ,2) feedness_rts_norm,
        format( ( hashtags_own / 
                (select ( sum(hashtags_own * num_of_related_tweets_own) / sum(num_of_related_tweets_own) ) hashtags_own_avg
                from t_user where search_id = tu.search_id and score > 0.1)) ,2) hashtags_own_norm,
        format( ( hashtags_rts / 
                (select ( sum(hashtags_rts * num_of_related_tweets_rts) / sum(num_of_related_tweets_rts) ) hashtags_rts_avg
                from t_user where search_id = tu.search_id and score > 0.1)) ,2) hashtags_rts_norm,
        format( ( rating_own / 
                (select ( sum(rating_own * num_of_related_tweets_own) / sum(num_of_related_tweets_own) ) rating_own_avg
                from t_user where search_id = tu.search_id and score > 0.1)) ,2) rating_own_norm,
        format( ( rating_rts / 
                (select ( sum(rating_rts * num_of_related_tweets_rts) / sum(num_of_related_tweets_rts) ) rating_rts_avg
                from t_user where search_id = tu.search_id and score > 0.1)) ,2) rating_rts_norm,
        format( ( term_variation_own / 
                (select ( sum(term_variation_own * num_of_related_tweets_own) / sum(num_of_related_tweets_own) ) term_variation_own_avg
                from t_user where search_id = tu.search_id and score > 0.1)) ,2) term_variation_own_norm,
        format( ( term_variation_rts / 
                (select ( sum(term_variation_rts * num_of_related_tweets_rts) / sum(num_of_related_tweets_rts) ) term_variation_rts_avg
                from t_user where search_id = tu.search_id and score > 0.1)) ,2) term_variation_rts_norm,
        format( retweet_ratio_new ,2) retweet_ratio_new, 
        format( search_relateness_new ,2) search_relateness_new 
 from t_user tu 
 where search_id = ' . $searchId .
' and score > 0.1 and num_of_tweets_fetched >= 50
 order by score desc
 ) t 
 order by new_score_norm desc, score desc';
*/


function printNullSafe($mblogChar) {
	return "if($mblogChar is null,0,$mblogChar)";
}

function addHashTagsRts($id) {
	if ($id<239) {
		return 'hashtags_own_norm';
	} else {
		return 'hashtags_rts_norm'; 
	}
}

$result = mysql_query($usersSql);
$rowId = 1;
while($row = mysql_fetch_array($result)) {
	echo '<tr>'
	/*
	. '<td>' . $rowId++  . '</td>'
	*/ 
	. append_td('Final Score', $row['new_score'])
	. append_td('Content Based Score', $row['score'])
	. '<td><a href="user.php?search=' . $searchId . '&user=' . $row['screen_name'] . '">' . $row['screen_name'] . '</a> [' . $row['statuses_count'] . ']</td>'
	/*
	. '<td><a href="user.php?search=' . $searchId . '&user=' . $row['screen_name'] . '">' . $row['score'] . '</a></td>'
	. '<td><a href="http://twitter.com/' . $row['screen_name'] . '" title="' . $row['name'] . ' @ ' . $row['location'] . '">' . $row['screen_name'] . '</a> [' . $row['statuses_count'] . ']</td>' 
	. append_td('Iter', intval($row['iter'])-1)
	*/
	. append_td('Followers / Friends', $row['followers_count'].' / '.$row['friends_count']) 
	
	. append_td_for_char('Socialness of original tweets (Socialness of retweeted tweets)', $row['socialness_own'], $row['socialness_rts'])
	. append_td_for_char('Feedness of original tweets (Feedness of retweeted tweets)', $row['feedness_own'], $row['feedness_rts'])
	. append_td_for_char('Hashtags of original tweets (Hashtags of retweeted tweets)', $row['hashtags_own'], $row['hashtags_rts'])
	. append_td_for_char('RetweetCount of original tweets (RetweetCount of retweeted tweets)', $row['rating_own'], $row['rating_rts'])
	. append_td_for_char('TermVariation of original tweets (TermVariation of retweeted tweets)', $row['term_variation_own'], $row['term_variation_rts'])
	
	. append_td('RetweetRatio', $row['retweet_ratio_new'])
	. append_td('Recommendation Result', $row['r'])
	/*
	. '<td>' . number_format( doubleval($row['search_relateness']) * doubleval($row['rating_of_related_tweets']) ,3) . '</td>'
	. '<td>' . number_format( doubleval($row['search_relateness']) * doubleval($row['rating_of_related_tweets']) * doubleval($row['score']) ,3) . '</td>'
	*/
	. '</tr>';
}
closeCon($con);


function append_td_for_char($tooltip, $val_own, $val_rts) {
	return append_td($tooltip, trimZerosAfterPeriod($val_own) . ' (' . trimZerosAfterPeriod($val_rts) . ')');
}

function trimZerosAfterPeriod($doubleVal) {
	$intVal = intval($doubleVal);
	if ($intVal == $doubleVal) {
		return $intVal;
	} else {
		return $doubleVal;
	}
}

function append_td($tooltip, $val) {
	return '<td><a href="" class="tag_cloud" rel="twipsy" data-original-title="' . $tooltip . '">' . $val . '</a></td>';
}

?>
</tbody>
</table>

<!----------------------------------------- </user_rank_table> ------------------------------------------------------------------------------>

<?php include_once 'postprocess.php'; ?>
