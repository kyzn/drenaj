<?php include_once 'includes/Utils.php'; ?>
<?php include_once 'includes/DbUtils.php'; ?>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="http://mbostock.github.com/d3/d3.js?1.29.1"></script>
    <script type="text/javascript" src="http://mbostock.github.com/d3/d3.geom.js?1.29.1"></script>
    <script type="text/javascript" src="http://mbostock.github.com/d3/d3.layout.js?1.29.1"></script>

	<style type="text/css">
		
		path.link {
			fill: none;
			stroke: #666;
			stroke-width: 1.5px;
		}
		
		marker#licensing {
			fill: green;
		}
		
		path.link.licensing {
			stroke: green;
		}
		
		path.link.resolved {
			stroke-dasharray: 0,2 1;
		}
		
		circle {
			fill: #ccc;
			stroke: #333;
			stroke-width: 1.5px;
		}
		
		text {
			font: 10px sans-serif;
			pointer-events: none;
		}
		
		text.shadow {
			stroke: #fff;
			stroke-width: 3px;
			stroke-opacity: .8;
		}
	
	</style>

  </head>
  <body>

<script type="text/javascript">

// http://blog.thomsonreuters.com/index.php/mobile-patent-suits-graphic-of-the-day/
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

var svg = d3.select("body").append("svg:svg")
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


  circle.on("click", function(d) {
	    alert("You selected node "+ d);
   });
  
}

</script>

<?php 
closeCon($con);
?>

</body>
</html>
 