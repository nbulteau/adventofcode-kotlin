package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day07Test {
    private val day = Day07(2024, 7, "Bridge Repair")
    
    val test = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(3749, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(11387, day.partTwo(test))
    }
}