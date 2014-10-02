package gui;

import utility.MultipleScreens;
import utility.SetLocation;
import content.enumoption.HistogramDisplayOption;
import app.Test;

/**
 * Histogram window.
 * @author Junhao
 *
 */
@SuppressWarnings("serial")
public class HistogramStudent extends Histogram {
    
    public HistogramStudent(Test test) {
		super(test);
		
		this.histogramDisplayOption = HistogramDisplayOption.HIDE;
		
		SetLocation.setCenterScreen(this, MultipleScreens.getNumberOfScreens() - 1);
	}

	/** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    protected void initComponents() {

        jLabelTitle = new javax.swing.JLabel();
        canvas1 = new gui.Canvas(test, this);

        this.setResizable(false);
        this.setTitle("Student Histogram");
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        
        jLabelTitle.setFont(new java.awt.Font("����", 0, 18)); // NOI18N
        jLabelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitle.setText("Question");
        jLabelTitle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        org.jdesktop.layout.GroupLayout canvas1Layout = new org.jdesktop.layout.GroupLayout(canvas1);
        canvas1.setLayout(canvas1Layout);
        canvas1Layout.setHorizontalGroup(
            canvas1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 606, Short.MAX_VALUE)
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
                    .add(canvas1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
                    .add(jLabelTitle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabelTitle)
                .add(18, 18, 18)
                .add(canvas1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>
    
    /**
     * Associate the histogram with a question.
     * @param questionIndex index of the question. 
     */
    public void setQuestion(int questionIndex) {
    	this.question = this.test.getCourse().getSession().getQuestion(questionIndex);
    	this.jLabelTitle.setText(this.question.getTitle());
    	
    	this.repaint();
    	this.update();
    }
    
    /**
     * Change the state of how histogram display, and whether to reveal correct answer or not.
     * This setting can be changed using student's toolbar or by instructor's clicker remote.
     */
    public void changeHistogramDisplayOption() {
		switch (histogramDisplayOption) {
		case HIDE:
			histogramDisplayOption = HistogramDisplayOption.SHOWWITHOUTCORRECTANSWER;
			break;
		case SHOWWITHOUTCORRECTANSWER: 
			histogramDisplayOption = HistogramDisplayOption.SHOWWITHCORRECTANSWER;
			break;
		default: 
			histogramDisplayOption = HistogramDisplayOption.HIDE;
		}
		
		setHistogramDisplayOption(histogramDisplayOption);
    }
}