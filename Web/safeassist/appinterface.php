<?php
error_reporting(E_ALL);
ini_set("display_errors","On");

include_once "./include/workerLogin.inc.php";
include_once "./include/copilotLogin.inc.php";
include_once "./include/workerAlarm.inc.php";
include_once "./include/copilotAlarm.inc.php";
include_once "./include/workerLocation.inc.php";
include_once "./include/copilotLocation.inc.php";
include_once "./include/gpstest.inc.php";
include_once "./include/removeAssoc.inc.php";

class AppInterface {

    private $worker_id;

    public function __construct($worker_id, $key) {

        if($key != "s2K4Jd092Kdc78sdKKCs2423")  {
            die();
        }

        $this->worker_id = $worker_id;
    }

    public function execute($action) {

        switch($action) {
            case "workerLogin":
                $result = $this->doWorkerLogin();
                break;
            case "copilotLogin":
                $result = $this->doCopilotLogin();
                break;
            case "workerLocation":
                $result = $this->doWorkerLocation();
                break;
            case "copilotLocation":
                $result = $this->doCopilotLocation();
                break;
            case "workerAlarm":
                $result = $this->doWorkerAlarm();
                break;
            case "copilotAlarm":
                $result = $this->doCopilotAlarm();
                break;
            case "removeAssoc":
                $result = $this->doRemoveAssoc();
                break;
            default:
                echo "Request error";
                die();
        }

        echo $result;
    }

    private function doWorkerLogin() {
      $password = htmlspecialchars($_POST["password"]);
      $login = new WorkerLogin($this->worker_id, $password);
      return $login->validate();
      die();
    }

    private function doCopilotLogin() {
      $password = htmlspecialchars($_POST["password"]);
      $login = new CopilotLogin($this->worker_id, $password);
      return $login->validate();
      die();
    }

    private function doWorkerLocation() {
      $lat = htmlspecialchars($_POST["lat"]);
      $lon = htmlspecialchars($_POST["lon"]);
      $location = new WorkerLocation($this->worker_id);
      return $location->setLocation($lat, $lon);
      die();
    }

    private function doCopilotLocation() {
      $lat = htmlspecialchars($_POST["lat"]);
      $lon = htmlspecialchars($_POST["lon"]);
      $location = new CopilotLocation($this->worker_id);
      return $location->setLocation($lat, $lon);
      die();
    }

    private function doWorkerAlarm() {
      $alarm = new WorkerAlarm($this->worker_id);
      return $alarm->getAlarm();
      die();
    }

    private function doCopilotAlarm() {
      $alarm = new CopilotAlarm($this->worker_id);
      return $alarm->getAlarm();
      die();
    }
    
    private function doRemoveAssoc() {
      $assoc = new RemoveAssoc($this->worker_id);
      return $assoc->removeAssoc();
      die();
    }
    


}

$key = htmlspecialchars($_POST["key"]);
$action = htmlspecialchars($_POST["action"]);
$worker_id = htmlspecialchars($_POST["worker_id"]);

$app = new AppInterface($worker_id, $key);
$app->execute($action);

?>
