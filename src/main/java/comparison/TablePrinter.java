package comparison;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            final TableRow<String> row = new TableRow<>(List.of(
                fmt(result.getTestData().getInput()),
                formatInput(result.getTestData()).map(TablePrinter::fmt).orElse("—"),
                fmt(result.getResult()),
                fmt(result.getTestData().getExpected()),
                String.join(", ", result.getCorrectImplementations())
            ));

            table.addRow(row);
        }

        System.out.println(table.build());
        System.out.println();
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

}
