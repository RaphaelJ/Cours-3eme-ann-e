//import java.io.StringReader;
import java.io.PrintStream;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

class MyHandler extends DefaultHandler {
    private PrintStream out;
    
    public MyHandler(PrintStream out) {
        this.out = out;
    }
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {  
        for (int i = 0; i < atts.getLength(); i++) {
            out.printf("URI:%s, LOCAL NAME:%s, QNAME:%s, VALUE:\"%s\"\n",
                    atts.getURI(i), atts.getLocalName(i),
                    atts.getQName(i), atts.getValue(i));
        }
    }
}

public class AttrValueNorm {
    public static void main(String[] args) throws Exception {
        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        XMLReader parser = factory.newSAXParser().getXMLReader();
        parser.setContentHandler(new MyHandler(out));
        parser.setErrorHandler(new DefaultHandler());
        parser.setFeature("http://xml.org/sax/features/namespaces", true);
        parser.setFeature("http://xml.org/sax/features/namespace-prefixes", true);

        out.printf("http://xml.org/sax/features/namespaces : %s\n", parser.getFeature("http://xml.org/sax/features/namespaces"));
        out.printf("http://xml.org/sax/features/namespace-prefixes : %s\n", parser.getFeature("http://xml.org/sax/features/namespace-prefixes"));

        
        //String document = "<root att1=\"\tthis\nis   a\n\ntest   \" att2=\"&#x9;this&#xA;is   a\n\ntest   \"/>";
        //parser.parse(new InputSource(new StringReader(document)));
        parser.parse(args[0]);
    }
}
