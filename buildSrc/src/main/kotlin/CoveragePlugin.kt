import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import groovy.lang.Closure
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.task
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

class CoveragePlugin : Plugin<Project> {

    companion object {
        const val TASK_GROUP = "Reporting"
    }

    override fun apply(project: Project) = with(project) {
        val extension = extensions.create("coverage", CoveragePluginExtension::class.java)

        apply<JacocoPlugin>()

        taskAlias("jacocoTestReport", "jacocoDebugUnitTestReport")

        android {

            @Suppress("UNCHECKED_CAST")
            testOptions.unitTests.all(closureOf<Test> {
                configure<JacocoTaskExtension> {
                    isIncludeNoLocationClasses = true
                    excludes = listOf("jdk.internal.*")
                }
            } as Closure<Test>)

            unitTestVariants.all {
                val capitalizedName = name.capitalize()
                val variant = testedVariant as BaseVariant

                task<JacocoReport>("jacoco${capitalizedName}Report") {
                    dependsOn("test$capitalizedName")

                    group = TASK_GROUP
                    description = "Generates Jacoco unit tests coverage reports on the ${variant.name.capitalize()} build"

                    reports {
                        xml.isEnabled = true
                        html.isEnabled = true
                    }

                    variant.sourceSets.forEach {
                        sourceDirectories.from(it.javaDirectories)
                    }

                    fun filteredFileTree(source: Any) = fileTree(source)
                            .include(extension.includes)
                            .exclude(extension.excludes)

                    classDirectories.apply {
                        from(filteredFileTree(variant.javaCompileProvider.get().destinationDir))
                        from(filteredFileTree("$buildDir/tmp/kotlin-classes/${variant.name}"))
                    }

                    executionData.from(fileTree(buildDir) {
                        include("jacoco/test$capitalizedName.exec")
                    })

                    doLast {
                        println("Wrote HTML coverage report to ${reports.html.destination.toURI()}/index.html")
                        println("Wrote XML coverage report to ${reports.xml.destination.toURI()}")
                    }
                }
            }

            android {
                if (this is LibraryExtension) {

                    // for variants with flavors, provide a helper "jacoco${buildType}UnitTestReport" task
                    unitTestVariants.all {
                        if (productFlavors.isNotEmpty()) {
                            tasks.maybeCreate("jacoco${buildType.name.capitalize()}UnitTestReport").apply {
                                group = TASK_GROUP
                                description = "Generates Jacoco unit tests coverage reports for all variants of ${buildType.name} build type"

                                dependsOn("jacoco${this@all.name.capitalize()}Report")
                            }
                        }
                    }

                }
            }
        }
    }
}
