package information_client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;


public class MainForm extends javax.swing.JFrame {
    public static final byte SUCCESS = (byte) 'S';
    public static final byte FAIL = (byte) 'F';
    
    private Socket _sock;
    private InputStream _sock_in;
    private OutputStream _sock_out;

    /** Creates new form MainForm */
    public MainForm() throws IOException {
        initComponents();
        
        // Se connecte au serveur
        this._sock = new Socket("127.0.0.1", 39005);
        this._sock_in = this._sock.getInputStream();
        this._sock_out = this._sock.getOutputStream();
        
        // Désactive les contrôles par défaut
        this.monnaiesList.setEnabled(false);
        this.meteoJoursList.setEnabled(false);
        
        this.alcoolsCheck.setEnabled(false);
        this.parfumsCheck.setEnabled(false);
        this.tabacsCheck.setEnabled(false);
        
        // Remplit les monnaies disponibles
        this.monnaiesList.setSelectionMode(
           ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        );
        DefaultListModel model = (DefaultListModel) this.monnaiesList.getModel();
        model.addElement("Livre sterling");
        model.addElement("Dollar Américain");
        model.addElement("Yen japonais");
        
        // Remplit les jours du mois
        int n_jours = Calendar.getInstance()
                              .getActualMaximum(Calendar.DAY_OF_MONTH);
        model = (DefaultListModel) this.meteoJoursList.getModel();
        for (int i = 1; i <= n_jours; i++) {
            model.addElement(String.valueOf(i));
        }
        
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
        ferryText = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        voyageurText = new javax.swing.JTextField();
        monnaiesCheck = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        monnaiesList = new javax.swing.JList();
        meteoCheck = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        meteoJoursList = new javax.swing.JList();
        taxesCheck = new javax.swing.JCheckBox();
        alcoolsCheck = new javax.swing.JCheckBox();
        parfumsCheck = new javax.swing.JCheckBox();
        tabacsCheck = new javax.swing.JCheckBox();
        envoieButton = new javax.swing.JButton();
        erreurLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Ferry:");

        jLabel2.setText("Nom voyageur:");

        voyageurText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voyageurTextActionPerformed(evt);
            }
        });

        monnaiesCheck.setText("Lister les cours monétaire");
        monnaiesCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monnaiesCheckActionPerformed(evt);
            }
        });

        monnaiesList.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(monnaiesList);

        meteoCheck.setText("Aficher la météo");
        meteoCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                meteoCheckActionPerformed(evt);
            }
        });

        jLabel3.setText("Jour(s) du mois:");

        meteoJoursList.setModel(new DefaultListModel());
        jScrollPane2.setViewportView(meteoJoursList);

        taxesCheck.setText("Lister les produits hors taxes");
        taxesCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taxesCheckActionPerformed(evt);
            }
        });

        alcoolsCheck.setText("Alcools ");

        parfumsCheck.setText("Parfums");

        tabacsCheck.setText("Tabacs");
        tabacsCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tabacsCheckActionPerformed(evt);
            }
        });

        envoieButton.setText("Envoyer la demande");
        envoieButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                envoieButtonActionPerformed(evt);
            }
        });

        erreurLabel.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ferryText, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(voyageurText, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(monnaiesCheck)
                                            .addComponent(meteoCheck))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addComponent(jLabel3))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)))
                            .addComponent(taxesCheck)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(alcoolsCheck)
                        .addGap(18, 18, 18)
                        .addComponent(parfumsCheck)
                        .addGap(18, 18, 18)
                        .addComponent(tabacsCheck))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(envoieButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(erreurLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(ferryText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(voyageurText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(monnaiesCheck)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(meteoCheck)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7)
                .addComponent(taxesCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(alcoolsCheck)
                    .addComponent(parfumsCheck)
                    .addComponent(tabacsCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(erreurLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(envoieButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void voyageurTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voyageurTextActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_voyageurTextActionPerformed

private void monnaiesCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monnaiesCheckActionPerformed
    this.monnaiesList.setEnabled(this.monnaiesCheck.isSelected());
}//GEN-LAST:event_monnaiesCheckActionPerformed

private void meteoCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_meteoCheckActionPerformed
    this.meteoJoursList.setEnabled(this.meteoCheck.isSelected());
}//GEN-LAST:event_meteoCheckActionPerformed

private void tabacsCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tabacsCheckActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_tabacsCheckActionPerformed

private void taxesCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taxesCheckActionPerformed
    boolean checked = this.taxesCheck.isSelected();
    this.alcoolsCheck.setEnabled(checked);
    this.parfumsCheck.setEnabled(checked);
    this.tabacsCheck.setEnabled(checked);
}//GEN-LAST:event_taxesCheckActionPerformed

private void envoieButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_envoieButtonActionPerformed
    if (this.ferryText.getText().isEmpty()
        || this.voyageurText.getText().isEmpty()) {
        this.erreurLabel.setText(
            "Veillez à renseigner les champs Ferry et Nom voyageur"
        );
    } else {
            try {
                this.erreurLabel.setText("");
                
                Document doc = this.genDocument();
                this.sendDocument(doc);
            } catch (TransformerConfigurationException ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerException ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
}//GEN-LAST:event_envoieButtonActionPerformed

    private Document genDocument()
            throws ParserConfigurationException, TransformerException 
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        DOMImplementation domImpl = parser.getDOMImplementation();

        DocumentType type = domImpl.createDocumentType(
            "demande_infos", null, "demande_infos.dtd"
        );
        Document doc = domImpl.createDocument(null, "demande_infos", type);
        Element root = doc.getDocumentElement();

        // Informations sur le ferry et l'utilisateur
        Element infos = doc.createElement("infos");
        infos.setAttribute("ferry", this.ferryText.getText());
        infos.setAttribute("voyageur", this.voyageurText.getText());
        root.appendChild(infos);

        // Ajoute les cours des monnaies
        Element monnaies = doc.createElement("monnaies");
        if (this.monnaiesCheck.isSelected()) {
            for (Object nom_obj : this.monnaiesList.getSelectedValues()) {
                Element monnaie = doc.createElement("monnaie");
                String nom = (String) nom_obj;
                monnaie.appendChild(doc.createTextNode(nom));
                monnaies.appendChild(monnaie);
            }
        }
        root.appendChild(monnaies);

        // Ajoute les cours des monnaies
        Element meteo = doc.createElement("meteo");
        if (this.meteoCheck.isSelected()) {
            for (Object nom_obj : this.meteoJoursList.getSelectedValues()) {
                Element jour = doc.createElement("jour");
                String nom = (String) nom_obj;
                jour.appendChild(doc.createTextNode(nom));
                meteo.appendChild(jour);
            }
        }
        root.appendChild(meteo);

        // Ajoute les produits tax free
        Element tax_free = doc.createElement("tax_free");
        if (this.taxesCheck.isSelected()) {
            if (this.alcoolsCheck.isSelected()) {
                tax_free.appendChild(doc.createElement("alcools"));
            }
            if (this.parfumsCheck.isSelected()) {
                tax_free.appendChild(doc.createElement("parfums"));
            }
            if (this.tabacsCheck.isSelected()) {
                tax_free.appendChild(doc.createElement("tabacs"));
            }
        }
        root.appendChild(tax_free);

        return doc;
    }

    private void sendDocument(Document doc)
            throws TransformerConfigurationException, TransformerException,
                   IOException
    {
        StringWriter out = new StringWriter();
        
        // Ecrit le document dans un stream en mémoire
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transform = transFactory.newTransformer();
        transform.setOutputProperty(OutputKeys.METHOD, "xml");
        transform.setOutputProperty(OutputKeys.INDENT,"yes");                
        Source input = new DOMSource(doc);
        Result output = new StreamResult(out);
        transform.transform(input, output);
        
        // Envoie le vecteur de bytes
        ObjectOutputStream obj_out = new ObjectOutputStream(this._sock_out);
        obj_out.writeObject(out.toString());
        this._sock_out.flush();
        
        byte reponse = (byte) this._sock_in.read();
        if (reponse == SUCCESS) {
            this.erreurLabel.setText("Demande réussie");
        } else {
            this.erreurLabel.setText("Erreur lors du traitement de la demande");
        }
    }

/**
 * @param args the command line arguments
 */
public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
     * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ClassNotFoundException ex) {
        java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {

        public void run() {
                try {
                    new MainForm().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    });
}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox alcoolsCheck;
    private javax.swing.JButton envoieButton;
    private javax.swing.JLabel erreurLabel;
    private javax.swing.JTextField ferryText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JCheckBox meteoCheck;
    private javax.swing.JList meteoJoursList;
    private javax.swing.JCheckBox monnaiesCheck;
    private javax.swing.JList monnaiesList;
    private javax.swing.JCheckBox parfumsCheck;
    private javax.swing.JCheckBox tabacsCheck;
    private javax.swing.JCheckBox taxesCheck;
    private javax.swing.JTextField voyageurText;
    // End of variables declaration//GEN-END:variables

}
