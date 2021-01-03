package comparison.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

public class OpenCsvImpl implements CsvImpl {

    @Override
    public String getName() {
        return "Opencsv";
    }

    @Override
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows) {
        try {
            final List<String[]> list = new CSVReaderBuilder(new StringReader(data)).build()
                .readAll();

            if (skipEmptyRows) {
                return list.stream()
                    .filter(l -> l.length > 0 && !l[0].isEmpty())
                    .collect(Collectors.toList());
            }

            return list;
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        } catch (final CsvException e) {
            throw new IllegalStateException(e);
        }
    }

}
