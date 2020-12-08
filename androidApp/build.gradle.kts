import Dependencies.AndroidX
import Dependencies.Hilt
import Dependencies.Testing.JUnit5

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("de.mannodermaus.android-junit5")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

apply<ReleaseNotesPlugin>()

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
    maven(url = "https://dl.bintray.com/badoo/maven")
}

android {
    compileSdkVersion(Android.compileSdk)
    defaultConfig {
        applicationId = "com.remote_first.androidApp"
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
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":splash"))
    implementation(project(":shared"))
    implementation(project(":design"))
    implementation(project(":navigation"))

    implementation(platform("com.google.firebase:firebase-bom:26.1.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    implementation(Dependencies.Kotlin.stdlib)
    implementation(Dependencies.Kotlin.coroutinesAndroid)

    implementation(AndroidX.material)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.lifeCycleKTX)
    implementation(AndroidX.viewModelKTX)
    implementation(AndroidX.viewModelSavedState)
    implementation(AndroidX.coreKTX)
    implementation(AndroidX.swipeRefreshLayout)
    implementation(AndroidX.recyclerView)
    implementation(AndroidX.cardView)
    implementation(AndroidX.annotation)
    implementation(AndroidX.fragmentKtx)
    implementation(AndroidX.activityKtx)
    implementation("androidx.room:room-runtime:2.2.5")

    implementation(Hilt.hilt)
    kapt(Hilt.hiltCompiler)
    implementation(AndroidX.hiltLifecycleViewModel)
    kapt(AndroidX.hiltCompiler)

    val reaktiveVersion = "1.1.17"
    implementation("com.badoo.reaktive:reaktive:$reaktiveVersion")
    implementation("com.badoo.reaktive:reaktive-annotations:$reaktiveVersion")
    implementation("com.badoo.reaktive:coroutines-interop:$reaktiveVersion")

    implementation("io.github.reactivecircus.flowbinding:flowbinding-material:1.0.0-beta02")

    implementation("com.github.aakira:napier:$1.4.1")
//    val flaggerVersion = "1.0.9"
//    implementation("com.glovoapp.featureflagger:library:$flaggerVersion")
//    kapt("com.glovoapp.featureflagger:compiler:$flaggerVersion")

    implementation("io.coil-kt:coil:1.0.0-rc3")

    androidTestRuntimeOnly(JUnit5.androidTestRunner)
    testRuntimeOnly(JUnit5.jupiterEngine)
    testRuntimeOnly(JUnit5.vintageEngine)
    testImplementation(JUnit5.api)
    testImplementation(JUnit5.params)
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableTransformForLocalTests = true
}
