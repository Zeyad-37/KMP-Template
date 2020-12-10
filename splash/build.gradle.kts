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

    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Kotlin.coroutinesAndroid)

    implementation(Libs.AndroidX.material)
    implementation(Libs.AndroidX.lifeCycleKTX)
    implementation(Libs.AndroidX.viewModelKTX)
    implementation(Libs.AndroidX.viewModelSavedState)
    implementation(Libs.AndroidX.fragmentKtx)
    implementation(Libs.AndroidX.activityKtx)
    implementation(Libs.AndroidX.coreKTX)
    implementation(Libs.AndroidX.constraintLayout)
}
