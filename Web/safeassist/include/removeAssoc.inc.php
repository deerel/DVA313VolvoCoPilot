<?php
include_once "connection.inc.php";


class RemoveAssoc {

    private $db;
    private $username;

    public function __construct($username) {
        $this->db = new Connection();
        $this->username = $username;
    }

    public function __destruct(){
  		$this->db = null;
  	}

    public function removeAssoc(){
        $stmt = $this->db->getInstance()->prepare("DELETE FROM AssocUandC WHERE copilot_id = :copilot_id");
        $stmt->bindParam(':copilot_id', $this->username);

        if($stmt->execute()){
          return "Removing successful";
        }else{
          return "Removing failed";
        }
    }

}



?>
