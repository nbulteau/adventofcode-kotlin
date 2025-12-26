package me.nicolas.adventofcode.year2019

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 25: Cryostasis ---
// https://adventofcode.com/2019/day/25
@FlowPreview
fun main() {
    val data = readFileDirectlyAsText("/year2019/day25/data.txt")
    val day = Day25(2019, 25)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

@FlowPreview
class Day25(year: Int, day: Int, title: String = "Cryostasis") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String, print: Boolean = true): Int {
        val program: MutableMap<Long, Long> = data
            .split(",")
            .withIndex()
            .associateTo(mutableMapOf()) { it.index.toLong() to it.value.toLong() }

        playGame(program)

        return 0
    }

    fun partTwo(data: String): Int {
        // No meaningful second part for this setup in the repo; keep consistent with tests
        return 0
    }

    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    fun playGame(program: MutableMap<Long, Long>) = runBlocking {
        val computer = NIC(
            memory = program.copyOf(),
            output = Channel(Channel.UNLIMITED)
        ).also {
            launch { it.start() }
        }
        launch {
            while (!computer.output.isClosedForReceive) {
                computer.output.receive()
                while (!computer.output.isEmpty) {
                    if (computer.output.isClosedForReceive) {
                        return@launch
                    }
                    print(computer.output.receive().toInt().toChar())
                }
                if (!computer.output.isClosedForReceive) {
                    withContext(Dispatchers.Default) {
                        "${readln()}\n"
                    }.forEach { c -> computer.input.send(c.code.toLong()) }
                }
            }
        }
    }
}