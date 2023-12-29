package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day09Test {


    private val day = Day09(2018, 9, "Marble Mania")

    @Test
    fun `part one training one`() {
        val data = """
            9 players; last marble is worth 25 points
        """.trimIndent()
        assertEquals(32, day.partOne(data ))
    }

    @Test
    fun `part one training two`() {
        val data = """
            10 players; last marble is worth 1618 points
        """.trimIndent()
        assertEquals(8317, day.partOne(data ))
    }

    @Test
    fun `part one training three`() {
        val data = """
            13 players; last marble is worth 7999 points
        """.trimIndent()
        assertEquals(146373, day.partOne(data ))
    }


    @Test
    fun `part one training four`() {
        val data = """
            17 players; last marble is worth 1104 points
        """.trimIndent()
        assertEquals(2764, day.partOne(data ))
    }

    @Test
    fun `part one training five`() {
        val data = """
            21 players; last marble is worth 6111 points
        """.trimIndent()
        assertEquals(54718, day.partOne(data ))
    }


    @Test
    fun `part one training six`() {
        val data = """
            30 players; last marble is worth 5807 points
        """.trimIndent()
        assertEquals(37305, day.partOne(data ))
    }
}