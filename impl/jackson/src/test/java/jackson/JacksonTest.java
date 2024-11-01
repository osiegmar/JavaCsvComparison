package jackson;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;

import comparison.AbstractTest;
import specreader.spec.TestSpecSettings;

public class JacksonTest extends AbstractTest {

    protected JacksonTest() {
        super("Jackson");
    }

    protected List<List<String>> parseCsvRecords(final TestSpecSettings settings, final String input)
        throws IOException {
        return configure(settings)
            .readerFor(TypeFactory.defaultInstance().constructCollectionType(List.class, String.class))
            .<List<String>>readValues(input)
            .readAll();
    }

    private static CsvMapper configure(final TestSpecSettings settings) {
        return new CsvMapper()
            .enable(CsvParser.Feature.WRAP_AS_ARRAY)
            .configure(CsvParser.Feature.SKIP_EMPTY_LINES, settings.skipEmptyLines())
            .configure(CsvParser.Feature.ALLOW_COMMENTS, switch (settings.commentMode()) {
                case NONE -> false;
                case SKIP -> true;
                case READ -> throw new UnsupportedOperationException();
            });
    }

    @Override
    protected boolean isSettingsSupported(final TestSpecSettings settings) {
        // Jackson does not support reading comments
        return settings.commentMode() != specreader.spec.TestSpecCommentMode.READ;
    }

}
