package me.nicolas.adventofcode.year2021

import me.nicolas.adventofcode.blue
import me.nicolas.adventofcode.green
import me.nicolas.adventofcode.prettyPrint
import me.nicolas.adventofcode.readFileDirectlyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

// https://adventofcode.com/2021/day/11
@OptIn(ExperimentalTime::class)
fun main() {

    val training = readFileDirectlyAsText("/year2021/day11/training.txt")
    val data = readFileDirectlyAsText("/year2021/day11/data.txt")

    val inputs = data.split("\n")
    val lines = inputs.map { line -> line.toList().map { point -> point.toString().toInt() } }

    prettyPrint(
        message = "Part one answer",
        measureTimedValue { Day11().partOne(lines) })

    prettyPrint(
        message = "Part two answer",
        measureTimedValue { Day11().partTwo(lines) })
}

class Day11 {

    companion object {
        private const val gridSize = 10
    }

    class Octopus(initialEnergyLevel: Int) {

        var energyLevel: Int = initialEnergyLevel
            private set

        var hasFlashDuringStep: Boolean = false
            private set

        fun setEnergyLevelTo0IfHasFlashDuringStep() {
            if (hasFlashDuringStep) {
                energyLevel = 0
            }
        }

        fun incEnergyLevelByOne() {
            energyLevel++
        }

        fun flash() {
            hasFlashDuringStep = true
        }

        fun isFlashingOctopus(): Boolean {
            return !hasFlashDuringStep && (energyLevel > 9)
        }

        fun resetHasFlashDuringStep() {
            hasFlashDuringStep = false
        }

        override fun toString() = if (energyLevel == 0) blue(energyLevel) else green(energyLevel)
    }

    class Grid(private val octopuses: List<Octopus>) {

        var nbFlashes = 0
            private set

        private val adjacentLocations = mutableMapOf<Octopus, List<Octopus>>()

        init {
            octopuses.forEachIndexed() { index, octopus ->
                adjacentLocations[octopus] = adjacentLocations(index)
            }
        }

        fun processStep() {
            incEnergyLevels()

            var flashingOctopuses = flashingOctopuses().toMutableList()
            while (flashingOctopuses.isNotEmpty()) {
                val octopus = flashingOctopuses.first()
                flashingOctopuses.remove(octopus)
                nbFlashes++
                octopus.flash()

                // increases the energy level of all adjacent octopuses by 1
                adjacentLocations[octopus]?.forEach { adjacentOctopus -> adjacentOctopus.incEnergyLevelByOne() }

                flashingOctopuses = flashingOctopuses().toMutableList()
            }

            // any octopus that flashed during this step has its energy level set to 0
            octopuses.forEach { octopus -> octopus.setEnergyLevelTo0IfHasFlashDuringStep() }
            resetOctopusesStates()
        }

        fun display() {
            octopuses.forEachIndexed { index, octopus ->
                print(octopus)
                if (index % gridSize == gridSize - 1) {
                    print("\n")
                }
            }
            println()
        }

        private fun flashingOctopuses(): List<Octopus> {
            return octopuses.filter { octopus ->
                octopus.isFlashingOctopus()
            }
        }

        fun allFlash(): Boolean {
            return octopuses.all { octopus -> octopus.energyLevel == 0 }
        }

        private fun resetOctopusesStates() {
            octopuses.forEach { octopus -> octopus.resetHasFlashDuringStep() }
        }

        private fun incEnergyLevels() {
            octopuses.forEach { octopus -> octopus.incEnergyLevelByOne() }
        }

        private fun adjacentLocations(index: Int): List<Octopus> {
            val adjacentLocations = mutableListOf<Octopus>()
            val x = index % gridSize
            val y = index / gridSize

            // up
            if (y > 0) {
                adjacentLocations.add(octopuses[index - 10])
                // up left
                if (x > 0) {
                    adjacentLocations.add(octopuses[index - 11])
                }
                // up right
                if (x < gridSize - 1) {
                    adjacentLocations.add(octopuses[index - 9])
                }
            }
            // down
            if (y < gridSize - 1) {
                adjacentLocations.add(octopuses[index + 10])
                // down left
                if (x > 0) {
                    adjacentLocations.add(octopuses[index + 9])
                }
                // down right
                if (x < gridSize - 1) {
                    adjacentLocations.add(octopuses[index + 11])
                }
            }
            // left
            if (x > 0) {
                adjacentLocations.add(octopuses[index - 1])
            }
            // right
            if (x < gridSize - 1) {
                adjacentLocations.add(octopuses[index + 1])
            }
            return adjacentLocations
        }
    }

    fun partOne(lines: List<List<Int>>): Int {

        val octopuses = lines.flatMap { row -> row.map { energy -> Octopus(energy) } }
        val grid = Grid(octopuses)

        for (index in 1..100) {
            grid.processStep()
        }
        println("After step 100:")
        grid.display()
        return grid.nbFlashes
    }

    fun partTwo(lines: List<List<Int>>): Int {

        val octopuses = lines.flatMap { row -> row.map { energy -> Octopus(energy) } }
        val grid = Grid(octopuses)

        var index = 0
        do {
            index++
            grid.processStep()
            if (grid.allFlash()) {
                break
            }
        } while (true)

        println("After step $index:")
        grid.display()
        return index
    }
}


