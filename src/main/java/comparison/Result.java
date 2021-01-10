package comparison;

public class Result {

    private final String text;
    private final boolean exception;

    public Result(final String text, final boolean exception) {
        this.text = text;
        this.exception = exception;
    }

    public String getText() {
        return text;
    }

    public boolean isException() {
        return exception;
    }

}
