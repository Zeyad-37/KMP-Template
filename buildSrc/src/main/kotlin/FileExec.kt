/*
 * Based on https://stackoverflow.com/a/54620293
 */

import java.io.File
import java.io.InputStream
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

fun InputStream.readText() =
        bufferedReader().readText().trimEnd()

infix fun File.command(command: String) =
        runCommand(*command.split("\\s+".toRegex()).toTypedArray())

fun File.runCommand(vararg arguments: String,
                    timeoutAmount: Long = 60,
                    timeoutUnit: TimeUnit = TimeUnit.SECONDS) = ProcessBuilder(*arguments)
        .directory(this)
        .start()
        .run {
            waitFor(timeoutAmount, timeoutUnit)

            if (exitValue() == 0) inputStream.readText()
            else throw ExecutionException(errorStream.readText(), null)
        }
