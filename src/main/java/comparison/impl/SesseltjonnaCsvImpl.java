package comparison.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.github.skjolber.stcsv.CsvReader;
import com.github.skjolber.stcsv.builder.StringArrayCsvReaderBuilder;
import com.github.skjolber.stcsv.sa.StringArrayCsvReader;

@SuppressWarnings("checkstyle:IllegalCatch")
public class SesseltjonnaCsvImpl implements CsvImpl {

    @Override
    public String getName() {
        return "sesseltjonna-csv";
    }

    @Override
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows) {
        if (skipEmptyRows) {
            throw new UnsupportedOperationException();
        }

        final List<String[]> list = new ArrayList<>();

        final StringArrayCsvReaderBuilder builder = StringArrayCsvReader.builder();

        try {
            final CsvReader<String[]> reader = builder.build(new StringReader(data));

            String[] row;
            while ((row = reader.next()) != null) {
                list.add(row.clone());
            }
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }

        return list;
    }

}
