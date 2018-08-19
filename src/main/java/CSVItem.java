import java.util.regex.Pattern;

public class CSVItem {
    private static final Pattern EXTENSION_PATTERN = Pattern.compile("\\..*?$");
    private String path;
    private String name;

    public CSVItem(String path) {
        this.path = path;
        String[] split = path.split("[/\\\\]");
        this.name = EXTENSION_PATTERN.matcher(split[split.length - 1]).replaceAll("");
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

}
