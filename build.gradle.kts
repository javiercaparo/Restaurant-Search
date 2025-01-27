plugins {
    id("com.android.application") version "7.3.0" apply false
    id("com.android.library") version "7.3.0" apply false
    id("org.jetbrains.kotlin.android") version Versions.KOTLIN apply false
    id("org.jetbrains.kotlin.jvm") version Versions.KOTLIN apply false
    id("com.google.dagger.hilt.android") version Versions.HILT apply false
    id("io.gitlab.arturbosch.detekt") version("1.23.7")
}

// repositories block removed

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
