<?php include_once 'preprocess.php'; ?>

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

<script>
  		$(function() {
  			$("table#rankTable").tablesorter({ sortList: [[0,2]] });
  			$("table#rankTableAggr").tablesorter({ sortList: [[0,2]] });
  		});
</script>

<?php 

$queryId = $_GET['id'];
$con = connect();

?>

<script>
   $(function () {
   		$("a[rel=twipsy]").twipsy({live: true})
   });
</script>

<?php 
$result = mysql_query('SELECT QUERY, FREEBASE_TYPE FROM T_KW_QUERY WHERE ID = ' . $queryId);
if($row = mysql_fetch_array($result)) {
	echo '<h1>'.$row['QUERY']. ' <small>['. $row['FREEBASE_TYPE'] . ']</small></h1>';
}
?>
<hr>

<h3>Clouds</h3>

<ul>

<li>
<a onclick ="javascript:showHide('allTagsDivDelicious')" href="javascript:;" >Delicious</a>
<div id="allTagsDivDelicious" style="DISPLAY: none" class="border">
		<?php echo get_tag_cloud("select tag, sum(weight) weight from t_kw_tags where query_id = $queryId and resource=1 group by tag"); ?>
</div>
</li>

<li>
<a onclick ="javascript:showHide('allTagsDivYoutube')" href="javascript:;" >Youtube</a>
<div id="allTagsDivYoutube" style="DISPLAY: none" class="border">
		<?php echo get_tag_cloud("select tag, sum(weight) weight from t_kw_tags where query_id = $queryId and resource=2 group by tag"); ?>
</div>
</li>

<li>
<a onclick ="javascript:showHide('allTagsDivFlickr')" href="javascript:;" >Flickr</a>
<div id="allTagsDivFlickr" style="DISPLAY: none" class="border">
		<?php echo get_tag_cloud("select tag, sum(weight) weight from t_kw_tags where query_id = $queryId and resource=3 group by tag"); ?>
</div>
</li>

<li>
<a onclick ="javascript:showHide('allTagsDivCommon')" href="javascript:;" >Intersection (Delicious,Youtube,Flickr)</a>
<div id="allTagsDivCommon" style="DISPLAY: none" class="border">
		<?php echo get_tag_cloud(
									    'select distinct tag, 1 ' .
									    'from t_kw_tags ' . 
									    "where query_id = $queryId " .
									    'and resource = 1 ' .
									    "and tag in (select distinct tag from t_kw_tags where query_id = $queryId and resource = 2) " .
									    "and tag in (select distinct tag from t_kw_tags where query_id = $queryId and resource = 3) ");?>
</div>
</li>

<li>
<a onclick ="javascript:showHide('allTagsDivKwMap')" href="javascript:;" >KwMap</a>
<div id="allTagsDivKwMap" style="DISPLAY: none" class="border">
		<?php echo get_tag_cloud("select tag, sum(weight) weight from t_kw_tags where query_id = $queryId and resource=4 group by tag"); ?>
</div>
</li>

<li>
<a onclick ="javascript:showHide('allTagsDivRedirects')" href="javascript:;" >DbPedia Redirects</a>
<div id="allTagsDivRedirects" style="DISPLAY: none" class="border">
		<?php echo get_tag_cloud("select tag, weight from t_kw_tags where query_id = $queryId and resource=5"); ?>
</div>
</li>

<li>
<a onclick ="javascript:showHide('allTagsDiv')" href="javascript:;" >All (Delicious, Youtube, Flickr, KwMap, DbPedia Redirects)</a>
<div id="allTagsDiv" style="DISPLAY: none" class="border">
		<?php echo get_tag_cloud("select tag, sum(weight) weight from t_kw_tags where query_id = $queryId and resource in (1,2,3,4,5) group by tag"); ?>
</div>
</li>

<li>
<a onclick ="javascript:showHide('allTagsDivCategories')" href="javascript:;" >DbPedia Categories</a>
<div id="allTagsDivCategories" style="DISPLAY: none" class="border">
		<?php echo get_tag_cloud("select tag, 1 weight from t_kw_tags where query_id = $queryId and resource = 8"); ?>
</div>
</li>

</ul>
  
<h3>Tables</h3>

<ul>

<li>
<a onclick ="javascript:showHide('allTagsAggrTableDiv')" href="javascript:;" >Table All Aggr (Delicious, Youtube, Flickr, KwMap, DbPediaRedirects)</a>
<div id="allTagsAggrTableDiv" style="DISPLAY: none" class="border">

	<table id="rankTableAggr" class="zebra-striped">
	<thead><tr>
		<th>Id</th>
		<th>Tag</th>
		<th>Weight</th>
	</tr></thead>
	<tbody>
	
	<?php 
	$result = mysql_query("select tag, sum(weight) weight from t_kw_tags where query_id = $queryId and resource in (1,2,3,4,5) group by tag order by weight, tag");
	
	$rowId = 1;
	while($row = mysql_fetch_array($result)) {
		echo '<tr>'
		. '<td>' . $rowId++  . '</td>' 
		. append_td('tag', $row['tag']) 
		. append_td('weight', $row['weight']) 
		. '</tr>';
	}
	?>
	</tbody>
	</table>
</div>
</li>

<li>
<a onclick ="javascript:showHide('allTagsTableDiv')" href="javascript:;" >Table All (Delicious, Youtube, Flickr, KwMap, DbPediaRedirects)</a>
<div id="allTagsTableDiv" style="DISPLAY: none" class="border">

	<table id="rankTable" class="zebra-striped">
	<thead><tr>
		<th>Id</th>
		<th>Tag</th>
		<th>Query</th>
		<th>Weight</th>
		<th>Resource</th>
	</tr></thead>
	<tbody>
	
	<?php 
	$result = mysql_query('SELECT tag, search_txt, weight, resource_desc from v_kw_tags where query_id = ' . $queryId . ' and resource in (1,2,3,4,5) order by weight, tag;');
	
	$rowId = 1;
	while($row = mysql_fetch_array($result)) {
		echo '<tr>'
		. '<td>' . $rowId++  . '</td>' 
		. append_td('tag', $row['tag']) 
		. append_td('search_txt', $row['search_txt'])
		. append_td('weight', $row['weight']) 
		. append_td('resource', $row['resource_desc'])
		. '</tr>';
	}
	closeCon($con);
	
	function append_td($tooltip, $val) {
		return '<td><a href="" class="tag_cloud" rel="twipsy" data-original-title="' . $tooltip . '">' . $val . '</a></td>';
	}
	
	?>
	</tbody>
	</table>
</div>
</li>

</ul>



<!----------------------------------------- </user_rank_table> ------------------------------------------------------------------------------>

<?php include_once 'postprocess.php'; ?>
