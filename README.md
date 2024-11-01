# Interpretation of the CSV Standard by Java Libraries

Unfortunately, there is no real industry standard for CSV files.
The closest thing we do currently have (since 2005) is [RFC 4180](https://tools.ietf.org/html/rfc4180).
Working with non-standardized data often comes with surprises, but what exactly happens when parsing
CSV data that doesn't match this RFC?

This project is about to find that out...

> [!NOTE]
> This project was created during the development of [FastCSV].
> Since this comparison uses the result of FastCSV as a reference value (expected result),
> the comparison is highly biased.

## Test results

The [test suite](tests) consists of currently 60 tests in total.
To pass a test, the library must return the expected result or throw an exception if the input is invalid.
If the library lacks a feature (like skipping empty lines or comments), the test is marked as skipped.
If a test is neither passed nor skipped, it is marked as failed.

| Library            | Checks passed |     Checks skipped |      Checks failed |
|:-------------------|--------------:|-------------------:|-------------------:|
| [Commons CSV]      |            48 | :heavy_minus_sign: |                 12 |
| [CSVeed]           |            28 |                  7 |                 25 |
| [FastCSV]          |            60 | :heavy_minus_sign: | :heavy_minus_sign: |
| [Jackson CSV]      |            49 |                  7 |                  4 |
| [Java CSV]         |            51 |                  7 |                  2 |
| [Opencsv]          |            38 |                 17 |                  5 |
| [picocsv]          |            50 |                  3 |                  7 |
| [sesseltjonna-csv] |            30 |                 17 |                 13 |
| [SimpleFlatMapper] |            41 |                 17 |                  2 |
| [Super CSV]        |            49 |                  7 |                  4 |
| [Univocity]        |            51 |                  7 |                  2 |

A detailed report can be found at: https://osiegmar.github.io/JavaCsvComparison

The tests clearly show that there are significant differences between the libraries –
especially when it comes to non-standardized data.

## Execute

To run the tests and generate the report locally, execute the following command:

```shell
./gradlew test allureAggregateServe
```

## Library Implementor?

The tests are written in a way that they can be easily adapted to other libraries and languages.
They can also be used as unit tests for your library, like done in [FastCSV].

Feel free to use the tests for your own project.


[Commons CSV]: https://commons.apache.org/proper/commons-csv/
[CSVeed]: https://42bv.github.io/CSVeed/
[FastCSV]: https://fastcsv.org
[Jackson CSV]: https://github.com/FasterXML/jackson-dataformats-text
[Java CSV]: https://sourceforge.net/projects/javacsv/
[Opencsv]: https://opencsv.sourceforge.net
[picocsv]: https://github.com/nbbrd/picocsv
[sesseltjonna-csv]: https://github.com/skjolber/sesseltjonna-csv
[SimpleFlatMapper]: https://simpleflatmapper.org
[Super CSV]: https://super-csv.github.io/super-csv/index.html
[Univocity]: https://github.com/uniVocity/univocity-parsers
