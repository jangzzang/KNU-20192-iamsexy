<?php
	$con = mysqli_connect("127.0.0.1","root","","MobileCommunity");
	mysqli_set_charset($con,"utf8");
	
	if(mysqli_connect_errno($con)){
		echo "Failed to connect to MySQL:" . mysqli_connect_error();
	}
	
	$year = $_POST['year'];
	$month = $_POST['month'];
	$date = $_POST['date'];
	$contents = $_POST['contents'];
	
	

	$result =mysqli_query($con,"insert into schedule (year,month,date,contents) values ('$year','$month','$date','$contents')");

	if($result){
		echo 'success';
	}else{
		echo 'failure';
	}
	mysqli_close($con);
?>
