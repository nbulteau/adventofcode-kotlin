package me.nicolas.adventofcode.year2017

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day09Test {
    private val day = Day09()

    @Test
    fun partOne_examples() {
        assertEquals(1, day.partOne("{}"))
        assertEquals(6, day.partOne("{{{}}}"))
        assertEquals(5, day.partOne("{{},{}}"))
        assertEquals(16, day.partOne("{{{},{},{{}}}}"))
        assertEquals(1, day.partOne("{<{},{},{{}}>}"))
        assertEquals(1, day.partOne("{<a>,<a>,<a>,<a>}"))
        assertEquals(9, day.partOne("{{<ab>},{<ab>},{<ab>},{<ab>}}"))
        assertEquals(9, day.partOne("{{<!!>},{<!!>},{<!!>},{<!!>}}"))
        assertEquals(3, day.partOne("{{<a!>},{<a!>},{<a!>},{<ab>}}"))
    }

    @Test
    fun partTwo_examples() {
        assertEquals(0, day.partTwo("<>"))
        assertEquals(17, day.partTwo("<random characters>"))
        assertEquals(3, day.partTwo("<<<<>"))
        assertEquals(2, day.partTwo("<{!>}>"))
        assertEquals(0, day.partTwo("<!!>"))
        assertEquals(0, day.partTwo("<!!!>>"))
        assertEquals(10, day.partTwo("<{o\"i!a,<{i<a>"))
    }
}