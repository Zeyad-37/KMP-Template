plugins {
    module
}

android {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":design"))
    implementation(project(":navigation"))

    implementation(Dependencies.Kotlin.stdlib)
    implementation(Dependencies.Kotlin.coroutinesAndroid)

    implementation(Dependencies.AndroidX.material)
    implementation(Dependencies.AndroidX.lifeCycleKTX)
    implementation(Dependencies.AndroidX.viewModelKTX)
    implementation(Dependencies.AndroidX.viewModelSavedState)
    implementation(Dependencies.AndroidX.fragmentKtx)
    implementation(Dependencies.AndroidX.activityKtx)
    implementation(Dependencies.AndroidX.coreKTX)
    implementation(Dependencies.AndroidX.constraintLayout)
}
