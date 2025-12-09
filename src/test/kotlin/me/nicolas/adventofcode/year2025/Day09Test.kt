package me.nicolas.adventofcode.year2025

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day09Test {
    private val day = Day09(2025, 9)
    
    val test = """
        7,1
        11,1
        11,7
        9,7
        9,5
        2,5
        2,3
        7,3
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(50L, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(24L, day.partTwo(test))
    }
}