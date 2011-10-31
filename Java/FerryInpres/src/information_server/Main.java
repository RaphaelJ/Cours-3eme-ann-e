/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package information_server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author rapha
 */
public class Main {
    public static void main(String args[]) throws IOException
    {
        ServerSocket server_sock = new ServerSocket(39005);
        
        for (;;) {
            Socket sock = server_sock.accept();
            System.out.println("Accept");
            InputStream in = sock.getInputStream();
            OutputStream outoutputStream = sock.getOutputStream();
            
            try {
                ParseDemande(in);
            } catch (Exception e) {
                
            }
        }
    }
        
    public static void ParseDemande(InputStream in)
            throws ParserConfigurationException, SAXException, IOException
    {
        // Parse le XML recu
        SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
        XMLReader parser = factory.newSAXParser().getXMLReader();

        SaxParsingHandler handler = new SaxParsingHandler();
        parser.setContentHandler(handler);
        parser.setErrorHandler(handler);
        
        System.out.println("Parsing ...");

        parser.parse(new InputSource(in));
        
        System.out.println("Alcools: " + handler.alcools);
        System.out.println("Tabacs: " + handler.tabacs);
        System.out.println("Parfums: " + handler.parfums);
        
        System.out.println("Jours: " + handler.meteo_jours.size());
        System.out.println("Monnaies: " + handler.monnaies.size());
    }
}

class SaxParsingHandler extends DefaultHandler {
    private StringBuilder _str_acc;

    public String ferry;
    public String voyageur;

    public LinkedList<String> monnaies = new LinkedList<String>();
    public LinkedList<String> meteo_jours = new LinkedList<String>();
    public boolean alcools = false;
    public boolean parfums = false;
    public boolean tabacs = false;

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException
    {
        if ("infos".equals(localName)) {
            this.ferry = atts.getValue("ferry");
            this.voyageur = atts.getValue("voyageur");
        } else if ("alcools".equals(localName)) {
            this.alcools = true;
        } else if ("parfums".equals(localName)) {
            this.parfums = true;
        } else if ("tabacs".equals(localName)) {
            this.tabacs = true;
        }

        this._str_acc.setLength(0);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException
    {
        if ("monnaie".equals(localName))
            this.monnaies.add(this._str_acc.toString());
        else if ("jour".equals(localName))
            this.meteo_jours.add(this._str_acc.toString());

    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException
    {
        this._str_acc.append(ch, start, length);
    }

//    @Override
//    public void ignorableWhitespace(char[] ch, int start, int length)
//            throws SAXException {
//        String s = new String(ch, start, length);
//        ArrayList<String> codePoints = new ArrayList<String>();
//        for (int i = 0; i < s.length(); i++) {
//            int codePoint = s.codePointAt(i);
//            codePoints.add(String.format("0x%X", codePoint));
//            if (i >= 0xD800 && i <= 0xDBFF) {
//                // useless in this specific case
//                i++;
//            }
//        }
//        out.printf("ignorableWhitespace. [%s]\n", join(codePoints, ", "));
//    }

}