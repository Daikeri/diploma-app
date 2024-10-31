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
    }
}

rootProject.name = "movie-rec-sys"
include(":app")
include(":feature")
include(":feature:favorite")
include(":feature:authorization")
include(":data")
include(":app:test")
include(":data:auth")
include(":core")
include(":core:image-downloader")
include(":feature:auth")
