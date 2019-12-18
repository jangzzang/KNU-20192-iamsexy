<?php 
	$con = mysqli_connect("localhost","root","","MobileCommunity");
	if(!$con){
		echo "MYSQL 접속에러";
		echo mysqli_connect_error();
		exit();		
	}
	date_default_timezone_set('Asia/Seoul');

	mysqli_set_charset($con,"utf8");

	$sql= "select *from board ORDER BY number DESC";
	$result = mysqli_query($con,$sql);
	
	$row = mysqli_fetch_array($result);
	$timetocheck =strtotime($row[6]);
	$now= date('Y-m-d H:i:s');
	$timetonow = strtotime($now);
	$usetime = $timetonow-$timetocheck;
	$data = array();
	
	if($usetime<=1800){
		array_push($data,
			array(
			'title'=>$row[1]));		
	
	header('Content-Type: application/json; charset=utf8');
	$json = json_encode(array("notice"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
	echo $json;
	
	}else if ($usetime>1800){
		
		echo 0;
	}
	else{  
		echo "SQL문 처리중 에러 발생 : "; 
		echo mysqli_error($con);
	}	 
	mysqli_close($con);    
?>

