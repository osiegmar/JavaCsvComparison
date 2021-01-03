package comparison.impl;

import java.io.StringReader;
import java.util.List;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

public class UnivocityImpl implements CsvImpl {

    @Override
    public String getName() {
        return "Univocity";
    }

    @Override
    public List<String[]> readCsv(final String data, final boolean skipEmptyRows) {
        final CsvParserSettings settings = new CsvParserSettings();
        settings.trimValues(false);
        settings.setNullValue("");
        settings.setSkipEmptyLines(skipEmptyRows);

        final CsvParser parser = new CsvParser(settings);
        parser.beginParsing(new StringReader(data));

        return parser.parseAll();
    }

}
