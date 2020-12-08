import org.gradle.api.tasks.util.PatternFilterable
import org.gradle.api.tasks.util.PatternSet

@Suppress("LeakingThis")
open class CoveragePluginExtension : PatternFilterable by PatternSet() {

    init {
        exclude("android/**/*.class")
        exclude("**/R.class")
        exclude("**/R$*.class")
        exclude("**/BuildConfig.class")
        exclude("**/Manifest*.class")

        exclude("**/*Module.class") // dagger modules
        exclude("**/*Module_*.class") // dagger modules providers
        exclude("**/*Module\$Companion.class") // dagger modules's companion
        exclude("**/*Module_Companion_*.class") // dagger modules's companion providers
        exclude("**/Dagger*Component.class") // covers component implementations
        exclude("**/Dagger*Component\$Builder.class") // covers component builders
        exclude("**/*_MembersInjector.class") // dagger injectors generated code
        exclude("**/*_Factory.class") // dagger providers generated code
        exclude("**/*_Factory\$InstanceHolder.class") // dagger providers generated code
    }
}
