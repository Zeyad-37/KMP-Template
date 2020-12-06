pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }

}
rootProject.name = "Remote1st"

include(":androidApp")
include(":splash")
include(":shared")
include(":design")
include(":navigation")
include(":shared_network")
include(":server")
include(":browser")
