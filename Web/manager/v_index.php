<?php include 'components/authentication.php' ?>  
<?php include 'components/session-check.php' ?>
<?php include '../_database/database.php'; ?>
<?php
// ???
error_reporting(E_ALL);
ini_set("display_errors","On");



// session_start();
// require_once './config/config.php';
// require_once 'includes/auth_validate.php';


//Get Dashboard information
//$db->get('customers');
//$numCustomers = $db->count; -->

include_once('includes/header.php');
?>
<?php 
 if(isset($_SESSION['username'])){
              if(isset($_SESSION['lloji'])){
        
            if($_SESSION['lloji']==1)
                   header("location: volvo.png");
        }
      }
?>

<head>
	<meta charset="utf-8" />
	<link rel="icon" type="image/png" href="assets/img/favicon.ico">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
    <meta name="viewport" content="width=device-width" />

    <!-- Bootstrap core CSS     -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet" />
    <!--  Light Bootstrap Table core CSS    -->
    <link href="assets/css/light-bootstrap-dashboard.css?v=1.4.0" rel="stylesheet"/>
    <!--     Fonts and icons     -->
    <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <link href='http://fonts.googleapis.com/css?family=Roboto:400,700,300' rel='stylesheet' type='text/css'>
    <link href="assets/css/pe-icon-7-stroke.css" rel="stylesheet" />
</head>
<body>

<div class="wrapper">
    <div class="sidebar" data-color="blue" data-image=""> <!-- change color  -->
 	<div class="sidebar-wrapper">
            <div class="logo">
                <a href="#" class="simple-text">
                    Safe Assist
                </a>
            </div>

            <ul class="nav">
                <li class="active">
                    <a href="v_index.html">
                        <i class="pe-7s-graph"></i>
                        <p>Dashboard</p>
                    </a>
                </li>
				<li >
                    <a href="v_list.html">
                        <i class="pe-7s-graph"></i>
                        <p>List of Construction Sites</p>
                    </a>
                </li>
                <li>
                    <a href="v_add.html">
                        <i class="pe-7s-user"></i>
                        <p>Add Construction Site</p>
                    </a>
                </li>
                <li>
                    <a href="v_edit.html">
                        <i class="pe-7s-note2"></i>
                        <p>Edit Construction Site</p>
                    </a>
                </li>
                <li>
                    <a href="v_delete.html">
                        <i class="pe-7s-news-paper"></i>
                        <p>Delete Construction Site</p>
                    </a>
                </li>
              
                <li>
                    <a href="v_maps.html">
                        <i class="pe-7s-map-marker"></i>
                        <p>Maps</p>
                    </a>
                </li>
               
			
            </ul>
    	</div>
    </div>

    <div class="main-panel">
        <nav class="navbar navbar-default navbar-fixed">
            <div class="container-fluid" >
                <div class="navbar-header">                  
                    <a class="navbar-brand" href="v_index.html">Dashboard</a>
                </div>
                <div class="collapse navbar-collapse">
                    <ul class="nav navbar-nav navbar-left">                                          
                        <li>
                           <a href="">
                                <i class="fa fa-search"></i>
								<p class="hidden-lg hidden-md">Search</p>
                            </a>
                        </li>
                    </ul>

                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a href="#">
                                <p>Log out</p>
                            </a>
                        </li>						
                    </ul>
                </div>
            </div>
        </nav>
        </div>
    </div>
</div>


</body>

    <!--   Core JS Files   -->
    <script src="assets/js/jquery.3.2.1.min.js" type="text/javascript"></script>
	<script src="assets/js/bootstrap.min.js" type="text/javascript"></script>
    <!--  Google Maps Plugin    -->
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE"></script>

<?php include_once('includes/footer.php'); ?>