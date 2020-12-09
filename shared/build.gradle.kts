import Libs.AndroidX
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework.BitcodeEmbeddingMode.BITCODE
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("kotlin-parcelize")
    id("com.squareup.sqldelight")
    id("dagger.hilt.android.plugin")
}

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}

kotlin {
    android()
    val isDevice = System.getenv("SDK_NAME")?.startsWith("iphoneos") == true
    val ios: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget = if (isDevice) ::iosArm64 else ::iosX64
    ios("ios") {
        binaries {
            framework {
                baseName = "shared"
                embedBitcode = BITCODE
                transitiveExport = true
            }
        }
    }
    js("js") {
        browser()
    }
    sourceSets {

        val hiltVersion = "2.28-alpha"

        val commonMain by getting {
            dependencies {
                implementation(project(":shared_network"))

                implementation(Libs.Reaktive.reaktive)
                implementation(Libs.Reaktive.reaktiveAnnotation)
                implementation(Libs.Reaktive.reaktiveInterop)

                implementation(Libs.Kotlin.coroutinesCore)

                implementation(Libs.Kotlin.serialization)

                api(Libs.Ktor.core)
                implementation(Libs.Ktor.logging)
                implementation(Libs.Ktor.clientSerialization)

                api(Libs.SqlDelight.runtime)

                api(Libs.Other.napier)

                implementation(Libs.Hilt.hilt)
                configurations["kapt"].dependencies.add(DefaultExternalModuleDependency(
                        "com.google.dagger", "hilt-android-compiler", hiltVersion)
                )
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(AndroidX.viewModelKTX)
                implementation(AndroidX.viewModelSavedState)
                implementation(Libs.Kotlin.coroutinesAndroid)
                implementation(Libs.Ktor.android)
                implementation(Libs.SqlDelight.android)
                implementation(Libs.Hilt.hilt)
                configurations["kapt"].dependencies.add(DefaultExternalModuleDependency(
                        "com.google.dagger", "hilt-android-compiler", hiltVersion)
                )
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.1")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Libs.Kotlin.coroutinesNativeMetaData)
                implementation(Libs.Kotlin.coroutinesNative)
                implementation(Libs.Ktor.iOS)
                implementation(Libs.SqlDelight.iOS)
                configurations["kapt"].dependencies.add(DefaultExternalModuleDependency(
                        "com.google.dagger", "hilt-android-compiler", hiltVersion)
                )
            }
        }
        val iosTest by getting
        val jsMain by getting {
            dependencies {
                implementation(Libs.Kotlin.coroutinesJS)
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        all {
            languageSettings.progressiveMode = true
            languageSettings.useExperimentalAnnotation("kotlin.Experimental")
        }
    }
}

android {
    compileSdkVersion(Android.compileSdk)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(Android.minSdk)
        targetSdkVersion(Android.targetSdk)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.remote_first.shared.cache"
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val targetName = "ios"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)
