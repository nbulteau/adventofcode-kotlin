package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day04Test {
    private val day = Day04(2024, 4, "")

    val testPart1 = """
        ....XXMAS.
        .SAMXMS...
        ...S..A...
        ..A.A.MS.X
        XMASAMX.MM
        X.....XA.A
        S.S.S.S.SS
        .A.A.A.A.A
        ..M.M.M.MM
        .X.X.XMASX
    """.trimIndent()

    val testPart2 = """
        .M.S......
        ..A..MSMS.
        .M.S.MAA..
        ..A.ASMSM.
        .M.S.M....
        ..........
        S.S.S.S.S.
        .A.A.A.A..
        M.M.M.M.M.
        ..........   
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(18, day.partOne(testPart1))
    }

    @Test
    fun partTwo() {
        assertEquals(9, day.partTwo(testPart2))
    }
}