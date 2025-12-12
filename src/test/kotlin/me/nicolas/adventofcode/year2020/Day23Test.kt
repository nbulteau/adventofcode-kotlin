package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day23Test {

    private val day = Day23(2020, 23, "Crab Cups")

    @Test
    fun `part one - 1`() {
        assertEquals(92658374L, day.partOne("389125467", 10))
    }

    @Test
    fun `part one - 2`() {
        assertEquals(67384529L, day.partOne("389125467", 100))
    }

    @Test
    fun `part two`() {
        assertEquals(149245887792L, day.partTwo(data = "389125467"))
    }
}

