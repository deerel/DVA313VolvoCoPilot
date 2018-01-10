<?php
session_start();
require_once './config/config.php';
//If User has already logged in, redirect to dashboard page.
if (isset($_SESSION['user_logged_in']) && $_SESSION['user_logged_in'] === TRUE) {
header('Location:authenticate.php');}


//If user has previously selected "remember me option", his credentials are stored in cookies.
if(isset($_COOKIE['name']) && isset($_COOKIE['password']))
{
	//Get user credentials from cookies.
	$username = filter_var($_COOKIE['name']);
	$passwd = filter_var($_COOKIE['password']);
	$db->where ("name", $username);
	$db->where ("password", $passwd);
   
    if ($db->count >= 1) 
    {
    	//Allow user to login.
        $_SESSION['user_logged_in'] = TRUE;
        $_SESSION['admin_type'] = $row[0]['admin_type'];
        header('Location:index.php');
        exit;
    }
    else //Username Or password might be changed. Unset cookie
    {
    unset($_COOKIE['name']);
    unset($_COOKIE['password']);
    setcookie('name', null, -1, '/');
    setcookie('password', null, -1, '/');
    header('Location:login.php');
    exit;
    }
}



include_once 'includes/header.php';
?>
<div id="page-" class="col-md-4 col-md-offset-4">
	<form class="form loginform" method="POST" action="authenticate.php">
		<div class="login-panel panel panel-default">
			<div class="panel-heading">Please Log in</div>
			<div class="panel-body">
				<div class="form-group">
					<label class="control-label">username</label>
					<input type="text" name="username" class="form-control" required="required">
				</div>
				<div class="form-group">
					<label class="control-label">password</label>
					<input type="password" name="passwd" class="form-control" required="required">
				</div>
				<div class="checkbox">
					<label>
						<input name="remember" type="checkbox" value="1">Remember Me
					</label>
				</div>
				<?php
				if(isset($_SESSION['login_failure'])){ ?>
				<div class="alert alert-danger alert-dismissable fade in">
					<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
					<?php echo $_SESSION['login_failure']; unset($_SESSION['login_failure']);?>
				</div>
				<?php } ?>
				<button type="submit" class="btn btn-success loginField" >Login</button>
			</div>
		</div>
	</form>
</div>
<?php include_once 'includes/footer.php'; ?>