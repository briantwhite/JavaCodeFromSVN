package gui;

import content.enumoption.HistogramDisplayOption;
import utility.SetLocation;
import gui.enumoption.EnumChartType;
import gui.enumoption.EnumUserGroup;
import app.Test;

/**
 * Histogram window.
 * @author Junhao
 *
 */
@SuppressWarnings("serial")
public class HistogramInstructor extends Histogram {
    
    /**
     * Creates new form Histogram.
     */
    public HistogramInstructor(Test test) {
        super(test);
        
        this.histogramDisplayOption = HistogramDisplayOption.SHOWWITHCORRECTANSWER;
        
        SetLocation.setCenterScreen(this, 0);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    protected void initComponents() {

        jPopupMenuAllChartType = new javax.swing.JPopupMenu();
        jMenuItemBarVertical = new javax.swing.JMenuItem();
        jMenuItemBarHorizontal = new javax.swing.JMenuItem();
        jMenuItemPie = new javax.swing.JMenuItem();
        jPopupMenuBySection = new javax.swing.JPopupMenu();
        jMenuItemAll = new javax.swing.JMenuItem();
        jMenuItemSection = new javax.swing.JMenuItem();
        jButtonCorrectAnswer = new javax.swing.JButton();
        jButtonBySection = new javax.swing.JButton();
        jButtonChartType = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();
        jButtonLast = new javax.swing.JButton();
        jLabelTitle = new javax.swing.JLabel();
        jButtonFilter = new javax.swing.JButton();
        canvas1 = new gui.Canvas(test, this);

        this.setResizable(false);
        this.setTitle("Instructor Histogram");
        
        jMenuItemBarVertical.setText("Bar chart-vertical");
        jMenuItemBarVertical.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemBarVerticalActionPerformed(evt);
            }
        });
        jPopupMenuAllChartType.add(jMenuItemBarVertical);

        jMenuItemBarHorizontal.setText("Bar chart-horizontal");
        jMenuItemBarHorizontal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemBarHorizontalActionPerformed(evt);
            }
        });
        jPopupMenuAllChartType.add(jMenuItemBarHorizontal);

        jMenuItemPie.setText("Pie Chart");
        jMenuItemPie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPieActionPerformed(evt);
            }
        });
        jPopupMenuAllChartType.add(jMenuItemPie);

        jMenuItemSection.setText("Show each section");
        jMenuItemSection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSectionActionPerformed(evt);
            }
        });
        jPopupMenuBySection.add(jMenuItemSection);

        jMenuItemAll.setText("Show all");
        jMenuItemAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAllActionPerformed(evt);
            }
        });
        jPopupMenuBySection.add(jMenuItemAll);
        
        jButtonFilter.setIcon(new javax.swing.ImageIcon("./res/filter.png")); // NOI18N
        jButtonFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFilterActionPerformed(evt);
            }
        });
        jButtonFilter.setVisible(false);
        
        jButtonCorrectAnswer.setIcon(new javax.swing.ImageIcon("./res/correctanswer.png")); // NOI18N
        jButtonCorrectAnswer.setToolTipText("Select correct answer");
        jButtonCorrectAnswer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCorrectAnswerActionPerformed(evt);
            }
        });

        jButtonChartType.setIcon(new javax.swing.ImageIcon("./res/charttype.png")); // NOI18N
        jButtonChartType.setToolTipText("Select chart type");
        jButtonChartType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButtonChartTypeMouseClicked(evt);
            }
        });
        jButtonChartType.setEnabled(false);
        
        jButtonBySection.setIcon(new javax.swing.ImageIcon("./res/bysection.png")); // NOI18N
        jButtonBySection.setToolTipText("Show all or by section");
        jButtonBySection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonBySectionMouseClicked(evt);
            }
        });

        jButtonNext.setIcon(new javax.swing.ImageIcon("./res/next.png")); // NOI18N
        jButtonNext.setToolTipText("Next question");
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });

        jButtonLast.setIcon(new javax.swing.ImageIcon("./res/last.png")); // NOI18N
        jButtonLast.setToolTipText("Last question");
        jButtonLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLastActionPerformed(evt);
            }
        });

        jLabelTitle.setFont(new java.awt.Font("����", 0, 18)); // NOI18N
        jLabelTitle.setText("Question");
        jLabelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        org.jdesktop.layout.GroupLayout canvas1Layout = new org.jdesktop.layout.GroupLayout(canvas1);
        canvas1.setLayout(canvas1Layout);
        canvas1Layout.setHorizontalGroup(
            canvas1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );
        canvas1Layout.setVerticalGroup(
            canvas1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, canvas1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jButtonFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jButtonCorrectAnswer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jButtonBySection, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jButtonChartType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 330, Short.MAX_VALUE)
                        .add(jButtonLast, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jButtonNext, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabelTitle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabelTitle)
                .add(18, 18, 18)
                .add(canvas1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonBySection, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonChartType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonNext, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonLast, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonCorrectAnswer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void jMenuItemBarVerticalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Popup menu change chart type vertical.
    	
        this.setChartType(EnumChartType.BARVERTICAL);
        this.test.getHistogramStudent().setChartType(EnumChartType.BARVERTICAL);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItemBarHorizontalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // Popup menu change chart type horizontal.
    	
        this.setChartType(EnumChartType.BARHORIZONTAL);
        this.test.getHistogramStudent().setChartType(EnumChartType.BARHORIZONTAL);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItemPieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // Popup menu change chart type pie.
    	
        this.setChartType(EnumChartType.PIE);
        this.test.getHistogramStudent().setChartType(EnumChartType.PIE);
    }//GEN-LAST:event_jMenuItem3ActionPerformed.                            
	
	private void jMenuItemAllActionPerformed(java.awt.event.ActionEvent evt) {
	    // Popup menu show all groups together.
		
		this.setUserGroup(EnumUserGroup.ALL);
		this.jButtonChartType.setEnabled(true);
		
		this.test.getHistogramStudent().setUserGroup(EnumUserGroup.ALL);
	}
	
	private void jMenuItemSectionActionPerformed(java.awt.event.ActionEvent evt) {
	    // Popup menu show lab sections.
		
		this.setUserGroup(EnumUserGroup.LAB);
		this.jButtonChartType.setEnabled(false);
		
		this.test.getHistogramStudent().setUserGroup(EnumUserGroup.LAB);
	}

    private void jButtonFilterActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
	
    private void jButtonCorrectAnswerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCorrectAnswerActionPerformed
        // Button Correct Answer
    	
        SetLocation.setCenterParent(this, test.getSelectCorrect());
        test.getSelectCorrect().resetSelectCorrect();
        test.getSelectCorrect().setVisible(true);
    }//GEN-LAST:event_jButtonCorrectAnswerActionPerformed
    
    private void jButtonChartTypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonChartTypeMouseReleased
        // Button Chart type
    	
    	if (this.jButtonChartType.isEnabled()) {
    		this.jPopupMenuAllChartType.show(evt.getComponent(), evt.getX(), evt.getY());
    	}
    }//GEN-LAST:event_jButtonChartTypeMouseReleased

    private void jButtonBySectionMouseClicked(java.awt.event.MouseEvent evt) {
        // Button User group type
    	
        this.jPopupMenuBySection.show(evt.getComponent(), evt.getX(), evt.getY());
    }
    
    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNextActionPerformed
        // Button Next one
    	
    	this.test.getCourse().getSession().nextQuestion();
    }//GEN-LAST:event_jButtonNextActionPerformed

    private void jButtonLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLastActionPerformed
        // Button Last One
    	
    	this.test.getCourse().getSession().lastQuestion();
    }
    
    /**
     * Associate the histogram with a question.
     * @param questionIndex index of the question. 
     */
    public void setQuestion(int questionIndex) {
    	this.question = this.test.getCourse().getSession().getQuestion(questionIndex);
    	this.configureNextLastButtons(questionIndex, this.test.getCourse().getSession().getQuestionAmount());
    	this.jLabelTitle.setText(this.question.getTitle());
    	
    	this.repaint();
    	this.update();
    }

	/**
	 * Disable buttons when voting starts.
	 */
    public void disableButtons() {
        this.jButtonLast.setEnabled(false);
        this.jButtonNext.setEnabled(false);
    }
    
    /**
     * Enable buttons when voting stops.
     */
    public void enableButtons() {
    	int questionIndex = this.test.getCourse().getSession().getCurrentQuestionIndex();
    	int questionAmount = this.test.getCourse().getSession().getQuestionAmount();
    	this.configureNextLastButtons(questionIndex, questionAmount);
    }
    
    /**
     * Configure next question and last question buttons.
     * @param questionIndex index of the question.
     * @param totalQuestionAmount total number of questions in this session.
     */
    private void configureNextLastButtons(int questionIndex, int questionAmount) {
    	if (questionIndex != -1 && questionIndex != 0) {
    		this.jButtonLast.setEnabled(true);
    	} else {
    		this.jButtonLast.setEnabled(false);
    	}
    	
    	if (questionIndex != questionAmount - 1) {
    		this.jButtonNext.setEnabled(true);
    	} else {
    		this.jButtonNext.setEnabled(false);
    	}
    }
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBySection;
    private javax.swing.JButton jButtonChartType;
    private javax.swing.JButton jButtonCorrectAnswer;
    private javax.swing.JButton jButtonFilter;
    private javax.swing.JButton jButtonLast;
    private javax.swing.JButton jButtonNext;
    private javax.swing.JMenuItem jMenuItemAll;
    private javax.swing.JMenuItem jMenuItemBarHorizontal;
    private javax.swing.JMenuItem jMenuItemBarVertical;
    private javax.swing.JMenuItem jMenuItemPie;
    private javax.swing.JMenuItem jMenuItemSection;
    private javax.swing.JPopupMenu jPopupMenuAllChartType;
    private javax.swing.JPopupMenu jPopupMenuBySection;
    // End of variables declaration//GEN-END:variables
    
}
