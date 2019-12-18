<?php

	$con = mysqli_connect("localhost","root","","MobileCommunity");
	mysqli_set_charset($con,"utf8");

	
	if (mysqli_connect_errno($con))  	
	{  
		echo "Failed to connect to MySQL: " . mysqli_connect_error();  
	}  
	
	date_default_timezone_set('Asia/Seoul');
	
	$title = $_POST['title'];	
	$contents = $_POST['contents'];
	$number = $_POST['num'];  
	$date= date('Y-m-d H:i:s');
	

	
	
	
	$query = "UPDATE board SET title='".$title."',contents='".$contents."',date='".$date."' where number='".$number."' ";
	$result =mysqli_query($con,$query);	

	
	if($result){  
		echo 'success';  

	}  
	else{  
    echo 'failure';  
	}  
	mysqli_close($con);
	
	
?>
