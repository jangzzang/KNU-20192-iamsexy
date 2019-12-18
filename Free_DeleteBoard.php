<?php

	$con=mysqli_connect("127.0.0.1","root","","MobileCommunity");  
	mysqli_set_charset($con,"utf8");	

	
	
	$no = $_GET['no'];
	
	
   if(!$con){
      echo "MySQL 접속에러";
      echo mysqli_connect_error();
      exit();      
   }

	
	$sqldelete="DELETE FROM free_board where free_number='".$no."' ";
	$comment_sqldelete = " DELETE FROM free_comment where idnumber='".$no."'";
	
	$delete = mysqli_query($con,$sqldelete);
	$comment_delete = mysqli_query($con,$comment_sqldelete);
	
	if($delete){
		mysqli_query($con,$sqldelete);
		echo 'success';	
	}else {
		echo 'failure';
		
	}

?>
