package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day03Test {
    private val day = Day03(2021, 3, "Binary Diagnostic")

    val test = """
        00100
        11110
        10110
        10111
        10101
        01111
        00111
        11100
        10000
        11001
        00010
        01010
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(198, day.partOne(test.split("\n")))
    }

    @Test
    fun partTwo() {
        assertEquals(230, day.partTwo(test.split("\n")))
    }
}
