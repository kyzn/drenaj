<?php include_once 'includes/Utils.php'; ?>
<?php include_once 'includes/DbUtils.php'; ?>
<?php include_once 'includes/EtiketlerUtils.php'; ?>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Twitter Recommender</title>
    <meta name="description" content="Twitter User Recommendation">
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
    
    
    <!-- <visualsearch> -->
    
    <!-- 
	  <script src="lib/js/views/search_box.js" type="text/javascript" charset="utf-8"></script>
	  <script src="lib/js/views/search_facet.js" type="text/javascript" charset="utf-8"></script>
	  <script src="lib/js/views/search_input.js" type="text/javascript" charset="utf-8"></script>
	  <script src="lib/js/models/search_facets.js" type="text/javascript" charset="utf-8"></script>
	  <script src="lib/js/models/search_query.js" type="text/javascript" charset="utf-8"></script>
	  <script src="lib/js/utils/backbone_extensions.js" type="text/javascript" charset="utf-8"></script>
	  <script src="lib/js/utils/hotkeys.js" type="text/javascript" charset="utf-8"></script>
	  <script src="lib/js/utils/jquery_extensions.js" type="text/javascript" charset="utf-8"></script>
	  <script src="lib/js/utils/search_parser.js" type="text/javascript" charset="utf-8"></script>
	  <script src="lib/js/utils/inflector.js" type="text/javascript" charset="utf-8"></script>
	  <script src="lib/js/templates/templates.js" type="text/javascript" charset="utf-8"></script>
 -->
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

  </head>

  <body>

    <div class="topbar" data-scrollspy="scrollspy">
      <div class="topbar-inner">
        <div class="container">
          <a class="brand" href="index.php">Recommender</a>
          <ul class="nav">
            <li id="nav_search"><a href="search.php">New Search</a></li>
            <li id="nav_searches"><a href="searches.php">Searches</a></li>
            <li id="nav_about"><a href="about.php">About</a></li>
            <li id="nav_contact"><a href="contact.php">Contact</a></li>
          </ul>
          <!-- 
          <form class="pull-left" method="post" action="searches.php">
            <input name="query" type="text" placeholder="Search" />
          </form>
           -->
           <ul class="nav secondary-nav">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle">University</a>
              <ul class="dropdown-menu">
                <li><a href="http://www.cmpe.boun.edu.tr/soslab">SosLab</a></li>
                <li><a href="http://www.cmpe.boun.edu.tr">CMPE</a></li>
                <li class="divider"></li>
                <li><a href="http://www.boun.edu.tr">Bogazici University</a></li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    </div>


    <div class="container">
