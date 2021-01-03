package comparison.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

public class JavaCsvImpl implements CsvImpl {

    @Override
    public String getName() {
        return "JavaCSV";
    }

    @Override
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows) {
        final List<String[]> ret = new ArrayList<>();

        final com.csvreader.CsvReader csvReader = com.csvreader.CsvReader.parse(data);
        csvReader.setTrimWhitespace(false);
        csvReader.setSkipEmptyRecords(skipEmptyRows);

        try {
            while (csvReader.readRecord()) {
                ret.add(csvReader.getValues());
            }
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }

        return ret;
    }

}
