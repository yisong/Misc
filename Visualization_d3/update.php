<?php

   require 'config.php';

   $sign = $_GET["sign"];
   $value = $_GET["value"];
   $version = $_GET["version"];
   $name = explode(" % ", $_GET["name"]);

   try {
      $db = new PDO($dsn, $username, $password);
      $db->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );

      $stmt = $db->prepare("INSERT INTO Log VALUES(:name, :sign, :value, :version)");
      $stmt->bindParam(':sign', $sign);
      $stmt->bindParam(':value', $value);
      $stmt->bindParam(':version', $version);
 
      for($i = 0; $i < count($name); $i++){
   	 $stmt->bindParam(':name', $name[$i]);
	 $stmt->execute();
     }
  } catch (Exception $e) {
      echo $e->getMessage();
  }
?>