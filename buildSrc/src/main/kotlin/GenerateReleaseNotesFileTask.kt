import org.gradle.api.DefaultTask
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

@Suppress("UnstableApiUsage")
open class GenerateReleaseNotesFileTask : DefaultTask() {

    @InputFiles
    lateinit var releaseNotes: FileTree

    @OutputFile
    lateinit var releaseNotesFile: File

    @TaskAction
    fun run() {
        releaseNotesFile
                .writeText(RELEASE_NOTES_HEADER + releaseNotes
                        .toTrimmedList()
                        .sortedBy { line -> PRIORITIES.indexOfFirst { regex -> regex.matches(line) } }
                        .joinToString("\n")
                )
    }

    private fun FileTree.toTrimmedList() = flatMap { file ->
        file.readLines()
                .filterNot { it.isBlank() }
                .map { line ->
                    line.trim().let { if (it.startsWith("- ")) it else "- $it" }
                }
    }

    companion object {
        internal const val RELEASE_NOTES_HEADER = "## Release notes\n\n"
        private val PRIORITIES = listOf(
                ".*\\[(CAP|GROWTH|CX)(:Feat)?].*".toRegex(RegexOption.IGNORE_CASE), // Features     [CaP] or [CaP:Feat]
                ".*\\[(CAP|GROWTH|CX):Fix].*".toRegex(RegexOption.IGNORE_CASE),     // Team Fixes   [CaP:Fix]
                ".*\\[TECH:FIX].*".toRegex(RegexOption.IGNORE_CASE),                // Tech Fixes   [Tech:Fix]
                ".*\\[TECH(:(CAP|GROWTH|CX))?].*".toRegex(RegexOption.IGNORE_CASE), // Tech         [Tech] or [Tech:CaP]
                ".*".toRegex()                                                      // Everything else
        )
    }

}
