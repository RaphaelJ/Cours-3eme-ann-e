/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package information_server;

import information_server.MeteoResponseProtocol.Temps;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;


/**
 *
 * @author rapha
 */
public class MainServer {
        
    public static void main(String args[])
            throws IOException, ParserConfigurationException, SAXException,
                   ClassNotFoundException, TransformerConfigurationException, 
                   TransformerException
    {
        ServerSocket server_sock = new ServerSocket(Config.MAIN_PORT);
        
        for (;;) {
            System.out.println("En attente d'un nouveau client");
            Socket sock = server_sock.accept();
            ObjectInputStream in = new ObjectInputStream(
                sock.getInputStream()
            );
            ObjectOutputStream out = new ObjectOutputStream(
                sock.getOutputStream()
            );
            
            try {
                SaxParsingHandler handler = parseDemande(in);
                TreeMap<String, Double> monnaies = demandeMonnaies(
                    handler.monnaies.toArray(new String[0])
                );
                TreeMap<Integer, Temps> temps = demandeMeteo(
                    handler.meteo_jours.toArray(new Integer[0])
                );
                FreetaxProduit[] produits = demandeFreetax(
                    handler.alcools, handler.parfums, handler.tabacs
                );
                String filename = exportXSLT(monnaies, temps, produits);
                out.writeObject(filename);
            } catch (Exception e) { }
            
            sock.close();
        }
    }
        
    private static SaxParsingHandler parseDemande(ObjectInputStream obj_in)
            throws ParserConfigurationException, SAXException, IOException,
                   ClassNotFoundException
    {
        System.out.println("Réception de la requête");
        
        String str_buffer = (String) obj_in.readObject();
        
        // Parse le XML recu
        SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
        XMLReader parser = factory.newSAXParser().getXMLReader();

        SaxParsingHandler handler = new SaxParsingHandler();
        parser.setContentHandler(handler);
        parser.setErrorHandler(handler);
        
        System.out.println("Parsing de la requête");

        parser.parse(new InputSource(new StringReader(str_buffer)));
        
        return handler;
    }
    
    private static TreeMap<String, Double> demandeMonnaies(String[] monnaies)
            throws IOException, ClassNotFoundException 
    {        
        if (monnaies.length > 0) {
            System.out.println("Demande au serveur des monnaies");
            
            Socket monnaies_sock = new Socket(
                Config.MONNAIES_SERVEUR, Config.MONNAIES_PORT
            );
            ObjectInputStream monnaies_in = new ObjectInputStream(
                monnaies_sock.getInputStream()
            );
            ObjectOutputStream monnaies_out = new ObjectOutputStream( 
                monnaies_sock.getOutputStream()
            );

            MonnaiesQueryProtocol query = new MonnaiesQueryProtocol(monnaies);

            monnaies_out.writeObject(query);
            monnaies_out.flush();

            MonnaiesResponseProtocol response
                    = (MonnaiesResponseProtocol) monnaies_in.readObject();

            for (String i : monnaies) {
                System.out.println(i + " " + response.getMonnaies().get(i));
            }
            return response.getMonnaies();
        } else {
            return new TreeMap<String, Double>();
        }
    }

    private static TreeMap<Integer, Temps> demandeMeteo(Integer[] meteo_jours)
            throws IOException, ClassNotFoundException 
    {        
        if (meteo_jours.length > 0) {
            System.out.println("Demande au serveur météo");
            
            Socket meteo_sock = new Socket(
                Config.METEO_SERVEUR, Config.METEO_PORT
            );
            ObjectInputStream meteo_in = new ObjectInputStream(
                meteo_sock.getInputStream()
            );
            ObjectOutputStream meteo_out = new ObjectOutputStream( 
                meteo_sock.getOutputStream()
            );

            MeteoQueryProtocol query = new MeteoQueryProtocol(meteo_jours);

            meteo_out.writeObject(query);
            meteo_out.flush();

            MeteoResponseProtocol response
                    = (MeteoResponseProtocol) meteo_in.readObject();

            for (Integer i : meteo_jours) {
                System.out.println(i + " " + response.getResultats().get(i));
            }
            return response.getResultats();
        } else {
            return new TreeMap<Integer, Temps>();
        }
    }

    private static FreetaxProduit[] demandeFreetax(boolean alcools,
            boolean parfums, boolean tabacs)
            throws UnknownHostException, IOException, ClassNotFoundException
    {
        if (alcools || parfums || tabacs) {
            System.out.println("Demande au serveur freetax");
            
            Socket freetax_sock = new Socket(
                Config.FREETAX_SERVEUR, Config.FREETAX_PORT
            );
            ObjectInputStream freetax_in = new ObjectInputStream(
                freetax_sock.getInputStream()
            );
            ObjectOutputStream freetax_out = new ObjectOutputStream( 
                freetax_sock.getOutputStream()
            );

            FreetaxQueryProtocol query = new FreetaxQueryProtocol(
                alcools, parfums, tabacs
            );

            freetax_out.writeObject(query);
            freetax_out.flush();
            
            FreetaxResponseProtocol response
                    = (FreetaxResponseProtocol) freetax_in.readObject();

            for (FreetaxProduit p : response.getProduits()) {
                System.out.println(
                    p.getCategorie() + ": " + p.getNom() +
                    " - Quantité: " + p.getQuantite() + " - Prix: " +
                    p.getPrix()
                );
            }
            return response.getProduits();
        } else {
            return new FreetaxProduit[0];
        }
    }
    
    private static String exportXSLT(TreeMap<String, Double> monnaies,
            TreeMap<Integer, Temps> temps, FreetaxProduit[] produits)
            throws ParserConfigurationException,
            TransformerConfigurationException, TransformerException, IOException
    {
        // Construit le document XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        DOMImplementation domImpl = parser.getDOMImplementation();

        DocumentType type = domImpl.createDocumentType(
            "infos", null, "infos.dtd"
        );
        Document doc = domImpl.createDocument(null, "infos", type);
        Element root = doc.getDocumentElement();

        // Ajoute les cours des monnaies demandées
        Element monnaies_node = doc.createElement("monnaies");
        for (Map.Entry<String, Double> e : monnaies.entrySet()) {
            Element monnaie = doc.createElement("monnaie");
            monnaie.setAttribute("nom", e.getKey());
            monnaie.setAttribute("valeur", e.getValue().toString());
            monnaies_node.appendChild(monnaie);
        }
        root.appendChild(monnaies_node);

        // Ajoute les jours et la météo
        Element meteo = doc.createElement("meteo");
        for (Map.Entry<Integer, Temps> e : temps.entrySet()) {
            Element jour = doc.createElement("jour");
            jour.setAttribute("numero", e.getKey().toString());
            jour.setAttribute("temps", e.getValue().toString());
            meteo.appendChild(jour);
        }
        root.appendChild(meteo);
        
        // Ajoute les produits hors taxes
        Element freetax = doc.createElement("freetax");
        for (FreetaxProduit p : produits) {
            Element produit = doc.createElement("produit");
            produit.setAttribute("nom", p.getNom());
            produit.setAttribute("quantite", p.getQuantite());
            produit.setAttribute("prix", p.getPrix());
            freetax.appendChild(produit);
        }
        root.appendChild(freetax);
        
        // Ecrit le document dans un stream en mémoire
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT,"yes");
        StringWriter xml_str = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(xml_str));
        System.out.println(xml_str);
        
        // Convertit le document avec XSLT
        StringWriter html_str_buffer = new StringWriter();
        transformer = transFactory.newTransformer(
            new StreamSource(new File("infos.xsl"))
        );
        transformer.transform(new DOMSource(doc), new StreamResult(html_str_buffer));
        
        // Enregistre le document html
        // Utilise le hash comme nom de fichier
        String html_str = html_str_buffer.toString();
        File filename = new File(
            new File(Config.TOMCAT),
            String.valueOf(html_str.hashCode()) +".html"
        );
        FileWriter destFile = new FileWriter(filename);
        destFile.write(html_str);
        destFile.close();
        System.out.println(filename);
        
        return filename.toString();
    }    
}

class SaxParsingHandler extends DefaultHandler {
    private StringBuilder _str_acc = new StringBuilder();

    public String ferry;
    public String voyageur;

    public LinkedList<String> monnaies = new LinkedList<String>();
    public LinkedList<Integer> meteo_jours = new LinkedList<Integer>();
    public boolean alcools = false;
    public boolean parfums = false;
    public boolean tabacs = false;

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException
    {
        if ("infos".equals(qName)) {
            this.ferry = atts.getValue("ferry");
            this.voyageur = atts.getValue("voyageur");
        } else if ("alcools".equals(qName)) {
            this.alcools = true;
        } else if ("parfums".equals(qName)) {
            this.parfums = true;
        } else if ("tabacs".equals(qName)) {
            this.tabacs = true;
        }

        this._str_acc.setLength(0);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException
    {
        if ("monnaie".equals(qName))
            this.monnaies.add(this._str_acc.toString());
        else if ("jour".equals(qName))
            this.meteo_jours.add(Integer.parseInt(this._str_acc.toString()));

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