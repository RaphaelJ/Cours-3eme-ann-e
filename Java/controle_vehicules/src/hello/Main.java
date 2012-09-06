/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hello;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author rapha
 */
public class Main extends MIDlet implements CommandListener {
    private static final String SERVER_HOST = "10.59.14.3";
    private static final int SERVER_PORT = 39018;
    
    private DataInputStream in;
    private DataOutputStream out;
    
    private boolean midletPaused = false;
//<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Command exitCommand;
    private Command okCommand;
    private Command okCommand1;
    private Command okCommand2;
    private Command okCommand3;
    private Command okCommand4;
    private Command okCommand5;
    private Form LoginForm;
    private TextField textField;
    private Form CheckVehicule;
    private TextField textField1;
    private TextField textField2;
    private Alert MauvaisIdentifiants;
    private Form VehiculeRightPlace;
    private StringItem stringItem;
    private Form RecordVehicule;
    private TextField textField3;
    private TextField textField4;
    private StringItem stringItem2;
    private Form PoliceCheckBegin;
    private StringItem stringItem1;
//</editor-fold>//GEN-END:|fields|0|

    /**
     * The HelloMIDlet constructor.
     */
    public Main() {
    }

//<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
//</editor-fold>//GEN-END:|methods|0|
//<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initializes the application. It is called only once when the MIDlet is
     * started. The method is called before the
     * <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // write pre-initialize user code here
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
//</editor-fold>//GEN-END:|0-initialize|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
        switchDisplayable(null, getLoginForm());//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
    }//GEN-BEGIN:|3-startMIDlet|2|
//</editor-fold>//GEN-END:|3-startMIDlet|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
//</editor-fold>//GEN-END:|4-resumeMIDlet|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    /**
     * Switches a current displayable in a display. The
     * <code>display</code> instance is taken from
     * <code>getDisplay</code> method. This method is used by all actions in the
     * design for switching displayable.
     *
     * @param alert the Alert which is temporarily set to the display; if
     * <code>null</code>, then
     * <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
//</editor-fold>//GEN-END:|5-switchDisplayable|2|



//<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand ">//GEN-BEGIN:|18-getter|0|18-preInit
    /**
     * Returns an initialized instance of exitCommand component.
     *
     * @return the initialized component instance
     */
    public Command getExitCommand() {
        if (exitCommand == null) {//GEN-END:|18-getter|0|18-preInit
            // write pre-init user code here
            exitCommand = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|18-getter|1|18-postInit
            // write post-init user code here
        }//GEN-BEGIN:|18-getter|2|
        return exitCommand;
    }
//</editor-fold>//GEN-END:|18-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">//GEN-BEGIN:|7-commandAction|0|7-preCommandAction
    /**
     * Called by a system to indicated that a command has been invoked on a
     * particular displayable.
     *
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:|7-commandAction|0|7-preCommandAction
        // write pre-action user code here
        if (displayable == CheckVehicule) {//GEN-BEGIN:|7-commandAction|1|34-preAction
            if (command == okCommand1) {//GEN-END:|7-commandAction|1|34-preAction
                this.checkVehicule();                
//GEN-LINE:|7-commandAction|2|34-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|3|19-preAction
        } else if (displayable == LoginForm) {
            if (command == exitCommand) {//GEN-END:|7-commandAction|3|19-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|4|19-postAction
                // write post-action user code here
            } else if (command == okCommand) {//GEN-LINE:|7-commandAction|5|25-preAction
                connexion();
                
//GEN-LINE:|7-commandAction|6|25-postAction
                
            }//GEN-BEGIN:|7-commandAction|7|53-preAction
        } else if (displayable == MauvaisIdentifiants) {
            if (command == okCommand5) {//GEN-END:|7-commandAction|7|53-preAction
                // write pre-action user code here
                switchDisplayable(null, getLoginForm());//GEN-LINE:|7-commandAction|8|53-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|9|49-preAction
        } else if (displayable == PoliceCheckBegin) {
            if (command == okCommand4) {//GEN-END:|7-commandAction|9|49-preAction
                this.policeCheckBegin();
//GEN-LINE:|7-commandAction|10|49-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|11|45-preAction
        } else if (displayable == RecordVehicule) {
            if (command == okCommand3) {//GEN-END:|7-commandAction|11|45-preAction
                this.recordVehicule();
//GEN-LINE:|7-commandAction|12|45-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|13|41-preAction
        } else if (displayable == VehiculeRightPlace) {
            if (command == okCommand2) {//GEN-END:|7-commandAction|13|41-preAction
                this.vehiculeRightPlace();
               
//GEN-LINE:|7-commandAction|14|41-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|15|7-postCommandAction
        }//GEN-END:|7-commandAction|15|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|16|
//</editor-fold>//GEN-END:|7-commandAction|16|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: LoginForm ">//GEN-BEGIN:|14-getter|0|14-preInit
    /**
     * Returns an initialized instance of LoginForm component.
     *
     * @return the initialized component instance
     */
    public Form getLoginForm() {
        if (LoginForm == null) {//GEN-END:|14-getter|0|14-preInit
            // write pre-init user code here
            LoginForm = new Form("Welcome", new Item[]{getTextField()});//GEN-BEGIN:|14-getter|1|14-postInit
            LoginForm.addCommand(getExitCommand());
            LoginForm.addCommand(getOkCommand());
            LoginForm.setCommandListener(this);//GEN-END:|14-getter|1|14-postInit
            // write post-init user code here
        }//GEN-BEGIN:|14-getter|2|
        return LoginForm;
    }
//</editor-fold>//GEN-END:|14-getter|2|



//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand ">//GEN-BEGIN:|24-getter|0|24-preInit
    /**
     * Returns an initialized instance of okCommand component.
     *
     * @return the initialized component instance
     */
    public Command getOkCommand() {
        if (okCommand == null) {//GEN-END:|24-getter|0|24-preInit
            // write pre-init user code here
            okCommand = new Command("Ok", Command.OK, 0);//GEN-LINE:|24-getter|1|24-postInit
            // write post-init user code here
        }//GEN-BEGIN:|24-getter|2|
        return okCommand;
    }
//</editor-fold>//GEN-END:|24-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField ">//GEN-BEGIN:|27-getter|0|27-preInit
    /**
     * Returns an initialized instance of textField component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField() {
        if (textField == null) {//GEN-END:|27-getter|0|27-preInit
            // write pre-init user code here
            textField = new TextField("Num\u00E9ro de carte", null, 32, TextField.ANY);//GEN-LINE:|27-getter|1|27-postInit
            // write post-init user code here
        }//GEN-BEGIN:|27-getter|2|
        return textField;
    }
//</editor-fold>//GEN-END:|27-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: CheckVehicule ">//GEN-BEGIN:|28-getter|0|28-preInit
    /**
     * Returns an initialized instance of CheckVehicule component.
     *
     * @return the initialized component instance
     */
    public Form getCheckVehicule() {
        if (CheckVehicule == null) {//GEN-END:|28-getter|0|28-preInit
            
            CheckVehicule = new Form("V\u00E9rification d\'un v\u00E9hicule", new Item[]{getTextField1(), getTextField2()});//GEN-BEGIN:|28-getter|1|28-postInit
            CheckVehicule.addCommand(getOkCommand1());
            CheckVehicule.setCommandListener(this);//GEN-END:|28-getter|1|28-postInit
            // write post-init user code here
        }//GEN-BEGIN:|28-getter|2|
        return CheckVehicule;
    }
//</editor-fold>//GEN-END:|28-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: MauvaisIdentifiants ">//GEN-BEGIN:|30-getter|0|30-preInit
    /**
     * Returns an initialized instance of MauvaisIdentifiants component.
     *
     * @return the initialized component instance
     */
    public Alert getMauvaisIdentifiants() {
        if (MauvaisIdentifiants == null) {//GEN-END:|30-getter|0|30-preInit
            // write pre-init user code here
            MauvaisIdentifiants = new Alert("alert", "Mauvais identifiant", null, null);//GEN-BEGIN:|30-getter|1|30-postInit
            MauvaisIdentifiants.addCommand(getOkCommand5());
            MauvaisIdentifiants.setCommandListener(this);
            MauvaisIdentifiants.setTimeout(Alert.FOREVER);//GEN-END:|30-getter|1|30-postInit
            // write post-init user code here
        }//GEN-BEGIN:|30-getter|2|
        return MauvaisIdentifiants;
    }
//</editor-fold>//GEN-END:|30-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand1 ">//GEN-BEGIN:|33-getter|0|33-preInit
    /**
     * Returns an initialized instance of okCommand1 component.
     *
     * @return the initialized component instance
     */
    public Command getOkCommand1() {
        if (okCommand1 == null) {//GEN-END:|33-getter|0|33-preInit
            // write pre-init user code here
            okCommand1 = new Command("Ok", Command.OK, 0);//GEN-LINE:|33-getter|1|33-postInit
            // write post-init user code here
        }//GEN-BEGIN:|33-getter|2|
        return okCommand1;
    }
//</editor-fold>//GEN-END:|33-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField1 ">//GEN-BEGIN:|31-getter|0|31-preInit
    /**
     * Returns an initialized instance of textField1 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField1() {
        if (textField1 == null) {//GEN-END:|31-getter|0|31-preInit
            // write pre-init user code here
            textField1 = new TextField("Immatriculation", null, 32, TextField.ANY);//GEN-LINE:|31-getter|1|31-postInit
            // write post-init user code here
        }//GEN-BEGIN:|31-getter|2|
        return textField1;
    }
//</editor-fold>//GEN-END:|31-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField2 ">//GEN-BEGIN:|32-getter|0|32-preInit
    /**
     * Returns an initialized instance of textField2 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField2() {
        if (textField2 == null) {//GEN-END:|32-getter|0|32-preInit
            // write pre-init user code here
            textField2 = new TextField("Nationalit\u00E9", null, 32, TextField.ANY);//GEN-LINE:|32-getter|1|32-postInit
            // write post-init user code here
        }//GEN-BEGIN:|32-getter|2|
        return textField2;
    }
//</editor-fold>//GEN-END:|32-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: VehiculeRightPlace ">//GEN-BEGIN:|35-getter|0|35-preInit
    /**
     * Returns an initialized instance of VehiculeRightPlace component.
     *
     * @return the initialized component instance
     */
    public Form getVehiculeRightPlace() {
        if (VehiculeRightPlace == null) {//GEN-END:|35-getter|0|35-preInit
            // write pre-init user code here
            VehiculeRightPlace = new Form("D\u00E9placement du v\u00E9hicule", new Item[]{getStringItem()});//GEN-BEGIN:|35-getter|1|35-postInit
            VehiculeRightPlace.addCommand(getOkCommand2());
            VehiculeRightPlace.setCommandListener(this);//GEN-END:|35-getter|1|35-postInit
            // write post-init user code here
        }//GEN-BEGIN:|35-getter|2|
        return VehiculeRightPlace;
    }
//</editor-fold>//GEN-END:|35-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: RecordVehicule ">//GEN-BEGIN:|36-getter|0|36-preInit
    /**
     * Returns an initialized instance of RecordVehicule component.
     *
     * @return the initialized component instance
     */
    public Form getRecordVehicule() {
        if (RecordVehicule == null) {//GEN-END:|36-getter|0|36-preInit
            // write pre-init user code here
            RecordVehicule = new Form("V\u00E9hicule en infraction", new Item[]{getStringItem2(), getTextField3(), getTextField4()});//GEN-BEGIN:|36-getter|1|36-postInit
            RecordVehicule.addCommand(getOkCommand3());
            RecordVehicule.setCommandListener(this);//GEN-END:|36-getter|1|36-postInit
            // write post-init user code here
        }//GEN-BEGIN:|36-getter|2|
        return RecordVehicule;
    }
//</editor-fold>//GEN-END:|36-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: PoliceCheckBegin ">//GEN-BEGIN:|37-getter|0|37-preInit
    /**
     * Returns an initialized instance of PoliceCheckBegin component.
     *
     * @return the initialized component instance
     */
    public Form getPoliceCheckBegin() {
        if (PoliceCheckBegin == null) {//GEN-END:|37-getter|0|37-preInit
            // write pre-init user code here
            PoliceCheckBegin = new Form("Police appel\u00E9e", new Item[]{getStringItem1()});//GEN-BEGIN:|37-getter|1|37-postInit
            PoliceCheckBegin.addCommand(getOkCommand4());
            PoliceCheckBegin.setCommandListener(this);//GEN-END:|37-getter|1|37-postInit
            // write post-init user code here
        }//GEN-BEGIN:|37-getter|2|
        return PoliceCheckBegin;
    }
//</editor-fold>//GEN-END:|37-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand2 ">//GEN-BEGIN:|40-getter|0|40-preInit
    /**
     * Returns an initialized instance of okCommand2 component.
     *
     * @return the initialized component instance
     */
    public Command getOkCommand2() {
        if (okCommand2 == null) {//GEN-END:|40-getter|0|40-preInit
            // write pre-init user code here
            okCommand2 = new Command("Ok", Command.OK, 0);//GEN-LINE:|40-getter|1|40-postInit
            // write post-init user code here
        }//GEN-BEGIN:|40-getter|2|
        return okCommand2;
    }
//</editor-fold>//GEN-END:|40-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem ">//GEN-BEGIN:|39-getter|0|39-preInit
    /**
     * Returns an initialized instance of stringItem component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem() {
        if (stringItem == null) {//GEN-END:|39-getter|0|39-preInit
            // write pre-init user code here
            stringItem = new StringItem("D\u00E9placement du v\u00E9hicule", "Appuyez sur OK lorsque le v\u00E9hicule a retrouv\u00E9 sa place");//GEN-LINE:|39-getter|1|39-postInit
            // write post-init user code here
        }//GEN-BEGIN:|39-getter|2|
        return stringItem;
    }
//</editor-fold>//GEN-END:|39-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand3 ">//GEN-BEGIN:|44-getter|0|44-preInit
    /**
     * Returns an initialized instance of okCommand3 component.
     *
     * @return the initialized component instance
     */
    public Command getOkCommand3() {
        if (okCommand3 == null) {//GEN-END:|44-getter|0|44-preInit
            // write pre-init user code here
            okCommand3 = new Command("Ok", Command.OK, 0);//GEN-LINE:|44-getter|1|44-postInit
            // write post-init user code here
        }//GEN-BEGIN:|44-getter|2|
        return okCommand3;
    }
//</editor-fold>//GEN-END:|44-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField3 ">//GEN-BEGIN:|42-getter|0|42-preInit
    /**
     * Returns an initialized instance of textField3 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField3() {
        if (textField3 == null) {//GEN-END:|42-getter|0|42-preInit
            // write pre-init user code here
            textField3 = new TextField("Emplacement", null, 32, TextField.ANY);//GEN-LINE:|42-getter|1|42-postInit
            // write post-init user code here
        }//GEN-BEGIN:|42-getter|2|
        return textField3;
    }
//</editor-fold>//GEN-END:|42-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField4 ">//GEN-BEGIN:|43-getter|0|43-preInit
    /**
     * Returns an initialized instance of textField4 component.
     *
     * @return the initialized component instance
     */
    public TextField getTextField4() {
        if (textField4 == null) {//GEN-END:|43-getter|0|43-preInit
            // write pre-init user code here
            textField4 = new TextField("Mod\u00E8le", null, 32, TextField.ANY);//GEN-LINE:|43-getter|1|43-postInit
            // write post-init user code here
        }//GEN-BEGIN:|43-getter|2|
        return textField4;
    }
//</editor-fold>//GEN-END:|43-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand4 ">//GEN-BEGIN:|48-getter|0|48-preInit
    /**
     * Returns an initialized instance of okCommand4 component.
     *
     * @return the initialized component instance
     */
    public Command getOkCommand4() {
        if (okCommand4 == null) {//GEN-END:|48-getter|0|48-preInit
            // write pre-init user code here
            okCommand4 = new Command("Ok", Command.OK, 0);//GEN-LINE:|48-getter|1|48-postInit
            // write post-init user code here
        }//GEN-BEGIN:|48-getter|2|
        return okCommand4;
    }
//</editor-fold>//GEN-END:|48-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem1 ">//GEN-BEGIN:|47-getter|0|47-preInit
    /**
     * Returns an initialized instance of stringItem1 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem1() {
        if (stringItem1 == null) {//GEN-END:|47-getter|0|47-preInit
            // write pre-init user code here
            stringItem1 = new StringItem("Police appel\u00E9e", "Un email a \u00E9t\u00E9 envoy\u00E9 \u00E0 au poste de police le plus proche. Appuyez sur OK lorsque les policiers sont sur place.");//GEN-LINE:|47-getter|1|47-postInit
            // write post-init user code here
        }//GEN-BEGIN:|47-getter|2|
        return stringItem1;
    }
//</editor-fold>//GEN-END:|47-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: stringItem2 ">//GEN-BEGIN:|51-getter|0|51-preInit
    /**
     * Returns an initialized instance of stringItem2 component.
     *
     * @return the initialized component instance
     */
    public StringItem getStringItem2() {
        if (stringItem2 == null) {//GEN-END:|51-getter|0|51-preInit
            // write pre-init user code here
            stringItem2 = new StringItem("V\u00E9hicule non authoris\u00E9", "Le v\u00E9hicule n\'est pas authoris\u00E9. Donnez l\'emplacement et le mod\u00E8le de celui-ci pour faire prendre le relai aux services de police");//GEN-LINE:|51-getter|1|51-postInit
            // write post-init user code here
        }//GEN-BEGIN:|51-getter|2|
        return stringItem2;
    }
//</editor-fold>//GEN-END:|51-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand5 ">//GEN-BEGIN:|52-getter|0|52-preInit
    /**
     * Returns an initialized instance of okCommand5 component.
     *
     * @return the initialized component instance
     */
    public Command getOkCommand5() {
        if (okCommand5 == null) {//GEN-END:|52-getter|0|52-preInit
            // write pre-init user code here
            okCommand5 = new Command("Ok", Command.OK, 0);//GEN-LINE:|52-getter|1|52-postInit
            // write post-init user code here
        }//GEN-BEGIN:|52-getter|2|
        return okCommand5;
    }
//</editor-fold>//GEN-END:|52-getter|2|





    /**
     * Returns a display instance.
     *
     * @return the display instance.
     */
    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started. Checks whether the MIDlet have been
     * already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet();
        } else {
            initialize();
            startMIDlet();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     *
     * @param unconditional if true, then the MIDlet has to be unconditionally
     * terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }
    
    
    public void connexion()
    {
        (new Thread() {
            public void run()
            {
                try {
                    SocketConnection connexion = (SocketConnection) Connector.open(
                            "socket://"+SERVER_HOST+":"+SERVER_PORT
                    );

                    out = connexion.openDataOutputStream();
                    in = connexion.openDataInputStream();        

                    out.writeUTF(textField.getString());
                    out.flush();

                    switch (in.readChar()) {
                    case 'A':
                        switchDisplayable(null, getCheckVehicule());
                        break;
                    case 'F': default:
                        connexion.close();
                        switchDisplayable(null, getMauvaisIdentifiants());
                        break;
                    } 
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
    
    public void checkVehicule()
    { 
        (new Thread() {
            public void run()
            {
                try {
                    String immatriculation = textField1.getString();
                    String nationalite = textField2.getString();

                    out.writeUTF(immatriculation);
                    out.writeUTF(nationalite);
                    out.flush();

                    switch (in.readChar()) {
                    case 'A':
                        String navire = in.readUTF();
                        String traversee = in.readUTF();
                        
                        switchDisplayable(null, getVehiculeRightPlace());
                        stringItem.setText(
                            "Le véhicule devrait se trouver dans la file pour " +
                            "la traversée " + traversee + " du navire "
                            + navire + ".\n" +
                            "Appuyez sur OK lorsque le véhicule a retrouvé sa place."
                        );
                        break;
                    case 'F': default:
                        switchDisplayable(null, getRecordVehicule());
                        break;
                    }           
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void recordVehicule()
    {
        (new Thread() {
            public void run()
            {
                try {
                    String emplacement = textField3.getString();
                    String modele = textField4.getString();        

                    out.writeUTF(emplacement);
                    out.writeUTF(modele);
                    out.flush();

                    switchDisplayable(null, getPoliceCheckBegin());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void policeCheckBegin() {
        (new Thread() {
            public void run()
            {
                try {
                    out.writeChar('A');
                    out.flush();

                    switchDisplayable(null, getCheckVehicule());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void vehiculeRightPlace() {
        (new Thread() {
            public void run()
            {
                try {
                    out.writeChar('A');
                    out.flush();
                    
                    switchDisplayable(null, getCheckVehicule());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
}
