<?php
     
    require 'config.php';
    $v = $_GET["v"];  
   
    try {
     
        $db = new PDO($dsn, $username, $password);
        $db->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );
         
        $sth = $db->exec("DELETE FROM Log WHERE version > '".$v."'");
        $sth = $db->exec("DELETE FROM Display WHERE version > '".$v."'");
        $sth = $db->exec("DELETE FROM newlinetable WHERE version > '".$v."'");
         
    } catch (Exception $e) {
        echo $e->getMessage();
    }

?>