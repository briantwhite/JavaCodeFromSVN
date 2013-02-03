package protex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

	public MiniProtex(final ProblemSpecification problemSpec) {
				
		Protex protex = new Protex(true);
		final FoldingWindow foldingWindow = new FoldingWindow("Folding Window", protex);
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

		JButton submitButton = new JButton("Submit Answer");
		leftPanel.add(submitButton);
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkAndSubmitAnswer(foldingWindow, problemSpec);
			}
		});
		

		mainPanel.add(leftPanel);
		mainPanel.add(foldingWindow);

		add(mainPanel);
		pack();
	}

	private void checkAndSubmitAnswer(FoldingWindow foldingWindow, ProblemSpecification problemSpec) {
		
		// check for errors
		if ((problemSpec.targetShapeString == null) || (problemSpec.targetShapeString.equals(""))) {
			JOptionPane.showMessageDialog(null, "ERROR: No target shape has been specified.");
			return;
		}
		if (foldingWindow.getOutputPalette().getBackground().equals(Color.PINK)) {
			JOptionPane.showMessageDialog(null, "ERROR: The protein sequence you typed in \nhas not been folded. Click the \nFOLD button and re-submit.");
			return;
		}
		if (foldingWindow.getOutputPalette().getDrawingPane().getGrid() == null) {
			JOptionPane.showMessageDialog(null, "ERROR: There is no folded protein to check.\nPlease enter a protein and try again.");
			return;
		}
		
		// ok to submit
		String result = "INCORRECT";
		ShapeMatcher shapeMatcher = 
				new ShapeMatcher(problemSpec.targetShapeString, false);
		if (shapeMatcher.matchesTarget(
				foldingWindow.getOutputPalette().getDrawingPane().getGrid().getPP().getDirectionSequence())) {
			result = "CORRECT";
		} 

		// server communication
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		String csrftoken = null;
		URL url = null;
		try {
			url = new URL(problemSpec.edXCookieURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		if (url != null) {
			try {
				// you need to contact once to get the header to get the csrftoken "cookie"
				HttpURLConnection firstConnection = (HttpURLConnection)url.openConnection();
				Map<String, List<String>> headerFields = firstConnection.getHeaderFields();
				List<String> cookies = headerFields.get("Set-Cookie");
				if (cookies != null) {
					Iterator <String> sIt = cookies.iterator();
					while (sIt.hasNext()) {
						String s = sIt.next();
						if (s.startsWith("csrftoken")) {
							String part = s.split(";")[0];
							csrftoken = part.split("=")[1];
							//								System.out.println(csrftoken);
						}
					}
				}
				firstConnection.disconnect();

				if (csrftoken == null) {
					JOptionPane.showMessageDialog(this, "Could not access server");
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			// now login 
			boolean loginSuccess = false;
			if (csrftoken != null) {
				try {
					url = new URL(problemSpec.edXLoginURL);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				if (url != null) {
					HttpURLConnection secondConnection;
					try {
						secondConnection = (HttpURLConnection)url.openConnection();
						secondConnection.setDoInput(true);
						secondConnection.setDoOutput(true); // make it a POST
						secondConnection.setUseCaches(false);
						secondConnection.setRequestProperty("X-CSRFToken", csrftoken);
						secondConnection.setRequestProperty("Referer", problemSpec.edXCookieURL);

						DataOutputStream output = new DataOutputStream(secondConnection.getOutputStream());

						PasswordVault pv = PasswordVault.getInstance();
						if ((pv.email == null) || (pv.password == null)) {
							String[] emailAndPswd = PasswordDialog.getEmailAndPassword();
							pv.email = emailAndPswd[0];
							pv.password = emailAndPswd[1];
						}
						
						if ((pv.email == null) || (pv.password == null)) return;
						
						String content = 
								"email=" + URLEncoder.encode(pv.email, "UTF-8") 
								+ "&password=" + URLEncoder.encode(pv.password, "UTF-8")
								+ "&remember=" + URLEncoder.encode("false", "UTF-8");

						output.writeBytes(content);
						output.flush();
						output.close();

						String response = null;
						BufferedReader input = new BufferedReader(
								new InputStreamReader(
										new DataInputStream(secondConnection.getInputStream())));
						while (null != ((response = input.readLine()))) {
							if (response.contains("{\"success\": true}")) {
								loginSuccess = true;
								break;
							}
						}
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			if (!loginSuccess) {
				JOptionPane.showMessageDialog(this, "Login Failed");
				return;					
			}
			
			// now, submit it
			try {
				url = new URL(problemSpec.edXSubmissionURL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			
			StringBuffer b = new StringBuffer();
			if (url != null) {
				HttpURLConnection secondConnection;
				try {
					secondConnection = (HttpURLConnection)url.openConnection();
					secondConnection.setDoInput(true);
					secondConnection.setDoOutput(true); // make it a POST
					secondConnection.setUseCaches(false);
					secondConnection.setRequestProperty("X-CSRFToken", csrftoken);
					secondConnection.setRequestProperty("Referer",problemSpec.edXCookieURL);

					DataOutputStream output = new DataOutputStream(secondConnection.getOutputStream());

					String content = clean(problemSpec.edXLocation) + "=" + result;

					output.writeBytes(content);
					output.flush();
					output.close();

					String response = null;
					BufferedReader input = new BufferedReader(
							new InputStreamReader(
									new DataInputStream(secondConnection.getInputStream())));
					while (null != ((response = input.readLine()))) {
						b.append(response + "\n");
					}
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(this, "Submission Received by EdX Server\n" + result);
		}		
	}
	
	private String clean(String s) {
		return s.replace("/","-").replace(":","").replace("--","-").replace(".", "_");
	}

}
