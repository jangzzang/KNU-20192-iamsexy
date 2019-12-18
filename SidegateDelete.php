<?php

	$con=mysqli_connect("127.0.0.1","root","","MobileCommunity");  
	mysqli_set_charset($con,"utf8");	
	$no = $_GET['no'];
	
   if(!$con){
      echo "MySQL 접속에러";
      echo mysqli_connect_error();
      exit();      
   }

	
	$sqldelete="DELETE FROM side_board where number='".$no."' ";
	$delete = mysqli_query($con,$sqldelete);
	
	if($delete){
		mysqli_query($con,$sqldelete);
		echo 'success';	
	}else {
		echo 'failure';
		
	}

?>
