package gui;

import javax.swing.table.*;

import setting.enumoption.ShowResponsePattern;
import utility.SetLocation;

import content.Summary;

import app.Test;

/**
 * Response grid window.
 * @author Junhao
 */
@SuppressWarnings("serial")
public class ResponseGrid extends javax.swing.JFrame {

    /**
     * Creates new form ResponseGrid.
     * @throws ClassNotFoundException 
     */
    public ResponseGrid(Test test) {
        this.test = test;
    	initComponents();
        SetLocation.setCenterScreen(this, 0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     * @throws ClassNotFoundException 
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "", "", "", "", "", "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
            
        	@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass( int column ) 
        	{
        		return getValueAt(0, column).getClass();
        	}
        });
        
        this.setResizable(false);
        this.setTitle("Response Grid");
        
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Generate a new vote in the response grid.
     * @param summary summary associated with this clicker Id, for number of attempts.
     * @param index index of the vote; index starts from zero.
     * @throws ClassNotFoundException 
     */
    
    // TODO no more unregistered clickers. Unregistered clickers only when no student list is not provided.
    public void newVote(Summary summary, int index) throws ClassNotFoundException {
    	DefaultTableModel tableModel = (DefaultTableModel)this.jTable2.getModel();
        
    	// Compute the correct location for the new vote.
        int dataRowCount = index / tableModel.getColumnCount();
        if (dataRowCount >= tableModel.getRowCount()) {
        	tableModel.addRow(new Object [] {null, null, null, null, null, null, null, null});
        }
        int dataColumnCount = index % tableModel.getColumnCount() ;

        String voteStr;
        
        // Add clicker Id or student Id.
        if (test.getCourse().getShowResponsePattern() == ShowResponsePattern.ICLICKERID) {    
        	voteStr = summary.getId();
        } else {
        	if (summary.getStudent() == null) {
            	// If this is a clicker not registered, show "????????".
        		voteStr = "????????";
        	} else {
        		// If this clicker is registered.
        		voteStr = summary.getStudent().getStudentId();	
        	}
        }
        
        // Add choice.
        if (test.getCourse().isShowChoice()) {
        	voteStr += ": " + summary.getButtonFinal();
        }
        
        // Add number of attempts in terms of each clicker.
        if (test.getCourse().isShowAttempts()) {
        	voteStr += " " + summary.getNumberOfAttempts();
        }
        
        tableModel.setValueAt(voteStr, dataRowCount, dataColumnCount);
    }
    
    /**
     * Modify an existing vote in the response grid.
     * @param vote vote.
     * @param index index of the vote; index starts from zero.
     */
    public void modifyVote(Summary summary, int index) {
    	if (this.test.getCourse().isShowChoice()) {	// Need to modify only if the choice is shown in the response grid.
	    	DefaultTableModel tableModel = (DefaultTableModel)this.jTable2.getModel();
	    	
	        int dataRowCount = index / tableModel.getColumnCount();
	        int dataColumnCount = index % tableModel.getColumnCount();
	        
	        String voteStr = (String)tableModel.getValueAt(dataRowCount, dataColumnCount);
	        
	        String voteStrNew = voteStr.substring(0, 8) + ": " + summary.getButtonFinal();

	        // If need to show number of attempts.
	        if (test.getCourse().isShowAttempts()) {
	        	voteStrNew += " " + summary.getNumberOfAttempts();
	        }
	        
	        tableModel.setValueAt(voteStrNew, dataRowCount, dataColumnCount);
    	}
    }
    
    /**
     * Clear all cells.
     */
    public void resetResponseGrid() {
        TableModel tableModel = this.jTable2.getModel();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                tableModel.setValueAt("", i, j);
            }
        }    
    }
    
    private Test test;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}