package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day12Test {

    private val day = Day12(2023, 12, "")
    private val data = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
        """.trimIndent()
    @Test
    fun `part one training`() {
        assertEquals(21, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        assertEquals(525152, day.partTwo(data))
    }
}