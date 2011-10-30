
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

class MyHandler extends DefaultHandler {

    private StringBuilder acc;
    private PrintStream out;

    public MyHandler(PrintStream out) throws UnsupportedEncodingException {
        acc = new StringBuilder();
        this.out = out;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
        out.printf("startElement. uri:%s, localName:%s, qName:%s\n", uri, localName, qName);
        if (atts.getLength() > 0) {
            for (int i = 0; i < atts.getLength(); i++) {
                out.printf("    uri:%s, localName:%s, qName:%s = '%s'\n",
                        atts.getURI(i), atts.getLocalName(i),
                        atts.getQName(i), atts.getValue(i));
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        out.printf("endElement. uri:%s, localName:%s, qName:%s\n", uri, localName, qName);
        out.printf("Characters accumulated: %s\n", acc.toString());
        acc.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        acc.append(ch, start, length);
        out.printf("characters. [%s]\n", new String(ch, start, length));
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
        String s = new String(ch, start, length);
        ArrayList<String> codePoints = new ArrayList<String>();
        for (int i = 0; i < s.length(); i++) {
            int codePoint = s.codePointAt(i);
            codePoints.add(String.format("0x%X", codePoint));
            if (i >= 0xD800 && i <= 0xDBFF) {
                // useless in this specific case
                i++;
            }
        }
        out.printf("ignorableWhitespace. [%s]\n", join(codePoints, ", "));
    }
    
    public static String join(ArrayList a) {
        return join(a, null);
    }
    
    public static String join(ArrayList a, String separator) {
        StringBuilder buffer = new StringBuilder();
        int i = 0;
        for (; i < a.size() - 1; i++) {
            buffer.append(a.get(i));
            if (separator != null) buffer.append(separator);
        }
        if (i < a.size()) {
            buffer.append(a.get(i));
        }
        return buffer.toString();
    }
}

public class SAXExample2 {

    public static void main(String args[]) throws SAXException, ParserConfigurationException, UnsupportedEncodingException {
        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        //factory.setNamespaceAware(true);
        XMLReader parser = factory.newSAXParser().getXMLReader();
        //parser.setFeature("http://xml.org/sax/features/namespaces", true);
        //parser.setFeature("http://xml.org/sax/features/namespace-prefixes", true);

        out.printf("http://xml.org/sax/features/namespaces : %s\n", parser.getFeature("http://xml.org/sax/features/namespaces"));
        out.printf("http://xml.org/sax/features/namespace-prefixes : %s\n", parser.getFeature("http://xml.org/sax/features/namespace-prefixes"));
        DefaultHandler handler = new MyHandler(out);
        parser.setContentHandler(handler);
        parser.setErrorHandler(handler);

        try {
            parser.parse(args[0]);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } catch (SAXException e) {
            System.err.println("Parsing error: " + e.getMessage());
        }
    }
}