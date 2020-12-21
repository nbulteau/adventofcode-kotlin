package me.nicolas.adventofcode

import java.io.File
import java.nio.file.Paths


fun readFileDirectlyAsText(fileName: String): String {
    val path = Paths.get("src/main/resources/me/nicolas/adventofcode").toAbsolutePath().toString()
    return File("$path/$fileName").readText(Charsets.UTF_8)
}

fun flipString(string: String) = string.reversed()
