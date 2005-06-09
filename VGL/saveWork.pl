#!/usr/bin/perl
use CGI;
$query = CGI::new();

$cryptpw = "11uRs9Gu0knxE";

$password = $query->param('password');
$section = $query->param('section');
$fileName = $query->param('fileName');

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

# nuke all illegal chars
$fileName =~ s/[^A-Za-z0-9_-]//g;

# truncate to 20 chars
$fileName = substr($fileName, 0, 20);

# add the date
$fileName = $fileName . "." . (localtime)[4] . "." . (localtime)[3] .
    "." . (localtime)[2] . "." . (localtime)[1] . "." . (localtime)[0];

# see if the file already exists
if (-e $fileName) {
  print "A file with this name already exists.<br>";
  print "No file saved.";
  exit 1;
}

print $fileName;

exit 1;

