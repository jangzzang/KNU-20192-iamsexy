<?php
	
	$con = mysqli_connect("localhost","root","","MobileCommunity");
	mysqli_set_charset($con,"utf8");
	date_default_timezone_set('Asia/Seoul');

	if(!$con){
		echo "MYSQL 접속에러";
		echo mysqli_connect_error();
		exit();		
	}
	
		
	$sql= "select *from schedule";
	$result = mysqli_query($con,$sql);
	$data = array();
	
	if($result){
		while($row=mysqli_fetch_array($result)){
        array_push($data, 
            array('year'=>$row[0],
            'month'=>$row[1],
            'day'=>$row[2],
			'contents'=>$row[3]
		//	'password'=>$row[4],
			//'username'=>$row[5],
		//	'time'=>$row[6]
			));
    }

   
	header('Content-Type: application/json; charset=utf8');
	$json = json_encode(array("schedule"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
	echo $json;
	}  
	else{  
		echo "SQL문 처리중 에러 발생 : "; 
		echo mysqli_error($con);
	}	 
	mysqli_close($con);    
?>

