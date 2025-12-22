package me.nicolas.adventofcode.year2019

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day24Test {
    private val day = Day24(2019, 24)
    
    // Example from AoC 2019 Day 24
    private val example = """
        ....#
        #..#.
        #..##
        ..#..
        #....
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(2129920, day.partOne(example))
    }

    @Test
    fun partTwo() {
        // After 10 minutes, the example has 99 bugs in total (recursive simulation)
        assertEquals(99, day.partTwo(example, 10))
    }
}