#!/usr/bin/perl

use DBI;
use CGI;

#user-defined module to connect securely to database
use GradeDB;

$dbh = GradeDB::connect();

$script_url = "https://www.securebio.umb.edu/cgi-bin/iClicker.pl";
$name_of_id_field_in_assignments_txt = "iClicker Number";

if ($ENV{'CONTENT_LENGTH'} == 0) {
    $error_string = "";
    &show_form;
    exit();
}

$query = CGI->new();

$name = $query->param('Name');
$password = $query->param('Passwd');
$number = $query->param('Number');

$statement = "SELECT password FROM students
      WHERE name=\"$name\"";
$sth = $dbh->prepare($statement);
$sth->execute();
@result = $sth->fetchrow_array();
$sth->finish();
$pw = $result[0];
  
if (length($number) != 8) {
    $error_string = "<br><font color=#FF0000><b>Error: iClicker number must be exactly 8
    characters. You entered $number.</b></font><br>";
   	&show_form;
   	exit();
}


if ($number =~ m/[^0-9A-F]/) {
    $error_string = "<br><font color=#FF0000><b>Error: iClicker number can only 
   	       contain 0,1,2,3,4,5,6,7,8,9,A,B,C,D,E, or F. You entered $number.</b></font><br>";
   	&show_form;
   	exit();
}

if(&decrypt_pw($pw,$password) != 1){
    $error_string = "<br><font color=#FF0000><b>Error: Password incorrect 
   	       for $name.</b></font><br>";
   	&show_form;
   	exit();
}

$statement = "SELECT number FROM assignments WHERE name=\"$name_of_id_field_in_assignments_txt\"";
$sth = $dbh->prepare($statement);
$sth->execute();
@result = $sth->fetchrow_array();
$sth->finish();
$index = "grade".$result[0];

$statement = "UPDATE students SET $index = \"$number\" WHERE name=\"$name\""; 
$rows = $dbh->do($statement);
&print_header;

if ($rows != 1) {
    print "Error with database; contact Brian White<br>\n";
    &print_tail;
    exit();
}

print "<br><br><br><br>\n";
print "Thank you $name, ";
print "your iClicker Serial Number was successfully set to $number.<br>\n";
print "<a href=\"http://intro.bio.umb.edu/\">Return to Bio 111 and 112 ";
print "home page</a>.";
&print_tail;

exit();



sub show_form() {
    &print_header;
    print "<form action=\"$script_url\" method=\"POST\">\n";
    print "<h3>Enter your 8-character iClicker Serial Number</h3><br>\n";
    print "This will allow us to match your name and iClicker number<br>\n";
    print "so that we can give you credit for the questions you have answered.<hr>\n";
    
    print "<table border=1>\n";
    print "<tr><td colspan=2>";
    print $error_string;
    print "</td></tr>\n";
    
    print "<tr><td>\n";
    print "<ol>\n";
    
    print "  <li>Choose your name from this list:</li>\n";
    print "<select name=\"Name\" size=12>\n";
    $sth = $dbh->prepare("SELECT * FROM students ORDER BY name");
    $sth->execute();
    while (@result = $sth->fetchrow_array()) {
        print "<option value=\"$result[0]\">$result[0]</option>\n";
    }
    $sth->finish();
    print "</select></li>\n";
    
    print "<li>Enter your 8-digit UMS ID # (leave off the UMS):\n";
    print "    <input type=\"password\" name=\"Passwd\" size=20></li>\n";
    
    print "<li>Enter the Serial Number for your iClicker.\n";
    print "    See instructions on right.\n";
    print "    <input type=\"text\" name=\"Number\" size=10></li>\n";
    
    print "<li>Click this button:\n";
    print "    <input type=\"submit\"></li>\n";

    print "</ol>\n";
    print "</td>\n";
    print "<td>The serial number can be found on the back of your iClicker ";
    print "to the left of the battery cover.<br>";
    print "Note that it is an 8-character sequence using only the digits 0 thru 9 ";
    print "and the letters A thru F.<br>";
    print "For example, the serial number of the iClicker in the photo below ";
    print "is 0066A7C1 - note that the first two digits are zeroes not &quot;oh&quot;s.<br>\n";
    print "Please also use capital letters when entering your serial number ";
    print "(not 0066a7c1, for example).<br>\n";
    print "<img src=\"https://www.securebio.umb.edu/IClikerSerialNumber.jpg\"></td>\n";
    print "</tr></table>\n";
    print "</form>\n";
    &print_tail;
}
 
sub print_header() {
    print "Content-type: text/html\n\n";
    print "<html>\n";
    print "<head>\n";
    print "<title>Enter your iClicker Serial Number</title>\n";
    print "</head>\n";
    print "<body bgcolor=#c0c0c0>\n";
}

sub print_tail() {
    print "</body></html>\n";
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
