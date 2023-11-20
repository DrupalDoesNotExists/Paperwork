plugins {
    id("io.paperwork.devtools.kotlin")
}

dependencies {
    // Koin DI library
    implementation("io.insert-koin:koin-core:+")?.because(
        "Koin DI framework"
    )
}
