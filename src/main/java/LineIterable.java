import java.io.*;
import java.util.*;

public class LineIterable implements Iterable<List<String>>, AutoCloseable {
    private LineIterator iterator;

    private LineIterable(Reader reader) {
        iterator = new LineIterator(reader);
    }

    public static LineIterable fromFile(String filePath) throws IOException {
        return new LineIterable(new BufferedReader(new InputStreamReader(new FileInputStream(filePath))));
    }

    public static LineIterable fromString(String csv) {
        return new LineIterable(new BufferedReader(new StringReader(csv)));
    }

    public List<String> get() {
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return Collections.emptyList();
    }

    @Override
    public void close() throws Exception {
        if (iterator != null) {
            iterator.close();
        }
    }

    @Override
    public Iterator<List<String>> iterator() {
        return iterator;
    }

    private class LineIterator implements Iterator<List<String>>, AutoCloseable{
        private Scanner scanner;

        LineIterator(Reader reader) {
            scanner = new Scanner(reader);
        }

        @Override
        public boolean hasNext() {
            return scanner.hasNext();
        }

        @Override
        public List<String> next() {
            return Arrays.asList(scanner.next().split("[,\t]"));
        }

        @Override
        public void close() {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
}
