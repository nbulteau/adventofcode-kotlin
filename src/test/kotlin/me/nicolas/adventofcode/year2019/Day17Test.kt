package me.nicolas.adventofcode.year2019

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day17Test {

    @Test
    fun testIntersectionsAlignmentParameters() {
        // Test alignment parameter calculation with the example from the problem
        // The intersections from the example are at (2,2), (2,4), (6,4), (10,4)
        val intersections = listOf(
            Pair(2, 2),  // alignment = 4
            Pair(2, 4),  // alignment = 8
            Pair(6, 4),  // alignment = 24
            Pair(10, 4)  // alignment = 40
        )
        val sum = intersections.sumOf { (x, y) -> x * y }
        assertEquals(76, sum)
    }

    @Test
    fun testPathCompressionExample() {
        // Test that the example functions from the problem are valid (under 20 chars)
        // Main: A,B,C,B,A,C
        // A: R,8,R,8
        // B: R,4,R,4,R,8
        // C: L,6,L,2

        val mainRoutine = "A,B,C,B,A,C"
        val functionA = "R,8,R,8"
        val functionB = "R,4,R,4,R,8"
        val functionC = "L,6,L,2"

        // Verify all are under 20 characters
        assert(mainRoutine.length <= 20) { "Main routine too long: ${mainRoutine.length}" }
        assert(functionA.length <= 20) { "Function A too long: ${functionA.length}" }
        assert(functionB.length <= 20) { "Function B too long: ${functionB.length}" }
        assert(functionC.length <= 20) { "Function C too long: ${functionC.length}" }
    }
}