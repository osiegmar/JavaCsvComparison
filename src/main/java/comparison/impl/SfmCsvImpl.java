package comparison.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.simpleflatmapper.csv.CsvParser;

public class SfmCsvImpl implements CsvImpl {

    @Override
    public String getName() {
        return "Simpleflatmapper";
    }

    @Override
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows)
        throws IOException {

        if (skipEmptyRows) {
            throw new UnsupportedOperationException();
        }

        return CsvParser.dsl().disableSpecialisedCharConsumer().reader(data).stream()
            .collect(Collectors.toList());
    }

}
