package comparison.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CommonsCsvImpl implements CsvImpl {

    @Override
    public String getName() {
        return "Commons CSV";
    }

    @Override
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows)
        throws IOException {

        final CSVFormat format = CSVFormat.DEFAULT
            .withIgnoreEmptyLines(skipEmptyRows);

        final CSVParser parser = new CSVParser(new StringReader(data), format);

        final List<String[]> ret = new ArrayList<>();
        for (final CSVRecord rec : parser) {
            final List<String> row = new ArrayList<>();
            for (final String s : rec) {
                row.add(s);
            }
            ret.add(row.toArray(new String[0]));
        }

        return ret;
    }

}
