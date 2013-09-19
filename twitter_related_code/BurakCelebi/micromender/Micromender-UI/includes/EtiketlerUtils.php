<?php

function get_tag_data($tagCloudSql) {

	$con = connect();
	$result = mysql_query($tagCloudSql);

	$arr = Array();
	while($row = mysql_fetch_array($result)) {
		$tag = $row['tag'];
		$tagCount = $row['weight'];
		$arr[$tag] = $tagCount;
	}
	
    return $arr;
}

function get_tag_cloud($tagCloudSql) {

	$cloud_html = '';

	// Default font sizes
	$min_font_size = 10;
	$max_font_size = 40;

	// Pull in tag data
	$tags = get_tag_data($tagCloudSql);

	if (count($tags)>0) {
	
		$minimum_count = min(array_values($tags));
		$maximum_count = max(array_values($tags));
		$spread = $maximum_count - $minimum_count;

		if($spread == 0) {
			$spread = 1;
		}

		$cloud_tags = array(); // create an array to hold tag code
		foreach ($tags as $tag => $count) {
			
			$size = $min_font_size + ($count - $minimum_count) * ($max_font_size - $min_font_size) / $spread;				
			$cloud_tags[] = '<span style="font-size: '. floor($size) . 'px' . '" >'
			              . '<a href="#" class="tag_cloud" rel="twipsy" data-original-title="' . $count . '">'
			              . htmlspecialchars(stripslashes($tag)) . '</a></span>';
		}
		$cloud_html = join("\n", $cloud_tags) . "\n";
	}
	
	return $cloud_html;

}

/*
function etiketler($haberId) {
	$select_sql = "SELECT t.tag, t.id FROM posts p, post_tags pt, tags t WHERE p.id = $haberId AND p.id = pt.post_id AND pt.tag_id = t.id";
	$result = mysql_query($select_sql );

	$tagStr = '';
	while($row = mysql_fetch_array($result)) {
		$tagStr = $tagStr . '<a href="index.php?etiket=' . $row['id'] . '">' . $row['tag'] . '</a>, ';
	}
	
	return substr($tagStr, 0, -2);
}

function etiketlerCsv($haberId) {
	$select_sql = "SELECT t.tag, t.id FROM posts p, post_tags pt, tags t WHERE p.id = $haberId AND p.id = pt.post_id AND pt.tag_id = t.id";
	$result = mysql_query($select_sql );

	$tagStr = '';
	while($row = mysql_fetch_array($result)) {
		$tagStr = $tagStr . $row['tag'] . ', ';
	}
	
	return substr($tagStr, 0, -2);
}

function twitter($haberId) {
	$select_sql = "SELECT t.tag FROM posts p, post_tags pt, tags t WHERE p.id = $haberId AND p.id = pt.post_id AND pt.tag_id = t.id";
	$result = mysql_query($select_sql );

	$tagStr = '';
	while($row = mysql_fetch_array($result)) {
		$tagStr = $tagStr . '+' . $row['tag'];
	}
	
	echo '<a href="http://search.twitter.com/search?q=' . $tagStr . '">Search in Twitter</a>';
}

function etiketlerTekStrHaber($haberId) {
	$select_sql = ' SELECT GROUP_CONCAT(t.tag SEPARATOR \' \') etiketler' .
				' FROM posts p, post_tags pt, tags t' .
				   " WHERE p.id = $haberId" .
					' AND p.id = pt.post_id' .
					' AND pt.tag_id = t.id' .
				   ' GROUP BY p.id';
				   
	$result = mysql_query($select_sql);
	if($row = mysql_fetch_array($result)) {
		return $row['etiketler'];
	}
}

function etiketlerTekStrUser($userId) {
	$con = connect();
	$select_sql = ' SELECT GROUP_CONCAT(t.tag SEPARATOR \' \') etiketler ' .
				  ' FROM posts p, post_tags pt, tags t ' .
					" WHERE p.user_id = $userId " .
					' AND p.id = pt.post_id ' .
					' AND pt.tag_id = t.id ' .
					' GROUP BY p.user_id ';
				   
	$result = mysql_query($select_sql);
	if($row = mysql_fetch_array($result)) {
		return $row['etiketler'];
	}
}

*/

?>