package commonscsv;

import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import comparison.AbstractTest;
import specreader.spec.TestSpecCommentMode;
import specreader.spec.TestSpecSettings;

class CommonsCsvTest extends AbstractTest {

    CommonsCsvTest() {
        super("Commons CSV");
    }

    @Override
    protected List<List<String>> parseCsvRecords(final TestSpecSettings settings, final String input)
        throws IOException {
        return CSVParser.parse(input, configure(settings)).stream()
            .map(rec -> mapRecord(settings.commentMode(), rec))
            .toList();
    }

    private List<String> mapRecord(final TestSpecCommentMode commentMode, final CSVRecord rec) {
        if (!rec.hasComment()) {
            return rec.toList();
        }

        return switch (commentMode) {
            case READ -> List.of(rec.getComment());
            case SKIP -> List.of();
            case NONE -> throw new IllegalStateException("Comment detected but comment mode is NONE");
        };
    }

    private static CSVFormat configure(final TestSpecSettings settings) {
        return CSVFormat.DEFAULT.builder()
            .setIgnoreEmptyLines(settings.skipEmptyLines())
            .setCommentMarker(switch (settings.commentMode()) {
                case NONE -> null;
                case READ, SKIP -> '#';
            })
            .build();
    }

}
