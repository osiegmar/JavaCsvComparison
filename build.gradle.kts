plugins {
    java
    checkstyle
    id("io.qameta.allure") version "3.0.1" apply false
    id("io.qameta.allure-aggregate-report") version "3.0.1"
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

        testImplementation(platform("org.junit:junit-bom:6.0.2"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        testImplementation("io.qameta.allure:allure-junit5:2.32.0")
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
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.21.0")
    implementation("org.junit.jupiter:junit-jupiter-params:6.0.2")
    implementation("io.qameta.allure:allure-java-commons:2.32.0")
}
