package me.nicolas.adventofcode.utils


class Grid<T>(private val map: MutableMap<Pair<Int, Int>, T>) {
    companion object {
        // Create a grid from the data
        fun of(data: String): Grid<Char> {
            val map = data.lines().flatMapIndexed { rowIndex, row ->
                row.mapIndexed { colIndex, element ->
                    Pair(rowIndex, colIndex) to element
                }
            }.filter { it.second != ' ' }.toMap().toMutableMap()

            return Grid(map)
        }
    }

    // Return the list of indices
    val indices: List<Pair<Int, Int>> get() = map.keys.toList()

    operator fun get(value: T): Pair<Int, Int>? = map.entries.find { it.value == value }?.key
    operator fun get(point: Pair<Int, Int>): T? {
        return map[Pair(point.first, point.second)]
    }
    operator fun set(x: Int, y: Int, value: T) {
        map[Pair(x, y)] = value
    }
    operator fun set(point: Pair<Int, Int>, value: T) {
        map[Pair(point.first, point.second)] = value
    }

    fun getCardinalNeighbors(point: Pair<Int, Int>): List<Pair<Int, Int>> {
        val (x, y) = point
        val neighbors = mutableListOf<Pair<Int, Int>>()

        val up = Pair(x, y + 1)
        if (up in map) neighbors.add(up)
        val down = Pair(x, y - 1)
        if (down in map) neighbors.add(down)
        val left = Pair(x - 1, y)
        if (left in map) neighbors.add(left)
        val right = Pair(x + 1, y)
        if (right in map) neighbors.add(right)

        return neighbors
    }

    fun display() {
        val xMin = map.keys.minByOrNull { it.first }!!.first
        val xMax = map.keys.maxByOrNull { it.first }!!.first
        val yMin = map.keys.minByOrNull { it.second }!!.second
        val yMax = map.keys.maxByOrNull { it.second }!!.second

        for (x in xMin..xMax) {
            for (y in yMin..yMax) {
                print(map.getOrDefault(Pair(x, y), '.'))
            }
            println()
        }
        println()
    }
}



