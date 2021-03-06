package mail_client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

    /**
 *
 * @author rapha
 */
public class MainForm extends javax.swing.JFrame {
    public final int REFRESH_RATE = 30_000; // 30 Secondes
    
    private Session _session;
    private final String _email;
    private Store _pop3Store = null;
    private ArrayList<Message> _msgs;
    
    private Cipher _cryptor;
    private Cipher _decryptor;
    private Folder _folder = null;
    private final String _serveurPop;
    private final String _utilisateur;
    private final String _motDePasse;
    
    /** Creates new form MainForm */
    public MainForm(String serveurPop, String serveurSMTP,
            String utilisateur, String motDePasse, String email)
            throws NoSuchProviderException, MessagingException,
            KeyStoreException, NoSuchAlgorithmException, IOException,
            CertificateException, UnrecoverableKeyException,
            NoSuchPaddingException, InvalidKeyException
    {
        Security.addProvider(
            new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
        
        loadSymetricKey();
        
        this._email = email;
        this._serveurPop = serveurPop;
        this._utilisateur = utilisateur;
        this._motDePasse = motDePasse;
       
        Properties property = System.getProperties();
        property.put("mail.smtp.host", serveurSMTP);
        property.put("file.encoding", "iso-8859-1");
        this._session = Session.getDefaultInstance(property, null);
        
        initComponents();
        
        // Lance un thread pour réactualiser la liste des messages
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (;;) {
                        listerMessages();
                        System.out.println("Actualisation des messages");
                        Thread.sleep(REFRESH_RATE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        
        // Ecoute les sélections faites sur la liste des messages pour
        // ouvrir la fenêtre de lecture
        ListSelectionModel selectionModel = this.messagesList.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                messageSelected(lse);
            }
        });
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
        messagesList = new javax.swing.JList();
        nouveauMessageButton = new javax.swing.JButton();
        actualiserButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Messagerie");

        jLabel1.setText("Liste des messages");

        messagesList.setModel(new DefaultListModel());
        messagesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        messagesList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                messagesListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(messagesList);

        nouveauMessageButton.setText("Nouveau message");
        nouveauMessageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauMessageButtonActionPerformed(evt);
            }
        });

        actualiserButton.setText("Rechercher de nouveaux messages");
        actualiserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualiserButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nouveauMessageButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(actualiserButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nouveauMessageButton)
                    .addComponent(actualiserButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void nouveauMessageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauMessageButtonActionPerformed
    new NouveauMessage(
        this, true, this._session, this._email, this._email, "", this._cryptor,
        this._decryptor
    ).setVisible(true);
}//GEN-LAST:event_nouveauMessageButtonActionPerformed

private void actualiserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualiserButtonActionPerformed
        try {
            this.listerMessages();
        } catch (MessagingException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_actualiserButtonActionPerformed

private void messagesListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_messagesListMouseClicked
    try {
        new LireMessage( 
            this, true, this._session, this._email,
            this._msgs.get(((JList) (evt.getSource())).getSelectedIndex()),
            this._cryptor, this._decryptor
        ).setVisible(true);
    } catch (Exception ex) {
        Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
    }
}//GEN-LAST:event_messagesListMouseClicked

private void messageSelected(ListSelectionEvent lse)
{
}

private synchronized void listerMessages()
        throws MessagingException
{
    if (this._pop3Store != null) {
        // FIXME: relance la connexion pour recevoir les nouveaux messages
        this._folder.close(true);
        this._pop3Store.close();
    }
    
    this._pop3Store = this._session.getStore("pop3");
    this._pop3Store.connect(this._serveurPop, this._utilisateur, this._motDePasse);
    this._folder = this._pop3Store.getFolder("INBOX");
    this._folder.open(Folder.READ_ONLY);
    
    Message[] msgs = this._folder.getMessages();
    // Inverse la liste des messages
    LinkedList<Message> msgs_reverse = new LinkedList<Message>();
    for (Message msg : msgs) {
        msgs_reverse.addFirst(msg);
    }
    
    if (this._folder.getNewMessageCount() > 0) {
       this.setTitle(
           "Messagerie ("+ this._folder.getNewMessageCount() +" nouveau(x) messages)"
       );
    } else {
       this.setTitle("Messagerie (Pas de nouveau message)");
    }
    
    this._msgs = new ArrayList<Message>();
    DefaultListModel model = (DefaultListModel) this.messagesList.getModel();
    model.clear();
    for (Message m : msgs_reverse) {
        this._msgs.add(m);
        model.addElement(
            m.getSentDate() + ": " + m.getSubject() + " de " + m.getFrom()[0]
        );
    }
}
/**
     * @param args the command line arguments
     */

    
    // Lit une lige depuis l'entrée standard
    public static String readLine() throws IOException
    {
        BufferedReader inStream = new BufferedReader (
            new InputStreamReader(System.in)
        );
        return inStream.readLine();
    }
    
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
                    Properties prop = new Properties();
                    prop.load(new FileInputStream("ferryinpres.properties"));
                    
////                    System.out.println("Utilisateur: ");
////                    String utilisateur = readLine();
//                    String utilisateur = prop.getProperty("MAIL_USER");
////                    System.out.println("Mot de passe: ");
////                    String mot_de_passe = readLine();
//                    String mot_de_passe = prop.getProperty("MAIL_PASS");
                    
                    new MainForm(
                        //inxs.aileinfo
                        prop.getProperty("MAIL_POP3"), 
                        prop.getProperty("MAIL_SMTP"),
                        prop.getProperty("MAIL_USER"), 
                        prop.getProperty("MAIL_PASS"),
                        prop.getProperty("MAIL_EMAIL")
                    ).setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualiserButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList messagesList;
    private javax.swing.JButton nouveauMessageButton;
    // End of variables declaration//GEN-END:variables

    // Charge la clé depuis le key store. génère une clé si elle n'existe pas
    private void loadSymetricKey()
            throws KeyStoreException, NoSuchAlgorithmException, IOException,
            CertificateException, UnrecoverableKeyException,
            NoSuchPaddingException, InvalidKeyException
    {
        KeyStore ks = KeyStore.getInstance("JCEKS");
        SecretKey key;
        try {          
            // Tente de charger une clé existante
            ks.load(new FileInputStream("keystore.jceks"), "password".toCharArray());
           
            key = (SecretKey) ks.getKey("symetric key", "password".toCharArray());
        } catch (FileNotFoundException ex) {
            // Clé non existante
            ks.load(null);
            
            // Génère une nouvelle clé symétrique
            KeyGenerator gen = KeyGenerator.getInstance("DES");
            gen.init(new SecureRandom());
            key = gen.generateKey();
            ks.setKeyEntry(
                "symetric key", (Key) key,
                "password".toCharArray(), null
            );
            
            // Enregistre le kestore
            FileOutputStream fos = new FileOutputStream("keystore.jceks");
            ks.store(fos, "password".toCharArray());
            fos.close();
        }
        
        this._cryptor = Cipher.getInstance("DES/ECB/PKCS5Padding");
        this._cryptor.init(Cipher.ENCRYPT_MODE, key);
        this._decryptor = Cipher.getInstance("DES/ECB/PKCS5Padding");
        this._decryptor.init(Cipher.DECRYPT_MODE, key);
    }
}