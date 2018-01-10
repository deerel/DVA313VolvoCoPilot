<?php
include_once "connection.inc.php";

class CopilotLocation {

	private $db;
	private $copilot;

	public function __construct($copilot) {
		$this->db = new Connection();
		$this->copilot = $copilot;
  }

	public function __destruct(){
		$this->db = null;
	}

	public function setLocation($latitude, $longitude) {
		if(is_float($latitude + 0) && is_float($longitude + 0)) {
			try {
				$stmt = $this->db->getInstance()->prepare("UPDATE Copilot SET latitude = :latitude, longitude = :longitude WHERE copilot_id = :copilot_id");
				$stmt->bindParam(':latitude', $latitude);
				$stmt->bindParam(':longitude', $longitude);
				$stmt->bindParam(':copilot_id', $this->copilot);
				$stmt->execute();
			}
			catch(PDOException $e) {
				return "Insert error";
			}
			return "Inserting gps location successful";
		}
		return "No update";
	}

}

?>
