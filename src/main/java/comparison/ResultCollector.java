package comparison;

import static comparison.CharacterConv.print;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ResultCollector {

    private final Map<ImplementationResult, DataProvider.TestData> data = new HashMap<>();

    public Map<ImplementationResult, DataProvider.TestData> getData() {
        return data;
    }

    public void add(final DataProvider.TestData testData, final String implementationName,
                    final Result result) {
        final String expected = print(testData.getExpected());
        final ImplementationResult implementationResult =
            new ImplementationResult(implementationName, result, expected.equals(result.getText()));
        data.put(implementationResult, testData);
    }

    public List<UnexpectedResult> getUnexpectedResults(final String name) {
        final List<UnexpectedResult> ret = new ArrayList<>();
        data.entrySet().stream()
            .filter(e -> e.getKey().getImplementationName().equals(name))
            .filter(e -> !e.getKey().isExpected())
            .forEach(e -> ret.add(new UnexpectedResult(e.getValue(), e.getKey().getResult(),
                findCorrectImplementations(e.getValue()))));

        ret.sort(Comparator.comparing(e -> e.getTestData().getLineNo()));

        return ret;
    }

    private Set<String> findCorrectImplementations(final DataProvider.TestData testData) {
        return data.entrySet().stream()
            .filter(e -> e.getValue() == testData)
            .filter(e -> e.getKey().isExpected())
            .map(e -> e.getKey().getImplementationName())
            .collect(Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER)));
    }

}
