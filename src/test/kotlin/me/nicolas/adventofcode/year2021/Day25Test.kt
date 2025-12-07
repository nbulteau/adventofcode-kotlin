package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day25Test {
    private val day = Day25(2021, 25, "Sea Cucumber")

    val test = """
        v...>>.vv>
        .vv>>.vv..
        >>.>v>...v
        >>v>>.>.v.
        v>v.vv.v..
        >.>>..v.v.
        .vv..>.>v.
        v.v..>>v.v
        ....v..v.>
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(58, day.partOne(test.split("\n")))
    }
}
