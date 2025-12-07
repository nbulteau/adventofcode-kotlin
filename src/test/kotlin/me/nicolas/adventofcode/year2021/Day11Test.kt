package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day11Test {
    private val day = Day11(2021, 11, "Dumbo Octopus")

    val test = """
        5483143223
        2745854711
        5264556173
        6141336146
        6357385278
        4167524645
        2176841721
        6882881134
        4846848554
        5283751526
    """.trimIndent()

    @Test
    fun partOne() {
        val lines = test.split("\n").map { line -> line.toList().map { it.toString().toInt() } }
        assertEquals(1656, day.partOne(lines))
    }

    @Test
    fun partTwo() {
        val lines = test.split("\n").map { line -> line.toList().map { it.toString().toInt() } }
        assertEquals(195, day.partTwo(lines))
    }
}
