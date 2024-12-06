package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day06Test {
    private val day = Day06(2024, 6, "Guard Gallivant")
    
    val test = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(41, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(6, day.partTwo(test))
    }
}