package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day23Test {

    private val day = Day23("--- Day 23: Amphipod ---", "https://adventofcode.com/2021/day/23")

    private val dataPart1 = """
        #############
        #...........#
        ###B#C#B#D###
          #A#D#C#A#
          #########   
        """.trimIndent()

    private val dataPart2 = """
        #############
        #...........#
        ###B#C#B#D###
          #D#C#B#A#
          #D#B#A#C#
          #A#D#C#A#
          #########
        """.trimIndent()

    @Test
    fun `part one training`() {
        val lines = dataPart1.split("\n")

        assertEquals( 12521, day.partOne(lines))
    }

    @Test
    fun `part two training`() {
        val lines = dataPart2.split("\n")

        assertEquals( 44169, day.partTwo(lines))
    }
}