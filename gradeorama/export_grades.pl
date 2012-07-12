#!/usr/bin/perl
$| = 1;
use DBI;

#user-defined module to connect securely to database
use GradeDB;

$dbh = GradeDB::connect();

$firstline = "Name\tID#\tSection\tTA\t";
$sth = $dbh->prepare("SELECT name FROM assignments ORDER BY number");
$sth->execute();
while (@results = $sth->fetchrow_array()) {
	$firstline .= "$results[0]\t";
}
$sth->finish();
chop $firstline;

print "$firstline\n";
$statement = "SELECT * FROM students ORDER BY name";

$sth = $dbh->prepare($statement);
$sth->execute();
while (@results = $sth->fetchrow_array()) {
	$name = shift @results;
	$id = shift @results;
	$class = shift @results;
	$ins = shift @results;
	$pw = shift @results;
	$line = join("\t", @results);
	print "$name\t$id\t$class\t$ins\t$line\n";
}
$sth->finish();

$dbh->disconnect();
exit();
