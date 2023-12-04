package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day05Test {

    private val day = Day05("--- Day 5 :  ---", "https://adventofcode.com/2023/day/5")
    private val data = """
    """.trimIndent()
    @Test
    fun `part one training`() {
        val lines = data.split("\n")

        assertEquals( 13, day.partOne(lines))
    }

    @Test
    fun `part two training`() {
        val lines = data.split("\n")

        assertEquals( 30, day.partTwo(lines))
    }
}