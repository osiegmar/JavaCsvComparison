package univocity;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import comparison.AbstractTest;
import specreader.spec.TestSpecCommentMode;
import specreader.spec.TestSpecSettings;

public class UnivocityTest extends AbstractTest {

    protected UnivocityTest() {
        super("uniVocity");
    }

    protected List<List<String>> parseCsvRecords(final TestSpecSettings settings, final String input) {
        final CsvParser parser = new CsvParser(configure(settings));
        parser.beginParsing(new StringReader(input));

        return parser.parseAll().stream()
            .map(Arrays::asList)
            .toList();
    }

    private static CsvParserSettings configure(final TestSpecSettings settings) {
        final CsvParserSettings csvSettings = new CsvParserSettings();
        csvSettings.trimValues(false);
        csvSettings.setNormalizeLineEndingsWithinQuotes(false);
        csvSettings.setLineSeparatorDetectionEnabled(true);
        csvSettings.setNullValue("");
        csvSettings.setEmptyValue("");
        csvSettings.setSkipEmptyLines(settings.skipEmptyLines());
        csvSettings.setCommentProcessingEnabled(settings.commentMode() == TestSpecCommentMode.SKIP);
        return csvSettings;
    }

    @Override
    protected boolean isSettingsSupported(final TestSpecSettings settings) {
        // The way uniVocity reads/collects comments can't be controlled enough to handle the test cases
        return settings.commentMode() != specreader.spec.TestSpecCommentMode.READ;
    }

}
