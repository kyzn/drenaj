<?php
ob_start();
header( 'Location: ../' . $_GET['page'] . '.php') ;
ob_flush();
?>