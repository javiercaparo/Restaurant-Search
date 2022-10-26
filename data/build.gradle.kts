import Dependencies.hilt

plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.HILT)
}

android {
    namespace = "com.michaelmccormick.restaurantsearch.data"
    compileSdk = Versions.TARGET_SDK
    defaultConfig {
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
    }
    compileOptions {
        sourceCompatibility = Versions.JAVA
        targetCompatibility = Versions.JAVA
    }
    kotlinOptions.jvmTarget = Versions.JAVA.toString()
    packagingOptions.resources.excludes.addAll(Configuration.EXCLUDED_PACKAGING_OPTIONS)
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

kapt.correctErrorTypes = true

dependencies {
    implementation(project(Modules.NETWORK))
    implementation(project(Modules.Core.MODELS))

    hilt()
    implementation(Dependencies.Logging.TIMBER)

    testImplementation(project(Modules.Core.TEST))
}
