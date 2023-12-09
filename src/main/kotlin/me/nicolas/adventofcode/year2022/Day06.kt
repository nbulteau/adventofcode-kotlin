package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {

    val training = readFileDirectlyAsText("/year2022/day06/training.txt")
    val data = readFileDirectlyAsText("/year2022/day06/data.txt")

    val day = Day06(2022, 6, "Tuning Trouble")
    prettyPrintPartOne { day.solve(4, data) }
    prettyPrintPartTwo { day.solve(14, data) }
    prettyPrintPartOne { day.solveWithSet(4, data) }
    prettyPrintPartOne { day.solveWithSet(14, data) }
}

private class Day06(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun solve(capacity: Int, datastream: String): Int {
        val bufferOfFour = FifoFixedSizeQueue(capacity)
        return (capacity..datastream.length).first {
            bufferOfFour.offer(datastream[it]).isStartOfPacketMarker()
        }
    }

    fun solveWithSet(capacity: Int, datastream: String): Int {
        return (capacity..datastream.length).first {
            datastream.substring(it - capacity, it).toSet().size == capacity
        }
    }

    private class FifoFixedSizeQueue(val capacity: Int) {
        /** The queued items  */
        val items: Array<Char?> = arrayOfNulls(capacity)

        /** Number of elements in the queue  */
        var count: Int = 0

        fun offer(e: Char): FifoFixedSizeQueue {
            if (count == items.size) {
                shiftLeft()
                count--
            }
            items[count] = e
            count++

            return this
        }

        fun isStartOfPacketMarker(): Boolean {
            if (count < capacity) return false
            var value = true
            var checker = 0
            for (element in items) {
                val c = element!!.code - 'a'.code
                if (checker and (1 shl c) > 0) {
                    value = false
                    break
                }
                checker = checker or (1 shl c)
            }
            return value
        }

        private fun shiftLeft() {
            var i = 1
            while (i < items.size) {
                if (items[i] == null) {
                    break
                }
                items[i - 1] = items[i]
                i++
            }
        }
    }
}
