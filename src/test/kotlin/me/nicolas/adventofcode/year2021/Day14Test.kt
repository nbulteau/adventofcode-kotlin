package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day14Test {
    private val day = Day14(2021, 14, "Extended Polymerization")

    val test = """
        NNCB
        
        CH -> B
        HH -> N
        CB -> H
        NH -> C
        HB -> C
        HC -> B
        HN -> C
        NN -> C
        BH -> H
        NC -> B
        NB -> B
        BN -> B
        BB -> N
        BC -> B
        CC -> N
        CN -> C
    """.trimIndent()

    @Test
    fun partOne() {
         assertEquals(1588, day.partOne(test))
    }

    @Test
    fun partTwo() {
         assertEquals(2188189693529, day.partTwo(test))
    }
}
