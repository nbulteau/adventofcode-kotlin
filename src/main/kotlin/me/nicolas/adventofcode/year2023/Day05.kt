package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import java.util.*
import kotlin.math.min

fun main() {
    val data = readFileDirectlyAsText("/year2023/day05/data.txt")
    val day = Day05("--- Day 5: If You Give A Seed A Fertilizer ---", "https://adventofcode.com/2023/day/5")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
    prettyPrintPartTwo("(Brut force)") { day.partTwoBrutForce(data) }
}

class Day05(title: String, adventOfCodeLink: String) : AdventOfCodeDay(title, adventOfCodeLink) {

    // Represents a mapping between two ranges
    private data class Mapping(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long) {

        // Returns true if the value is in the range
        fun inRange(value: Long): Boolean = value >= sourceRangeStart && value < sourceRangeStart + rangeLength

        // Returns the mapped value
        fun mapValue(value: Long): Long = destinationRangeStart + (value - sourceRangeStart)
    }

    // All mappings between source and destination
    // @mappings : The map is sorted according to the natural ordering of its keys
    private data class AlmanacEntry(val source: String, val destination: String, val mappings: TreeMap<Long, Mapping>) {
        fun getMapping(value: Long): Long {
            // Returns a key-value mapping associated with the greatest key less than or equal to the given key,
            // or null if there is no such key.
            val potentialMatch = mappings.floorEntry(value)
            // If the value is not in the range, return the value
            if (potentialMatch == null || !potentialMatch.value.inRange(value)) {
                return value
            }

            return potentialMatch.value.mapValue(value)
        }

        // Get all mappings range for a range
        fun getMappingForRange(range: LongRange): List<LongRange> {
            val result = mutableListOf<LongRange>()
            var start = range.first

            // iterate over the range
            while (start in range) {
                val floorMapping = mappings.floorEntry(start)
                // If the value is in the range, add the mapped range
                if (floorMapping != null && floorMapping.value.inRange(start)) {
                    // Get the end of the range
                    val end = min(floorMapping.value.sourceRangeStart + floorMapping.value.rangeLength - 1, range.last)
                    // Add the mapped range
                    result.add(LongRange(floorMapping.value.mapValue(start), floorMapping.value.mapValue(end)))

                    start = end + 1
                } else {
                    // Returns a key-value mapping associated with the least key greater than or equal to the given key,
                    // or null if there is no such key
                    val ceilMapping = mappings.ceilingEntry(start)
                    // Get the end of the range
                    val end = if (ceilMapping != null) {
                        min(range.last, ceilMapping.value.sourceRangeStart - 1)
                    } else {
                        range.last
                    }
                    // Add the range
                    result.add(LongRange(start, end))

                    start = end + 1
                }
            }
            return result
        }
    }

    private fun getLocationForSeed(seed: Long, almanac: Map<String, AlmanacEntry>): Long {
        var value = seed
        var type = "seed"
        do {
            val almanacEntry = almanac[type]!!
            value = almanacEntry.getMapping(value)
            type = almanacEntry.destination
        } while (type != "location")

        return value
    }

    private fun getLocationsForSeedRanges(seed: List<LongRange>, almanac: Map<String, AlmanacEntry>): List<LongRange> {
        var values = seed
        var type = "seed"
        do {
            val almanacEntry = almanac[type]!!
            values = values.flatMap { almanacEntry.getMappingForRange(it) }
            type = almanacEntry.destination
        } while (type != "location")

        return values
    }

    private fun extractAlmanac(blocks: List<String>): Map<String, AlmanacEntry> {
        val almanacEntries = blocks.drop(1).map { block ->
            val lines = block.split("\n")
            val (source, destination) = lines.first().replace(" map:", "").split("-to-")
            val mappings = lines.drop(1).map { line ->
                val (destinationRangeStart, sourceRangeStart, rangeLength) = line.split(" ").map { it.toLong() }
                Mapping(destinationRangeStart, sourceRangeStart, rangeLength)
            }
            val entries = mappings.associateBy { mapping -> mapping.sourceRangeStart }.toMap(TreeMap())

            AlmanacEntry(source, destination, entries)
        }

        return almanacEntries.associateBy { mapping -> mapping.source }
    }

    private fun extractSeeds(blocks: List<String>) =
        blocks.first().replace("seeds: ", "").trim().split(" ").map { it.toLong() }

    private fun extractSeedsFromRanges(blocks: List<String>) =
        blocks.first().replace("seeds: ", "").trim().split(" ").map { it.toLong() }
            .windowed(2, 2)
            .map { it[0]..<it[0] + it[1] }

    fun partOne(data: String): Long {
        val almanac = data.split("\n\n")
        val seeds = extractSeeds(almanac)
        val mappings = extractAlmanac(almanac)

        return seeds.minOf { seed -> getLocationForSeed(seed, mappings) }
    }

    // Brut force solution for part two (very slow) :( (takes more than 5 minutes)
    fun partTwoBrutForce(data: String): Long {
        val almanac = data.split("\n\n")
        val seeds = extractSeedsFromRanges(almanac)
        val mappings = extractAlmanac(almanac)

        return seeds.map { range ->
            range.toList().minOf { seed -> getLocationForSeed(seed, mappings) }
        }.toList().min()
    }

    fun partTwo(data: String): Long {
        val almanac = data.split("\n\n")
        val seeds = extractSeedsFromRanges(almanac)
        val mappings = extractAlmanac(almanac)

        return getLocationsForSeedRanges(seeds, mappings).minBy { it.first }.first
    }
}

