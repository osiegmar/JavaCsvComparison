package comparison.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.List;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

public class OpenCsvImpl implements CsvImpl {

    @Override
    public String getName() {
        return "Opencsv";
    }

    @Override
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows) {
        if (skipEmptyRows) {
            throw new UnsupportedOperationException();
        }

        try {
            return new CSVReaderBuilder(new StringReader(data)).build()
                .readAll();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        } catch (final CsvException e) {
            throw new IllegalStateException(e);
        }
    }

}
