<?php
error_reporting(E_ALL);
ini_set("display_errors","On");

class Connection {

    private $host = 'localhost';
    private $db   = '';
    private $user = '';
    private $pass = '';
    private $charset = 'utf8mb4';

    private $dsn;
    
    private $opt = [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES   => false,
    ];
    private $pdo;
    
    public function __construct() {
        $this->connect();
    }
    
    public function connect() {
        $this->dsn = "mysql:host=$this->host;dbname=$this->db;charset=$this->charset";
        try {
        $this->pdo = new PDO($this->dsn, $this->user, $this->pass, $this->opt);
        } catch (Exception $e) {
            print "Error: " . $e->getMessage();
            die();
        }
    }
	
	public function getInstance() {
		return $this->pdo;
	}
    
    public function query($sql) {
        
        try {
            $result = $this->pdo->query($sql);
        } catch (Exception $e) {
            print "Error: " . $e->getMessage();
            die();
        }
        
        return $result;
    }

}


?>