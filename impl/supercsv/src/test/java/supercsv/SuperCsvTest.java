package supercsv;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.comment.CommentStartsWith;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import comparison.AbstractTest;
import specreader.spec.TestSpecSettings;

public class SuperCsvTest extends AbstractTest {

    protected SuperCsvTest() {
        super("Super CSV");
    }

    protected List<List<String>> parseCsvRecords(final TestSpecSettings settings, final String input)
        throws IOException {

        final CsvListReader csvListReader = new CsvListReader(new StringReader(input),
            configure(settings));

        final List<List<String>> ret = new ArrayList<>();

        List<String> row;
        while ((row = csvListReader.read()) != null) {
            // convert nulls to empty strings for consistency
            ret.add(row.stream().map(s -> s == null ? "" : s).toList());
        }

        return ret;
    }

    private static CsvPreference configure(final TestSpecSettings settings) {
        final CsvPreference.Builder builder = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
            .ignoreEmptyLines(settings.skipEmptyLines());

        if (settings.commentMode() == specreader.spec.TestSpecCommentMode.SKIP) {
            builder.skipComments(new CommentStartsWith("#"));
        }

        return builder.build();
    }

    @Override
    protected boolean isSettingsSupported(final TestSpecSettings settings) {
        // Super CSV does not support reading comments
        return settings.commentMode() != specreader.spec.TestSpecCommentMode.READ;
    }

}
