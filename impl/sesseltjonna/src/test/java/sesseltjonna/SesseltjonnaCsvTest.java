package sesseltjonna;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.skjolber.stcsv.sa.rfc4180.RFC4180StringArrayCsvReader;

import comparison.AbstractTest;
import specreader.spec.TestSpecCommentMode;
import specreader.spec.TestSpecSettings;

public class SesseltjonnaCsvTest extends AbstractTest {

    protected SesseltjonnaCsvTest() {
        super("sesseltjonna-csv");
    }

    protected List<List<String>> parseCsvRecords(final TestSpecSettings settings, final String input)
        throws Exception {

        final var reader = RFC4180StringArrayCsvReader.builder()
            .build(new StringReader(input));

        final List<List<String>> ret = new ArrayList<>();
        String[] row;
        while ((row = reader.next()) != null) {
            // Convert nulls to empty strings to emulate the behavior of other CSV parsers
            final List<String> fields = Arrays.stream(row)
                .map(s -> s == null ? "" : s)
                .toList();

            ret.add(fields);
        }

        return ret;
    }

    @Override
    protected boolean isSettingsSupported(final TestSpecSettings settings) {
        // sesseltjonna-csv does neither support skipping empty lines nor handling comments
        return !settings.skipEmptyLines() && settings.commentMode() == TestSpecCommentMode.NONE;
    }

}
