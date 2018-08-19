import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XMLBuilder {
    private final DocumentBuilderFactory documentBuilderFactory;
    private final DocumentBuilder documentBuilder;
    private Document document;
    private Element rootElement;

    public XMLBuilder(String fragmentName) throws ParserConfigurationException {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        document = documentBuilder.newDocument();
        rootElement = document.createElement(fragmentName + "List");
        document.appendChild(rootElement);
    }

    public Element createTextElement(String name, String data) {
        Element result = document.createElement(name);
        result.setTextContent(data);
        return result;
    }

    public Element createMultiElement(String name, Iterable<? extends Node> nodes) {
        Element result = document.createElement(name);
        for (Node node : nodes) {
            result.appendChild(node);
        }
        return result;
    }

    public Element createMainElement(String fragmentName, List<String> dataList, List<String> headerList, Map<String, Map<String, List<Element>>> subItemListMap) {
        String id = dataList.get(0);
        int columnSize = headerList.size();
        Element mainElement = document.createElement(fragmentName);

        for (int i = 0; i < columnSize; i++) {
            String header = headerList.get(i);
            Element subElement = null;
            if (subItemListMap.containsKey(header)) {
                String listName = header + "List";
                List<Element> subItemList = new ArrayList<>();
                Map<String, List<Element>> subItemMap = subItemListMap.get(header);
                if (subItemMap != null) {
                    List<Element> elementList = subItemMap.get(id);
                    if (elementList != null) {
                        for (Element e : elementList) {
                            e = removeAttribute(e, "ref");
                            subItemList.add(e);
                        }
                    }
                    subElement = createMultiElement(listName, subItemList);
                }
            } else {
                String item = "";
                if (dataList.size() > i) {
                    item = dataList.get(i);
                }
                subElement = createTextElement(header, item);
            }
            mainElement.appendChild(subElement);
        }

        return mainElement;
    }

    public Element createSubMultiElement(String fragmentName, List<String> dataList, List<String> headerList) {
        String id = dataList.get(0);
        Element element = document.createElement(fragmentName);
        element.setAttribute("ref", id);
        int columnSize = headerList.size();
        for (int i = 1; i < columnSize; i++) {
            String header = headerList.get(i);
            String data = dataList.get(i);
            Element sub = document.createElement(header);
            sub.setTextContent(data);
            element.appendChild(sub);
        }
        return element;
    }

    public void appendToRootElement(Element element) {
        rootElement.appendChild(element);
    }

    public Document getDocument() {
        return document;
    }

    public Element removeAttribute(Element e, String attributeName) {
        e.removeAttribute(attributeName);
        return e;
    }
}
