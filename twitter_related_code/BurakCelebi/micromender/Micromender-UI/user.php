<?php include_once 'preprocess.php'; ?>

<?php 
$con = connect();
$searchId = $_GET['search'];
$user = $_GET['user'];
?>

<h1><a href="show_search_main.php?id=<?php echo $searchId;?>#rankTableAnchor" title="back to search results">< <?php echo $user; ?></a></h1>


<?php 

function p_td($contentArr, $format) {
	echo '<tr>';
	foreach ($contentArr as $content) {
		if ($format) {
			echo '<td align="center">'. number_format(doubleval($content),2) .'</td>';
		} else {
			echo '<td align="center">'. $content .'</td>';
		}
	}
	echo '</tr>';
}	

$userSql = 'select * from t_user where screen_name=\''.$user.'\' and search_id='.$searchId;
$user_rec = mysql_query($userSql);
while($row = mysql_fetch_array($user_rec)) {
	
	$profileImg = $row['profile_image_url'];
	$name = $row['name'];
	$description = $row['description'];
	$location = $row['location'];
	
	$statuses_count = $row['statuses_count'];
	$friends_count = $row['friends_count'];
	$followers_count = $row['followers_count'];
	
	$socialness_own = $row['socialness_own'];
	$feedness_own = $row['feedness_own'];
	$hashtags_own = $row['hashtags_own'];
	$rating_own = $row['rating_own'];
	$term_variation_own = $row['term_variation_own'];
	
	$socialness_rts = $row['socialness_rts'];
	$feedness_rts = $row['feedness_rts'];
	$hashtags_rts = $row['hashtags_rts'];
	$rating_rts = $row['rating_rts'];
	$term_variation_rts = $row['term_variation_rts'];
	
	
	$retweeted_mean = $row['retweeted_mean'];
	$search_relateness = $row['search_relateness'];
	
	$in_degree = $row['in_degree'];
	$out_degree = $row['out_degree'];
	$betweenness = $row['betweenness'];
	$closeness = $row['closeness'];
	
}

?>

<div class="row">
	<div class="span16">

				<table class="zebra-striped">
					<thead>
						<tr><th>Socialness</th><th>Feedness</th><th>Hashtags</th><th>Term Variation</th><th>Reputation</th></tr>
					</thead>
					<tbody>
						<?php  p_td(array($socialness_own, $feedness_own, $hashtags_own, $term_variation_own, $rating_own), true); ?>
					</tbody>
				</table>
	
	</div>
	
		<div class="span16">

				<table class="zebra-striped">
					<thead>
						<tr><th>Socialness</th><th>Feedness</th><th>Hashtags</th><th>Term Variation</th><th>Reputation</th></tr>
					</thead>
					<tbody>
						<?php  p_td(array($socialness_rts, $feedness_rts, $hashtags_rts, $term_variation_rts, $rating_rts), true); ?>
					</tbody>
				</table>
	
	</div>
	
</div>

<div class="row">

	<div class="span8">
	
		<div class="row">

			<div class="span8">

				<div class="border" style="background-color:#F3F3F3; padding:20px;">		
					<img src="<?php echo $profileImg;?>"><br>
				<b>
					<?php
					echo $name . '<br>';
					echo '<a href="http://twitter.com/' . $user . '" target="_blank">@'. $user . '</a>';
					if($location) {
						echo ', ' . $location;
					}
					?>
				</b>
				<?php echo  '<br>' . $description; ?>
				<hr>
				
				<table class="zebra-striped">
					<thead>
						<tr><th>Tweets</th><th>Following</th><th>Followers</th></tr>
					</thead>
					<tbody>
						<?php p_td(array($statuses_count,$friends_count,$followers_count), false); ?>
					</tbody>
				</table>

				<table class="zebra-striped">
					<thead>
						<tr><th>InDegree</th><th>OutDegree</th><th>Betweenness</th><th>Closeness</th></tr>
					</thead>
					<tbody>
						<?php p_td(array($in_degree,$out_degree,$betweenness,$closeness), true); ?>
					</tbody>
				</table>

				<!-- 
				<table class="zebra-striped">
					<thead>
						<tr><th>Socialness</th><th>Feedness</th><th>Retweeted Mean</th><th>Retweet Ratio</th><th>Term Variation</th><th>Search Relateness</th><th>Rating of Related Tweets</th></tr>
					</thead>
					<tbody>
						<?php  p_td(array($chattiness, $feedness, $retweeted_mean, $retweet_ratio, $term_variation, $search_relateness, $rating_of_related_tweets)); ?>
					</tbody>
				</table>
	 			-->

				</div>
			</div>


			
		</div>

	

	<!-- 
		<div class="border" style="background-color:#F3F3F3; padding:20px;">

			<h5>
			<a href="http://twitter.com/#!/pkleef/status/142364458591846400">pkleef</a>
			<small></small>
			</h5>
			@Wikier @kidehen did you ask the people behind ohloh to lift restrictions?			<h6><small>2011-12-02 00:08:26</small></h6>
			<hr>

			<h5>
			<a href="http://twitter.com/#!/pkleef/status/139844567041196033">pkleef</a>
			<small></small>
			</h5>
			#dbpedia is going down for 15 min maintenance by @OpenLink #lod #lod2 #semanticweb #semweb			<h6><small>2011-11-25 01:15:17</small></h6>
			<hr>
			
		</div>	
	 -->
	</div>
	
		
	<div class="span8">
		
		<h3>Contributions <small>(hashtags+words)</small></h3>
		
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
					' and screen_name = \'' . $user . '\'' .
					' and ut.id = th.tweet_id ' .
					' and h.id = th.hashtag_id ' .
					' group by h.id ' .
					' union all ' .
					' select w.word term, count(*) weight ' .
					' from v_search_result_user_tweets ut, t_token_word w, t_tweet_word tw ' .
					' where search_id = ' . $searchId . 
					' and screen_name = \'' . $user . '\'' .
					' and ut.id = tw.tweet_id ' .
					' and w.id = tw.word_id ' .
					' group by w.id ' .
					' ) as terms ' .
					' where weight > 1 ' .
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
		
		
			var users = {
				    "name": "users",
				    "children": [
					<?php 
				$networkSql =
							' select p_user, sum(weight) weight ' .
							' from ' .
							' ( ' .
				            ' select tm.mention p_user, count(*) weight ' .
							' from v_search_result_user_tweets ut, t_tweet_mention tm ' .
							' where ut.search_id = ' . $searchId .
							' and ut.screen_name = \'' . $user . '\'' .
							' and tm.tweet_id = ut.id ' .
							' and ut.is_retweet = 0 ' .
							' group by p_user ' .
							' union all' .
							' select retweeted_screen_name p_user, count(*) weight ' .
							' from v_search_result_user_tweets ut ' .
							' where ut.search_id = ' . $searchId .
							' and ut.screen_name = \'' . $user . '\'' .
							' and ut.is_retweet = 1 ' .
					        ' group by p_user ' .
							' ) as users ' .
							' group by p_user ' .
							' order by weight desc';
		
					$bubleStr = '';
					$result = mysql_query($networkSql);
					while($row = mysql_fetch_array($result)) {
						$bubleStr .= '{"name": "' .  $row['p_user'] . '",' . "\n";
						$bubleStr .= '"children": [' . "\n";
						$bubleStr .= '{"name": "' .  $row['p_user'] . '","size": ' . $row['weight'] . '},' . "\n"; 
						$bubleStr .= ']' . "\n";
						$bubleStr .= '},' . "\n";
					}
					echo substr( $bubleStr, 0, strlen($bubleStr) );
					?>
				    ]
			};
		
			
			bubleChart(terms, 'terms_chart');
			
			</script>	
	</div>
	
	
</div>

<div class="row">

	<div class="span8">
	
		<div class="border" style="background-color:#F3F3F3; padding:10px;">
		<?php 
		$result = mysql_query("select id, tweet, date, retweeted_id, retweeted_screen_name, retweet_count from v_search_result_user_tweets ut where ut.search_id = $searchId and ut.screen_name in ('$user') order by date desc");
		while($row = mysql_fetch_array($result)) {
			
			$from_screen = $row['retweeted_screen_name'] != '' ? $row['retweeted_screen_name'] : $user;
			$from_id = $row['retweeted_id'] != '' ? $row['retweeted_id'] : $row['id'];
			$tweet = $row['tweet'];
			?>
			
			<h5>
			<a href="http://twitter.com/#!/<?php echo $from_screen; ?>/status/<?php echo $from_id; ?>"><?php echo $from_screen; ?></a>
			<small><?php if ($row['retweet_count']!='0'){echo $row['retweet_count'].' RTs';} ?></small>
			</h5>
			<p><?php echo $tweet; ?><small> [<?php echo $row['date']; ?>]</small></p>
			<hr>
		<?php
		}
		?>
		</div>
	
	
	</div>
	
	<div class="span8">
		<div id="users_share"></div>
	</div>


</div>

<!----------------------------------------------- <users_share> ----------------------------------------->

		<script type="text/javascript">
		
			var chart;
			$(document).ready(function() {
				chart = new Highcharts.Chart({
					chart: {
						renderTo: 'users_share',
						plotBackgroundColor: null,
						plotBorderWidth: null,
						plotShadow: false
					},
					title: {
						text: 'Mentioned and Retweeted Users'
					},
					tooltip: {
						formatter: function() {
							return '<b>'+ this.point.name +'</b>: '+ this.percentage +' %';
						}
					},
					plotOptions: {
						pie: {
							allowPointSelect: true,
							cursor: 'pointer',
							dataLabels: {
					        	enabled: false
							},
							showInLegend: true
						}
					},
				    series: [{
						type: 'pie',
						name: 'User share',
						data: [
							<?php 
								$result = mysql_query($networkSql);
								
								if($row = mysql_fetch_array($result)) {
									echo '{name:\'' . $row['p_user'] . '\', y:' . $row['weight'] . ', sliced:true, selected:true},' . "\n";
								}
								
								while($row = mysql_fetch_array($result)) {
									echo '[\'' . $row['p_user'] . '\', ' . $row['weight'] . '],' . "\n";
								}
							?>
						]
					}]
				});
			});
				
		</script>
		
<!----------------------------------------------- </users_share> ----------------------------------------->


<!----------------------------------------- <user_rank_table> ------------------------------------------------------------------------------->

<!-- 
<a id="userTweetsTableAnchor"></a><p>&nbsp;</p>

<script>
$(function() {
	$("table#userTweetsTable").tablesorter({
		sortList: [[7,0]] });
});
</script>
<table id="userTweetsTable" class="zebra-striped">
<thead><tr>
<th>n</th>
<th>tweet</th> 
<th>date</th>
<th>retweeted_id</th> 
<th>retweeted_screen_name</th> 
<th>retweet_count</th>
</tr></thead>
<tbody>
<?php
/*
$result = mysql_query("select id, tweet, date, retweeted_id, retweeted_screen_name, retweet_count from v_search_result_user_tweets ut where ut.search_id = $searchId and ut.screen_name in ('$user') order by retweet_count desc");
$rowId = 1;
while($row = mysql_fetch_array($result)) {
	echo '<tr><td>' . $rowId++  . '</td><td><a href="http://twitter.com/#!/BurakCelebi/status/' . $row['id'] . '">' . $row['tweet'] . '</a></td><td>' . $row['date'] . '</td><td>' . $row['retweeted_id'] . '</td><td>' . $row['retweeted_screen_name'] . '</td><td>' . $row['retweet_count'] . '</td></tr>';
}
*/
?>
</tbody>
</table>
-->
 
<!----------------------------------------- </user_rank_table> ------------------------------------------------------------------------------>


<?php closeCon($con); ?>

<script type="text/javascript">
	var active_nav = "nav_searches";
</script>
<?php include_once 'postprocess.php'; ?>