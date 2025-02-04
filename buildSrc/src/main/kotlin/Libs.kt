import Versions.coroutinesVersion
import Versions.hiltVersion
import Versions.hiltXVersion
import Versions.junit5Version
import Versions.kotlinVersion
import Versions.ktorVersion
import Versions.lifeCycleVersion
import Versions.reaktiveVersion
import Versions.serializationVersion
import Versions.sqlDelightVersion

object Versions {
    const val kotlinVersion = "1.4.21"
    const val stetho = "1.5.1"
    const val lottie = "3.5.0"
    const val leakCanary = "2.0"
    const val facebookSdk = "8.1.0"
    const val instabug = "10.0.1"
    const val swipeRefresh = "1.1.0"

    //junit5
    const val junit5Version = "5.7.0"

    const val mparticle = "5.7.0"
    const val room = "5.7.0"

    const val ktorVersion = "1.4.3"
    const val sqlDelightVersion = "1.4.3"
    const val hiltVersion = "2.28-alpha"

    const val reaktiveVersion = "1.1.17"
    const val coroutinesVersion = "1.4.2"
    const val serializationVersion = "1.0.1"

    const val lifeCycleVersion = "2.2.0"

    const val hiltXVersion = "1.0.0-alpha02"
}

object Android {
    const val minSdk = 24
    const val compileSdk = 30
    const val targetSdk = compileSdk
    const val buildTools = "30.0.1"
}

object Libs {

    object Reaktive {
        const val reaktive = "com.badoo.reaktive:reaktive:$reaktiveVersion"
        const val reaktiveAnnotation = "com.badoo.reaktive:reaktive-annotations:$reaktiveVersion"
        const val reaktiveInterop = "com.badoo.reaktive:coroutines-interop:$reaktiveVersion"
    }

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
        const val coroutinesJS = "org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$coroutinesVersion"
        const val coroutinesNative = "org.jetbrains.kotlinx:kotlinx-coroutines-core-native:$coroutinesVersion"
        const val coroutinesNativeMetaData = "org.jetbrains.kotlinx:kotlinx-coroutines-core-metadata:nativeMain:$coroutinesVersion"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion"
    }

    object Ktor {
        const val core = "io.ktor:ktor-client-core:$ktorVersion"
        const val logging = "io.ktor:ktor-client-logging:$ktorVersion"
        const val clientSerialization = "io.ktor:ktor-client-serialization:$ktorVersion"
        const val android = "io.ktor:ktor-client-android:$ktorVersion"
        const val iOS = "io.ktor:ktor-client-ios:$ktorVersion"
        const val js = "io.ktor:ktor-client-js:$ktorVersion"
        const val jsCore = "io.ktor:ktor-client-core-js:$ktorVersion"
        const val jsJson = "io.ktor:ktor-client-json-js:$ktorVersion"
        const val jsSerialization = "io.ktor:ktor-client-serialization-js:$ktorVersion"
        const val jsLogging = "io.ktor:ktor-client-logging-js:$ktorVersion"
        const val serverNetty = "io.ktor:ktor-server-netty:$ktorVersion"
        const val serverSerialization = "io.ktor:ktor-serialization:$ktorVersion"
        const val serverTests = "io.ktor:ktor-server-tests:$ktorVersion"
    }

    object SqlDelight {
        const val runtime = "com.squareup.sqldelight:runtime:$sqlDelightVersion"
        const val android = "com.squareup.sqldelight:android-driver:$sqlDelightVersion"
        const val iOS = "com.squareup.sqldelight:native-driver:$sqlDelightVersion"
    }

    object Hilt {
        const val hilt = "com.google.dagger:hilt-android:$hiltVersion"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:$hiltVersion"
    }

    object AndroidX {
        const val viewModelKTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleVersion"
        const val viewModelSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifeCycleVersion"
        const val lifeCycleKTX = "androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycleVersion"
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val coreKTX = "androidx.core:core-ktx:1.3.2"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefresh}"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0"
        const val cardView = "androidx.cardview:cardview:1.0.0"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.2.5"
        const val activityKtx ="androidx.activity:activity-ktx:1.1.0"
        const val multidex = "androidx.multidex:multidex:2.0.1"
        const val material = "com.google.android.material:material:1.2.1"
        const val annotation = "androidx.annotation:annotation:$1.1.0"
        const val hiltLifecycleViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:$hiltXVersion"
        const val hiltCompiler = "androidx.hilt:hilt-compiler:$hiltXVersion"

//        const val browser = "androidx.browser:browser:${androidxBrowser}"
//        const val collectionKtx = "androidx.collection:collection-ktx:$androidxCollectionVersion"
//        const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:$androidxLifecycleVersion"
//        const val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:$androidxLifecycleVersion"
//        const val lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
    }

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:26.1.1"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    }

    object Logging {
        const val napier = "com.github.aakira:napier:1.4.1"
    }

    object Analytics {
        const val mparticleCore = "com.mparticle:android-core:${Versions.mparticle}"
        const val mparticleAdjust = "com.mparticle:android-adjust-kit:${Versions.mparticle}"
        const val mparticleAppboy = "com.mparticle:android-appboy-kit:${Versions.mparticle}"
    }

    object DataDog {
//        const val datadogFacade = "com.glovo.datadog:glovo-facade:${Versions.glovoDataDog}"
//        const val datadogCompiler = "com.glovo.datadog:glovo-compiler:${Versions.glovoDataDog}"
    }

    object Testing {

        val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"
        val uiAutomator = "com.android.support.test.uiautomator:uiautomator-v18:2.1.3"
        val wiremock = "com.github.tomakehurst:wiremock:2.25.1"
        val barista = "com.schibsted.spain:barista:3.6.0"

        object Android {
            val core = "androidx.test:core:1.3.0"
            val junit = "androidx.test.ext:junit:1.1.1"
            val junit_ktx = "androidx.test.ext:junit-ktx:1.1.1"
            val espresso_core = "androidx.test.espresso:espresso-core:3.2.0"
            val runner = "androidx.test:runner:1.2.0"
            val rules = "androidx.test:rules:1.2.0"
            val fragment_testing = "androidx.fragment:fragment-testing:1.2.5"
            val core_testing = "androidx.arch.core:core-testing:2.1.0"
        }

        object Robolectric {
            val robolectric = "org.robolectric:robolectric:4.4"
            val shadowsMultidex = "org.robolectric:shadows-multidex:4.3.1"
        }

        object Mocking {
            val core = "org.mockito:mockito-core:3.6.28"
            val android = "org.mockito:mockito-android:3.6.28"
            val nhaarman = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"
            val mockk = "io.mockk:mockk:1.10.3"
        }

        object JUnit5 {
            val api = "org.junit.jupiter:junit-jupiter-api:$junit5Version"
            val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$junit5Version"
            val vintageEngine = "org.junit.vintage:junit-vintage-engine:$junit5Version"
            val migrationSupport = "org.junit.jupiter:junit-jupiter-migrationsupport:$junit5Version"
            val params = "org.junit.jupiter:junit-jupiter-params:$junit5Version"
            val androidTestRunner = "de.mannodermaus.junit5:android-test-runner:1.2.0"
        }
    }

    object Other {

        const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"
        const val instabug = "com.instabug.library:instabug:${Versions.instabug}"
        const val facebookSdk = "com.facebook.android:facebook-android-sdk:${Versions.facebookSdk}"
        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
        const val napier = "com.github.aakira:napier:1.4.1"
    }

//    object Payment {
//        const val processout = "com.github.processout:processout-android:${Versions.processout}"
//        const val adyen3ds2 = "com.adyen.threeds:adyen-3ds2:${Versions.adyen3ds2}"
//        const val ravelinCore = "com.ravelin.sdk:ravelin-core:${Versions.ravelin}"
//    }
}