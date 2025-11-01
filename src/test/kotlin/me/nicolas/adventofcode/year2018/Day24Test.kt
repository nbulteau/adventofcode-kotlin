package me.nicolas.adventofcode.year2018

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day24Test {

    private val testInput = """
Immune System:
17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2
989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3

Infection:
801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1
4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4
    """.trimIndent()

    @Test
    fun `part one should return 5216`() {
        val day = Day24(2018, 24, "Immune System Simulator 20XX")
        assertEquals(5216, day.partOne(testInput))
    }

    @Test
    fun `part two should work with boost`() {
        val day = Day24(2018, 24, "Immune System Simulator 20XX")
        val result = day.partTwo(testInput)
        assertEquals(51, result)
    }
}

