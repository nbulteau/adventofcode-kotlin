package me.nicolas.adventofcode.year2019

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
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

    // Build the Intcode program map and start automatic exploration
    val program: MutableMap<Long, Long> = data
        .split(",")
        .withIndex()
        .associateTo(mutableMapOf()) { it.index.toLong() to it.value.toLong() }

    day.exploreAll(program)
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

    /**
     * Explore the entire map automatically by driving the droid programmatically.
     * Builds a simple coordinate map and prints it when finished.
     */
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    fun exploreAll(program: MutableMap<Long, Long>) = runBlocking {
        val computer = NIC(
            memory = program.copyOf(),
            output = Channel(Channel.UNLIMITED)
        )

        launch { try { computer.start() } catch (_: Throwable) { /* ignore NIC errors */ } }

        // Helper to read until the next "Command?" prompt (returns full text)
        suspend fun readUntilPrompt(): String {
            val sb = StringBuilder()
            try {
                while (true) {
                    val v = computer.output.receive()
                    val c = v.toInt().toChar()
                    sb.append(c)
                    if (sb.endsWith("Command?\n")) break
                }
            } catch (e: ClosedReceiveChannelException) {
                // Return whatever we accumulated when the output channel closed.
                return sb.toString()
            }
            return sb.toString()
        }

        // Helper to send an ASCII command (adds terminating newline)
        suspend fun sendCommand(cmd: String) {
            if (computer.input.isClosedForSend) return
            ("$cmd\n").forEach { ch ->
                if (computer.input.isClosedForSend) return
                try {
                    computer.input.send(ch.code.toLong())
                } catch (_: Throwable) {
                    // channel closed or cancelled; ignore remaining sends
                    return
                }
            }
        }

        // Direction helpers
        val deltas = mapOf("north" to (0 to -1), "south" to (0 to 1), "west" to (-1 to 0), "east" to (1 to 0))
        val reverse = mapOf("north" to "south", "south" to "north", "west" to "east", "east" to "west")
        // Replace Room to include items
        data class Room(val name: String, val text: String, val items: List<String> = emptyList())

        val visited = mutableMapOf<Pair<Int, Int>, Room>()
        val edges = mutableMapOf<Pair<Int, Int>, MutableMap<String, Pair<Int, Int>>>()

        val pos = 0 to 0

        // Read initial welcome and first prompt
        val initial = try {
            readUntilPrompt()
        } catch (e: ClosedReceiveChannelException) {
            ""
        }
        // parse initial room if any
        fun parseRoom(text: String): Room {
            // Room name is on a line starting and ending with ==
            val nameLine = text.lineSequence().firstOrNull { it.startsWith("==") && it.endsWith("==") }?.trim('=', ' ') ?: "<unknown>"

            // Parse items: look for a section starting with "Items here:" then lines starting with "- "
            val lines = text.lineSequence().toList()
            val idxItems = lines.indexOfFirst { it.trim() == "Items here:" }
            val items = if (idxItems == -1) {
                emptyList()
            } else {
                val res = mutableListOf<String>()
                for (i in idxItems + 1 until lines.size) {
                    val line = lines[i].trim()
                    if (line.isEmpty()) break
                    if (line.startsWith("- ")) res.add(line.removePrefix("- ").trim())
                    else break
                }
                res
            }

            return Room(nameLine, text, items)
        }

        if (initial.isNotEmpty()) visited[pos] = parseRoom(initial)

        fun availableDoors(text: String): List<String> {
            val lines = text.lineSequence().toList()
            val idx = lines.indexOfFirst { it.trim() == "Doors here lead:" }
            if (idx == -1) return emptyList()
            val doors = mutableListOf<String>()
            for (i in idx + 1 until lines.size) {
                val line = lines[i].trim()
                if (line.isEmpty()) break
                if (line.startsWith("- ")) doors.add(line.removePrefix("- ").trim())
                else break
            }
            return doors
        }

        // DFS exploration
        suspend fun dfs(current: Pair<Int, Int>) {
            val room = visited[current] ?: return
            val text = room.text
            val doors = availableDoors(text)
            edges.getOrPut(current) { mutableMapOf() }
            for (dir in doors) {
                val delta = deltas[dir] ?: continue
                val neighbor = (current.first + delta.first) to (current.second + delta.second)
                if (visited.containsKey(neighbor)) {
                    // record edge and continue
                    edges[current]!![dir] = neighbor
                    continue
                }

                // move
                sendCommand(dir)
                val out = readUntilPrompt()
                val newRoom = parseRoom(out)
                visited[neighbor] = newRoom
                edges[current]!![dir] = neighbor
                edges.getOrPut(neighbor) { mutableMapOf() }[reverse[dir]!!] = current

                // recurse
                dfs(neighbor)

                // backtrack
                sendCommand(reverse[dir]!!)
                readUntilPrompt()
            }
        }

        try {
            try {
                dfs(pos)
            } catch (e: ClosedReceiveChannelException) {
                // NIC output closed while reading inside dfs; ignore and proceed to print what we have
            }

            // Print simple map based on room coordinates
            val xs = visited.keys.map { it.first }
            val ys = visited.keys.map { it.second }
            val minX = xs.minOrNull() ?: 0
            val maxX = xs.maxOrNull() ?: 0
            val minY = ys.minOrNull() ?: 0
            val maxY = ys.maxOrNull() ?: 0

            println("\nExplored rooms: ${visited.size}")

            // ASCII grid with room names and items
            val cellWidth = 28
            val cellHeight = 3

            fun padCenter(s: String, width: Int): String {
                if (s.length >= width) return s.substring(0, width)
                val left = (width - s.length) / 2
                val right = width - s.length - left
                return " ".repeat(left) + s + " ".repeat(right)
            }

            fun formatCell(x: Int, y: Int): List<String> {
                val room = visited[x to y]
                if (room == null) {
                    return List(cellHeight) { " ".repeat(cellWidth) }
                }
                // Line 0: room name (trimmed)
                val name = room.name
                val line0 = if (x to y == (0 to 0)) "$name (S)" else name
                val line0f = padCenter(line0.take(cellWidth), cellWidth)
                // Line 1: items joined, truncated
                val itemsText = if (room.items.isEmpty()) "" else room.items.joinToString(", ")
                val line1f = padCenter(itemsText.take(cellWidth), cellWidth)
                // Line 2: coordinates
                val coordText = "($x,$y)"
                val line2f = padCenter(coordText, cellWidth)
                return listOf(line0f, line1f, line2f)
            }

            // Header: x labels
            val header = StringBuilder()
            header.append(" ".repeat(6)) // left margin for y labels
            for (x in minX..maxX) {
                val label = "x=$x"
                header.append(padCenter(label, cellWidth))
            }
            println(header.toString())

            // Horizontal separator
            val sep = "-".repeat(6 + (maxX - minX + 1) * cellWidth)
            println(sep)

            for (y in minY..maxY) {
                // For each sub-line of the cell height
                for (sub in 0 until cellHeight) {
                    val rowSb = StringBuilder()
                    // left y label on first subline
                    if (sub == 1) rowSb.append(String.format("y=%3d ", y)) else rowSb.append("      ")
                    for (x in minX..maxX) {
                        rowSb.append(formatCell(x, y)[sub])
                    }
                    println(rowSb.toString())
                }
                println(sep)
            }

            // Optionally print room names with coords and items
            println("\nRooms (coord -> name [items]):")
            visited.forEach { (coord, r) ->
                if (r.items.isEmpty()) println("$coord -> ${r.name}")
                else println("$coord -> ${r.name} [${r.items.joinToString(", ")}]")
            }
        } catch (e: Throwable) {
            // Catch anything unexpected during exploration to avoid crashing the process
            println("\nExploration aborted due to: ${e::class.simpleName}: ${e.message}")
            println("Explored rooms so far: ${visited.size}")
            println("Rooms (coord -> name):")
            visited.forEach { (coord, r) -> println("$coord -> ${r.name}") }
        } finally {
            try {
                if (!computer.input.isClosedForSend) computer.input.close()
            } catch (_: Exception) {
                // ignore
            }
        }
    }
}

/*
                  x=0                         x=1                         x=2                         x=3                         x=4
--------------------------------------------------------------------------------------------------------------------------------------------------
                                      Gift Wrapping Center
y= -4                                     molten lava
                                             (1,-4)
--------------------------------------------------------------------------------------------------------------------------------------------------
                                          Science Lab                   Corridor                                                Storage
y= -3                                       asterisk                 infinite loop                                              photons
                                             (1,-3)                      (2,-3)                                                  (4,-3)
--------------------------------------------------------------------------------------------------------------------------------------------------
                                     Warp Drive Maintenance             Kitchen                     Sick Bay                 Crew Quarters
y= -2                                    spool of cat6                                                sand                giant electromagnet
                                             (1,-2)                      (2,-2)                      (3,-2)                      (4,-2)
--------------------------------------------------------------------------------------------------------------------------------------------------
                                          Engineering            Hot Chocolate Fountain              Arcade                     Holodeck
y= -1                                                                     mug                     festive hat                  tambourine
                                             (1,-1)                      (2,-1)                      (3,-1)                      (4,-1)
--------------------------------------------------------------------------------------------------------------------------------------------------
            Hull Breach (S)                 Hallway
y=  0
                 (0,0)                       (1,0)
--------------------------------------------------------------------------------------------------------------------------------------------------
                                            Passages
y=  1                                      escape pod
                                             (1,1)
--------------------------------------------------------------------------------------------------------------------------------------------------

 */