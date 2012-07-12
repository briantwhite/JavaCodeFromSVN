#!/usr/bin/perl
$| = 1;
use DBI;

#user-defined module to connect securely to database
use GradeDB;

$dbh = GradeDB::connect();
 

#########################################################################
# GradeORama v2.0   - BW						#
#									#
# A web-based gradebook server.						#
# now with mysql database						#
# Copyright 2005, Kristina L. Pfaff-Harris (http://tesol.net/scripts/)	#
# All rights reserved.                                             	#
# Modified by Brian White (brian.white@umb.edu)				#
#########################################################################

# Next, $admlogin is a login you'd like to use for the overall 
# "super administrator" of this program.  When you login to the admin
# interface with this login, you'll be able to add/edit/and delete
# instructors, as well as work with gradebooks, classes, and students.
# (Normal instructors who you add cannot add other instructors.)
# **CHANGE** this to an appropriate login. It can be pretty much
# anything you like, but must be between single quotes as I have
# below.

$admlogin = 'brian'; 

# $initial_password is a password you'd like to use when you first
# login to the program. The way it works is this: the first time you
# start up a brand new installation of GradeORama, the program will
# set the password for "$admlogin" above to what you put here.  You
# should immediately log in and change it to something else so that
# it will be encrypted.  Once you change it, this initial password
# won't work anymore -- it's only for the first time you run the
# program.  **CHANGE** this to an appropriate password.

$initial_password = "top33dog";
 

# Now, in order to run the web interface, GradeORama needs to know
# where it can find itself on the web. $cgi_url is the full web address
# (URL) to your gradeorama.cgi program. **CHANGE** this to the web address
# of where you've put the gradeorama.cgi program. NOTE: this should
# probably begin with "http://".

$cgi_url = "https://www.securebio.umb.edu/cgi-bin/gradeorama.cgi";

# You can choose the size of the
# popup window. **CHANGE** $grade_window_width to the number of pixels
# wide you want the window to be. **CHANGE** $grade_window_height to
# the number of pixels tall you want the window to be.

$grade_window_width = 800;

$grade_window_height = 600;

$header = qq[
<!-- START HTML HEADER -->
<html> <head>
           <META HTTP-EQUIV=\"PRAGMA\" CONTENT=\"NO-CACHE\">
	   <title>Grades For Bio 111</title></head>
           <body bgcolor=\"#FFFFFF\">
           <h3><font color=green>Grades For Bio 111</h3></font>
          <P>
<!-- END HTML HEADER -->
];

# $footer is pretty much the same deal as $header above: this HTML
# is at the end of all pages generated by the program.
# Put any HTML you like in between
# <!-- START HTML FOOTER -->
# and
# <!-- END HTML FOOTER -->

$footer = qq[
<!-- START HTML FOOTER -->
<p> </td> </tr> </table></font><hr>
	   </body></html>
<!-- END HTML FOOTER -->
];

%data = &get_data;

if($data{'FA'} !~ /Export/){
	print "Content-type: text/html\n\n" if $ct != 1;
 	}
$admformtop = "<hr><form method=POST action=\"$cgi_url\">
               <input type=hidden name=admlogin 
                 value=\"$data{'admlogin'}\">
               <input type=hidden name=admpasswd 
                 value=\"$data{'admpasswd'}\">
	       ";

# If you remove this link, without replacing it with a similar
# HTML comment (as above in the License section) you are in violation
# of the license for this program. No, I don't have the resources to
# enforce this, and I know that some people will remove it anyway.
# But honestly, since you didn't have to pay for this, is the link
# really too much to ask? :)

$footer = "<hr><font size=-2><a href=\"http://www.tesol.net/scripts/\">
	  Powered by 
          GradeORama v1.0.4 Online Gradebook Server</a> 
          <br>
          Modified by <a href=\"http://intro.bio.umb.edu/BW/\">
          Brian White</a>$footer";

# First, if this is a new install, check the initial password
# and make sure it's encrypted.

&initial_pw_setup;

$data{'FA'} =~ s/^\s+//g;
$data{'FA'} =~ s/\s+$//g;

if($data{'FA'} eq "admin" || $data{'FA'} eq "Log Out"){
  $data{'admlogin'} = ""; $data{'admpasswd'} = "";
  &admin_login_screen;
} elsif($data{'FA'} eq "Student Login"){
  &check_password_student;
  &show_grades;
} elsif($data{'FA'} ne ""){
  $close_browser = " onClick=\"self.close();\""; 
  &check_password_admin;
  $st = " style=\"width: 10em;\"";
  if($data{'admlogin'} eq $admlogin){
	$admextras = "<tr><td align=center bgcolor=#C0C0C0>
	<input type=submit name=FA value=\"Edit Instructor\"$st></td>
        </tr>
    ";
  }
  $admin_footer = "$admformtop
	<table border=1>
	$admextras
        <tr>
	<td align=center bgcolor=#C0C0C0>
	<input type=submit name=FA value=\"   Edit Gradebooks   \"$st>
        </td>
	<td align=center bgcolor=#C0C0C0>
	<input type=submit name=FA value=\"Add Students\"$st></td>
	<td align=center bgcolor=#C0C0C0>
	<input type=submit name=FA value=\" Edit Student \"$st></td></tr>
	<tr><td align=center colspan=3><input type=submit name=FA 
            value=\" Export Grades to Spreadsheet or ASCII File \" 
            style=\"width: 31em\"></td></tr>
	<tr><td align=center colspan=3 bgcolor=#C0C0C0>
	<input type=submit name=FA$close_browser 
            value=\"    Log Out     \" style=\"width: 31em\"></td></tr>
	</table>
	</form>";

  if($data{'FA'} eq "Export Grades to Spreadsheet or ASCII File" ||
    $data{'FA'} eq "Export Grades"){
    &export_grades;
  }
  if($data{'FA'} eq "Edit Instructor"){
    &edit_instructor;
  } elsif($data{'FA'} eq "Edit Gradebooks" ||
      $data{'FA'} eq "Finished Adding Students"){
    &edit_gradebook;
  } elsif($data{'FA'} eq "Update Gradebook"){
    &update_gradebook;
  } elsif($data{'FA'} eq "Add Students"){
    &add_students_form;
  } elsif($data{'FA'} eq "Delete Student"){
    &delete_student;
  } elsif($data{'FA'} eq "Edit Student"){
    &edit_student;
  } elsif($data{'FA'} eq "Add This Student"){
    &add_student_to_system;
  } else {
   print "$header
	 <b>Welcome $data{'admlogin'}.</b>  <br>\n";	 
   print "<b>Please choose from one
	 of the following options:</b><br>
	 $admin_footer <hr>";
	 
      &show_class_info;

   print "$footer";
   
   $dbh->disconnect();
   exit;
  }
} else {
  &default_page;
}

#---- end of main loop-----#

sub default_page {
  $javascript = "
  <script language=\"JavaScript\">
  
  function gradeorama_popup () {

   var vurl_string = '$cgi_url?Login=';
     vurl_string += escape(document.GradeORama.Login.value);
     vurl_string += '&Password=';
     vurl_string += escape(document.GradeORama.Password.value);
     vurl_string += '&FA=Student%20Login';
     window.open(vurl_string,'GradeORama','width=$grade_window_width,height=$grade_window_height,left=0,top=0,toolbar=no,scrollbars=yes,resizable=yes,status=no,directories=no,menubar=no,location=no');
  return false;
  }
  </script>
  ";
  $form_name = " name=\"GradeORama\" ";
  $submit_button = "<script language=\"JavaScript\">\ndocument.write('<input type=\"submit\" onClick=\"return gradeorama_popup();\" value=\"Log in\">');\n</script>\n";
	
  print "$header
	$javascript
	$error
	<form method=POST$form_name action=\"$cgi_url\">
        <b>Use this page to access your grades so far.</b><br><br>
        <ol>
	  <li><b>Select your Name from this list:</b>\n";

  print "<select name=\"Login\" size=12>\n";
  print "<option>--Choose your name from this list---</option>\n";
  $sth = $dbh->prepare("SELECT * FROM students ORDER BY name");
  $sth->execute();
  while (@result = $sth->fetchrow_array()) {
    print "<option value=\"$result[0]\">$result[0]</option>\n";
  }
  $sth->finish();
  print "</select></li>\n";
  if($js_new_window == 1){
    $admin_link = "<a href=\"\" 
          onClick=\"window.open('$cgi_url?FA=admin','GradeORama');
          return false;\">Gradebook Administration</a>";
  } else { 
	$admin_link = "<a href=$cgi_url?FA=admin>Gradebook Administration</a>";
  }

  print "<li><b>Enter your Password (Your UMS ID#):</b>
	<td><input type=password name=Password value=\"$data{'Password'}\"></li>
	<input type=hidden name=FA value=\"Student Login\">
        <li><b>Click:</b> $submit_button &nbsp; &nbsp; $logout_button</li>
        </ol>
	</form>
	<font size=-2>$admin_link</font>
	$footer";
  $dbh->disconnect();
  exit();
}

sub add_student_to_system {

  if($data{'Student'} eq "" || $data{'StudentPw'} eq "" || 
     $data{'StudentPw2'} eq "" || $data{'Class'} eq ""){
    $message = "Error: insufficient information. Please enter a student login
	      ID, password (twice!), and select a class for the student.<br>
	      <br>";
	&add_students_form($message);
    $dbh->disconnect();
    exit();
  }
  
  if($data{'StudentPw'} ne $data{'StudentPw2'}){
    $message = "Error: Student passwords do not match.<br><br>";
    &add_students_form($message);
    $dbh->disconnect();
    exit();
  }

  # fill in the empty grades
  $gline = "";
  $sth = $dbh->prepare("SELECT * FROM assignments ORDER BY number");
  $sth->execute();
  while (@parts = $sth->fetchrow_array()) {
    $gline .= "NULL,";
  }
  chop $gline;

  #get the instructor for this class
  $class = $data{'Class'};
  $statement = "SELECT name FROM instructors
                          WHERE class_list LIKE \"%$class%\"";
  $sth = $dbh->prepare($statement);
  $sth->execute();
  @result = $sth->fetchrow_array();
  $sth->finish();
  $ins = $result[0];

  $cryptpw = &encrypt_pw($data{'StudentPw'});

  $statement = "INSERT INTO students VALUES (
                        \"$data{'Student'}\",
                        \"$data{'StudentPw'}\",
                        \"$class\",
                        \"$ins\",
                        \"$cryptpw\",
                        $gline)";
  $rows = $dbh->do($statement);

  $message = "$data{'Student'} added to $data{'Class'}";

  &add_students_form($message);
  $dbh->disconnect();
  exit();
}


sub edit_student {

  if($data{'FA2'} eq "Edit"){
    ($stu, $cls) = split(/\|/, $data{'Student'});
    print "$header
	      $admformtop
	      $error
	      <b>Editing info for $stu in $cls</b><br><br>\n";

    $sth = $dbh->prepare("SELECT class_list FROM instructors");
    $sth->execute();
    while (@results = $sth->fetchrow_array()) {
      $classSet = $results[0];
      push @classes, split(/\%/, $classSet); 
    } 

     print "Choose a new lab section for $stu:\n";
     print "<select name=\"Class\">\n";
     
     foreach $class (sort @classes) {
	   print "<option value=\"$class\" ";
       if ($class eq $cls) {
         print "SELECTED";
       }
       print ">$class</option>\n";
     }
     
     print "</select>
	       <input type=hidden name=FA value=\"Edit Student\">
	       <input type=hidden name=FA2 value=\"Confirm\">
	       <input type=hidden name=Student value=\"$stu\">
	       <input type=submit value=\"Make Changes\">
	       </td></tr></table></form>
	       $footer";
      $dbh->disconnect();
      exit();
  } elsif($data{'FA2'} eq "Confirm"){
    $dstu = $data{'Student'};
    $dcls = $data{'Class'};

    # get the instructor for the new class
    $statement = "SELECT name FROM instructors
                          WHERE class_list LIKE \"%$dcls%\"";
    $sth = $dbh->prepare($statement);
    $sth->execute();
    @result = $sth->fetchrow_array();
    $sth->finish();
    $ins = $result[0];

    $statement = "UPDATE students SET section=\"$dcls\",
                        TA=\"$ins\" 
                        WHERE name=\"$dstu\"";
    $rows = $dbh->do($statement);
     
    print "$header
	    <b>Section for $dstu changed: 
            New Section: $dcls with $ins</b><br>
	    <b>Note: Do not use your browser's \"Back\" button to
	    go to the previous screen. Use the \"Edit Student\" 
	    function instead.</b>
	    $footer";
    $dbh->disconnect();
    exit();
  } else {
    print "$header $admformtop
	  $error
	  <b>Please select a student to edit:</b>
	  <select name=Student>";
    $sth = $dbh->prepare("SELECT name, section FROM students
                           ORDER BY name");
    $sth->execute();
    while (@results = $sth->fetchrow_array()) {
      print "<option value=\"$results[0]|$results[1]\">
                $results[0] ($results[1])</option>\n";
    }
    print "</select>
	 <input type=hidden name=FA value=\"Edit Student\">
	 <input type=hidden name=FA2 value=\"Edit\">
	 <input type=submit value=\"Edit Student\">
	 </form>
	 $footer";
    $dbh->disconnect();
    exit();
  }
}


sub add_students_form {
  $sth = $dbh->prepare("SELECT class_list FROM instructors");
  $sth->execute();
  while (@results = $sth->fetchrow_array()) {
    $classSet = $results[0];
    push @classes, split(/\%/, $classSet); 
  } 
 
 $message = $_[0] unless ($_[0] eq "FA");
 print "$header
	$admformtop
	<b>$message</b>
	<table border=1>
	<tr><th>Student Info</th><th>Add to Class(es)</th></tr>
	<tr><td valign=top>
        <table border=0>
	<tr><td>Student Name:</td>
	<td><input type=text name=Student value=\"$data{'Student'}\"></td>
	</tr>
	<tr><td>Student Password:</td>
	<td><input type=password name=StudentPw 
	     value=\"$data{'StudentPw'}\"></td>
	</tr>
	<tr><td>Student Password again:</td>
	<td><input type=password name=StudentPw2 
	    value=\"$data{'StudentPw2'}\"></td>
	</tr></table>
	</td><td valign=top>
        <select name=\"Class\">\n";
 @classes = sort @classes;
 foreach $class (@classes){

   foreach $dclass (split(/\0/, $data{'Class'})){
     if($class eq "$dclass" && $dclass ne ""){
        $ck = " SELECTED";
     }
   }
   print "<option value=\"$class\"$ck> $class</option>\n"
    if $class ne "";
    $ck = "";
 }
   print "</select>
        </td></tr></table>
	 <input type=submit name=FA value=\"Add This Student\">
	 <input type=submit name=FA value=\"Finished Adding Students\">
	 </form>$footer";
   $dbh->disconnect();
   exit();
}


sub edit_gradebook {
  if($data{'FA2'} eq "Show Class"){

  print "$header
	  <font color=#FF0000><b>$message</b></font><br>
	  <b>Gradebook for $data{'Class'}</b><br>
          You may change the grades for any student at any time.  
	  When you are finished, click
	  \"Update Gradebook\" to save your changes.<br><br>
	  $admformtop <table border=1>\n";

  #do the top row of the table
  print "<tr>\n";
  print "<th><font color=blue>Name</font></th><th>Section</th><th>TA</th>\n";
  $sth = $dbh->prepare("SELECT name FROM assignments
                              ORDER BY number");
  $num_assignments = $sth->execute();
    
  $colCount = 0;
  while (@results = $sth->fetchrow_array()) {
    if ((($colCount/10) == int($colCount/10)) && ($colCount != 0)) {
      print "<th><font color=blue>Name</font></th><th><font color=blue>ID#</font></th>";
    }
    $colCount++; 

    print "<th>$results[0]</th>";
  }
  print "</tr>\n";
  $sth->finish();

  if (($data{'Class'} eq "All") && ($data{'admlogin'} eq $admlogin)) {
    $statement = "SELECT * FROM students ORDER BY name";
  } else {
    $statement = "SELECT * FROM students 
        WHERE section=\"$data{'Class'}\" ORDER BY name";
  }
  $sth = $dbh->prepare($statement);
  $num_students = $sth->execute();

  $base_tab = 0;

  while (@results = $sth->fetchrow_array()) {
    $base_tab++;
    $i = 0;

    $name = shift @results;
    $id = shift @ results;
    $class = shift @results;
    $ins = shift @results;
    $pw = shift @results;

    print "<tr><td><font color=blue>$name</font></td><td><font color=blue>$id</font></td><td>$class</td><td>$ins</td>";

    $colCount = 0;
    foreach $grade (@results){

      $taborder = ($colCount * $num_students) + $base_tab;

      if ((($colCount/10) == int($colCount/10)) && ($colCount != 0)) {
        print "<td><font color=blue>$name</font></td>";
      }
      $colCount++; 
       
      print "<td align=center><input type=text name=\"$name$i\" value=\"$grade\" size=3";
      print " tabindex=$taborder></td>\n";
      $i++;
      }
     print "</tr>\n";
   }
   $sth->finish();
   print "</table>
	  <input type=hidden name=Class value=\"$data{'Class'}\">
	  <input type=hidden name=numgrades value=\"$num_assignments\">
	  <table border=0>
	  <tr><td><input type=submit name=FA value=\"Update Gradebook\"></td>
	  <td><input type=submit name=FA value=\"Finished with Gradebook\"></td>
	  </tr></table>
	  </form>$footer";
  $dbh->disconnect();
  exit(); 
 }

  # get the classes for this instructor
  #see if administrator - if so, see all classes

  my @classes;

  if ($data{'admlogin'} eq $admlogin) {
    $statement = "SELECT class_list FROM instructors";
  } else {
    $statement = "SELECT class_list FROM instructors 
                   WHERE name=\"$data{'admlogin'}\"";
  }
  $sth = $dbh->prepare($statement);
  $sth->execute();
  while (@results = $sth->fetchrow_array()) {
    push @classes, split(/\%/, $results[0]);
  }
 
  print "$header
	<hr>
	$admformtop
	<b>Work with grades from:</b>
	<select name=Class>";
  foreach $class (sort(@classes)){
    chomp($class);
    print "<option value=\"$class\">$class</option>\n" if $class ne "";
  }
  if ($data{'admlogin'} eq $admlogin) {
     print "<option value=\"All\">All Sections</option>\n";
  }
  print "</select>
	  <input type=submit value=\"Go\">
	  <input type=hidden name=FA value=\"Edit Gradebooks\">
	  <input type=hidden name=FA2 value=\"Show Class\">
	  </form>
	  $footer";
}

sub update_gradebook {
  # Update students
  # Student grades come in as lisa0, lisa1, etc.
  # $numgrades is the number of grades we have.

  #first, get a list of students in this section
  if (($data{'Class'} eq "All") && ($data{'admlogin'} eq $admlogin)) {
    $statement = "SELECT name FROM students
                        ORDER BY name";
  } else {
    $statement = "SELECT name FROM students 
       WHERE section=\"$data{'Class'}\" ORDER BY name";
  }
  $sth = $dbh->prepare($statement);
  $sth->execute();
  my @students;
  while (@results = $sth->fetchrow_array()) {
    push @students, $results[0];
  }

  open (LOG, ">>/usr/local/gradeorama/grade.log");
  $now = localtime(time);
  $host = $ENV{'REMOTE_ADDR'};

  foreach $student (@students){ 
    $grade_line = "";
    for ($i = 0; $i < $data{'numgrades'}; $i++){
      $test = "$student$i";
      $grade_line .= "grade$i=\"$data{$test}\", ";
    }
    chop $grade_line;
    chop $grade_line;
    $statement = "UPDATE students SET $grade_line 
                     WHERE name=\"$student\"";
    $rows = $dbh->do($statement);
    print LOG "$now,$host,$data{'Class'},$student\n";
  }
  close LOG;
 
  $message = "<b>$data{'Class'} grades updated.</b><br>";
  $data{'FA2'} = "Show Class";
  &edit_gradebook;
  $dbh->disconnect();
  exit();
}

sub edit_instructor {

  if($data{'FA2'} eq "Edit"){
    print "$header
	 $admformtop
	   <font color=#FF0000>$error</font><br>
	   <table border=0>
	   <tr><th align=left>
	   Editing Password for:
	   </th><th>
	   $data{'Name'}
	   </th></tr>
	   <tr><th align=left>
	   New Instructor Password:
	   </th><th>
	   <input type=password name=Password 
             value=\"$data{'Password'}\">
	   </th></tr>
	   <tr><th align=left>
	   New Instructor Password again:
	   </th><th>
	   <input type=password name=Password2
             value=\"$data{'Password2'}\">
	   </th></tr>
	   </table>
	   <input type=hidden name=FA2 value=\"Really Edit\">
           <input type=hidden name=Name value=\"$data{Name}\">
	   <input type=submit name=FA value=\"Edit Instructor\">
           </form>$footer";
  } elsif ($data{'FA2'} eq "Really Edit"){
    # Do the edit
    if(($data{'Password'} ne $data{'Password2'}) ||
         $data{'Password'} eq "" || $data{'Password2'} eq ""){
        $error = "Error: Passwords do not match or are blank.";
        $data{'FA2'} = "Edit";
        &edit_instructor;
     }
	
  $data{'Name'} = &unpipe($data{'Name'});
  $data{'Password'} = &unpipe($data{'Password'});

  $cryptpw = &encrypt_pw($data{'Password'});
  $statement = "UPDATE instructors SET password=\"$cryptpw\" 
                 WHERE name=\"$data{'Name'}\"";
  $rows = $dbh->do($statement);

  print "$header
	  <b>Instructor $data{'Name'} successfully updated.</b>
	  $footer";

 } else {
  #get list of instructors
  $sth = $dbh->prepare("SELECT name FROM instructors ORDER BY name");
  $sth->execute();
  while (@results = $sth->fetchrow_array()) {
    $instructors .= "<option value=\"$results[0]\">$results[0]</option>\n";
  }
  $sth->finish();

  print "$header
       $admformtop
       <select name=Name>$instructors</select>
       <input type=hidden name=FA2 value=\"Edit\">
       <input type=submit name=FA value=\"Edit Instructor\">
       </form>
       $footer";

 }
 $dbh->disconnect();
 exit();
}


sub get_data {
    local($string);

    # get data
    if ($ENV{'REQUEST_METHOD'} eq 'GET') {
        $string = $ENV{'QUERY_STRING'};
    }				
    else { read(STDIN, $string, $ENV{'CONTENT_LENGTH'}); }

    # split data into name=value pairs
    @data = split(/&/, $string);
   
    # split into name=value pairs in associative array
    foreach (@data) {
	split(/=/, $_);
	$_[0] =~ s/\+/ /g; # plus to space
	$_[0] =~ s/%00//g; # We don' need no steenking nulls :)
        $_[0] =~ s/%0a/newline/g;
	$_[0] =~ s/%(..)/pack("c", hex($1))/ge; # hex to alphanumeric
	if(defined($data{$_[0]})){ 
	   $data{$_[0]} .= "\0";
	   $data{$_[0]} .= "$_[1]";
	   }
	else {
	$data{"$_[0]"} = $_[1];
	  }
    }
    # translate special characters
    foreach (keys %data) {
	$data{"$_"} =~ s/\+/ /g; # plus to space
	$data{"$_"} =~ s/%00//g; # We don' need no steenking nulls :)
        $data{"$_"} =~ s/%0a/newline/g;
	$data{"$_"} =~ s/%(..)/pack("c", hex($1))/ge; # hex to alphanumeric
    }

    %data;			# return associative array of name=value
}

sub check_password_admin {

  $statement = "SELECT password FROM instructors 
                  WHERE name=\"$data{'admlogin'}\"";
  $sth = $dbh->prepare($statement);
  $sth->execute();
  @result = $sth->fetchrow_array();
  $sth->finish();
  $pw = $result[0];

  if(&decrypt_pw($pw,$data{'admpasswd'}) != 1 ||
     $data{'admpasswd'} eq ""){
     $error = "<br><font color=#FF0000><b>Error: Password incorrect 
	       for $data{'admlogin'}.</b></font><br>";
     &admin_login_screen;
     }

}

sub check_password_student {

  $statement = "SELECT password FROM students
      WHERE name=\"$data{'Login'}\"";
  $sth = $dbh->prepare($statement);
  $sth->execute();
  @result = $sth->fetchrow_array();
  $sth->finish();
  $pw = $result[0];
  
  $passwordEntered = $data{'Password'};
  $passwordEntered =~ s/[^0-9]//g; 

  if(&decrypt_pw($pw,$passwordEntered) != 1){
    $error = "<br><font color=#FF0000><b>Error: Password incorrect 
   	       for $data{'Login'}.</b></font><br>";
    $logout_button = "<input type=\"button\" onClick=\"self.close();\" value=\"Close Window\">";
    &default_page;
  }
}


sub show_grades {

  $logout_button = "<input type=button onClick=\"self.close()\" 
    value=\"Log Out\">";

  #get the assignment names
  $sth = $dbh->prepare("SELECT name FROM assignments
                           ORDER BY number");
  $sth->execute();
  my @assignments;
  while (@results = $sth->fetchrow_array()) {
    push @assignments, $results[0];
  }
  $sth->finish();
 
  #get the grades
  $statement = "SELECT * FROM students 
                  WHERE name=\"$data{'Login'}\"";
  $sth = $dbh->prepare($statement);
  $sth->execute();
  @results = $sth->fetchrow_array();
  $sth->finish();

  # drop off the name, ID#, section, TA, and password
  $name = shift @results;
  $id = shift @results;
  $class = shift @results;
  $ins = shift @results;
  $pw = shift @results;
  print "$header
	<b>Welcome, $name! Here are your Grades for $class with TA
          $ins:</b><br>$logout_button<br>
	<table border=1><tr><th><b>Assignment (Max points)</b></th>
	<th>Grade</th></tr>\n";
  for ($i = 0; $i <= $#assignments; $i++){
    if ($results[$i] eq "") {
      $results[$i] = "&nbsp;";
    }
    print "<tr><td>$assignments[$i]</td>
               <td align=center>$results[$i]</td></tr>\n";
  }
  print "</table>$logout_button$footer";
  $dbh->disconnect();
  exit();
}

sub admin_login_screen {

print "$header
       $error
       <form method=POST action=$cgi_url>
       <b>Please log in for Gradebook Administration:</b>
       <table border=0>
       <tr><td><b>Login/ID:</b></td>
       <td><input type=text name=admlogin value=\"$data{'admlogin'}\"></td>
       </tr>
       <tr><td><b>Password:</b></td>
       <td><input type=password name=admpasswd value=\"$data{'admpasswd'}\"></td>
       </tr>
       <input type=hidden name=FA value=\"Admin Login\">
       <tr><td><input type=submit value=\"Log In\"></td><td></td></tr>
       </table></form>
       <font size=2><a href=$cgi_url>Main Gradebook Login</a></font>
       $footer";
exit();

}

sub encrypt_pw {

  my $cryptpw = $_[0];
  $cryptpw = crypt($cryptpw, time.$$);
  $cryptpw;

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

sub initial_pw_setup {

# Okey doke. If there's nothing in the instructors file, we'll
# set it up with the password they specified in "initial password".
  $statement = "SELECT password FROM instructors 
                  WHERE name=\"$admlogin\"";
  $sth = $dbh->prepare($statement);
  $sth->execute();
  @result = $sth->fetchrow_array();
  $sth->finish();
  $test = $result[0];

  if($test eq ""){
    $initialCryptPW = crypt($initial_password, time.$$);
    $statement = "UPDATE instructors SET password=\"$initialCryptPW\"
                    WHERE name=\"$admlogin\"";
    $rows = $dbh->do($statement);

    }

}

sub unpipe {

 my($stuff) = $_[0];
 # Just a quick routine to get rid of things that could hurt
 # our program...
 $stuff =~ s/\%/&#37;/g;
 $stuff =~ s/\|/&#124;/g;
 $stuff =~ s/\"/&quot;/g;
 $stuff =~ s/</&lt;/g;
 $stuff =~ s/>/&gt;/g;

 $stuff;

}

sub repipe {

 my($stuff) = $_[0];
# Just a quick routine to put things back to normal for mail
 $stuff =~ s/&#37;/\%/g;
 $stuff =~ s/&#166;/\|/g;
 $stuff =~ s/&quot;/\"/g;
 $stuff =~ s/&lt;/</g;
 $stuff =~ s/&gt;/>/g;

 $stuff;

}

sub export_grades {
  if($data{'Confirm'} ne "Yes"){
  print "Content-type: text/html\n\n";
  print "$header
	<form method=POST$form_name action=$cgi_url>
	<table border=0>
	<tr><td><b>Export Grades from which Class:</b></td>
	<td><select name=Class>";
  my @classes;

  if ($data{'admlogin'} eq $admlogin) {
    $statement = "SELECT class_list FROM instructors";
  } else {
    $statement = "SELECT class_list FROM instructors 
                   WHERE name=\"$data{'admlogin'}\"";
  }
  $sth = $dbh->prepare($statement);
  $sth->execute();
  while (@results = $sth->fetchrow_array()) {
    push @classes, split(/\%/, $results[0]);
  }
 
  foreach $class (sort(@classes)){
    print "<option value=\"$class\">$class</option>\n";
  }
 
  if ($data{'admlogin'} eq $admlogin) {
    print "<option value=\"All\">All sections</option>\n";
  }
 
  print "</select></td></tr>
	<input type=hidden name=FA value=\"Export Grades to Spreadsheet or ASCII File\">
	<input type=hidden name=Confirm value=\"Yes\">
	<input type=hidden name=admlogin value=\"$data{'admlogin'}\">
        <input type=hidden name=admpasswd value=\"$data{'admpasswd'}\">
	<input type=submit value=\"Export Grades\">
	</td></tr>
	</form></td></tr></table>
	";
 print $footer;
 $dbh->disconnect();
 exit();
 }

#print it out
  $firstline = "Name|ID#|Section|TA|";
  $sth = $dbh->prepare("SELECT name FROM assignments ORDER BY number");
  $sth->execute();
  while (@results = $sth->fetchrow_array()) {
    $firstline .= "$results[0]|";
  }
  $sth->finish();
  chop $firstline;

 print "Content-type: text/plain\n\n";
 print "$firstline\n";
 if (($data{'admlogin'} eq $admlogin) 
       && ($data{'Class'} eq "All")) {
   $statement = "SELECT * FROM students ORDER BY name";
 } else {
   $statement = "SELECT * FROM students 
                   WHERE section=\"$data{'Class'}\"
                   ORDER BY name";
 }

  $sth = $dbh->prepare($statement);
  $sth->execute();
  while (@results = $sth->fetchrow_array()) {
    $name = shift @results;
    $id = shift @results;
    $class = shift @results;
    $ins = shift @results;
    $pw = shift @results;
    $line = join('|', @results);
    print "$name|$id|$class|$ins|$line\n";
  }
  $sth->finish();
 
 $dbh->disconnect();
 exit();
}

sub show_class_info {
  print "<table border=1>\n";
  print "<tr><th>Section</th><th>Students</th></tr>\n";
  $total = 0;
  $sth = $dbh->prepare("SELECT section, COUNT(*) FROM students GROUP BY section");
  $sth->execute();
  while (@results = $sth->fetchrow_array()) {
    print "<tr><td>$results[0]</td><td";
    if ($results[1] > 24) {
      print " bgcolor=red>$results[1]";
    } elsif ($results[1] < 24) {
      print " bgcolor=green>$results[1]";    
    } else {
      print ">$results[1]";
    }
    print "</td></tr>\n";
    $total += $results[1];
  }
  $sth->finish();
  print "<tr><td bgcolor=cyan><b>Total</b></td><td bgcolor=cyan><b>$total</b></td></tr>";
  print "</table>\n";
  
  print "<hr>\n";
  
  print "<table border=1>\n";
  print "<tr><th>Name</th><th>Section</th><th>TA</th></tr>\n";
  $sth = $dbh->prepare("SELECT name, section, TA FROM students ORDER BY name");
  $sth->execute();
  while (@results = $sth->fetchrow_array()) {
    print "<tr><td>$results[0]</td><td>$results[1]</td><td>$results[2]</td></tr>\n";
  }
  $sth->finish();
  print "</table>\n";
  print "<hr>\n";
}


