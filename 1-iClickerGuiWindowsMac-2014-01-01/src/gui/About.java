package gui;

import utility.SetLocation;

/**
 * About window.
 * @author Junhao
 *
 */
@SuppressWarnings("serial")
public class About extends javax.swing.JFrame {

    /**
     * Creates new form About
     */
    public About() {
        initComponents();
        SetLocation.setCenterScreen(this, 0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabelClickerpp = new javax.swing.JLabel();
        jLabelCopyright = new javax.swing.JLabel();
        jLabelUBC = new javax.swing.JLabel();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        this.setTitle("About");

        jLabelClickerpp.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        jLabelClickerpp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelClickerpp.setText("Clicker++");

        jLabelCopyright.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCopyright.setText("Copyright 2013");

        jLabelUBC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUBC.setText(" University of British Columbia");

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelClickerpp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelCopyright, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelUBC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(jButtonClose)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelClickerpp)
                .addGap(18, 18, 18)
                .addComponent(jLabelCopyright)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelUBC)
                .addGap(18, 18, 18)
                .addComponent(jButtonClose)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }
    
    // Variables declaration - do not modify
    private javax.swing.JButton jButtonClose;
    private javax.swing.JLabel jLabelClickerpp;
    private javax.swing.JLabel jLabelCopyright;
    private javax.swing.JLabel jLabelUBC;
    // End of variables declaration
}
