package me.nicolas.adventofcode.year2017

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day03Test {
    private val day = Day03()

    @Test
    fun partOne_examples() {
        // Examples from the problem statement
        assertEquals(0, day.partOne("1"))      // center
        assertEquals(3, day.partOne("12"))     // example: 12 -> 3
        assertEquals(2, day.partOne("23"))     // example: 23 -> 2
        assertEquals(31, day.partOne("1024"))  // example: 1024 -> 31
    }

    @Test
    fun partTwo_examples() {
        // Known spiral-sum progression examples
        assertEquals(2, day.partTwo("1"))   // first value > 1 is 2
        assertEquals(4, day.partTwo("2"))   // first value > 2 is 4
        assertEquals(10, day.partTwo("5"))  // first value > 5 is 10
        // larger example from problem description: first value > 747 is 806
        assertEquals(806, day.partTwo("747"))
    }
}