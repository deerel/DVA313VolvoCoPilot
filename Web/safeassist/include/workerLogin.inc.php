<?php
include_once "connection.inc.php";


class WorkerLogin {

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
            $stmt = $this->db->getInstance()->prepare("SELECT worker_id FROM Worker WHERE worker_id = :username && password = :password");
            $stmt->bindParam(':username', $this->username);
            $stmt->bindParam(':password', $this->password);
            $stmt->execute();
        }
        catch(PDOException $e) {
            return "Validation error";
        }

        if($stmt->rowCount() > 0) {
            //maybe fetching is not really neccassery since the username is unique and the executing gives us only 1 row if the user existed or not
            $return = $stmt->fetch(PDO::FETCH_ASSOC);
            if($return["worker_id"] == $this->username) {
                return "Inserting successful";
            } else {
                return "Error during login";
            }
            return "Unexpected error";
        }

        return "Username or password was wrong";

    }

}



?>
