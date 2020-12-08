import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class CodeStylePlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        if (this != rootProject) {
            throw IllegalStateException("This plugin can only be applied from root project")
        }

        val extension = extensions.create("codeStyle", CodeStylePluginExtension::class.java)

        val cloneUrl = extension.remoteURIProvider(remoteURL).toString()
        val cloneDir = file("$buildDir/codeStyle")
        val ideaCodeStyleDir = File(ideaDir, "codeStyles")
        val codeStyleSrcFile = File(cloneDir, extension.filePath)
        val codeStyleTargetFile = File(ideaCodeStyleDir, "Project.xml")
        val codeStyleConfigFile = File(ideaCodeStyleDir, "codeStyleConfig.xml")

        val cloneTask = task("cloneCodeStyleRepository") {
            group = TASK_GROUP
            description = "Git-clones the given code-style repository"

            inputs.property("cloneUri", cloneUrl)
            inputs.property("branch", extension.branch)
            outputs.dir(cloneDir)

            doFirst {
                println("Cloning styles from $cloneUrl, branch ${extension.branch}")

                cloneDir.deleteRecursively()
                rootDir.runCommand("git", "clone", "--depth=1", "--single-branch",
                        "--branch", extension.branch, cloneUrl, cloneDir.toRelativeString(rootDir))
                File(cloneDir, ".git").deleteRecursively()
            }
        }

        val codeStyleTask = task("configureWorkspaceCodeStyle") {
            group = TASK_GROUP
            description = "Generates the `$ideaCodeStyleDir` folder and its content"

            dependsOn(cloneTask)
            inputs.property("filePath", extension.filePath)
            inputs.dir(cloneDir)
            outputs.dir(ideaCodeStyleDir)

            doFirst {
                ideaCodeStyleDir.mkdirs()

                val styleContent = codeStyleSrcFile
                        .asDocument()
                        .documentElement
                        .apply { setAttribute("name", "Project") }

                codeStyleTargetFile.outputStream().use { os ->
                    "<component name=\"ProjectCodeStyleConfiguration\" />"
                            .asDocument()
                            .apply { documentElement.appendChild(importNode(styleContent, true)) }
                            .writeTo(os)
                }

                codeStyleConfigFile.writeText("""
                    <component name="ProjectCodeStyleConfiguration">
                        <state>
                            <option name="USE_PER_PROJECT_SETTINGS" value="true" />
                        </state>
                    </component>
                    """.trimIndent())
            }
        }

        if (ideaWorkspace.isFile) {
            registerActivationTask(codeStyleTask, ActivateOn.BEFORE_SYNC)
        }

    }

    companion object {

        private const val TASK_GROUP = "codeStyle"

    }

}
