buildscript {
    val kotlinVersion = "1.4.10"
    val sqlDelightVersion = "1.4.3"
    val hiltVersion = "2.28-alpha"
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.6.2.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://dl.bintray.com/badoo/maven")
    }
}
