import java.io.File;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XSLT2 {

    public static void main(String[] args) {
        try {
            File stylesheet = new File("books.xslt");
            File srcFile = new File("books.xml");
            File destFile = new File("output.html");
            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(stylesheet);
            Transformer transformer = factory.newTransformer(xslt);
			String color = "blue";
			if (args.length == 1) color = args[0];
            transformer.setParameter("color", color);

            Source request = new StreamSource(srcFile);
            Result response = new StreamResult(destFile);
            transformer.transform(request, response);

        } catch (TransformerException e) {
            System.err.println(e);
        }
    }
}
