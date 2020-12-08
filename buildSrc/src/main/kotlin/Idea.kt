import org.gradle.api.Project
import org.gradle.api.Task
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File

val Project.ideaDir: File
    get() = rootProject.file(".idea")

val Project.ideaWorkspace: File
    get() = File(ideaDir, "workspace.xml")

enum class ActivateOn(internal val tag: String) {
    BEFORE_SYNC("before_sync"),
    BEFORE_COMPILE("before_compile"),
    BEFORE_REBUILD("before_rebuild"),
    AFTER_SYNC("after_sync"),
    AFTER_COMPILE("after_compile"),
    AFTER_REBUILD("after_rebuild")
}

fun Project.registerActivationTask(task: Task, type: ActivateOn) =
        registerActivationTask(task.name, type)

fun Project.registerActivationTask(taskName: String, type: ActivateOn) {
    var changed = false

    fun Node.findOrCreate(tag: String,
                          selector: String = "",
                          onCreate: Element.() -> Unit = {}) = evaluate(tag + selector)
            .firstOrNull()
            ?: appendChild(ownerDocument.createElement(tag).apply(onCreate)).also { changed = true }

    ideaWorkspace.asDocument()
            .findOrCreate("project")
            .findOrCreate("component", "[@name='ExternalProjectsManager']") { setAttribute("name", "ExternalProjectsManager") }
            .findOrCreate("system", "[@id='GRADLE']") { setAttribute("id", "GRADLE") }
            .findOrCreate("state")
            .findOrCreate("task", "[@path='\$PROJECT_DIR\$']") { setAttribute("path", "\$PROJECT_DIR\$") }
            .findOrCreate("activation")
            .findOrCreate(type.tag)
            .findOrCreate("task", "[@name='$taskName']") { setAttribute("name", taskName) }
            .also { doc ->
                if (changed) {
                    ideaWorkspace.outputStream().use {
                        doc.ownerDocument.writeTo(it)
                    }
                }
            }
}
