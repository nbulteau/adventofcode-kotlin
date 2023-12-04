package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day01Test {

    private val day = Day01("--- Day 1: Chronal Calibration ---", "https://adventofcode.com/2018/day/1")

    @Test
    fun `part one training`() {
        val data = """
            +1
            -2
            +3
            +1      
        """.trimIndent()
        val lines = data.split("\n")

        assertEquals( 3, day.partOne(lines))
    }

    @Test
    fun `part two training`() {
        val data = """    
            +1
            -2
            +3
            +1       
        """.trimIndent()
        val lines = data.split("\n")

        assertEquals( 2, day.partTwo(lines))
    }
}