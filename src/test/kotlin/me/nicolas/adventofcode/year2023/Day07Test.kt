package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day07Test {

    private val day = Day07("--- Day 7: Camel Cards ---", "https://adventofcode.com/2023/day/7")
    private val data = """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """.trimIndent()
    @Test
    fun `part one training`() {
        assertEquals( 6440, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        assertEquals( 5905, day.partTwo(data))
    }
}