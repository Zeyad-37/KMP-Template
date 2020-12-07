plugins {
    module
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":design"))
    implementation(project(":navigation"))

    val kotlinVersion = "1.4.20"
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1")

    implementation("com.google.android.material:material:1.2.1")
    val lifeCycleVersion = "2.2.0"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifeCycleVersion")
    implementation("androidx.fragment:fragment-ktx:1.2.5")
    implementation("androidx.activity:activity-ktx:1.1.0")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
}
