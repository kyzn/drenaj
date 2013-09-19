<?php include_once 'preprocess.php'; ?>

<?php 

if (isset($_SESSION['userId'])) {

	$con = connect();
	$userId = $_SESSION['userId'];
	?>
	
	<div class="alert-message warning">
		<br/><p align="center">Have you answered our <a href="https://docs.google.com/spreadsheet/viewform?formkey=dDBIckk1WUlEU0o1ZnZPN2c2S1RrQXc6MQ#gid=0" target="_blank">2-minute survey</a>? 
		You can submit before the evaluation. Thanks!</p><br/>
	</div>
	
	<table class="zebra-striped">
	
	<thead><tr>
		<th>Query</th>
		<th>Status</th>
	</tr></thead>
	<tbody>
	<?php
	$con = connect();
	$result = mysql_query('select ID, QUERY, STATUS, (select v_val from t_ref where id=\'t_eval_user_query__status\' and i_key=uq.status) STATUS_DESC from t_eval_user_query uq where USER_ID='.$userId . ' and STATUS>-1 order by status, query');
	while($row = mysql_fetch_array($result)) {
		
		if ($row['STATUS']=='1' ) {
			echo '<tr><td><a href=recom.php?id=' . $row['ID'] . '>' . $row['QUERY'] . '</a></td>';
		} else {
			echo '<tr><td>' . $row['QUERY'] . '</td>';
		}
		
		echo '<td>'. $row['STATUS_DESC'] . '</td></tr>';
	}
	closeCon($con);
	
	?>
	<tbody>
	</table>

<?php 
} else {
?>
	Please login.
<?php 
} 
?>
	
<script type="text/javascript">
	var active_nav = "nav_users";
</script>
	
<?php include_once 'postprocess.php'; ?>
