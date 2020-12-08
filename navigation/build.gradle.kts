plugins {
    module
}

android {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(Dependencies.Kotlin.stdlib)
    implementation(Dependencies.AndroidX.coreKTX)
    implementation(Dependencies.AndroidX.appcompat)
}
