package me.nicolas.adventofcode.utils

import java.io.File
import java.nio.file.Paths
import kotlin.time.DurationUnit
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

const val ANSI_RESET = "\u001B[0m"

fun red(any: Any) = "\u001B[31m$any$ANSI_RESET"
fun green(any: Any) = "\u001B[32m$any$ANSI_RESET"
fun yellow(any: Any) = "\u001B[33m$any$ANSI_RESET"
fun blue(any: Any) = "\u001B[34m$any$ANSI_RESET"

fun readFileDirectlyAsText(fileName: String): String {
    val path = Paths.get("src/main/resources/me/nicolas/adventofcode").toAbsolutePath().toString()
    return File("$path/$fileName").readText(Charsets.UTF_8)
}

fun prettyPrint(message: String, timedResponse: TimedValue<Any>) {
    println("$message ${blue(timedResponse.value)} (${green(timedResponse.duration.toDouble(DurationUnit.MILLISECONDS))} ms)")
}

fun prettyPrintPartOne(message: String? = null,lambda: () -> Any) {
    prettyPrint("Part one answer ${message ?: ""} :", measureTimedValue { lambda() })
}

fun prettyPrintPartTwo(message: String? = null,lambda: () -> Any) {
    prettyPrint("Part two answer ${message ?: ""} :", measureTimedValue { lambda() })
}

