/*
 * Chung Ying Yu cs681~3 Fall 2002- Spring 2003 Project VGL File:
 * ~cyyu/src/HelpFrame.java
 * 
 * @author Chung Ying Yu $Id: HelpFrame.java,v 1.1 2004-09-24 15:46:39 brian Exp $
 */

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpFrame extends JFrame {
	private JTextArea textArea;

	private JScrollPane areaScrollPane;

	public HelpFrame() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		textArea = new JTextArea();
		try {
			FileReader fr = new FileReader("UserGuide.txt");
			BufferedReader br = new BufferedReader(fr);
			String ss;
			String aa = "";
			while ((ss = br.readLine()) != null) {
				aa = aa + ss + "\n";
			}
			textArea.append(aa);
			fr.close();
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		textArea.setFont(new Font("Serif", Font.PLAIN, 12));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		areaScrollPane = new JScrollPane(textArea);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("img/Help.gif"));
		this.getContentPane().setLayout(null);
		this.setResizable(false);
		this.setTitle("VGL Configuration Help");
		this.setSize(new Dimension(500, 600));
		areaScrollPane.setBounds(new Rectangle(0, 0, 492, 565));
		this.getContentPane().add(areaScrollPane, null);
		areaScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	}
}