plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
}

android {
    namespace = "com.michaelmccormick.restaurantsearch.features.requestlocation.ui"
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
}

dependencies {
    implementation(Dependencies.UI.ACTIVITY)
    implementation(Dependencies.GooglePlayServices.LOCATION)
}
