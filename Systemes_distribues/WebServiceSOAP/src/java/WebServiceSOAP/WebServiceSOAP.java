/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServiceSOAP;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.*;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author rapha
 */
@WebService(serviceName = "WebServiceSOAP")
@HandlerChain(file = "WebServiceSOAP_handler.xml")
public class WebServiceSOAP {
    private static final String user = "rapha";
    private static final String pass = "pass";
    
    @Resource
    WebServiceContext context;

    /**
     * Web service operation
     */
    @WebMethod(operationName = "HistoriqueVols")
    public @WebResult(name="Vol") Vol[] HistoriqueVol() throws AuthException
    {
        try {
            return new Database().historiqueVols();
        } catch (Exception ex) {
            Logger.getLogger(WebServiceSOAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Vol[0];
    }
    
    @WebMethod(operationName = "ProchainsVols")
    public @WebResult(name="Vol") Vol[] ProchainsVols() throws AuthException
    {
        try {
            return new Database().prochainsVols();
        } catch (Exception ex) {
            Logger.getLogger(WebServiceSOAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Vol[0];
    }
    
    @WebMethod(operationName = "Vols")
    public @WebResult(name="Vol") Vol[] VolsDate(@WebParam(name = "date") Date date)
            throws AuthException
    {
        try {
            return new Database().volsDate(date);
        } catch (Exception ex) {
            Logger.getLogger(WebServiceSOAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Vol[0];
    }
}