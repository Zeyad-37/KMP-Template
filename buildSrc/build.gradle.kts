plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    google()
}

dependencies {
    val kotlinVersion = "1.4.20"
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("com.android.tools.build:gradle:4.1.1")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
    implementation("de.mannodermaus.gradle.plugins:android-junit5:1.6.2.0")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.28-alpha")
    api("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.15.0-RC1")
}
