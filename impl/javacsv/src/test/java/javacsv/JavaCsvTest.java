package javacsv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;

import comparison.AbstractTest;
import specreader.spec.TestSpecSettings;

public class JavaCsvTest extends AbstractTest {

    protected JavaCsvTest() {
        super("JavaCSV");
    }

    protected List<List<String>> parseCsvRecords(final TestSpecSettings settings, final String input)
        throws IOException {
        final CsvReader csvReader = configure(settings, input);

        final List<List<String>> ret = new ArrayList<>();
        while (csvReader.readRecord()) {
            ret.add(List.of(csvReader.getValues()));
        }

        return ret;
    }

    private static CsvReader configure(final TestSpecSettings settings, final String input) {
        final var csvReader = CsvReader.parse(input);
        csvReader.setTrimWhitespace(false);
        csvReader.setUseComments(switch (settings.commentMode()) {
            case NONE -> false;
            case SKIP -> true;
            case READ -> throw new UnsupportedOperationException();
        });
        csvReader.setSkipEmptyRecords(settings.skipEmptyLines());
        return csvReader;
    }

    @Override
    protected boolean isSettingsSupported(final TestSpecSettings settings) {
        // JavaCSV does not support reading comments
        return settings.commentMode() != specreader.spec.TestSpecCommentMode.READ;
    }

}
