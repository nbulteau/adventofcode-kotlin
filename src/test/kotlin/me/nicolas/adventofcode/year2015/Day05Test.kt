package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day05Test {

    val day = Day05(2015, 5, "Perfectly Spherical Houses in a Vacuum")

    @Test
    fun `part one with no vowels`() {
        val data = "bcdfg"
        assertEquals(0, day.partOne(data))
    }

    @Test
    fun `part one with forbidden strings`() {
        val data = "abcdpqxy"
        assertEquals(0, day.partOne(data))
    }

    @Test
    fun `part one with double letters and enough vowels`() {
        val data = "ugknbfddgicrmopn"
        assertEquals(1, day.partOne(data))
    }

    @Test
    fun `part two with no repeating pairs`() {
        val data = "abcdefg"
        assertEquals(0, day.partTwo(data))
    }

    @Test
    fun `part two with repeating pairs and one letter between`() {
        val data = "xyxyx"
        assertEquals(1, day.partTwo(data))
    }

    @Test
    fun `part two with overlapping pairs`() {
        val data = "aaa"
        assertEquals(0, day.partTwo(data))
    }

    @Test
    fun `part two with multiple valid strings`() {
        val data = """
        qjhvhtzxzqqjkmpb
        xxyxx
        uurcxstgmygtbstg
        ieodomkazucvgmuy
        """.trimIndent()
        assertEquals(2, day.partTwo(data))
    }
}