package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.max

// https://adventofcode.com/2021/day/18
fun main() {
    val data = readFileDirectlyAsText("/year2021/day18/data.txt")
    val day = Day18(2021, 18, "Snailfish")
    val lines = data.split("\n")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

class Day18(year: Int, day: Int, title: String = "Snailfish") : AdventOfCodeDay(year, day, title) {

    fun partOne(lines: List<String>): Int {
        val sNumbers = lines.map { line -> parseLine(line) }
        val sNumber = sNumbers.reduce { acc, sNum -> add(acc, sNum) }

        return sNumber.magnitude()
    }

    fun partTwo(lines: List<String>): Int {
        val sNumbers = lines.map { line -> parseLine(line) }

        var magnitudeMax = 0
        for (i in sNumbers.indices) {
            for (j in sNumbers.indices) {
                if (i != j) {
                    magnitudeMax = max(magnitudeMax, add(sNumbers[i], sNumbers[j]).magnitude())
                }
            }
        }

        return magnitudeMax
    }

    sealed class SnailfishNumber {

        private fun findPairToExplode(nestedLevel: Int): Pair? {
            if (nestedLevel == 0)
                return this as? Pair?
            if (this is Pair) {
                left.findPairToExplode(nestedLevel - 1)?.let { return it }
                right.findPairToExplode(nestedLevel - 1)?.let { return it }
            }
            return null
        }

        fun reduce(): SnailfishNumber {
            var snailfishNumber = this
            do {
                val pairToExplode = snailfishNumber.findPairToExplode(4)
                val regularNumberToSplit = snailfishNumber.findRegularNumberToSplit()

                if (pairToExplode != null) {
                    snailfishNumber = snailfishNumber.explode(pairToExplode)
                } else if (regularNumberToSplit != null) {
                    snailfishNumber = snailfishNumber.split(regularNumberToSplit)
                }
            } while ((pairToExplode != null) || (regularNumberToSplit != null))

            return snailfishNumber
        }

        private fun explode(pairToExplode: Pair): SnailfishNumber {
            val explodeOperations =
                mutableMapOf<SnailfishNumber, SnailfishNumber>(pairToExplode to RegularNumber(0))

            val all = traverse(pairToExplode)
            val index = all.indexOf(pairToExplode)
            // look left
            val leftRegularNumber = all.getOrNull(index - 1) as? RegularNumber
            leftRegularNumber?.let {
                val left = pairToExplode.left as RegularNumber
                explodeOperations[it] = RegularNumber(it.value + left.value)
            }
            // look right
            val rightRegularNumber = all.getOrNull(index + 1) as? RegularNumber
            rightRegularNumber?.let {
                val right = pairToExplode.right as RegularNumber
                explodeOperations[it] = RegularNumber(it.value + right.value)
            }

            return this.replace(explodeOperations)
        }

        private fun split(regularNumberToSplit: RegularNumber): SnailfishNumber {
            // split regular number : the left element of the pair should be the regular number divided by two and rounded down,
            // while the right element of the pair should be the regular number divided by two and rounded up.
            val splitOperations = mapOf<SnailfishNumber, SnailfishNumber>(
                regularNumberToSplit to Pair(
                    RegularNumber(regularNumberToSplit.value / 2),
                    RegularNumber((regularNumberToSplit.value + 1) / 2)
                )
            )

            return this.replace(splitOperations)
        }

        abstract fun replace(operations: Map<SnailfishNumber, SnailfishNumber>): SnailfishNumber

        abstract fun findRegularNumberToSplit(): RegularNumber?

        abstract fun traverse(keep: Pair): List<SnailfishNumber>

        abstract fun magnitude(): Int
    }

    class RegularNumber(val value: Int) : SnailfishNumber() {
        override fun replace(operations: Map<SnailfishNumber, SnailfishNumber>): SnailfishNumber {
            operations[this]?.let { return it }
            return this
        }

        override fun findRegularNumberToSplit(): RegularNumber? {
            return if (value >= 10) this else null
        }

        override fun traverse(keep: Pair): List<SnailfishNumber> {
            return listOf(this)
        }

        override fun magnitude(): Int {
            return value
        }

        override fun toString(): String = value.toString()
    }

    class Pair(val left: SnailfishNumber, val right: SnailfishNumber) : SnailfishNumber() {

        override fun replace(operations: Map<SnailfishNumber, SnailfishNumber>): SnailfishNumber {
            operations[this]?.let { return it }
            return Pair(left.replace(operations), right.replace(operations))
        }

        override fun findRegularNumberToSplit(): RegularNumber? {
            left.findRegularNumberToSplit()?.let { return it }
            right.findRegularNumberToSplit()?.let { return it }

            return null
        }

        override fun traverse(keep: Pair): List<SnailfishNumber> {
            return if (this == keep) listOf(this) else left.traverse(keep) + right.traverse(keep)
        }

        override fun magnitude(): Int {
            return 3 * left.magnitude() + 2 * right.magnitude()
        }

        override fun toString(): String = "[$left,$right]"
    }

    private fun parseLine(line: String): SnailfishNumber {
        var index = 0

        fun recursiveParse(): SnailfishNumber {
            // This is a Pair
            if (line[index] == '[') {
                index++
                val left = recursiveParse()
                index++ // ','
                val right = recursiveParse()
                index++ // ']'
                return Pair(left, right)
            }
            // This is a regular number
            return RegularNumber(line[index++].digitToInt())
        }

        return recursiveParse()
    }

    // addition
    fun add(first: SnailfishNumber, second: SnailfishNumber): SnailfishNumber {
        return Pair(first, second).reduce()
    }

}
