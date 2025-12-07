package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12Test {
    private val day = Day12(2021, 12, "Passage Pathing")

    val test = """
        start-A
        start-b
        A-c
        A-b
        b-d
        A-end
        b-end
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(10, day.partOne(test.split("\n")))
    }

    @Test
    fun partTwo() {
        assertEquals(36, day.partTwo(test.split("\n")))
    }
}
