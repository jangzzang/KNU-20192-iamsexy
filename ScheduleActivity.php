<?php
	
	$con = mysqli_connect("localhost","root","","MobileCommunity");
	mysqli_set_charset($con,"utf8");
		
		if(!$con){
			echo "MYSQL 접속에러";
			echo mysqli_connect_error();
			exit();
	
		}
		
		$year = $_GET['year'];
		$month = $_GET['month'];
		$date = $_GET['date'];
		
		$sql ="select *from schedule where year='".$year."' and month='".$month."' and date= '".$date."' ";
		$result =mysqli_query($con,$sql);
		$data =array();
		
		if($result){
		while($row=mysqli_fetch_array($result)){
        array_push($data, 
            array('year'=>$row[0],
            'month'=>$row[1],
            'date'=>$row[2],
			'contents'=>$row[3]
			));
		}

		
		header('Content-Type: application/json; charset=utf8');
		$json = json_encode(array("scheduleactivity"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
		echo $json;
		}  
		else{  
			echo "SQL문 처리중 에러 발생 : "; 
			echo mysqli_error($con);
		}	 
		mysqli_close($con);    
?>
