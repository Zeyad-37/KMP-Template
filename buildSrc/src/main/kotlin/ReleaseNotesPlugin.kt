import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.task

class ReleaseNotesPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val releaseNotesDir = rootProject.file("release_notes").apply { mkdirs() }

            task<GenerateReleaseNotesFileTask>("generateReleaseNotesFile") {
                group = TASK_GROUP
                description = "Generate release_notes.md based on release_notes folder"

                releaseNotes = fileTree(releaseNotesDir)
                releaseNotesFile = rootProject.file("release_notes.md")
            }

            task<Delete>("cleanReleaseNotes") {
                group = TASK_GROUP
                description = "Clean release_notes folder"

                delete(releaseNotesDir)
            }
        }
    }

    companion object {
        private const val TASK_GROUP = "releaseNotes"

    }
}
