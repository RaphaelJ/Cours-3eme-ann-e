import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class DOMExample3 {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();

        // Load an existing document
        //Document doc = parser.parse(args[0]);
        
        // Create doc from scratch
        
        DOMImplementation domImpl = parser.getDOMImplementation();
        DocumentType type = domImpl.createDocumentType("library", null, "library.dtd");
        Document doc = domImpl.createDocument(null, "library", type);

        // Create new book element

        Element newBook = doc.createElement("book");
        newBook.setAttribute("isbn", "0-321-15040-6");

        // Insert it in the tree

        Element library = doc.getDocumentElement();
        library.appendChild(newBook);

        Element newTitle = doc.createElement("title");
        newBook.appendChild(newTitle);
        newTitle.appendChild(doc.createTextNode("Effective XML"));
        
        Element newAuthor = doc.createElement("author");
        newBook.appendChild(newAuthor);

        Element newFirstname = doc.createElement("firstname");
        newAuthor.appendChild(newFirstname);
        newFirstname.appendChild(doc.createTextNode("Elliotte"));
        
        Element newLastname = doc.createElement("lastname");
        newAuthor.appendChild(newLastname);
        newLastname.appendChild(doc.createTextNode("Rusty Harold"));
        


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