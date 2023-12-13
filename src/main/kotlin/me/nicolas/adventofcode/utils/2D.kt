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
    val rows: Int get() = maxX - minX + 1
    val columns: Int get() = maxY - minY + 1
    val minX: Int get() = map.keys.minOfOrNull { it.first } ?: 0
    val minY: Int get() = map.keys.minOfOrNull { it.second } ?: 0
    val maxX: Int get() = map.keys.maxOfOrNull { it.first } ?: 0
    val maxY: Int get() = map.keys.maxOfOrNull { it.second } ?: 0

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

    fun getRow(row: Int) = map.filter { it.key.first == row }.values.toList()
    fun getColumn(column: Int) = map.filter { it.key.second == column }.values.toList()

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

    fun invert(): Grid<T> {
        val newGrid = Grid<T>(mutableMapOf())

        for ((point, value) in map) {
            val newPoint = Pair(point.second, point.first)
            newGrid[newPoint] = value
        }

        return newGrid
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



