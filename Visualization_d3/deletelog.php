<?php

   require 'config.php';

   $sign = $_GET["sign"];
   $value = $_GET["value"];
   $version = $_GET["version"];
   $name = $_GET["name"];

   try {
      $db = new PDO($dsn, $username, $password);
      $db->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );

      $stmt = $db->prepare("DELETE FROM Log WHERE name=:name And sign=:sign And value=:value And version=:version");
      $stmt->bindParam(':sign', $sign);
      $stmt->bindParam(':value', $value);
      $stmt->bindParam(':version', $version);
 
    	 $stmt->bindParam(':name', $name);
	 $stmt->execute();
     
  } catch (Exception $e) {
      echo $e->getMessage();
  }
?>