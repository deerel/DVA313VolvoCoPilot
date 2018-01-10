<?php
session_start();
require_once 'includes/auth_validate.php';
require_once './config/config.php';
//$query = "select field1, fieldn from table [where clause][group by clause][order by clause][limit clause]";

$query = "select site_id, site_name, p1_longitude, p1_latitude, p2_longitude, p2_latitude from ConstructionSite";
$result = conn($query);
if (($result)||(mysql_errno == 0))
{
  echo "<table width='100%'><tr>";
  if (mysql_num_rows($result)>0)
  {
          //loop thru the field names to print the correct headers
          $i = 0;
          while ($i < mysql_num_fields($result))
          {
       echo "<th>". mysql_field_name($result, $i) . "</th>";
       $i++;
    }
    echo "</tr>";

    //display the data
    while ($rows = mysql_fetch_array($result,MYSQL_ASSOC))
    {
      echo "<tr>";
      foreach ($rows as $data)
      {
        echo "<td align='center'>". $data . "</td>";
      }
    }
  }else{
    echo "<tr><td colspan='" . ($i+1) . "'>No Results found!</td></tr>";
  }
  echo "</table>";
}else{
  echo "Error in running query :". mysql_error();
}
?>