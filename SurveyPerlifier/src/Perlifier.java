
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Perlifier {

	// strings for making the agenda items
	static final String agendaStart = new String("<html><ol>");

	static final String agendaStepOne = new String(
			"<li>Enter setup information.</li>");

	static final String agendaStepTwo = new String(
			"<li>Select html survey file for conversion.</li>");

	static final String agendaStepThree = new String(
			"<li>Review list of survey questions.</li>");

	static final String agendaStepFour = new String(
			"<li>Select directory for output files.</li>");

	static final String agendaStepFive = new String("<li>All Done.</li>");

	static final String agendaEnd = new String("</ol></html>");

	// the steps in the process
	StepOne stepOne;

	StepTwo stepTwo;

	StepThree stepThree;

	StepFour stepFour;

	StepFive stepFive;

	int currentStep;

	// where the data are stored
	DataBank dataBank;

	public Perlifier() { //constructor

		stepOne = new StepOne();
		stepTwo = new StepTwo();
		stepThree = new StepThree();
		stepFour = new StepFour();
		stepFive = new StepFive();

		currentStep = 1;

		// where the data are stored
		dataBank = new DataBank();

	}

	public static void main(String[] args) {

		final Perlifier perlifier = new Perlifier();

		// the GUI frame
		final JFrame masterFrame = new JFrame(
				"Convert html surveys to perl scripts");
		final JLabel agendaText = new JLabel("");
		final JPanel actionPanel = new JPanel();
		final JPanel buttonPanel = new JPanel();
		JButton nextButton = new JButton("Next ->");
		JButton backButton = new JButton("<- Back");
		JButton quitButton = new JButton("Quit");
		buttonPanel.add(quitButton);
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);

		perlifier.stepOne.setup(actionPanel, agendaText, perlifier.dataBank);

		masterFrame.setBounds(0, 0, 900, 500);
		masterFrame.getContentPane().add(agendaText, BorderLayout.WEST);
		masterFrame.getContentPane().add(actionPanel, BorderLayout.CENTER);
		masterFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		masterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		masterFrame.setVisible(true);

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (perlifier.currentStep) {
				case 1:
					perlifier.stepOne.cleanup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.stepTwo.setup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.currentStep++;
					break;

				case 2:
					perlifier.stepTwo.cleanup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.stepThree.setup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.currentStep++;
					break;

				case 3:
					perlifier.stepThree.cleanup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.stepFour.setup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.currentStep++;
					break;

				case 4:
					perlifier.stepFour.cleanup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.stepFive.setup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.currentStep++;
					break;

				case 5:
					break;

				}

			}
		});

		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (perlifier.currentStep) {
				case 1:
					break;

				case 2:
					perlifier.stepTwo.cleanup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.stepOne.setup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.currentStep--;
					break;

				case 3:
					perlifier.stepThree.cleanup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.stepTwo.setup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.currentStep--;
					break;

				case 4:
					perlifier.stepFour.cleanup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.stepThree.setup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.currentStep--;
					break;

				case 5:
					perlifier.stepFive.cleanup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.stepFour.setup(actionPanel, agendaText,
							perlifier.dataBank);
					perlifier.currentStep--;
					break;

				}

			}
		});

		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

}