package comparison;

import static comparison.CharacterConv.parse;
import static comparison.CharacterConv.print;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

        for (final CsvImpl csvImplementation : IMPLS) {
            for (final DataProvider.TestData data : testData) {
                dataTest(csvImplementation, data).ifPresent(result ->
                    resultCollector.add(data, csvImplementation.getName(), result));
            }
        }

        for (final CsvImpl csvImplementation : IMPLS) {
            final List<UnexpectedResult> unexpectedResults =
                resultCollector.getUnexpectedResults(csvImplementation.getName());

            if (!unexpectedResults.isEmpty()) {
                TablePrinter.createTable(csvImplementation.getName(), unexpectedResults);
            }
        }
    }

    @SuppressWarnings("checkstyle:IllegalCatch")
    private static Optional<String> dataTest(final CsvImpl impl, final DataProvider.TestData data) {
        final String parsedSource = parse(data.getInput());

        try {
            return Optional.of(print(impl.readCsv(parsedSource, data.isSkipEmptyLines())));
        } catch (final UnsupportedOperationException e) {
            return Optional.empty();
        } catch (final Exception e) {
            return Optional.of(e.getClass().getName());
        }
    }

}
