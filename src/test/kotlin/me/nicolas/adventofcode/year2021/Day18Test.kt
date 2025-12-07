package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day18Test {
    private val day = Day18(2021, 18, "Snailfish")

    val test = """
        [[[0,[5,8]],[[1,7],[6,0]]],[[3,[1,[3,8]]],[[0,[6,7]],[1,[9,[9,0]]]]]
        [[[5,[2,8]],4],[5,[[9,9],0]]]
        [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
        [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
        [[[7,[6,4]],[3,[1,3]]],[[[0,6],6],[[6,3],[4,9]]]]
        [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
        [[[[5,4],[7,7]],8],[[8,3],8]]
        [[9,3],[[9,9],[6,[4,9]]]]
        [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
        [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(4140, day.partOne(test.split("\n")))
    }

    @Test
    fun partTwo() {
        assertEquals(3993, day.partTwo(test.split("\n")))
    }
}
