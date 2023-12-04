package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 7: Handy Haversacks ---
// https://adventofcode.com/2020/day/7
fun main() {

    val training = readFileDirectlyAsText("/year2020/day07/training.txt")
    val data = readFileDirectlyAsText("/year2020/day07/data.txt")

    val bags = data.split("\n")

    // Part One
    partOne(bags)

    // Part Two
    partTwo(bags)
}

private fun partOne(bags: List<String>) {
    val rules = parseRules(bags)

    val set = recursiveFind("shiny gold", rules)

    println("Part one = ${set.size}")
}

private fun partTwo(bags: List<String>) {
    val rules = parseRules(bags)

    val total = recursiveCount("shiny gold", rules)

    println("Part two = $total")
}

private fun parseRules(bags: List<String>): MutableMap<String, Map<String, Int>?> {

    val matchResult = Regex("""(\d) ([a-z ]*) bag""")
    val rules = mutableMapOf<String, Map<String, Int>?>()

    bags.forEach { line ->
        val parts = line.split(" bags contain ")
        val bagName = parts[0]
        val bagCapacity = parts[1]

        if (bagCapacity.contains("no other bags.")) {
            rules[bagName] = null
        } else {
            val matchedResults = matchResult.findAll(bagCapacity)

            val inBags = mutableMapOf<String, Int>()
            for (matchedText in matchedResults) {
                inBags[matchedText.groups[2]?.value!!] = matchedText.groups[1]?.value?.toInt()!!
            }
            rules[bagName] = inBags
        }
    }
    return rules
}

private fun recursiveFind(bag: String, rules: Map<String, Map<String, Int>?>): Set<String> {

    return if (rules[bag] == null) {
        emptySet()
    } else {
        val set = rules
            .filterValues { map -> map != null && map.containsKey(bag) }
            .map { it.key }
            .toSet()
        set + set.flatMap { recursiveFind(it, rules) }
    }
}

private fun recursiveCount(bag: String, rules: Map<String, Map<String, Int>?>): Int {

    var total = 0
    rules[bag]?.forEach {
        val count = it.value
        val inBag = it.key
        total += count + count * recursiveCount(inBag, rules)
    }

    return total
}





