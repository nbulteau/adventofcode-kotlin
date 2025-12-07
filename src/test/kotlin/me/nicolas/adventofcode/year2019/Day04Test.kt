package me.nicolas.adventofcode.year2019

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class Day04Test {

    private val day04 = Day04(2019, 4)

    @Test
    fun isIncreasingSequence() {
        assertTrue(day04.isIncreaseOrStayTheSame("111111"))
        assertTrue(day04.isIncreaseOrStayTheSame("123789"))
        assertFalse(day04.isIncreaseOrStayTheSame("223450"))
    }

    @Test
    fun hasTwoAdjacentDigits() {

        assertTrue(day04.hasTwoAdjacentDigitsAreTheSame("111111"))
        assertFalse(day04.hasTwoAdjacentDigitsAreTheSame("123789"))
        assertTrue(day04.hasTwoAdjacentDigitsAreTheSame("223450"))
        assertTrue(day04.hasTwoAdjacentDigitsAreTheSame("123799"))
    }

    @Test
    fun hasAOnlyTwoAdjacentDigitsAreTheSame() {

        assertTrue(day04.hasAOnlyTwoAdjacentDigitsAreTheSame("112233"))
        assertFalse(day04.hasAOnlyTwoAdjacentDigitsAreTheSame("123444"))
        assertTrue(day04.hasAOnlyTwoAdjacentDigitsAreTheSame("111122"))
    }
}