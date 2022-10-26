import Dependencies.compose
import Dependencies.hilt

plugins {
    id("com.android.application")
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.HILT)
}

android {
    namespace = "com.michaelmccormick.restaurantsearch"
    compileSdk = Versions.TARGET_SDK
    defaultConfig {
        applicationId = "com.michaelmccormick.restaurantsearch"
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "com.michaelmccormick.restaurantsearch.HiltTestRunner"
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = Versions.JAVA
        targetCompatibility = Versions.JAVA
    }
    kotlinOptions.jvmTarget = Versions.JAVA.toString()
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = Versions.COMPOSE_COMPILER
    packagingOptions.resources.excludes.addAll(Configuration.EXCLUDED_PACKAGING_OPTIONS)
}

kapt.correctErrorTypes = true

dependencies {
    implementation(project(Modules.DATA))
    implementation(project(Modules.Features.Search.UI))
    implementation(project(Modules.Features.RequestLocation.UI))
    implementation(project(Modules.Features.RequestLocation.DOMAIN))
    implementation(project(Modules.Core.MODELS))

    hilt()
    compose()
    implementation(Dependencies.Logging.TIMBER)

    androidTestImplementation(Dependencies.UiTest.TEST_RUNNER)
    androidTestImplementation(Dependencies.UiTest.ESPRESSO_INTENTS)
    androidTestImplementation(Dependencies.UiTest.COMPOSE_UI_TEST)
    androidTestImplementation(Dependencies.UiTest.HILT_TEST)
    kaptAndroidTest(Dependencies.DI.HILT_COMPILER)
}
