package me.nicolas.adventofcode.year2015

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day25Test {
    private val day = Day25(2015, 25)
    
    val test = """
        To continue, please consult the code grid in the manual.  Enter the code at row 1, column 6.
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(33511524, day.partOne(test))
    }
}