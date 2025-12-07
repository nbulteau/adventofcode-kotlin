package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day09Test {
    private val day = Day09(2021, 9, "Smoke Basin")

    val test = """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678
    """.trimIndent()

    @Test
    fun partOne() {
        val inputs = test.split("\n").map { line -> line.toList().map { it.toString().toInt() } }
        assertEquals(15, day.partOne(inputs))
    }

    @Test
    fun partTwo() {
        val inputs = test.split("\n").map { line -> line.toList().map { it.toString().toInt() } }
        assertEquals(1134, day.partTwo(inputs))
    }
}
