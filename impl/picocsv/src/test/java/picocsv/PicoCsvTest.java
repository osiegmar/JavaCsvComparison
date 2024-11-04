package picocsv;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comparison.AbstractTest;
import nbbrd.picocsv.Csv;
import specreader.spec.TestSpecCommentMode;
import specreader.spec.TestSpecSettings;

public class PicoCsvTest extends AbstractTest {

    protected PicoCsvTest() {
        super("picocsv");
    }

    protected List<List<String>> parseCsvRecords(final TestSpecSettings settings, final String input)
        throws IOException {
        final List<List<String>> ret = new ArrayList<>();

        final Csv.Reader csv = configure(settings, input);
        while (csv.readLine()) {
            if (csv.isComment() && settings.commentMode() == specreader.spec.TestSpecCommentMode.SKIP) {
                // emulating the behavior of other libraries
                continue;
            }

            if (!csv.readField()) {
                // emulating the behavior of other libraries
                if (settings.skipEmptyLines()) {
                    continue;
                }
                ret.add(Collections.emptyList());
                continue;
            }

            final List<String> row = new ArrayList<>();
            do {
                row.add(csv.toString());
            } while (csv.readField());

            ret.add(row);
        }

        return ret;
    }

    private static Csv.Reader configure(final TestSpecSettings settings, final String input) throws IOException {
        final Csv.Format format = Csv.Format.RFC4180.toBuilder()
            .acceptMissingField(settings.skipEmptyLines())
            .comment(settings.commentMode() == TestSpecCommentMode.NONE ? '\0' : '#')
            .build();

        final Csv.ReaderOptions options = Csv.ReaderOptions.builder()
            .lenientSeparator(true)
            .build();

        return Csv.Reader.of(format, options, new StringReader(input));
    }

}
