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

        final Csv.Reader csv = Csv.Reader.of(Csv.Format.DEFAULT,
                                             Csv.ReaderOptions.builder()
                                                              .lenientSeparator(true)
                                                              .build(),
                                             new StringReader(data));
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

            // if (row.isEmpty()) row.add(""); // uncomment to fully match FastCSV

            rows.add(row.toArray(String[]::new));
        }

        return rows;
    }

}
