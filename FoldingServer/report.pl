#!/usr/bin/perl
use Time::HiRes qw ( gettimeofday tv_interval );
use CGI;
$query = new CGI;
$sequence = $query->param('seq');
print "Content-type: text/html \n\n";
print "<html><body>\n";
print "Hi there! The sequence was $sequence<br>\n";
$t0 = [gettimeofday];
open (PROG, "orterun --hostfile /pkhome/pkhome/tmp/bhosts pyMPI report.py $sequence |");
while ($line = <PROG>) {
  print "$line<br>\n";
}
close PROG;
$delay = tv_interval($t0);
print "it took $delay seconds<br>\n";
print "</body></html>\n";
exit 1;
