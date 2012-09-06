package mail_client;

import java.awt.Frame;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.swing.DefaultListModel;
import sun.misc.BASE64Decoder;

/**
 *
 * @author rapha
 */
public class LireMessage extends javax.swing.JDialog {
    private final Session _smtpSession;
    private final String _email;
    private final Message _msg;
    private final Frame _parent;
    
    private final Cipher _cryptor;
    private final Cipher _decryptor;

    /** Creates new form LireMessage */
    public LireMessage(java.awt.Frame parent, boolean modal,
            Session smtpSession, String email, Message msg, Cipher cryptor,
            Cipher decryptor)
            throws MessagingException, IOException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException 
    {
        super(parent, modal);
        this._parent = parent;
        this._smtpSession = smtpSession;
        this._email = email;
        this._msg = msg;
        
        this._cryptor = cryptor;
        this._decryptor = decryptor;
        
        initComponents();
        
        this.emetteurLabel.setText(msg.getFrom()[0].toString());
        this.sujetLabel.setText(msg.getSubject());
        
        Object content = msg.getContent();
        
        if (content instanceof Multipart) { // Récupération du texte et des pièces jointes
            Multipart parts = (Multipart) content;

            for (int i = 0; i < parts.getCount(); i++) {
                Part p = parts.getBodyPart(i);

                if (p.isMimeType("text/plain")) {
                    // Texte du message

                    if (msg.getSubject().startsWith("FRONTIER_WANTED")) {
                        // Texte crypté

                        // Décode le message en Base64
                        System.out.println("Base64: " + (String) p.getContent());
                        BASE64Decoder decoder = new BASE64Decoder();
                        byte[] crypted_bytes = decoder.decodeBuffer((String) p.getContent());

                        // Décrypte le message
                        byte[] bytes_message = this._decryptor.doFinal(crypted_bytes);
                        System.out.println(bytes_message.length);
                        ByteArrayInputStream bis = new ByteArrayInputStream(bytes_message);
                        ObjectInput in = new ObjectInputStream(bis);   
                        MessageCrypte obj_message = (MessageCrypte) in.readObject();

                        // Vérifie le digest
                        if (obj_message.getHash() == obj_message.getMessage().hashCode()) {
                            // Message complet
                            this.messagearea.setText(obj_message.getMessage());
                        } else {
                            this.messagearea.setText("Message corrompu !");
                        }

                    } else {
                         this.messagearea.setText((String) p.getContent());
                    }
                } else if (p.getDisposition() != null
                    && p.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)) {
                    // Enregistre la pièce jointe

                    InputStream is = p.getInputStream();
                    FileOutputStream fos = new FileOutputStream(
                        new File("pieces_jointes", p.getFileName())
                    );

                    int c;
                    while ((c = is.read()) != -1)
                        fos.write(c);

                    fos.close();

                    DefaultListModel model = (DefaultListModel) this.piecesJointesList.getModel();
                    model.addElement(p.getFileName());
                }
            }
        } else { // Message texte
            // Texte du message
            if (msg.getSubject().startsWith("FRONTIER_WANTED")) {
                // Texte crypté

                // Décode le message en Base64
                System.out.println("Base64: " + (String) content);
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] crypted_bytes = decoder.decodeBuffer((String) content);

                // Décrypte le message
                byte[] bytes_message = this._decryptor.doFinal(crypted_bytes);
                System.out.println(bytes_message.length);
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes_message);
                ObjectInput in = new ObjectInputStream(bis);   
                MessageCrypte obj_message = (MessageCrypte) in.readObject();

                // Vérifie le digest
                if (obj_message.getHash() == obj_message.getMessage().hashCode()) {
                    // Message complet
                    this.messagearea.setText(obj_message.getMessage());
                } else {
                    this.messagearea.setText("Message corrompu !");
                }

            } else {
                 this.messagearea.setText((String) content);
            }
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
        emetteurLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        sujetLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messagearea = new javax.swing.JTextArea();
        repondreButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        piecesJointesList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lire un message");

        jLabel1.setText("Emetteur:");

        emetteurLabel.setText("jLabel2");

        jLabel2.setText("Sujet:");

        sujetLabel.setText("jLabel3");

        jLabel3.setText("Message:");

        messagearea.setColumns(20);
        messagearea.setRows(5);
        jScrollPane1.setViewportView(messagearea);

        repondreButton.setText("Répondre");
        repondreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repondreButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Pièces jointes:");

        piecesJointesList.setModel(new DefaultListModel());
        jScrollPane2.setViewportView(piecesJointesList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sujetLabel)
                            .addComponent(emetteurLabel)))
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(repondreButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(emetteurLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(sujetLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(repondreButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void repondreButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repondreButtonActionPerformed
    try {
        new NouveauMessage(
            this._parent, true, this._smtpSession, this._email,
            this._msg.getFrom()[0].toString(),
            "RE: " + this._msg.getSubject(), this._cryptor, this._decryptor
        ).setVisible(true);
    } catch (MessagingException ex) {
        Logger.getLogger(LireMessage.class.getName()).log(Level.SEVERE, null, ex);
    }
}//GEN-LAST:event_repondreButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel emetteurLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea messagearea;
    private javax.swing.JList piecesJointesList;
    private javax.swing.JButton repondreButton;
    private javax.swing.JLabel sujetLabel;
    // End of variables declaration//GEN-END:variables
}
