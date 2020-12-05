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
include(":shared")
include(":server")
include(":browser")
