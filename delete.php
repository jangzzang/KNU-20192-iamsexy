<?php 

   $con = mysqli_connect("localhost","root","","MobileCommunity");
    mysqli_set_charset($con,"utf8");
		
	
   $id = $_GET ['id'];
   $password = $_GET['password'];
   
   if(!$con){
      echo "MySQL 접속에러";
      echo mysqli_connect_error();
      exit();      
   }
   
  
   
   
   
   $sql = "select *from person where id='".$id."' and password='".$password."' ";
   $result = mysqli_query($con,$sql);
   $sqldelete ="DELETE FROM person where id='".$id."' and password='".$password."' ";
   $delete = mysqli_query($con,$sqldelete);
   
   
   
   if($result->num_rows){
	 mysqli_query($con,$sqldelete);
	 echo 'success';
   }else{
     echo 'failure';  
   }
  ?>