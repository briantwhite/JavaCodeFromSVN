
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StepOne {
	JTextField namesTxtField;

	JTextField surveyDirField;

	JTextField scriptURLField;

	JCheckBox prePostCheckBox;

	JTextField preSurveyNameField;

	JTextField postSurveyNameField;

	JTextField getnameNameField;

	JTextField surveyScriptNameField;

	JTextField startPreSurveyDateField;

	JTextField endPreSurveyDateField;

	JTextField startPostSurveyDateField;

	JTextField endPostSurveyDateField;

	public StepOne() {
		namesTxtField = new JTextField();
		surveyDirField = new JTextField();
		scriptURLField = new JTextField();
		prePostCheckBox = new JCheckBox("Pre and Post");
		preSurveyNameField = new JTextField();
		postSurveyNameField = new JTextField();
		getnameNameField = new JTextField();
		surveyScriptNameField = new JTextField();
		startPreSurveyDateField = new JTextField();
		endPreSurveyDateField = new JTextField();
		startPostSurveyDateField = new JTextField();
		endPostSurveyDateField = new JTextField();
	}

	public void setup(JPanel actionPane, JLabel agendaText, DataBank dataBank) {

		agendaText.setText(Perlifier.agendaStart
				+ "<li><font color=red>Enter setup information.</font></li>"
				+ Perlifier.agendaStepTwo + Perlifier.agendaStepThree
				+ Perlifier.agendaStepFour + Perlifier.agendaStepFive
				+ Perlifier.agendaEnd);
		agendaText.repaint();

		actionPane.setLayout(new GridLayout(12, 2));
		namesTxtField.setText(dataBank.namesTxt);
		surveyDirField.setText(dataBank.surveyDir);
		scriptURLField.setText(dataBank.scriptURL);
		prePostCheckBox.setSelected(dataBank.prePost);
		preSurveyNameField.setText(dataBank.preSurveyHeadline);
		postSurveyNameField.setText(dataBank.postSurveyHeadline);
		getnameNameField.setText(dataBank.getnamePlName);
		surveyScriptNameField.setText(dataBank.surveyPlName);
		startPreSurveyDateField.setText(dataBank.startPreSurveyDate);
		endPreSurveyDateField.setText(dataBank.endPreSurveyDate);
		startPostSurveyDateField.setText(dataBank.startPostSurveyDate);
		endPostSurveyDateField.setText(dataBank.endPostSurveyDate);

		actionPane
				.add(new JLabel("Path to file of names & ID#s:", JLabel.RIGHT));
		actionPane.add(namesTxtField);
		actionPane.add(new JLabel("Path to survey directory:", JLabel.RIGHT));
		actionPane.add(surveyDirField);
		actionPane.add(new JLabel("Base URL of survey scripts:", JLabel.RIGHT));
		actionPane.add(scriptURLField);
		actionPane.add(new JLabel("Pre survey only or pre & post?",
				JLabel.RIGHT));
		actionPane.add(prePostCheckBox);
		actionPane
				.add(new JLabel(
						"<html>Title for Pre Survey:<br>"
								+ "<font size=-2 color=blue>(Title if no post-survey.)</font></html>",
						JLabel.RIGHT));
		actionPane.add(preSurveyNameField);
		actionPane
				.add(new JLabel(
						"Title for Post Survey:<br>"
								+ "<font size=-2 color=blue>(Ignore if no post-survey.)</font></html>",
						JLabel.RIGHT));
		actionPane.add(postSurveyNameField);
		actionPane.add(new JLabel("Name of login script:", JLabel.RIGHT));
		actionPane.add(getnameNameField);
		actionPane.add(new JLabel("Name of Survey script:", JLabel.RIGHT));
		actionPane.add(surveyScriptNameField);
		actionPane
				.add(new JLabel(
						"<html>Start date for pre survey:<br>"
								+ "<font size=-2 color=blue>(Start date if no post-survey.)</font></html>",
						JLabel.RIGHT));
		actionPane.add(startPreSurveyDateField);
		actionPane
				.add(new JLabel(
						"<html>End date for pre survey:<br>"
								+ "<font size=-2 color=blue>(Ignore if no post-survey.)</font></html>",
						JLabel.RIGHT));
		actionPane.add(endPreSurveyDateField);
		actionPane
				.add(new JLabel(
						"<html>Start date for post survey:<br>"
								+ "<font size=-2 color=blue>(Ignore if no post-survey.)</font></html>",
						JLabel.RIGHT));
		actionPane.add(startPostSurveyDateField);
		actionPane
				.add(new JLabel(
						"<html>End date for post survey:<br>"
								+ "<font size=-2 color=blue>(End date if no post-survey.)</font></html>",
						JLabel.RIGHT));
		actionPane.add(endPostSurveyDateField);

		actionPane.repaint();
	}

	public void cleanup(JPanel actionPane, JLabel agendaText, DataBank dataBank) {

		dataBank.namesTxt = namesTxtField.getText();
		dataBank.surveyDir = surveyDirField.getText();
		dataBank.scriptURL = scriptURLField.getText();
		dataBank.prePost = prePostCheckBox.isSelected();
		dataBank.preSurveyHeadline = preSurveyNameField.getText();
		dataBank.postSurveyHeadline = postSurveyNameField.getText();
		dataBank.getnamePlName = getnameNameField.getText();
		dataBank.surveyPlName = surveyScriptNameField.getText();
		agendaText.removeAll();
		actionPane.removeAll();
		agendaText.repaint();
		actionPane.repaint();
	}

}