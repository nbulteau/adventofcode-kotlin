package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day09Test {
    private val day = Day09(2024, 9)
    
    val test = """
        2333133121414131402
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(1928, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(2858, day.partTwo(test))
    }
}