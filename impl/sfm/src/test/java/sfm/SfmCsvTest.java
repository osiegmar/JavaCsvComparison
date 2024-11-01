package sfm;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.simpleflatmapper.csv.CsvParser;

import comparison.AbstractTest;
import specreader.spec.TestSpecCommentMode;
import specreader.spec.TestSpecSettings;

public class SfmCsvTest extends AbstractTest {

    protected SfmCsvTest() {
        super("SimpleFlatMapper");
    }

    protected List<List<String>> parseCsvRecords(final TestSpecSettings settings, final String input)
        throws IOException {

        return CsvParser.dsl().disableSpecialisedCharConsumer().reader(input).stream()
            .map(r -> Arrays.stream(r).toList())
            .toList();
    }

    @Override
    protected boolean isSettingsSupported(final TestSpecSettings settings) {
        // SimpleFlatMapper does neither support skipping empty lines nor handling comments
        return !settings.skipEmptyLines() && settings.commentMode() == TestSpecCommentMode.NONE;
    }

}
