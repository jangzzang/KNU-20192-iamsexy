<?php

	$con=mysqli_connect("127.0.0.1","root","","MobileCommunity");  
	mysqli_set_charset($con,"utf8");	

	
	$id = $_GET['id'];
	$username = $_GET['username'];
	$status = $_GET['status'];
	$newstatus = $_GET['newstatus'];
	
	if(!$con){
		echo "MySQL error";
	    echo mysqli_connect_error();
        exit();      
   }
	
	
	$sqlchange = "UPDATE person SET status = '".$newstatus."' WHERE username = '".$username."' and id='".$id."' ";
	
	$change =mysqli_query($con,$sqlchange);
	
	
	if($change){
		mysqli_query($con,$change);
		echo 'success';	
	}else {
		echo 'failure';
		
	}
	
?>