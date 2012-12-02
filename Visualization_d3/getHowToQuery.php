<?php
require 'config.php';

function full_copy( $source, $target ) {
    if ( is_dir( $source ) ) {
        @mkdir( $target );
        $d = dir( $source );
        while ( FALSE !== ( $entry = $d->read() ) ) {
            if ( $entry == '.' || $entry == '..' ) {
                continue;
            }
            $Entry = $source . '/' . $entry; 
            if ( is_dir( $Entry ) ) {
                full_copy( $Entry, $target . '/' . $entry );
                continue;
            }
            copy( $Entry, $target . '/' . $entry );
        }
 
        $d->close();
    }else {
        copy( $source, $target );
    }
}

$p = $_GET["savedProblems"]; 

$source ='specifications/problem'.$p;
$destination = 'specifications/problem';
full_copy($source, $destination);
unlink("./data/runs/example10/time.txt");
unlink("./data/runs/example10/solverBreakdown.txt");

try {
 
    $db = new PDO($dsn, $username, $password);
    $db->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );
     
    $sth = $db->exec("DELETE FROM Log");
    $sth = $db->exec("DROP TABLE IF EXISTS Display");
    $sth = $db->exec("DROP TABLE IF EXISTS newlinetable");
    
    $f = fopen("specifications/problem/initializeLog.sql", "r");
    $insertSQL = fgets($f, 256);

    $sth = $db->exec($insertSQL);
     
} catch (Exception $e) {
    echo $e->getMessage();
}

$output;
exec("java -jar commit.jar", $output);

$s = $output[count($output) - 1];
if ($s == "Invalid Constraint") {
   echo $s;
}

header( 'Location: ./index.html' ) ;

?>