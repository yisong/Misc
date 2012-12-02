<?php

	require 'config.php';
     
    try {
     
        $db = new PDO($dsn, $username, $password);
        $db->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );
         
        $sth = $db->query("select column_name from information_schema.columns where table_name= 'lineitem10' order by ordinal_position");
        $data = $sth->fetchAll();
         
        echo json_encode( $data );
         
    } catch (Exception $e) {
        echo $e->getMessage();
    }
?>