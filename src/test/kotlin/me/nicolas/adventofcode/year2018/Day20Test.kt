package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day20Test {
    private val day = Day20()

    private val test1 = $$"^WNE$"

    private val test2 = $$"^ENWWW(NEEE|SSE(EE|N))$"

    private val test3 = $$"^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$"

    @Test
    fun partOne() {
        assertEquals(3, day.partOne(test1))
        assertEquals(10, day.partOne(test2))
        assertEquals(18, day.partOne(test3))
    }
}