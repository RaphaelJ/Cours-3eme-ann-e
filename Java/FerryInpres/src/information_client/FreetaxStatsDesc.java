/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FreetaxStatsDesc.java
 *
 * Created on 8 déc. 2011, 16:20:54
 */
package information_client;

import information_server.FreetaxStatsDescReponse;
import javax.swing.DefaultListModel;

/**
 *
 * @author rapha
 */
public class FreetaxStatsDesc extends javax.swing.JDialog {

    /** Creates new form FreetaxStatsDesc */
    public FreetaxStatsDesc(java.awt.Frame parent, boolean modal,
            FreetaxStatsDescReponse data) {
        super(parent, modal);
        initComponents();
        
        DefaultListModel model = (DefaultListModel) this.dataList.getModel();
        
        int jour;
        for (jour = 1; jour <= data.getVentes().length; jour++) {
            model.addElement("Jour #" + jour + ": " + data.getVentes()[jour-1] );
        }
        
        this.moyenneLabel.setText(String.valueOf(data.getMoyenne()));
        this.ecartTypeLabel.setText(String.valueOf(data.getEcartType()));
        this.modeLabel.setText(String.valueOf(data.getMode()));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataList = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        moyenneLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ecartTypeLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        modeLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Données:");

        dataList.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(dataList);

        jLabel2.setText("Moyenne:");

        moyenneLabel.setText("jLabel3");

        jLabel4.setText("Ecart-type:");

        ecartTypeLabel.setText("jLabel5");

        jLabel6.setText("Mode:");

        modeLabel.setText("jLabel7");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(modeLabel)
                            .addComponent(ecartTypeLabel)
                            .addComponent(moyenneLabel))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(moyenneLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ecartTypeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(modeLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList dataList;
    private javax.swing.JLabel ecartTypeLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel modeLabel;
    private javax.swing.JLabel moyenneLabel;
    // End of variables declaration//GEN-END:variables
}
