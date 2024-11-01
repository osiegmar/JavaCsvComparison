rootProject.name = "JavaCsvComparison"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

// include everything from the impl directory
file("impl").listFiles()?.filter { it.isDirectory }?.forEach {
    include(":${it.name}")
    project(":${it.name}").projectDir = it
}
