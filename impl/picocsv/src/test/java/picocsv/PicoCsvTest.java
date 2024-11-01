package picocsv;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import comparison.AbstractTest;
import nbbrd.picocsv.Csv;
import specreader.spec.TestSpecSettings;

public class PicoCsvTest extends AbstractTest {

    protected PicoCsvTest() {
        super("picocsv");
    }

    protected List<List<String>> parseCsvRecords(final TestSpecSettings settings, final String input)
        throws IOException {
        final List<List<String>> ret = new ArrayList<>();

        final Csv.Reader csv = configure(input);
        while (csv.readLine()) {
            if (csv.isComment() && settings.commentMode() == specreader.spec.TestSpecCommentMode.SKIP) {
                // emulating the behavior of other libraries
                continue;
            }

            final List<String> row = new ArrayList<>();
            while (csv.readField()) {
                row.add(csv.toString());
            }

            ret.add(row);
        }

        return ret;
    }

    private static Csv.Reader configure(final String input) throws IOException {
        final Csv.Format format = Csv.Format.RFC4180.toBuilder()
            .acceptMissingField(false)
            .build();

        // picocsv does not support disabling comments (which are not part of RFC 4180)

        final Csv.ReaderOptions options = Csv.ReaderOptions.builder()
            .lenientSeparator(true)
            .build();

        return Csv.Reader.of(format, options, new StringReader(input));
    }

    @Override
    protected boolean isSettingsSupported(final TestSpecSettings settings) {
        // picocsv does not support skipping empty lines
        return !settings.skipEmptyLines();
    }

}
