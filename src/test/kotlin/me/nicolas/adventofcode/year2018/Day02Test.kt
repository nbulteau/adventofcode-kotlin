package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day02Test {

    private val day = Day02("--- Day 2: Inventory Management System ---", "https://adventofcode.com/2018/day/2")

    @Test
    fun `part one training`() {
        val data = """
            abcdef
            bababc
            abbcde
            abcccd
            aabcdd
            abcdee
            ababab
        """.trimIndent()
        val lines = data.split("\n")

        assertEquals( 12, day.partOne(lines))
    }

    @Test
    fun `part two training`() {
        val data = """
            abcde
            fghij
            klmno
            pqrst
            fguij
            axcye
            wvxyz
        """.trimIndent()
        val lines = data.split("\n")

        assertEquals( "fgij", day.partTwo(lines))
    }
}