package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day03Test {
    private val day = Day03(2024, 3, "")

    val testPart1 = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"

    val testPart2 = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"

    @Test
    fun partOne() {
        assertEquals(161, day.partOne(testPart1))
    }

    @Test
    fun partTwo() {
        assertEquals(48, day.partTwo(testPart2))
    }
}