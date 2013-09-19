<?php include_once 'preprocess.php'; ?>

<?php include 'show_search_common.php';?>

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
		
		// closeCon($con);
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

<!----------------------------------------- </user_network> ------------------------------------------------------------------------------>


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
	$("table#rankTable").tablesorter({
		sortList: [[0,0]] });
});
</script>

<table id="rankTable" class="zebra-striped">
<thead><tr>
<th>Rank</th>
<th>Score</th>
<th>Screen Name [Statuses]</th>
<th>InDegree</th>
<th>OutDegree</th>
<th>Betweenness</th>
<th>Closeness</th>
</tr></thead>
<tbody>
<?php
$result = mysql_query('SELECT screen_name, name, location, statuses_count, format(score, 4) score, format(in_degree,3) in_degree, format(out_degree,3) out_degree, format(betweenness,3) betweenness, format(closeness,3) closeness from t_user where search_id = ' . $searchId . ' order by score desc');
$rowId = 1;
while($row = mysql_fetch_array($result)) {
	echo 
	  '<tr>'
	. '<td>' . $rowId++  . '</td>'
	. '<td><a href="user.php?search=' . $searchId . '&user=' . $row['screen_name'] . '">' . $row['score'] . '</a></td>'
	. '<td><a href="http://twitter.com/' . $row['screen_name'] . '" title="' . $row['name'] . ' @ ' . $row['location'] . '">' . $row['screen_name'] . '</a> [' . $row['statuses_count'] . ']</td>'
	. append_td('in-degree', number_format( doubleval($row['in_degree']) ,3))
	. append_td('out-degree', number_format( doubleval($row['out_degree']) ,3))
	. append_td('betweenness', number_format( doubleval($row['betweenness']) ,3))
	. append_td('closeness', number_format( doubleval($row['closeness']) ,3))
	. '</tr>';
}
closeCon($con);

function append_td($tooltip, $val) {
	return '<td><a href="" class="tag_cloud" rel="twipsy" data-original-title="' . $tooltip . '">' . $val . '</a></td>';
}
?>
</tbody>
</table>
<?php include_once 'postprocess.php'; ?>
