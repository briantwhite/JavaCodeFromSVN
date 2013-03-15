#!/usr/bin/perl
#
#
# generates a set of all possible "evolving new functions" xml files
#  for each code - use it to evolve best dimer to ligand binder and polymer, etc
#
# expects codes in Codes/
#
# expects Template.xml
#
# deals with:
#	- matching codes and rev trans stuff
#	- sends all H codes to processor 0
#	- sends all L codes to processor 1
#
#

# fill base; "R" makes it random
$fill = "T";

# these are the starting proteins
#  they're highly evolved for each of their particular functions
#	see Square Evolution log -06 page 22 and -07 page 40
$bestPolymer = "MHRDDDDIVEIKDLKVLFHRLL";
$bestDimer = "MRAKHHKCEFFF";
$bestLigandBinder = "MFTCFRCKCRKHFFREIYKFE";

# xml for making various targets
$dimerTags = "<LigandSequence>*</LigandSequence>\n"
			. "<LigandStructure>*</LigandStructure>\n"
			. "<LigandRotamer>2</LigandRotamer>\n";
			
$polymerTags = "<LigandSequence>*</LigandSequence>\n"
			. "<LigandStructure>*</LigandStructure>\n"
			. "<LigandRotamer>0</LigandRotamer>\n";
			
$ligandTags = "<LigandSequence>AKGKEGDH</LigandSequence>\n"
			. "<LigandStructure>RRDDDLD</LigandStructure>\n"
			. "<LigandRotamer>-1</LigandRotamer>\n";

# other useful xml
$xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			. "<Data>\n";
$port0 = "<PortNum>12000</PortNum>\n";
$port1 = "<PortNum>12001</PortNum>\n";
$log0 = "<LogFileName>output0.txt</LogFileName>\n";
$log1 = "<LogFileName>output1.txt</LogFileName>\n";
$fit0 = "<FitnessFileName>fitness0.txt</FitnessFileName>\n";
$fit1 = "<FitnessFileName>fitness1.txt</FitnessFileName>\n";
$badP0 = "<BadProteinFileName>badProteins0.txt</BadProteinFileName>\n";
$badP1 = "<BadProteinFileName>badProteins1.txt</BadProteinFileName>\n";

open (TEMPLATE, "Template.xml");
while ($line = <TEMPLATE>) {
	$templateXML .= $line;
}
close(TEMPLATE);


# get all the codes you'll have to work with
print "Getting Code Files\n";
my @codeFileNames;
opendir(CODEDIR, "Codes/");
while ($file = readdir(CODEDIR)) {
	if ($file =~ m/xml/) {
		push(@codeFileNames, $file);
	}
}
closedir(CODEDIR);

mkdir ("xml0");
mkdir ("xml1");

# make sets of starting DNA sequences
print "Making DNAs\n";
my %polymerDNAs = ();
my %dimerDNAs = ();
my %ligandDNAs = ();

foreach $codeFileName (@codeFileNames) {
	$codeName = $codeFileName;
	$codeName =~ s/\.xml//g;
	print "\t$codeName\n";
	
	$d = `java -classpath SquareEvolution.jar SE.RevTransAndPad Codes/$codeFileName $bestDimer $fill 100`;
	if ($d eq 'Failed') {
		die "Can't make DNA for $codeFileName\n";
	}
	$dimerDNAs{$codeName} = $d;
	
	$p = `java -classpath SquareEvolution.jar SE.RevTransAndPad Codes/$codeFileName $bestPolymer $fill 100`;
	if ($p eq 'Failed') {
		die "Can't make DNA for $codeFileName\n";
	}
	$polymerDNAs{$codeName} = $p;
	
	$l = `java -classpath SquareEvolution.jar SE.RevTransAndPad Codes/$codeFileName $bestLigandBinder $fill 100`;
	if ($l eq 'Failed') {
		die "Can't make DNA for $codeFileName\n";
	}
	$ligandDNAs{$codeName} = $l;
}

# make the polymer -> ligand binder & -> dimer files
print "Making p -> lb and p ->d files\n";
foreach $codeFileName (@codeFileNames) {
		$codeName = $codeFileName;
		$codeName =~ s/\.xml//g;
		print "\t$codeName\n";
		if ($codeName =~ m/H/) {
			$toLBxmlFileName = "xml0/".$codeFileName."_p_lb.xml";
			$toDxmlFileName = "xml0/".$codeFileName."_p_d.xml";
			$tailXML = $port0 . $log0 . $fit0 . $badP0;
		} else {
			$toLBxmlFileName = "xml1/".$codeFileName."_p_lb.xml";
			$toDxmlFileName = "xml1/".$codeFileName."_p_d.xml";
			$tailXML = $port1 . $log1 . $fit1 . $badP1;
		}
		
		open(TO_L_FILE, ">$toLBxmlFileName");
		print TO_L_FILE $xmlHeader;
		print TO_L_FILE "<StartingDNA>";
		print TO_L_FILE $polymerDNAs{$codeName};
		print TO_L_FILE "</StartingDNA>\n";
		print TO_L_FILE $ligandTags;
		print TO_L_FILE $tailXML;
		print TO_L_FILE $templateXML;
		close TO_L_FILE;
		
		open(TO_D_FILE, ">$toLBxmlFileName");
		print TO_D_FILE $xmlHeader;
		print TO_D_FILE "<StartingDNA>";
		print TO_D_FILE $polymerDNAs{$codeName};
		print TO_D_FILE "</StartingDNA>\n";
		print TO_D_FILE $dimerTags;
		print TO_D_FILE $tailXML;
		print TO_D_FILE $templateXML;
		close TO_D_FILE;
}



