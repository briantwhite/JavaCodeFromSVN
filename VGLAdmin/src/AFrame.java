/**
 * Chung Ying Yu cs681~3 Fall 2002- Spring 2003 Project VGL File: AFrame.java
 * 
 * @author Chung Ying Yu $Id: AFrame.java,v 1.1 2004-09-24 15:46:40 brian Exp $
 *         This program is free software; you can redistribute it and/or modify
 *         it under the terms of the GNU General Public License as published by
 *         the Free Software Foundation; either version 2 of the License, or (at
 *         your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.TimeZone;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class AFrame extends JFrame {
	private JMenuBar jMenuBar = new JMenuBar();

	private JMenu menu = new JMenu("File");

	private JMenuItem item0 = new JMenuItem("New problem");

	private JMenuItem item1 = new JMenuItem("Open problem");

	private JMenuItem item2 = new JMenuItem("Save problem");

	private JMenuItem item3 = new JMenuItem("Exit");

	private JMenu menu2 = new JMenu("Edit");

	private JMenuItem item4 = new JMenuItem("Clear");

	private JMenu menu3 = new JMenu("Help");

	private JMenuItem item5 = new JMenuItem("Help Topics");

	private JMenuItem item6 = new JMenuItem("About");

	//private ImgPanel contentPane;
	private JPanel contentPane = new JPanel();
	
	private JLabel lbl2alleles = new JLabel();

	private JLabel lblsd = new JLabel();

	private JLabel lblcd = new JLabel();
	
	private JLabel lbl3alleles = new JLabel();
	
	private JLabel lblhd = new JLabel();

	private JLabel lblsl = new JLabel();

	private JLabel lblau = new JLabel();

	private JLabel lblxx = new JLabel();

	private JLabel lblzz = new JLabel();

	private JCheckBox chkpm = new JCheckBox();

	private JSlider jSlider1 = new JSlider();  //co-incomplete dominance

	private JSlider jSlider2 = new JSlider();  //sex-linkage

	private JSlider jSlider3 = new JSlider();  //sex-linkage type
	
	private JSlider jSlider4 = new JSlider();  //2 vs 3 allele slider
	
	private JSlider jSlider5 = new JSlider();  //hierarchical vs circular dominance

	private JTextField jTextField1 = new JTextField();

	private JTextField jTextField2 = new JTextField();

	private JTextField jTextField3 = new JTextField();

	private JLabel jLabel1 = new JLabel();

	private JLabel jLabel2 = new JLabel();

	private JLabel jLabel3 = new JLabel();

	private JLabel jLabel4 = new JLabel();

	private JLabel jLabel5 = new JLabel();

	private JLabel jLabel6 = new JLabel();

	private Hashtable labelTable1 = new Hashtable();

	private Hashtable labelTable2 = new Hashtable();

	private Hashtable labelTable3 = new Hashtable();
	
	private JPanel jpanel0 = new JPanel();   //top panel for alleles

	private JPanel jPanel1 = new JPanel();   //middle panel for dominance

	private JPanel jPanel2 = new JPanel();   //bottom panel for sex-linkage

	private JToolBar toolbar = new JToolBar();

	private File selFile;

	private URL openURL = Admin.class.getResource("img/Open.gif");
	private JButton jButton1 = new JButton(new ImageIcon(openURL));

	private URL saveURL = Admin.class.getResource("img/Save.gif");
	private JButton jButton2 = new JButton(new ImageIcon(saveURL));

	private URL clearURL = Admin.class.getResource("img/Clear.gif");
	private JButton jButton3 = new JButton(new ImageIcon(clearURL));

	private URL helpURL = Admin.class.getResource("img/Help.gif");
	private JButton jButton4 = new JButton(new ImageIcon(helpURL));

	private AData data;

	private XWriter xwriter;

	private XReader xreader;

	private Image logo;

	private JOptionPane pane = new JOptionPane();

	/**
	 * Construct the frame
	 */
	public AFrame() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Components initialization
	 */
	private void jbInit() throws Exception {
		// setup the basic panel and the image
		//Image img =
		// Toolkit.getDefaultToolkit().getImage("img/background.gif");
		//contentPane = new ImgPanel(img);
		contentPane.setLayout(null);
		logo = Toolkit.getDefaultToolkit().getImage("img/logo.gif");
		this.setIconImage(logo);
		this.setSize(new Dimension(317, 600));
		this.setTitle("Virtual Genetics Lab Admin");
		contentPane.setFont(new java.awt.Font("Dialog", 0, 9));
		contentPane.setBounds(new Rectangle(0, 0, 316, 595));

		toolbar.setBounds(new Rectangle(0, 0, 160, 40));
		this.getContentPane().add(contentPane, null);

		// initial components
		setComponents();
		// add menu
		jMenuBar.add(menu);
		jMenuBar.add(menu2);
		jMenuBar.add(menu3);
		menu.add(item0);
		menu.add(item1);
		menu.add(item2);
		menu.add(item3);
		menu2.add(item4);
		menu3.add(item5);
		menu3.add(item6);

		// add components to the content pane
		this.setJMenuBar(jMenuBar);
		contentPane.add(toolbar, null);
		// add components to jPanel1
		jPanel1.setLayout(null);
		jPanel1.add(lbl2alleles, null); // 2 alleles
		jPanel1.add(lblcd, null);       //"co-incomplete dominance"
		jPanel1.add(lblsd, null);       //"simple dominance"
		jPanel1.add(lbl3alleles, null); // "3 alleles"
		jPanel1.add(lblhd, null);		  //"hierarchical dom"
		jPanel1.add(jTextField1, null); //value of "dominance"
		jPanel1.add(jLabel2, null);     //"100"
		jPanel1.add(jLabel1, null);     //"0"
		jPanel1.add(jSlider1, null);    // co-incomplete dominance slider
		jPanel1.add(jSlider5, null);    // hierarchical/circular dom slider
		contentPane.add(chkpm, null);   //practice mode checkbox
		// add components to jPanel2
		jPanel2.setLayout(null);
		jPanel2.add(lblzz, null);
		jPanel2.add(lblxx, null);
		jPanel2.add(lblau, null);
		jPanel2.add(lblsl, null);
		jPanel2.add(jSlider2, null);
		jPanel2.add(jSlider3, null);
		jPanel2.add(jTextField3, null);
		jPanel2.add(jTextField2, null);
		jPanel2.add(jLabel3, null);
		jPanel2.add(jLabel4, null);
		jPanel2.add(jLabel5, null);
		jPanel2.add(jLabel6, null);
		// add components to toolbar
		toolbar.setFloatable(false);
		toolbar.add(jButton1, null);
		toolbar.add(jButton2, null);
		toolbar.add(jButton3, null);
		toolbar.add(jButton4, null);

		contentPane.add(jPanel2, null);
		contentPane.add(jPanel1, null);

		jPanel2.setBounds(new Rectangle(2, 250, 306, 173));
		jPanel1.setBounds(new Rectangle(2, 40, 307, 200));
	}

	/**
	 * Overridden so we can exit when window is closed
	 * 
	 * @param e
	 *            WindowEvent
	 */
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		}
	}

	/**
	 * set all the components values
	 */
	public void setComponents() {
		jButton4.setPreferredSize(new Dimension(20, 20));
		jPanel1.add(lbl2alleles, null);
		jPanel1.add(lbl3alleles, null);
		jPanel1.add(lblcd, null);
		jPanel1.add(lblsd, null);
		jPanel1.add(lblhd, null);
		jPanel1.add(jTextField1, null);
		jPanel1.add(jLabel2, null);
		jPanel1.add(jLabel1, null);
		jPanel1.add(jSlider1, null);
		jPanel1.add(jSlider5, null);
		jPanel2.add(lblzz, null);
		jPanel2.add(lblxx, null);
		jPanel2.add(lblau, null);
		jPanel2.add(lblsl, null);
		jPanel2.add(jSlider2, null);
		jPanel2.add(jSlider3, null);
		jPanel2.add(jTextField3, null);
		jPanel2.add(jTextField2, null);
		jPanel2.add(jLabel3, null);
		jPanel2.add(jLabel4, null);
		jPanel2.add(jLabel5, null);
		jPanel2.add(jLabel6, null);
		pane.setIcon(new ImageIcon(logo));
		jPanel1.setFont(new java.awt.Font("Dialog", 1, 9));
		jPanel2.setFont(new java.awt.Font("Dialog", 1, 9));

		jButton1.setToolTipText("Open problem");
		jButton1.setBorderPainted(true);
		jButton1.setOpaque(false);
		jButton1.setContentAreaFilled(false);

		jButton2.setToolTipText("Save problem ");
		jButton2.setBorderPainted(true);
		jButton2.setOpaque(false);
		jButton2.setContentAreaFilled(false);

		jButton3.setToolTipText("Clear");
		jButton3.setBorderPainted(true);
		jButton3.setContentAreaFilled(false);

		jButton4.setToolTipText("Help");
		jButton4.setBorderPainted(true);
		jButton4.setContentAreaFilled(false);

		jSlider1.setFont(new java.awt.Font("Dialog", 1, 9));
		jSlider1.setBounds(new Rectangle(174, 41, 120, 50));

		jSlider2.setFont(new java.awt.Font("Dialog", 0, 9));
		jSlider2.setBounds(new Rectangle(174, 41, 120, 50));

		jSlider3.setBounds(new Rectangle(174, 116, 120, 50));
		
		jSlider5.setFont(new java.awt.Font("Dialog", 0, 9));
		jSlider5.setBounds(new Rectangle(174, 116, 120, 50));

		jTextField1.setFont(new java.awt.Font("Dialog", 0, 9));
		jTextField1.setBounds(new Rectangle(218, 21, 30, 20));

		jTextField2.setFont(new java.awt.Font("Dialog", 0, 9));
		jTextField2.setBounds(new Rectangle(218, 21, 30, 20));

		jTextField3.setFont(new java.awt.Font("Dialog", 0, 9));
		jTextField3.setBounds(new Rectangle(218, 97, 30, 20));

		jLabel1.setFont(new java.awt.Font("Dialog", 0, 9));
		jLabel1.setBounds(new Rectangle(178, 21, 24, 20));
		jLabel1.setForeground(Color.black);

		jLabel2.setFont(new java.awt.Font("Dialog", 0, 9));
		jLabel2.setBounds(new Rectangle(273, 21, 25, 20));
		jLabel2.setForeground(Color.black);

		jLabel3.setFont(new java.awt.Font("Dialog", 0, 9));
		jLabel3.setBounds(new Rectangle(178, 21, 24, 20));
		jLabel3.setForeground(Color.black);

		jLabel4.setFont(new java.awt.Font("Dialog", 0, 9));
		jLabel4.setBounds(new Rectangle(273, 21, 25, 20));
		jLabel4.setForeground(Color.black);

		jLabel5.setFont(new java.awt.Font("Dialog", 0, 9));
		jLabel5.setBounds(new Rectangle(178, 97, 24, 20));
		jLabel5.setForeground(Color.black);

		jLabel6.setFont(new java.awt.Font("Dialog", 0, 9));
		jLabel6.setBounds(new Rectangle(273, 97, 24, 20));
		jLabel6.setForeground(Color.black);

		jLabel1.setText("0");
		jLabel2.setText("100");
		jLabel3.setText("0");
		jLabel4.setText("100");
		jLabel5.setText("0");
		jLabel6.setText("100");
		
		lbl2alleles.setBounds(new Rectangle(16, 10, 156, 22));
		lbl2alleles.setFont(new java.awt.Font("Dialog", 0, 12));
		lbl2alleles.setForeground(Color.red);
		lbl2alleles.setText("For 2-allele models");

		lblcd.setBounds(new Rectangle(16, 62, 156, 22));
		lblcd.setFont(new java.awt.Font("Dialog", 0, 11));
		lblcd.setForeground(Color.black);
		lblcd.setText("co/incomplete dominance");

		lblsd.setBounds(new Rectangle(16, 22, 155, 22));
		lblsd.setFont(new java.awt.Font("Dialog", 0, 11));
		lblsd.setForeground(Color.black);
		lblsd.setText("simple dominance");
		
		lblhd.setBounds(new Rectangle(16, 137, 155, 22));
		lblhd.setFont(new java.awt.Font("Dialog", 0, 11));
		lblhd.setForeground(Color.black);
		lblhd.setText("hierarchical dominance");		

		lblzz.setBounds(new Rectangle(16, 137, 175, 22));
		lblzz.setFont(new java.awt.Font("Dialog", 0, 11));
		lblzz.setForeground(Color.black);
		lblzz.setText("ZZ (female) & ZW (male)");

		lblsl.setBounds(new Rectangle(16, 62, 175, 22));
		lblsl.setFont(new java.awt.Font("Dialog", 0, 11));
		lblsl.setForeground(Color.black);
		lblsl.setText("sex-linked");

		lblxx.setBounds(new Rectangle(16, 97, 156, 22));
		lblxx.setFont(new java.awt.Font("Dialog", 0, 11));
		lblxx.setForeground(Color.black);
		lblxx.setText(" XX (male) & XY (female)");

		lblau.setBounds(new Rectangle(16, 22, 107, 22));
		lblau.setFont(new java.awt.Font("Dialog", 0, 11));
		lblau.setForeground(Color.black);
		lblau.setText("autosomal");

		sliderSetting(jSlider1, labelTable1, jTextField1);
		sliderSetting(jSlider2, labelTable2, jTextField2);
		sliderSetting(jSlider3, labelTable3, jTextField3);

		chkpm.setFont(new java.awt.Font("Dialog", 0, 11));
		chkpm.setOpaque(false);
		chkpm.setForeground(Color.black);
		chkpm.setText("Practice Mode");
		chkpm.setBounds(new Rectangle(5, 314, 200, 20));

		jButton1.addActionListener(new OpenListener());
		jButton2.addActionListener(new SaveListener());
		jButton3.addActionListener(new ClearListener());
		jButton4.addActionListener(new HelpListener());

		jTextField1.setText("50");
		jTextField1.addFocusListener(new TextValueListener1(jSlider1));
		jTextField1.addActionListener(new TextValueListener2(jSlider1));
		jTextField2.setText("50");
		jTextField2.addFocusListener(new TextValueListener1(jSlider2));
		jTextField2.addActionListener(new TextValueListener2(jSlider2));
		jTextField3.setText("50");
		jTextField3.addFocusListener(new TextValueListener1(jSlider3));
		jTextField3.addActionListener(new TextValueListener2(jSlider3));

		jPanel1.setBorder(new TitledBorder("Dominance"));
		jPanel2.setBorder(new TitledBorder("Sex Linkage"));
		jPanel1.setOpaque(false);
		jPanel1.setForeground(Color.black);
		jPanel2.setOpaque(false);
		jPanel2.setForeground(Color.black);

		item0.addActionListener(new NewListener());
		item1.addActionListener(new OpenListener());
		item2.addActionListener(new SaveListener());
		item3.addActionListener(new ExitListener());
		item4.addActionListener(new ClearListener());
		item5.addActionListener(new HelpListener());
		item6.addActionListener(new AboutListener());
	}

	/**
	 * Setup the slider with the hastable label and text
	 * 
	 * @param js
	 *            setup this slider
	 * @param ht
	 *            the Hashtable use to paint the ticks
	 * @param jt
	 *            the JTextField to interact with the slider value
	 */
	public void sliderSetting(JSlider js, Hashtable ht, JTextField jt) {
		JLabel j1 = new JLabel("0");
		JLabel j2 = new JLabel("100");
		j1.setFont(new java.awt.Font("Dialog", 0, 9));
		j2.setFont(new java.awt.Font("Dialog", 0, 9));
		j1.setForeground(Color.black);
		j2.setForeground(Color.black);
		js.addChangeListener(new SliderListener(jt));
		js.putClientProperty("JSlider.isFilled", Boolean.TRUE);
		js.setMajorTickSpacing(50);
		js.setMinorTickSpacing(25);
		ht.put(new Integer(100), j1);
		ht.put(new Integer(0), j2);
		js.setLabelTable(ht);
		// don't want to paint the trick beacuse after load the image, it is
		// quite annoy
		// js.setPaintTicks(true);
		js.setPaintLabels(true);
		js.setOpaque(false);
	}

	/**
	 * Set the whole slider disable When the sexLinkage is selected to 0,
	 * disable jSilder3 selections
	 */
	public void disableSliderSet() {
		lblxx.setEnabled(false);
		lblzz.setEnabled(false);
		jTextField3.setEnabled(false);
		jLabel5.setEnabled(false);
		jLabel6.setEnabled(false);
		jSlider3.setEnabled(false);
		jSlider3.setPaintLabels(false);
	}

	/**
	 * The methood use let the whole slider set enable When the sexLinkage is
	 * not selected to 0, enable jSilder3 selections
	 */
	public void enableSliderSet() {
		lblxx.setEnabled(true);
		lblzz.setEnabled(true);
		jTextField3.setEnabled(true);
		jLabel5.setEnabled(true);
		jLabel6.setEnabled(true);
		jSlider3.setEnabled(true);
		jSlider3.setPaintLabels(true);
	}

	/**
	 * The method to collect all the data for saving
	 * 
	 * @param output
	 *            contain the data information
	 * @param problemname
	 *            save file's name
	 */
	public void collectData(FileOutputStream output, String problemname) {
		data = new AData();
		xwriter = new XWriter();
		data.setDominance(jTextField1.getText());
		data.setSexlinked(jTextField2.getText());
		data.setChance(jTextField3.getText());
		// get current time
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getDefault());

		if (chkpm.isSelected())
			data.setPracticemode("1");
		else
			data.setPracticemode("0");

		xwriter.createXML(data, output);
	}

	/**
	 * The method to read the data from xml file
	 * 
	 * @param input
	 *            read the data from FileInputStream
	 */
	public void showData(FileInputStream input) {
		data = new AData();
		xreader = new XReader();
		xreader.setData(data, input);
		if ((Integer.parseInt(data.getDominance()) >= 0)
				&& (Integer.parseInt(data.getDominance()) <= 100)) {
			jSlider1.setValue(Integer.parseInt(data.getDominance()));
			jTextField1.setText(data.getDominance());
		} else
			JOptionPane.showMessageDialog(null,
					"The Dominance value should between 0 to 100", "Alert",
					JOptionPane.ERROR_MESSAGE);
		if ((Integer.parseInt(data.getSexlinked()) >= 0)
				&& (Integer.parseInt(data.getSexlinked()) <= 100)) {
			jSlider2.setValue(Integer.parseInt(data.getSexlinked()));
			jTextField2.setText(data.getSexlinked());
		} else
			JOptionPane.showMessageDialog(null,
					"The Sex Linked value should between 0 to 100", "Alert",
					JOptionPane.ERROR_MESSAGE);
		if ((Integer.parseInt(data.getChance()) >= 0)
				&& (Integer.parseInt(data.getChance()) <= 100)) {
			jSlider3.setValue(Integer.parseInt(data.getChance()));
			jTextField3.setText(data.getChance());
		} else
			JOptionPane.showMessageDialog(null,
					"The Chance value should between 0 to 100", "Alert",
					JOptionPane.ERROR_MESSAGE);

		if (data.getPracticemode().equals("0"))
			chkpm.setSelected(false);
		else if (data.getPracticemode().equals("1"))
			chkpm.setSelected(true);
		else
			JOptionPane.showMessageDialog(null, "Practice mode value error", "Alert",
					JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Set all the values to default
	 */
	public void clear() {
		jSlider1.setValue(50);
		jSlider2.setValue(50);
		jSlider3.setValue(50);
		jTextField1.setText("50");
		jTextField2.setText("50");
		jTextField3.setText("50");
		chkpm.setSelected(false);
	}

	/**
	 * SliderListener implements ChangeLister
	 */
	class SliderListener implements ChangeListener {
		JTextField jtf;

		/**
		 * SliderListener constructor
		 * 
		 * @param jtf
		 *            the JTextField contains the value
		 */
		public SliderListener(JTextField jtf) {
			this.jtf = jtf;
		}

		/**
		 * When the Sider is cheanged, we modify the text value
		 * 
		 * @param e
		 *            used to notify interested parties that state has changed
		 *            in the event source
		 */
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			if (!source.getValueIsAdjusting()) {
				int val = (int) source.getValue();
				jtf.setText(Integer.toString(val));
				if (source.equals(jSlider2)) {
					if (val == 100)
						disableSliderSet();
					else
						enableSliderSet();
				}
			}
		}
	}

	/**
	 * TextValueListener1 implements FocusListener
	 */
	class TextValueListener1 implements FocusListener {
		JSlider js;

		/**
		 * constructor
		 */
		public TextValueListener1(JSlider js) {
			this.js = js;
		}

		/** */
		public void focusGained(FocusEvent e) {
		}

		/**
		 * when the focus lost we change the slider position to present the text
		 * value
		 * 
		 * @param e
		 */
		public void focusLost(FocusEvent e) {
			int val = 0;
			JTextField jtf = (JTextField) e.getSource();
			try {
				val = Integer.parseInt(jtf.getText());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null,
						"Please input a number between 0 to 100", "Alert",
						JOptionPane.ERROR_MESSAGE);
			}
			if (val < 0 || val > 100)
				JOptionPane.showMessageDialog(null,
						"Please input a number between 0 to 100", "Alert",
						JOptionPane.ERROR_MESSAGE);
			else
				js.setValue(val);
			if (jtf.equals(jTextField2)) {
				if (val == 0)
					disableSliderSet();
				else
					enableSliderSet();
			}
		}
	}

	/**
	 * TextValueListener implements ActionListener
	 */
	class TextValueListener2 implements ActionListener {
		JSlider js;

		/**
		 * constructor
		 */
		public TextValueListener2(JSlider js) {
			this.js = js;
		}

		/**
		 * After user input the value in the textfield we change the slider
		 * position to present the text value
		 * 
		 * @param e
		 */
		public void actionPerformed(ActionEvent e) {
			int val = 0;
			JTextField jtf = (JTextField) e.getSource();
			try {
				val = Integer.parseInt(jtf.getText());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null,
						"Please input a number between 0 to 100", "Alert",
						JOptionPane.ERROR_MESSAGE);
			}
			if (val < 0 || val > 100)
				JOptionPane.showMessageDialog(null,
						"Please input a number between 0 to 100", "Alert",
						JOptionPane.ERROR_MESSAGE);
			else
				js.setValue(val);
			if (jtf.equals(jTextField2)) {
				if (val == 0)
					disableSliderSet();
				else
					enableSliderSet();
			}
		}
	}

	/**
	 * OpenListener implements ActionListener
	 */
	class OpenListener implements ActionListener {
		/**
		 * when the open is clicked, create a FileChooser, read the data and
		 * display
		 */
		public void actionPerformed(ActionEvent e) {
			OpenFrame openFrame = new OpenFrame();
			JFileChooser fc = new JFileChooser(".");
			item2.setEnabled(true);
			fc.addChoosableFileFilter(new XmlFilter());
			int returnVal = fc.showOpenDialog(openFrame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					selFile = fc.getSelectedFile();
					FileInputStream input = new FileInputStream(selFile);
					showData(input);
				} catch (Exception ex) {
					System.err.println(ex);
				}
				// let the open file can be save
				jButton2.setEnabled(true);
			}
		}
	}

	/**
	 * SaveListener implements ActionListener
	 */
	class SaveListener implements ActionListener {
		/**
		 * when the save is clicked, create a FileChooser,collect all values and
		 * write to a file
		 */
		public void actionPerformed(ActionEvent e) {
			File sfile;
			FileOutputStream output;
			SaveFrame saveFrame = new SaveFrame();
			JFileChooser fc = new JFileChooser(".");
			StringBuffer sb = new StringBuffer();
			fc.addChoosableFileFilter(new XmlFilter());
			int returnVal = fc.showSaveDialog(saveFrame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					selFile = fc.getSelectedFile();
					if (!selFile.toString().endsWith(".prb")) {
						sb.append(selFile.toString()).append(".prb");
						sfile = new File(sb.toString());
						output = new FileOutputStream(sfile);
					} else
						output = new FileOutputStream(selFile);
					collectData(output, sb.toString());
					JOptionPane.showMessageDialog(null, "Saved the file successfully",
							"Message", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Can not save the file",
							"Alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * ClearListener implements ActionListener
	 */
	class ClearListener implements ActionListener {
		/**
		 * reset all the components value
		 */
		public void actionPerformed(ActionEvent e) {
			clear();
		}
	}

	/**
	 * ExitListener implements ActionListener
	 */
	class ExitListener implements ActionListener {
		/** exit the system */
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	/**
	 * HelpListener implements ActionListener
	 */
	class HelpListener implements ActionListener {
		/**
		 * read the UserGiude.xml and generate the html file
		 * 
		 * @param e
		 */
		public void actionPerformed(ActionEvent e) {
			final JEditorPane helpPane = new JEditorPane();
			helpPane.setEditable(false);
			helpPane.setContentType("text/html");

			try {
				helpPane.setPage(AFrame.class.getResource("Help/index.html"));
			} catch (Exception ex) {
				JOptionPane
						.showMessageDialog(
								null,
								"Be sure the help folder is in the same folder as VGL.",
								"Can't find help file.",
								JOptionPane.ERROR_MESSAGE);
				return;
			}

			JScrollPane helpScrollPane = new JScrollPane(helpPane);
			JDialog helpDialog = new JDialog();
			JButton backButton = new JButton("Go Back to Top of Page");
			helpDialog.getContentPane().setLayout(new BorderLayout());
			helpDialog.getContentPane().add(backButton, BorderLayout.NORTH);
			helpDialog.getContentPane()
					.add(helpScrollPane, BorderLayout.CENTER);
			Dimension screenSize = getToolkit().getScreenSize();
			helpDialog.setBounds((screenSize.width / 8),
					(screenSize.height / 8), (screenSize.width * 8 / 10),
					(screenSize.height * 8 / 10));
			helpDialog.show();

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
						helpPane.setPage(new URL("file://"
								+ System.getProperty("user.dir")
								+ System.getProperty("file.separator")
								+ "Help/index.html"));
					} catch (Exception e) {
						System.err.println("Couldn't open help file"
								+ e.toString());
					}
				}
			});
		}

	}

	/**
	 * AboutListener implements ActionListener
	 */
	class AboutListener implements ActionListener {
		/**
		 * Display VGL info
		 * 
		 * @param e
		 */
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "Prototype Version 1.1.5\n"
					+ "Copyright 2003\n" + "VGL Team.\n"
					+ "All Rights Reserved\n", "About Virtual Genetics Lab...",
					JOptionPane.INFORMATION_MESSAGE, new ImageIcon(logo));
		}
	}

	/**
	 * NewListener implements ActionListener
	 */
	class NewListener implements ActionListener {
		/**
		 * set all the values to default
		 * 
		 * @param e
		 */
		public void actionPerformed(ActionEvent e) {
			item2.setEnabled(true);
			clear();
		}
	}
}

