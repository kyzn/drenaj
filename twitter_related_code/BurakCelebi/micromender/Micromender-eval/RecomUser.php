<?php

class RecomUser {
	
	public $id;
	public $screenName;
	
	function __construct($id,$screenName) {
		$this->id = $id;
		$this->screenName = $screenName;
	}
}

?>