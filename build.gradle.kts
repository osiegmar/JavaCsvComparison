plugins {
    java
    checkstyle
    id("io.qameta.allure") version "2.12.0" apply false
    id("io.qameta.allure-aggregate-report") version "2.12.0"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.addAll(listOf("-parameters"))
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.qameta.allure")

    dependencies {
        implementation(rootProject)

        testImplementation(platform("org.junit:junit-bom:5.12.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        testImplementation("io.qameta.allure:allure-junit5:2.29.0")
    }

    tasks.withType<Test> {
        useJUnitPlatform()

        ignoreFailures = true

        testLogging {
            events = emptySet()
        }

        systemProperty("tests.path", "${rootDir}/tests")

        finalizedBy("allureReport")
    }
}

dependencies {
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.1")
    implementation("org.junit.jupiter:junit-jupiter-params:5.11.3")
    implementation("io.qameta.allure:allure-java-commons:2.29.0")
}
