package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day07Test {
    private val day = Day07(2021, 7, "The Treachery of Whales")

    val test = "16,1,2,0,4,2,7,1,2,14"

    @Test
    fun partOne() {
        val input = test.split(",").map { it.toInt() }
        assertEquals(37, day.partOne(input))
    }

    @Test
    fun partTwo() {
        val input = test.split(",").map { it.toInt() }
        assertEquals(168, day.partTwo(input))
    }
}
