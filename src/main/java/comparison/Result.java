package comparison;

public final class Result {

    private final String text;
    private final boolean exception;
    private final boolean unsupported;

    private Result(final String text, final boolean exception, final boolean unsupported) {
        this.text = text;
        this.exception = exception;
        this.unsupported = unsupported;
    }

    public static Result result(final String text) {
        return new Result(text, false, false);
    }

    public static Result exception(final String text) {
        return new Result(text, true, false);
    }

    public static Result unsupported() {
        return new Result(null, false, true);
    }

    public String getText() {
        return text;
    }

    public boolean isException() {
        return exception;
    }

    public boolean isUnsupported() {
        return unsupported;
    }

}
