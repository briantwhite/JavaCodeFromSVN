package gui;

import java.io.IOException;

import content.Course;

import app.*;

/**
 * Delete course dialog.
 * @author Junhao
 * 
 */
@SuppressWarnings("serial")
public class DeleteCourse extends javax.swing.JFrame {

    /**
     * Create new form DeleteCourse.
     */
    public DeleteCourse(Test test) {
        this.test = test;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel18 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jButtonNo = new javax.swing.JButton();
        jButtonYes = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();

        this.setResizable(false);
        this.setTitle("Delete Course");
        
        jLabel18.setText("this course if you delete it.");

        jLabel17.setText("You will lose all of the student data for ");

        jButtonNo.setText("  No  ");
        jButtonNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButtonYes.setText("  Yes ");
        jButtonYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					jButton5ActionPerformed(evt);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });

        jLabel16.setText("Are you sure you want to delete this course?");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel17))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jButtonYes)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonNo)))))
                .addGap(0, 15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonYes)
                    .addComponent(jButtonNo))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Button No
    	
        this.setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) throws IOException {//GEN-FIRST:event_jButton5ActionPerformed
        // Button Yes

    	// Delete course in the file system.
    	Course.delectCourse(courseName);
    	
    	// Update course list in Welcome window. 
        test.getWelcome().updateCourseList();
        
        this.setVisible(false);
    }//GEN-LAST:event_jButton5ActionPerformed
    
    /**
     * Remember the name of the course to be deleted.
     * @param courseName name of the course, including course number and section number.
     */
    public void setFields(String courseName) {
    	this.courseName = courseName;
    }
    
    private Test test;
    private String courseName;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonYes;
    private javax.swing.JButton jButtonNo;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    // End of variables declaration//GEN-END:variables
}