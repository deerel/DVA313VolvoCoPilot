<?php
session_start();
require_once './config/config.php';
require_once './includes/auth_validate.php';


//serve POST method, After successful insert, redirect to customers.php page.
if ($_SERVER['REQUEST_METHOD'] == 'POST') 
{
    //Mass Insert Data. Keep "name" attribute in html form same as column name in mysql table.
    $data_to_store = filter_input_array(INPUT_POST);
    $last_id = $db->insert ('customers', $data_to_store);
    
    if($last_id)
    {
    	$_SESSION['success'] = "Customer added successfully!";
    	header('location: customers.php');
    	exit();
    }  
}

//We are using same form for adding and editing. This is a create form so declare $edit = false.
$edit = false;

require_once 'includes/header.php'; 
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
        <div class="sidebar-wrapper">
            <div class="logo">
                <a class="simple-text">
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
                <li class="active">
                    <a href="addOperator.html">
                        <i class="pe-7s-add-user"></i>
                        <p>Add Operator</p>
                    </a>
                </li>
                <li>
                    <a href="editOperator.html">
                        <i class="pe-7s-note2"></i>
                        <p>Edit Operator</p>
                    </a>
                </li>
                <li>
                    <a href="deleteOperator.html">
                        <i class="pe-7s-delete-user"></i>
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

<!--   INSTEAD OF CLASS CONTENT
<div id="page-wrapper">
<div class="row">
     <div class="col-lg-12">
            <h2 class="page-header">Add Customers</h2>
        </div>
        
</div>
    <form class="form" action="" method="post"  id="customer_form" enctype="multipart/form-data">
       <?php  include_once('./includes/forms/customer_form.php'); ?>
    </form>
</div> -->

        <div class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="header">
                                <h4 class="title">Add Operator</h4>
                            </div>
                            <div class="content">
                                <form>
                                    <div class="row">
                                        
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                <label>Username</label>
                                                <input type="text" class="form-control" placeholder="Username" >
                                            </div>
                                        </div>
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
										<div class="col-md-4">
                                            <div class="form-group">
                                                <label for="exampleInputEmail1">Job</label>
                                                <input type="email" class="form-control" placeholder="Job">
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group">
                                                <label>About Operator</label>
                                                <textarea rows="5" class="form-control" placeholder="Information about the type of job operator will do"></textarea>
                                            </div>
                                        </div>
                                    </div>

                                    <button type="submit" class="btn btn-info btn-fill pull-right">Save</button>
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
   <script type="text/javascript">
$(document).ready(function(){
   $("#customer_form").validate({
       rules: {
            f_name: {
                required: true,
                minlength: 3
            },
            l_name: {
                required: true,
                minlength: 3
            },   
        }
    });
});
</script>

<?php include_once 'includes/footer.php'; ?>


