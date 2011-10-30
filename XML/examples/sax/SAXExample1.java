import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException; // JAXP
import javax.xml.parsers.SAXParser; // JAXP
import javax.xml.parsers.SAXParserFactory; // JAXP
import org.xml.sax.*; // SAX
import org.xml.sax.helpers.DefaultHandler; // SAX

public class SAXExample1 {

    public static void main(String args[]) throws ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        System.out.println(factory.getClass().getName());

        SAXParser saxParser = factory.newSAXParser();
        System.out.println(saxParser.getClass().getName());
        
        XMLReader xmlReader = saxParser.getXMLReader();
        System.out.println(xmlReader.getClass().getName());

        DefaultHandler handler = new DefaultHandler();
        xmlReader.setContentHandler(handler);
        xmlReader.setErrorHandler(handler);

        try {
            xmlReader.parse(args[0]);
            System.out.println("Everything is ok !");
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } catch (SAXException e) {
            System.err.println("Parsing error: " + e.getMessage());
        }
    }
}