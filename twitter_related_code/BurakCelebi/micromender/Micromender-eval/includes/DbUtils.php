<?php
  function connect() {
      // Change this line to reflect whatever Database system you are using:
      $con = mysql_connect ('localhost', 'root', '');
      if (!$con)  {
        die('Could not connect: ' . mysql_error());
      }
      mysql_select_db("recommicro", $con);
      
      return $con;
  }
  
  function query($con, $sql) {
      if (!mysql_query($sql,$con)) {
        die('Error: ' . mysql_error());
      }
  }

  function closeCon($con) {
      mysql_close($con);
  }
?>