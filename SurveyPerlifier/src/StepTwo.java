
import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StepTwo { //select survey file for analysis

	JFileChooser inputSurveyFileChooser;

	public StepTwo() {
		inputSurveyFileChooser = new JFileChooser();
	}

	public void setup(JPanel actionPane, JLabel agendaText, DataBank dataBank) {
		agendaText
				.setText(Perlifier.agendaStart
						+ Perlifier.agendaStepOne
						+ "<li><font color=red>Select html survey file for conversion.</font></li>"
						+ Perlifier.agendaStepThree + Perlifier.agendaStepFour
						+ Perlifier.agendaStepFive + Perlifier.agendaEnd);
		agendaText.repaint();

		actionPane.setLayout(new BorderLayout());
		actionPane.add(inputSurveyFileChooser, BorderLayout.CENTER);
		inputSurveyFileChooser.setControlButtonsAreShown(false);
		if (!dataBank.getSurveyHtmlFileName().equals("")) {
			inputSurveyFileChooser.setSelectedFile(new File(dataBank
					.getSurveyHtmlFileName()));
		}
		actionPane.repaint();
	}

	public void cleanup(JPanel actionPane, JLabel agendaText, DataBank dataBank) {
		if (inputSurveyFileChooser.getSelectedFile() != null) {
			dataBank.setSurveyHtmlFileName(inputSurveyFileChooser
					.getSelectedFile().toString());
		}
		agendaText.removeAll();
		actionPane.removeAll();
		agendaText.repaint();
		actionPane.repaint();
	}

}