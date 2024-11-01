module JavaCsvComparison {

    exports comparison;
    exports specreader;
    exports specreader.spec;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires org.junit.jupiter.params;
    requires io.qameta.allure.commons;
    requires io.qameta.allure.model;

}
