<?php
	
	$con = mysqli_connect("localhost","root","","MobileCommunity");
	mysqli_set_charset($con,"utf8");
	date_default_timezone_set('Asia/Seoul');

	if(!$con){
		echo "MYSQL 접속에러";
		echo mysqli_connect_error();
		exit();		
	}
	
	$no = $_GET['no'];
	
	$sql= "select *from comment where idnumber='".$no."' ";
	$result = mysqli_query($con,$sql);
	$data = array();
	
	if($result){
		while($row=mysqli_fetch_array($result)){
        array_push($data, 
            array('id'=>$row[0],
            'username'=>$row[1],
            'contents'=>$row[2],
			'time'=>$row[3]
		//	'password'=>$row[4],
			//'username'=>$row[5],
		//	'time'=>$row[6]
			));
    }

   
	header('Content-Type: application/json; charset=utf8');
	$json = json_encode(array("innerboard"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
	echo $json;
	}  
	else{  
		echo "SQL문 처리중 에러 발생 : "; 
		echo mysqli_error($con);
	}	 
	mysqli_close($con);    
?>

