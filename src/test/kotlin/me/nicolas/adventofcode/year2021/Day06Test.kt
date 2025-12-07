package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day06Test {
    private val day = Day06(2021, 6, "Lanternfish")

    val test = "3,4,3,1,2"

    @Test
    fun partOne() {
        val input = test.split(",").map { it.toInt() }
        val lanterns = (0..8).associateWith { idx -> input.count { it == idx }.toLong() }
        assertEquals(5934, day.partOne(lanterns))
    }

    @Test
    fun partTwo() {
        val input = test.split(",").map { it.toInt() }
        val lanterns = (0..8).associateWith { idx -> input.count { it == idx }.toLong() }
        assertEquals(26984457539, day.partTwo(lanterns))
    }
}
