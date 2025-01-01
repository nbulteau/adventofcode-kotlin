package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day17Test {
    private val day = Day17(2015, 17)
    
    val test = """
        20
        15
        10
        5
        5
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(4, day.partOne(test, target = 25))
    }

    @Test
    fun partTwo() {
        assertEquals(3, day.partTwo(test, target = 25))
    }
}