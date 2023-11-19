# Contributing Guidelines

In order to keep the codebase clean, stable and well-supported every contribution
have to follow a set of rules. These are easy enough so there is no need for a lot
of extra effort.

### Git commits

This repository is currently using the Gitflow strategy, so Your commits should be
formatted properly. Otherwise, It'll be hard to merge these changes to the codebase
without breaking the overall workflow.

Also, commit messages should follow the [conventional commits](https://conventionalcommits.org/)
style so everything (possibly including automated workers, etc.) would work fine.

### Kotlin conventions

As Paperwork is written in Kotlin, Your code have to meet the requirements
of [good code conventions](https://kotlinlang.org/docs/coding-conventions.html)
defined by the KotlinLang.

KotlinLang also defines the [library creator guidelines](https://kotlinlang.org/docs/jvm-api-guidelines-introduction.html).

### Document everything

Any library should be well documented. Otherwise, It may appear hard to use
and understand for the end user. This project is not an exception. Every function,
class and variable should be documented if possible.

Documentation should be formatted as a standard kotlin doc described in the KotlinLang
coding conventions. Dokka is used for building the HTML out of it.

### Test and You're the best

Every feature should have a unit test under the `test` module. It is needed
to quickly perform the needed checks and to mitigate bugs and unintended behaviours.

Before actually pushing Your changes, You have to run the `check` task that
runs enforces the linting and code style rules, runs the tests.

### Divide and Conquer

To provide a better support for the various platforms, You should always separate
the core, platform-independent logic from the platform-specific implementations.

E.g., You're implementing the configuration serialization module, and You want to
provide an advanced support for Minecraft types serialization. Then You would put the
shared serialization logic to the `configser-core` and Paper-specific extensions to the
`configser-paper` to provide an end users with the fine-grained control of what they
include to their classpath.

```
.
├── configser-core
│   ├── CoreClass.kt
│   └── SharedConstants.kt
└── configser-paper
    ├── PaperMagicConstants.kt
    └── PaperTypeDeserializer.kt

```

Naming conventions for the resulting modules are `<feature-name>-core` and `<feature-name>-<platform>`.
