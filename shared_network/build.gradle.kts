import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
}

kotlin {
    jvm()
    val isDevice = System.getenv("SDK_NAME")?.startsWith("iphoneos") == true
    val ios: (String) -> KotlinNativeTarget = if (isDevice) ::iosArm64 else ::iosX64
    ios("ios") {
        binaries {
            framework {
                baseName = "shared_network"
            }
        }
    }
    js("js") {
        browser()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                val serializationVersion = "1.0.1"
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        val iosMain by getting {
            dependencies {
            }
        }
        val iosTest by getting {
            dependencies {
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
