package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day24Test {
    private val day = Day24(2021, 24, "Arithmetic Logic Unit")

    val test = """
        inp w
        add z w
        mod z 2
        div w 2
        add y w
        mod y 2
        div w 2
        add x w
        mod x 2
        div w 2
        mod w 2
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(92L, day.partOne(test.split("\n")))
    }

    @Test
    fun partTwo() {
        assertEquals(71L, day.partTwo(test.split("\n")))
    }
}
