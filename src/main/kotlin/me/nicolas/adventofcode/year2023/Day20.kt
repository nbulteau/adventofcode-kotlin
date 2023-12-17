package me.nicolas.adventofcode.year2023

import me.nicolas.adventofcode.utils.*

fun main() {
    val data = readFileDirectlyAsText("/year2023/day20/data.txt")
    val day = Day20(2023, 20, "")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day20(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
    fun partOne(data: String): Long {
        val board = parseBoard(data)

        var lowPulses = 0L
        var highPulses = 0L
        repeat(1000) {
            val (deltaLowPulses, deltaHighPulses) = pressButton(board)
            lowPulses += deltaLowPulses
            highPulses += deltaHighPulses
        }

        return lowPulses * highPulses
    }

    fun partTwo(data: String): Long {
        val board = parseBoard(data)

        var press = 1L
        val cycles = HashMap<String, Long>()

        while (true) {
            val toWork = ArrayList<Pulse>()
            toWork.add(Pulse("broadcaster", "", false))

            while (toWork.isNotEmpty()) {
                val pulse = toWork.removeFirst()

                // The machine turns on when a single low pulse is sent to rx. (&gh -> rx)
                val conjunctionModule = (board["gh"]!! as ConjunctionModule)
                if (conjunctionModule.conjInputStatesHigh.contains(pulse.target)) {
                    val foo = conjunctionModule.conjInputStatesHigh[pulse.target]!!
                    if (!cycles.contains(pulse.target) && foo) {
                        cycles[pulse.target] = press
                    }
                    if (cycles.keys == conjunctionModule.conjInputStatesHigh.keys) {
                        println("cycles = $cycles")
                        return cycles.values.reduce { acc, cycle -> acc.lcm(cycle) }
                    }
                }

                val module = board[pulse.target] ?: continue

                module.processPulse(pulse).forEach { newPulse ->
                    toWork.add(newPulse)
                }
            }

            press++
        }
    }

    // A pulse is a message that is sent from one module to another.
    data class Pulse(val target: String, val from: String, val isHigh: Boolean)

    // Module types are: % (flip-flop), & (conjunction), and anything else (broadcaster)
    sealed class Module(
        open val name: String,
        open val targets: List<String>,
    ) {

        // Process a pulse and return a list of pulses to be sent to the targets
        abstract fun processPulse(pulse: Pulse): List<Pulse>
    }

     data class FlipFlopModule(
        override val name: String,
        override val targets: List<String>,
        var flipFlopState: Boolean = false,
    ) : Module(name, targets) {

         // If a flip-flop module receives a high pulse, it is ignored and nothing happens.
         // However, if a flip-flop module receives a low pulse, it flips between on and off.
         // If it was off, it turns on and sends a high pulse. If it was on, it turns off and sends a low pulse.

         override fun processPulse(pulse: Pulse): List<Pulse> {
            return if (pulse.isHigh) {
                emptyList()
            } else {
                val sendHigh = !flipFlopState
                flipFlopState = !flipFlopState
                targets.map { target ->
                    Pulse(target, name, sendHigh)
                }
            }
        }
    }

    data class ConjunctionModule(
        override val name: String,
        override val targets: List<String>,
        // Map of input module name to its state
        val conjInputStatesHigh: MutableMap<String, Boolean> = mutableMapOf(),
    ) : Module(name, targets) {

        // When a pulse is received, the conjunction module first updates its memory for that input.
        // Then, if it remembers high pulses for all inputs, it sends a low pulse; otherwise, it sends a high pulse.

        override fun processPulse(pulse: Pulse): List<Pulse> {
            conjInputStatesHigh[pulse.from] = pulse.isHigh
            val sendHigh = !conjInputStatesHigh.all { (_, value) -> value }
            return targets.map { target ->
                Pulse(target, name, sendHigh)
            }
        }
    }

    data class BroadcasterModule(
        override val name: String,
        override val targets: List<String>,
    ) : Module(name, targets) {

        // When it receives a pulse, it sends the same pulse to all of its destination modules.
        override fun processPulse(pulse: Pulse): List<Pulse> {
            return targets.map { target ->
                Pulse(target, name, pulse.isHigh)
            }
        }
    }

    // Press the button and return the number of low and high pulses sent out by the broadcaster. (Part One)
    private fun pressButton(board: Map<String, Module>): Pair<Long, Long> {
        // Start with a single low pulse sent to the broadcaster module
        val toWork = ArrayList<Pulse>()
        toWork.add(Pulse("broadcaster", "", false))

        var lowPulseCount = 0L
        var highPulsesCount = 0L

        // Process all the pulses until there is no more to process
        while (toWork.isNotEmpty()) {
            val pulse = toWork.removeFirst()

            if (pulse.isHigh) {
                highPulsesCount++
            } else {
                lowPulseCount++
            }

            val module = board[pulse.target] ?: continue

            // Add the new pulses to the queue to be processed
            module.processPulse(pulse).forEach { newPulse ->
                toWork.add(newPulse)
            }
        }

        return Pair(lowPulseCount, highPulsesCount)
    }

    // Parse the board and return a map of module name to module.
    private fun parseBoard(data: String): Map<String, Module> {
        val modules = data.lines().map { line ->
            val parts = line.split(" -> ")

            val name = parts[0].replace("%", "").replace("&", "")
            val targets = parts[1].split(", ")
            when (parts[0][0]) {
                '%' -> FlipFlopModule(name, targets)
                '&' -> ConjunctionModule(name, targets)
                else -> BroadcasterModule(name, targets)
            }
        }

        // Initialize the conjunctions modules with all the inputs to false
        modules.filterIsInstance<ConjunctionModule>()
            .forEach { module ->
                modules.filter { fromModule -> fromModule.targets.contains(module.name) }
                    .forEach { fromModule ->
                        module.conjInputStatesHigh[fromModule.name] = false
                    }
            }

        // Return a map of module name to module instance for easy lookup later
        return modules.associateBy { module -> module.name }
    }
}

