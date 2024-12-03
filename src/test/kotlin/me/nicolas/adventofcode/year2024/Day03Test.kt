package me.nicolas.adventofcode.year2024

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day03Test {
    private val day = Day03(2024, 3, "")

    val test = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"
    val test_part2 = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"

    @Test
    fun partOne() {
        assertEquals(161, day.partOne(test))
    }

    @Test
    fun partTwo() {
        assertEquals(48, day.partTwo(test_part2))
    }
}