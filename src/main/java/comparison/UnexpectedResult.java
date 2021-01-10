package comparison;

import java.util.Set;

public class UnexpectedResult {

    private final DataProvider.TestData testData;
    private final Result result;
    private final Set<String> correctImplementations;

    public UnexpectedResult(final DataProvider.TestData testData, final Result result,
                            final Set<String> correctImplementations) {
        this.testData = testData;
        this.result = result;
        this.correctImplementations = correctImplementations;
    }

    public DataProvider.TestData getTestData() {
        return testData;
    }

    public Result getResult() {
        return result;
    }

    public Set<String> getCorrectImplementations() {
        return correctImplementations;
    }

}
