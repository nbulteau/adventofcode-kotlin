package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day10Test {
    private val day = Day10(2021, 10, "Syntax Scoring")

    val test = """
        [({(<(())[]>[[{[]{<()<>>
        [(()[<>])]({[<{<<[]>>(
        {([(<{}[<>[]}>{[]{[(<()>
        (((({<>}<{<{<>}{[]{[]{}
        [[<[([]))<([[{}[[()]]]
        [{[{({}]{}}([{[{{{}}([]
        {<[[]]>}<{[{[{[]{()[[[]
        [<(<(<(<{}))><([]([]()
        <{([([[(<>()){}]>(<<{{
        <{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(26397, day.partOne(test.split("\n")))
    }

    @Test
    fun partTwo() {
        assertEquals(288957, day.partTwo(test.split("\n")))
    }
}
