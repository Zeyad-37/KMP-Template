plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
    kotlin("kapt")
    id("de.mannodermaus.android-junit5")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
    maven(url = "https://dl.bintray.com/badoo/maven")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.remote_first.androidApp"
        minSdkVersion(24)
        targetSdkVersion(30)
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
    implementation(project(":shared"))

    implementation(platform("com.google.firebase:firebase-bom:26.1.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    val kotlinVersion = "1.4.10"
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1")

    implementation("com.google.android.material:material:1.2.1")

    val lifeCycleVersion = "2.2.0"
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifeCycleVersion")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.annotation:annotation:1.1.0")
    implementation("androidx.fragment:fragment-ktx:1.2.5")
    implementation("androidx.activity:activity-ktx:1.1.0")
    implementation("androidx.room:room-runtime:2.2.5")

    val hiltVersion = "2.28-alpha"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    val hiltXVersion = "1.0.0-alpha02"
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:$hiltXVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltXVersion")


    val reaktiveVersion = "1.1.17"
    implementation("com.badoo.reaktive:reaktive:$reaktiveVersion")
    implementation("com.badoo.reaktive:reaktive-annotations:$reaktiveVersion")
    implementation("com.badoo.reaktive:coroutines-interop:$reaktiveVersion")

    implementation("io.github.reactivecircus.flowbinding:flowbinding-material:1.0.0-beta02")

//    val flaggerVersion = "1.0.9"
//    implementation("com.glovoapp.featureflagger:library:$flaggerVersion")
//    kapt("com.glovoapp.featureflagger:compiler:$flaggerVersion")

    implementation("io.coil-kt:coil:1.0.0-rc3")

    val junit5Version = "5.6.2"
    androidTestRuntimeOnly("de.mannodermaus.junit5:android-test-runner:1.2.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit5Version")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junit5Version")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit5Version")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junit5Version")
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableTransformForLocalTests = true
}
