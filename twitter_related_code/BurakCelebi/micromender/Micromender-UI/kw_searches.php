<?php include_once 'preprocess.php'; ?>

<a href="kw_search_main.php">New Search</a>

<table class="zebra-striped">

<thead><tr>
	<th>Query</th>
	<th>Freebase Id</th>
	<th>Freebase Type</th>
	<th>Type</th>
	<th>Status</th>
</tr></thead>
<tbody>
<?php
$con = connect();
$result = mysql_query('SELECT ID, QUERY, FREEBASE_ID, FREEBASE_TYPE, TYPE, STATUS FROM T_KW_QUERY ORDER BY ID DESC');
while($row = mysql_fetch_array($result)) {
	echo '<tr><td><a href=kw_search_result.php?id=' . $row['ID'] . '>' . $row['QUERY'] . '</a></td><td>'. $row['FREEBASE_ID'] .'</td><td>'. $row['FREEBASE_TYPE'] .'</td><td>'. $row['TYPE'] .'</td><td>'. $row['STATUS'] .'</td></tr>';
}
closeCon($con);

?>
<tbody>
</table>

<script type="text/javascript">
	var active_nav = "nav_search";
</script>

<?php include_once 'postprocess.php'; ?>