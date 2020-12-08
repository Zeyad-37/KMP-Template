import Dependencies.AndroidX
import Dependencies.Hilt
import Dependencies.Testing.JUnit5
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

apply<DetektPlugin>()

configure<DetektExtension> {
    ignoreFailures = true
    file("$rootDir/config/detekt/detekt.yml").takeIf { it.isFile }?.let { config.from(it) }
    file("$rootDir/config/detekt/baseline.xml").takeIf { it.isFile }?.let { baseline = it }
    reports {
        html.enabled = true
        xml.enabled = true
        txt.enabled = false
    }
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
