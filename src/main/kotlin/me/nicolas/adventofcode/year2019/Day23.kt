package me.nicolas.adventofcode.year2019

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import me.nicolas.adventofcode.utils.writeFileDirectlyAsText

// --- Day 23: Category Six ---
// https://adventofcode.com/2019/day/23
// Solution inspired from Todd Ginsberg blog: https://todd.ginsberg.com/post/advent-of-code/2019/day23/

fun main() {
    val data = readFileDirectlyAsText("/year2019/day23/data.txt")
    val day = Day23(2019, 23)
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day23(year: Int, day: Int, title: String = "Category Six") : AdventOfCodeDay(year, day, title) {

    /**
     * partOne:
     * - Parse the IntCode program into a sparse memory map.
     * - Start the network of 50 NICs and a router, then wait for the first packet
     *   delivered to address 255 (the NAT). The NAT packet is forwarded to the
     *   responseChannel by the supplied handler; the second element (Y) is returned.
     */
    fun partOne(data: String): Int {
        val program: MutableMap<Long, Long> = data
            .split(",")
            .withIndex()
            .associateTo(mutableMapOf()) { it.index.toLong() to it.value.toLong() }

        return runDroids(program) { _, natChannel, responseChannel ->
            responseChannel.send(natChannel.receive())
        }
    }

    /**
     * partTwo:
     * - Currently does not contain an implementation.
     */
    fun partTwo(data: String): Int {
        return 0
    }

    /**
     * runDroids:
     * - Instantiate 50 NICs (droids), each with its own input/output channels.
     * - Create a router coroutine that consumes triples (dest, x, y) and forwards
     *   packets to the correct NIC input, or to the natChannel when dest == 255.
     * - Launch a NAT handler (natHandler) to process packets sent to address 255.
     * - For each NIC:
     *     * launch its execution coroutine (start)
     *     * initialize it by sending its network address as first input
     *     * launch a coroutine that reads three outputs at a time (dest,x,y)
     *       and sends the triple to the router
     * - Wait for the responseChannel to receive the NAT reply (handler decides when)
     *   and then cancel all children coroutines.
     */
    private fun runDroids(
        program: MutableMap<Long, Long>,
        natHandler: suspend (Array<NIC>, ReceiveChannel<Pair<Long, Long>>, SendChannel<Pair<Long, Long>>) -> Unit
    ): Int = runBlocking {
        val droids = Array(50) {
            NIC(memory = program.copyOf(), output = Channel(UNLIMITED))
        }
        val responseChannel = Channel<Pair<Long, Long>>(CONFLATED)
        val natChannel = Channel<Pair<Long, Long>>(UNLIMITED)
        val router = router(droids, natChannel)

        launch {
            natHandler(droids, natChannel, responseChannel)
        }

        droids.forEachIndexed { id, droid ->
            launch {
                droid.start()
            }
            // Initialize with network id.
            droid.input.send(id.toLong())
            droid.input.send(-1)

            launch {
                while (true) {
                    router.send(
                        Triple(
                            droid.output.receive(),
                            droid.output.receive(),
                            droid.output.receive()
                        )
                    )
                }
            }
        }

        responseChannel.receive().second.toInt().also {
            this.coroutineContext.cancelChildren()
        }
    }

    /**
     * router:
     * - Receives triples (to, x, y).
     * - If destination is 255, forward the (x,y) pair to natChannel.
     * - Otherwise, enqueue x and y on the destination NIC's input channel in order.
     *
     * This coroutine centralizes packet routing for the simulated network.
     */
    private fun CoroutineScope.router(
        droids: Array<NIC>,
        natChannel: SendChannel<Pair<Long, Long>>
    ): SendChannel<Triple<Long, Long, Long>> {
        val input: Channel<Triple<Long, Long, Long>> = Channel(UNLIMITED)
        launch {
            while (true) {
                val (to, x, y) = input.receive()
                if (to == 255L) {
                    natChannel.send(Pair(x, y))
                } else {
                    droids[to.toInt()].input.send(x)
                    droids[to.toInt()].input.send(y)
                }
            }
        }
        return input
    }

    fun <T, R> MutableMap<T, R>.copyOf(): MutableMap<T, R> =
        mutableMapOf<T, R>().also { it.putAll(this) }
}