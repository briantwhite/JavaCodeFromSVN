
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class StepThree {

	//the stringbuffer for the part that generates the survey html
	StringBuffer surveyFormGenerator = new StringBuffer();

	//the stringbuffer for the part that generates the survey.txt file
	StringBuffer surveyTxtFileGenerator = new StringBuffer();

	//the stringbuffer for the part that checks to see if all questions
	// have been answered
	StringBuffer verificationGenerator = new StringBuffer();

	//the stringbuffer for verifing checkbox groups - keeps the currently
	// active checkbox list
	StringBuffer checkBoxesToVerify = new StringBuffer();

	public StepThree() {
	}

	public void setup(JPanel actionPane, JLabel agendaText, DataBank dataBank) {

		//clear out question list & string buffers
		dataBank.clearSurveyHtmlLines();
		dataBank.clearQuestionList();
		dataBank.clearQuestionInfo();
		surveyFormGenerator = new StringBuffer();
		surveyTxtFileGenerator = new StringBuffer();
		verificationGenerator = new StringBuffer();

		agendaText
				.setText(Perlifier.agendaStart
						+ Perlifier.agendaStepOne
						+ Perlifier.agendaStepTwo
						+ "<li><font color=red>Review list of survey questions.</font></li>"
						+ Perlifier.agendaStepFour + Perlifier.agendaStepFive
						+ Perlifier.agendaEnd);
		agendaText.repaint();

		//process the survey file & create an array of questions & properties
		//first, read in the file
		String line;
		BufferedReader inStream = null;

		try {
			inStream = new BufferedReader(new FileReader(dataBank
					.getSurveyHtmlFileName()));
			while ((line = inStream.readLine()) != null) {
				dataBank.addLineToSurveyHtml(line);
			}
		} catch (FileNotFoundException e) {
			dataBank.addQuestionInfoLine("No survey file selected!");
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (Exception e) {
				System.err.println("unable to close outstream");
			}
		}

		//process it line-by-line
		//first, variables that persist from line to line
		int i = 0; //line counter
		String name = new String(""); // the name of the currently-active form
									  // element
		String newName = new String(""); // the name of the next form element
		String errorName = new String(""); // what to call the question if the
										   // entry is blank
		boolean isAFormTag; // set if the line contains a form tag
		boolean firstRadioButtonInSet = true;
		boolean firstCheckBoxInSet = true;
		boolean checkBoxVerificationPending = false; // we have a set of
													 // checkboxes
		//that have not been completed yet
		boolean isRadioCheckText = false; //set if an input tag that is either
		//radio button, textfield, or checkbox
		// that is, NOT a submit or reset button

		String currentLine;

		//now analyze each line of the rest of the file
		// set up some regexps
		Pattern formTag = Pattern.compile("<form", Pattern.CASE_INSENSITIVE);
		Pattern startSelectTag = Pattern.compile("<select",
				Pattern.CASE_INSENSITIVE);
		Pattern endSelectTag = Pattern.compile("</select>",
				Pattern.CASE_INSENSITIVE);
		Pattern nameTag = Pattern.compile(" name=", Pattern.CASE_INSENSITIVE);
		Pattern optionTag = Pattern
				.compile("<option", Pattern.CASE_INSENSITIVE);
		Pattern valueTag = Pattern.compile(" value=", Pattern.CASE_INSENSITIVE);
		Pattern inputTag = Pattern.compile("<input", Pattern.CASE_INSENSITIVE);
		Pattern typeTag = Pattern.compile(" type=", Pattern.CASE_INSENSITIVE);
		Pattern checkboxTag = Pattern.compile("checkbox",
				Pattern.CASE_INSENSITIVE);
		Pattern radioTag = Pattern.compile("radio", Pattern.CASE_INSENSITIVE);
		Pattern textTag = Pattern.compile("text", Pattern.CASE_INSENSITIVE);
		Pattern textAreaTag = Pattern.compile("<textarea",
				Pattern.CASE_INSENSITIVE);
		Pattern rowsTag = Pattern.compile(" rows=", Pattern.CASE_INSENSITIVE);
		Pattern colsTag = Pattern.compile(" cols=", Pattern.CASE_INSENSITIVE);
		Pattern lengthTag = Pattern.compile(" length=",
				Pattern.CASE_INSENSITIVE);
		Pattern synopsisTag = Pattern.compile(" synopsis=",
				Pattern.CASE_INSENSITIVE);
		Pattern endOfCheckBoxSetTag = Pattern.compile("<endofcheckboxset>",
				Pattern.CASE_INSENSITIVE);
		Pattern errorNameTag = Pattern.compile(" errorname=",
				Pattern.CASE_INSENSITIVE);
		Pattern missingDataReportTag = Pattern.compile("<missingdatareport>",
				Pattern.CASE_INSENSITIVE);

		while (i < dataBank.getNumSurveyHtmlLines()) {
			currentLine = dataBank.getLineFromSurvey(i);
			isAFormTag = false;

			// deal with the <form> tag
			Matcher findFormTag = formTag.matcher(currentLine);
			if (findFormTag.find()) {
				isAFormTag = true;
				surveyFormGenerator.append("print \"<form action=\\\""
						+ dataBank.scriptURL + dataBank.surveyPlName
						+ "\\\" method=\\\"POST\\\">\\n\";\n");
				surveyFormGenerator
						.append("print \"<input type=\\\"hidden\\\" name=\\\"Name\\\" value=\\\""
								+ "$testname\\\">\\n\";\n");
				surveyFormGenerator
						.append("print \"<input type=\\\"hidden\\\" name=\\\"id\\\" value=\\\""
								+ "$testID\\\">\\n\";\n");
				if (dataBank.prePost) {
					surveyFormGenerator
							.append("print \"<input type=\\\"hidden\\\" name=\\\"prepost\\\" value=\\\""
									+ "$prepost\\\">\\n\";\n");
				}
			} // done with form tag

			// deal with a pop-up list
			Matcher findSelectTag = startSelectTag.matcher(currentLine);

			if (findSelectTag.find()) {
				isAFormTag = true;

				String lineRemainder = currentLine.substring(findSelectTag
						.end());
				name = getParamValue(nameTag, lineRemainder);
				String synopsis = getParamValue(synopsisTag, lineRemainder);
				errorName = getParamValue(errorNameTag, lineRemainder);

				// save question name in data bank
				dataBank.addNewQuestion(name);

				// save question info in data bank
				dataBank.addQuestionInfoLine("Question #"
						+ String.valueOf(dataBank.getNumQuestions())
						+ ": type=pop-up-list; Name=" + name + ";");
				dataBank.addQuestionInfoLine("  synopsis=" + synopsis);
				dataBank.addQuestionInfoLine("  errorName=" + errorName);
				dataBank.addQuestionInfoLine("  Possible values:");

				// generate the perl code
				surveyFormGenerator.append("print \"<select name=\\\"" + name
						+ "\\\">\\n\";\n");

				surveyTxtFileGenerator.append("print OUTFILE \"{{" + name
						+ ": " + escapeQuotes(synopsis) + "}}\\n\";\n");
				surveyTxtFileGenerator.append("print OUTFILE $" + name + ";\n");
				surveyTxtFileGenerator.append("print OUTFILE \"\\n\\n\";\n");

				verificationGenerator.append("if($" + name + " eq \"\") { \n");
				verificationGenerator
						.append("     $complete .= \"Choose one of the options for ");
				if (errorName.equals("")) {
					verificationGenerator.append(name);
				} else {
					verificationGenerator.append(escapeQuotes(errorName));
				}
				verificationGenerator.append(".<br>\\n\";\n");
				verificationGenerator.append("}\n");

			} // done with select tag

			// deal with an <option> tag
			Matcher findOptionTag = optionTag.matcher(currentLine);
			if (findOptionTag.find()) {
				isAFormTag = true;
				String partAfterOptionTag = currentLine.substring(findOptionTag
						.end());

				// see if it has a value tag
				String value = getParamValue(valueTag, partAfterOptionTag);

				// then get the choice
				String[] remainderParts = partAfterOptionTag.split(">");
				String choice = remainderParts[1].trim();

				// add to the question info
				dataBank.addQuestionInfoLine("    -" + choice + "; value="
						+ value);

				// now, print out the appropriate perl code
				surveyFormGenerator.append("print \"<option ");
				if (!value.equals("")) {
					surveyFormGenerator.append("value=\\\"" + value
							+ "\\\" \";\n");
				}
				surveyFormGenerator.append("if ($" + name + " eq ");
				if (value.equals("")) {
					surveyFormGenerator.append("\"" + choice + "\"");
				} else {
					surveyFormGenerator.append("\"" + value + "\"");
				}
				surveyFormGenerator.append(") {\n");
				surveyFormGenerator.append("    print \"SELECTED\";\n");
				surveyFormGenerator.append("}\n");
				surveyFormGenerator.append("print \">" + choice + "\\n\";\n");
			}

			// deal with form tags that start with <input: checkboxes, radio
			// buttons, and text
			Matcher findInputTag = inputTag.matcher(currentLine);
			if (findInputTag.find()) {
				isAFormTag = true;
				isRadioCheckText = false;
				String partAfterInputTag = currentLine.substring(findInputTag
						.end());

				// see if it's a checkbox & if so, deal with it
				Matcher findCheckboxTag = checkboxTag
						.matcher(partAfterInputTag);
				if (findCheckboxTag.find()) {
					checkBoxVerificationPending = true;
					isRadioCheckText = true;

					name = getParamValue(nameTag, partAfterInputTag);
					String value = getParamValue(valueTag, partAfterInputTag);
					String synopsis = getParamValue(synopsisTag,
							partAfterInputTag);
					if (firstCheckBoxInSet) {
						errorName = getParamValue(errorNameTag,
								partAfterInputTag);
						firstCheckBoxInSet = false;
					}

					// save question name in data bank
					dataBank.addNewQuestion(name);

					// save question info in data bank
					dataBank.addQuestionInfoLine("Question #"
							+ String.valueOf(dataBank.getNumQuestions())
							+ ": type=checkbox; Name=" + name + ";");
					dataBank.addQuestionInfoLine("  checked value=" + value
							+ "; synopsis=" + synopsis);
					dataBank.addQuestionInfoLine("  errorName="
							+ escapeQuotes(errorName));

					// print out appropriate perl code
					surveyFormGenerator
							.append("print \"<input type=\\\"checkbox\\\" name=\\\""
									+ name
									+ "\\\" value=\\\""
									+ value
									+ "\\\" \";\n");
					surveyFormGenerator.append("if ($" + name + " eq \""
							+ value + "\") { \n");
					surveyFormGenerator.append("     print \"CHECKED\";\n");
					surveyFormGenerator.append("}\n");
					surveyFormGenerator.append("print \">\\n\";\n");

					surveyTxtFileGenerator.append("print OUTFILE \"{{" + name
							+ ": " + escapeQuotes(synopsis) + "}}\\n\";\n");
					surveyTxtFileGenerator.append("print OUTFILE $" + name
							+ ";\n");
					surveyTxtFileGenerator
							.append("print OUTFILE \"\\n\\n\";\n");

					checkBoxesToVerify.append("($" + name + " eq \"\") and ");

				} //done with checkbox

				//see if it's a radio button and, if so, deal with it
				Matcher findRadioTag = radioTag.matcher(partAfterInputTag);
				if (findRadioTag.find()) {
					newName = getParamValue(nameTag, partAfterInputTag);
					if (newName.equals(name)) {
						firstRadioButtonInSet = false;
					} else {
						firstRadioButtonInSet = true;
					}
					isRadioCheckText = true;

					name = newName;

					String value = getParamValue(valueTag, partAfterInputTag);
					String synopsis = new String("");
					if (firstRadioButtonInSet) {
						synopsis = getParamValue(synopsisTag, partAfterInputTag);
					}
					errorName = getParamValue(errorNameTag, partAfterInputTag);

					// save question name in data bank
					if (firstRadioButtonInSet) {
						dataBank.addNewQuestion(name);
					}

					// save question info in data bank
					if (firstRadioButtonInSet) {
						dataBank.addQuestionInfoLine("Question #"
								+ String.valueOf(dataBank.getNumQuestions())
								+ ": type=radio-button; Name=" + name + ";");
						dataBank.addQuestionInfoLine("  synopsis=" + synopsis);
						dataBank.addQuestionInfoLine("  errorName="
								+ escapeQuotes(errorName));
					}
					dataBank.addQuestionInfoLine("    -checked value=" + value);

					// print out appropriate perl code
					surveyFormGenerator
							.append("print \"<input type=\\\"radio\\\" name=\\\""
									+ name
									+ "\\\" value=\\\""
									+ value
									+ "\\\" \";\n");
					surveyFormGenerator.append("if ($" + name + " eq \""
							+ value + "\") { \n");
					surveyFormGenerator.append("     print \"CHECKED\";\n");
					surveyFormGenerator.append("}\n");
					surveyFormGenerator.append("print \">\\n\";\n");

					if (firstRadioButtonInSet) {
						surveyTxtFileGenerator.append("print OUTFILE \"{{"
								+ name + ": " + escapeQuotes(synopsis)
								+ "}}\\n\";\n");
						surveyTxtFileGenerator.append("print OUTFILE $" + name
								+ ";\n");
						surveyTxtFileGenerator
								.append("print OUTFILE \"\\n\\n\";\n");

						verificationGenerator.append("if($" + name
								+ " eq \"\" ) {\n");
						verificationGenerator
								.append("     $complete .= \"Click one of the buttons for ");
						if (errorName.equals("")) {
							verificationGenerator.append(name);
						} else {
							verificationGenerator.append(errorName);
						}
						verificationGenerator.append(".<br>\\n\";\n");
						verificationGenerator.append("}\n");
					}

				} // done with radio button

				// see if its a text field and, if so, deal with it
				Matcher findTextTag = textTag.matcher(partAfterInputTag);
				if (findTextTag.find()) {
					name = getParamValue(nameTag, partAfterInputTag);
					String value = getParamValue(valueTag, partAfterInputTag);
					String synopsis = getParamValue(synopsisTag,
							partAfterInputTag);
					String length = getParamValue(lengthTag, partAfterInputTag);
					errorName = getParamValue(errorNameTag, partAfterInputTag);
					isRadioCheckText = true;

					// save question name in data bank
					dataBank.addNewQuestion(name);

					// save question info in data bank
					dataBank.addQuestionInfoLine("Question #"
							+ String.valueOf(dataBank.getNumQuestions())
							+ ": type=text; Name=" + name + ";");
					dataBank.addQuestionInfoLine("  length=" + length
							+ " characters; synopsis=" + synopsis);
					dataBank.addQuestionInfoLine("  errorName="
							+ escapeQuotes(errorName));

					// print out appropriate perl code
					surveyFormGenerator
							.append("print \"<input type=\\\"text\\\" name=\\\""
									+ name
									+ "\\\" length=\\\""
									+ length
									+ "\\\" value=\\\"$"
									+ name
									+ "\\\" >\\n\";\n");

					surveyTxtFileGenerator.append("print OUTFILE \"{{" + name
							+ ": " + escapeQuotes(synopsis) + "}}\\n\";\n");
					surveyTxtFileGenerator.append("print OUTFILE $" + name
							+ ";\n");
					surveyTxtFileGenerator
							.append("print OUTFILE \"\\n\\n\";\n");

					verificationGenerator.append("if($" + name
							+ " eq \"\" ) { \n");
					verificationGenerator.append("     $complete .= \"Answer ");
					if (errorName.equals("")) {
						verificationGenerator.append(name);
					} else {
						verificationGenerator.append(errorName);
					}
					verificationGenerator.append(".<br>\\n\";\n");
					verificationGenerator.append("}\n");

				} // done with text field

				//if not any of the above, than it's a "submit" or "reset"
				// button
				//so just save the line unchanged
				if (!isRadioCheckText) {
					surveyFormGenerator.append("print \""
							+ escapeQuotes(currentLine).trim() + "\\n\";\n");
				}

			} // done with <input> tag of any type

			//see if it's a textarea and, if so, deal with it
			Matcher findTextAreaTag = textAreaTag.matcher(currentLine);
			if (findTextAreaTag.find()) {
				isAFormTag = true;
				String partAfterTextAreaTag = currentLine
						.substring(findTextAreaTag.end());

				name = getParamValue(nameTag, partAfterTextAreaTag);
				String rows = getParamValue(rowsTag, partAfterTextAreaTag);
				String synopsis = getParamValue(synopsisTag,
						partAfterTextAreaTag);
				String cols = getParamValue(colsTag, partAfterTextAreaTag);
				errorName = getParamValue(errorNameTag, partAfterTextAreaTag);

				// save question name in data bank
				dataBank.addNewQuestion(name);

				// save question info in data bank
				dataBank.addQuestionInfoLine("Question #"
						+ String.valueOf(dataBank.getNumQuestions())
						+ ": type=textarea; Name=" + name + ";");
				dataBank.addQuestionInfoLine("  rows=" + rows + " lines; cols="
						+ cols + " characters; synopsis=" + synopsis);
				dataBank.addQuestionInfoLine("  errorName="
						+ escapeQuotes(errorName));

				// print out appropriate perl code
				surveyFormGenerator.append("print \"<textarea name=\\\"" + name
						+ "\\\" rows=\\\"" + rows + "\\\" cols=\\\"" + cols
						+ "\\\" >$" + name + "\\n\";\n");

				surveyTxtFileGenerator.append("print OUTFILE \"{{" + name
						+ ": " + escapeQuotes(synopsis) + "}}\\n\";\n");
				surveyTxtFileGenerator.append("print OUTFILE $" + name + ";\n");
				surveyTxtFileGenerator.append("print OUTFILE \"\\n\\n\";\n");

				verificationGenerator.append("if($" + name + " eq \"\" ) { \n");
				verificationGenerator.append("     $complete .= \"Answer ");
				if (errorName.equals("")) {
					verificationGenerator.append(name);
				} else {
					verificationGenerator.append(errorName);
				}
				verificationGenerator.append(".<br>\\n\";\n");
				verificationGenerator.append("}\n");

			} // done with textarea

			//see if its an end of checkbox set tag & deal with it
			Matcher findEndOfCheckBoxSetTag = endOfCheckBoxSetTag
					.matcher(currentLine);
			if (findEndOfCheckBoxSetTag.find()) {
				isAFormTag = true;
				if (checkBoxVerificationPending) {
					verificationGenerator.append("if ("
							+ checkBoxesToVerify.toString().substring(0,
									checkBoxesToVerify.length() - 5) + ") {\n");
					verificationGenerator
							.append("    $complete .= \"Check one of the box(es) for ");
					if (errorName.equals("")) {
						verificationGenerator.append(name);
					} else {
						verificationGenerator.append(escapeQuotes(errorName));
					}
					verificationGenerator.append(".<br>\\n\";\n");
					verificationGenerator.append("}\n");
					checkBoxVerificationPending = false;
					firstCheckBoxInSet = true;
					checkBoxesToVerify = new StringBuffer();
				}
			} // done with end of checkbox

			//see if its an "insert missing data report here" tag & deal with
			// it
			Matcher findMissingDataReportTag = missingDataReportTag
					.matcher(currentLine);
			if (findMissingDataReportTag.find()) {
				isAFormTag = true;
				surveyFormGenerator.append("<missingdatareport>");
			} // done with missing form data tag

			// default, if a normal html line
			if (!isAFormTag) {
				surveyFormGenerator.append("print \""
						+ escapeQuotes(currentLine).trim() + "\\n\";\n");
			}

			// done with this line
			i++;
		} // all done!

		//

		//display the result
		actionPane.setLayout(new BorderLayout());
		JTextArea questionInfoTextArea = new JTextArea(30, 50);
		questionInfoTextArea.setText(dataBank.getQuestionInfo());
		JScrollPane questionInfoScrollPane = new JScrollPane(
				questionInfoTextArea);
		actionPane.add(questionInfoScrollPane, BorderLayout.CENTER);
		actionPane.repaint();

	}

	// other methods

	public String getParamValue(Pattern paramNamePattern, String inputString) {
		Matcher findThisTag = paramNamePattern.matcher(inputString);
		String value = new String("");
		if (findThisTag.find()) {
			String partAfterThisTag = inputString.substring(findThisTag.end());
			value = extractNextParamValue(partAfterThisTag);
		}
		return value;
	}

	public String extractNextParamValue(String inputString) {
		String[] lineParts = inputString.split("\"");
		return lineParts[1];
	}

	public String escapeQuotes(String inputString) {
		String escInputString = inputString.replaceAll(
				Character.toString('\"'), "\\\\\"");
		return escInputString.replaceAll(Character.toString('\''), "\\\\\'");
	}

	public void cleanup(JPanel actionPane, JLabel agendaText, DataBank dataBank) {

		//take care of gui
		agendaText.removeAll();
		actionPane.removeAll();
		agendaText.repaint();
		actionPane.repaint();

		//send stuff up to databank
		dataBank.setSurveyFormPerlCode(surveyFormGenerator.toString());
		dataBank.setSurveyTxtFilePerlCode(surveyTxtFileGenerator.toString());
		dataBank.setErrorCheckCode(verificationGenerator.toString());

	}

}