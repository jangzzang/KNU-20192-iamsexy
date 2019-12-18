<?php 
	$con = mysqli_connect("localhost","root","","MobileCommunity");
	if(!$con){
		echo "MYSQL 접속에러";
		echo mysqli_connect_error();
		exit();		
	}
	date_default_timezone_set('Asia/Seoul');

	mysqli_set_charset($con,"utf8");

	$sql= "select *from free_board ORDER BY date DESC";
	$result = mysqli_query($con,$sql);
	$data = array();
	
	if($result){
		while($row=mysqli_fetch_array($result)){
        array_push($data, 
            array('num'=>$row[0],
          //  'title'=>$row[1],
            'contents'=>$row[2]
	//		'id'=>$row[3],
		//	'password'=>$row[4],
			//'username'=>$row[5],
		//	'time'=>$row[6]
			));
    }

   
	header('Content-Type: application/json; charset=utf8');
	$json = json_encode(array("noticeitem"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
	echo $json;
	}  
	else{  
		echo "SQL문 처리중 에러 발생 : "; 
		echo mysqli_error($con);
	}	 
	mysqli_close($con);    
?>

