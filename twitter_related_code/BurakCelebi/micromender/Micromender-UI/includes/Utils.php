<?php include_once 'PorterStemmer.php'; ?>
<?php
	session_start();
	
	function e($str) {
		echo $str;
	}
	
	function thisPage() {
		$currentFile = $_SERVER["SCRIPT_NAME"];
		$parts = Explode('/', $currentFile);
		$currentFile = $parts[count($parts) - 1];
		return $currentFile;
	}
	
	function sortByLength($arr1, $arr2) {
    $c1 = count($arr1);
    $c2 = count($arr2);

    return $c1 < $c2 ? -1 : $c1 == $c2 ? 0 : 1;
}


function lengthAtLeast($var) {
	return count($var) > 0;
}

function lengthAtLeastU($var) {
	return count($var) > 1;
}

function compareFunc($v1,$v2) {

$v1=PorterStemmer::Stem($v1);
$v2=PorterStemmer::Stem($v2);

if ($v1===$v2)
  {
  return 0;
  }
if ($v1 > $v2) return 1;
  {
  return -1;
  }
return 1;
}
	
   
function genRandomString($length = ""){

  $code = md5(uniqid(rand(), true));
  if ($length != "") return substr($code, 0, $length);   
  else return $code;
  
}

function redirectTo($page) {
	ob_start();
	header( 'Location: ' . $page.'.php') ;
	ob_flush();
}

function getPageName() {
	return basename($_SERVER['SCRIPT_NAME'], '.php');	
}

?>