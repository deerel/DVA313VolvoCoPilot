<?php
include_once "connection.inc.php";

class GPSTest {
	
	private $db;
	private $worker;
	
	public function __construct($worker) {
		$this->db = new Connection();
		$this->worker = $worker;
    }
	
	public function setLocation($latitude, $longitude) {
		if(is_float($latitude + 0) && is_float($longitude + 0)) {
			try { 
				$stmt = $this->db->getInstance()->prepare("INSERT INTO gpstest (worker_id, latitude, longitude) VALUES (:worker_id, :latitude, :longitude)");
				$stmt->bindParam(':latitude', $latitude);
				$stmt->bindParam(':longitude', $longitude);
				$stmt->bindParam(':worker_id', $this->worker);
				$stmt->execute();
			}
			catch(PDOException $e) {
				//echo "Insert error";
				echo $e.getMessage();
				return false;
			}
			echo "Inserting gps location successful";
			return true;
		}
		echo "Insert error";
		return false;
	}
	
}

?>