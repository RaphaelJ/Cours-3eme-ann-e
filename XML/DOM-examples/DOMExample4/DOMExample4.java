import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMExample4 {
    public static Element findElement(Element root) {
        // Locate the good title element
            NodeList titles = root.getElementsByTagName("title");
            int i = 0;
            boolean not_found = true;
            while (i < titles.getLength() && not_found) {
                String titleText = titles.item(i).getFirstChild().getNodeValue();
                if (titleText.equals("Java & XML")) not_found = false;
                else i++;
            }
            if (not_found) return null;
            else return (Element)titles.item(i).getParentNode();
    }
    
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder parser = factory.newDocumentBuilder();

        // Load an existing document
        Document doc = parser.parse(args[0]);
        //doc.normalize();
        
        
        Element book = findElement(doc.getDocumentElement());
        if (book == null) {
            System.out.println("Book with the right title not found !");
            System.exit(1);
        }

        Node title = book.getFirstChild().getFirstChild();
        title.setNodeValue("Java and XML");        

        // Serialize document tree

        TransformerFactory transFactory = TransformerFactory.newInstance();
        //transFactory.setAttribute("indent-number", 2);
        Transformer idTransform = transFactory.newTransformer();
        idTransform.setOutputProperty(OutputKeys.METHOD, "xml");
        idTransform.setOutputProperty(OutputKeys.INDENT,"yes");                
        Source input = new DOMSource(doc);
        Result output = new StreamResult(System.out);
        idTransform.transform(input, output);
    }
}
