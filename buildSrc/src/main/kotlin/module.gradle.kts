import Dependencies.AndroidX
import Dependencies.Hilt
import Dependencies.Testing.JUnit5

plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}

android {
    sourceSets["main"].manifest.srcFile("src/main/AndroidManifest.xml")
    compileSdkVersion(Android.compileSdk)
    defaultConfig {
        minSdkVersion(Android.minSdk)
        targetSdkVersion(Android.targetSdk)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
        testInstrumentationRunnerArgument("runnerBuilder", "de.mannodermaus.junit5.AndroidJUnit5Builder")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
        exclude("META-INF/*.kotlin_module")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures.viewBinding = true
}

dependencies {
    implementation(Hilt.hilt)
    kapt(Hilt.hiltCompiler)
    implementation(AndroidX.hiltLifecycleViewModel)
    kapt(AndroidX.hiltCompiler)

    androidTestRuntimeOnly(JUnit5.androidTestRunner)
    testRuntimeOnly(JUnit5.jupiterEngine)
    testRuntimeOnly(JUnit5.vintageEngine)
    testImplementation(JUnit5.api)
    testImplementation(JUnit5.params)
}
