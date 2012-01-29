/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SOAPWebService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author rapha
 */
@WebService(serviceName = "SOAPWebService")
public class SOAPWebService {
    private static final String user = "rapha";
    private static final String pass = "pass";
    
    @Resource
    WebServiceContext context;
    
    private void _auth() throws AuthException
    {
//        MessageContext msg = context.getMessageContext();
// 
//	//get detail from request headers
//        Map headers = (Map) msg.get(MessageContext.HTTP_REQUEST_HEADERS);
//        List userList = (List) headers.get("Username");
//        List passList = (List) headers.get("Password");
//        
//        if (userList == null || passList == null
//            || user.equals(userList.get(0).toString())
//            || pass.equals(passList.get(0).toString())) {
//            throw new AuthException();
//        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "HistoriqueVols")
    public @WebResult(name="Vol") Vol[] HistoriqueVol() throws AuthException
    {
        this._auth();
        
        try {
            return new Database().historiqueVols();
        } catch (Exception ex) {
            Logger.getLogger(SOAPWebService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Vol[0];
    }
    
    @WebMethod(operationName = "ProchainsVols")
    public @WebResult(name="Vol") Vol[] ProchainsVols() throws AuthException
    {
        this._auth();
        
        try {
            return new Database().prochainsVols();
        } catch (Exception ex) {
            Logger.getLogger(SOAPWebService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Vol[0];
    }
    
    @WebMethod(operationName = "Vols")
    public @WebResult(name="Vol") Vol[] VolsDate(Date date) throws AuthException
    {
        this._auth();
        
        try {
            return new Database().volsDate(date);
        } catch (Exception ex) {
            Logger.getLogger(SOAPWebService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Vol[0];
    }
}