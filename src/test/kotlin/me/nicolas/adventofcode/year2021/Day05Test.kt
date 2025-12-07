package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day05Test {
    private val day = Day05(2021, 5, "Hydrothermal Venture")

    val test = """
        0,9 -> 5,9
        8,0 -> 0,8
        9,4 -> 3,4
        2,2 -> 2,1
        7,0 -> 7,4
        6,4 -> 2,0
        0,9 -> 2,9
        3,4 -> 1,4
        0,0 -> 8,8
        5,5 -> 8,2
    """.trimIndent()

    @Test
    fun partOne() {
        val lines = day.parseLines(test.split("\n"))
        assertEquals(5, day.partOne(10, lines))
    }

    @Test
    fun partTwo() {
        val lines = day.parseLines(test.split("\n"))
        assertEquals(12, day.partTwo(10, lines))
    }
}
