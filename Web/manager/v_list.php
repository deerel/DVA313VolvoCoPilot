<?php include 'components/authentication.php' ?>  
<?php include 'components/session-check.php' ?>
<?php include '../_database/database.php'; ?>
<?php
// ???
error_reporting(E_ALL);
ini_set("display_errors","On");


?>

<?php 
if(isset($_SESSION['username'])){
  if(isset($_SESSION['lloji'])){

    if($_SESSION['lloji']==1)
     header("location: volvo.png");
}
}
?>



<!-- 
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
} -->

<?p include_once 'includes/header.php'; define('MYSQL_ASSOC',MYSQLI_ASSOC);
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
                <a href="#" class="simple-text">
                    Safe Assist
                </a>
            </div>
            <ul class="nav">
                <li >
                    <a href="v_index.php">
                        <i class="pe-7s-graph"></i>
                        <p>Dashboard</p>
                    </a>
                </li>
                <li class="active">
                    <a href="v_list.php">
                        <i class="pe-7s-user"></i>
                        <p>List of Construction Sites</p>
                    </a>
                </li>
                <li>
                    <a href="v_add.html">
                        <i class="pe-7s-add-user"></i>
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
                    <a href="v_logout.php">
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
                                <th>Site ID</th>
                                <th>Site Name ______________________</th>
                                <th>P1-longitute</th>
                                <th>P1-latitude</th>
                                <th>P2-longitute</th>
                                <th>P2-latitude</th>
                            </thead>
                            <tbody>
                                        <!-- <tr>
                                        	<td>kot</td>
                                        	<td>kot</td>
                                        	<td>kot</td>
                                        	<td>kot</td>
                                        	<td>kot</td>
                                            <td>kot</td>
                                        </tr> -->
                                        <?php  
                                        $query = "SELECT site_id, site_name, p1_longitude, p1_latitude, p2_longitude, p2_latitude FROM ConstructionSite";


                                        $result =  mysqli_query($conn, $query) or die(mysqli_errno());
                                        $rws= mysqli_num_rows($result);

                                        if (($result)||(mysqli_errno == 0))
                                        {
                                            echo "<table width='100%'><tr>";
                                            if (mysqli_num_rows($result)>0)
                                            {
                                        //loop thru the field names to print the correct headers
                                                // $i = 0;
                                                // while ($i < mysqli_num_fields($result))
                                                // {
                                                //     echo "<th>". mysqli_fetch_fields($result, $i) . "</th>";
                                                //     $i++;
                                                // }
                                                // echo "</tr>";

                                    //display the data
                                                while ($rows = mysqli_fetch_array($result,MYSQLI_ASSOC))
                                                {
                                                    echo "<tr>";
                                                    foreach ($rows as $data)
                                                    {
                                                        echo "<td align='left'>". $data . "</td>";
                                                    }
                                                }
                                            }else{
                                                echo "<tr><td colspan='" . ($i+1) . "'>No Results found!</td></tr>";
                                            }
                                            echo "</table>";
                                        }else{
                                            echo "Error in running query :". mysqli_error();
                                        }
                                        ?>
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