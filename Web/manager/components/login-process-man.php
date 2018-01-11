<?php

error_reporting(E_ALL);
ini_set("display_errors","On");

session_start();

if(isset($_POST['submit']))
    // ||$_REQUEST['auto']==1)
{

    include '../_database/database.php';
    $errmsg_arr = array();

    $username = trim($_POST['username']);
    $password =  trim($_POST['password']);
    
    $sql="SELECT id,name,password,role  FROM Worker WHERE name='$username' AND password='$password'";
    $prova = 0;
    
    
    $result =  mysqli_query($conn, $sql) or die(mysqli_error($conn));
    $rws= mysqli_num_rows($result);
 
    if($rws==1){
        // if($rws['role']==="MANAGER"){
        $rws =  mysqli_fetch_array($result);
        $_SESSION['username']=$rws['name']; 
        $_SESSION['password']=$rws['password']; 
        $_SESSION['id'] = $rws['id']; 
        $_SESSION['lloji'] = 2; 
        header("location:../v_index.php");
            // ?name=$username&request=login&status=success"); 
    }
    else{
        echo("error:" .mysqli_connect_errno()." dhe pastaj kemi: " .$prova );
    } 

}
else { 

    $errmsg_arr[] = 'user name and password not found';
    $errflag = true;
    if($errflag) {
        $_SESSION['ERRMSG_ARR'] = $errmsg_arr;
        session_write_close();
        header("location:https://www.volvo.com/home.html");
        exit();

    }
}


?>