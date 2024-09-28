package comparison.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import nbbrd.picocsv.Csv;

public class PicocsvImpl implements CsvImpl {

    @Override
    public String getName() {
        return "picocsv";
    }

    @Override
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows) throws Exception {
        final List<String[]> rows = new ArrayList<>();

        final Csv.Format format = Csv.Format.RFC4180.toBuilder()
            .acceptMissingField(false)
            .build();
        final Csv.ReaderOptions options = Csv.ReaderOptions.builder()
            .lenientSeparator(true)
            .build();
        final Csv.Reader csv = Csv.Reader.of(format, options, new StringReader(data));
        while (csv.readLine()) {
            if (csv.isComment()) {
                continue;
            }

            final List<String> row = new ArrayList<>();
            while (csv.readField()) {
                row.add(csv.toString());
            }

            if (skipEmptyRows && row.isEmpty()) {
                continue;
            }

            rows.add(row.toArray(String[]::new));
        }

        return rows;
    }

}
