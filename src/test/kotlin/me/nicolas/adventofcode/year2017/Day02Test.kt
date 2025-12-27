package me.nicolas.adventofcode.year2017

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day02Test {
    private val day = Day02()
    
    // empty input test (existing)
    val emptyInput = """ 
        
    """.trimIndent()

    // Example for part one (checksum)
    val samplePartOne = """
        5 1 9 5
        7 5 3
        2 4 6 8
    """.trimIndent()

    // Example for part two (divisible pair)
    val samplePartTwo = """
        5 9 2 8
        9 4 7 3
        3 8 6 5
    """.trimIndent()

    @Test
    fun emptyInput_partOne() {
        assertEquals(0, day.partOne(emptyInput))
    }

    @Test
    fun emptyInput_partTwo() {
        assertEquals(0, day.partTwo(emptyInput))
    }

    @Test
    fun partOne_example() {
        assertEquals(18, day.partOne(samplePartOne))
    }

    @Test
    fun partTwo_example() {
        assertEquals(9, day.partTwo(samplePartTwo))
    }
}