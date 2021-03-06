package gui;

import java.io.File;

import javax.swing.JOptionPane;

import content.Course;
import app.*;

/**
 * Edit course dialog.
 * @author Junhao
 * 
 */
@SuppressWarnings("serial")
public class EditCourse extends javax.swing.JFrame {

    /**
     * Create new form EditCourse.
     */
    public EditCourse(Test test) {
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

        jLabel14 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldCourseNumber = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldSectionNumber = new javax.swing.JTextField();
        jButtonCancel = new javax.swing.JButton();
        jTextFieldCourseName = new javax.swing.JTextField();
        jButtonSave = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();

        this.setResizable(false);
        this.setTitle("Edit Course");
        
        jLabel14.setText("students can use it to identify your course.");

        jLabel10.setText("Section Number:");

        jLabel9.setText("Course Number:");

        jLabel8.setText("Your course information should be specific enough so that your ");

        jTextFieldCourseNumber.setText("\n");

        jLabel7.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        jLabel7.setText("Edit your course information.");

        jTextFieldSectionNumber.setText("\n");

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jTextFieldCourseName.setText("\n");

        jButtonSave.setText(" Save ");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					jButtonSaveActionPerformed(evt);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });

        jLabel15.setText("Course Name:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel14)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel15))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldSectionNumber)
                                    .addComponent(jTextFieldCourseNumber)
                                    .addComponent(jTextFieldCourseName, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 2, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSave)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextFieldCourseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextFieldCourseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextFieldSectionNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonSave))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        // Button Cancel
    	
        this.setVisible(false);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) throws Exception {//GEN-FIRST:event_jButtonSaveActionPerformed
        // Button Save
    	
    	// Obtain text field containing course name, course number and section number.
        String courseName = jTextFieldCourseName.getText().trim();
        String courseNumber = jTextFieldCourseNumber.getText().trim();
        String sectionNumber = jTextFieldSectionNumber.getText().trim();
        
        // Check if at least one field is filled.
        if (courseName.equals("") && courseNumber.equals("") && sectionNumber.equals("")) {
        	JOptionPane.showMessageDialog(this, "Please fill at least one of the three fields to create a course", "Empty Course Information!", JOptionPane.ERROR_MESSAGE);
        	return;
        }
        
        // Check if illegal characters exist.
        if (Course.ifTextLegal(courseName) == false || Course.ifTextLegal(courseNumber) == false || Course.ifTextLegal(sectionNumber) == false) {
        	JOptionPane.showMessageDialog(this, "Only A-Z, a-z, 0-9 and space are allowed.", "Illegal Characters Detected!", JOptionPane.ERROR_MESSAGE);
        	return;
        }
        
        // Generate the new name of the folder representing this course.
        String finalName = courseName
                + (courseNumber.equals("") ? "" : "-") + courseNumber
                + (sectionNumber.equals("") ? "" : "-") + sectionNumber;
                
        // Check if the course already exists.
		String coursePath = "./Classes/" + finalName;

		File file = new File(coursePath);
	    
	    if (file.exists()) {
        	JOptionPane.showMessageDialog(this, "Course with the same name exists. Please use a new name.", "Course Exists@!", JOptionPane.ERROR_MESSAGE);
        	return;
	    }
        
	    // Rename course in the file system
    	Course.modifyCourse(this.courseName, finalName);
    	
        // Update course list in Welcome window. 
        test.getWelcome().updateCourseList();
        
        this.setVisible(false);
    }//GEN-LAST:event_jButtonSaveActionPerformed
    
    /**
     * Create all fields when the dialog is launched.
     * @param courseName name of the course, including course number and section number.
     */
    public void setFields(String courseName) {
    	this.courseName = courseName;
        this.jTextFieldCourseName.setText("");
        this.jTextFieldCourseNumber.setText("");
        this.jTextFieldSectionNumber.setText("");
        
        if (courseName != null) {
            String[] finalName = courseName.split("-");
            jTextFieldCourseName.setText(finalName[0]);
            if (finalName.length > 1) {
            	jTextFieldCourseNumber.setText(finalName[1]);
            	if (finalName.length > 2) {
            		jTextFieldSectionNumber.setText(finalName[2]);
            	}
            }
        }
    }
    
    private Test test;
    private String courseName; 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextFieldCourseName;
    private javax.swing.JTextField jTextFieldCourseNumber;
    private javax.swing.JTextField jTextFieldSectionNumber;
    // End of variables declaration//GEN-END:variables
}
