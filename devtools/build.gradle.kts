plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    // Kotlin
    implementation(kotlin("gradle-plugin", "+"))?.because(
        "Kotlin gradle plugin"
    )

    // Dokka gradle plugin
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:+")?.because(
        "Dokka documentation engine plugin"
    )

    // Spotless
    implementation("com.diffplug.spotless:spotless-plugin-gradle:+")?.because(
        "Spotless linting (ktlint included) plugin"
    )
}
