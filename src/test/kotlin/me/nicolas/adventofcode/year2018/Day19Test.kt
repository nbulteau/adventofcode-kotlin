package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day19Test {
    private val day = Day19()
    
    val test = """
        #ip 0
        seti 5 0 1
        seti 6 0 2
        addi 0 1 0
        addr 1 2 3
        setr 1 0 0
        seti 8 0 4
        seti 9 0 5
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(7, day.partOne(test))
    }
}