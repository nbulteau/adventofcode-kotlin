package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day07Test {

    private val data = """
        Step C must be finished before step A can begin.
        Step C must be finished before step F can begin.
        Step A must be finished before step B can begin.
        Step A must be finished before step D can begin.
        Step B must be finished before step E can begin.
        Step D must be finished before step E can begin.
        Step F must be finished before step E can begin.
    """.trimIndent()
    private val day = Day07(2018, 7, "The Sum of Its Parts", data)

    @Test
    fun `part one training`() {
        assertEquals("CABDFE", day.partOne())
    }

    @Test
    fun `part two training`() {
        assertEquals(15, day.partTwo(2) { char -> 0 + (char - 'A' + 1) })
    }
}