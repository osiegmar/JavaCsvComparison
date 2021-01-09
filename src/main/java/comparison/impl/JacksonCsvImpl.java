package comparison.impl;

import java.io.IOException;
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
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows)
        throws IOException {

        final CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
        if (skipEmptyRows) {
            mapper.enable(CsvParser.Feature.SKIP_EMPTY_LINES);
        }

        final MappingIterator<String[]> it = mapper.readerFor(String[].class).readValues(data);

        final List<String[]> ret = new ArrayList<>();
        while (it.hasNext()) {
            ret.add(it.next());
        }

        return ret;
    }

}
