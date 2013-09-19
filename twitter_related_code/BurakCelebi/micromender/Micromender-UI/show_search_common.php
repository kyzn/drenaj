<?php
$searchId = $_GET['id']; 

$con = connect();

$result = mysql_query('SELECT QUERY, SEMANTIC_SEARCH_ID, START_TIME, FINISH_TIME FROM T_SEARCH WHERE ID = ' . $searchId);
if($row = mysql_fetch_array($result)) {
	$semantic_search_id = $row['SEMANTIC_SEARCH_ID']; 
	echo '<h1>'.$row['QUERY']. ' <small>['. $row['START_TIME'] . ']</small></h1>';
}
?>
<hr>

<?php if ($semantic_search_id) { ?>
	<a href="kw_search_result.php?id=<?php echo $semantic_search_id;?>">semantic keywords</a>
<?php }?>

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

<ul class="tabs">
  <li id="show_search_main"><a href="show_search_main.php?id=<?php echo $searchId;?>">Home</a></li>
  <li id="show_search_network"><a href="show_search_network.php?id=<?php echo $searchId;?>">Network</a></li>
  <li id="show_search_terms"><a href="show_search_terms.php?id=<?php echo $searchId;?>">Terms</a></li>
</ul>

<script type="text/javascript">
	document.getElementById("<?php echo getPageName(); ?>").setAttribute("class","active");
	var active_nav = "nav_searches";
</script>