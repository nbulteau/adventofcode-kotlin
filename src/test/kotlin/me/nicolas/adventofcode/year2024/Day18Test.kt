package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day18Test {
    private val day = Day18(2024, 18, "RAM Run", gridSize = 7, nbBytes = 12)
    
    val test = """
        5,4
        4,2
        4,5
        3,0
        2,1
        6,3
        2,4
        1,5
        0,6
        3,3
        2,6
        5,1
        1,2
        5,5
        2,5
        6,5
        1,4
        0,4
        6,4
        1,1
        6,1
        1,0
        0,5
        1,6
        2,0
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(22, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals("6,1", day.partTwo(test))
    }
}