package mail_client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.crypto.Cipher;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;

import sun.misc.BASE64Encoder;

/**
 *
 * @author rapha
 */
public class NouveauMessage extends javax.swing.JDialog {
    private Session _smtpSession;
    private String _email;
    
    private final Cipher _cryptor;
    private final Cipher _decryptor;
    
    private LinkedList<String> _piecesJointes = new LinkedList<String>();

    /** Creates new form NouveauMessage */
    public NouveauMessage(java.awt.Frame parent, boolean modal, 
            Session smtpSession, String email, Cipher cryptor, Cipher decryptor)
    {
        this(parent, modal, smtpSession, email, "", "", cryptor, decryptor);
    }

    NouveauMessage(java.awt.Frame parent, boolean modal, 
            Session smtpSession, String email, String destinataire,
            String sujet, Cipher cryptor, Cipher decryptor)
    {
        super(parent, modal);
        this._smtpSession = smtpSession;
        this._email = email;
        
        this._cryptor = cryptor;
        this._decryptor = decryptor;
        
        initComponents();
        
        this.destinataireText.setText(destinataire);
        this.sujetText.setText(sujet);
        
        this.cryptageLabel.setVisible(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        sujetText = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        destinataireText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageText = new javax.swing.JTextArea();
        envoyerButton = new javax.swing.JButton();
        erreurLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        messageTypeCombo = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        piecesJointesList = new javax.swing.JList();
        ajoutPieceButton = new javax.swing.JButton();
        cryptageLabel = new javax.swing.JLabel();

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nouveau message");

        jLabel1.setText("Sujet:");

        jLabel2.setText("Destinataire:");

        destinataireText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                destinataireTextActionPerformed(evt);
            }
        });

        jLabel3.setText("Message:");

        messageText.setColumns(20);
        messageText.setRows(5);
        jScrollPane1.setViewportView(messageText);

        envoyerButton.setText("Envoyer");
        envoyerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                envoyerButtonActionPerformed(evt);
            }
        });

        erreurLabel.setForeground(new java.awt.Color(204, 0, 0));

        jLabel4.setText("Type message:");

        messageTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FRONTIER_INFO", "FRONTIER_INFO_ACK", "FRONTIER_WANTED", "FRONTIER_WANTED_ACK", "FRONTIER_WANTED_TOO_LATE" }));
        messageTypeCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                messageTypeComboItemStateChanged(evt);
            }
        });
        messageTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messageTypeComboActionPerformed(evt);
            }
        });

        jLabel5.setText("Pièces jointes:");

        piecesJointesList.setModel(new DefaultListModel());
        jScrollPane3.setViewportView(piecesJointesList);

        ajoutPieceButton.setText("Ajouter une pièce jointe");
        ajoutPieceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajoutPieceButtonActionPerformed(evt);
            }
        });

        cryptageLabel.setForeground(new java.awt.Color(102, 153, 0));
        cryptageLabel.setText("Le contenu du message sera crypté et signé");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(messageTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(381, 381, 381))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sujetText, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
                                    .addComponent(destinataireText, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 338, Short.MAX_VALUE)
                                .addComponent(cryptageLabel)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(erreurLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(envoyerButton)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addContainerGap(631, Short.MAX_VALUE))
                    .addComponent(ajoutPieceButton)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(messageTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(destinataireText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sujetText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cryptageLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ajoutPieceButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(erreurLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(envoyerButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void destinataireTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_destinataireTextActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_destinataireTextActionPerformed

private void envoyerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_envoyerButtonActionPerformed
    try {
        MimeMessage msg = new MimeMessage(this._smtpSession);
        msg.setFrom(new InternetAddress(this._email));
        msg.setRecipient(
            Message.RecipientType.TO,
            new InternetAddress(this.destinataireText.getText())
        );
        
        msg.setSubject(
            this.messageTypeCombo.getSelectedItem() + ": "
            + this.sujetText.getText()
        );
        
        Multipart parts = new MimeMultipart();
        
        String message;
        // Crypte le message si FRONTIER_WANTED
        if (this.messageTypeCombo.getSelectedItem().equals("FRONTIER_WANTED")) {
            // Crypte et hash la partie texte du message
            MessageCrypte msg_crypte = new MessageCrypte(
                this.messageText.getText(),
                this.messageText.getText().hashCode()
            );
            
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);   
            out.writeObject(msg_crypte);
            
            byte[] bytes = bos.toByteArray();
            System.out.println("Bytes: " + bytes.length);
            byte[] crypted_bytes = this._cryptor.doFinal(bytes);
            out.close();
            bos.close();
           
            System.out.println("Crypted bytes: " + crypted_bytes.length);
            
            // Encode le message crypté en base64
            BASE64Encoder encoder = new BASE64Encoder();
            message = encoder.encode(crypted_bytes);
            System.out.println("Base64: "+message);
        } else {
            // Ajout de la partie texte du message
            message = this.messageText.getText();
        }

        MimeBodyPart body = new MimeBodyPart();
        body.setText(message);
        parts.addBodyPart(body);
        
        // Ajout des pièces jointes
        for (String path : this._piecesJointes) {
            System.out.println("Piece jointe: " + path);
            MimeBodyPart piece = new MimeBodyPart();
            DataSource so = new FileDataSource(path);
            piece.setDataHandler(new DataHandler(so));
            piece.setFileName(new File(path).getName());
            parts.addBodyPart(piece);
        }
        
        msg.setContent(parts);
        
        Transport.send(msg);
        
        this.dispose();
    } catch (Exception ex) {
        this.erreurLabel.setText(ex.getMessage());
        ex.printStackTrace();
    }
}//GEN-LAST:event_envoyerButtonActionPerformed

private void ajoutPieceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajoutPieceButtonActionPerformed
    JFileChooser fc = new JFileChooser();
    int returnVal = fc.showOpenDialog(this);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        
        DefaultListModel model = (DefaultListModel) this.piecesJointesList.getModel();
        this._piecesJointes.add(file.getPath());
        model.addElement(file.getName());
    }
}//GEN-LAST:event_ajoutPieceButtonActionPerformed

private void messageTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messageTypeComboActionPerformed
    
}//GEN-LAST:event_messageTypeComboActionPerformed

private void messageTypeComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_messageTypeComboItemStateChanged
    // Affiche le message affirmant que le contenu du message sera crypté
    // et signé.
    
    this.cryptageLabel.setVisible(
        this.messageTypeCombo.getSelectedItem().equals("FRONTIER_WANTED")
    );
}//GEN-LAST:event_messageTypeComboItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ajoutPieceButton;
    private javax.swing.JLabel cryptageLabel;
    private javax.swing.JTextField destinataireText;
    private javax.swing.JButton envoyerButton;
    private javax.swing.JLabel erreurLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea messageText;
    private javax.swing.JComboBox messageTypeCombo;
    private javax.swing.JList piecesJointesList;
    private javax.swing.JTextField sujetText;
    // End of variables declaration//GEN-END:variables
}