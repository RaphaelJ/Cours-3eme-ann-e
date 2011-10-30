
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import java.io.PrintStream;

class MyHandler extends DefaultHandler {

    private StringBuilder acc;
    private PrintStream out;

    public MyHandler() throws Exception {
        acc = new StringBuilder();
        out = new PrintStream(System.out, true, "UTF-8");
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (acc.length() > 0) {
            out.println(acc.toString());
            acc.setLength(0);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        acc.append(ch, start, length);
    }
}

public class Dump {

    public static void main(String args[]) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        XMLReader parser = factory.newSAXParser().getXMLReader();
        //parser.setFeature("http://xml.org/sax/features/validation", false);
        parser.setContentHandler(new MyHandler());
        parser.setErrorHandler(new DefaultHandler());
        parser.parse(args[0]);
    }
}