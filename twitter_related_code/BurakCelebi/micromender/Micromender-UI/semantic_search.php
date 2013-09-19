<?php 

if  ( isset($_POST['input_query']) ) {

	include_once 'includes/DbUtils.php';
	
	$query = addslashes($_POST['input_query']);
	$freebaseId = addslashes($_POST['freebase_id']);
	$freebaseType = addslashes($_POST['freebase_type']);
	
	$con = connect();

	query($con, "INSERT INTO t_kw_query (query,freebase_id,freebase_type,type) VALUES ('$query', '$freebaseId', '$freebaseType', 1)");
	$semantic_search_id = mysql_insert_id();
	
	query($con, "INSERT INTO t_search (query, semantic_search_id) VALUES ('$query', '$semantic_search_id')");
	
	closeCon($con);
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
    
<link type="text/css" rel="stylesheet" href="http://freebaselibs.com/static/suggest/1.3/suggest.min.css" />

<script src="js/jquery/jquery-1.7.min.js"></script>
<script type="text/javascript" src="http://freebaselibs.com/static/suggest/1.3/suggest.min.js"></script>

<script type="text/javascript">
$(function()
{
	$("#input_query")
	.suggest({
		all_types:true})
		.bind("fb-select", function(e, data) {
			$('#freebase_id').val(data.id);
			$('#freebase_type').val(data["n:type"].name);
		})
});

function clickClear() {
	document.query_form.input_query.value = "";
	document.query_form.freebase_id.value = "";
}

</script>

</head>

<body>

	<form action="semantic_search.php" name="query_form" id="query_form" method="post">
    	<input type="text" name="input_query" id="input_query" onclick="clickClear()" value="search" size="50">
		<input type="hidden" type="text" name="freebase_id" id="freebase_id" size="50" >
		<input type="hidden" type="text" name="freebase_type" id="freebase_type" size="50" >
		<input type="submit" class="btn primary" value="Search">
	</form>

</body>

</html>