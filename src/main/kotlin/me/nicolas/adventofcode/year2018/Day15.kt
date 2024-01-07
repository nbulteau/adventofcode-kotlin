package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.*


// https://adventofcode.com/2018/day/15
fun main() {
    val data = readFileDirectlyAsText("/year2018/day15/data.txt")
    val day = Day15(2018, 15, "Beverage Bandits")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day15(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): Int {
        val (cave, units) = parseData(data)

        var rounds = 0
        while (processRound(cave, units)) {
            //printCave(cave)
            rounds++
        }

        return units
            .filterNot { unit -> unit.dead }
            .sumOf { unit -> unit.hitPoints } * rounds
    }

    fun partTwo(data: String): Int {
        var elfAttackPoints = 3
        while (true) {
            val (cave, units) = parseData(data, elfAttackPoints)

            var rounds = 0
            while (processRound(cave, units)) {
                //printCave(cave)
                rounds++
            }

            if (units.filter { unit -> unit.team == Team.Elf }.none { unit -> unit.dead }) {
                // println("rounds: $rounds, units: ${units.map { unit -> "${unit.team} = ${unit.hitPoints}" }}, elfAttackPoints: $elfAttackPoints")
                return units
                    .filterNot { unit -> unit.dead }
                    .sumOf { unit -> unit.hitPoints } * rounds
            }
            elfAttackPoints++
        }
    }

    // debug
    private fun printCave(cave: Array<CharArray>) {
        cave.forEach { row ->
            row.forEach { c ->
                print(c)
            }
            println()
            println()
        }
    }

    // Process a single round of combat (return false if combat is over)
    private fun processRound(cave: Array<CharArray>, units: List<Unit>): Boolean {

        // Sort units in reading order (important for pathfinding) and process them one at a time
        for (unit in units.sorted()) {

            // Skip dead units (they don't get to move or attack)
            if (unit.dead) {
                continue
            }
            // Check if combat is over before we start the round (we might have killed the last enemy)
            if (units.filterNot { it.dead }.distinctBy { it.team }.count() <= 1) {
                return false
            }

            // Is there a target in range? Since it is not in range of a target, it moves.
            var target = unit.getTargetsInRange(units).firstOrNull()
            if (target == null) {
                // Move to best spot
                val path: List<Point> = unit.findPathToBestEnemyAdjacentSpot(units, cave)
                // If we found a path, move to the first step in it (which is the closest spot)
                if (path.isNotEmpty()) {
                    unit.moveTo(path.first(), cave)
                }
                // Find a target again now that we've moved
                target = unit.getTargetsInRange(units).firstOrNull()
            }

            // Fight if we have a target
            if (target != null) {
                // Attack the target with the lowest hit points (reading order)
                unit.attack(target)
                if (target.dead) {
                    // Remove dead unit from the cave
                    cave[target.location.y][target.location.x] = '.'
                }
            }
        }

        // Combat is not over yet (we didn't kill the last enemy) so return true
        return true
    }

    // Parse the input data into a cave and a list of units
    private fun parseData(data: String, elfAttackPoints: Int = 3): Pair<Array<CharArray>, List<Unit>> {
        // Convert the input data into a cave (2D array of chars)
        val cave = data.lines().map { row -> row.toCharArray() }.toTypedArray()
        // Loop over the cave and find all units
        val units = mutableListOf<Unit>()
        cave.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                when (c) {
                    'E' -> units.add(Unit(Team.Elf, Point(x, y), attackPoints = elfAttackPoints))
                    'G' -> units.add(Unit(Team.Goblin, Point(x, y)))
                }
            }
        }

        return cave to units
    }

    private enum class Team(val label: Char) {
        Elf('E'),
        Goblin('G');
    }

    // Unit is Comparable so we can sort them in reading order (which is important for pathfinding)
    private data class Unit(
        val team: Team,
        var location: Point,
        var hitPoints: Int = 200,
        var attackPoints: Int = 3,
        var dead: Boolean = false,
    ) : Comparable<Unit> {

        // Attack the target
        fun attack(target: Unit) {
            target.hitPoints -= this.attackPoints
            // If the target is dead, mark it as such
            if (target.hitPoints <= 0) {
                target.dead = true
            }
        }

        // Find all enemies in range (reading order)
        fun getTargetsInRange(others: List<Unit>): List<Unit> =
            others.filter { unit ->
                unit != this &&
                        !this.dead &&
                        !unit.dead &&
                        unit.team != this.team &&
                        this.location.distanceTo(unit.location) == 1
            }
                .sortedWith(compareBy({ it.hitPoints }, { it.location }))

        // Find the best spot to move to (reading order) to get in range of an enemy (or empty if none found)
        fun findPathToBestEnemyAdjacentSpot(units: List<Unit>, cave: Array<CharArray>): List<Point> {
            val enemiesAdjacentOpenSpots = getAllEnemiesAdjacentOpenSpots(units, cave)

            return pathToAllEnemies(enemiesAdjacentOpenSpots, cave)
        }

        // Move to the given location (and update the cave)
        fun moveTo(place: Point, caves: Array<CharArray>) {
            caves[location.y][location.x] = '.'
            location = place
            caves[location.y][location.x] = team.label
        }

        // Get all open spots adjacent to an enemy (reading order)
        private fun getAllEnemiesAdjacentOpenSpots(units: List<Unit>, cave: Array<CharArray>): Set<Point> =
            units
                .filterNot { unit -> unit.dead } // Skip dead units
                .filterNot { unit -> unit.team == team } // Skip units on our team
                .flatMap { unit ->
                    unit.location.cardinalNeighbors().filter { neighbor -> cave[neighbor.y][neighbor.x] == '.' }
                }
                .toSet()

        override fun compareTo(other: Unit): Int =
            location.compareTo(other.location)

        // Find a path to any of the given enemies (or empty if none found) using BFS
        private fun pathToAllEnemies(enemies: Set<Point>, caves: Array<CharArray>): List<Point> {
            val seen = mutableSetOf(location)
            val paths = ArrayDeque<List<Point>>()

            // Seed the queue with each of our neighbors, in reading order (that's important)
            location.cardinalNeighbors()
                .filter { point -> caves[point.y][point.x] == '.' }
                .forEach { point -> paths.add(listOf(point)) }

            // While there are still paths to explore, take the first one off the queue
            while (paths.isNotEmpty()) {
                val path: List<Point> = paths.removeFirst()
                val pathEnd: Point = path.last()

                // If this path ends at an enemy, we're done! Return the path.
                if (pathEnd in enemies) {
                    return path
                }

                // Otherwise, add all of the neighbors of the path's end to the queue (if we haven't seen them before)
                if (pathEnd !in seen) {
                    seen.add(pathEnd)
                    pathEnd.cardinalNeighbors()
                        .filter { point -> caves[point.y][point.x] == '.' }
                        .filterNot { point -> point in seen }
                        .forEach { point -> paths.add(path + point) }
                }
            }

            return emptyList()
        }
    }
}