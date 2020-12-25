package me.nicolas.adventofcode.year2019

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class Day04Test {

    @Test
    fun isIncreasingSequence() {

        assertTrue(Day04().isIncreaseOrStayTheSame("111111"))
        assertTrue(Day04().isIncreaseOrStayTheSame("123789"))
        assertFalse(Day04().isIncreaseOrStayTheSame("223450"))
    }

    @Test
    fun hasTwoAdjacentDigits() {

        assertTrue(Day04().hasTwoAdjacentDigitsAreTheSame("111111"))
        assertFalse(Day04().hasTwoAdjacentDigitsAreTheSame("123789"))
        assertTrue(Day04().hasTwoAdjacentDigitsAreTheSame("223450"))
        assertTrue(Day04().hasTwoAdjacentDigitsAreTheSame("123799"))
    }

    @Test
    fun hasAOnlyTwoAdjacentDigitsAreTheSame() {

        assertTrue(Day04().hasAOnlyTwoAdjacentDigitsAreTheSame("112233"))
        assertFalse(Day04().hasAOnlyTwoAdjacentDigitsAreTheSame("123444"))
        assertTrue(Day04().hasAOnlyTwoAdjacentDigitsAreTheSame("111122"))
    }
}