
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

class MyErrorHandler implements ErrorHandler {

    public void warning(SAXParseException exception) throws SAXException {
        System.err.println("*** warning called !");
        System.err.println(exception.getMessage());
        throw exception;
    }

    public void error(SAXParseException exception) throws SAXException {
        System.err.println("*** error called !");
        System.err.println(exception.getMessage());
        //throw exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        System.err.println("*** fatalError called !");
        System.err.println(exception.getMessage());
        throw exception;

    }
}

public class SAXExample3 {

    public static void main(String args[]) throws ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        XMLReader parser = factory.newSAXParser().getXMLReader();

        parser.setContentHandler(new DefaultHandler());
        parser.setErrorHandler(new MyErrorHandler());

        try {
            parser.parse(args[0]);
            System.out.println("Everything is OK.");
        } catch (IOException e) {
            System.err.printf("Handler in main. I/O error: %s\n", e.getMessage());
        } catch (SAXException e) {
            SAXParseException spe = (SAXParseException) e.getException();
            System.err.printf("Handler in main. Parsing error: %s\n", e.getMessage());
            if (spe != null) {
                System.err.println("Additional infos: " + spe.getLineNumber()
                        + "/" + spe.getColumnNumber());
            }
        }
    }
}