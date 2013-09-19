<?php include_once 'includes/DbUtils.php'; ?>
<?php

$con = connect();

$myusername=$_POST['email'];
$mypassword=$_POST['pwd'];

// To protect MySQL injection
$myusername = stripslashes($myusername);
$mypassword = stripslashes($mypassword);
$myusername = trim(mysql_real_escape_string($myusername));
$mypassword = trim(mysql_real_escape_string($mypassword));

$sql="select ID from t_eval_user where email='$myusername' and password='$mypassword' and status=0";
$result=mysql_query($sql);

while($row = mysql_fetch_array($result)) {
	$userId = $row['ID'];
}

$count=mysql_num_rows($result);

if($count==1){
	if (!isset($_SESSION['userId'])) {
		session_start();
		$_SESSION['userId'] = $userId;
		$_SESSION['email'] = $myusername;
		if(array_key_exists('yon', $_GET)) {
			header( 'Location: ' . $_GET['yon']);
		} else {
			header( 'Location: user.php');
		}
		
	}
} else {
	$loc = 'Location: index.php?re';
	if(array_key_exists('yon', $_GET)) {
		$loc .= '&yon=' . $_GET['yon'];
	}
	header($loc);
}
?>