<?php  

$con=mysqli_connect("127.0.0.1","root","","MobileCommunity");  

 
mysqli_set_charset($con,"utf8");

if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  

$idnumber = $_POST['idnumber'];  
$username = $_POST['username'];  
$contents = $_POST['contents'];
$time = date('Y-m-d H:i:s');



  

$result = mysqli_query($con,"insert into free_comment (idnumber, username, contents,time) values ('$idnumber','$username','$contents','$time')");  

  

  if($result){  

    echo 'success';  

  }  

  else{  

    echo 'failure';  

  }  

  

  

mysqli_close($con);  
?> 


 