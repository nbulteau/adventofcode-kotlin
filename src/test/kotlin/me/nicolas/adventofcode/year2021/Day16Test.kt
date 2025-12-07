package me.nicolas.adventofcode.year2021

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day16Test {
    private val day = Day16(2021, 16)

    // Multiple test cases for Day16
    @Test
    fun partOne() {
        assertEquals(16, day.partOne("8A004A801A8002F478"))
        assertEquals(12, day.partOne("620080001611562C8802118E34"))
        assertEquals(23, day.partOne("C0015000016115A2E0802F182340"))
        assertEquals(31, day.partOne("A0016C880162017C3686B18A3D4780"))
    }

    @Test
    fun partTwo() {
        assertEquals(3, day.partTwo("C200B40A82"))
        assertEquals(54, day.partTwo("04005AC33890"))
        assertEquals(7, day.partTwo("880086C3E88112"))
        assertEquals(9, day.partTwo("CE00C43D881120"))
        assertEquals(0, day.partTwo("F600BC2D8F"))
        assertEquals(0, day.partTwo("9C005AC2F8F0"))
        assertEquals(1, day.partTwo("9C0141080250320F1802104A08"))
    }
}
