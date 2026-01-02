package me.nicolas.adventofcode.year2017

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day10Test {
    private val day = Day10()
    
    val test = """
        3, 4, 1, 5
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(12, day.partOne(test, 5))
    }

    @Test
    fun partTwo() {
        // Use the official examples for part two: empty string -> a2582a3a...
        assertEquals("a2582a3a0e66e6e86e3812dcb672a272", day.partTwo(""))
        assertEquals("33efeb34ea91902bb2f59c9920caa6cd", day.partTwo("AoC 2017"))
        assertEquals("3efbe78a8d82f29979031a4aa0b16a9d", day.partTwo("1,2,3"))
        assertEquals("63960835bcdc130f0b66d7ff4f6a5a8e", day.partTwo("1,2,4"))
    }
}