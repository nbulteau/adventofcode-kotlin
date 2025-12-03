package me.nicolas.adventofcode.year2025

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day03Test {
    private val day = Day03(2025, 3)

    val test = """
    987654321111111
    811111111111119
    234234234234278
    818181911112111
""".trimIndent()

    @Test
    fun partOne() {
        assertEquals(357, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(3121910778619L, day.partTwo(test))
    }

}