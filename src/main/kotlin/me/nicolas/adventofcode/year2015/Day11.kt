package me.nicolas.adventofcode.year2015

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo

// --- Day 11: Corporate Policy ---
// https://adventofcode.com/2015/day/11
fun main() {
    val day = Day11(2015, 11)
    prettyPrintPartOne { day.partOne("hxbxwxba") }
    prettyPrintPartTwo { day.partTwo("hxbxwxba") }
}

class Day11(year: Int, day: Int, title: String = "Corporate Policy") : AdventOfCodeDay(year, day, title) {

    private fun incrementPassword(password: String): String {
        val chars = password.toCharArray()
        var index = 7

        while (index >= 0) {
            if (chars[index] == 'z') {
                chars[index] = 'a'
                index--
            } else {
                chars[index]++
                break
            }
        }

        return String(chars)
    }

    private fun isValidPassword(password: String): Boolean {
        // Check for forbidden characters
        if (password.contains(Regex("[iol]"))) {
            return false
        }

        // Check for increasing straight of at least three letters
        var hasStraight = false
        for (i in 0..password.length - 3) {
            if ((password[i] + 1 == password[i + 1]) && (password[i + 1] + 1 == password[i + 2])) {
                hasStraight = true
                break
            }
        }
        if (!hasStraight) {
            return false
        }

        // Check for at least two different, non-overlapping pairs of letters
        var pairCount = 0

        var index = 0
        while (index <= password.length - 2) {
            if (password[index] == password[index + 1]) {
                pairCount++
                index++ // Skip the next character to avoid overlapping pairs
            }
            index++
        }
        return pairCount >= 2
    }

    private fun getNextPassword(currentPassword: String): String {
        var newPassword = incrementPassword(currentPassword)
        while (!isValidPassword(newPassword)) {
            newPassword = incrementPassword(newPassword)
        }
        return newPassword
    }

    fun partOne(data: String) = getNextPassword(data)

    fun partTwo(data: String)= getNextPassword(getNextPassword(data))
}