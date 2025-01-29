@file:Suppress("UnstableApiUsage", "UnstableApiUsage", "UnstableApiUsage", "UnstableApiUsage",
    "RedundantSuppression", "RedundantSuppression"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Restaurant Search"
include(":app")
include(":network")
include(":core:test")
include(":data")
include(":core:models")
include(":features:search:domain")
include(":features:search:ui")
include(":core:ui")
include(":features:requestlocation:ui")
include(":features:requestlocation:domain")
