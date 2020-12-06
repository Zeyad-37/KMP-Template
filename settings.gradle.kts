pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "Remote1st"

include(":androidApp", ":splash", ":shared", ":design", ":shared_network", ":server", ":browser")
