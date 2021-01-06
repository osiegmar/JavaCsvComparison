package comparison;

import static comparison.CharacterConv.parse;
import static comparison.CharacterConv.print;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import comparison.impl.CommonsCsvImpl;
import comparison.impl.CsvImpl;
import comparison.impl.FastCsvImpl;
import comparison.impl.JacksonCsvImpl;
import comparison.impl.JavaCsvImpl;
import comparison.impl.OpenCsvImpl;
import comparison.impl.SfmCsvImpl;
import comparison.impl.SuperCsvImpl;
import comparison.impl.UnivocityImpl;

@SuppressWarnings("checkstyle:ClassDataAbstractionCoupling")
public final class Comparison {

    private Comparison() {
    }

    @SuppressWarnings({"checkstyle:UncommentedMain", "checkstyle:RegexpMultiline"})
    public static void main(final String[] args) throws IOException {
        final List<CsvImpl> csvImplementations = List.of(
            new CommonsCsvImpl(),
            new FastCsvImpl(),
            new JacksonCsvImpl(),
            new JavaCsvImpl(),
            new OpenCsvImpl(),
            new SfmCsvImpl(),
            new SuperCsvImpl(),
            new UnivocityImpl()
        );

        final List<String> implNames = csvImplementations.stream()
            .map(CsvImpl::getName)
            .collect(Collectors.toList());

        final RecordCollector recordCollector = new RecordCollector(implNames);

        for (final DataProvider.TestData data : DataProvider.loadTestData("/test.txt")) {
            final ResultCollector resultCollector = new ResultCollector();

            final boolean allExpected = dataTest(csvImplementations, data, resultCollector);
            if (!allExpected) {
                recordCollector.addRecord(data, resultCollector);
            }
        }

        TablePrinter.createTable(recordCollector);
    }

    @SuppressWarnings("checkstyle:IllegalCatch")
    private static boolean dataTest(final List<CsvImpl> impls, final DataProvider.TestData data,
                                    final ResultCollector resultCollector) {
        final String expected = print(data.getExpected());
        final String parsedSource = parse(data.getInput());

        boolean allExpected = true;
        for (final CsvImpl impl : impls) {
            try {
                final String result = print(impl.readCsv(parsedSource, data.isSkipEmptyLines()));
                if (!expected.equals(result)) {
                    allExpected = false;
                }
                resultCollector.addRecord(impl.getName(), result);
            } catch (final UnsupportedOperationException e) {
                allExpected = false;
                resultCollector.addRecord(impl.getName(), "[unsupported]");
            } catch (final Exception e) {
                allExpected = false;
                resultCollector.addRecord(impl.getName(), "EXCEPTION");
            }
        }

        return allExpected;
    }

}
