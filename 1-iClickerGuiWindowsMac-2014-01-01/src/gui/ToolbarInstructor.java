package gui;

import gui.enumoption.EnumQuestionListState;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import app.Test;

import content.enumoption.HistogramDisplayOption;

/**
 * Toolbar on instructor's laptop.
 * @author Junhao
 *
 */
@SuppressWarnings("serial")
public class ToolbarInstructor extends javax.swing.JFrame {
    
	private static int MAX_VOTES_PER_SECOND = 150;
    /**
     * Creates new form ToolbarInstructor.
     */
    public ToolbarInstructor(Test test) {
        this.test = test;
         
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenuHistogramOption = new javax.swing.JPopupMenu();
        jMenuItemHide = new javax.swing.JMenuItem();
        jMenuItemShowWithoutCorrectAnswer = new javax.swing.JMenuItem();
        jMenuItemShowWithCorrectAnswer = new javax.swing.JMenuItem();
        jPopupMenuSettingOption = new javax.swing.JPopupMenu();
        jMenuItemQuestionOnTheFly = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemLoadQuestionList = new javax.swing.JMenuItem();
        jMenuItemMySettings = new javax.swing.JMenuItem();
        jButtonA2E = new javax.swing.JButton();
        jButtonSetting = new javax.swing.JButton();
        jButtonHistogram = new javax.swing.JButton();
        jLabelTime = new javax.swing.JLabel();
        jLabelVotes = new javax.swing.JLabel();
        VotesPerSecondBar = new javax.swing.JProgressBar(0, MAX_VOTES_PER_SECOND);
        jButtonChangeCourse = new javax.swing.JButton();

        jPopupMenuHistogramOption.setLabel("");

        jMenuItemHide.setText("Hide Histogram");
        jMenuItemHide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHideActionPerformed(evt);
            }
        });
        jPopupMenuHistogramOption.add(jMenuItemHide);

        jMenuItemShowWithoutCorrectAnswer.setText("Show Histogram");
        jMenuItemShowWithoutCorrectAnswer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemShowWithoutCorrectAnswerActionPerformed(evt);
            }
        });
        jPopupMenuHistogramOption.add(jMenuItemShowWithoutCorrectAnswer);

        jMenuItemShowWithCorrectAnswer.setText("Show Histogram With Correct Answer");
        jMenuItemShowWithCorrectAnswer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemShowWithCorrectAnswerActionPerformed(evt);
            }
        });
        jPopupMenuHistogramOption.add(jMenuItemShowWithCorrectAnswer);

        jMenuItemQuestionOnTheFly.setText("Question On The Fly");
        jMenuItemQuestionOnTheFly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemQuestionOnTheFlyActionPerformed(evt);
            }
        });
        jPopupMenuSettingOption.add(jMenuItemQuestionOnTheFly);
        jPopupMenuSettingOption.add(jSeparator1);

        jMenuItemLoadQuestionList.setText("Load Question List");
        jMenuItemLoadQuestionList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					jMenuItemLoadQuestionListActionPerformed(evt);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
        jPopupMenuSettingOption.add(jMenuItemLoadQuestionList);
        //jPopupMenuSettingOption.add(jSeparator3);
        
        //jMenuItemMySettings.setText("My Settings");
        jMenuItemMySettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMySettingsActionPerformed(evt);
            }
        });
        jPopupMenuSettingOption.add(jMenuItemMySettings);
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);
        setTitle("Instructor toolbar");
        
        jButtonA2E.setText("Start");
        jButtonA2E.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					jButtonA2EActionPerformed(evt);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });

        jButtonHistogram.setText("Histogram");
        jButtonHistogram.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonHistogramnMouseClicked(evt);
            }
        });

        jButtonSetting.setText("Setting");
        jButtonSetting.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonSettingMouseClickered(evt);
            }
        });

        jButtonChangeCourse.setText("Change Course");
        jButtonChangeCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChangeCourseActionPerformed(evt);
            }
        });

        jLabelTime.setText("Time");

        jLabelVotes.setText("Votes");
        
        VotesPerSecondBar.setValue(50);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButtonA2E, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jButtonHistogram)
                .add(18, 18, 18)
                .add(jButtonSetting)
                .add(18, 18, 18)
                .add(jButtonChangeCourse)
                .add(18, 18, 18)
                .add(jLabelTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabelVotes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(VotesPerSecondBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE, false)
                    .add(jButtonA2E, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButtonHistogram, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButtonSetting, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabelTime)
                    .add(jButtonChangeCourse)
                    .add(jLabelVotes)
                    .add(VotesPerSecondBar))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

	private void jButtonA2EActionPerformed(java.awt.event.ActionEvent evt) throws Exception {//GEN-FIRST:event_jButtonA2EActionPerformed
        // Button A-E
    	
        if (test.getCourse().getSession().isVotingEnabled() == false) { // Voting is not enabled.
        	this.test.getCourse().getSession().startQuestion();
        } else {
        	this.test.getCourse().getSession().stopQuestion();
        }
    }

    private void jButtonHistogramnMouseClicked(java.awt.event.MouseEvent evt) {
        // Button Histogram
    	
        this.jPopupMenuHistogramOption.show(evt.getComponent(), evt.getX(), evt.getY());
    }

    private void jButtonSettingMouseClickered(java.awt.event.MouseEvent evt) {
    	// Button Setting
    	
    	if (this.jButtonSetting.isEnabled() == false) return;
    	
        this.jPopupMenuSettingOption.show(evt.getComponent(), evt.getX(), evt.getY());
    }
    
    private void jButtonChangeCourseActionPerformed(java.awt.event.ActionEvent evt) {
        // Button change course
    	
    	this.test.getAbout().setVisible(false);
    	
    	this.test.getAdHocQuestionEditor().setVisible(false);
    	
    	this.test.getQuestionPanel().setVisible(false);
    	
    	this.test.getRunQuestionList().setVisible(false);
    	
    	this.test.getResponseGrid().setVisible(false);
    	this.test.getResponseGrid().resetResponseGrid();
    	
    	this.test.getSelectCorrect().setVisible(false);
    	this.test.getSelectCorrect().resetSelectCorrect();
    	
        this.test.getHistogramInstructor().setVisible(false);
        this.test.getHistogramInstructor().update();
        
        this.test.getHistogramStudent().setVisible(false);
        this.test.getHistogramStudent().update();
        
    	this.jLabelTime.setText("Time");
    	this.jLabelVotes.setText("Votes");
    	this.setVisible(false);
    	
    	this.test.getToolbarStudent().resetToolbarStudent();
    	this.test.getToolbarStudent().setVisible(false);
    	
    	this.test.getWelcome().setVisible(true);
    }                                                   

    private void jMenuItemHideActionPerformed(java.awt.event.ActionEvent evt) {
    	// Histogram Hide
    	
        this.test.getHistogramInstructor().setHistogramDisplayOption(HistogramDisplayOption.HIDE);
    }

    private void jMenuItemShowWithoutCorrectAnswerActionPerformed(java.awt.event.ActionEvent evt) {
        // Histogram Show With Correct Answer
    	
    	this.test.getHistogramInstructor().setHistogramDisplayOption(HistogramDisplayOption.SHOWWITHOUTCORRECTANSWER);
    }

    private void jMenuItemShowWithCorrectAnswerActionPerformed(java.awt.event.ActionEvent evt) {
    	// Histogram Show Without Correct Answer
    	
    	this.test.getHistogramInstructor().setHistogramDisplayOption(HistogramDisplayOption.SHOWWITHCORRECTANSWER);
    }

    private void jMenuItemQuestionOnTheFlyActionPerformed(java.awt.event.ActionEvent evt) {
        // Button Settings Question On The Fly
    	
    	this.test.getAdHocQuestionEditor().setVisible(true);
    }

    private void jMenuItemLoadQuestionListActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
    	JFileChooser jFileChooser = new JFileChooser();
    	jFileChooser.setCurrentDirectory(new java.io.File("./Classes/" + this.test.getCourse().getCourseName()));
    	jFileChooser.setAcceptAllFileFilterUsed(false);
    	jFileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".csv Files", new String[] { "csv" }));
    	
    	int returnVal = jFileChooser.showOpenDialog(this);
    	
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            this.test.getCourse().loadCourseQuestionList(file.getPath());
        } else {
        	return;
        }
        
        this.test.getRunQuestionList().loadListQuestion();
        this.test.getRunQuestionList().setEnumQuestionListState(EnumQuestionListState.PLAY);
        this.test.getRunQuestionList().setVisible(true);        
    }

    private void jMenuItemMySettingsActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	
//   	 TODO show setting panel
//      test.getMySettings().readConfiguration();
//      test.getMySettings().setDefaultTab();
//      SetLocation.setCenterScreen(test.getMySettings());
//      test.getMySettings().setVisible(true);
    }

    /**
     * Change widgets on Toolbar when voting starts.
     */
    public void startChangeLabel() {
    	this.jButtonA2E.setText("Stop");
        this.jButtonSetting.setEnabled(false);
        this.jButtonChangeCourse.setEnabled(false);
        this.jLabelVotes.setText("0");
    }
    
    /**
     * Change widgets on Toolbar when voting stops.
     */
    public void stopChangeLabel() {
        this.jButtonA2E.setText("Start");
        this.jButtonSetting.setEnabled(true);
        this.jButtonChangeCourse.setEnabled(true);
    }
    
    /**
     * Update time displayed on the Toolbar.
     * @param time time.
     */
    public void updateTime(String time) {
        this.jLabelTime.setText(time);
    }
        
    /**
     * Update total number of votes received.
     * @param amount amount of votes.
     */
    public void updateVotes(int amount) {
        this.jLabelVotes.setText(amount + "");
    }

    /**
     * Update number of votes received since last report.
     * Votes per second meter
     * @param amount amount of votes.
     */
    public void updateVotesPerSecond(int amount) {
    	if (amount > MAX_VOTES_PER_SECOND) amount = MAX_VOTES_PER_SECOND;
        this.VotesPerSecondBar.setValue(amount);
    }

    private Test test;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonA2E;
    private javax.swing.JButton jButtonChangeCourse;
    private javax.swing.JButton jButtonHistogram;
    private javax.swing.JButton jButtonSetting;
    private javax.swing.JLabel jLabelTime;
    private javax.swing.JLabel jLabelVotes;
    private javax.swing.JProgressBar VotesPerSecondBar;	// added by bw
    private javax.swing.JMenuItem jMenuItemHide;
    private javax.swing.JMenuItem jMenuItemLoadQuestionList;
    private javax.swing.JMenuItem jMenuItemMySettings;
    private javax.swing.JMenuItem jMenuItemQuestionOnTheFly;
    private javax.swing.JMenuItem jMenuItemShowWithCorrectAnswer;
    private javax.swing.JMenuItem jMenuItemShowWithoutCorrectAnswer;
    private javax.swing.JPopupMenu jPopupMenuHistogramOption;
    private javax.swing.JPopupMenu jPopupMenuSettingOption;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
