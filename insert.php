<?php  

$con=mysqli_connect("127.0.0.1","root","","MobileCommunity");  

 
mysqli_set_charset($con,"utf8");

if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  

$id = $_POST['id'];  
$password = $_POST['password'];  
$studentid = $_POST['studentid'];
$username = $_POST['username'];
$phonenumber =$_POST['phonenum'];


  

$result = mysqli_query($con,"insert into Person (id,password,studentid,username,phonenum,status) values ('$id','$password','$studentid','$username','$phonenumber',0)");  



  if($result){  

    echo 'success';  

  }  
  else{  

    echo 'failure';  

  }  


mysqli_close($con);  

?> 


 