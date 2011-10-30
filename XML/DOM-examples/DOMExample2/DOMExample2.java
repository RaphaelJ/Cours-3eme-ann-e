import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class DOMExample2 {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();

        System.out.println("Parsing XML document...");
        Document doc;
        doc = parser.parse(args[0]);

        // Xerces Java 2

        /* Deprecated. This class was deprecated in Xerces 2.9.0.
         * It is recommended that new applications use the DOM Level 3
         * LSSerializer or JAXP's Transformation API for XML (TrAX)
         * for serializing XML and HTML.
         * See the Xerces documentation for more information.
         */  
        /*
        System.out.println("XERCES: Displaying XML document...");
        OutputFormat of = new OutputFormat(doc, "ISO-8859-1", true);
        PrintWriter pw = new PrintWriter(System.out);
        BaseMarkupSerializer bms = new XMLSerializer(pw, of);
        bms.serialize(doc);
*/
        // JAXP

        System.out.println("JAXP: Displaying XML document...");
        TransformerFactory transFactory = TransformerFactory.newInstance();
        System.out.println(transFactory.getClass().getName());
        //transFactory.setAttribute("indent-number", 2);
        Transformer idTransform = transFactory.newTransformer();
        idTransform.setOutputProperty(OutputKeys.METHOD, "xml");
        idTransform.setOutputProperty(OutputKeys.INDENT,"yes");                
        Source input = new DOMSource(doc);
        Result output = new StreamResult(System.out);
        idTransform.transform(input, output);
    }
}
