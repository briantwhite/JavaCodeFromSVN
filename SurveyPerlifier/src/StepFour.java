
import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StepFour {

	JFileChooser outputDirChooser;

	public StepFour() {
		outputDirChooser = new JFileChooser();
	}

	public void setup(JPanel actionPane, JLabel agendaText, DataBank dataBank) {
		agendaText
				.setText(Perlifier.agendaStart
						+ Perlifier.agendaStepOne
						+ Perlifier.agendaStepTwo
						+ Perlifier.agendaStepThree
						+ "<li><font color=red>Select directory for output files.</font></li>"
						+ Perlifier.agendaStepFive + Perlifier.agendaEnd);
		agendaText.repaint();

		actionPane.setLayout(new BorderLayout());
		actionPane.add(outputDirChooser, BorderLayout.CENTER);
		outputDirChooser.setControlButtonsAreShown(false);
		outputDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		outputDirChooser.setCurrentDirectory(new File(dataBank.outputDir));
		outputDirChooser.setSelectedFile(new File(dataBank.outputDir));
		actionPane.repaint();

	}

	public void cleanup(JPanel actionPane, JLabel agendaText, DataBank dataBank) {
		dataBank.outputDir = outputDirChooser.getCurrentDirectory().toString();
		agendaText.removeAll();
		actionPane.removeAll();
		agendaText.repaint();
		actionPane.repaint();

	}

}