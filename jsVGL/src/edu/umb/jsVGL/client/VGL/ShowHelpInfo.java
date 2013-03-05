package edu.umb.jsVGL.client.VGL;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class ShowHelpInfo {
	
	static void showHelpInfo(VGLII vglII) {
		final JEditorPane helpPane = new JEditorPane();
		helpPane.setEditable(false);
		helpPane.setContentType("text/html"); //$NON-NLS-1$

		try {
			helpPane.setPage(VGLII.class.getResource(
					Messages.getInstance().getInstance().getString("VGLII.HelpFileDir") 
					+ "/"
					+ "index.html")); //$NON-NLS-1$
		} catch (Exception e) {
			JOptionPane.showMessageDialog(vglII,
					Messages.getInstance().getInstance().getString("VGLII.HelpWarning1"), //$NON-NLS-1$
					Messages.getInstance().getInstance().getString("VGLII.HelpWarning2"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			return;
		}

		JScrollPane helpScrollPane = new JScrollPane(helpPane);
		JDialog helpDialog = new JDialog(vglII, Messages.getInstance().getInstance().getString("VGLII.HelpPage")); //$NON-NLS-1$
		JButton backButton = new JButton(Messages.getInstance().getInstance().getString("VGLII.ReturnToTop")); //$NON-NLS-1$
		helpDialog.getContentPane().setLayout(new BorderLayout());
		helpDialog.getContentPane().add(backButton, BorderLayout.NORTH);
		helpDialog.getContentPane().add(helpScrollPane, BorderLayout.CENTER);
		Dimension screenSize = vglII.getToolkit().getScreenSize();
		helpDialog.setBounds((screenSize.width / 8), (screenSize.height / 8),
				(screenSize.width * 8 / 10), (screenSize.height * 8 / 10));
		helpDialog.setVisible(true);

		helpPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						helpPane.setPage(e.getURL());
					} catch (IOException ioe) {
						System.err.println(ioe.toString());
					}
				}
			}

		});

		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					helpPane.setPage(VGLII.class.getResource(
							Messages.getInstance().getInstance().getString("VGLII.HelpFileDir") 
							+ System.getProperty("file.separator")
							+ "index.html")); //$NON-NLS-1$
				} catch (Exception e) {
					System.err
					.println(Messages.getInstance().getInstance().getString("VGLII.HelpWarning3") + e.toString()); //$NON-NLS-1$
				}
			}
		});

	}

}
