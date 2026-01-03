package me.nicolas.adventofcode.year2024

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 9: Disk Fragmenter ---
// https://adventofcode.com/2024/day/9
fun main() {
    val data = readFileDirectlyAsText("/year2024/day09/data.txt")
    val day = Day09(2024, 9)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day09(year: Int, day: Int, title: String = "Disk Fragmenter") : AdventOfCodeDay(year, day, title) {


    fun partOne(data: String): Long {

        val blockArray = parseDiskMap(data)
        val compactedFiles = blockArray.compact()

        return compactedFiles.checksum()
    }

    fun partTwo(data: String): Long {

        // Pair<IdBlock, Size>
        val idBlocksWithSize = mutableListOf<Pair<Int, Int>>()
        // Pair<IdBlock, Size>
        val freeSpaceBlocksWithSize = mutableListOf<Pair<Int, Int>>()
        val blockArray: MutableList<Long?> = data.foldIndexed(emptyList()) { index, acc: List<Long?>, char ->
            val amount = char.digitToInt()
            if (index % 2 == 0) {
                idBlocksWithSize.add(acc.size to amount)
                acc + List(amount) { (index / 2).toLong() }
            } else {
                if (amount > 0) {
                    freeSpaceBlocksWithSize.add(acc.size to amount)
                }
                acc + List(amount) { null }
            }
        }.toMutableList()

        // Compact
        while (idBlocksWithSize.isNotEmpty()) {
            val idBlock = idBlocksWithSize.last().first
            val idBlockSize = idBlocksWithSize.last().second
            for (index in freeSpaceBlocksWithSize.indices) {
                val idFreeSpaceBlock = freeSpaceBlocksWithSize[index].first
                val idFreeSpaceBlockSize = freeSpaceBlocksWithSize[index].second
                // Attempt to move each file exactly once in order of decreasing file ID number starting with the file with the highest file ID number.
                if (idFreeSpaceBlock < idBlock
                    && idFreeSpaceBlockSize >= idBlockSize
                ) {
                    // Move
                    for (i in 0 until idBlockSize) {
                        blockArray[idFreeSpaceBlock + i] =
                            blockArray[idBlock + i]

                        blockArray[idBlock + i] = null
                    }
                    // Block is free now
                    freeSpaceBlocksWithSize[index] =
                        idFreeSpaceBlock + idBlockSize to idFreeSpaceBlockSize - idBlockSize
                    break
                }
            }
            idBlocksWithSize.removeLast()
        }

        return blockArray.checksum()
    }

    // Each character's digit value indicates how many consecutive elements follow it (either filled with IDs or nulls for free space).
    private fun parseDiskMap(data: String): List<Long?> =
        data.foldIndexed(emptyList()) { index, acc: List<Long?>, char ->
            val amount = char.digitToInt()
            if (index % 2 == 0) {
                acc + List(amount) { (index / 2).toLong() }
            } else {
                acc + List(amount) { null }
            }
        }


    // Compacts the list by moving all non-null elements to the beginning, maintaining their order.
    private fun List<Long?>.compact(): List<Long?> {
        val workingBlockArray = this.toMutableList()
        var freeIndex = 0
        var lastIndex = workingBlockArray.size - 1

        while (freeIndex < lastIndex) {
            if (workingBlockArray[freeIndex] != null) {
                freeIndex++
            } else {
                workingBlockArray[freeIndex] = workingBlockArray[lastIndex]
                workingBlockArray[lastIndex] = null
                lastIndex--
            }
        }

        return workingBlockArray
    }

    // Computes a checksum for the list based on the index multiplied by the element value (or 0 if null), summing these values.
    private fun List<Long?>.checksum(): Long = this.withIndex().sumOf { (index, id) -> index * (id ?: 0L) }
}