import java.io.IOException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.DocumentType;
import org.xml.sax.SAXException;

// JAXP
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

// Xerces Java 2 implementation
// import org.apache.xerces.parsers.DOMParser;

public class DOMExample1 {
    public static void main(String[] args) throws ParserConfigurationException {

        // Xerces Java 2 implementation specific instantiation
        // DOMParser parser = new DOMParser();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(false);
        factory.setValidating(false);

        DocumentBuilder parser = factory.newDocumentBuilder();

        // Check which DOM level 2 modules are implemented

        DOMImplementation domImpl = parser.getDOMImplementation();

        String[] features = { "Core", "XML", "HTML", "Views", "StyleSheets",
                "CSS", "CSS2", "Events", "UIEvents", "MouseEvents",
                "MutationEvents", "HTMLEvents", "Traversal", "Range" };

        System.out.println("Implementation: " + domImpl.getClass().getName());
        for (int i = 0; i < features.length; i++)         
            if (domImpl.hasFeature(features[i], "2.0"))
                System.out.println("Supports " + features[i]);
            else
                System.out.println("Does not support " + features[i]);

        try {
            System.out.println("Parsing XML document...");
            Document doc = parser.parse(args[0]);
            System.out.println("Displaying XML document...");
            System.out.println("<?xml version=\"" + doc.getXmlVersion()
                    + "\" encoding=\"" + doc.getXmlEncoding() + "\"?>");
            displayNode(doc);
        } catch (SAXException e) {
			System.err.println("SAXException");
            System.err.println(e.getMessage());
        } catch (IOException e) {
			System.err.println("IOException");
            System.err.println(e.getMessage());
        }
    }

    static void displayNode(Node node) {
        switch (node.getNodeType()) {
        case Node.DOCUMENT_NODE:
            NodeList nodes = node.getChildNodes();
                for (int i = 0; i < nodes.getLength(); i++)
                    displayNode(nodes.item(i));
            break;
        case Node.ELEMENT_NODE:
            String name = node.getNodeName();
            System.out.print("<" + name);

            // Print attributes
            NamedNodeMap attributes = node.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node current = attributes.item(i);
                System.out.print(" " + current.getNodeName() + "=\""
                        + current.getNodeValue() + "\"");
            }
            System.out.print(">");

            // Recurse on each child node
            NodeList children = node.getChildNodes();
            if (children != null) {
                if ((children.item(0) != null)
                        && (children.item(0).getNodeType() == Node.ELEMENT_NODE))
                    System.out.println();
                for (int i = 0; i < children.getLength(); i++)
                    displayNode(children.item(i));
            }
            System.out.println("</" + name + ">");
            break;
        case Node.TEXT_NODE:
            System.out.print(":" + node.getNodeValue() + ":");
            break;
        case Node.CDATA_SECTION_NODE:
            System.out.println("<![CDATA[" + node.getNodeValue() + "]]>");
            break;
        case Node.ENTITY_REFERENCE_NODE:
            System.out.print("&" + node.getNodeName() + ";");
            break;
        case Node.DOCUMENT_TYPE_NODE:
            DocumentType docType = (DocumentType) node;
            
            System.out.print("<!DOCTYPE " + docType.getName());
            if (docType.getPublicId() != null)
                System.out.print(" PUBLIC \"" + docType.getPublicId() + "\" ");
            else
                System.out.print(" SYSTEM ");
            System.out.println("\"" + docType.getSystemId() + "\">");
            break;
        case Node.COMMENT_NODE:
            System.out.println("<!--" + node.getNodeValue() + "-->");
            break;
        }
    }
}