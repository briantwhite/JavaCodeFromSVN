#!/usr/bin/perl
use CGI;
$query = CGI::new();

$guessAASeq = $query->param('guess');
$targetShape = $query->param('target');

print "content-type: text/html \n\n<html><body>";


open MATCHER, "java -cp Protex.jar protex.ShapeMatcher $guessAASeq \"$targetShape\" |";
while ($line = <MATCHER>) {
  print "$line";
}
close MATCHER;

print "</body></html>\n";

exit 0;
