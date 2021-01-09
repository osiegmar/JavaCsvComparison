package comparison.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.github.skjolber.stcsv.CsvReader;
import com.github.skjolber.stcsv.builder.StringArrayCsvReaderBuilder;
import com.github.skjolber.stcsv.sa.StringArrayCsvReader;

public class SesseltjonnaCsvImpl implements CsvImpl {

    @Override
    public String getName() {
        return "sesseltjonna-csv";
    }

    @Override
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows) throws Exception {
        if (skipEmptyRows) {
            throw new UnsupportedOperationException();
        }

        final StringArrayCsvReaderBuilder builder = StringArrayCsvReader.builder();

        final CsvReader<String[]> reader = builder.build(new StringReader(data));

        final List<String[]> ret = new ArrayList<>();
        String[] row;
        while ((row = reader.next()) != null) {
            ret.add(row.clone());
        }

        return ret;
    }

}
