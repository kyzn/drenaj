<?php include_once 'preprocess.php'; ?>
<?php include_once 'RecomUser.php'; ?>

<?php 

if (!isset($_SESSION['userId'])) { ?>
	Please login.	
<?php
} else {

	if  ( isset($_POST['recom']) ) {
		
		$evalUserId = intval($_SESSION['userId']);
		$userQueryId = $_POST['recom'];
		
		$con = connect();
		
		$result=mysql_query('select ID, QUERY from t_eval_user_query where status=2 and id=' . $userQueryId);
		if($row = mysql_fetch_array($result)) {
			$query_txt = $row['QUERY']; 
		}
		if( mysql_num_rows($result)==1 ){
			echo 'Something is wrong with ' . $query_txt;
		} else {
			
			foreach (array_keys($_POST) as $postElemKey) {
					
				if (substr($postElemKey,0,3) == 'or-') {

					/*
					$recomId = intval(substr($postElemKey,3)); //CAUTION: intval('3web') is also 3!
					$fromMicroMender = ($recomId==0);
					*/
					
					$eval = intval(addslashes($_POST[$postElemKey]));
						
					// if ($fromMicroMender) {
						$screenName = substr($postElemKey,3);
						$evalSql = "insert into t_eval_recom (eval_user_query_id, rec_source, screen_name, eval) values ($userQueryId, 0, '$screenName', $eval)";
					/*
					} else {
						$evalSql = "update t_eval_recom set eval=$eval where id=$recomId";
			
					}
					*/
					query($con, $evalSql);
						
				}
					
			}
			
			query($con, 'update t_eval_user_query set status=2, eval_date=NOW() where id = ' . $userQueryId);
			
			closeCon($con);
			?>
			<script type="text/javascript">
				redirectTo('user.php');
			</script>
	<?php 
		}
		
	} else {
		
		$con = connect();
		$userQueryId = $_GET['id'];
		
		$result = mysql_query('select USER_ID, QUERY, SEARCH_ID from t_eval_user_query where ID='.$userQueryId . ' and status=1');

		if($row = mysql_fetch_array($result)) {
			
			// $query= $row['QUERY'];
			$queryUserId= $row['USER_ID'];
			$searchId= $row['SEARCH_ID'];
			
			$sessionUserId = $_SESSION['userId'];
			
			if ($queryUserId != $sessionUserId ) { ?>
				<script type="text/javascript">
					redirectTo('user.php');
				</script>
			<?php } else {
			
					$result = mysql_query('SELECT QUERY, FINISH_TIME FROM t_search WHERE ID = ' . $searchId);
					if($row = mysql_fetch_array($result)) {
						$query = $row['QUERY'];
						$finishTime = $row['FINISH_TIME'];
					}
					?>

					<h2><?php echo $query;?><small> Finished on [<?php echo $finishTime; ?>]</small></h2>

					<div class="row">
					
						<div class="span16">
						
							<div class="alert-message info">
							<p><strong>Attention: </strong>We do not always expect you to <i>satisfy</i> all Twitter accounts (no problem if you really do so). We expect you to express what you really think about them.</p>
							</div>
							
							<div class="alert-message info">
								<p><strong>Notice: </strong>In addition to original tweets, please also consider the retweets by the users when evaluating them.</p>
							</div>

							<div class="alert-message warning">
							<p><strong>Warning: </strong>Rarely, a Twitter page may not be opened at once and it says "Sorry, that page doesn’t exist!". Please refresh the page when you encounter such a case.</p>
						  	<br/>
						  	<p>Please select "Not Applicable" for
						  		<ul>
									<li>Protected accounts. <a href="https://twitter.com/#!/HaberSiz" target="_blank">Sample</a></li>
									<li>Accounts containing so few English tweets which are inadequate to make a decision. <a href="https://twitter.com/#!/ajarabic" target="_blank">Sample</a></li>
									<li>Accounts do not have any tweets anymore. <a href="https://twitter.com/#!/missprisci" target="_blank">Sample</a></li>
									<li>Any case you think it is the best choice.</li>									
						  		</ul>
							</p>
							</div>
							
							<form name="evalForm" method="post" action="recom.php">
							
							<input name="recom" type="hidden" value="<?php echo $userQueryId; ?>"> <!-- oh, no.. -->
							
							<script>
					  		$(function() {
					    		$("table#rankTable").tablesorter({ sortList: [[0,0]] });
					  		});
							</script>
							<table id="rankTable" class="zebra-striped">
							<thead><tr><th>User</th><th></th></tr></thead>
								<tbody>
								<?php

								$users = array();
									
								/*
								$result = mysql_query('select ID, SCREEN_NAME from t_eval_recom where eval_user_query_id='.$userQueryId);
								while($row = mysql_fetch_array($result)) {
									array_push($users, new RecomUser($row['ID'], $row['SCREEN_NAME']));
								}
								*/
									
								/*
								function printNullSafe($mblogChar) {
									return "if($mblogChar is null,0,$mblogChar)";
								}
$usersSql = '
select
  format( (
    	    0.27 * ( ' . printNullSafe('socialness_own_norm') . ' + ' . printNullSafe('feedness_own_norm') . ' + ' . printNullSafe('term_variation_own_norm') . ')'
      . ' + 0.05 * ' . printNullSafe('hashtags_own_norm')
      . ' + 0.13 * ( ' . printNullSafe('socialness_rts_norm') . ' + ' . printNullSafe('feedness_rts_norm') . ' + ' . printNullSafe('term_variation_rts_norm') . ')'
      . ' + 0.03 * ' . printNullSafe('hashtags_rts_norm') 
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
									
								$usersSql = 'select screen_name from t_user where search_id = ' . $searchId . ' and num_of_tweets_fetched>=50 order by new_score desc, score desc limit 10';
									
								$result = mysql_query($usersSql);
								while($row = mysql_fetch_array($result)) {
									array_push($users, new RecomUser($row['screen_name'], $row['screen_name']));
								}
								closeCon($con);

									
								shuffle($users);
								foreach ($users as $user) {
									?>										
									<tr>
										<td><a href="http://twitter.com/<?php echo $user->screenName ?>" target="_blank">@<?php echo $user->screenName; ?></a></td>	
										<td><label><input type="radio" name="or-<?php echo $user->id;?>" value="2"> Satisfied</label></td>
										<td><label><input type="radio" name="or-<?php echo $user->id;?>" value="1"> Partially satisfied</label></td>
										<td><label><input type="radio" name="or-<?php echo $user->id;?>" value="0"> Not satisfied</label></td>
										<td><label><input type="radio" name="or-<?php echo $user->id;?>" value="-2"> Not Applicable</label></td>
									</tr>	
									<?php
									}
								?>
								<tbody>
							</table>
							
							<div class="well">
								<input type="submit" class="btn primary" value="Done!">
							</div>
					
							</form>
							
						</div>
						
						<!-- 
						<div class="span8">
							<iframe src="http://mobile.twitter.com/burakcelebi" width="100%" height="420px" ></iframe>
						</div>
						 -->
						
					</div>
						
			<?php }	
			
		} else { ?>
			<script type="text/javascript">
				redirectTo('user.php');
			</script>
		<?php }
		?>
		
	<?php 
	}

} 

?>

<script type="text/javascript">

$("form").submit(function() {
	
	var radio_groups = {}
	$(":radio").each(function(){
	    radio_groups[this.name] = true;
	});

	for(group in radio_groups){
	    if_checked = !!$(":radio[name="+group+"]:checked").length

	    if (!if_checked) {
	    	alert( 'Please evaluate user ' + group.substr(3) );
	    	return false;
	    }
	}
    $('input[type=submit]', this).attr('disabled', 'disabled');
	return true;
})

	var active_nav = "nav_users";
</script>

<?php include_once 'postprocess.php'; ?>
