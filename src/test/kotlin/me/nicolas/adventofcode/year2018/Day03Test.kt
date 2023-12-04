package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day03Test {
    private val day = Day03("--- Day 3: No Matter How You Slice It ---", "https://adventofcode.com/2018/day/3")

    @Test
    fun `part one training`() {
        val data = """
            #1 @ 1,3: 4x4
            #2 @ 3,1: 4x4
            #3 @ 5,5: 2x2
        """.trimIndent()
        val lines = data.split("\n")

        assertEquals( 4, day.partOne(lines))
    }

    @Test
    fun `part two training`() {
        val data = """
            #1 @ 1,3: 4x4
            #2 @ 3,1: 4x4
            #3 @ 5,5: 2x2
        """.trimIndent()
        val lines = data.split("\n")

        assertEquals( 3, day.partTwo(lines))
    }
}