package comparison.impl;

import java.util.List;

public interface CsvImpl {

    String getName();

    List<String[]> readCsv(String data, boolean skipEmptyRows) throws Exception;

}
