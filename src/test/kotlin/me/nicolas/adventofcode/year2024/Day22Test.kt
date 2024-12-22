package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day22Test {
    private val day = Day22(2024, 22)
    
    val test1 = """
        1
        10
        100
        2024
    """.trimIndent()

    val test2 = """
        123
    """.trimIndent()

    val test3 = """
        1
        2
        3
        2024
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(37327623, day.partOne(test1))
    }

    @Test
    fun partTwo() {
        assertEquals(23, day.partTwo(test3))
    }
}