package comparison;

public class ImplementationResult {

    private final String implementationName;
    private final String result;
    private final boolean expected;

    public ImplementationResult(final String implementationName, final String result,
                                final boolean expected) {
        this.implementationName = implementationName;
        this.result = result;
        this.expected = expected;
    }

    public String getImplementationName() {
        return implementationName;
    }

    public String getResult() {
        return result;
    }

    public boolean isExpected() {
        return expected;
    }

}
