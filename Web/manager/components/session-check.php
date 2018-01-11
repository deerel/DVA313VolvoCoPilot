<?php
    @session_start();
    require '_database/database.php';
    if(!$_SESSION['username']){
        header("location:https://www.volvo.com/home.html");
    }
?>