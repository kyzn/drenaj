<?php include_once 'includes/Utils.php'; ?>
<?php include_once 'includes/DbUtils.php'; ?>
<?php include_once 'includes/EtiketlerUtils.php'; ?>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Micromender Evaluation</title>
    <meta name="description" content="Micromender User Evaluation">
    
    
	<!-- 
		PLEASE DO NOT TRY TO BREAK IT :)
		THANKS.
	-->
	
	
    <meta name="author" content="Burak Celebi">

	<script type="text/javascript" src="js/microrecom.js"></script>
	<style type="text/css">@import url('css/microrecom.css');</style>
	<style type="text/css">@import url('css/my-d3.css');</style>
	

    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- Le styles -->
    <link href="css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 60px;
      }
    </style>

    <!-- Le fav and touch icons -->
    <link rel="shortcut icon" href="images/favicon.ico">
    <link rel="apple-touch-icon" href="images/apple-touch-icon.png">
    <link rel="apple-touch-icon" sizes="72x72" href="images/apple-touch-icon-72x72.png">
    <link rel="apple-touch-icon" sizes="114x114" href="images/apple-touch-icon-114x114.png">
    
    <script src="js/jquery/jquery-1.7.min.js"></script>
    <script src="js/jquery/jquery.tablesorter.min.js"></script>

<!-- <visualsearch> -->
	  <script src="js/jquery/jquery.ui.core.js" type="text/javascript" charset="utf-8"></script>
	  <script src="js/jquery/jquery.ui.widget.js" type="text/javascript" charset="utf-8"></script>
	  <script src="js/jquery/jquery.ui.position.js" type="text/javascript" charset="utf-8"></script>
	  <script src="js/jquery/jquery.ui.autocomplete.js" type="text/javascript" charset="utf-8"></script>
	  <script src="js/underscore-1.1.5.js" type="text/javascript" charset="utf-8"></script>
	  <script src="js/backbone-0.5.0.js" type="text/javascript" charset="utf-8"></script>
<!-- <visualsearch> -->

    <script src="js/bootstrap-dropdown.js"></script>
    <script src="js/bootstrap-twipsy.js"></script>
    <script src="js/bootstrap-popover.js"></script>
    
    
    <script src="js/highcharts/highcharts.js"></script>
    
    
    <script src="js/visualsearch.js" type="text/javascript"></script>
	<!--[if (!IE)|(gte IE 8)]><!-->
	  <link href="css/visualsearch-datauri.css" media="screen" rel="stylesheet" type="text/css" />
	<!--<![endif]-->
	<!--[if lte IE 7]><!-->
	  <link href="css/visualsearch.css" media="screen" rel="stylesheet" type="text/css" />
	<!--<![endif]-->
	
	
	<!-- 
	
	<script src="js/assets/highlight.js" type="text/javascript" charset="utf-8"></script>
	<script src="js/assets/javascript.js" type="text/javascript" charset="utf-8"></script>
	<script src="js/assets/xml.js" type="text/javascript" charset="utf-8"></script>
	<script>
	    hljs.initHighlightingOnLoad();
	</script>
	 -->

	<!-- </visualsearch> -->


	<!-- <d3> -->
	<!-- 
	<script type="text/javascript" src="http://mbostock.github.com/d3/d3.js?2.3.0"></script>
    <script type="text/javascript" src="http://mbostock.github.com/d3/d3.geom.js?2.3.0"></script>
    <script type="text/javascript" src="http://mbostock.github.com/d3/d3.layout.js?2.3.0"></script>
    -->
    <script type="text/javascript" src="js/d3/d3.js"></script>
    <script type="text/javascript" src="js/d3/d3.geom.js"></script>
    <script type="text/javascript" src="js/d3/d3.layout.js"></script>
    
	<!-- </d3> -->

	<script type="text/javascript">
	
		function fn_login() {	
			var email = document.getElementById('email').value;
			var pwd = document.getElementById('pwd').value;

			if(email==='' || pwd==='') {
				alert('Please enter your email and password');
			} else {
				document.loginForm.submit();
			}
			
		}
		
	</script>

  </head>

  <body>

    <div class="topbar" data-scrollspy="scrollspy">
      <div class="topbar-inner">
        <div class="container">
          <a class="brand" href="index.php">Micromender Evaluation</a>
          <ul class="nav">
            <?php if (isset($_SESSION['userId'])) { ?>
            	<li id="nav_users"><a href="user.php">My Queries</a></li>
            <?php } ?>
            <li id="nav_contact"><a href="contact.php">Contact</a></li>
          </ul>
          <?php if (isset($_SESSION['userId'])) { ?>
          <ul class="nav secondary-nav">
          	<li id="nav_email" class="active"><a href="user.php"><?php echo $_SESSION['email']; ?></a></li>
          	<li id="nav_logout"><a href="logout.php">Logout</a></li>
          </ul>
          <?php } else { ?>
	          <form name="loginForm" action="login_control.php" method="post" class="pull-right">
	            <input id="email" name="email" class="span3" type="text" placeholder="E-mail">
	            <input id="pwd" name="pwd" class="span3" type="password" placeholder="Password">
	            <button class="btn" type="submit" onclick="fn_login();">Sign in</button>
	          </form>
          <?php } ?>
        </div>
      </div>
    </div>


    <div class="container">
