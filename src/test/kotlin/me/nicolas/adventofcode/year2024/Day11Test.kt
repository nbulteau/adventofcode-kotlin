package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day11Test {
    private val day = Day11(2024, 11)
    
    val test = """
        125 17
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(22, day.partOne(test, 6))
    }

    @Test
    fun partTwo() {
        assertEquals(55312, day.partTwo(test, 25))
    }
}