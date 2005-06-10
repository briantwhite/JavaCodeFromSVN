<html>
<head>
<title>Saved VGL Problems from 
<?php
  $dir = getcwd();
  echo ereg_replace('/', '', strrchr($dir,"/"));
?>
</title>
<script language="JavaScript">
var newWindow;
function showVGL(fileName, shortName) {
	if (newWindow != null) {
		newWindow.close();
    }
	newWindow = window.open("","", "height=200, width=400");
	newWindow.document.writeln("<html><head></head>");
	newWindow.document.writeln("This is VGL Problem: " + shortName + "<br><br>");
	newWindow.document.writeln("<applet code=\"VGLApplet.class\"");
	newWindow.document.writeln("archive=\"../VGL.jar\" name=\"VGL\" ");
	newWindow.document.writeln("width=350 height=150>");
	newWindow.document.writeln("<PARAM NAME=\"workFile\" VALUE=\"http://intro.bio.umb.edu/" + fileName + "\">");
    newWindow.document.writeln("</applet> <br>");
	newWindow.document.writeln("</body></html>");
}

</script>
</head>
<body bgcolor=lightblue onunload="newWindow.close()">
<center>
<font size=+3>
Saved VGL Problems from 
<?php
  $dir = getcwd();
  echo ereg_replace('/', '', strrchr($dir,"/"));
?>
</font>
<br><br>
</center>

<form>
<font size=+2 color=red>Click on one of the buttons below to run your VGL problem:</font><br><br>
<font color=blue><b><u>Notes</b></u>:
<ul>
  <li>Only load one problem at a time.</li>
  <li>If you have loaded more than one problem and the buttons stop working, try re-loading this page.</li>
  <li>You should allow printing when asked or the program will not run.</li>
  <li>The Cages will all appear on top of each other; you will have to drag them off of one another.</li>
  <li>You may have to quit the browser to get all the VGL windows and cages to disappear.</li>
</ul>
</font>
<br>
<b><u>Saved Problems</b></u>:
<ul>
<?php 
  $foundAny = 0;
  $dh = opendir('.');
  while (false != ($fileName = readdir($dh))) {
    if (false != ereg('.wrk', $fileName)) {
      $files[] = $fileName;
      $foundAny = 1;
    }
  }
  closedir($dh);

 if ($foundAny == 1) {
    sort($files);
    $dirName = ereg_replace('Documents/', '', strstr(getcwd(), 'Documents/'));
    foreach ($files As $fileName) {
      $shortName = ereg_replace('.wrk', '', $fileName);
      echo "<li><input type=\"button\" ";
      echo "value=\"$shortName\" onClick=\"showVGL('{$dirName}/{$fileName}', '$shortName')\">";
      $date = date('D F j, Y', filemtime($fileName));
      $daytime = date ('h:i A', filemtime($fileName));
      echo "<font color=green> Saved at <i>$daytime</i> on <i>$date</i></font></li>\n";
    }
  }
?>
</ul>

</form>

</body>
</html>