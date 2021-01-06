package comparison.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;

import org.simpleflatmapper.csv.CsvParser;

public class SfmCsvImpl implements CsvImpl {

    @Override
    public String getName() {
        return "Sfm";
    }

    @Override
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows) {
        if (skipEmptyRows) {
            throw new UnsupportedOperationException();
        }

        try {
            return CsvParser.dsl().disableSpecialisedCharConsumer().reader(data).stream()
                .collect(Collectors.toList());
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
