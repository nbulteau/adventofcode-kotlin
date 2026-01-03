package me.nicolas.adventofcode.year2017

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day11Test {
    private val day = Day11()

    @Test
    fun partOne() {
        assertEquals(3, day.partOne("ne,ne,ne"))
        assertEquals(0, day.partOne("ne,ne,sw,sw"))
        assertEquals(2, day.partOne("ne,ne,s,s"))
        assertEquals(3, day.partOne("se,sw,se,sw,sw"))
    }

    @Test
    fun partTwo() {
        assertEquals(3, day.partTwo("ne,ne,ne"))
        assertEquals(2, day.partTwo("ne,ne,sw,sw"))
        assertEquals(2, day.partTwo("ne,ne,s,s"))
        assertEquals(3, day.partTwo("se,sw,se,sw,sw"))
    }
}