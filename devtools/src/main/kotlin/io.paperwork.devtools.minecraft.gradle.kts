plugins {
    id("io.paperwork.devtools.kotlin")
}

dependencies {
    // Depend on the Paper API. Compile scope
    // as it is only suitable for plugins which
    // run on top of the server, so It is already
    // provided and shouldn't be shaded!
    compileOnly("io.papermc.paper:paper-api:+")
}
