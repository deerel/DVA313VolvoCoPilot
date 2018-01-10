<?php
session_start();
require_once 'includes/auth_validate.php';
require_once './config/config.php';

// Serve deletion if POST method and del_id is set.

//Get data from query string
$search_string = filter_input(INPUT_GET, 'search_string');


$filter_col = filter_input(INPUT_GET, 'filter_col');
$order_by = filter_input(INPUT_GET, 'order_by');
$page = filter_input(INPUT_GET, 'page');
$pagelimit = 20;
if ($page == "") {
    $page = 1;
}
// If filter types are not selected we show latest added data first
if ($filter_col == "") {
    $filter_col = "id";
}
if ($order_by == "") {
    $order_by = "desc";
}

// select the columns
$select = array('id', 'f_name', 'l_name', 'gender', 'phone');

// If user searches 
if ($search_string) 
{
    $db->where('f_name', '%' . $search_string . '%', 'like');
    $db->orwhere('l_name', '%' . $search_string . '%', 'like');
}


if ($order_by) 
{
    $db->orderBy($filter_col, $order_by);
}

$db->pageLimit = $pagelimit;
$customers = $db->arraybuilder()->paginate("customers", $page, $select);
$total_pages = $db->totalPages;

// get columns for order filter
foreach ($customers as $value) {
    foreach ($value as $col_name => $col_value) {
        $filter_options[$col_name] = $col_name;
    }
    //execute only once
    break;
}
include_once 'includes/header.php';
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
				<li class="active">
                    <a href="table.html">
                        <i class="pe-7s-user"></i>
                        <p>List of Operators</p>
                    </a>
                </li>
                <li>
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
                            <div class="content table-responsive table-full-width">
                                <table class="table table-hover table-striped">
                                    <thead>
                                        <th>ID</th>
                                    	<th>Name</th>
                                    	<th>Salary</th>
                                    	<th>Construction ID</th>
                                    	<th>Job</th>
                                    </thead>
                                    <tbody>
                                        <tr>
                                        	<td>Operator_ID</td>
                                        	<td>Dakota Rice</td>
                                        	<td>$36,738</td>
                                        	<td>1</td>
                                        	<td>Truck driver</td>
                                        </tr>
                                        <tr>
                                        	<td>Operator_ID</td>
                                        	<td>Minerva Hooper</td>
                                        	<td>$23,789</td>
                                        	<td>2</td>
                                        	<td>Machine driver</td>
                                        </tr>
                                        <tr>
                                        	<td>Operator_ID</td>
                                        	<td>Sage Rodriguez</td>
                                        	<td>$56,142</td>
                                        	<td>2</td>
                                        	<td>Worker X</td>
                                        </tr>
                                        <tr>
                                        	<td>Operator_ID</td>
                                        	<td>Philip Chaney</td>
                                        	<td>$38,735</td>
                                        	<td>2</td>
                                        	<td>Worker X</td>
                                        </tr>
                                        <tr>
                                        	<td>Operator_ID</td>
                                        	<td>Doris Greene</td>
                                        	<td>$63,542</td>
                                        	<td>3</td>
                                        	<td>Truck driver</td>
                                        </tr>
                                        <tr>
                                        	<td>Operator_ID</td>
                                        	<td>Mason Porter</td>
                                        	<td>$78,615</td>
                                        	<td>3</td>
                                        	<td>Worker Y</td>
                                        </tr>
                                    </tbody>
                                </table>

                            </div>
                        </div>
                    </div>


                   
                </div>
            </div>
        </div>
<!--
		<table class="table table-striped table-bordered table-condensed">
        <thead>
            <tr>
                <th class="header">#</th>
                <th>Name</th>
                <th>Gender</th>
                <th>phone</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <?php
            foreach ($customers as $row) { ?>
                <tr>
	                <td><?php echo $row['id'] ?></td>
	                <td><?php echo $row['f_name']." ".$row['l_name'] ?></td>
	                <td><?php echo $row['gender'] ?></td>
	                <td><?php echo $row['phone'] ?> </td>
	                <td>
					<a href="edit_customer.php?customer_id=<?php echo $row['id'] ?>&operation=edit" class="btn btn-primary" style="margin-right: 8px;"><span class="glyphicon glyphicon-edit"></span>

					<a href=""  class="btn btn-danger delete_btn" data-toggle="modal" data-target="#confirm-delete-<?php echo $row['id'] ?>" style="margin-right: 8px;"><span class="glyphicon glyphicon-trash"></span></td>
				</tr>

						<!-- Delete Confirmation Modal-->
			<!--		 <div class="modal fade" id="confirm-delete-<?php echo $row['id'] ?>" role="dialog">
					    <div class="modal-dialog">
					      <form action="delete_customer.php" method="POST">
					      <!-- Modal content-->
					<!--	      <div class="modal-content">
						        <div class="modal-header">
						          <button type="button" class="close" data-dismiss="modal">&times;</button>
						          <h4 class="modal-title">Confirm</h4>
						        </div>
						        <div class="modal-body">
						      
						        		<input type="hidden" name="del_id" id = "del_id" value="<?php echo $row['id'] ?>">
						        	
						          <p>Are you sure you want to delete this customer?</p>
						        </div>
						        <div class="modal-footer">
						        	<button type="submit" class="btn btn-default pull-left">Yes</button>
						         	<button type="button" class="btn btn-default" data-dismiss="modal">No</button>
						        </div>
						      </div>
					      </form>
					      
					    </div>
  					</div>

                
                
           
            <?php } ?>      
        </tbody>
    </table> 
	--->
        


    </div>
</div>


</body>

    <!--   Core JS Files   -->
    <script src="assets/js/jquery.3.2.1.min.js" type="text/javascript"></script>
	<script src="assets/js/bootstrap.min.js" type="text/javascript"></script>
    <!--  Google Maps Plugin    -->
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE"></script>
   
<?php include_once './includes/footer.php'; ?>