package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day14Test {
    private val day = Day14(2018, 14, "Mine Cart Madness")

    @Test
    fun `part one training one`() {
        assertEquals("5158916779", day.partOne(9))
    }

    @Test
    fun `part one training two`() {
        assertEquals("0124515891", day.partOne(5))
    }

    @Test
    fun `part one training three`() {
        assertEquals("9251071085", day.partOne(18))
    }

    @Test
    fun `part one training four`() {
        assertEquals("5941429882", day.partOne(2018))
    }
}