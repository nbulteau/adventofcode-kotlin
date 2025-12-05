package me.nicolas.adventofcode.year2025

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day05Test {
    private val day = Day05(2025, 5)
    
    val test = """
        3-5
        10-14
        16-20
        12-18
        
        1
        5
        8
        11
        17
        32
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(3, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(14, day.partTwo(test))
    }
}