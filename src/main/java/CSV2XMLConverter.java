import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.*;

public class CSV2XMLConverter {
    private CSVItem mainCSVItem;
    private List<CSVItem> subCSVItemList;

    public CSV2XMLConverter(String mainCSVPath, String... subCSVItemPaths) {
        mainCSVItem = new CSVItem(mainCSVPath);
        subCSVItemList = new ArrayList<>();
        for (String subCSVItemPath : subCSVItemPaths) {
            subCSVItemList.add(new CSVItem(subCSVItemPath));
        }
    }

    public Document convert() throws Exception {
        try (LineIterable iterable = LineIterable.fromFile(mainCSVItem.getPath())) {
            String fragmentName = mainCSVItem.getName();
            XMLBuilder xmlBuilder = new XMLBuilder(fragmentName);
            List<String> mainHeaderList = iterable.get();

            Map<String, Map<String, List<Element>>>  subItemListMap = createMultiItemMap(xmlBuilder);

            for (List<String> item : iterable) {
                Element element = xmlBuilder.createMainElement(fragmentName, item, mainHeaderList, subItemListMap);
                xmlBuilder.appendToRootElement(element);
            }

            return xmlBuilder.getDocument();
        }
    }

    private Map<String, Map<String, List<Element>>> createMultiItemMap(XMLBuilder xmlBuilder) throws Exception {
        Map<String, Map<String, List<Element>>> result = new HashMap<>();

        for (CSVItem subItem : subCSVItemList) {
            String key = subItem.getName();
            Map<String, List<Element>> value = createMultiItemMap(xmlBuilder, subItem);
            result.put(key, value);
        }

        return result;
    }

    private Map<String, List<Element>> createMultiItemMap(XMLBuilder xmlBuilder, CSVItem subCSVItem) throws Exception {
        try (LineIterable iterable = LineIterable.fromFile(subCSVItem.getPath())) {
            List<String> headerList = iterable.get();
            Map<String, List<Element>> result = new HashMap<>();
            for (List<String> item : iterable) {
                Element subElement = xmlBuilder.createSubMultiElement(subCSVItem.getName(), item, headerList);
                String id = subElement.getAttribute("ref");
                List<Element> list = result.getOrDefault(subElement.getAttribute("ref"), new ArrayList<>());
                list.add(xmlBuilder.removeAttribute(subElement, "ref"));
                if (!result.containsKey(id)) {
                    result.put(id, list);
                }
            }
            return result;
        } catch (IOException e) {
            return Collections.emptyMap();
        }
    }
}
