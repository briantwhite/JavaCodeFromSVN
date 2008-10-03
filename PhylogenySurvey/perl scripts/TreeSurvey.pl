#!/usr/bin/perl

use DBI;
use GradeDB;
#use TreeDB;

$script_url = "https://www.securebio.umb.edu/cgi-bin/TreeSurvey.pl";

# if we enter without params, give the student login page
#  otherwise, process survey data
if ($ENV{'CONTENT_LENGTH'} == 0) {
	&login_page;
} else {
	&load_survey;
}

exit 1;

#------------------

sub login_page {
	print "Content-type: text/html\n\n";
	print "<html><head>\n";
	print "<title>Login to the Phylogeny Survey</title>\n";
	print "</head>\n";
	print "<body bgcolor = \"yellow\">\n";
	
	print "<form action=\"$script_url\" method=\"POST\">\n";

	$dbh = GradeDB::connect();
	
	print "<font size=+3>Login to the phylogeny survey site</font><br>\n";
	print "Choose your name from this list:<br>\n";
    print "<select name=\"Name\" size=12>\n";
	$sth = $dbh->prepare("SELECT * FROM students ORDER BY name");
    $sth->execute();
    while (@result = $sth->fetchrow_array()) {
        print "<option value=\"$result[0]\">$result[0]</option>\n";
    }
    $sth->finish();
    print "</select><br>\n";
    print "Enter your 8-digit UMS ID # (leave off the UMS):\n";
    print "<input type=\"password\" name=\"Passwd\" size=20><br>\n";
    print "  <input type=\"submit\" value=\"Login\">\n";
    
	print "</form>\n";
	
	print "</body></html>\n";
}

sub load_survey {
	
	$query = CGI->new();

	$name = $query->param('Name');
	$password = $query->param('Passwd');

	$statement = "SELECT password FROM students WHERE name=\"$name\"";
	$sth = $dbh->prepare($statement);
	$sth->execute();
	@result = $sth->fetchrow_array();
	$sth->finish();
	$pw = $result[0];
	
	print "Content-type: text/html\n\n";
	print "<html><head>\n";
	print "<title>Phylogeny Survey for $name</title>\n";
	print "</head>\n";
	print "<body bgcolor = \"blue\">\n"; 
	
	print "$name<br> $password<br></body></html>\n";
  
}

sub decrypt_pw {

	my $cryptpw = $_[0];
	my $pw = $_[1];
	my $isok = 0;
	if(crypt($pw, time.$$) eq $cryptpw && $pw ne "" && $cryptpw ne ""){
		$isok = 1;
	}
	$isok;
}
