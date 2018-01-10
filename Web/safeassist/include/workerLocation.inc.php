<?php
include_once "connection.inc.php";

class WorkerLocation {

	private $db;
	private $worker;

	public function __construct($worker) {
		$this->db = new Connection();
		$this->worker = $worker;
  }

	public function __destruct(){
		$this->db = null;
	}

	public function setLocation($latitude, $longitude) {
		if(is_float($latitude + 0) && is_float($longitude + 0)) {
			try {
				$stmt = $this->db->getInstance()->prepare("UPDATE Worker SET latitude = :latitude, longitude = :longitude WHERE worker_id = :worker_id");
				$stmt->bindParam(':latitude', $latitude);
				$stmt->bindParam(':longitude', $longitude);
				$stmt->bindParam(':worker_id', $this->worker);
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
