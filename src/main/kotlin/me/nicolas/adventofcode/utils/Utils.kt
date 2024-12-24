package me.nicolas.adventofcode.utils

import java.io.File
import java.nio.file.Paths
import kotlin.time.DurationUnit
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

const val ANSI_RESET = "\u001B[0m"

// Color the output in the console with ANSI escape codes (https://en.wikipedia.org/wiki/ANSI_escape_code)
fun red(any: Any) = "\u001B[31m$any$ANSI_RESET"
fun green(any: Any) = "\u001B[32m$any$ANSI_RESET"
fun yellow(any: Any) = "\u001B[33m$any$ANSI_RESET"
fun blue(any: Any) = "\u001B[34m$any$ANSI_RESET"

/** Read a file directly as text from the resources folder (without using the java class loader) */
fun readFileDirectlyAsText(fileName: String): String {
    val path = Paths.get("src/main/resources/me/nicolas/adventofcode").toAbsolutePath().toString()
    return File("$path/$fileName").readText(Charsets.UTF_8)
}

fun writeFileDirectlyAsText(fileName: String, content: String) {
    val path = Paths.get("src/main/resources/me/nicolas/adventofcode").toAbsolutePath().toString()
    File("$path/$fileName").writeText(content)
}

/** pretty print the result of a function with the time it took to execute */
fun prettyPrint(message: String, timedResponse: TimedValue<Any>) {
    println("$message ${blue(timedResponse.value)} (${green(timedResponse.duration.toDouble(DurationUnit.MILLISECONDS))} ms)")
}

/** pretty print the result of part one with the time it took to execute */
fun prettyPrintPartOne(message: String? = null,lambda: () -> Any) {
    prettyPrint("Part one answer ${message ?: ""} :", measureTimedValue { lambda() })
}

/** pretty print the result of part two with the time it took to execute */
fun prettyPrintPartTwo(message: String? = null,lambda: () -> Any) {
    prettyPrint("Part two answer ${message ?: ""} :", measureTimedValue { lambda() })
}

fun <T> Iterable<T>.combinations(length: Int): Sequence<List<T>> {
    val pool = this.toList()
    val n = pool.size
    if (length > n) return emptySequence()
    val indices = IntArray(length) { it }
    return sequence {
        while (true) {
            yield(indices.map { pool[it] })
            var i = length
            do {
                i--
                if (i == -1) return@sequence
            } while (indices[i] == i + n - length)
            indices[i]++
            for (j in i + 1 until length) indices[j] = indices[j - 1] + 1
        }
    }
}

