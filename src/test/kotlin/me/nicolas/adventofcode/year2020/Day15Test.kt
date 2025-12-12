package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day15Test {

    private val day = Day15()

    @Test
    fun `part one - 1`() {
        assertEquals(1, day.partOne("1,3,2"))
    }

    @Test
    fun `part one - 2`() {
        assertEquals(10, day.partOne("2,1,3"))
    }

    @Test
    fun `part one - 3`() {
        assertEquals(27, day.partOne("1,2,3"))
    }

    @Test
    fun `part one - 4`() {
        assertEquals(78, day.partOne("2,3,1"))
    }

    @Test
    fun `part one - 5`() {
        assertEquals(438, day.partOne("3,2,1"))
    }

    @Test
    fun `part one - 6`() {
        assertEquals(1836, day.partOne("3,1,2"))
    }

    @Test
    fun `part two - 1`() {
        assertEquals(175594, day.partTwo("0,3,6"))
    }

    @Test
    fun `part two - 2`() {
        assertEquals(2578, day.partTwo("1,3,2"))
    }

    @Test
    fun `part two - 3`() {
        assertEquals(3544142, day.partTwo("2,1,3"))
    }

    @Test
    fun `part two - 4`() {
        assertEquals(261214, day.partTwo("1,2,3"))
    }

    @Test
    fun `part two - 5`() {
        assertEquals(6895259, day.partTwo("2,3,1"))
    }

    @Test
    fun `part two - 6`() {
        assertEquals(18, day.partTwo("3,2,1"))
    }

    @Test
    fun `part two - 7`() {
        assertEquals(362, day.partTwo("3,1,2"))
    }
}

