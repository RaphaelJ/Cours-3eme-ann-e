import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XSLT1 {

    public static void main(String[] args) throws IOException,
            TransformerException {
        File stylesheet = new File("../../exemples/XSLT/ex8.xslt");
        File srcFile = new File("../../exemples/XSLT/library.xml");
        //File destFile = new File("output.txt");
        java.io.Writer destFile = new OutputStreamWriter(System.out, "ISO-8859-1");
        
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(stylesheet);
        Transformer transformer = factory.newTransformer(xslt);

        Source request = new StreamSource(srcFile);
        Result response = new StreamResult(destFile);
        transformer.transform(request, response);
    }
}