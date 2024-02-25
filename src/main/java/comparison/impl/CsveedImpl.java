package comparison.impl;

import java.io.StringReader;
import java.util.List;
import java.util.stream.StreamSupport;

import org.csveed.api.CsvClient;
import org.csveed.api.CsvClientImpl;

public class CsveedImpl implements CsvImpl {

    @Override
    public String getName() {
        return "CSVeed";
    }

    @Override
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows) throws Exception {
        final CsvClient<?> csvClient = new CsvClientImpl<>(new StringReader(data));
        csvClient.setSeparator(',');
        csvClient.setUseHeader(false);
        csvClient.skipEmptyLines(skipEmptyRows);

        return csvClient.readRows().stream()
                        .map(CsveedImpl::toArray)
                        .toList();
    }

    private static String[] toArray(final Iterable<String> strings) {
        return StreamSupport.stream(strings.spliterator(), false).toArray(String[]::new);
    }

}
