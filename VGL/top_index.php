<html>
<head>
<title>Saved VGL Problems from Bio 111 Fall 2005</title>
</head>
<body bgcolor=lightcyan>
<center><font size=+3>Saved VGL Problems from Bio 111 Fall 2005</font></center>
<br>
Click on the link to your section from the list below.<br>
<font color=red>You may have to disable your pop-up blocker.</font>
<ul>
<?php 
  $dh = opendir('.');
  while (false != ($fileName = readdir($dh))) {
    if (is_dir($fileName) && ($fileName != '.') && ($fileName != '..')) {
      $files[] = $fileName;
    }
  }
  closedir($dh);
  
  sort($files);
  
  $dirName = ereg_replace('Documents/', '', strstr(getcwd(), 'Documents/'));
  
  foreach ($files As $fileName) {
    echo "<li><a href=\"$fileName/\">$fileName</a></li>\n";
  }
?>
</ul>


</body>
</html>