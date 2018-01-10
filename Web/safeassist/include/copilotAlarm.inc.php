<?php
include_once "connection.inc.php";

/*
const ALARM_ALARM_LEVEL_0 = 3;
const ALARM_ALARM_LEVEL_1 = 4;
const ALARM_ALARM_LEVEL_2 = 5;
const ALARM_ALARM_LEVEL_3 = 6;
const ALARM_NOTIFICATION = 7;
const ALARM_NO_RESPONSE = 8;
*/
class CopilotAlarm {

    private $db;
    private $worker;
    private $workerLocation;
    private $copilotAlarm;
    private $workerAlarm;

    public function __construct($worker) {
        $this->db = new Connection();
        $this->worker = $worker;
	      if($this->setWorkersLocation() == ALARM_NO_RESPONSE) {
            return ALARM_NO_RESPONSE;
            die();
        }
    }

    public function __destruct(){
  		$this->db = null;
  	}

    public function getAlarm() {
        $isInside = $this->isInsideConstructionSite();
/*
        if($isInside == ALARM_NO_RESPONSE) {
            return ALARM_NO_RESPONSE;
        }*/

      if($isInside) {
        $sql = "SELECT * FROM Worker WHERE worker_id != '".$this->worker."'";
        $this->workerAlarm = $this->compareToWorkers($this->db->query($sql));

        $sql2 = "SELECT * FROM Copilot WHERE copilot_id != '".$this->worker."'";
        $this->copilotAlarm = $this->compareToCopilots($this->db->query($sql2));


        if($this->workerAlarm >= $this->copilotAlarm){
          return $this->workerAlarm;
        }else{
          return $this->copilotAlarm;
        }
  	  } else {
  			return ALARM_ALARM_LEVEL_0;
  		}
    }

    private function setWorkersLocation() {
        //$sql = "SELECT * FROM Worker WHERE worker_id = '".$this->worker."'";
        $sql = "SELECT * FROM Copilot WHERE copilot_id = '".$this->worker."'";
        $result = $this->db->query($sql);

        if ($row = $result->fetch()) {
            $lat1 = $row["latitude"];
            $lon1 = $row["longitude"];
            $this->workerLocation = array("longitude" => $row["longitude"], "latitude" => $row["latitude"]);
            return 0; // Ok!
        } else {
            return ALARM_NO_RESPONSE;
        }
    }

    private function compareToWorkers($others) {
        while($row = $others->fetch()) {
            $shortest = 1000000.0; // Stores the shortest distance between worker and the others to find the minimum
            if($row["latitude"] != "") {
                $worker = $row["worker_id"];
                $distance = $this->distance($this->workerLocation["latitude"], $this->workerLocation["longitude"], $row["latitude"], $row["longitude"]);
                if($distance < $shortest) $shortest = $distance;
                if( $distance < 5) {
					         return ALARM_ALARM_LEVEL_3;
                } elseif ($distance < 10) {
					         return ALARM_ALARM_LEVEL_2;
                }
            }
        }
        return ALARM_ALARM_LEVEL_1;
    }

    private function compareToCopilots($others) {
        while($row = $others->fetch()) {
            $shortest = 1000000.0; // Stores the shortest distance between worker and the others to find the minimum
            if($row["latitude"] != "") {
                $worker = $row["copilot_id"];
                $distance = $this->distance($this->workerLocation["latitude"], $this->workerLocation["longitude"], $row["latitude"], $row["longitude"]);
                if($distance < $shortest) $shortest = $distance;
                if( $distance < 10) {
					         return ALARM_ALARM_LEVEL_3;
                } elseif ($distance < 50) {
					         return ALARM_ALARM_LEVEL_2;
                }
            }
        }
        return ALARM_ALARM_LEVEL_1;
    }

    private function distance($lat1, $lon1, $lat2, $lon2) {

      $R = 6371.392896;

      $latDistance = deg2rad($lat2 - $lat1);
      $lonDistance = deg2rad($lon2 - $lon1);
      $a = sin($latDistance / 2) * sin($latDistance / 2)
              + cos(deg2rad($lat1)) * cos(deg2rad($lat2))
              * sin($lonDistance / 2) * sin($lonDistance / 2);
      $c = 2 * atan2(sqrt($a), sqrt(1 - $a));
      $distance = $R * $c * 1000; // convert to meters

      $distance = pow($distance, 2);

      return sqrt($distance);

	}

	private function isInsideConstructionSite() {
			try {
			$pdo = $this->db->getInstance();
			$stmt = $pdo->prepare("SELECT * FROM ConstructionSite WHERE p1_latitude > :lat1 && p1_longitude < :lon1 && p2_latitude < :lat2 && p2_longitude > :lon2");
			$stmt->bindParam(':lat1', $this->workerLocation["latitude"]);
			$stmt->bindParam(':lon1', $this->workerLocation["longitude"]);
			$stmt->bindParam(':lat2', $this->workerLocation["latitude"]);
			$stmt->bindParam(':lon2', $this->workerLocation["longitude"]);
			$stmt->execute();
			$result = $stmt->fetchAll();
			}
			catch(PDOException $e) {
				return $e->getMessage();
				return ALARM_NO_RESPONSE;
				//die();
			}

			return count($result)>0 ? true : false;
	}

}

?>
