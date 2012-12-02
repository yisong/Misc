<?php

  
    require 'config.php';
     
    try {
     
        $db = new PDO($dsn, $username, $password);
        $db->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );
         
        $sth = $db->query("SELECT * FROM Log Order by version");
        $data = $sth->fetchAll();
         
        echo json_encode( $data );
         
    } catch (Exception $e) {
        echo $e->getMessage();
    }
?>