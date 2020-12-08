import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DomainObjectSet
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.configure

fun Project.taskAlias(name: String, target: Task) =
        taskAlias(name, target.path)

fun Project.taskAlias(name: String, target: String): TaskProvider<Task> =
        tasks.register(name) {
            val targetTask = tasks.getByPath(target)

            group = targetTask.group
            description = targetTask.description

            dependsOn(targetTask)
        }

internal fun Project.android(action: TestedExtension.() -> Unit) = configure(action)

val TestedExtension.variants: DomainObjectSet<out BaseVariant>
    get() = when (this) {
        is AppExtension -> applicationVariants
        is LibraryExtension -> libraryVariants
        else -> error("unsupported module type: $this")
    }
