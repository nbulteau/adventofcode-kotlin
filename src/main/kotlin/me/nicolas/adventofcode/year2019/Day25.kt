package me.nicolas.adventofcode.year2019

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

// --- Day 25: Cryostasis ---
// https://adventofcode.com/2019/day/25
fun main() {
    val data = readFileDirectlyAsText("/year2019/day25/data.txt")
    val day = Day25(2019, 25)
    prettyPrintPartOne { day.partOne(data) }
}

class Day25(year: Int, day: Int, title: String = "Cryostasis") : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String, interactive: Boolean = false): String {
        val program: MutableMap<Long, Long> = data
            .split(",")
            .withIndex()
            .associateTo(mutableMapOf()) { it.index.toLong() to it.value.toLong() }

        if (interactive) {
            println("Running in INTERACTIVE mode (playGame). Use commands like 'north', 'take <item>', 'inv')")
            playGame(program)
            return ""
        } else {
            return exploreAll(program)
        }
    }

    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    fun playGame(program: MutableMap<Long, Long>) = runBlocking {
        val computer = NIC(
            memory = program.copyOf(),
            output = Channel(Channel.UNLIMITED)
        ).also { launch { it.start() } }

        // relay output to stdout and forward stdin lines to the NIC
        launch {
            try {
                while (!computer.output.isClosedForReceive) {
                    val v = computer.output.receive()
                    print(v.toInt().toChar())
                    while (!computer.output.isEmpty) print(computer.output.receive().toInt().toChar())
                    if (!computer.output.isClosedForReceive) {
                        withContext(Dispatchers.Default) { "${readln()}\n" }
                            .forEach { c -> computer.input.send(c.code.toLong()) }
                    }
                }
            } catch (_: ClosedReceiveChannelException) {
                // ignore
            }
        }
    }


    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    fun exploreAll(program: MutableMap<Long, Long>): String = runBlocking {
        val computer = NIC(
            memory = program.copyOf(),
            output = Channel(Channel.UNLIMITED)
        )

        launch {
            try {
                computer.start()
            } catch (_: Throwable) { /* ignore NIC start errors */
            }
        }

        suspend fun readUntilPrompt(): String {
            val sb = StringBuilder()
            try {
                while (true) {
                    val v = computer.output.receive()
                    sb.append(v.toInt().toChar())
                    if (sb.endsWith("Command?\n")) break
                }
            } catch (_: ClosedReceiveChannelException) {
                return sb.toString()
            }
            return sb.toString()
        }

        suspend fun sendCommand(cmd: String) {
            if (computer.input.isClosedForSend) return
            ("$cmd\n").forEach { ch ->
                if (computer.input.isClosedForSend) return
                try {
                    computer.input.send(ch.code.toLong())
                } catch (_: Throwable) {
                    return
                }
            }
        }

        val reverse = mapOf("north" to "south", "south" to "north", "west" to "east", "east" to "west")

        data class Room(
            val name: String,
            val text: String,
            val items: List<String> = emptyList(),
            val doors: List<String> = emptyList()
        )

        // graph structures
        val visited = mutableMapOf<Int, Room>()
        val edges = mutableMapOf<Int, MutableMap<String, Int>>()
        var nextId = 0
        val startId = nextId++

        // local BFS helper that uses `edges`
        fun findPath(start: Int, goal: Int): List<String>? {
            if (start == goal) return emptyList()
            val q = ArrayDeque<Int>()
            val prev = mutableMapOf<Int, Pair<Int, String>>()
            q.add(start)
            val seen = mutableSetOf(start)
            while (q.isNotEmpty()) {
                val cur = q.removeFirst()
                val neigh = edges[cur] ?: continue
                for ((dir, nid) in neigh) {
                    if (nid in seen) continue
                    seen.add(nid)
                    prev[nid] = cur to dir
                    if (nid == goal) {
                        val path = mutableListOf<String>()
                        var node = nid
                        while (node != start) {
                            val (p, d) = prev[node]!!
                            path.add(0, d)
                            node = p
                        }
                        return path
                    }
                    q.add(nid)
                }
            }
            return null
        }

        // logging
        val logSb = StringBuilder()
        fun dbg(msg: String, debug: Boolean = false) {
            if (debug && msg.startsWith("DEBUG: ")) {
                logSb.append(msg).append('\n')
            }
        }

        fun parseRoom(text: String): Room {
            val nameLine = text.lineSequence().map { it.trimEnd() }
                .firstOrNull { it.trim().startsWith("==") && it.trim().endsWith("==") }
            val name =
                nameLine?.let { Regex("^==\\s*(.+?)\\s*==\\s*$").find(it.trim())?.groupValues?.get(1) } ?: "<unknown>"

            val lines = text.lineSequence().toList()
            val idxItems = lines.indexOfFirst { it.trim().equals("Items here:", ignoreCase = true) }
            val items = if (idxItems == -1) emptyList() else run {
                val res = mutableListOf<String>()
                for (i in idxItems + 1 until lines.size) {
                    val line = lines[i].trim()
                    if (line.isEmpty()) break
                    if (line.startsWith("- ")) res.add(line.removePrefix("- ").trim()) else break
                }
                res
            }

            val idxDoors = lines.indexOfFirst { it.trim().equals("Doors here lead:", ignoreCase = true) }
            val doors = if (idxDoors == -1) emptyList() else run {
                val res = mutableListOf<String>()
                for (i in idxDoors + 1 until lines.size) {
                    val line = lines[i].trim()
                    if (line.isEmpty()) break
                    if (line.startsWith("- ")) res.add(line.removePrefix("- ").trim().lowercase()) else break
                }
                res
            }

            if (name == "<unknown>") dbg(
                "DEBUG: could not parse room name from output snippet: ${
                    text.lines().take(6).joinToString(" ")
                }"
            )
            return Room(name, text, items, doors)
        }

        // initialize starting room
        val initial = try {
            readUntilPrompt()
        } catch (_: ClosedReceiveChannelException) {
            ""
        }
        if (initial.isNotEmpty()) {
            val r = parseRoom(initial)
            visited[startId] = r
            dbg("DEBUG: startId=${0} -> ${r.name} [${r.items.joinToString(", ")}]")
        }

        fun generateDotFile(edges: Map<Int, Map<String, Int>>, visited: Map<Int, Room>) {
            try {
                val path = Paths.get("src/main/resources/me/nicolas/adventofcode/year2019/day25/day25-explored-map.dot")
                val sb = StringBuilder()
                sb.appendLine("digraph G {")
                for ((fromId, map) in edges) {
                    val fromRoom = visited[fromId]
                    val fromLabel = fromRoom?.name?.replace("\"", "\\\"") ?: "room_$fromId"
                    for ((dir, toId) in map) {
                        val toRoom = visited[toId]
                        val toLabel = toRoom?.name?.replace("\"", "\\\"") ?: "room_$toId"
                        sb.appendLine("  \"$fromLabel\" -> \"$toLabel\" [label=\"$dir\"];")
                    }
                }
                sb.appendLine("}")
                Files.writeString(path, sb.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
                println("Wrote explored map DOT file to: ${path.toAbsolutePath()}")
            } catch (t: Throwable) {
                println("Could not write DOT file: ${t::class.simpleName}: ${t.message}")
            }
        }

        // DFS over roomId graph (suspend)
        suspend fun dfs(currentId: Int) {
            val room = visited[currentId] ?: return
            val doors = room.doors
            edges.getOrPut(currentId) { mutableMapOf() }
            for (dir in doors) {
                dbg("DEBUG: at roomId=$currentId (${room.name}) trying dir='$dir')")

                sendCommand(dir)
                val out = readUntilPrompt()

                // ignore outright invalid moves
                if (out.contains("You can't go that way", ignoreCase = true) || out.contains(
                        "you cant go that way",
                        ignoreCase = true
                    )
                ) {
                    dbg("DEBUG: invalid movement '$dir' from $currentId; ignoring")
                    continue
                }

                // detect ejection/trap (no header and contains ejection keywords)
                val looksLikeTrap = !out.contains("==") && (out.contains("Alert!", true) || out.contains(
                    "ejected",
                    true
                ) || out.contains("heavier", true))
                if (looksLikeTrap) {
                    dbg(
                        "DEBUG: detected trap/ejection after moving '$dir' from $currentId; snippet: ${
                            out.lines().take(4).joinToString(" | ")
                        }"
                    )
                    val checkpointEntry = visited.entries.firstOrNull {
                        it.value.name.contains(
                            "security",
                            true
                        ) || it.value.name.contains("checkpoint", true)
                    }
                    if (checkpointEntry != null) {
                        val pathBack = findPath(checkpointEntry.key, currentId)
                        if (pathBack != null) for (d in pathBack) {
                            sendCommand(d); readUntilPrompt()
                        }
                    }
                    continue
                }

                val newRoom = parseRoom(out)
                if (newRoom.name == "<unknown>") {
                    dbg(
                        "DEBUG: parseRoom returned <unknown>; treating as trap/ejection. snippet: ${
                            out.lines().take(6).joinToString(" | ")
                        }"
                    )
                    val checkpointEntry2 = visited.entries.firstOrNull {
                        it.value.name.contains(
                            "security",
                            true
                        ) || it.value.name.contains("checkpoint", true)
                    }
                    if (checkpointEntry2 != null) {
                        val pathBack2 = findPath(checkpointEntry2.key, currentId)
                        if (pathBack2 != null) for (d in pathBack2) {
                            sendCommand(d); readUntilPrompt()
                        }
                    }
                    continue
                }

                fun signature(r: Room) =
                    r.name.trim().lowercase() + "|" + r.items.sorted().joinToString(",") + "|" + r.doors.sorted()
                        .joinToString(",")

                val newSig = signature(newRoom)
                val existing = visited.entries.firstOrNull { signature(it.value) == newSig }
                val neighborId = existing?.key
                    ?: run {
                        val id = nextId++
                        visited[id] = newRoom
                        dbg("DEBUG: discovered new roomId=$id -> ${newRoom.name} via '$dir' from $currentId")
                        id
                    }

                edges[currentId]!![dir] = neighborId
                edges.getOrPut(neighborId) { mutableMapOf() }[reverse[dir]!!] = currentId

                if (existing == null) dfs(neighborId)

                dbg("DEBUG: backtracking from roomId=$neighborId to $currentId via '${reverse[dir]!!}'")
                sendCommand(reverse[dir]!!)
                readUntilPrompt()
            }
        }

        // Top-level try/catch/finally for exploration
        try {
            try {
                dfs(startId)
            } catch (_: ClosedReceiveChannelException) { /* continue */
            }

            dbg("\nExplored rooms: ${visited.size}")

            dbg("\nRooms (id -> name [items]):")
            visited.forEach { (id, r) ->
                dbg(
                    if (r.items.isEmpty()) "$id -> ${if (id == 0) "${r.name} (START)" else r.name}" else "$id -> ${if (id == 0) "${r.name} (START)" else r.name} [${
                        r.items.joinToString(
                            ", "
                        )
                    }]"
                )
            }

            dbg("\nEdges (id -> dir -> id):")
            edges.forEach { (id, map) -> dbg("$id -> ${map.entries.joinToString(", ") { "${it.key}->${it.value}" }}") }

            // generate dot file for visualization
            generateDotFile(edges, visited)

            // collect items and test deadly items
            val itemsToTest = mutableListOf<Pair<String, Int>>()
            visited.forEach { (id, r) -> r.items.forEach { item -> itemsToTest.add(item to id) } }
            val knownDeadlyItems = setOf("infinite loop", "molten lava", "photons", "escape pod", "giant electromagnet")
            knownDeadlyItems.forEach { itemsToTest.removeIf { (item, _) -> item == it } }

            println("\nTesting ${itemsToTest.size} items for deadly behavior...")
            val safeItems = mutableListOf<String>()
            for ((item, roomId) in itemsToTest) {
                val path =
                    findPath(startId, roomId) ?: run { dbg("Could not find path to $item at room $roomId"); continue }

                val testComputer = NIC(memory = program.copyOf(), output = Channel(Channel.UNLIMITED))
                var died = false
                var reasonText = ""
                val job = launch {
                    try {
                        testComputer.start()
                    } catch (_: Throwable) { /* ignore */
                    }
                }

                suspend fun readUntilPromptTest(): String {
                    val sb = StringBuilder()
                    try {
                        while (true) {
                            val v = testComputer.output.receive()
                            sb.append(v.toInt().toChar())
                            if (sb.endsWith("Command?\n")) break
                        }
                    } catch (_: ClosedReceiveChannelException) {
                        return sb.toString()
                    }
                    return sb.toString()
                }

                suspend fun sendCommandTest(cmd: String) {
                    if (testComputer.input.isClosedForSend) return
                    ("$cmd\n").forEach { ch ->
                        if (testComputer.input.isClosedForSend) return else try {
                            testComputer.input.send(ch.code.toLong())
                        } catch (_: Throwable) {
                            return
                        }
                    }
                }

                try {
                    for (d in path) {
                        sendCommandTest(d)
                        try {
                            readUntilPromptTest()
                        } catch (_: ClosedReceiveChannelException) { /* ignore */
                        }
                    }
                    sendCommandTest("take $item")
                    val out: String = try {
                        readUntilPromptTest()
                    } catch (_: ClosedReceiveChannelException) {
                        ""
                    }

                    if (!out.contains("Command?")) {
                        died = true
                        reasonText = out.lines().joinToString(" ") { it.trim() }.take(200)
                    } else {
                        sendCommandTest("drop $item"); try {
                            readUntilPromptTest()
                        } catch (_: ClosedReceiveChannelException) { /* ignore */
                        }
                    }
                } catch (e: Throwable) {
                    died = true
                    reasonText = "Exception: ${e::class.simpleName}: ${e.message}"
                } finally {
                    try {
                        if (!testComputer.input.isClosedForSend) testComputer.input.close()
                    } catch (_: Exception) {
                    }
                    job.cancel()
                }

                dbg("- $item at $roomId -> ${if (died) "DEADLY" else "SAFE"}${if (reasonText.isNotBlank()) ": $reasonText" else ""}")
                if (!died) safeItems.add(item)
            }

            // --- Password search using safe items (collected from test) ---
            val desiredSafeItems = safeItems.ifEmpty {
                listOf(
                    "spool of cat6",
                    "mug",
                    "asterisk",
                    "sand",
                    "tambourine",
                    "festive hat"
                )
            }

            val itemRooms = mutableMapOf<String, Int>()
            for ((id, room) in visited) {
                for (it in room.items) {
                    if (it in desiredSafeItems) itemRooms[it] = id
                }
            }

            val missing = desiredSafeItems.filter { it !in itemRooms.keys }
            if (missing.isNotEmpty()) dbg(
                "Could not locate these required items in the explored map: ${
                    missing.joinToString(
                        ", "
                    )
                }"
            )
            else {
                val checkpointEntry = visited.entries.firstOrNull {
                    it.value.name.contains(
                        "security",
                        true
                    ) || it.value.name.contains("checkpoint", true)
                }
                if (checkpointEntry == null) dbg("Security Checkpoint not found in explored rooms; cannot run weight combination tests automatically.")
                else {
                    val checkpointId = checkpointEntry.key
                    dbg("\nSecurity Checkpoint found at $checkpointId (${checkpointEntry.value.name})")

                    // move to checkpoint
                    val pathToCheckpoint = findPath(startId, checkpointId)
                    if (pathToCheckpoint == null) dbg("No path from start to checkpoint found")
                    else {
                        for (d in pathToCheckpoint) {
                            sendCommand(d); readUntilPrompt()
                        }

                        // collect items
                        for (it in desiredSafeItems) {
                            val coord = itemRooms[it] ?: continue
                            val path = findPath(checkpointId, coord) ?: continue
                            for (d in path) {
                                sendCommand(d); readUntilPrompt()
                            }
                            sendCommand("take $it"); readUntilPrompt()
                            for (d in path.asReversed()) {
                                sendCommand(reverse[d]!!); readUntilPrompt()
                            }
                        }

                        val nbSafeItems = desiredSafeItems.size
                        val found = false
                        val candidateDirs =
                            edges[checkpointId]?.keys?.toList() ?: listOf("north", "south", "east", "west")

                        for (mask in 0 until (1 shl nbSafeItems)) {
                            // drop all first
                            for (itm in desiredSafeItems) {
                                sendCommand("drop $itm"); readUntilPrompt()
                            }

                            // take subset
                            val subset = mutableListOf<String>()
                            for (i in 0 until nbSafeItems) if ((mask shr i) and 1 == 1) subset.add(desiredSafeItems[i])

                            dbg("DEBUG: TRY subset mask=$mask -> [${subset.joinToString(", ")}] (size=${subset.size})")
                            for (itm in subset) {
                                sendCommand("take $itm"); readUntilPrompt()
                            }

                            for (dir in candidateDirs) {
                                dbg(
                                    "DEBUG: attempting direction '$dir' from checkpoint with items [${
                                        subset.joinToString(
                                            ", "
                                        )
                                    }]"
                                )
                                sendCommand(dir)
                                val out = readUntilPrompt()
                                val outSnippet = out.lines().joinToString(" | ") { it.trim() }.take(400)
                                dbg("DEBUG: output snippet for dir='$dir': $outSnippet")

                                val password = Regex("\\d{6,}").find(out)?.value
                                if (password != null) {
                                    val msg = "PASSWORD FOUND with items [${subset.joinToString(", ")}]: $password"
                                    println("\n$msg")
                                    dbg(msg)

                                    return@runBlocking password
                                }

                                if (out.contains("==") && out.contains("Command?")) {
                                    sendCommand(reverse[dir]!!); readUntilPrompt()
                                } else dbg("DEBUG: did not move into a room for dir='$dir' (likely ejected or blocked)")
                            }
                            if (found) break
                        }

                        if (!found) dbg("No valid combination found among ${1 shl nbSafeItems} subsets")
                    }
                }
            }

        } catch (e: Throwable) {
            dbg("\nExploration aborted due to: ${e::class.simpleName}: ${e.message}")
            dbg("Explored rooms so far: ${visited.size}")
            dbg("Rooms (id -> name):")
            visited.forEach { (id, r) -> dbg("$id -> ${r.name}") }
        } finally {
            try {
                if (!computer.input.isClosedForSend) computer.input.close()
            } catch (_: Exception) {
            }

            try {
                val path = Paths.get("run-output.txt")
                Files.writeString(
                    path,
                    logSb.toString(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
                )
                dbg("Wrote exploration log to: ${path.toAbsolutePath()}")
            } catch (t: Throwable) {
                dbg("Could not write log file: ${t::class.simpleName}: ${t.message}")
            }
        }

        return@runBlocking "PASSWORD NOT FOUND"
    }

}
