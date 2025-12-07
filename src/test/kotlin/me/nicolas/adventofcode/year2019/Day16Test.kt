package me.nicolas.adventofcode.year2019

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day16Test {
    private val day = Day16(2019, 16)

    @Test
    fun `part one sample 1`() {
        assertEquals("24176176", day.partOne("80871224585914546619083218645595"))
    }

    @Test
    fun `part one sample 2`() {
        assertEquals("73745418", day.partOne("19617804207202209144916044189917"))
    }

    @Test
    fun `part one sample 3`() {
        assertEquals("52432133", day.partOne("69317163492948606335995924319873"))
    }

    @Test
    fun `part two sample 1`() {
        assertEquals("84462026", day.partTwo("03036732577212944063491565474664"))
    }

    @Test
    fun `part two sample 2`() {
        assertEquals("78725270", day.partTwo("02935109699940807407585447034323"))
    }

    @Test
    fun `part two sample 3`() {
        assertEquals("53553731", day.partTwo("03081770884921959731165446850517"))
    }
}