package me.nicolas.adventofcode.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MathsUtilsKtTest {

    @Test
    fun testGcd() {
        assert(8L.gcd(12L) == 4L)
        assert(54L.gcd(24L) == 6L)
        assert(48L.gcd(180L) == 12L)
    }

    @Test
    fun testLcm() {
        assert(8L.lcm(12L) == 24L)
        assert(54L.lcm(24L) == 216L)
        assert(48L.lcm(180L) == 720L)
    }

    @Test
    fun testManhattanDistance() {
        assert(Pair(0, 0).manhattanDistance(Pair(0, 0)) == 0L)
        assert(Pair(0, 0).manhattanDistance(Pair(1, 0)) == 1L)
        assert(Pair(0, 0).manhattanDistance(Pair(0, 1)) == 1L)
        assert(Pair(0, 0).manhattanDistance(Pair(1, 1)) == 2L)
        assert(Pair(0, 0).manhattanDistance(Pair(2, 3)) == 5L)
        assert(Pair(0, 0).manhattanDistance(Pair(-2, -3)) == 5L)
    }

    @Test
    fun testLcmList() {
        assert(listOf(8L, 12L).lcm() == 24L)
        assert(listOf(54L, 24L).lcm() == 216L)
        assert(listOf(48L, 180L).lcm() == 720L)
    }

    @Test
    fun testShoelaceArea() {
        val points = listOf(
            Pair(3L, 4L),
            Pair(5L, 11L),
            Pair(12L, 8L),
            Pair(9L, 5L),
            Pair(5L, 6L)
        )
        assertEquals(30L, shoelaceArea(points))
    }
}