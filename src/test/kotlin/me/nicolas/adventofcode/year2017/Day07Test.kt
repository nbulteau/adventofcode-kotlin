package me.nicolas.adventofcode.year2017

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day07Test {
    private val day = Day07()
    
    val test = """
        pbga (66)
        xhth (57)
        ebii (61)
        havc (66)
        ktlj (57)
        fwft (72) -> ktlj, cntj, xhth
        qoyq (66)
        padx (45) -> pbga, havc, qoyq
        tknk (41) -> ugml, padx, fwft
        jptl (61)
        ugml (68) -> gyxo, ebii, jptl
        gyxo (61)
        cntj (57)
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals("tknk", day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(60, day.partTwo(test))
    }
}