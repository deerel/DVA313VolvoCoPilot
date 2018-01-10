<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Safe Assist</title>
  <style type="text/css">
    body {
      font-family: Arial, sans-serif;
      padding: 0;
      border: 0;
      position: absolute;
      background-image: url("background.jpg");
      font-size: 13px;
      background-size: 100%; 
    }
    .studentform {
      float: left;
      margin-top: 100px;
    }

    .username, .password {
     margin:0 0 0 50px;
     background-color: white; 
     border-color: blue;
     width: 300px; 
     height: 30px;}
     .forgot {
      margin: 0 0 0 50px;
    }
    .lab{
     margin:0 0 0 50px;
     font-family: Arial;
     font-weight: bold;}

     .signin{
      margin:0 0 0 50px;
      font-family: Arial;
      font-size: 13px;
      width: 89px; 
      height: 30px;
      background-color: #0055ff;
      color: white; }
      a, a:hover, a:visited {
        color: #427fed;
        cursor: pointer;
        text-decoration: underline;
      }
      a:hover {
       color: grey;
     }
     .register{
      margin:0 0 0 80px;
    }
    .image {
     margin:0 0 0 150px;
   }

   .boxtransparent {

     width: 400px;
     height: 450px;
     margin-left: 150px;
     background-color: white;
     background: rgba(255,255,255,0.8);

   }
   .instructorform{

     float: right;
     margin-top: 100px;
     margin-left: 190px;
     

   }

 </style>

</head>
<body> 
  <div class = "studentform">
    <form method="POST" action="components/login-process-adm.php" enctype="multipart/form-data">
      <p>

        <div class="boxtransparent">
          <p>
          </br></br>
          <img class="image" src="user.png" width: 100px;>
        </p>
        <label class="lab">ADMINISTRATOR USERNAME</label><p>
        <p>
          <input type="text" class="username" name="username" required="required" autocomplete="off" placeholder="  Username"></p>
          <p>
            <label class="lab">PASSWORD</label></p>

            <input type="password" class="password" name="password" required="required" autocomplete="off">
          </p>

          <!-- Forget Password Option for Version 2.0 -->
         <!--  <span class="separator">·</span>
         <a class="forgot" href="" rel="noopener">Forgot password?</a> -->
         <p>
          <input type="submit" class="signin" name="submit" value="SIGN IN">
        </p>
      </div>
    </form>
  </div>

  <div class = "instructorform">
    <form method="POST" action="components/login-process-man.php" enctype="multipart/form-data" >
      <p>
        <div class="boxtransparent" >
          <p>
          </br></br>
          <img class="image" src="user.png" width: 100px;>
        </p>
        <label class="lab">MANAGER USERNAME</label><p>
        <p>
          <input type="text" class="username" name="username" required="required" placeholder="  Username"></p>
          <p>
            <label class="lab">PASSWORD</label></p>

            <input type="password" class="password" name="password" required="required" autocomplete="current-password">

          </p>

          <!-- Forget Password Option for Version 2.0 -->
          <!-- <span class="separator">·</span>
          <a class="forgot" href="" rel="noopener">Forgot password?</a> -->
          <p>
            <input type="submit" class="signin" name="submit" value="SIGN IN">
          </p>

        </div>
      </form>
    </div>


      <?php
      session_start();
      require '_database/database.php';

      if(isset($_SESSION['username'])){
        if(isset($_SESSION['lloji'])){
          if($_SESSION['lloji']==1)
            header("location: SIDORELA.php");
          else if($_SESSION['lloji']==2)
             header("location: v_index.html");


        }
        
      }

      ?>

   

  </body>
  </html>