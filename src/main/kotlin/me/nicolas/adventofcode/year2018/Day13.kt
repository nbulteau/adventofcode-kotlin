package me.nicolas.adventofcode.year2018

import me.nicolas.adventofcode.utils.*


// https://adventofcode.com/2018/day/13
fun main() {
    val data = readFileDirectlyAsText("/year2018/day13/data.txt")
    val day = Day13(2018, 13, "Mine Cart Madness")
    prettyPrintPartOne { day.partOne(data) }
    prettyPrintPartTwo { day.partTwo(data) }
}

class Day13(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(data: String): String {
        val (tracks, carts) = extractTracksAndCarts(data)
        solve(carts, tracks) { cartSet -> cartSet.count { !it.alive } == 0 }

        // debug(tracks, carts)
        // There are two carts that crashed, so we can find them by filtering the carts by alive status
        val firstCrash = carts.find { !it.alive }!!.position

        return "${firstCrash.second},${firstCrash.first}"
    }

    fun partTwo(data: String): String {
        val (tracks, carts) = extractTracksAndCarts(data)
        solve(carts, tracks) { cartSet -> cartSet.count { it.alive } > 1 }

        // debug(tracks, carts)
        // There is only one cart left, so we can find it by filtering the carts by alive status
        val lastCart = carts.find { it.alive }!!.position

        return "${lastCart.second},${lastCart.first}"
    }

    // Solve the puzzle by iterating over the carts until the stop condition is met (all carts crashed or only one cart left)
    private fun solve(carts: Set<Cart>, tracks: Grid<Char>, stopCondition: (Set<Cart>) -> Boolean) {
        while (stopCondition(carts)) {
            // Sort carts by position (top to bottom, left to right)
            val sortedCarts = carts.sortedWith(compareBy({ it.position.second }, { it.position.first }))
            for (cart in sortedCarts) {
                // Skip carts that have already crashed (not alive)
                if(!cart.alive) continue

                // Process a tick (move the cart, change direction if needed) based on the current track piece
                cart.position = cart.position.first + cart.direction.dx to cart.position.second + cart.direction.dy
                processDirection(tracks, cart)

                // check if the cart collides with another cart (same position)
                val collidingCart = sortedCarts.find { it.collidesWith(cart) }
                if (collidingCart != null) {
                    cart.alive = false
                    collidingCart.alive = false
                }
            }
        }
    }

    // Debugging function to display the tracks and carts in the console (not used in the solution)
    private fun debug(
        tracks: Grid<Char>,
        sortedCarts: Set<Cart>,
    ) {
        val copy = tracks.toMap().toMutableMap()
        for (cart in sortedCarts) {
            when {
                !cart.alive -> copy[cart.position] = 'X'
                cart.direction == Direction.Up -> copy[cart.position] = '^'
                cart.direction == Direction.Right -> copy[cart.position] = '>'
                cart.direction == Direction.Down -> copy[cart.position] = 'v'
                cart.direction == Direction.Left -> copy[cart.position] = '<'
            }
        }
        Grid(copy).display()
    }

    // Process the direction of the cart based on the current track piece and the cart's direction
    private fun processDirection(tracks: Grid<Char>, cart: Cart) {
        when (tracks[cart.position]) {
            '/' -> {
                cart.direction = when (cart.direction) {
                    Direction.Up -> Direction.Right
                    Direction.Right -> Direction.Up
                    Direction.Down -> Direction.Left
                    Direction.Left -> Direction.Down
                }
            }

            '\\' -> {
                cart.direction = when (cart.direction) {
                    Direction.Up -> Direction.Left
                    Direction.Right -> Direction.Down
                    Direction.Down -> Direction.Right
                    Direction.Left -> Direction.Up
                }
            }

            '+' -> {
                cart.direction = cart.direction.next(cart.turn)
                // Change the turn for the next crossing (left, straight, right, left, ...)
                cart.turn = when (cart.turn) {
                    Turn.Left -> Turn.Straight
                    Turn.Straight -> Turn.Right
                    Turn.Right -> Turn.Left
                    else -> error("Invalid turn: ${cart.turn}")
                }
            }
        }
    }

    // Extract the tracks and carts from the input data
    private fun extractTracksAndCarts(data: String): Pair<Grid<Char>, Set<Cart>> {
        val tracks = Grid.of(data)
        val carts: Set<Cart> = tracks.toMap().mapNotNull { (point, value) ->
            if (value in setOf('>', '<', '^', 'v')) {
                // Replace cart with track piece (for the display)
                tracks[point] = when (value) {
                    '>' -> '-'
                    '<' -> '-'
                    '^' -> '|'
                    'v' -> '|'
                    else -> {
                        error("Invalid cart: $value")
                    }
                }
                Cart(point, Direction.of(value))
            } else {
                null
            }
        }.toSet()
        return Pair(tracks, carts)
    }


    private data class Turn(val dx: Int, val dy: Int) {
        companion object {
            val Left = Turn(-1, 0)
            val Right = Turn(1, 0)
            val Straight = Turn(0, 0)
        }
    }

    // Enum representing the direction of a cart (up, right, down, left)
    private enum class Direction(val dx: Int, val dy: Int) {
        Up(-1, 0), Right(0, 1), Down(1, 0), Left(0, -1);

        fun next(turn: Turn): Direction =
            when (turn) {
                Turn.Left -> when (this) {
                    Up -> Left
                    Right -> Up
                    Down -> Right
                    Left -> Down
                }

                Turn.Right -> when (this) {
                    Up -> Right
                    Right -> Down
                    Down -> Left
                    Left -> Up
                }

                Turn.Straight -> this

                else -> {
                    error("Invalid turn: $turn")
                }
            }

        companion object {
            fun of(char: Char): Direction =
                when (char) {
                    '^' -> Up
                    '>' -> Right
                    'v' -> Down
                    '<' -> Left
                    else -> error("Invalid direction: $char")
                }
        }
    }

    // Class representing a cart (position, direction, turn, alive)
    private data class Cart(
        var position: Pair<Int, Int>,
        var direction: Direction, var turn: Turn = Turn.Left,
        var alive: Boolean = true,
    ) {
        // Check if this cart collides with another cart (same position)
        fun collidesWith(other: Cart): Boolean =
            this != other
                    && this.alive && other.alive
                    && position.first == other.position.first
                    && position.second == other.position.second

    }
}