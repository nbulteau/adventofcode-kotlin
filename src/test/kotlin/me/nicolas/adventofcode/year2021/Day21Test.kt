package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day21Test {
    private val day = Day21(2021, 21, "Dirac Dice")

    val test = """
        Player 1 starting position: 4
        Player 2 starting position: 8
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(739785, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(444356092776315UL, day.partTwo(test))
    }
}
