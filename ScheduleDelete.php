
<?php

	$con=mysqli_connect("127.0.0.1","root","","MobileCommunity");  
	mysqli_set_charset($con,"utf8");	

	
	
	$year = $_GET['year'];
	$month = $_GET['month'];
	$day = $_GET['day'];
	
	
   if(!$con){
      echo "MySQL 접속에러";
      echo mysqli_connect_error();
      exit();      
   }

	
	$sqldelete="DELETE FROM schedule where year='".$year."' and month='".$month."' and date = '".$day."' ";
	$delete = mysqli_query($con,$sqldelete);
	
	if($delete){
		mysqli_query($con,$sqldelete);
		echo 'success';	
	}else {
		echo 'failure';
		
	}

?>
