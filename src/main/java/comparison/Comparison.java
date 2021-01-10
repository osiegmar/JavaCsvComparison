package comparison;

import static comparison.CharacterConv.parse;
import static comparison.CharacterConv.print;

import java.io.IOException;
import java.util.List;

import comparison.impl.CommonsCsvImpl;
import comparison.impl.CsvImpl;
import comparison.impl.FastCsvImpl;
import comparison.impl.JacksonCsvImpl;
import comparison.impl.JavaCsvImpl;
import comparison.impl.OpenCsvImpl;
import comparison.impl.SesseltjonnaCsvImpl;
import comparison.impl.SfmCsvImpl;
import comparison.impl.SuperCsvImpl;
import comparison.impl.UnivocityImpl;

@SuppressWarnings("checkstyle:ClassDataAbstractionCoupling")
public final class Comparison {

    private static final List<CsvImpl> IMPLS = List.of(
        new CommonsCsvImpl(),
        new FastCsvImpl(),
        new JacksonCsvImpl(),
        new JavaCsvImpl(),
        new OpenCsvImpl(),
        new SesseltjonnaCsvImpl(),
        new SfmCsvImpl(),
        new SuperCsvImpl(),
        new UnivocityImpl()
    );

    private Comparison() {
    }

    @SuppressWarnings("checkstyle:UncommentedMain")
    public static void main(final String[] args) throws IOException {
        final List<DataProvider.TestData> testData = DataProvider.loadTestData("/test.txt");

        final ResultCollector resultCollector = new ResultCollector();

        // Execute tests and collect results
        for (final CsvImpl csvImplementation : IMPLS) {
            for (final DataProvider.TestData data : testData) {
                resultCollector.add(data, csvImplementation.getName(),
                    dataTest(csvImplementation, data));
            }
        }

        // Print results
        for (final CsvImpl csvImplementation : IMPLS) {
            final List<UnexpectedResult> unexpectedResults =
                resultCollector.getUnexpectedResults(csvImplementation.getName());

            if (!unexpectedResults.isEmpty()) {
                TablePrinter.createTable(csvImplementation.getName(), unexpectedResults);
            }
        }

        // Print big picture
        TablePrinter.createTable(testData, resultCollector);
    }

    @SuppressWarnings("checkstyle:IllegalCatch")
    private static Result dataTest(final CsvImpl impl, final DataProvider.TestData data) {
        final String parsedSource = parse(data.getInput());

        Result value;
        try {
            value = Result.result(print(impl.readCsv(parsedSource, data.isSkipEmptyLines())));
        } catch (final UnsupportedOperationException e) {
            value = Result.unsupported();
        } catch (final Exception e) {
            value = Result.exception(e.getClass().getSimpleName());
        }

        return value;
    }

}
