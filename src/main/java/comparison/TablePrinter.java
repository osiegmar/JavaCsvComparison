package comparison;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.table.TableRow;

@SuppressWarnings("checkstyle:RegexpMultiline")
public final class TablePrinter {

    private TablePrinter() {
    }

    static void createTable(final String name, final List<UnexpectedResult> results) {
        System.out.println("## Unexpected results in " + name);

        final Table.Builder table = new Table.Builder()
            .addRow("Input", "Flags", name, "Expected", "Implemented as expected by");

        for (final UnexpectedResult result : results) {
            if (result.getResult().isUnsupported()) {
                continue;
            }

            final TableRow<String> row = new TableRow<>(List.of(
                fmt(result.getTestData().getInput()),
                formatInput(result.getTestData()).map(TablePrinter::fmt).orElse("—"),
                fmtResult(result.getResult()),
                fmt(result.getTestData().getExpected()),
                String.join(", ", result.getCorrectImplementations())
            ));

            table.addRow(row);
        }

        System.out.println(table.build());
        System.out.println();
    }

    public static void createTable(final List<DataProvider.TestData> testData,
                                   final ResultCollector resultCollector) {

        final Set<String> implementations = resultCollector.getData().keySet().stream()
            .map(ImplementationResult::getImplementationName)
            .collect(Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER)));

        System.out.println("## Big picture");

        final List<String> header = new ArrayList<>();
        header.addAll(List.of("Input", "Flags", "Expected"));
        header.addAll(implementations);
        final Table.Builder table = new Table.Builder().addRow(new TableRow<>(header));

        for (final DataProvider.TestData testDatum : testData) {
            final List<String> row = new ArrayList<>(List.of(
                fmt(testDatum.getInput()),
                formatInput(testDatum).map(TablePrinter::fmt).orElse("—"),
                fmt(testDatum.getExpected())
            ));

            final List<Map.Entry<ImplementationResult, DataProvider.TestData>> collect =
                resultCollector.getData().entrySet().stream()
                    .filter(e -> e.getValue() == testDatum)
                    .collect(Collectors.toList());

            for (final String implementation : implementations) {
                final Map.Entry<ImplementationResult, DataProvider.TestData> entry = collect
                    .stream()
                    .filter(e -> e.getKey().getImplementationName().equals(implementation))
                    .findFirst()
                    .orElseThrow();

                row.add(fmtResultBigPicture(entry.getValue(), entry.getKey().getResult()));
            }

            table.addRow(new TableRow<>(row));
        }

        System.out.println(table.build());
        System.out.println();

    }

    private static String fmtResult(final Result result) {
        if (result.isException()) {
            return ":boom: " + result.getText();
        }

        return "`" + result.getText() + "`";
    }

    private static String fmt(final String s) {
        return '`' + s + '`';
    }

    private static Optional<String> formatInput(final DataProvider.TestData key) {
        final List<String> l = new ArrayList<>();
        if (key.isSkipEmptyLines()) {
            l.add("SE");
        }
        if (key.isSkipComments()) {
            l.add("SC");
        }
        if (key.isReadComments()) {
            l.add("RC");
        }

        return l.isEmpty() ? Optional.empty() : Optional.of("[" + String.join(",", l) + "]");
    }

    @SuppressWarnings("checkstyle:ReturnCount")
    private static String fmtResultBigPicture(final DataProvider.TestData testData,
                                              final Result result) {
        if (result.isException()) {
            return ":boom:";
        }

        if (result.isUnsupported()) {
            return ":heavy_minus_sign:";
        }

        if (result.getText().equals(testData.getExpected())) {
            return ":white_check_mark:";
        }

        return ":x:";
    }

}
