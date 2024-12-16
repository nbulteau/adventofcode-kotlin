package me.nicolas.adventofcode.utils

/**
 * Grid implementation with a map of Pair<Int, Int> to T
 * This class represents a 2D grid where each cell is represented by a pair of integers (x, y) and holds a value of type T.
 */
class Grid<T>(private val map: MutableMap<Pair<Int, Int>, T> = mutableMapOf()) {

    companion object {
        // Create a grid (Grid<Char>) from the data string (each line is a row)
        fun of(data: String): Grid<Char> {
            val map = data.lines().flatMapIndexed { rowIndex, row ->
                row.mapIndexed { colIndex, element ->
                    Pair(rowIndex, colIndex) to element
                }
            }.filter { it.second != ' ' }.toMap().toMutableMap()

            return Grid(map)
        }
    }

    /** Return a copy of the map */
    fun copy(): Grid<T> = Grid(map.toMutableMap())

    fun toMap(): Map<Pair<Int, Int>, T> = map.toMap()

    /** Return the list of indices (Pair<Int, Int>) */
    val indices: List<Pair<Int, Int>>
        get() = map.keys.toList()

    val rows: Int
        get() = maxX - minX + 1

    val columns: Int
        get() = maxY - minY + 1

    val minX: Int
        get() = map.keys.minOfOrNull { point -> point.first } ?: 0

    val minY: Int
        get() = map.keys.minOfOrNull { point -> point.second } ?: 0

    val maxX: Int
        get() = map.keys.maxOfOrNull { point -> point.first } ?: 0

    val maxY: Int
        get() = map.keys.maxOfOrNull { point -> point.second } ?: 0

    operator fun get(value: T): Pair<Int, Int>? = map.entries.find { entry -> entry.value == value }?.key

    /**
     * Get the value at the given point in the grid.
     * If the point does not exist in the grid, it returns null.
     */
    operator fun get(point: Pair<Int, Int>): T? {
        return map[Pair(point.first, point.second)]
    }

    operator fun get(x: Int, y: Int): T? {
        return map[Pair(x, y)]
    }

    operator fun set(x: Int, y: Int, value: T) {
        map[Pair(x, y)] = value
    }

    operator fun set(point: Pair<Int, Int>, value: T) {
        map[Pair(point.first, point.second)] = value
    }

    fun getRow(row: Int) = map.filter { it.key.first == row }.values.toList()

    fun getColumn(column: Int) = map.filter { it.key.second == column }.values.toList()

    /**
     * Get all the cardinal neighbors (up, down, left, right) of the given point in the grid.
     * It returns a list of points that are neighbors to the given point.
     */
    fun getCardinalNeighbors(point: Pair<Int, Int>): List<Pair<Int, Int>> {
        val (x, y) = point
        val neighbors = mutableListOf<Pair<Int, Int>>()

        val up = Pair(x, y + 1)
        if (up in map) {
            neighbors.add(up)
        }
        val down = Pair(x, y - 1)
        if (down in map) {
            neighbors.add(down)
        }
        val left = Pair(x - 1, y)
        if (left in map) {
            neighbors.add(left)
        }
        val right = Pair(x + 1, y)
        if (right in map) {
            neighbors.add(right)
        }

        return neighbors
    }

    /**
     * Find all occurrences of a given value in the grid.
     * This function takes a value of type T and returns a list of points (Pair<Int, Int>) where this value is found in the grid.
     */
    fun findAll(t: T): List<Pair<Int, Int>> =
        map.filter { cell ->
            cell.value === t
        }.keys.toList()

    fun findOne(t: T): Pair<Int, Int>? =
        map.entries.find { (_, value) -> value === t }?.key

    /**
     * Invert the grid. This function swaps the x and y coordinates of each point in the grid.
     * It returns a new grid with the inverted points.
     */
    fun invert(): Grid<T> {
        val newGrid = Grid<T>(mutableMapOf())

        for ((point, value) in map) {
            val newPoint = Pair(point.second, point.first)
            newGrid[newPoint] = value
        }

        return newGrid
    }

    /** Check if the grid is empty */
    fun isEmpty(): Boolean = map.isEmpty()

    fun isValid(x: Int, y: Int) = x in 0..<rows && y >= 0 && y < columns

    fun isValid(point: Point) = isValid(point.x, point.y)

    fun isValid(point: Pair<Int, Int>) = isValid(point.first, point.second)


    /** Clear the grid */
    fun clear() {
        map.clear()
    }

    /** Check if the grid contains a specific value */
    fun contains(value: T): Boolean = map.containsValue(value)

    /** Replace all occurrences of a specific value with a new value */
    fun replace(oldValue: T, newValue: T) {
        map.replaceAll { _, value ->
            if (value == oldValue) {
                newValue
            } else {
                value
            }
        }
    }

    /** Count the number of occurrences of a specific value in the grid */
    fun count(value: T): Int = map.values.count { it == value }

    /** Flood fill the grid with a new value starting from a specific point */
    fun floodFill(startPoint: Pair<Int, Int>, newValue: T): Grid<T> {
        val originalValue = get(startPoint) ?: return this
        val visited = mutableSetOf<Pair<Int, Int>>()
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(startPoint)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current in visited || get(current) != originalValue) {
                continue
            }
            this[current] = newValue
            visited.add(current)
            queue.addAll(getCardinalNeighbors(current))
        }

        return this
    }

    /**
     * Display the grid (Debug purpose)
     * This function prints the grid to the console for debugging purposes.
     */
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





