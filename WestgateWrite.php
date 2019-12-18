<?php  

$con=mysqli_connect("127.0.0.1","root","","MobileCommunity");  

 
mysqli_set_charset($con,"utf8");

if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  
	date_default_timezone_set('Asia/Seoul');
	$title = $_POST['title'];
	$contents = $_POST['contents'];
	$id = $_POST['id'];  
	$password = $_POST['password'];  
	$username = $_POST['username'];
	$date = date('Y-m-d H:i:s');
	


$result = mysqli_query($con,"insert into west_board (number,title,contents,id,password,username,time) values (null,'$title','$contents','$id','$password','$username','$date')");  

  

  if($result){  

    echo 'success';  

  }  

  else{  

    echo 'failure';  

  }  

  

  

mysqli_close($con);  
?> 


 