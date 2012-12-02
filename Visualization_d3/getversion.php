<?php
     
    require 'config.php';
     
    try {
     
        $db = new PDO($dsn, $username, $password);
        $db->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );
         
        $sth = $db->query("SELECT max(version) FROM Display");
        $data = $sth->fetch();
         
        echo $data['max'];
         
    } catch (Exception $e) {
        echo $e->getMessage();
    }

?>