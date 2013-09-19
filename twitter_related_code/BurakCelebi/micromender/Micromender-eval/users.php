<?php include_once 'preprocess.php'; ?>

<table class="zebra-striped">

<thead><tr>
	<th>Name</th>
</tr></thead>
<tbody>
<?php
$con = connect();
$result = mysql_query('select ID, NAME from t_eval_user where STATUS = 0');
while($row = mysql_fetch_array($result)) {
	echo '<tr><td><a href=user.php?id=' . $row['ID'] . '>' . $row['NAME'] . '</a></td></tr>';
}
closeCon($con);


?>
<tbody>
</table>

<script type="text/javascript">
	var active_nav = "nav_users";
</script>

<?php include_once 'postprocess.php'; ?>
