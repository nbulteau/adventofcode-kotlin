package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {
    val data = readFileDirectlyAsText("/year2023/day19/data.txt")
    val day = Day19(2023, 19, "Aplenty")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day19(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Long {
        // Split the data into workflows and parts
        val (workflowStrings, partStrings) = data.split("\n\n").map { it.split("\n") }

        // Parse the workflows and parts
        val workflows = workflowStrings.map { line -> parseWorkflow(line) }.associateBy { workflow -> workflow.name }
        val parts = partStrings.map { line -> parsePart(line) }

        // Filter the parts that are accepted by the workflows
        val acceptedParts = parts.filter { part -> applyWorkflows(workflows, part) == "A" }

        // Return the sum of the values of the accepted parts
        return acceptedParts.sumOf { part -> part.values.values.sum().toLong() }
    }

    fun partTwo(data: String): Long {
        // Split the data into workflows and parts
        val (workflowStrings) = data.split("\n\n").map { it.split("\n") }

        // Parse the workflows
        val workflows = workflowStrings.map { line -> parseWorkflow(line) }.associateBy { workflow -> workflow.name }
        // Apply the workflows to a range of parts
        val ranges =
            applyWorkflows(workflows, PartRange(mapOf('x' to 1..4000, 'm' to 1..4000, 'a' to 1..4000, 's' to 1..4000)))

        // Filter the parts that are accepted by the workflows
        val acceptedParts = ranges.entries.filter { (_, value) -> value == "A" }.map { (key, _) -> key }

        return acceptedParts.sumOf { partRange -> partRange.combinations() }
    }

    // Class representing a part : {x=310,m=3257,a=1233,s=1596}
    private data class Part(val values: Map<Char, Int>)

    // Class representing a range of parts (for part two)
    private data class PartRange(val ranges: Map<Char, IntRange>) {

        companion object {
            val EMPTY: PartRange = PartRange(
                mapOf(
                    'x' to IntRange.EMPTY,
                    'm' to IntRange.EMPTY,
                    'a' to IntRange.EMPTY,
                    's' to IntRange.EMPTY
                )
            )
        }

        // Function to get a new PartRange with values below a certain value
        fun below(category: Char, value: Int): PartRange {
            // Get the range for the category
            val range = ranges[category]!!
            // Create a new range with values below the given value
            val newRange = if (value in range) {
                IntRange(range.first, value - 1)
            } else if (value >= range.last) {
                range
            } else {
                IntRange.EMPTY
            }
            return PartRange(ranges.toMutableMap().apply { this[category] = newRange })
        }

        // Function to get a new PartRange with values above a certain value
        fun above(category: Char, value: Int): PartRange {
            // Get the range for the category
            val range = ranges[category]!!
            // Create a new range with values above the given value
            val newRange = if (value in range) {
                IntRange(value + 1, range.last)
            } else if (value <= range.first) {
                range
            } else {
                IntRange.EMPTY
            }
            return PartRange(ranges.toMutableMap().apply { this[category] = newRange })
        }

        // Function to calculate the number of combinations in the PartRange
        fun combinations() =
            (ranges['x']?.last!! - ranges['x']?.first!! + 1L) * (ranges['m']?.last!! - ranges['m']?.first!! + 1L) * (ranges['a']?.last!! - ranges['a']?.first!! + 1L) * (ranges['s']?.last!! - ranges['s']?.first!! + 1L)
    }


    /**
     * Class representing a workflow : px{a<2006:qkq,m>2090:A,rfg}
     * @param name : the name of the workflow (px in the example)
     * @param rules : the list of rules in the workflow ([Rule(2, '<', 2006, "qkq"), Rule(1, '>', 2090, "A"), Rule(-1, ' ', 0, "rfg")] in the example)
     */
    private data class Workflow(val name: String, val rules: List<Rule>)

    /**
     * Class representing a rule : a<2006:qkq => Rule(0, '<', 2006, "qkq")
     * @param category : the category to test ('X' if the rule is always satisfied)
     * @param comparison : the comparison to use (< or >)
     * @param rightValue : the value to compare to (2006 in the example)
     * @param destination : the destination workflow if the rule is satisfied (qkq in the example)
     */
    private data class Rule(val category: Char, val comparison: Char, val rightValue: Int, val destination: String) {
        // Function to test if a part satisfies the rule
        fun test(part: Part): Boolean {
            // If the category is 'X', the rule is always satisfied
            if (category == 'X') {
                return true
            }
            // Get the value for the category
            val leftValue = part.values[category]!!
            // Check if the value satisfies the comparison
            return when (comparison) {
                '<' -> leftValue < rightValue
                '>' -> leftValue > rightValue
                else -> throw IllegalArgumentException("Bad comparison $comparison")
            }
        }

        // Function to split a PartRange using the rule (for part two)
        fun splitBy(partRange: PartRange): Pair<PartRange, PartRange> {
            if (category == 'X') {
                return partRange to PartRange.EMPTY
            }
            // Split the PartRange based on the comparison
            return when (comparison) {
                '<' -> partRange.below(category, rightValue) to partRange.above(category, rightValue - 1)
                '>' -> partRange.above(category, rightValue) to partRange.below(category, rightValue + 1)
                else -> throw IllegalArgumentException("Bad comparison $comparison")
            }
        }
    }

    // Function to apply the workflows to a part
    private fun applyWorkflows(workflows: Map<String, Workflow>, part: Part): String {
        // Start with the workflow "in"
        var workflowName = "in"
        while (workflowName != "A" && workflowName != "R") {
            val workflow = workflows[workflowName]!!
            // Update the workflow by testing the part against each rule in the workflow
            // and getting the result of the first rule that the part satisfies
            workflowName = workflow.rules.first { it.test(part) }.destination
        }

        return workflowName
    }

    // This function applies the workflows to a PartRange (for part two)
    private fun applyWorkflows(workflows: Map<String, Workflow>, partRange: PartRange): Map<PartRange, String> {
        // Initialize a queue with the initial PartRange and the workflow "in"
        val queue = ArrayDeque<Pair<PartRange, String>>()
        queue += partRange to "in"
        // Initialize a mutable map to store the results
        val results = mutableMapOf<PartRange, String>()
        // Continue the loop until the queue is empty
        while (queue.isNotEmpty()) {
            // Remove the first element from the queue
            val element = queue.removeFirst()
            // Get the workflow from the map using the workflow name from the element
            val workflow = workflows[element.second]!!
            // Initialize the remaining range as the PartRange from the element
            var remainingRange = element.first
            // Loop over each rule in the workflow
            for (rule in workflow.rules) {
                val (t, f) = rule.splitBy(remainingRange)
                if (rule.destination == "A" || rule.destination == "R") {
                    results += t to rule.destination
                } else {
                    queue += t to rule.destination
                }
                remainingRange = f
            }
        }
        return results
    }

    // Function to parse a workflow from a string
    private fun parseWorkflow(line: String): Workflow {
        // Split the string into the name and the rules
        val (name, rulesString) = line.split('{', '}')
        // Parse the rules
        val ruleStrings = rulesString.split(',')

        return Workflow(name, ruleStrings.map { rule -> parseRule(rule) })
    }

    // Function to parse a rule from a string : a<2006:qkq,m>2090:A,rfg
    private fun parseRule(rule: String): Rule {
        // If the string does not contain a ':', return a rule that is always satisfied
        if (!rule.contains(':')) {
            // A rule that is always satisfied
            return Rule('X', ' ', 0, rule)
        }
        val category = rule[0]
        val comparison = rule[1]
        val split = rule.substring(2).split(':')
        val rightValue = split[0].toInt()
        val destination = split[1]

        return Rule(category, comparison, rightValue, destination)
    }

    // Function to parse a part from a string
    private fun parsePart(line: String): Part {
        val values = line.substring(1, line.length - 1).split(',').map { it.substring(2).toInt() }

        return Part(mapOf('x' to values[0], 'm' to values[1], 'a' to values[2], 's' to values[3]))
    }
}
