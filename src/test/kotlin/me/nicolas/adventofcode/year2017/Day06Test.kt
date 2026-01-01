package me.nicolas.adventofcode.year2017

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day06Test {
    private val day = Day06()
    
    val test = """
        0 2 7 0
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(5, day.partOne(test))
    }

    @Test
    fun partTwo() {
        // The example from Advent of Code: the loop size for "0 2 7 0" is 4
        assertEquals(4, day.partTwo(test))
    }
}