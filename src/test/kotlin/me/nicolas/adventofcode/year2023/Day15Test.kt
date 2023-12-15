package me.nicolas.adventofcode.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day15Test {
    private val day = Day15(2023, 15, "Lens Library")


    @Test
    fun `part one training one`() {
        val data = "HASH"
        assertEquals(52, day.partOne(data))
    }

    @Test
    fun `part one training two`() {
        val data = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"

        assertEquals(1320, day.partOne(data))
    }

    @Test
    fun `part two training`() {
        val data = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"

        assertEquals(145, day.partTwo(data))
    }
}