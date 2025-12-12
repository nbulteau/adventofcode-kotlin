package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 04: Passport Processing ---
// https://adventofcode.com/2020/day/4
fun main() {
    val data = readFileDirectlyAsText("/year2020/day04/data.txt")
    val day = Day04( )
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day04(year: Int = 20202, day: Int = 4, title: String = "Passport Processing") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val passports = data.split("\n\n")
        return passports.count { passport ->
            val fields = passport.split(Regex("\\s+"))
                .map { it.substringBefore(":") }
                .toSet()
            fields.size == 8 || (fields.size == 7 && "cid" !in fields)
        }
    }

    fun partTwo(data: String): Int {
        val passports = data.split("\n\n")
        return passports.count { passport ->
            val fields = passport.split(Regex("\\s+")).filter { it.isNotEmpty() }
                .associate { it.substringBefore(":") to it.substringAfter(":") }

            val hasRequiredFields = fields.keys.containsAll(listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"))
            if (!hasRequiredFields) return@count false

            val byr = fields["byr"]?.toIntOrNull() in 1920..2002
            val iyr = fields["iyr"]?.toIntOrNull() in 2010..2020
            val eyr = fields["eyr"]?.toIntOrNull() in 2020..2030
            val hgt = isValidHeight(fields["hgt"])
            val hcl = fields["hcl"]?.matches(Regex("#[0-9a-f]{6}")) == true
            val ecl = fields["ecl"] in setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
            val pid = fields["pid"]?.matches(Regex("^[0-9]{9}$")) == true

            byr && iyr && eyr && hgt && hcl && ecl && pid
        }
    }

    private fun isValidHeight(hgt: String?): Boolean {
        if (hgt == null) return false
        return when {
            hgt.endsWith("cm") -> hgt.removeSuffix("cm").toIntOrNull() in 150..193
            hgt.endsWith("in") -> hgt.removeSuffix("in").toIntOrNull() in 59..76
            else -> false
        }
    }
}
