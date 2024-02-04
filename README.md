# Parsing non-standard CSV with different Java CSV libraries

Unfortunately there is no real industry standard for CSV files. The closest thing we do currently
have (since 2005) is [RFC 4180](https://tools.ietf.org/html/rfc4180).
Working with non-standardized data often comes with surprises but what exactly happens when parsing
CSV data that doesn't match this RFC?

This project is about to find that out...

This benchmark project was created for the development of
[FastCSV](https://github.com/osiegmar/FastCSV).

> :warning: Due to the fact that this comparison uses the result of FastCSV as a reference value
> (expected result) the comparison is highly biased.

## Implementations under test

- Commons CSV 1.10.0
- FastCSV 3.0.0
- Jackson CSV 2.16.1
- Java CSV 2.0
- Opencsv 5.9
- sesseltjonna-csv 1.0.25
- SimpleFlatMapper 8.2.3
- Super CSV 2.4.0
- Univocity 2.9.1

## Execute

    ./gradlew run

# Results

In order to have results which are easy to compare (even with multi-line records) we need to use
some special characters.
The libraries itself don't get in touch with these special characters as the conversion is done
from the outside.

| Character | Usage             |
| --------- | ----------------- |
| `␣`       | Space             |
| `␍`       | Carriage return   |
| `␊`       | Line feed         |
| `[SE]`    | Skip emtpy rows   |
| `⏎`       | New row           |
| `↷`       | New column        |
| `◯`       | Empty field       |
| `∅`       | Empty list        |

## Unexpected results in Commons CSV
| Input    | Flags | Commons CSV                 | Expected | Implemented as expected by                    |
| -------- | ----- | --------------------------- | -------- | --------------------------------------------- |
| `A,"B`   | —     | :boom: UncheckedIOException | `A↷B`    | FastCSV, JavaCSV, Simpleflatmapper, Univocity |
| `"A,B`   | —     | :boom: UncheckedIOException | `A,B`    | FastCSV, JavaCSV, Simpleflatmapper, Univocity |
| `"D"␣`   | —     | `D`                         | `D␣`     | FastCSV, Opencsv, SuperCSV                    |
| `"A,B"␣` | —     | `A,B`                       | `A,B␣`   | FastCSV, SuperCSV                             |
| `"D"z`   | —     | :boom: UncheckedIOException | `Dz`     | FastCSV, Opencsv, SuperCSV                    |
| `"A,B"z` | —     | :boom: UncheckedIOException | `A,Bz`   | FastCSV, SuperCSV                             |

## Unexpected results in JacksonCSV
| Input    | Flags | JacksonCSV                         | Expected | Implemented as expected by                    |
| -------- | ----- | ---------------------------------- | -------- | --------------------------------------------- |
| `A,"B`   | —     | :boom: RuntimeJsonMappingException | `A↷B`    | FastCSV, JavaCSV, Simpleflatmapper, Univocity |
| `"A,B`   | —     | :boom: RuntimeJsonMappingException | `A,B`    | FastCSV, JavaCSV, Simpleflatmapper, Univocity |
| `"D"␣`   | —     | `D`                                | `D␣`     | FastCSV, Opencsv, SuperCSV                    |
| `"A,B"␣` | —     | `A,B`                              | `A,B␣`   | FastCSV, SuperCSV                             |
| `"D"z`   | —     | :boom: RuntimeJsonMappingException | `Dz`     | FastCSV, Opencsv, SuperCSV                    |
| `"A,B"z` | —     | :boom: RuntimeJsonMappingException | `A,Bz`   | FastCSV, SuperCSV                             |

## Unexpected results in JavaCSV
| Input    | Flags | JavaCSV | Expected | Implemented as expected by |
| -------- | ----- | ------- | -------- | -------------------------- |
| `"D"␣`   | —     | `D`     | `D␣`     | FastCSV, Opencsv, SuperCSV |
| `"A,B"␣` | —     | `A,B`   | `A,B␣`   | FastCSV, SuperCSV          |
| `"D"z`   | —     | `D`     | `Dz`     | FastCSV, Opencsv, SuperCSV |
| `"A,B"z` | —     | `A,B`   | `A,Bz`   | FastCSV, SuperCSV          |

## Unexpected results in Opencsv
| Input    | Flags | Opencsv                          | Expected | Implemented as expected by                                                               |
| -------- | ----- | -------------------------------- | -------- | ---------------------------------------------------------------------------------------- |
| `A,"B`   | —     | :boom: CsvMalformedLineException | `A↷B`    | FastCSV, JavaCSV, Simpleflatmapper, Univocity                                            |
| `A,B"`   | —     | :boom: CsvMalformedLineException | `A↷B"`   | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |
| `"A,B`   | —     | :boom: CsvMalformedLineException | `A,B`    | FastCSV, JavaCSV, Simpleflatmapper, Univocity                                            |
| `"A␍B"`  | —     | `A␊B`                            | `A␍B`    | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |
| `"A␍␊B"` | —     | `A␊B`                            | `A␍␊B`   | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |
| `"A,B"␣` | —     | `A,B"␣`                          | `A,B␣`   | FastCSV, SuperCSV                                                                        |
| `␣"D"`   | —     | `␣D`                             | `␣"D"`   | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |
| `␣"D"␣`  | —     | `␣D"␣`                           | `␣"D"␣`  | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |
| `"A,B"z` | —     | `A,B"z`                          | `A,Bz`   | FastCSV, SuperCSV                                                                        |
| `z"D"`   | —     | `zD`                             | `z"D"`   | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |
| `z"A,B"` | —     | `zA,B`                           | `z"A↷B"` | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |
| `z"D"z`  | —     | `zD"z`                           | `z"D"z`  | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |

## Unexpected results in sesseltjonna-csv
| Input      | Flags | sesseltjonna-csv           | Expected   | Implemented as expected by                                                                |
| ---------- | ----- | -------------------------- | ---------- | ----------------------------------------------------------------------------------------- |
| `A,␊B`     | —     | :boom: CsvException        | `A↷◯⏎B`    | Commons CSV, FastCSV, JacksonCSV, JavaCSV, Opencsv, Simpleflatmapper, SuperCSV, Univocity |
| `␣,␊D`     | —     | :boom: CsvException        | `␣↷◯⏎D`    | Commons CSV, FastCSV, JacksonCSV, JavaCSV, Opencsv, Simpleflatmapper, SuperCSV, Univocity |
| `A␍B`      | —     | `A␍B`                      | `A⏎B`      | Commons CSV, FastCSV, JacksonCSV, JavaCSV, Opencsv, Simpleflatmapper, SuperCSV            |
| `␍D`       | —     | `␍D`                       | `◯⏎D`      | Commons CSV, FastCSV, JacksonCSV, JavaCSV, Opencsv, Simpleflatmapper, SuperCSV            |
| `A,"B`     | —     | :boom: CsvBuilderException | `A↷B`      | FastCSV, JavaCSV, Simpleflatmapper, Univocity                                             |
| `"A,B`     | —     | :boom: CsvBuilderException | `A,B`      | FastCSV, JavaCSV, Simpleflatmapper, Univocity                                             |
| `A␊B,C`    | —     | `A⏎B,C`                    | `A⏎B↷C`    | Commons CSV, FastCSV, JacksonCSV, JavaCSV, Opencsv, Simpleflatmapper, SuperCSV, Univocity |
| `A,B␊C`    | —     | :boom: CsvException        | `A↷B⏎C`    | Commons CSV, FastCSV, JacksonCSV, JavaCSV, Opencsv, Simpleflatmapper, SuperCSV, Univocity |
| `A␊;B,C␊D` | —     | `A⏎;B,C⏎D`                 | `A⏎;B↷C⏎D` | Commons CSV, FastCSV, JacksonCSV, JavaCSV, Opencsv, Simpleflatmapper, SuperCSV, Univocity |
| `"D"␣`     | —     | `D`                        | `D␣`       | FastCSV, Opencsv, SuperCSV                                                                |
| `"A,B"␣`   | —     | `A,B`                      | `A,B␣`     | FastCSV, SuperCSV                                                                         |
| `"D"z`     | —     | `D`                        | `Dz`       | FastCSV, Opencsv, SuperCSV                                                                |
| `"A,B"z`   | —     | `A,B`                      | `A,Bz`     | FastCSV, SuperCSV                                                                         |

## Unexpected results in Simpleflatmapper
| Input    | Flags | Simpleflatmapper | Expected | Implemented as expected by |
| -------- | ----- | ---------------- | -------- | -------------------------- |
| `"D"␣`   | —     | `D"␣`            | `D␣`     | FastCSV, Opencsv, SuperCSV |
| `"A,B"␣` | —     | `A,B"␣`          | `A,B␣`   | FastCSV, SuperCSV          |
| `"D"z`   | —     | `D"z`            | `Dz`     | FastCSV, Opencsv, SuperCSV |
| `"A,B"z` | —     | `A,B"z`          | `A,Bz`   | FastCSV, SuperCSV          |

## Unexpected results in SuperCSV
| Input    | Flags | SuperCSV                 | Expected | Implemented as expected by                                                               |
| -------- | ----- | ------------------------ | -------- | ---------------------------------------------------------------------------------------- |
| `A,"B`   | —     | :boom: SuperCsvException | `A↷B`    | FastCSV, JavaCSV, Simpleflatmapper, Univocity                                            |
| `A,B"`   | —     | :boom: SuperCsvException | `A↷B"`   | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |
| `"A,B`   | —     | :boom: SuperCsvException | `A,B`    | FastCSV, JavaCSV, Simpleflatmapper, Univocity                                            |
| `"A␍B"`  | —     | `A␊B`                    | `A␍B`    | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |
| `"A␍␊B"` | —     | `A␊B`                    | `A␍␊B`   | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |
| `␣"D"`   | —     | `␣D`                     | `␣"D"`   | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |
| `␣"D"␣`  | —     | `␣D␣`                    | `␣"D"␣`  | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |
| `z"D"`   | —     | `zD`                     | `z"D"`   | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |
| `z"A,B"` | —     | `zA,B`                   | `z"A↷B"` | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |
| `z"D"z`  | —     | `zDz`                    | `z"D"z`  | Commons CSV, FastCSV, JacksonCSV, JavaCSV, sesseltjonna-csv, Simpleflatmapper, Univocity |

## Unexpected results in Univocity
| Input    | Flags  | Univocity | Expected | Implemented as expected by                                                                       |
| -------- | ------ | --------- | -------- | ------------------------------------------------------------------------------------------------ |
| `A␍B`    | —      | `A␍B`     | `A⏎B`    | Commons CSV, FastCSV, JacksonCSV, JavaCSV, Opencsv, Simpleflatmapper, SuperCSV                   |
| `D␍`     | —      | `D␍`      | `D`      | Commons CSV, FastCSV, JacksonCSV, JavaCSV, Opencsv, sesseltjonna-csv, Simpleflatmapper, SuperCSV |
| `␍D`     | —      | `␍D`      | `◯⏎D`    | Commons CSV, FastCSV, JacksonCSV, JavaCSV, Opencsv, Simpleflatmapper, SuperCSV                   |
| `␍D`     | `[SE]` | `␍D`      | `D`      | Commons CSV, FastCSV, JacksonCSV, JavaCSV, SuperCSV                                              |
| `A␍␊B`   | —      | `A␍⏎B`    | `A⏎B`    | Commons CSV, FastCSV, JacksonCSV, JavaCSV, Opencsv, sesseltjonna-csv, Simpleflatmapper, SuperCSV |
| `D␍␊`    | —      | `D␍`      | `D`      | Commons CSV, FastCSV, JacksonCSV, JavaCSV, Opencsv, sesseltjonna-csv, Simpleflatmapper, SuperCSV |
| `␍␊D`    | —      | `␍⏎D`     | `◯⏎D`    | Commons CSV, FastCSV, JacksonCSV, JavaCSV, Opencsv, sesseltjonna-csv, Simpleflatmapper, SuperCSV |
| `␍␊D`    | `[SE]` | `␍⏎D`     | `D`      | Commons CSV, FastCSV, JacksonCSV, JavaCSV, SuperCSV                                              |
| `"D"␣`   | —      | `D`       | `D␣`     | FastCSV, Opencsv, SuperCSV                                                                       |
| `"A,B"␣` | —      | `A,B`     | `A,B␣`   | FastCSV, SuperCSV                                                                                |
| `"D"z`   | —      | `"D"z`    | `Dz`     | FastCSV, Opencsv, SuperCSV                                                                       |
| `"A,B"z` | —      | `"A,B"z`  | `A,Bz`   | FastCSV, SuperCSV                                                                                |

## Big picture
| Input      | Flags  | Expected   | Commons CSV        | FastCSV            | JacksonCSV         | JavaCSV            | Opencsv            | sesseltjonna-csv   | Simpleflatmapper   | SuperCSV           | Univocity          |
| ---------- | ------ | ---------- | ------------------ | ------------------ | ------------------ | ------------------ | ------------------ | ------------------ | ------------------ | ------------------ | ------------------ |
| `D`        | —      | `D`        | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `D,D`      | —      | `D↷D`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `,D`       | —      | `◯↷D`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `␣`        | —      | `␣`        | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `␣,␣`      | —      | `␣↷␣`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `,␣`       | —      | `◯↷␣`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `␣D`       | —      | `␣D`       | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `␣D␣,␣D␣`  | —      | `␣D␣↷␣D␣`  | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `D,`       | —      | `D↷◯`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `A,␊B`     | —      | `A↷◯⏎B`    | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :boom:             | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `␣,`       | —      | `␣↷◯`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `␣,␊D`     | —      | `␣↷◯⏎D`    | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :boom:             | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `A␊B`      | —      | `A⏎B`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `D␊`       | —      | `D`        | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `␊D`       | —      | `◯⏎D`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `␊D`       | `[SE]` | `D`        | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :heavy_minus_sign: | :heavy_minus_sign: | :heavy_minus_sign: | :white_check_mark: | :white_check_mark: |
| `A␍B`      | —      | `A⏎B`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: | :white_check_mark: | :x:                |
| `D␍`       | —      | `D`        | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x:                |
| `␍D`       | —      | `◯⏎D`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: | :white_check_mark: | :x:                |
| `␍D`       | `[SE]` | `D`        | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :heavy_minus_sign: | :heavy_minus_sign: | :heavy_minus_sign: | :white_check_mark: | :x:                |
| `A␍␊B`     | —      | `A⏎B`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x:                |
| `D␍␊`      | —      | `D`        | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x:                |
| `␍␊D`      | —      | `◯⏎D`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x:                |
| `␍␊D`      | `[SE]` | `D`        | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :heavy_minus_sign: | :heavy_minus_sign: | :heavy_minus_sign: | :white_check_mark: | :x:                |
| `"␣D␣"`    | —      | `␣D␣`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `"D"`      | —      | `D`        | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `"D",D`    | —      | `D↷D`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `D,"D"`    | —      | `D↷D`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `A,"B`     | —      | `A↷B`      | :boom:             | :white_check_mark: | :boom:             | :white_check_mark: | :boom:             | :boom:             | :white_check_mark: | :boom:             | :white_check_mark: |
| `A,B"`     | —      | `A↷B"`     | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :boom:             | :white_check_mark: | :white_check_mark: | :boom:             | :white_check_mark: |
| `"A,B`     | —      | `A,B`      | :boom:             | :white_check_mark: | :boom:             | :white_check_mark: | :boom:             | :boom:             | :white_check_mark: | :boom:             | :white_check_mark: |
| `"""D"`    | —      | `"D`       | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `"D"""`    | —      | `D"`       | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `"A""B"`   | —      | `A"B`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `"A␊B"`    | —      | `A␊B`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `"A␍B"`    | —      | `A␍B`      | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: |
| `"A␍␊B"`   | —      | `A␍␊B`     | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: |
| `A␊B,C`    | —      | `A⏎B↷C`    | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `A,B␊C`    | —      | `A↷B⏎C`    | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :boom:             | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `A␊;B,C␊D` | —      | `A⏎;B↷C⏎D` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `"D"␣`     | —      | `D␣`       | :x:                | :white_check_mark: | :x:                | :x:                | :white_check_mark: | :x:                | :x:                | :white_check_mark: | :x:                |
| `"A,B"␣`   | —      | `A,B␣`     | :x:                | :white_check_mark: | :x:                | :x:                | :x:                | :x:                | :x:                | :white_check_mark: | :x:                |
| `␣"D"`     | —      | `␣"D"`     | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: |
| `␣"D"␣`    | —      | `␣"D"␣`    | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: |
| `"D"z`     | —      | `Dz`       | :boom:             | :white_check_mark: | :boom:             | :x:                | :white_check_mark: | :x:                | :x:                | :white_check_mark: | :x:                |
| `"A,B"z`   | —      | `A,Bz`     | :boom:             | :white_check_mark: | :boom:             | :x:                | :x:                | :x:                | :x:                | :white_check_mark: | :x:                |
| `z"D"`     | —      | `z"D"`     | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: |
| `z"A,B"`   | —      | `z"A↷B"`   | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: |
| `z"D"z`    | —      | `z"D"z`    | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: | :white_check_mark: | :x:                | :white_check_mark: |
