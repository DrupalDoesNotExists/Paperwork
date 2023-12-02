plugins {
    id("io.paperwork.devtools.koin")
}

dependencies {
    // Include configurate core as an API scope
    // because it should be transitive.
    api("org.spongepowered:configurate-core:+")
    api("org.spongepowered:configurate-extra-kotlin:+")

    // Format dependencies
    implementation("org.spongepowered:configurate-xml:+")
    implementation("org.spongepowered:configurate-yaml:+")
    implementation("org.spongepowered:configurate-hocon:+")
    implementation("org.spongepowered:configurate-jackson:+")
}
