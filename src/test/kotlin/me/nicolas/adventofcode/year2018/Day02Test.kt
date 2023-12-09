package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day02Test {

    private val day = Day02(2022, 2, "Inventory Management System")

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
        assertEquals(12, day.partOne(data))
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
        assertEquals("fgij", day.partTwo(data))
    }
}