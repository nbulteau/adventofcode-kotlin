package me.nicolas.adventofcode.year2025

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day06Test {
    private val day = Day06(2025, 6)
    
    val test = """
123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   +  
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(4277556, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(3263827, day.partTwo(test))
    }
}