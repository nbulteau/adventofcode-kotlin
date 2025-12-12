package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigInteger

class Day18Test {

    private val day = Day18(2020, 18, "Operation Order")

    @Test
    fun `part one - 1`() {
        assertEquals(BigInteger("71"), day.partOne("1 + 2 * 3 + 4 * 5 + 6"))
    }

    @Test
    fun `part one - 2`() {
        assertEquals(BigInteger("51"), day.partOne("1 + (2 * 3) + (4 * (5 + 6))"))
    }

    @Test
    fun `part one - 3`() {
        assertEquals(BigInteger("26"), day.partOne("2 * 3 + (4 * 5)"))
    }

    @Test
    fun `part one - 4`() {
        assertEquals(BigInteger("437"), day.partOne("5 + (8 * 3 + 9 + 3 * 4 * 3)"))
    }

    @Test
    fun `part one - 5`() {
        assertEquals(BigInteger("12240"), day.partOne("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"))
    }

    @Test
    fun `part one - 6`() {
        assertEquals(BigInteger("13632"), day.partOne("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))
    }

    @Test
    fun `part two - 1`() {
        assertEquals(BigInteger("231"), day.partTwo("1 + 2 * 3 + 4 * 5 + 6"))
    }

    @Test
    fun `part two - 2`() {
        assertEquals(BigInteger("51"), day.partTwo("1 + (2 * 3) + (4 * (5 + 6))"))
    }

    @Test
    fun `part two - 3`() {
        assertEquals(BigInteger("46"), day.partTwo("2 * 3 + (4 * 5)"))
    }

    @Test
    fun `part two - 4`() {
        assertEquals(BigInteger("1445"), day.partTwo("5 + (8 * 3 + 9 + 3 * 4 * 3)"))
    }

    @Test
    fun `part two - 5`() {
        assertEquals(BigInteger("669060"), day.partTwo("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"))
    }

    @Test
    fun `part two - 6`() {
        assertEquals(BigInteger("23340"), day.partTwo("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))
    }
}

