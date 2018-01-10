<?php

session_start();
if(isset($_POST['submit'])||$_REQUEST['auto']==1)
{

    require '../_database/database.php';
    $errmsg_arr = array();
    $username = trim($_POST['username']);
    $password =  trim($_POST['password']);
    
    $sql="SELECT ID,username,password FROM studentdb WHERE username='$username'AND password='$password'";
    
    
    $result =  mysqli_query($conn, $sql) or die(mysqli_errno());
    $rws= mysqli_num_rows($result);

    if($rws==1){
        $rws =  mysqli_fetch_array($result);
        $_SESSION['username']=$rws['username'];
        $_SESSION['password']=$rws['password'];
        $_SESSION['id'] = $rws['ID'];
        $_SESSION['lloji'] = 1;
        header("location:../home.php");    
    
    }
    else { 
        $errmsg_arr[] = 'user name and password not found';
        $errflag = true;
        if($errflag) {
            $_SESSION['ERRMSG_ARR'] = $errmsg_arr;
            session_write_close();
            header("location:../components/authentication-check.php ");
            exit();
        }
    }
}
?>