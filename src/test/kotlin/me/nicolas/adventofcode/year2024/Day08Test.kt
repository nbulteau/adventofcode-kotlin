package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day08Test {
    private val day = Day08(2024, 8)

    val test1 = """
        ..........
        ..........
        ..........
        ....a.....
        ..........
        .....a....
        ..........
        ..........
        ..........
        ..........
    """.trimIndent()

    val test2 = """
        ..........
        ..........
        ..........
        ....a.....
        ........a.
        .....a....
        ..........
        ..........
        ..........
        ..........
    """.trimIndent()

    val test3 = """
        T.........
        ...T......
        .T........
        ..........
        ..........
        ..........
        ..........
        ..........
        ..........
        ..........
    """.trimIndent()

    val test = """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(14, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(34, day.partTwo(test))
    }
}