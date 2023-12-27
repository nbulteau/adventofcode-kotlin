package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// https://adventofcode.com/2018/day/8
fun main() {
    val data = readFileDirectlyAsText("/year2018/day08/data.txt")
    val day = Day08(2018, 8, "Memory Maneuver")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}


class Day08(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    // LicenseNode class represents a node in the license tree
    private data class LicenseNode(val children: List<LicenseNode>, val meta: List<Int>) {
        // Property to calculate the sum of metadata entries for a LicenseNode
        val metaSum: Int
            get() = meta.sum() + children.sumOf { it.metaSum }

        // Property to calculate the value of a LicenseNode
        val value: Int
            get() = when {
                children.isNotEmpty() -> meta.sumOf { children.getOrNull(it - 1)?.value ?: 0 }
                else -> meta.sum()
            }
    }


    fun partOne(data: String): Int {
        val nodes = data.split(" ").map(String::toInt)
        val (root, _) = listOfIntToLicences(nodes)

        return root.metaSum
    }

    fun partTwo(data: String): Int {
        val nodes = data.split(" ").map(String::toInt)
        val (root, _) = listOfIntToLicences(nodes)

        return root.value
    }

    private fun listOfIntToLicences(lists: List<Int>): Pair<LicenseNode, List<Int>> {
        val (childCount, metaCount) = lists.take(2)
        val children = mutableListOf<LicenseNode>()
        val left = generateSequence(lists.drop(2)) {
            val (nb, left) = listOfIntToLicences(it)
            children += nb
            left
        }.take(childCount + 1).last()

        return LicenseNode(children, left.take(metaCount)) to left.drop(metaCount)
    }

}