plugins {
    application
    checkstyle
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "comparison.Comparison"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.18.0")
    implementation("com.github.skjolber.sesseltjonna-csv:parser:1.0.25")
    implementation("com.opencsv:opencsv:5.9")
    implementation("com.univocity:univocity-parsers:2.9.1")
    implementation("de.siegmar:fastcsv:3.3.1")
    implementation("net.sf.supercsv:super-csv:2.4.0")
    implementation("net.sourceforge.javacsv:javacsv:2.0")
    implementation("net.steppschuh.markdowngenerator:markdowngenerator:1.3.1.1")
    implementation("org.apache.commons:commons-csv:1.10.0")
    implementation("org.simpleflatmapper:sfm-csv:8.2.3")
    implementation("org.csveed:csveed:0.7.5")
    implementation("org.slf4j:slf4j-nop:2.0.7")
    implementation("com.github.nbbrd.picocsv:picocsv:2.4.0")
    implementation("org.apache.commons:commons-csv:1.12.0")
    implementation("org.simpleflatmapper:sfm-csv:9.0.2")
    implementation("org.csveed:csveed:0.8.2")
    implementation("org.slf4j:slf4j-nop:2.0.16")
    implementation("com.github.nbbrd.picocsv:picocsv:2.4.0")
}
