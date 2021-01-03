package comparison;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.table.TableRow;

@SuppressWarnings("checkstyle:RegexpMultiline")
public final class TablePrinter {

    private TablePrinter() {
    }

    static void createTable(final RecordCollector recordCollector) {
        final List<String> names = recordCollector.getNames();
        Collections.sort(names);

        // map: oddity group | results
        final Map<String, List<List<String>>> oddities = new HashMap<>();

        for (final Map.Entry<DataProvider.TestData, ResultCollector> entry
            : recordCollector.getRecords().entrySet()) {

            final List<String> vals = new ArrayList<>();

            // Input
            vals.add(String.format("%s%s", entry.getKey().getInput(), formatInput(entry.getKey())));

            // Common
            final String commonResult = findCommonResult(analyzeResults(entry.getValue(), names));

            // Determine oddity group
            final List<String> invalidImpl = names.stream()
                .filter(n -> !entry.getValue().getValueByImpl(n).equals(commonResult))
                .collect(Collectors.toList());

            final String oddityGroup = invalidImpl.size() > 1
                ? "## Mixed behaviour"
                : "## Oddities in " + invalidImpl.get(0);

            // Results
            for (final String name : names) {
                if (invalidImpl.size() > 1) {
                    vals.add(entry.getValue().getValueByImpl(name));
                } else {
                    vals.add(fmt(commonResult, entry.getValue().getValueByImpl(name)));
                }
            }

            oddities.computeIfAbsent(oddityGroup, e -> new ArrayList<>())
                .add(vals);
        }

        print(names, oddities);
    }

    private static void print(final List<String> names,
                              final Map<String, List<List<String>>> tableIn) {

        for (final Map.Entry<String, List<List<String>>> entry : tableIn.entrySet()) {
            System.out.println(entry.getKey());

            final List<String> header = new ArrayList<>();
            header.add("Input");
            header.addAll(names);

            final Table.Builder table = new Table.Builder()
                .addRow(new TableRow<>(header));

            for (final List<String> strings : entry.getValue()) {
                final List<String> collect = strings.stream().map(s -> "`" + s + "`")
                    .collect(Collectors.toList());
                table.addRow(new TableRow<>(collect));
            }

            System.out.println(table.build());
            System.out.println();
        }
    }

    private static Map<String, AtomicInteger> analyzeResults(final ResultCollector value,
                                                             final List<String> names) {

        final Map<String, AtomicInteger> vals = new HashMap<>();
        for (final String name : names) {
            vals.computeIfAbsent(value.getValueByImpl(name), e -> new AtomicInteger(0))
                .incrementAndGet();
        }
        return vals;
    }

    private static String findCommonResult(final Map<String, AtomicInteger> vals) {
        return vals.entrySet().stream()
            .max(Comparator.comparing(e -> e.getValue().get()))
            .orElseThrow(IllegalStateException::new).getKey();
    }

    private static String formatInput(final DataProvider.TestData key) {
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

        return l.isEmpty() ? "" : " [" + String.join(",", l) + "]";
    }

    static String fmt(final String commonResult, final String actual) {
        return actual + (commonResult.equals(actual) ? "" : " ✘");
    }

}
