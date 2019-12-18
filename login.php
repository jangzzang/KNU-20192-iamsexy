<?php  

$con=mysqli_connect("localhost","root","", "MobileCommunity" );  
if (!$con)  
{  
    echo "MySQL 접속 에러 : ";
    echo mysqli_connect_error();
    exit();  
}  

mysqli_set_charset($con,"utf8"); 

$sql="select * from person";
$result=mysqli_query($con,$sql);
$data = array();   

if($result){  
    
    while($row=mysqli_fetch_array($result)){
        array_push($data, 
            array('id'=>$row[0],
            'password'=>$row[1],
            'studentid'=>$row[2],
			'username'=>$row[3],
			'status'=>$row[5]
		));
    }

    //echo "<pre>"; print_r($data); echo '</pre>';
	header('Content-Type: application/json; charset=utf8');
	$json = json_encode(array("webnautes"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
	echo $json;
}  
else{  
    echo "SQL문 처리중 에러 발생 : "; 
    echo mysqli_error($con);
} 
mysqli_close($con);    
?>

