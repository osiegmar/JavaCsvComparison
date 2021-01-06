# Parsing non-standard CSV with different Java CSV libraries

Unfortunately there is no real industry standard for CSV files. The closest thing we do currently
have (since 2005) is [RFC 4180](https://tools.ietf.org/html/rfc4180).
Working with non-standardized data often comes with surprises but what exactly happens when parsing
CSV data that doesn't match this RFC?

This project is about to find that out...

This benchmark project was created for the development of
[FastCSV](https://github.com/osiegmar/FastCSV).

## Implementations under test

- Commons CSV 1.8
- FastCSV 2.0.0
- Jackson CSV 2.12.0
- JavaCSV 2.0
- Opencsv 5.3
- SimpleFlatMapper 8.2.3
- Super CSV 2.4.0
- Univocity 2.9.0

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
| `✘`       | Unexpected        |

## Mixed behaviour
| Input    | Commons CSV | FastCSV  | JacksonCSV  | JavaCSV  | Opencsv     | Sfm      | SuperCSV    | Univocity |
| -------- | ----------- | -------- | ----------- | -------- | ----------- | -------- | ----------- | --------- |
| `A,"B`   | `EXCEPTION` | `A↷B`    | `EXCEPTION` | `A↷B`    | `EXCEPTION` | `A↷B`    | `EXCEPTION` | `A↷B`     |
| `A,B"`   | `A↷B"`      | `A↷B"`   | `A↷B"`      | `A↷B"`   | `EXCEPTION` | `A↷B"`   | `EXCEPTION` | `A↷B"`    |
| `"A,B`   | `EXCEPTION` | `A,B`    | `EXCEPTION` | `A,B`    | `EXCEPTION` | `A,B`    | `EXCEPTION` | `A,B`     |
| `"A␍B"`  | `A␍B`       | `A␍B`    | `A␍B`       | `A␍B`    | `A␊B`       | `A␍B`    | `A␊B`       | `A␍B`     |
| `"A␍␊B"` | `A␍␊B`      | `A␍␊B`   | `A␍␊B`      | `A␍␊B`   | `A␊B`       | `A␍␊B`   | `A␊B`       | `A␍␊B`    |
| `"D"␣`   | `D`         | `D␣`     | `D`         | `D`      | `D␣`        | `D"␣`    | `D␣`        | `D`       |
| `"A,B"␣` | `A,B`       | `A,B␣`   | `A,B`       | `A,B`    | `A,B"␣`     | `A,B"␣`  | `A,B␣`      | `A,B`     |
| `␣"D"`   | `␣"D"`      | `␣"D"`   | `␣"D"`      | `␣"D"`   | `␣D`        | `␣"D"`   | `␣D`        | `␣"D"`    |
| `␣"D"␣`  | `␣"D"␣`     | `␣"D"␣`  | `␣"D"␣`     | `␣"D"␣`  | `␣D"␣`      | `␣"D"␣`  | `␣D␣`       | `␣"D"␣`   |
| `"D"z`   | `EXCEPTION` | `Dz`     | `EXCEPTION` | `D`      | `Dz`        | `D"z`    | `Dz`        | `"D"z`    |
| `"A,B"z` | `EXCEPTION` | `A,Bz`   | `EXCEPTION` | `A,B`    | `A,B"z`     | `A,B"z`  | `A,Bz`      | `"A,B"z`  |
| `z"D"`   | `z"D"`      | `z"D"`   | `z"D"`      | `z"D"`   | `zD`        | `z"D"`   | `zD`        | `z"D"`    |
| `z"A,B"` | `z"A↷B"`    | `z"A↷B"` | `z"A↷B"`    | `z"A↷B"` | `zA,B`      | `z"A↷B"` | `zA,B`      | `z"A↷B"`  |
| `z"D"z`  | `z"D"z`     | `z"D"z`  | `z"D"z`     | `z"D"z`  | `zD"z`      | `z"D"z`  | `zDz`       | `z"D"z`   |

## Oddities in Univocity
| Input      | Commons CSV | FastCSV | JacksonCSV | JavaCSV | Opencsv | Sfm   | SuperCSV | Univocity |
| ---------- | ----------- | ------- | ---------- | ------- | ------- | ----- | -------- | --------- |
| `A␍B`      | `A⏎B`       | `A⏎B`   | `A⏎B`      | `A⏎B`   | `A⏎B`   | `A⏎B` | `A⏎B`    | `A␍B ✘`   |
| `D␍`       | `D`         | `D`     | `D`        | `D`     | `D`     | `D`   | `D`      | `D␍ ✘`    |
| `␍D`       | `◯⏎D`       | `◯⏎D`   | `◯⏎D`      | `◯⏎D`   | `◯⏎D`   | `◯⏎D` | `◯⏎D`    | `␍D ✘`    |
| `␍D [SE]`  | `D`         | `D`     | `D`        | `D`     | `D`     | `D`   | `D`      | `␍D ✘`    |
| `A␍␊B`     | `A⏎B`       | `A⏎B`   | `A⏎B`      | `A⏎B`   | `A⏎B`   | `A⏎B` | `A⏎B`    | `A␍⏎B ✘`  |
| `D␍␊`      | `D`         | `D`     | `D`        | `D`     | `D`     | `D`   | `D`      | `D␍ ✘`    |
| `␍␊D`      | `◯⏎D`       | `◯⏎D`   | `◯⏎D`      | `◯⏎D`   | `◯⏎D`   | `◯⏎D` | `◯⏎D`    | `␍⏎D ✘`   |
| `␍␊D [SE]` | `D`         | `D`     | `D`        | `D`     | `D`     | `D`   | `D`      | `␍⏎D ✘`   |
