#!/usr/bin/perl

use DBI;
use CGI;
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
	
	print "Content-type: text/html\n\n";
	print "<html><head>\n";
	print "<title>Phylogeny Survey for $name</title>\n";
	print "</head>\n";
	print "<body bgcolor = \"lightblue\">\n"; 
	
	$dbh = GradeDB::connect();
	$statement = "SELECT password FROM students
 	     WHERE name=\"$name\"";
	$sth = $dbh->prepare($statement);
	$sth->execute();
	@result = $sth->fetchrow_array();
	$sth->finish();
	$pw = $result[0];

	if(&decrypt_pw($pw,$password) != 1){
  		print "<br><font color=#FF0000><b>Error: Password incorrect 
   		       for $name.</b></font><br>";
   		print "<a href=\"$script_url\">Click here to return to login screen</a>.\n";
   		print "</body></html>\n";
   		exit 1;
	}
	
	print "<center><font size=+2>Phylogeny Survey for $name</font></center><br>\n";
	print "<b>Instructions:</b><br>\n";
	print "<ul>\n";
  	print "<li>Drag Organisms to where you want them</li>\n";
  	print "<li>Shift-click on the workspace to add a node</li>\n";
  	print "<li>Click on an item (organism or node) to select it. It's border will turn red</li>\n";
  	print "<li>When you have two items (organism and/or node) selected, you can link or unlink them</li>\n";
  	print "<li>Links will remain connected when you move items.</li>\n";
  	print "<li>Click &quot;Label&quot; to add a text label.</li>\n";
  	print "<li>Click &quot;Delete&quot; to delete a test label or node.</li>\n";
  	print "<li>Select two objects connected by a link and click &quot;Split&quot; \n";
  	print "to add a new node in the middle of the link.</li>\n";
	print "</ul>\n";
	print "<applet code=\"phylogenySurvey.SurveyApplet.class\" \n";
	print "archive=\"http://www.securebio.umb.edu/phylogenySurvey.jar\" \n";
	print "width=1020 height=1020>\n";
  	print "          You have to enable Java on your machine !</applet>\n";

  
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
