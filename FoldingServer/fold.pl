#!/usr/bin/perl
use Time::HiRes qw ( gettimeofday tv_interval );
use CGI;
$query = new CGI;
$sequence = $query->param('seq');
@sequences = split( /,/, $sequence);
$sequence_list = join ' ', @sequences;
print "Content-type: text/html \n\n";
print "<html><body>\n";
print "Hi there! There were " . @sequences . " sequences submitted<br>\n";
$t0 = [gettimeofday];
open (PROG, "orterun --hostfile /pkhome/pkhome/tmp/bhosts pyMPI fold.py $sequence_list |");
while ($line = <PROG>) {
  print "$line<br>\n";
}
close PROG;
$delay = tv_interval($t0);
print "it took $delay seconds<br>\n";
print "</body></html>\n";
exit 1;
