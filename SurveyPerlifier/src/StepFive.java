
import java.awt.BorderLayout;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StepFive {
	StringBuffer getnamePlGenerator;

	StringBuffer surveyPlGenerator;

	public StepFive() {
		getnamePlGenerator = new StringBuffer();
		surveyPlGenerator = new StringBuffer();
	}

	public void setup(JPanel actionPane, JLabel agendaText, DataBank dataBank) {
		agendaText.setText(Perlifier.agendaStart + Perlifier.agendaStepOne
				+ Perlifier.agendaStepTwo + Perlifier.agendaStepThree
				+ Perlifier.agendaStepFour
				+ "<li><font color=red>All Done.</font></li>"
				+ Perlifier.agendaEnd);
		agendaText.repaint();

		actionPane.setLayout(new BorderLayout());
		actionPane.add(new JLabel(
				"<html><center><font size=+3>All done</font></center><br>"
						+ "Files were saved in:" + dataBank.outputDir),
				BorderLayout.CENTER);
		actionPane.repaint();

		//set up the getname.pl script - the one that authenticates the user
		// and checks the date
		getnamePlGenerator.append("#!/usr/bin/perl\n");
		getnamePlGenerator.append("use CGI;\n");
		getnamePlGenerator.append("use Time::Local;\n");
		getnamePlGenerator.append("%NamesAndIDs = ();\n");
		getnamePlGenerator.append("open(LIST,\"<" + dataBank.namesTxt
				+ "\") or die (\"could not open " + dataBank.namesTxt
				+ "\");\n");
		getnamePlGenerator.append("@WholeList = <LIST>;\n");
		getnamePlGenerator.append("close(LIST);\n");
		getnamePlGenerator
				.append("print \"Content-type: text/html \\n\\n\";\n");
		getnamePlGenerator
				.append("print \"<html><head><title>Verfy Your Identity</title></head>\\n\";\n");
		getnamePlGenerator
				.append("print \"<center><h2>This is a secure transmission</h2></center><br>\\n\";\n");
		getnamePlGenerator.append("$thisYear = int((localtime)[5]);\n");
		getnamePlGenerator.append("$preStart = timelocal(0,0,0,"
				+ dataBank.startPreSurveyDate.split("/")[1] + ","
				+ dataBank.startPreSurveyDate.split("/")[0]
				+ " - 1,$thisYear);\n");
		if (dataBank.prePost) {
			getnamePlGenerator.append("$preEnd = timelocal(0,0,0,"
					+ dataBank.endPreSurveyDate.split("/")[1] + ","
					+ dataBank.endPreSurveyDate.split("/")[0]
					+ " - 1,$thisYear);\n");
			getnamePlGenerator.append("$postStart = timelocal(0,0,0,"
					+ dataBank.startPostSurveyDate.split("/")[1] + ","
					+ dataBank.startPostSurveyDate.split("/")[0]
					+ " - 1,$thisYear);\n");
		}
		getnamePlGenerator.append("$postEnd = timelocal(0,0,0,"
				+ dataBank.endPostSurveyDate.split("/")[1] + ","
				+ dataBank.endPostSurveyDate.split("/")[0]
				+ " - 1,$thisYear);\n");
		getnamePlGenerator.append("$now = time;\n");
		getnamePlGenerator.append("if($now < $preStart) {\n");
		getnamePlGenerator
				.append("    print \"It is too early to enter a survey yet.\";\n");
		getnamePlGenerator.append("    exit;\n");
		getnamePlGenerator.append("}\n");
		getnamePlGenerator.append("if($now > $postEnd) {\n");
		getnamePlGenerator
				.append("    print \"It is too late to enter a survey this semester.\";\n");
		getnamePlGenerator.append("    exit;\n");
		getnamePlGenerator.append("}\n");
		if (dataBank.prePost) {
			getnamePlGenerator
					.append("if(($now > $preEnd) && ($now < $postStart)) {\n");
			getnamePlGenerator
					.append("    print \"It is too late to enter a pre-survey and too soon to enter a post-survey.\";\n");
			getnamePlGenerator.append("    exit;\n");
			getnamePlGenerator.append("}\n");
			getnamePlGenerator.append("if($now > $postStart) {\n");
			getnamePlGenerator.append("    $prePost = \"post\";\n");
			getnamePlGenerator.append("} else {\n");
			getnamePlGenerator.append("    $prePost = \"pre\";\n");
			getnamePlGenerator.append("}\n");
		}
		getnamePlGenerator
				.append("print \"<font color=red>Before you can enter your survey, we have to be sure who you are.\\n\";\n");
		getnamePlGenerator.append("print \"</font><br>\\n\";\n");
		getnamePlGenerator.append("print \"<form action=\\\""
				+ dataBank.scriptURL + dataBank.surveyPlName
				+ "\\\" method=\\\"GET\\\">\\n\";\n");
		getnamePlGenerator.append("foreach $NameIDPair (@WholeList) {\n");
		getnamePlGenerator
				.append("    @SplitUp = split /\\t+/, $NameIDPair;\n");
		getnamePlGenerator
				.append("    $NamesAndIDs{$SplitUp[0]} = $SplitUp[1];\n");
		getnamePlGenerator.append("}\n");
		getnamePlGenerator
				.append("print \"Choose your name from the list below.<br>\\n\";\n");
		getnamePlGenerator
				.append("print \"You may have to scroll down to find it.<br>\\n\";\n");
		getnamePlGenerator
				.append("print \"<select name=\\\"Name\\\" size=15>\\n\";\n");
		getnamePlGenerator.append("foreach $key (sort keys %NamesAndIDs) {\n");
		getnamePlGenerator.append("    print \"<option>$key\\n\";\n");
		getnamePlGenerator.append("}\n");
		getnamePlGenerator.append("print \"</select>\\n\";\n");
		getnamePlGenerator.append("print \"<br>\\n\";\n");
		getnamePlGenerator
				.append("print \"And type your ID# in the space below as a password.<br>\\n\";\n");
		getnamePlGenerator
				.append("print \"Usually, this is your social security number.<br>\\n\";\n");
		getnamePlGenerator
				.append("print \"You can enter it with or without dashes or spaces.<br>\\n\";\n");
		getnamePlGenerator
				.append("print \"<input type=\\\"password\\\" name=\\\"id\\\" size=12><br><br><br>\\n\";\n");
		getnamePlGenerator
				.append("print \"Then click the SUBMIT button.<br>\\n\";\n");
		if (dataBank.prePost) {
			getnamePlGenerator
					.append("print \"<input type=\\\"hidden\\\" name=\\\"prepost\\\" "
							+ "value=\\\"$prePost\\\">\\n\";\n");
		}
		getnamePlGenerator
				.append("print \"<input type=\\\"submit\\\" value=\\\"Submit\\\"><br>\\n\";\n");
		getnamePlGenerator
				.append("print \"<input type=\\\"reset\\\"><br>\\n\";\n");
		getnamePlGenerator.append("print \"<form><hr></body></html>\\n\";\n");
		getnamePlGenerator.append("exit;\n");

		// save the entry/validation script
		BufferedWriter outStream = null;
		try {
			outStream = new BufferedWriter(new FileWriter(dataBank.outputDir
					+ "/" + dataBank.getnamePlName));
			outStream.write(getnamePlGenerator.toString());
			outStream.newLine();
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			try {
				if (outStream != null) {
					outStream.close();
				}
			} catch (Exception e) {
				System.err
						.println("Unable to close output stream for getname.pl");
			}
		}

		//now make the survey perl script
		surveyPlGenerator.append("#!/usr/bin/perl \n");
		surveyPlGenerator.append("use CGI; \n");
		surveyPlGenerator.append("%NamesAndIDs = ();\n");
		surveyPlGenerator.append("@survey_files = ();\n");
		surveyPlGenerator.append("$query = CGI::new();\n");
		surveyPlGenerator.append("$testname = $query->param(\"Name\");\n");
		surveyPlGenerator.append("$testID = $query->param(\"id\");\n");
		surveyPlGenerator.append("$prepost = $query->param(\"prepost\");\n");
		surveyPlGenerator.append("$testID =~ s/[^0-9]//g;\n");
		surveyPlGenerator.append("open(LIST,\"<" + dataBank.namesTxt
				+ "\") or die (\"could not open " + dataBank.namesTxt
				+ "\");\n");
		surveyPlGenerator.append("@WholeList = <LIST>;\n");
		surveyPlGenerator.append("close(LIST);\n");
		surveyPlGenerator.append("print \"Content-type: text/html \\n\\n\";\n");
		surveyPlGenerator.append("print \"<html><head><title>\";\n");
		if (dataBank.prePost) {
			surveyPlGenerator.append("if ($prepost eq \"pre\") { \n");
			surveyPlGenerator.append("   print \"" + dataBank.preSurveyHeadline
					+ "\";\n");
			surveyPlGenerator.append("} else { \n");
			surveyPlGenerator.append("   print \""
					+ dataBank.postSurveyHeadline + "\";\n");
			surveyPlGenerator.append("}\n");
		} else {
			surveyPlGenerator.append("print \"" + dataBank.preSurveyHeadline
					+ "\";\n");
		}
		surveyPlGenerator.append("foreach $NameIDPair (@WholeList) {\n");
		surveyPlGenerator.append("    @SplitUp = split /\\t+/, $NameIDPair;\n");
		surveyPlGenerator
				.append("    $NamesAndIDs{$SplitUp[0]} = $SplitUp[1];\n");
		surveyPlGenerator.append("}\n");
		surveyPlGenerator.append("print \"</title></head>\\n\";\n");
		surveyPlGenerator
				.append("print \"<center><h2>This is a secure transmission</h2></center><br>\\n\";\n");

		// check name and id #
		surveyPlGenerator.append("if(!(exists $NamesAndIDs{$testname})) {\n");
		surveyPlGenerator
				.append("    print \"<font color=red>Your name is not on the class list."
						+ " Please contact the survey administrator.</font><br>\\n\";\n");
		surveyPlGenerator.append("    print \"</body></html>\\n\";\n");
		surveyPlGenerator.append("    exit;\n");
		surveyPlGenerator.append("} \n");
		surveyPlGenerator
				.append("if ($NamesAndIDs{$testname} != $testID) { \n");
		surveyPlGenerator
				.append("    print \"<font color=red>Your ID number does not match our records."
						+ " Please go back and try again.</font><br>\\n\";\n");
		surveyPlGenerator.append("    print \"</body></html>\\n\";\n");
		surveyPlGenerator.append("    exit;\n");
		surveyPlGenerator.append("} \n");

		// name and ID OK
		//change White, Brian to White_Brian.pre.txt
		surveyPlGenerator.append("$fixedname = $testname;\n");
		surveyPlGenerator.append("$fixedname =~ s/[^a-zA-z,]//g;\n");
		surveyPlGenerator.append("$fixedname =~ s/,/_/g;\n");

		if (dataBank.prePost) {
			surveyPlGenerator
					.append("$filename = $fixedname.\".\".$prepost.\".txt\";\n");
		} else {
			surveyPlGenerator.append("$filename = $fixedname.\".txt\";\n");
		}
		// if it is a GET, then it came from the entry validation script
		//   and therefore has no survey data with it
		//   so you'll have to see if the survey file exists
		surveyPlGenerator
				.append("if ($ENV{\'REQUEST_METHOD\'} eq \"GET\") {\n");
		surveyPlGenerator.append("    opendir SURVEY_DIR, \""
				+ dataBank.surveyDir
				+ "\" or die (\"could not open survey directory\");\n");
		surveyPlGenerator.append("    @survey_files = readdir(SURVEY_DIR);\n");
		surveyPlGenerator.append("    closedir SURVEY_DIR;\n");
		surveyPlGenerator.append("    $exists = 0;\n");
		surveyPlGenerator.append("    foreach $survey (@survey_files) { \n");
		surveyPlGenerator.append("        if ($survey eq $filename) { \n");
		surveyPlGenerator.append("            $exists = 1;\n");
		surveyPlGenerator.append("            last;\n");
		surveyPlGenerator.append("        }\n");
		surveyPlGenerator.append("    }\n");
		surveyPlGenerator.append("if ($exists eq 1) {\n");

		// if file exitsts then read in values
		surveyPlGenerator
				.append("print \"We found your survey file $filename; you can edit it below"
						+ "<hr>\\n\";\n");
		surveyPlGenerator.append("open (SURVEY_FILE, \"<" + dataBank.surveyDir
				+ "$filename\")"
				+ " or die (\"could not open survey file\");\n");
		surveyPlGenerator
				.append("flock (SURVEY_FILE, 2) or die (\"could not lock survey file\");\n");
		surveyPlGenerator.append("@WholeSurvey = <SURVEY_FILE>;\n");
		surveyPlGenerator
				.append("flock (SURVEY_FILE, 8) or die (\"could not un-lock survey file\");\n");
		surveyPlGenerator
				.append("close (SURVEY_FILE) or die (\"could not close survey file\");\n");
		surveyPlGenerator.append("foreach $line (@WholeSurvey) {\n");
		surveyPlGenerator.append("    $AllLines = $AllLines.$line;\n");
		surveyPlGenerator.append("}\n");

		// pull out individual responses using a complex regexp
		// note that the last one is different
		for (int questionNum = 0; questionNum < dataBank.getNumQuestions() - 1; questionNum++) {
			surveyPlGenerator.append("$AllLines =~ m/{{"
					+ dataBank.getAQuestion(questionNum)
					+ "[^}]*}}\\n([^{]*){{/s;\n");
			surveyPlGenerator.append("    $"
					+ dataBank.getAQuestion(questionNum) + "=$1;\n");
			surveyPlGenerator.append("    chomp $"
					+ dataBank.getAQuestion(questionNum) + ";\n");
			surveyPlGenerator.append("    chomp $"
					+ dataBank.getAQuestion(questionNum) + ";\n");
		}
		// do the last one specially
		surveyPlGenerator.append("$AllLines =~ m/{{"
				+ dataBank.getAQuestion(dataBank.getNumQuestions() - 1)
				+ "[^}]*}}\\n([^{]*)/s;\n");
		surveyPlGenerator.append("    $"
				+ dataBank.getAQuestion(dataBank.getNumQuestions() - 1)
				+ "=$1;\n");
		surveyPlGenerator
				.append("    chomp $"
						+ dataBank.getAQuestion(dataBank.getNumQuestions() - 1)
						+ ";\n");
		surveyPlGenerator
				.append("    chomp $"
						+ dataBank.getAQuestion(dataBank.getNumQuestions() - 1)
						+ ";\n");

		//if file does not exist, then set all values to ""
		surveyPlGenerator.append("} else {\n");
		for (int questionNum = 0; questionNum < dataBank.getNumQuestions(); questionNum++) {
			surveyPlGenerator.append("    $"
					+ dataBank.getAQuestion(questionNum) + "=\"\";\n");
		}

		surveyPlGenerator.append("} \n");
		surveyPlGenerator.append("} \n");
		//if not a GET, see if it is a PUT and therefore it came from this
		// script
		// so get the values from the CGI
		surveyPlGenerator
				.append("if ($ENV{\'REQUEST_METHOD\'} eq \"POST\") {\n");
		for (int questionNum = 0; questionNum < dataBank.getNumQuestions(); questionNum++) {
			surveyPlGenerator.append("    $"
					+ dataBank.getAQuestion(questionNum)
					+ " = $query->param(\""
					+ dataBank.getAQuestion(questionNum) + "\");\n");
			surveyPlGenerator.append("    $"
					+ dataBank.getAQuestion(questionNum) + " =~ s/{/[/g;\n");
			surveyPlGenerator.append("    $"
					+ dataBank.getAQuestion(questionNum) + " =~ s/}/]/g;\n");
		}

		surveyPlGenerator.append("}\n");

		// it must be either post or get - otherwise the script was accessed
		// wrong
		surveyPlGenerator.append("if (($ENV{\'REQUEST_METHOD\'} ne \"GET\") "
				+ "and ($ENV{\'REQUEST_METHOD\'} ne \"POST\")) {\n");
		surveyPlGenerator
				.append("    print \"Script entered incorrectly; terminating.\";\n");
		surveyPlGenerator.append("    exit;\n");
		surveyPlGenerator.append("} \n");

		//now have the values in the right variables

		//first, save them in the file
		surveyPlGenerator.append("open (OUTFILE, \">" + dataBank.surveyDir
				+ "$filename\")"
				+ " or die (\"could not open survey file\");\n");
		surveyPlGenerator
				.append("flock (OUTFILE, 2) or die (\"could not lock survey file\");\n");
		surveyPlGenerator.append("$stamp = time;\n");
		surveyPlGenerator.append("print OUTFILE \"$stamp\\n\";\n");
		surveyPlGenerator.append("print OUTFILE \"$testname\\n\";\n");
		if (dataBank.prePost) {
			surveyPlGenerator.append("print OUTFILE \"$prepost-survey\\n\";\n");
		}
		surveyPlGenerator.append("$todaysdate = localtime(time);\n");
		surveyPlGenerator.append("print OUTFILE \"$todaysdate\\n\";\n");
		surveyPlGenerator.append(dataBank.getSurveyTxtFilePerlCode() + "\n");
		surveyPlGenerator
				.append("flock (OUTFILE, 8) or die (\"could not un-lock survey file\");\n");
		surveyPlGenerator
				.append("close (OUTFILE) or die (\"could not close survey file\");\n");

		//then, output the survey

		//first, create a list of missing entries
		StringBuffer missingDataReport = new StringBuffer();
		missingDataReport.append("$complete = \"\";\n");
		missingDataReport.append(dataBank.getErrorCheckCode() + "\n");
		missingDataReport.append("if ($complete ne \"\") {\n");
		missingDataReport
				.append("print \"<hr>Your work is not complete, you should be sure to:<br>\\n\";\n");
		missingDataReport
				.append("print \"<font color=red>$complete <br></font><hr>\\n\";\n");
		missingDataReport.append("}\n");

		//finally, the survey itself
		// first, replace the <misingdatareport> tag with the list of missing
		// entries
		StringBuffer surveyPerlCodeBuffer = new StringBuffer(dataBank
				.getSurveyFormPerlCode());
		int start = surveyPerlCodeBuffer.indexOf("<missingdatareport>");
		int end = surveyPerlCodeBuffer.indexOf(">", start);
		if (start != -1) {
			StringBuffer partialPerlCodeBuffer = surveyPerlCodeBuffer.delete(
					start, end + 1);
			String fixedSurveyFormPerlCode = partialPerlCodeBuffer.insert(
					start, missingDataReport.toString()).toString();
			surveyPlGenerator.append(fixedSurveyFormPerlCode);
		} else {
			surveyPlGenerator.append(dataBank.getSurveyFormPerlCode());
		}
		surveyPlGenerator.append("exit;\n");

		// save the survey script
		BufferedWriter outFileStream = null;
		try {
			outFileStream = new BufferedWriter(new FileWriter(
					dataBank.outputDir + "/" + dataBank.surveyPlName));
			outFileStream.write(surveyPlGenerator.toString());
			outFileStream.newLine();
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			try {
				if (outFileStream != null) {
					outFileStream.close();
				}
			} catch (Exception e) {
				System.err
						.println("Unable to close output stream for survey.pl");
			}
		}

	}

	public void cleanup(JPanel actionPane, JLabel agendaText, DataBank dataBank) {

		//take care of gui
		agendaText.removeAll();
		actionPane.removeAll();
		agendaText.repaint();
		actionPane.repaint();

	}

}