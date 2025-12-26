package me.nicolas.adventofcode.year2019

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ClosedSendChannelException
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

    /**
     * Entry for part one of Day 25.
     *
     * Algorithm (high-level):
     * - Parse the input Intcode program into a mutable memory map indexed by address (Long -> Long).
     * - If run in interactive mode, start an interactive game session with the Intcode NIC and
     *   relay stdin/stdout so the user can manually explore and issue commands.
     * - Otherwise, run the automated exploration routine `exploreAll` which performs the following:
     *   - Launches an Intcode NIC running the supplied program.
     *   - Reads the textual room descriptions emitted by the NIC and parses them into `Room` objects.
     *   - Performs a depth-first exploration of reachable rooms, building a graph of rooms (visited)
     *     and directed edges labeled by movement directions (north/south/east/west).
     *   - Detects traps/ejections by inspecting output snippets and avoids revisiting identical room
     *     signatures (name+doors+items).
     *   - After exploration, collects items found in rooms and filters known deadly items.
     *   - Tests each remaining item by launching ephemeral NIC instances and attempting to pick it up
     *     to detect items that immediately kill or eject the player.
     *   - Using the set of safe items, moves to the security checkpoint and tries every subset of
     *     items (dropping/taking) to find the correct weight combination that yields the password.
     */
    fun partOne(data: String, interactive: Boolean = false): String {
        val program: Map<Long, Long> = data
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

    /**
     * Interactive play mode for the Intcode-based game.
     *
     * Purpose:
     * - Start the Intcode NIC and let a user interact with it via the terminal.
     *
     * High-level behavior:
     * - Create a NIC instance with a copy of the program and an unbounded output channel.
     * - Start the NIC in a coroutine so it runs concurrently with the I/O relay.
     * - Launch a coroutine that continuously reads bytes from the NIC's output channel and
     *   prints them to stdout so the user sees room descriptions and prompts.
     * - When a prompt appears, read a line from the user's stdin and forward every character
     *   to the NIC's input channel (including the newline) so the NIC receives commands.
     *
     * Notes:
     * - This mode is intended for debugging or manual play. It does not return a result;
     *   the user interacts directly with the running NIC.
     */
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    fun playGame(program: Map<Long, Long>) = runBlocking {
        val computer = NIC(
            memory = program.copyOf().toMutableMap(),
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

    private data class Room(
        val name: String,
        val text: String,
        val items: List<String> = emptyList(),
        val doors: List<String> = emptyList()
    )

    private val logSb = StringBuilder()

    private fun dbg(msg: String) {
        logSb.append(msg).append('\n')
    }

    // parseRoom now uses class-level dbg directly
    private fun parseRoom(text: String): Room {
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
            "DEBUG: could not parse room name from output snippet: ${text.lines().take(6).joinToString(" ")}"
        )
        return Room(name, text, items, doors)
    }

    private fun generateDotFile(
        edges: Map<Int, Map<String, Int>>,
        visited: Map<Int, Room>,
        knownDeadlyItems: Set<String> = emptySet()
    ) {
        try {
            val path = Paths.get("src/main/resources/me/nicolas/adventofcode/year2019/day25/day25-explored-map.dot")
            val stringBuilder = StringBuilder()
            stringBuilder.appendLine("digraph G {")
            for ((fromId, map) in edges) {
                val fromRoom = visited[fromId]
                val fromBase = fromRoom?.name ?: "room_$fromId"
                val fromItems = fromRoom?.items?.takeIf { it.isNotEmpty() }?.map { item ->
                    if (item.lowercase() in knownDeadlyItems) "${item}*" else item
                }?.joinToString(", ")
                val fromLabelRaw = if (fromItems != null) "$fromBase [$fromItems]" else fromBase
                val fromLabel = fromLabelRaw.replace("\"", "\\\"")
                for ((dir, toId) in map) {
                    val toRoom = visited[toId]
                    val toBase = toRoom?.name ?: "room_$toId"
                    val toItems = toRoom?.items?.takeIf { it.isNotEmpty() }?.map { item ->
                        if (item.lowercase() in knownDeadlyItems) "${item}*" else item
                    }?.joinToString(", ")
                    val toLabelRaw = if (toItems != null) "$toBase [$toItems]" else toBase
                    val toLabel = toLabelRaw.replace("\"", "\\\"")
                    stringBuilder.appendLine("  \"$fromLabel\" -> \"$toLabel\" [label=\"$dir\"];")
                }
            }
            stringBuilder.appendLine("}")
            Files.writeString(
                path,
                stringBuilder.toString(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
            println("Wrote explored map DOT file to: ${path.toAbsolutePath()}")
        } catch (t: Throwable) {
            println("Could not write DOT file: ${t::class.simpleName}: ${t.message}")
        }
    }

    private suspend fun readUntilPrompt(computer: NIC): String {
        val stringBuilder = StringBuilder()
        try {
            while (true) {
                val v = computer.output.receive()
                stringBuilder.append(v.toInt().toChar())
                if (stringBuilder.endsWith("Command?\n")) break
            }
        } catch (_: ClosedReceiveChannelException) {
            return stringBuilder.toString()
        }

        return stringBuilder.toString()
    }

    // sendCommand uses try/catch rather than isClosedForSend checks to avoid delicate API warnings
    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun sendCommand(cmd: String, computer: NIC) {
        val seq = "$cmd\n"
        for (ch in seq) {
            try {
                computer.input.send(ch.code.toLong())
            } catch (_: ClosedSendChannelException) {
                return
            } catch (_: Throwable) {
                return
            }
        }
    }

    /**
     * Automated exploration and password discovery routine.
     *
     * Purpose:
     * - Drive the Intcode NIC automatically to explore all reachable rooms, collect safe items,
     *   and search for the security checkpoint password using item-weight combinations.
     *
     * High-level steps:
     * 1. Start the NIC and read the initial room description.
     * 2. Parse room output into `Room` objects (name, items, doors).
     * 3. Perform a depth-first exploration of rooms:
     *    - Maintain `visited` (id -> Room) and `edges` (id -> dir -> id).
     *    - Use a room signature (name+items+doors) to avoid duplicates.
     *    - Detect traps/ejections by inspecting output snippets and backtrack as needed.
     * 4. After exploration, generate a DOT file for visualization (rooms labeled with items).
     * 5. Filter known-deadly items and test remaining items by spawning ephemeral NICs that
     *    pick up the item and observe if the player is ejected/killed.
     * 6. Using the set of safe items, move to the security checkpoint and try every subset
     *    of items (drop/take) to find the correct weight combination; return the password
     *    string when found.
     */
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    fun exploreAll(program: Map<Long, Long>): String = runBlocking {
        val computer = NIC(
            memory = program.copyOf().toMutableMap(),
            output = Channel(Channel.UNLIMITED)
        )

        launch {
            try {
                computer.start()
            } catch (_: Throwable) {
                // ignore NIC start errors
            }
        }

        // exploration state (was previously class-level/local) - keep local to avoid cross-run pollution
        val visited = mutableMapOf<Int, Room>()
        val edges = mutableMapOf<Int, MutableMap<String, Int>>()
        val reverse = mapOf("north" to "south", "south" to "north", "east" to "west", "west" to "east")
        val startId = 0
        var nextId = 1

        // reset class-level logger for this run
        logSb.setLength(0)

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

        // initialize starting room
        val initial = try {
            readUntilPrompt(computer)
        } catch (_: ClosedReceiveChannelException) {
            ""
        }
        if (initial.isNotEmpty()) {
            val r = parseRoom(initial)
            visited[startId] = r
            dbg("DEBUG: startId=${0} -> ${r.name} [${r.items.joinToString(", ")}]")
        }

        // DFS over roomId graph (suspend)
        suspend fun dfs(currentId: Int) {
            val room = visited[currentId] ?: return
            val doors = room.doors
            edges.getOrPut(currentId) { mutableMapOf() }
            for (dir in doors) {
                dbg("DEBUG: at roomId=$currentId (${room.name}) trying dir='$dir')")

                sendCommand(dir, computer)
                val out = readUntilPrompt(computer)

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
                            sendCommand(d, computer); readUntilPrompt(computer)
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
                            sendCommand(d, computer); readUntilPrompt(computer)
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
                sendCommand(reverse[dir]!!, computer)
                readUntilPrompt(computer)
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
            val knownDeadlyItems = setOf("infinite loop", "molten lava", "photons", "escape pod", "giant electromagnet")
            generateDotFile(edges, visited, knownDeadlyItems)

            // collect items and test deadly items
            val itemsToTest = mutableListOf<Pair<String, Int>>()
            visited.forEach { (id, r) -> r.items.forEach { item -> itemsToTest.add(item to id) } }
            knownDeadlyItems.forEach { itemsToTest.removeIf { (item, _) -> item == it } }

            println("\nTesting ${itemsToTest.size} items for deadly behavior...")
            val safeItems = mutableListOf<String>()
            for ((item, roomId) in itemsToTest) {
                val path =
                    findPath(startId, roomId) ?: run { dbg("Could not find path to $item at room $roomId"); continue }

                val testComputer = NIC(memory = program.copyOf().toMutableMap(), output = Channel(Channel.UNLIMITED))
                var died = false
                var reasonText = ""
                val job = launch {
                    try {
                        testComputer.start()
                    } catch (_: Throwable) { /* ignore */
                    }
                }

                try {
                    for (d in path) {
                        sendCommand(d, testComputer)
                        try {
                            readUntilPrompt(testComputer)
                        } catch (_: ClosedReceiveChannelException) { /* ignore */
                        }
                    }
                    sendCommand("take $item", testComputer)
                    val out: String = try {
                        readUntilPrompt(testComputer)
                    } catch (_: ClosedReceiveChannelException) {
                        ""
                    }

                    if (!out.contains("Command?")) {
                        died = true
                        reasonText = out.lines().joinToString(" ") { it.trim() }.take(200)
                    } else {
                        sendCommand("drop $item", testComputer); try {
                            readUntilPrompt(testComputer)
                        } catch (_: ClosedReceiveChannelException) { /* ignore */
                        }
                    }
                } catch (e: Throwable) {
                    died = true
                    reasonText = "Exception: ${e::class.simpleName}: ${e.message}"
                } finally {
                    try {
                        try {
                            testComputer.input.close()
                        } catch (_: Exception) {
                        }
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
                "Could not locate these required items in the explored map: ${missing.joinToString(", ")}"
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
                            sendCommand(d, computer); readUntilPrompt(computer)
                        }

                        // collect items
                        for (it in desiredSafeItems) {
                            val coord = itemRooms[it] ?: continue
                            val path = findPath(checkpointId, coord) ?: continue
                            for (d in path) {
                                sendCommand(d, computer); readUntilPrompt(computer)
                            }
                            sendCommand("take $it", computer); readUntilPrompt(computer)
                            for (d in path.asReversed()) {
                                sendCommand(reverse[d]!!, computer); readUntilPrompt(computer)
                            }
                        }

                        val nbSafeItems = desiredSafeItems.size
                        val found = false
                        val candidateDirs =
                            edges[checkpointId]?.keys?.toList() ?: listOf("north", "south", "east", "west")

                        for (mask in 0 until (1 shl nbSafeItems)) {
                            // drop all first
                            for (itm in desiredSafeItems) {
                                sendCommand("drop $itm", computer); readUntilPrompt(computer)
                            }

                            // take subset
                            val subset = mutableListOf<String>()
                            for (i in 0 until nbSafeItems) if ((mask shr i) and 1 == 1) subset.add(desiredSafeItems[i])

                            dbg("DEBUG: TRY subset mask=$mask -> [${subset.joinToString(", ")} ] (size=${subset.size})")
                            for (itm in subset) {
                                sendCommand("take $itm", computer); readUntilPrompt(computer)
                            }

                            for (dir in candidateDirs) {
                                dbg(
                                    "DEBUG: attempting direction '$dir' from checkpoint with items [${
                                        subset.joinToString(
                                            ", "
                                        )
                                    }]"
                                )
                                sendCommand(dir, computer)
                                val out = readUntilPrompt(computer)
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
                                    sendCommand(reverse[dir]!!, computer); readUntilPrompt(computer)
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
                try {
                    computer.input.close()
                } catch (_: Exception) {
                }
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
