package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day01Test {
    private val day = Day01(2021, 1)

    val test = """
        199
        200
        208
        210
        200
        207
        240
        269
        260
        263
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(7, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(5, day.partTwo(test))
    }
}
