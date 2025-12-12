package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day03Test {

    private val data = """
        ..##.......
        #...#...#..
        .#....#..#.
        ..#.#...#.#
        .#...##..#.
        ..#.##.....
        .#.#.#....#
        .#........#
        #.##...#...
        #...##....#
        .#..#...#.#
    """.trimIndent()

    private val day = Day03(2020, 3, "Toboggan Trajectory")

    @Test
    fun `part one`() {
        assertEquals(7, day.partOne(data))
    }

    @Test
    fun `part two`() {
        assertEquals(336, day.partTwo(data))
    }
}

