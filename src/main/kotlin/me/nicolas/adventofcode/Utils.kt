package me.nicolas.adventofcode

import java.io.File
import java.nio.file.Paths

typealias Grid = Array<CharArray>

fun readFileDirectlyAsText(fileName: String): String {
    val path = Paths.get("src/main/resources/me/nicolas/adventofcode").toAbsolutePath().toString()
    return File("$path/$fileName").readText(Charsets.UTF_8)
}

fun Grid.rotateClockwise(): Grid {
    val arrayOfCharArrays = Array(this.size) { CharArray(this[0].size) }

    for (row in arrayOfCharArrays.indices) {
        for (column in arrayOfCharArrays[0].indices) {
            arrayOfCharArrays[row][column] = this[this.size - 1 - column][row]
        }
    }
    return arrayOfCharArrays
}

fun Grid.flipHorizontal(): Grid {
    val arrayOfCharArrays = Array(this.size) { CharArray(this[0].size) }

    for (row in arrayOfCharArrays.indices) {
        for (column in arrayOfCharArrays[0].indices) {
            arrayOfCharArrays[row][column] = this[row][this[0].size - 1 - column]
        }
    }
    return arrayOfCharArrays
}