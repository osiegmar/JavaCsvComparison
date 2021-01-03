package comparison;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecordCollector {

    private final List<String> names;
    private final Map<DataProvider.TestData, ResultCollector> records = new LinkedHashMap<>();

    public RecordCollector(final List<String> names) {
        this.names = names;
    }

    public List<String> getNames() {
        return names;
    }

    public Map<DataProvider.TestData, ResultCollector> getRecords() {
        return records;
    }

    public void addRecord(final DataProvider.TestData testData, final ResultCollector result) {
        records.put(testData, result);
    }

}
