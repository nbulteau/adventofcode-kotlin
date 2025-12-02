package me.nicolas.adventofcode.year2025

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day02Test {
    private val day = Day02(2025, 2)

    val test = """
        11-22,95-115,998-1012,1188511880-1188511890,222220-222224,
        1698522-1698528,446443-446449,38593856-38593862,565653-565659,
        824824821-824824827,2121212118-2121212124
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(1227775554L, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(4174379265L, day.partTwo(test))
    }
}
