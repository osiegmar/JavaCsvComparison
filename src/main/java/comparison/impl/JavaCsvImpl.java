package comparison.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;

public class JavaCsvImpl implements CsvImpl {

    @Override
    public String getName() {
        return "JavaCSV";
    }

    @Override
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows)
        throws IOException {

        final CsvReader csvReader = CsvReader.parse(data);
        csvReader.setTrimWhitespace(false);
        csvReader.setSkipEmptyRecords(skipEmptyRows);

        final List<String[]> ret = new ArrayList<>();
        while (csvReader.readRecord()) {
            ret.add(csvReader.getValues());
        }

        return ret;
    }

}
