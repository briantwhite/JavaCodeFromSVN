package protex;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MiniProtex extends JFrame {

	/**
	 * This is for running via java web start and a CMS like edx
	 * 
	 * @param -targetShapeString= the target shape sequence of directions
	 * 			replace all ";" with "#"
	 * 			replace spaces with "_"
	 * @param -edXCookieURL= url to get the session cookie from
	 * @param -edXLoginURL= url for login
	 * @param -edXSubmissionURL= url for submitting the result
	 * @param -edXLocation= location to save result on cms
	 * 
	 */
	public static void main(String[] args) {
		ProblemSpecification problemSpec = new ProblemSpecification();
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.startsWith("-")) {
				String trimmedArg = arg.substring(1); // remove "-"
				String[] parts = trimmedArg.split("=");
				problemSpec = updateProblemSpec(problemSpec, parts[0], parts[1]);
			}
		}
		
		if (problemSpec.complete) {
			MiniProtex miniProtex = new MiniProtex(problemSpec);
			miniProtex.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "Error: not all parameters specified");
		}
	}

	private static ProblemSpecification updateProblemSpec(ProblemSpecification problemSpec, String paramName, String paramValue) {
		if (paramName.equals("targetShapeString")) problemSpec.setTargetShapeString(paramValue.replaceAll("_", " ").replaceAll("#", ";"));
		if (paramName.equals("edXCookieURL")) problemSpec.setEdXCookieURL(paramValue);
		if (paramName.equals("edXLoginURL")) problemSpec.setEdXLoginURL(paramValue);
		if (paramName.equals("edXSubmissionURL")) problemSpec.setEdXSubmissionURL(paramValue);
		if (paramName.equals("edXLocation")) problemSpec.setEdXLocation(paramValue);
		return problemSpec;
	}

	public MiniProtex(ProblemSpecification problemSpec) {
		Protex protex = new Protex(true);
		FoldingWindow foldingWindow = new FoldingWindow("Folding Window", protex);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

		JPanel aapPanel = new JPanel();
		aapPanel.setBorder(BorderFactory.createTitledBorder("Amino acids"));
		aapPanel.setLayout(new BoxLayout(aapPanel, BoxLayout.X_AXIS));
		AminoAcidPalette aaPalette = new AminoAcidPalette(225, 180, 4, 5, false);
		aapPanel.setMaximumSize(new Dimension(250, 200));
		aapPanel.add(aaPalette);
		aapPanel.add(Box.createRigidArea(new Dimension(1,180)));
		leftPanel.add(aapPanel);


		ProteinImageSet pis = ProteinImageFactory.buildProtein(problemSpec.targetShapeString, false);
		JPanel targetPanel = new JPanel();
		targetPanel.setBorder(BorderFactory.createTitledBorder("Target Shape"));
		targetPanel.add(new JLabel(new ImageIcon(pis.getFullScaleImage())));
		leftPanel.add(targetPanel);

		mainPanel.add(leftPanel);
		mainPanel.add(foldingWindow);

		add(mainPanel);
		pack();

	}

}
