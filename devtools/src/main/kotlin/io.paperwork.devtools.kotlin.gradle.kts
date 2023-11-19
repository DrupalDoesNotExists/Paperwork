plugins {
    kotlin("jvm")
    id("io.paperwork.devtools.dokka")
    id("com.diffplug.spotless")
}

repositories {
    mavenCentral()
}

dependencies {
    // Include test framework
    testImplementation(kotlin("test"))?.because(
        "Kotlin test environment"
    )
}

tasks.test {
    // Use JUnit as the test backend
    useJUnitPlatform()
}

// Configure linting
// with Spotless plugin
// and ktlint.
spotless {
    kotlin {
        ktlint()
        licenseHeaderFile("${project.rootDir.resolve("LICENSE")}")
    }
}
