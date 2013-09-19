<?php include_once 'preprocess.php'; ?>

<?php 

if  ( isset($_POST['query']) ) {

	$query = addslashes($_POST['query']);
	
	query(connect(), "INSERT INTO t_search (query) VALUES ('$query')");
	closeCon($con);
	
	// redirectTo('searches');
	?>
	<script type="text/javascript">
		redirectTo('searches.php'); 
	</script>
	
<?php
} else {

?>

	<h2>Simple search</h2>
	
	<form name="searchForm" method="post" action="search.php">
		<input name="query"  type="text" id="query" size="25">
		<input type="submit" class="btn primary" value="Search">
	</form>
	
	<h2>Advanced search</h2>

	<iframe src="semantic_search.php" width="100%" height="420px" style="border:none;"></iframe>
	
	<script type="text/javascript">
		var active_nav = "nav_search";
	</script>
	
	<?php include_once 'postprocess.php'; ?>

<?php } ?>