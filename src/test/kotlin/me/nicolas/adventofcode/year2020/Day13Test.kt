package me.nicolas.adventofcode.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day13Test {


    @Test
    fun modInverse() {
        assertEquals(31, Day13().modInverse(5, 77))

        assertEquals(8, Day13().modInverse(66, 17))
        assertEquals(4, Day13().modInverse(102, 11))
        assertEquals(1, Day13().modInverse(187, 6))

        assertEquals(5, Day13().modInverse(143, 7))
        assertEquals(4, Day13().modInverse(91, 11))
        assertEquals(12, Day13().modInverse(77, 13))
    }
}