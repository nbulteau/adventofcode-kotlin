package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day15Test {
    private val day = Day15(2021, 15, "Chiton")

    val test = """
        1163751742
        1381373672
        2136511328
        3694931569
        7463417111
        1319128137
        1359912421
        3125421639
        1293138521
        2311944581
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(40, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(315, day.partTwo(test))
    }
}
