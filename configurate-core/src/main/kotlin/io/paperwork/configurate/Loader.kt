/*
MIT License

Copyright (c) 2023 Paperwork Contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package io.paperwork.configurate

import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.dsl.module
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.ConfigurationVisitor
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.loader.ConfigurationLoader
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import org.spongepowered.configurate.transformation.ConfigurationTransformation
import java.nio.file.Path
import org.spongepowered.configurate.hocon.HoconConfigurationLoader as HoconLoader
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader as JacksonLoader
import org.spongepowered.configurate.loader.AbstractConfigurationLoader as AbstractLoader
import org.spongepowered.configurate.xml.XmlConfigurationLoader as XmlLoader
import org.spongepowered.configurate.yaml.YamlConfigurationLoader as YamlLoader

/**
 * Paperwork [TypeSerializerCollection] that contains
 * all the custom bindings.
 */
val PAPERWORK_COLLECTION: TypeSerializerCollection =
    TypeSerializerCollection.defaults().childBuilder()
        .registerAnnotatedObjects(objectMapperFactory())
        .build()

/**
 * Extension for the [AbstractLoader.Builder] that
 * shortcuts the serializer collection registration.
 *
 * @param collection Collection of serializers
 * @return This builder for chaining
 */
fun <T : AbstractLoader.Builder<T, L>?, L : AbstractLoader<*>?> AbstractLoader.Builder<T, L>.applySerdesCollection(
    collection: TypeSerializerCollection,
): T {
    // Register all serializers from the collection
    return this.defaultOptions { options ->
        options.serializers { it.registerAll(collection) }
    }
}

/**
 * Extension for the [AbstractLoader.Builder] that
 * shortcuts the appliance of the default options
 * preset.
 *
 * @param path Path to the file
 * @param collection Serializer collection to use. [PAPERWORK_COLLECTION] by default.
 * @return This builder for chaining
 */
fun <T : AbstractLoader.Builder<T, L>, L : AbstractLoader<*>> AbstractLoader.Builder<T, L>.withPreset(
    path: Path,
    collection: TypeSerializerCollection,
): T {
    return this.path(path).applySerdesCollection(collection)
}

/**
 * Extension for the [Module] that applies
 * all transformations and visitors by looking
 * them up in Koin.
 *
 * @param loader Node loader to use
 * @return Singleton definition for the Koin module
 */
private fun Module.koinCompliantNode(loader: ConfigurationLoader<*>): KoinDefinition<ConfigurationNode> {
    // Get the node
    val node = loader.load()

    // All lookups are done within the definition
    return single<ConfigurationNode> {
        // Apply all visitors
        getAll<ConfigurationVisitor<*, *, *>>() // Get all visitor bindings
            .forEach { node.visit(it) } // Apply every visitor

        // Apply all transformers
        getAll<ConfigurationTransformation>() // Get all transformations
            .forEach { it.apply(node) }

        node
    }
}

/**
 * Extension for the [AbstractLoader.Builder] that
 * shortcuts the [Module] construction from this
 * builder. The resulting module contains singletons
 * for this loader and [ConfigurationNode].
 *
 * If there are any [ConfigurationTransformation] or
 * [ConfigurationVisitor] definitions, then they're
 * applied to the [ConfigurationNode] in this order:
 * 1. Visitors
 * 2. Transformations
 *
 * @return Koin module
 */
fun <T : AbstractLoader.Builder<T, L>?, L : AbstractLoader<*>> AbstractLoader.Builder<T, L>.toKoinModule(): Module {
    // Build a loader instance from
    // this builder.
    val loader = this.build()

    return module {
        single<ConfigurationLoader<*>> { loader }
        koinCompliantNode(loader)
    }
}

// No configuration as it repeats itself.

fun xmlLoader(
    path: Path,
    collection: TypeSerializerCollection = PAPERWORK_COLLECTION,
): Module = XmlLoader.builder().withPreset(path, collection).toKoinModule()

fun yamlLoader(
    path: Path,
    collection: TypeSerializerCollection = PAPERWORK_COLLECTION,
): Module = YamlLoader.builder().withPreset(path, collection).toKoinModule()

fun jsonLoader(
    path: Path,
    collection: TypeSerializerCollection = PAPERWORK_COLLECTION,
): Module = JacksonLoader.builder().withPreset(path, collection).toKoinModule()

fun hoconLoader(
    path: Path,
    collection: TypeSerializerCollection = PAPERWORK_COLLECTION,
): Module = HoconLoader.builder().withPreset(path, collection).toKoinModule()
