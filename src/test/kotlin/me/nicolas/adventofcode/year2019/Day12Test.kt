package me.nicolas.adventofcode.year2019

import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12Test {

    @Test
    fun `part one - example 1`() {
        val input = """
            <x=-1, y=0, z=2>
            <x=2, y=-10, z=-7>
            <x=4, y=-8, z=8>
            <x=3, y=5, z=-1>
        """.trimIndent()

        val day = Day12(2019, 12, "The N-Body Problem", input)
        val moons = day.parseMoons(input).map { it.copy() }

        repeat(10) {
            day.simulateStep(moons)
        }

        val totalEnergy = moons.sumOf { it.totalEnergy() }
        assertEquals(179, totalEnergy)
    }

    @Test
    fun `part one - example 2`() {
        val input = """
            <x=-8, y=-10, z=0>
            <x=5, y=5, z=10>
            <x=2, y=-7, z=3>
            <x=9, y=-8, z=-3>
        """.trimIndent()

        val day = Day12(2019, 12, "The N-Body Problem", input)
        val moons = day.parseMoons(input).map { it.copy() }

        repeat(100) {
            day.simulateStep(moons)
        }

        val totalEnergy = moons.sumOf { it.totalEnergy() }
        assertEquals(1940, totalEnergy)
    }

    @Test
    fun `part one - actual input`() {
        val input = readFileDirectlyAsText("/year2019/day12/data.txt")
        val day = Day12(2019, 12, "The N-Body Problem", input)
        val result = day.partOne()
        println("Part One Answer: $result")
    }

    @Test
    fun `part two - example 1`() {
        val input = """
            <x=-1, y=0, z=2>
            <x=2, y=-10, z=-7>
            <x=4, y=-8, z=8>
            <x=3, y=5, z=-1>
        """.trimIndent()

        val day = Day12(2019, 12, "The N-Body Problem", input)
        val result = day.partTwo()
        assertEquals(2772L, result)
    }

    @Test
    fun `part two - example 2`() {
        val input = """
            <x=-8, y=-10, z=0>
            <x=5, y=5, z=10>
            <x=2, y=-7, z=3>
            <x=9, y=-8, z=-3>
        """.trimIndent()

        val day = Day12(2019, 12, "The N-Body Problem", input)
        val result = day.partTwo()
        assertEquals(4686774924L, result)
    }

    @Test
    fun `part two - actual input`() {
        val input = readFileDirectlyAsText("/year2019/day12/data.txt")
        val day = Day12(2019, 12, "The N-Body Problem", input)
        val result = day.partTwo()
        println("Part Two Answer: $result")
    }
}

