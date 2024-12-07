package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day02Test {
    private val day = Day02(2024, 2, "Red-Nosed Reports")

    val test = """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """.trimIndent()

    @Test
    fun partOne() {

        assertEquals(2, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(4, day.partTwo(test))
    }
}