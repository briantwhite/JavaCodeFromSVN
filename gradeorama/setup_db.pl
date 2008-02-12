#!/usr/bin/perl
use DBI;

#user-defined module to connect securely to database
use GradeDB;

$dbh = GradeDB::connect();

# set up the assignments DB first
$sth = $dbh->prepare("DROP TABLE  IF EXISTS assignments");
$sth->execute();

$sth = $dbh->prepare("CREATE TABLE assignments 
                      (
                           number INT PRIMARY KEY,
                           name VARCHAR(30)
                      )");
$sth->execute();

$statement = "INSERT into assignments VALUES ";
# assignments starting from 0;
$number = 0;

open FILE, "<assignments.txt";
  while ($line = <FILE>) {
    chomp $line;
    $statement = $statement."(\"$number\", \"$line\"),";
    $assignments[$number] = $line;
    $number++;
  }
close FILE;
chop $statement;

$sth = $dbh->prepare($statement);
$sth->execute();


# now do the instructors file
$sth = $dbh->prepare("DROP TABLE  IF EXISTS instructors");
$sth->execute();

$sth = $dbh->prepare("CREATE TABLE instructors 
                      (
                           name VARCHAR(30) PRIMARY KEY,
                           password VARCHAR(30),
                           class_list VARCHAR(50)
                      )");
$sth->execute();

$statement = "INSERT into instructors VALUES ";

open FILE, "<instructors.txt";
  @lines = grep(!/^#/, <FILE>);  # leave out comments
close FILE;

foreach $line (@lines) {
  chomp $line;
  @parts = split(/\|/, $line);
  $cryptpw = crypt($parts[1], time.$$);
  $statement = $statement."(\"$parts[0]\",\"$cryptpw\",\"$parts[2]\"),";
}
chop $statement;

$sth = $dbh->prepare($statement);
$sth->execute();

# finally, the big grades table "students"
$sth = $dbh->prepare("DROP TABLE IF EXISTS students");
$sth->execute();

$statement = "CREATE TABLE students 
              (
                  name VARCHAR(30) PRIMARY KEY,
                  section VARCHAR(20),
                  TA  VARCHAR(20),
                  password VARCHAR(30),\n";

for ($i = 0; $i < $number; $i++) {
  $statement .= "grade$i VARCHAR(10),\n";
}
chop $statement;
chop $statement;
$statement = $statement."\n)";

$sth = $dbh->prepare($statement);
$sth->execute();


open FILE, "<students.txt";
  @lines = grep(!/^#/, <FILE>);
close FILE;

$statement = "INSERT INTO students VALUES ";
foreach $line (@lines) {
  chomp $line;
  @parts = split(/\|/, $line);
  $cryptpw = crypt($parts[3], time.$$);
  $statement .= "(\"$parts[2]\",\"$parts[0]\",\"$parts[1]\",\"$cryptpw\",";
  for ($i = 0; $i < $number; $i++) {
    $statement .= "\"\",";
  }
  chop $statement;
  $statement .= "),";
}

chop $statement;

$sth = $dbh->prepare($statement);
$sth->execute();

$dbh->disconnect();

# set up the log file
open LOG, ">grade.log" or die ("couldn't open log file");
  $now = localtime(time);
  print LOG "Databases created $now\n";
close LOG;

exit;
