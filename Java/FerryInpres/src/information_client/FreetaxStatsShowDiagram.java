/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FreetaxStatsShowDiagram.java
 *
 * Created on 8 déc. 2011, 21:37:35
 */
package information_client;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author rapha
 */
public class FreetaxStatsShowDiagram extends javax.swing.JDialog {

    /** Creates new form FreetaxStatsShowDiagram */
    public FreetaxStatsShowDiagram(java.awt.Frame parent, boolean modal,
            JFreeChart chart) {
        super(parent, modal);
        initComponents();
        
        this.setContentPane(new ChartPanel(chart));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 673, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 494, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
