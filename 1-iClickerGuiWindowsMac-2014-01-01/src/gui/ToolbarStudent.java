package gui;

import app.Test;

import content.enumoption.HistogramDisplayOption;

/**
 * Toolbar on big screen.
 * @author Junhao
 *
 */
@SuppressWarnings("serial")
public class ToolbarStudent extends javax.swing.JFrame {
    
    /**
     * Creates new form Toolbar.
     */
    public ToolbarStudent(Test test) {
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
        jButtonHistogram = new javax.swing.JButton();
        jLabelTime = new javax.swing.JLabel();
        jLabelVotes = new javax.swing.JLabel();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);
        setTitle("Student toolbar");
        
        jButtonHistogram.setText("Histogram");
        jButtonHistogram.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonHistogramnMouseClicked(evt);
            }
        });

        jLabelTime.setText("Time");

        jLabelVotes.setText("Votes");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButtonHistogram)
                .add(18, 18, 18)
                .add(jLabelTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabelVotes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE, false)
                    .add(jButtonHistogram, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabelTime)
                    .add(jLabelVotes))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>
    
    private void jMenuItemHideActionPerformed(java.awt.event.ActionEvent evt) {
    	// Histogram Hide
    	
        this.test.getHistogramStudent().setHistogramDisplayOption(HistogramDisplayOption.HIDE);
    }

    private void jMenuItemShowWithoutCorrectAnswerActionPerformed(java.awt.event.ActionEvent evt) {
        // Histogram Show With Correct Answer
    	
    	this.test.getHistogramStudent().setHistogramDisplayOption(HistogramDisplayOption.SHOWWITHOUTCORRECTANSWER);
    }

    private void jMenuItemShowWithCorrectAnswerActionPerformed(java.awt.event.ActionEvent evt) {
    	// Histogram Show Without Correct Answer
    	
    	this.test.getHistogramStudent().setHistogramDisplayOption(HistogramDisplayOption.SHOWWITHCORRECTANSWER);
    }

    private void jButtonHistogramnMouseClicked(java.awt.event.MouseEvent evt) {
    	// Button Histogram
    	
        this.jPopupMenuHistogramOption.show(evt.getComponent(), evt.getX(), evt.getY());
    }
    
    /**
     * Change widgets on Toolbar when voting starts.
     */
    public void startChangeLabel() {
        this.jLabelVotes.setText("0");
    }
    
    /**
     * Change widgets on Toolbar when voting stops.
     */
    public void stopChangeLabel() {
    	
    }
    
    /**
     * Update time displayed on the toolbar.
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
     * Reset toolbar.
     */
    public void resetToolbarStudent() {
    	this.jLabelTime.setText("Time");
    	this.jLabelVotes.setText("Votes");
    }
    
    private Test test;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonHistogram;
    private javax.swing.JLabel jLabelTime;
    private javax.swing.JLabel jLabelVotes;
    private javax.swing.JMenuItem jMenuItemHide;
    private javax.swing.JMenuItem jMenuItemShowWithCorrectAnswer;
    private javax.swing.JMenuItem jMenuItemShowWithoutCorrectAnswer;
    private javax.swing.JPopupMenu jPopupMenuHistogramOption;
    // End of variables declaration//GEN-END:variables
    
}
