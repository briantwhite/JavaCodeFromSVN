package gui;

import gui.enumoption.EnumChartType;
import gui.enumoption.EnumUserGroup;
import app.Test;
import content.Question;
import content.enumoption.HistogramDisplayOption;

@SuppressWarnings("serial")
public class Histogram extends javax.swing.JFrame {
    /**
     * Creates new form Histogram.
     */
    protected Histogram(Test test) {
        this.test = test;
        
        this.histogramDisplayOption = HistogramDisplayOption.HIDE;
        
        this.chartType = EnumChartType.BARVERTICAL;
        this.userGroup = EnumUserGroup.LAB;
        
        this.question = null;
        
        initComponents();
    }
    
    protected void initComponents() {
    	
    }
    
    protected void formWindowClosed(java.awt.event.WindowEvent evt) {                                  
    	this.setHistogramDisplayOption(HistogramDisplayOption.HIDE);
    }
    
    public void update() {
    	this.canvas1.repaint();
    }

    protected EnumChartType getChartType() {
		return chartType;
	}

	protected void setChartType(EnumChartType chartType) {
		this.chartType = chartType;
		
		this.canvas1.repaint();
	}

	protected EnumUserGroup getUserGroup() {
		return userGroup;
	}

	protected void setUserGroup(EnumUserGroup userGroup) {
		this.userGroup = userGroup;
		
		this.canvas1.repaint();
	}

    public HistogramDisplayOption getHistogramDisplayOption() {
		return histogramDisplayOption;
	}

    public void setHistogramDisplayOption(HistogramDisplayOption histogramDisplayOption) {
    	this.histogramDisplayOption = histogramDisplayOption;
    	
    	if (this.histogramDisplayOption == HistogramDisplayOption.HIDE) {
    		this.setVisible(false);
    	} else {
    		this.setVisible(true);
    	}
    	
    	this.canvas1.repaint();
    }
    
    /**
     * Unbound the question with the histogram.
     */
    public void clearQuestion() {
    	this.question = null;
    }
    
    protected Test test;
    
    protected HistogramDisplayOption histogramDisplayOption;
	
    protected EnumChartType chartType;
    protected EnumUserGroup userGroup;
    
    protected Question question;
    	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected Canvas canvas1;
    protected javax.swing.JLabel jLabelTitle;
    // End of variables declaration//GEN-END:variables
}
