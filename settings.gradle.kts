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
include(":shared_network")
include(":server")
include(":browser")
