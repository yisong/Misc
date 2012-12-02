<?php

    $v=$_GET["v"];
    $n=$_GET["n"];         
    $lines = file("getnewtabletid.sql", FILE_IGNORE_NEW_LINES);

    require 'config.php';
     
    try {
     
        $db = new PDO($dsn, $username, $password);
        $db->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );
         
      $stmt = $db->prepare($lines[0]);

      $stmt->bindParam(':version', $v);
      $stmt->bindParam(':n', $n);
     $stmt->execute();  
     $data = $stmt->fetchAll();

         
        echo json_encode( $data );
         
    } catch (Exception $e) {
        echo $e->getMessage();
    }
?>