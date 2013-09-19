<?php include_once 'preprocess.php'; ?>

<?php 

if  ( isset($_POST['query']) ) {
	$query = strtolower( addslashes($_POST['query']) );
	query(connect(), "INSERT INTO t_search (query) VALUES ('$query')");
}
?>
<meta http-equiv="refresh" content="30">

<table class="zebra-striped">

<thead><tr>
	<th>Query</th>
	<th>Freebase Id</th>
	<th>Freebase Type</th>
	<th>Start Time</th>
	<th>End Time</th>
	<th>Status</th>
</tr></thead>
<tbody>
<?php
$con = connect();
$result = mysql_query('SELECT ID, START_TIME, FINISH_TIME, QUERY, (select FREEBASE_ID from t_kw_query where id=s.semantic_search_id) FREEBASE_ID, (select FREEBASE_TYPE from t_kw_query where id=s.semantic_search_id) FREEBASE_TYPE, STATUS FROM T_SEARCH s ORDER BY ID DESC');
while($row = mysql_fetch_array($result)) {
	echo '<tr><td><a href=show_search_main.php?id=' . $row['ID'] . '>' . $row['QUERY'] . '</a></td><td>'. $row['FREEBASE_ID'] .'</td><td>'. $row['FREEBASE_TYPE'] .'</td><td>' . $row['START_TIME'] . '</td><td>'. $row['FINISH_TIME'] . '</td><td>'. decodeQueryStatus($row['STATUS']) .'</td></tr>';
}
closeCon($con);

function decodeQueryStatus($status) {
	$arr = array('0'=>'label">invalid', '1'=>'label important">not started', '2'=>'label warning">processing', '3'=>'label success">finished');
	return '<span class="' . $arr[$status] . '</span>';
}

?>
<tbody>
</table>

<script type="text/javascript">
	var active_nav = "nav_searches";
</script>

<?php include_once 'postprocess.php'; ?>
