package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day17Test {
    private val day = Day17(2021, 17, "Trick Shot")

    val test = "target area: x=20..30, y=-10..-5"

    @Test
    fun partOne() {
        assertEquals(45, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(112, day.partTwo(test))
    }
}
