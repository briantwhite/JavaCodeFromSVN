package GradeDB;

use strict;
use DBI;

my $host_name = "localhost";
my $db_name = "grades";
my $dsn = "DBI:mysql:host=$host_name;database=$db_name";

#connext to mysql server, using hardwired name and password

sub connect
{
    return (DBI->connect ($dsn, "grader", "3rade5acce55",
    			  {PrintError => 0, RaiseError => 1}));
}

1;