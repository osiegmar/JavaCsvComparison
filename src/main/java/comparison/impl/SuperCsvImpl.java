package comparison.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

public class SuperCsvImpl implements CsvImpl {

    @Override
    public String getName() {
        return "SuperCSV";
    }

    @Override
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows) {
        final CsvPreference preference = new CsvPreference.Builder('"', ',', "\r\n")
            .ignoreEmptyLines(skipEmptyRows)
            .build();

        final CsvListReader csvListReader = new CsvListReader(new StringReader(data),
            preference);

        final List<String[]> list = new ArrayList<>();
        try {
            List<String> read;
            while ((read = csvListReader.read()) != null) {
                list.add(read.toArray(new String[0]));
            }
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }

        return list;
    }

}
