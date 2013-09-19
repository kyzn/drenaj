<!DOCTYPE html>
<html lang="en">

<head>

    
<link type="text/css" rel="stylesheet" href="http://freebaselibs.com/static/suggest/1.3/suggest.min.css" />

<script src="js/jquery/jquery-1.7.min.js"></script>
<script type="text/javascript" src="http://freebaselibs.com/static/suggest/1.3/suggest.min.js"></script>

<script type="text/javascript">
$(function()
{
	$("#brand_input")
	.suggest({
		all_types:true})
		.bind("fb-select", function(e, data) {
			$('#idOfKeyword').val(data.id);
			alert(data);
		})
});

function clickClear() {
	document.brand_input_form.txtSearch.value = "";
	document.brand_input_form.idOfKeyword.value = "";
	
}


</script>

</head>

<body>

<form action="prepareSearchGraphs.php" name="brand_input_form" id="brand_input_form" method="post">
    <input type="text" name="txtSearch" onclick="clickClear()" class="brand_input" id="brand_input" value="search" maxlength="50" length="30"> 
	<input id="idOfKeyword" name="idOfKeyword" value="" DISABLED/>
</form>


<?php 

function getTypeFromId() {
	
	$jsonquerystr = "http://www.freebase.com/api/service/mqlread?query={%20%22extended%22:1%2C%20%22query%22%3A%20{%20%22id%22:%20%22".idOfKeyword.value."%22%2C%20%22type%22:%20%22/common/topic%22%2C%20%22notable_for%22:%20null%20}%20}";
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL,$jsonquerystr);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
	curl_setopt($ch,CURLOPT_CONNECTTIMEOUT,10);
	$jsonresultstr = curl_exec($ch);
	curl_close($ch);
	$resultarray = json_decode($jsonresultstr, true);
	$notableFor = $resultarray["result"]["notable_for"];
	
	return $notableFor;
}

?>

</body>

</html>