import java.net.URI

@Suppress("LeakingThis")
open class CodeStylePluginExtension {

    var remoteURIProvider: (projectURI: URI) -> URI = { it.resolve("android-guidelines") }

    var branch = "master"

    var filePath = "guidelines/res/Glovo-Android-Scheme.xml"

}
