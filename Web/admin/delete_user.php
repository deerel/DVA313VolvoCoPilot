<?php 
session_start();
require_once 'includes/auth_validate.php';
require_once './config/config.php';
$del_id = filter_input(INPUT_POST, 'del_id');
if ($del_id && $_SERVER['REQUEST_METHOD'] == 'POST') 
{

	if($_SESSION['admin_type']!='super'){
		$_SESSION['failure'] = "You don't have permission to perform this action";
    	header('location: customers.php');
        exit;

	}
    $customer_id = $del_id;
    $db->where('id', $customer_id);
    $status = $db->delete('customers');
    
    if ($status) 
    {
        $_SESSION['info'] = "Customer deleted successfully!";
        header('location: customers.php');
        exit;
    }
    else
    {
    	$_SESSION['failure'] = "Unable to delete customer";
    	header('location: customers.php');
        exit;

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
    <div class="sidebar" data-color="blue" data-image="">

    <!--   you can change the color of the sidebar using: data-color="blue | azure | green | orange | red | purple" -->


    	<div class="sidebar-wrapper">
            <div class="logo">
                <a href="http://www.creative-tim.com" class="simple-text">
                    Safe Assist
                </a>
            </div>

            <ul class="nav">
                <li >
                    <a href="dashboard.html">
                        <i class="pe-7s-graph"></i>
                        <p>Dashboard</p>
                    </a>
                </li>
				<li >
                    <a href="table.html">
                        <i class="pe-7s-graph"></i>
                        <p>List of Operators</p>
                    </a>
                </li>
                <li>
                    <a href="addOperator.html">
                        <i class="pe-7s-user"></i>
                        <p>Add Operator</p>
                    </a>
                </li>
                <li>
                    <a href="editOperator.html">
                        <i class="pe-7s-note2"></i>
                        <p>Edit Operator</p>
                    </a>
                </li>
                <li class="active">
                    <a href="deleteOperator.html">
                        <i class="pe-7s-news-paper"></i>
                        <p>Delete Operator</p>
                    </a>
                </li>             
                <li>
                    <a href="maps.html">
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
                    <a class="navbar-brand" href="#">Dashboard</a>
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


        <div class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="header">
                                <h4 class="title">Delete Operator</h4>
                            </div>
                            <div class="content">
                                <form>
                                    <div class="row">
                                                                                
										<div class="col-md-3">
                                            <div class="form-group">
                                                <label>ID</label>
                                                <input type="text" class="form-control" placeholder="Id" >
                                            </div>
                                        </div>
                                       
										<div class="col-md-4">
                                            <div class="form-group">
                                                <label for="exampleInputEmail1">Construction Area ID</label>
                                                <input type="email" class="form-control" placeholder="Construction Area the operator will be working on">
                                            </div>
                                        </div>
										
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>First Name</label>
                                                <input type="text" class="form-control" placeholder="Company" >
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label>Last Name</label>
                                                <input type="text" class="form-control" placeholder="Last Name" >
                                            </div>
                                        </div>
                                    </div>                                                                                               
                                    <button type="submit" class="btn btn-info btn-fill pull-right">Delete</button>
                                    <div class="clearfix"></div>
                                </form>
                            </div>
                        </div>
                    </div>                
                </div>
            </div>
        </div>      
    </div>
</div>


</body>
    <!--   Core JS Files   -->
    <script src="assets/js/jquery.3.2.1.min.js" type="text/javascript"></script>
	<script src="assets/js/bootstrap.min.js" type="text/javascript"></script>
    <!--  Google Maps Plugin    -->
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE"></script> 

