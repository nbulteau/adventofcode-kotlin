package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2021/day/16
fun main() {
    val data = readFileDirectlyAsText("/year2021/day16/data.txt")
    val day = Day16(2021, 16, "Packet Decoder")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day16(year: Int, day: Int, title: String = "Packet Decoder") : AdventOfCodeDay(year, day, title) {

    fun partOne(input: String): Int {
        val binaryString = inputToBinaryString(input)
        val transmission = Transmission(binaryString)
        transmission.parse()

        return transmission.versionSum
    }

    fun partTwo(input: String): Long {
        val binaryString = inputToBinaryString(input)
        val transmission = Transmission(binaryString)

        return transmission.parse()
    }

    private class Transmission(private val binary: String) {

        companion object {
            const val SUM = 0
            const val PRODUCT = 1
            const val MINIMUM = 2
            const val MAXIMUM = 3
            const val GREATER_THAN = 5
            const val LESS_THAN = 6
            const val EQUALS_TO = 7

            private fun processOperation(operation: Int, values: ArrayList<Long>): Long {
                return when (operation) {
                    SUM -> values.sumOf { it }
                    PRODUCT -> values.fold(1) { acc, value -> acc * value }
                    MINIMUM -> values.minOf { it }
                    MAXIMUM -> values.maxOf { it }
                    GREATER_THAN -> if (values.first() > values.last()) 1L else 0L
                    LESS_THAN -> if (values.first() < values.last()) 1L else 0L
                    EQUALS_TO -> if (values.first() == values.last()) 1L else 0L
                    else -> throw RuntimeException("Illegal operation $operation")
                }
            }
        }

        private var currentIndex: Int = 0

        var versionSum = 0
            private set

        fun parse(): Long {
            var value = 0L
            processVersion() // version
            when (val type = next(offset = 3)) {
                4 -> value = nextValue()
                else -> {
                    when (next(offset = 1)) {
                        0 -> value = parseByLength(length = next(offset = 15), operation = type)
                        1 -> value = parseByPackets(nbPackets = next(offset = 11), operation = type)
                    }
                }
            }

            return value
        }

        private fun parseByPackets(nbPackets: Int, operation: Int): Long {
            val values = ArrayList<Long>()
            var numPackets = 0
            while (++numPackets <= nbPackets) {
                processVersion() // version
                processLengthType(values)
            }

            return processOperation(operation, values)
        }

        private fun parseByLength(length: Int, operation: Int): Long {
            val values = ArrayList<Long>()
            val start = this.currentIndex
            while (this.currentIndex - start < length) {
                processVersion() // version
                processLengthType(values)
            }

            return processOperation(operation, values)
        }

        private fun processVersion() {
            versionSum += next(3)
        }

        private fun processLengthType(values: ArrayList<Long>) {
            when (val type = next(offset = 3)) {
                4 -> values.add(nextValue())
                else -> when (next(offset = 1)) {
                    0 -> values.add(parseByLength(length = next(offset = 15), operation = type))
                    1 -> values.add(parseByPackets(nbPackets = next(offset = 11), operation = type))
                }
            }
        }

        private fun next(offset: Int): Int {
            val next = binary.substring(currentIndex, currentIndex + offset).toInt(2)
            currentIndex += offset

            return next
        }

        private fun nextValue(): Long {
            var value = 0L
            while (next(1) == 1) value = (value shl 4) + next(4)
            value = (value shl 4) + next(4)

            return value
        }
    }

    private fun inputToBinaryString(input: String): String {
        return input.map { char ->
            char.digitToInt(16).toString(2).padStart(4, '0')
        }.joinToString("")
    }
}
