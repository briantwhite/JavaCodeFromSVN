#!/usr/bin/perl
use CGI;
$query = CGI::new();

$cryptpw = "11uRs9Gu0knxE";

$password = $query->param('password');
$section = $query->param('section');
$fileName = $query->param('fileName');
$XMLdata = $query->param('XMLdata');
$HTMLdata = $query->param('HTMLdata');

print "content-type: text/html \n\n<html><body>";

# check password
if (crypt($password, $cryptpw) ne $cryptpw) {
  print "Incorrect Password.<br> No file saved.</body></html>";
  exit 1;
} 

# check to be sure section is legal
if ($section !~ m/^Section[0-9]{1,2}$/) {
  print "Incorrect Section: $section.";
  exit 1;
}

#process the filename

# turn spaces to underscores
$fileName =~ s/ /_/g;

# nuke all illegal chars
$fileName =~ s/[^A-Za-z0-9_-]//g;

# truncate to 25 chars
$fileName = substr($fileName, 0, 25);

# add 3 random digits to make it unique
srand;
$fileName = $fileName . (int(rand(1000) + 1));

# see if the file already exists
if (-e $fileName) {
  print "A file with this name already exists.<br>";
  print "No file saved.";
  exit 1;
}

# see if the xml is OK
#  first line contains "xml"
#  second line starts with "<Vgl>"
#  only 2 lines long
@XMLParts = split(/^/m, $XMLdata);
if (($XMLParts[0] !~ m/xml/) 
    || ($XMLParts[1] !~ m/^<Vgl>/)
    || (scalar(@XMLParts) != 2)) {
  print "The work file is not in the correct format.<br>\n";
  print "No file saved.\n";
  exit 1;
}

# see if the html is OK
#  first line starts with <html
@HTMLParts = split(/^/m, $HTMLdata);
if ($HTMLParts[0] !~ m/<html>/) {
  print "The html file is not in the correct format.</br>\n";
  print "No file saved.\n";
  exit 1;
}

open XML_FILE, ">/Library/WebServer/Documents/VGLProblems/$section/$fileName.wrk"
  or die ("Could not open $section/$fileName.wrk");
  print XML_FILE $XMLdata;
close XML_FILE;

open HTML_FILE, ">/Library/WebServer/Documents/VGLProblems/$section/$fileName.html"
  or die ("Could not open $section/$fileName.html");
  print HTML_FILE $HTMLdata;
close HTML_FILE;

chmod 0400, "/Library/WebServer/Documents/VGLProblems/$section/$fileName.wrk";
chmod 0400, "/Library/WebServer/Documents/VGLProblems/$section/$fileName.html";

print "This problem was saved on the server.<br>\n";
print "It can be found on the web page for $section.<br>\n";
print "It is called <font color=blue>$fileName</font>.<br>";
print "<font color=red>Please make a note of this name.</font><br>";

exit 1;

