package me.nicolas.adventofcode.year2025

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day10Z3Test {
    private val day = Day10(2025, 10)

    @Test
    fun testFirstMachineZ3() {
        val line = "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}"
        val machine = day.parseMachine(line)

        println("Testing Z3 solver for first machine...")
        println("Joltage Target: ${machine.joltageRequirements}")
        println("Buttons: ${machine.buttons}")

        val result = day.findMinPressesForJoltageZ3(machine)
        println("Min presses (Z3): $result")

        assertEquals(10, result)
    }

    @Test
    fun testSecondMachineZ3() {
        val line = "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}"
        val machine = day.parseMachine(line)

        println("Testing Z3 solver for second machine...")
        println("Joltage Target: ${machine.joltageRequirements}")
        println("Buttons: ${machine.buttons}")

        val result = day.findMinPressesForJoltageZ3(machine)
        println("Min presses (Z3): $result")

        assertEquals(12, result)
    }

    @Test
    fun testThirdMachineZ3() {
        val line = "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"
        val machine = day.parseMachine(line)

        println("Testing Z3 solver for third machine...")
        println("Joltage Target: ${machine.joltageRequirements}")
        println("Buttons: ${machine.buttons}")

        val result = day.findMinPressesForJoltageZ3(machine)
        println("Min presses (Z3): $result")

        assertEquals(11, result)
    }
}

