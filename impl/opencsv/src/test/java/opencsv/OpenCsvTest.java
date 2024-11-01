package opencsv;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import comparison.AbstractTest;
import specreader.spec.TestSpecSettings;

public class OpenCsvTest extends AbstractTest {

    protected OpenCsvTest() {
        super("Opencsv");
    }

    protected List<List<String>> parseCsvRecords(final TestSpecSettings settings, final String input)
        throws IOException, CsvException {
        return new CSVReaderBuilder(new StringReader(input)).build().readAll()
            .stream()
            .map(List::of)
            .toList();
    }

    @Override
    protected boolean isSettingsSupported(final TestSpecSettings settings) {
        // Opencsv does neither support skipping empty lines nor handling comments
        return !settings.skipEmptyLines()
               && settings.commentMode() == specreader.spec.TestSpecCommentMode.NONE;
    }

}
