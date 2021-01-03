package comparison.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;

public class JacksonCsvImpl implements CsvImpl {

    @Override
    public String getName() {
        return "JacksonCSV";
    }

    @Override
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows) {
        final CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
        if (skipEmptyRows) {
            mapper.enable(CsvParser.Feature.SKIP_EMPTY_LINES);
        }
        final MappingIterator<String[]> it;
        try {
            it = mapper.readerFor(String[].class).readValues(data);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }

        final List<String[]> ret = new ArrayList<>();
        while (it.hasNext()) {
            ret.add(it.next());
        }

        return ret;
    }

}
