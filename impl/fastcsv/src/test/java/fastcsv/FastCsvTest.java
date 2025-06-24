package fastcsv;

import java.util.List;

import comparison.AbstractTest;
import de.siegmar.fastcsv.reader.CommentStrategy;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRecord;
import specreader.spec.TestSpecSettings;

class FastCsvTest extends AbstractTest {

    protected FastCsvTest() {
        super("FastCSV");
    }

    protected List<List<String>> parseCsvRecords(final TestSpecSettings settings, final String input) {
        return configure(settings)
            .ofCsvRecord(input)
            .stream()
            .map(CsvRecord::getFields)
            .toList();
    }

    private static CsvReader.CsvReaderBuilder configure(final TestSpecSettings settings) {
        return CsvReader.builder()
            .allowExtraFields(true)
            .commentStrategy(switch (settings.commentMode()) {
                case NONE -> CommentStrategy.NONE;
                case READ -> CommentStrategy.READ;
                case SKIP -> CommentStrategy.SKIP;
            })
            .skipEmptyLines(settings.skipEmptyLines());
    }

}
