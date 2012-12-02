<?php

   $output;
   exec("java -jar commit.jar", $output);

   $s = $output[count($output) - 1];
   if ($s == "Invalid Constraint") {
      echo $s;
   }

?>