package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day18Test {
    private val day = Day18(2018, 18)
    
    val test = """
        .#.#...|#.
        .....#|##|
        .|..|...#.
        ..|#.....#
        #.#|||#|#|
        ...#.||...
        .|....|...
        ||...#|.#|
        |.||||..|.
        ...#.|..|.
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(1147, day.partOne(test))
    }
}