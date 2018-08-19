import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class CSV2XMLConverterHelper {
    public static void convertToFile(String targetFilePath, String mainCSVFilePath, String... subCSVFilePath) throws Exception {
        StreamResult result = new StreamResult(targetFilePath);
        convert(result, mainCSVFilePath, subCSVFilePath);
    }

    public static String convertToString(String mainCSVFilePath, String... subCSVFilePath) throws Exception {
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        convert(result, mainCSVFilePath, subCSVFilePath);
        return writer.toString();
    }

    private static void convert(StreamResult result, String mainCSVFilePath, String... subCSVFilePath) throws Exception {
        CSV2XMLConverter converter = new CSV2XMLConverter(mainCSVFilePath, subCSVFilePath);
        DOMSource domSource = new DOMSource(converter.convert());
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(domSource, result);
    }
}