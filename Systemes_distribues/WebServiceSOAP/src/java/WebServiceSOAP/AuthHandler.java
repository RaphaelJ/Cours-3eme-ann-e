/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServiceSOAP;

import java.util.Map.Entry;
import java.util.Set;
import javax.mail.MessageContext;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author rapha
 */
public class AuthHandler implements SOAPHandler<SOAPMessageContext>
{
    public Set<QName> getHeaders()
    {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext context)
    {
//        for (Entry<String, Object> e : context.entrySet()) {
//            System.err.println("Key: "+e.getKey());
//            System.err.println("Value: "+e.getValue());
//            
//        }
//        
//       System.err.println(msg.get(com.sun.xml.wss.XWSSConstants.USERNAME_PROPERTY).toString());
        
        return true;
    }

    public boolean handleFault(SOAPMessageContext context)
    {
        return true;
    }

    @Override
    public void close(javax.xml.ws.handler.MessageContext context) {
    }
}