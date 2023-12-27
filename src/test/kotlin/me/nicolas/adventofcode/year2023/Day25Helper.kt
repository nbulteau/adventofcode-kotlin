package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.io.File
import java.util.concurrent.TimeUnit

class Day25Helper {
    fun calc(graph: List<Pair<String, List<String>>>) {
        var dot = "graph G {\n"
        for ((from, to) in graph) {
            dot += "  $from"
            if (to.isNotEmpty()) dot += " -- ${to.joinToString(",")}"
            dot += "\n"
        }
        dot += "}"
        File("src/main/resources/me/nicolas/adventofcode/year2023/day25.dot").writeText(dot)
        val process = ProcessBuilder("dot", "-Tsvg", "day25.dot", "-o", "data.svg").start()
        process.waitFor(5, TimeUnit.SECONDS)
    }

    fun parse(data: String): List<Pair<String, List<String>>> {
        return data.lines().map { line ->
            val parts = line.split(": ")
            parts[0] to parts[1].split(" ").map { it.replace(",", "") }
        }
    }
}

fun main() {
    val data = readFileDirectlyAsText("/year2023/day25/data.txt")
    val graph = Day25Helper().parse(data)
    Day25Helper().calc(graph)
}