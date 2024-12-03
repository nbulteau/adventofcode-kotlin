package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day01Test {
    private val day = Day01(2024, 1, "Historian Hysteria")

    val test1 = """
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(11, day.partOne(test1))
    }

    @Test
    fun partTwo() {
        assertEquals(31, day.partTwo(test1))
    }
}