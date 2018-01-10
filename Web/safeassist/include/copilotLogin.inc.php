<?php
error_reporting(E_ALL);
ini_set("display_errors","On");
include_once "connection.inc.php";


class CopilotLogin {

    private $db;
    private $username;
    private $password;

    public function __construct($username, $password) {
        $this->db = new Connection();
        $this->username = $username;
        $this->password = $password;
    }

    public function __destruct(){
  		$this->db = null;
  	}

    public function validate(){
        try {
            $stmt = $this->db->getInstance()->prepare("SELECT role FROM Worker WHERE worker_id = :username && password = :password");
            $stmt->bindParam(':username', $this->username);
            $stmt->bindParam(':password', $this->password);
            $stmt->execute();
        }
        catch(PDOException $e) {
            return "Validation error";
        }
      //  return $stmt->rowCount();

        if($stmt->rowCount() > 0) {

            $check = $this->db->getInstance()->prepare("SELECT copilot_id FROM Copilot WHERE copilot_id = :copilot_id");
            $check->bindParam(':copilot_id', $this->username);
            $check->execute();

            //there is already a copilot row
            if($check->rowCount() > 0) {
              $assoc = $this->db->getInstance()->prepare("INSERT INTO AssocUandC (copilot_id, worker_id) VALUES (:copilot_id, :worker_id)");
              $assoc->bindParam(':copilot_id', $this->username);
              $assoc->bindParam(':worker_id', $this->username);
              $assoc->execute();

              return "Inserting successful";
            }else{
              $insert = $this->db->getInstance()->prepare("INSERT INTO Copilot (copilot_id) VALUES (:copilot_id)");
              $insert->bindParam(':copilot_id', $this->username);

              if($insert->execute()){
                $assoc = $this->db->getInstance()->prepare("INSERT INTO AssocUandC (copilot_id, worker_id) VALUES (:copilot_id, :worker_id)");
                $assoc->bindParam(':copilot_id', $this->username);
                $assoc->bindParam(':worker_id', $this->username);
                $assoc->execute();

                return "Inserting successful";
              }else{
                return "Could not insert the new worker";
              }
              /*$return = $stmt->fetch(PDO::FETCH_ASSOC);
              if($return["worker_id"] == $this->username) {
                  return "Inserting successful";
              } else {
                  return "No worker with that username was found";
              }
              return "Unexpected error";*/
            }

        }else{
          return "No worker with that username was found";
        }

        //return "Username or password was wrong";

    }

}



?>
