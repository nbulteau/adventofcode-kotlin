package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day11Test {
    private val day = Day11(2018, 11, "Chronal Charge")

    @Test
    fun `part one training one`() {
        assertEquals("33,45", day.partOne(18))
    }

    @Test
    fun `part one training two`() {
        assertEquals("21,61", day.partOne(42))
    }
}