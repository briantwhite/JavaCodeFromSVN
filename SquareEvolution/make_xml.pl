#!/usr/bin/perl
# first arg is GPU # to index the directories
# remaining args are a list of genetic code file names and generates
#  three xml files for running in square evolution
#   one to make dimers, one for polymers, and one for ligand binders
#   expects templates in template_xml<GPU#>/
#   puts results in xml<GPU#>/
#   expects code files in Codes/
#   not full path of code, just code file name .xml

$GPU = shift(@ARGV);
$templateXMLDir = "template_xml".$GPU."/";
$outputXMLDir = "xml".$GPU."/";

foreach $arg (@ARGV) {
    if ($arg =~ m/(.*).xml$/) {
    	$name = $1;
    	$name =~ s/Codes\///;

    	$arg =~ s/Codes\///;
        print "Making files for $arg \n";

        #first the dimers file
        open (INFILE, "${templateXMLDir}NoCode_d.xml");
        open (OUTFILE, ">${outputXMLDir}${name}_d.xml");
        while (<INFILE>) {
        	chomp;
        	if ($_ =~ m/<GeneticCodeFileName>/) {
        		print OUTFILE "<GeneticCodeFileName>Codes/$arg</GeneticCodeFileName>\n";
        	} else {
        		print OUTFILE "$_\n";
        	}
        }
    	close INFILE;
    	close OUTFILE;
    	print "\t${outputXMLDir}${name}_d.xml\n";
    

        #next the polymers file
        open (INFILE, "${templateXMLDir}NoCode_p.xml");
        open (OUTFILE, ">${outputXMLDir}${name}_p.xml");
        while (<INFILE>) {
        	chomp;
        	if ($_ =~ m/<GeneticCodeFileName>/) {
        		print OUTFILE "<GeneticCodeFileName>Codes/$arg</GeneticCodeFileName>\n";
        	} else {
        		print OUTFILE "$_\n";
        	}
        }
    	close INFILE;
    	close OUTFILE;
    	print "\t${outputXMLDir}${name}_p.xml\n";
    

        #finally the ligand binders file
        open (INFILE, "${templateXMLDir}NoCode_l.xml");
        open (OUTFILE, ">${outputXMLDir}${name}_l.xml");
        while (<INFILE>) {
        	chomp;
        	if ($_ =~ m/<GeneticCodeFileName>/) {
        		print OUTFILE "<GeneticCodeFileName>Codes/$arg</GeneticCodeFileName>\n";
        	} else {
        		print OUTFILE "$_\n";
        	}
        }
    	close INFILE;
    	close OUTFILE;
    	print "\t${outputXMLDir}${name}_l.xml\n";
    }

}