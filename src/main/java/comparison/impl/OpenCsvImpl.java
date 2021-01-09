package comparison.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

public class OpenCsvImpl implements CsvImpl {

    @Override
    public String getName() {
        return "Opencsv";
    }

    @Override
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows)
        throws IOException, CsvException {

        if (skipEmptyRows) {
            throw new UnsupportedOperationException();
        }

        return new CSVReaderBuilder(new StringReader(data)).build().readAll();
    }

}
