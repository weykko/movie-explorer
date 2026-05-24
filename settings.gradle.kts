pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "Movie Explorer"

include(":app")

// Core modules
include(":core:common")
include(":core:domain")
include(":core:data")

// Feature modules
include(":feature:movies")
include(":feature:search")
include(":feature:favorites")
include(":feature:filters")
include(":feature:profile")
