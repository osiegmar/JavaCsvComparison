package comparison;

import java.util.HashMap;
import java.util.Map;

public class ResultCollector {

    private final Map<String, String> records = new HashMap<>();

    public Map<String, String> getRecords() {
        return records;
    }

    public void addRecord(final String implName, final String result) {
        records.put(implName, result);
    }

    public String getValueByImpl(final String name) {
        return records.get(name);
    }

}
