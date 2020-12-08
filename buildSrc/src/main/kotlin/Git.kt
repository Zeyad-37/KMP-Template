import org.gradle.api.Project
import java.net.URI
import java.net.URISyntaxException
import java.util.concurrent.ExecutionException

val Project.gitRoot
    get() = projectDir command "git rev-parse --show-toplevel"

val Project.branch
    get() = projectDir command "git rev-parse --abbrev-ref HEAD"

val Project.commitSha
    get() = projectDir command "git rev-parse --short=6 HEAD"

val Project.commitsCount: Int
    get() = (projectDir command "git rev-list HEAD --count").toInt()

val Project.gitTag
    get() = try {
        projectDir command "git describe --tags --exact-match"

    } catch (_: ExecutionException) {
        null
    }

val Project.gitTagOrCommit
    get() = projectDir command "git describe --tags --always"

val Project.remoteURL: URI
    get() = (projectDir command "git remote get-url origin").let {
        try {
            URI(it)

        } catch (e: URISyntaxException) {
            try {
                URI.create("ssh://${it.replaceFirst(":", "/")}")

            } catch (_: URISyntaxException) {
                throw e
            }
        }
    }

private val gitCommitterRegex by lazy { "^\\s*(\\d+)\\s+(.*?)\\s+<(.*?)>\\s*$".toRegex() }

val Project.committers: List<GitCommitter>
    get() = (projectDir command "git shortlog -sne HEAD")
            .lineSequence()
            .mapNotNull { gitCommitterRegex.matchEntire(it)?.destructured }
            .map { (commits, name, email) -> GitCommitter(email = email, name = name, commits = commits.toInt()) }
            .sortedByDescending { it.commits }
            .toList()

data class GitCommitter(val email: String, val name: String, val commits: Int)
