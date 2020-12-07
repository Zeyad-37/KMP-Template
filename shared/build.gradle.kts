import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
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
                embedBitcode = org.jetbrains.kotlin.gradle.plugin.mpp.Framework.BitcodeEmbeddingMode.BITCODE
                transitiveExport = true
            }
        }
    }
    js("js") {
        browser()
    }
    sourceSets {

        val ktorVersion = "1.4.1"
        val sqlDelightVersion = "1.4.3"
        val hiltVersion = "2.28-alpha"

        val commonMain by getting {
            dependencies {
                implementation(project(":shared_network"))
                val reaktiveVersion = "1.1.17"
                implementation("com.badoo.reaktive:reaktive:$reaktiveVersion")
                implementation("com.badoo.reaktive:reaktive-annotations:$reaktiveVersion")
                implementation("com.badoo.reaktive:coroutines-interop:$reaktiveVersion")

                val coroutinesVersion = "1.4.1"
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

                val serializationVersion = "1.0.1"
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

                api("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")

                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")

                implementation("com.github.aakira:napier:1.4.1")

                implementation("com.google.dagger:hilt-android:$hiltVersion")
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
                implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.2.0")
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
                implementation("com.google.dagger:hilt-android:$hiltVersion")
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
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
                configurations["kapt"].dependencies.add(DefaultExternalModuleDependency(
                        "com.google.dagger", "hilt-android-compiler", hiltVersion)
                )
            }
        }
        val iosTest by getting
        val jsMain by getting {
            dependencies {
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
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(30)
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
