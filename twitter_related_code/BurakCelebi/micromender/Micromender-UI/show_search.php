<?php include_once 'preprocess.php'; ?>

<?php
$searchId = $_GET['id']; 

$con = connect();

$result = mysql_query('SELECT QUERY, START_TIME, FINISH_TIME FROM T_SEARCH WHERE ID = ' . $searchId);
if($row = mysql_fetch_array($result)) {
	echo '<h1>'.$row['QUERY']. ' <small>['. $row['START_TIME'] . ']</small></h1>';
}
?>

<hr>

<h3>Top related tags</h3>

<script>
   $(function () {
   		$("a[rel=twipsy]").twipsy({live: true})
   });
</script>

<div class="border">
	<?php echo get_tag_cloud("select tag, weight from t_search_extended where search_id = $searchId and type = 1"); ?>
</div>

<a onclick ="javascript:showHide('allTagsDiv')" href="javascript:;" >All Tags</a>
<div id="allTagsDiv" style="DISPLAY: none" class="border">
		<?php echo get_tag_cloud("select tag, weight from t_search_extended where search_id = $searchId and type = 2"); ?>
</div>

<hr>

<!----------------------------------------- <user_network> -------------------------------------------------------------------------------->

<div id="user_network" class="border" style="height:500px;width:960px;"></div>

<script type="text/javascript">

var links = [

<?php
	$searchId = $_GET['id'];
		
	$con = connect();

	$networkSql =   ' select ut.screen_name from_user, tm.mention to_user' .
					' from v_search_result_user_tweets ut, t_tweet_mention tm ' .
					' where ut.search_id = ' . $searchId .
					' and tm.tweet_id = ut.id ' .
					' and ut.is_retweet = 0 ' .
					' and mention in (select screen_name from t_user where search_id = ' . $searchId . ')' .
					' union all' .
					' select screen_name from_user, retweeted_screen_name to_user' .
					' from v_search_result_user_tweets ut ' .
					' where ut.search_id = ' . $searchId .
					' and ut.is_retweet = 1 ' .
					' and ut.retweeted_screen_name in (select screen_name from t_user where search_id = ' . $searchId . ')';
	
	$result = mysql_query($networkSql);
	while($row = mysql_fetch_array($result)) {
		print_edge($row['from_user'], $row['to_user'], 'suit');
	}
	
	/*
	$result = mysql_query('select screen_name, retweeted_screen_name as retweeted, count(*) weight from v_search_result_user_tweets ut where ut.search_id = ' . $searchId. ' and ut.is_retweet = 1 and ut.retweeted_screen_name in (select screen_name from t_user where search_id = '. $searchId . ') group by screen_name, retweeted_screen_name order by ut.screen_name, weight desc');
	while($row = mysql_fetch_array($result)) {
		print_edge($row['screen_name'], $row['retweeted'], findType($row['weight']) );
	}
	*/
	
	function print_edge($from, $to, $edgeType) {
		echo '{source: "' . $from . '", target: "' . $to . '", type: "' . $edgeType . '"},' . "\n";
	}
	
	function findType($weightStr) {
		$type = '';
		$weight = intval($weightStr);
		switch ($weight) {
			case 1:
				$type = 'resolved';
				break;
			case 2:
				$type = 'suit';
				break;
			default:
				$type = 'licensing';			
		}
		return $type;
	}
?>

];

var nodes = {};

// Compute the distinct nodes from the links.
links.forEach(function(link) {
  link.source = nodes[link.source] || (nodes[link.source] = {name: link.source});
  link.target = nodes[link.target] || (nodes[link.target] = {name: link.target});
});

var w = 960,
    h = 500;

var force = d3.layout.force()
    .nodes(d3.values(nodes))
    .links(links)
    .size([w, h])
    .linkDistance(60)
    .charge(-300)
    .on("tick", tick)
    .start();

var svg = d3.select("#user_network").append("svg:svg")
    .attr("width", w)
    .attr("height", h);

// Per-type markers, as they don't inherit styles.
svg.append("svg:defs").selectAll("marker")
   .data(["suit", "licensing", "resolved"])
   .enter().append("svg:marker")
   .attr("id", String)
   .attr("viewBox", "0 -5 10 10")
   .attr("refX", 15)
   .attr("refY", -1.5)
   .attr("markerWidth", 6)
   .attr("markerHeight", 6)
   .attr("orient", "auto")
   .append("svg:path")
   .attr("d", "M0,-5L10,0L0,5");

var path = svg.append("svg:g").selectAll("path")
              .data(force.links())
              .enter().append("svg:path")
              .attr("class", function(d) { return "link " + d.type; })
              .attr("marker-end", function(d) { return "url(#" + d.type + ")"; });

var circle = svg.append("svg:g").selectAll("circle")
              .data(force.nodes())
              .enter().append("svg:circle")
              .attr("r", 6)
              .call(force.drag);

var text = svg.append("svg:g").selectAll("g")
              .data(force.nodes())
              .enter().append("svg:g");

// A copy of the text with a thick white stroke for legibility.
text.append("svg:text")
    .attr("x", 8)
    .attr("y", ".31em")
    .attr("class", "shadow")
    .text(function(d) { return d.name; });

text.append("svg:text")
    .attr("x", 8)
    .attr("y", ".31em")
    .text(function(d) { return d.name; });

// Use elliptical arc path segments to doubly-encode directionality.
function tick() {
  path.attr("d", function(d) {
    var dx = d.target.x - d.source.x,
        dy = d.target.y - d.source.y,
        dr = Math.sqrt(dx * dx + dy * dy);
    return "M" + d.source.x + "," + d.source.y + "A" + dr + "," + dr + " 0 0,1 " + d.target.x + "," + d.target.y;
  });

  circle.attr("transform", function(d) {
    return "translate(" + d.x + "," + d.y + ")";
  });

  text.attr("transform", function(d) {
    return "translate(" + d.x + "," + d.y + ")";
  });

}

</script>


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

<!----------------------------------------- </user_network> -------------------------------------------------------------------------------->

<!-- 
<iframe width="960px" height="500px" class="border" scrolling="no" src="show_network.php?id=<?php echo $searchId?>">
</iframe>
 -->

<!----------------------------------------- <hashtag_chart> -------------------------------------------------------------------------------->

<!-- 1a) Optional: add a theme file -->
<!--
<script type="text/javascript" src="js/highcharts/themes/gray.js"></script>
-->

<!-- 1b) Optional: the exporting module -->
<script type="text/javascript" src="js/highcharts/modules/exporting.js"></script>
		

<!-- 2. Add the JavaScript to initialize the chart on document ready -->
<script type="text/javascript">

var chart;
$(document).ready(function() {
chart = new Highcharts.Chart({
	chart: {
	renderTo: 'chartContainer',
		defaultSeriesType: 'bar'
					},
		title: {
		text: 'Top Users\' Hashtags'
	},
		xAxis: {
			categories: ['#linkeddata', '#programming', '#rdf', '#rdfa', '#semantic', '#semanticweb', '#w3c']
					},
					yAxis: {
						min: 0,
						title: {
			text: 'Number of hashtags'
			},
			allowDecimals: false
			},
					legend: {
						backgroundColor: '#FFFFFF',
			reversed: true
	},
	tooltip: {
		formatter: function() {
		return '<b>'+ this.x +'</b><br/>'+
		this.series.name +': '+ this.y + '/'+ this.point.stackTotal;
		}
					},
					plotOptions: {
		series: {
		stacking: 'normal'
		}
		},
		series: [{
		name: 'planetrdf',
		data: [1,0,1,0,0,1,0]
		}, {
			name: 'SemanticBlogs',
			data: [0,0,0,0,40,0,0]
	}, {
			name: 'manusporny',
			data: [0,1,0,2,0,0,6]
			}, {
			name: 'faviki',
						data: [3,0,2,0,0,1,0]
			}, {
			name: 'ivan_herman',
						data: [4,0,0,2,0,1,1]
					
			}, {
			name: 'roylac',
						data: [1,0,3,0,1,1,1]
			}]
});


});
</script>
<!-- 3. Add the container -->
<div id="chartContainer" style="width: 800px; height: 400px; margin: 0 auto"></div>


<!----------------------------------------- </hashtag_chart> -------------------------------------------------------------------------------->

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
    		$("table#rankTable").tablesorter({ sortList: [[0,0]] });
  		});
</script>
<table id="rankTable" class="zebra-striped">
<thead><tr>
	<th>Rank</th>
	<th>Score</th>
	<th>Screen Name (Statuses)</th>
	<th>Followers / Friends</th>
	<th>Chattiness</th>
	<th>Feedness</th>
	<th>Retweeted Mean</th>
	<th>Retweet Ratio</th>
	<th>Term Variation</th>
	<th>Search Relateness</th>
	<th>Rating of Related Tweets</th>
	<th>New Score</th>
	<th>New Score * Score</th>
</tr></thead>
<tbody>
<?php 
$result = mysql_query('SELECT screen_name, name, statuses_count, followers_count, friends_count, location, format(score, 4) score, format(chattiness,3) chattiness, format(feedness,3) feedness, format(retweeted_mean,3) retweeted_mean, format(retweet_ratio,3) retweet_ratio, format(term_variation,3) term_variation, format(search_relateness,3) search_relateness, format(rating_of_related_tweets,3) rating_of_related_tweets from t_user where search_id = ' . $searchId . ' order by score desc;');
$rowId = 1;
while($row = mysql_fetch_array($result)) {
	echo '<tr>'
	. '<td>' . $rowId++  . '</td>' 
	. '<td><a href="user.php?search=' . $searchId . '&user=' . $row['screen_name'] . '">' . $row['score'] . '</a></td>' 
	. '<td><a href="http://twitter.com/' . $row['screen_name'] . '" title="' . $row['name'] . ' @ ' . $row['location'] . '">' . $row['screen_name'] . '</a> [' . $row['statuses_count'] . ']</td>' 
	. append_td('Followers / Friends', $row['followers_count'].' / '.$row['friends_count']) 
	. append_td('Chattiness', $row['chattiness']) 
	. append_td('Feedness', $row['feedness'])
	. append_td('Retweeted Mean', $row['retweeted_mean'])
	. append_td('Retweet Ratio', $row['retweet_ratio'])
	. append_td('Term Variation', $row['term_variation'])
	. append_td('Search Relateness', $row['search_relateness'])
	. append_td('Rating of Related Tweets', $row['rating_of_related_tweets'])
	. '<td>' . doubleval($row['search_relateness']) * doubleval($row['rating_of_related_tweets']) . '</td>'
	. '<td>' . doubleval($row['search_relateness']) * doubleval($row['rating_of_related_tweets']) * doubleval($row['score']) . '</td>'
	. '</tr>';
}
closeCon($con);

function append_td($tooltip, $val) {
	return '<td><a href="" class="tag_cloud" rel="twipsy" data-original-title="' . $tooltip . '">' . $val . '</a></td>';
}

?>
</tbody>
</table>

<!----------------------------------------- </user_rank_table> ------------------------------------------------------------------------------>

<script type="text/javascript">
	var active_nav = "nav_searches";
</script>
<?php include_once 'postprocess.php'; ?>
