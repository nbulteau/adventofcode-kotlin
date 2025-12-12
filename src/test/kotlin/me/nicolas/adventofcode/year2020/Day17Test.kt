package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day17Test {

    private val data = """
        .#.
        ..#
        ###
    """.trimIndent()

    private val dayPOne = Day17PartOne()
    private val dayPTwo = Day17PartTwo()

    @Test
    fun partOne() {
        assertEquals(112, dayPOne.partOne(data))
    }

    @Test
    fun partTwo() {
        assertEquals(848, dayPTwo.partTwo(data))
    }
}