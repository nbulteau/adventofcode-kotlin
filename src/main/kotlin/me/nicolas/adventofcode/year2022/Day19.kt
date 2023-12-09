package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import kotlin.math.min

fun main() {
    val training = readFileDirectlyAsText("/year2022/day19/training.txt")
    val data = readFileDirectlyAsText("/year2022/day19/data.txt")

    val inputs = data.split("\n")

    val day = Day19(2022, 19, "Not Enough Minerals", inputs)
    prettyPrintPartOne { day.partOne(minutes = 24) }
    prettyPrintPartTwo { day.partTwo(minutes = 32) }
}


private class Day19(year: Int, day: Int, title: String, inputs: List<String>) :
    AdventOfCodeDay(year, day, title) {

    private data class Blueprint(
        val oreRobotOreCosts: Int,
        val clayRobotOreCosts: Int,
        val obsidianRobotOreCosts: Int,
        val obsidianRobotClayCosts: Int,
        val geodeRobotOreCosts: Int,
        val geodeRobotObsidianCosts: Int,
    )

    private val blueprints = buildBlueprint(inputs)

    private fun buildBlueprint(inputs: List<String>): List<Blueprint> {
        val regex =
            Regex("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.")
        return inputs.map { line ->
            val blueprint = regex.matchEntire(line)!!.groupValues.drop(1).map { it.toInt() }
            Blueprint(
                oreRobotOreCosts = blueprint[1],
                clayRobotOreCosts = blueprint[2],
                obsidianRobotOreCosts = blueprint[3],
                obsidianRobotClayCosts = blueprint[4],
                geodeRobotOreCosts = blueprint[5],
                geodeRobotObsidianCosts = blueprint[6]
            )
        }
    }

    fun partOne(minutes: Int): Int {

        return blueprints.mapIndexed { index, blueprint ->
            solve(
                blueprint.oreRobotOreCosts,
                blueprint.clayRobotOreCosts,
                blueprint.obsidianRobotOreCosts,
                blueprint.obsidianRobotClayCosts,
                blueprint.geodeRobotOreCosts,
                blueprint.geodeRobotObsidianCosts,
                minutes
            ) * (index + 1)
        }.sum()
    }

    fun partTwo(minutes: Int): Int {
        return blueprints.take(3).map { blueprint ->
            solve(
                blueprint.oreRobotOreCosts,
                blueprint.clayRobotOreCosts,
                blueprint.obsidianRobotOreCosts,
                blueprint.obsidianRobotClayCosts,
                blueprint.geodeRobotOreCosts,
                blueprint.geodeRobotObsidianCosts,
                minutes
            )
        }.reduce { acc, value -> acc * value }
    }

    private data class State(val resources: Resources, val robots: Robots, val minutes: Int)
    private data class Resources(val ore: Int, val clay: Int, val obsidian: Int, val openGeodes: Int)
    private data class Robots(val oreRobot: Int, val clayRobot: Int, val obsidianRobot: Int, val geodeRobot: Int)

    fun solve(
        oreRobotOreCosts: Int,
        clayRobotOreCosts: Int,
        obsidianRobotOreCosts: Int,
        obsidianRobotClayCosts: Int,
        geodeRobotOreCosts: Int,
        geodeRobotObsidianCosts: Int,
        minutes: Int,
    ): Int {
        var best = 0
        val queue = ArrayDeque<State>().apply {
            add(State(Resources(0, 0, 0, 0), Robots(1, 0, 0, 0), minutes))
        }
        val seen = mutableSetOf<State>()
        while (queue.isNotEmpty()) {
            val state = queue.removeFirst()
            val (resource, robots, time) = state
            var (ore, clay, obsidian, geodes) = resource
            var (oreRobot, clayRobot, obsidianRobot, geodeRobot) = robots

            best = maxOf(best, geodes)
            if (time == 0) {
                continue // elephants are hungry
            }

            if (state in seen) {
                continue
            }
            // Add all possible States
            seen.add(state)

            // Robots
            // Since we can only produce one robot per turn,
            // we don't need more material-collecting robots than the maximum of what is needed for any particular recipe
            val maxOreCosts = maxOf(oreRobotOreCosts, clayRobotOreCosts, obsidianRobotOreCosts, geodeRobotOreCosts)
            val maxClayCosts = obsidianRobotClayCosts
            val maxObsidianCosts = geodeRobotObsidianCosts

            oreRobot = min(oreRobot, maxOreCosts)
            clayRobot = min(clayRobot, maxClayCosts)
            obsidianRobot = min(obsidianRobot, maxObsidianCosts)

            // Resources
            ore = min(ore, maxOreCosts * time - oreRobot * (time - 1))
            clay = min(clay, maxClayCosts * time - clayRobot * (time - 1))
            obsidian = min(obsidian, maxObsidianCosts * time - obsidianRobot * (time - 1))

            // has enough ore and obsidian to build geode robot -> it is the optimal option
            if (ore >= geodeRobotOreCosts && obsidian >= geodeRobotObsidianCosts) {
                queue.add(
                    State(
                        Resources(
                            ore - geodeRobotOreCosts + oreRobot,
                            clay + clayRobot,
                            obsidian - geodeRobotObsidianCosts + obsidianRobot,
                            geodes + geodeRobot
                        ),
                        Robots(oreRobot, clayRobot, obsidianRobot, geodeRobot + 1),
                        time - 1
                    )
                )
            } else {
                queue.add(
                    State(
                        Resources(ore + oreRobot, clay + clayRobot, obsidian + obsidianRobot, geodes + geodeRobot),
                        Robots(oreRobot, clayRobot, obsidianRobot, geodeRobot),
                        time - 1
                    )
                )

                // has enough ore to build ore robot
                if (ore >= oreRobotOreCosts) {
                    queue.add(
                        State(
                            Resources(
                                ore - oreRobotOreCosts + oreRobot,
                                clay + clayRobot,
                                obsidian + obsidianRobot,
                                geodes + geodeRobot
                            ),
                            Robots(oreRobot + 1, clayRobot, obsidianRobot, geodeRobot),
                            time - 1
                        )
                    )
                }
                // has enough ore to build clay robot
                if (ore >= clayRobotOreCosts) {
                    queue.add(
                        State(
                            Resources(
                                ore - clayRobotOreCosts + oreRobot,
                                clay + clayRobot,
                                obsidian + obsidianRobot,
                                geodes + geodeRobot
                            ),
                            Robots(
                                oreRobot, clayRobot + 1, obsidianRobot, geodeRobot
                            ),
                            time - 1
                        )
                    )
                }
                // has enough ore and clay to build obsidian robot
                if (ore >= obsidianRobotOreCosts && clay >= obsidianRobotClayCosts) {
                    queue.add(
                        State(
                            Resources(
                                ore - obsidianRobotOreCosts + oreRobot,
                                clay - obsidianRobotClayCosts + clayRobot,
                                obsidian + obsidianRobot,
                                geodes + geodeRobot
                            ),
                            Robots(oreRobot, clayRobot, obsidianRobot + 1, geodeRobot),
                            time - 1
                        )
                    )
                }
            }
        }
        return best
    }
}


