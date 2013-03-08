#!/usr/bin/perl
# usage: multi.pl <gpu #>

$GPU = $ARGV[0];

$xmlToDoDir = "xml".$GPU."/";
$xmlDoneDir = "completed_xml".$GPU."/";
$resultDir = "results/";

opendir(XML, $xmlToDoDir);
@rawXmlFiles = readdir(XML);
closedir(XML);
@xmlFiles = sort(@rawXmlFiles);
foreach $xmlFile (@xmlFiles) {
	if ($xmlFile =~ m/.xml$/) {
		$header = $xmlFile;
		$header =~ s/.xml//;
		$filename = $xmlToDoDir.$xmlFile;
		print "Processing $filename as $header on GPU $GPU\n";
		mkdir $resultDir."$header";
		$result = `java -jar SquareEvolution.jar $filename`;
		print "\t$result\n";

		system("mv badProteins$GPU.txt $resultDir$header/badProteins.txt");
		system("mv fitness$GPU.txt $resultDir$header/fitness.txt");
		system("mv output$GPU.txt $resultDir$header/output.txt");
		system("cp $filename $resultDir$header/");
		system("mv $filename $xmlDoneDir");
	}
}
