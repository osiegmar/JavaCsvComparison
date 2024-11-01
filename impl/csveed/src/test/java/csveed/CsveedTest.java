package csveed;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.csveed.api.CsvClient;
import org.csveed.api.CsvClientImpl;
import org.csveed.api.Row;

import comparison.AbstractTest;
import specreader.spec.TestSpecSettings;

public class CsveedTest extends AbstractTest {

    CsveedTest() {
        super("CSVeed");
    }

    @Override
    protected List<List<String>> parseCsvRecords(final TestSpecSettings settings, final String input) {
        final List<List<String>> ret = new ArrayList<>();

        final CsvClient<?> csv = configure(settings, input);

        Row row;
        while ((row = csv.readRow()) != null) {
            ret.add(collect(row));
        }

        return ret;
    }

    private static CsvClient<?> configure(final TestSpecSettings settings, final String input) {
        final CsvClient<?> csvClient = new CsvClientImpl<>(new StringReader(input));
        csvClient.setSeparator(',');
        csvClient.setUseHeader(false);
        csvClient.skipEmptyLines(settings.skipEmptyLines());
        csvClient.skipCommentLines(switch (settings.commentMode()) {
            case NONE -> false;
            case SKIP -> true;
            case READ -> throw new UnsupportedOperationException();
        });
        return csvClient;
    }

    private static List<String> collect(Row row) {
        final List<String> fields = new ArrayList<>();
        row.forEach(fields::add);
        return fields;
    }

    @Override
    protected boolean isSettingsSupported(final TestSpecSettings settings) {
        // CSVeed does not support reading comments
        return settings.commentMode() != specreader.spec.TestSpecCommentMode.READ;
    }

}
