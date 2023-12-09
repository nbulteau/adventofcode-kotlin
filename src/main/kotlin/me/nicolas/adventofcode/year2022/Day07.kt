package me.nicolas.adventofcode.year2022

import me.nicolas.adventofcode.utils.AdventOfCodeDay
import me.nicolas.adventofcode.utils.prettyPrintPartOne
import me.nicolas.adventofcode.utils.prettyPrintPartTwo
import me.nicolas.adventofcode.utils.readFileDirectlyAsText

fun main() {

    val training = readFileDirectlyAsText("/year2022/day07/training.txt")
    val data = readFileDirectlyAsText("/year2022/day07/data.txt")

    val lines = data.split("\n")

    val day = Day07(2022, 7, "No Space Left On Device")
    prettyPrintPartOne { day.partOne(lines) }
    prettyPrintPartTwo { day.partTwo(lines) }
}

private class Day07(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {

    fun partOne(lines: List<String>): Int {
        val filesystem = buildFileSystem(lines)

        val nodesList = filesystem.findAllOfTheDirectoriesWithATotalSizeOfAtMost(100000)

        return nodesList.sumOf { it.size }
    }

    fun partTwo(lines: List<String>): Int {
        val filesystem = buildFileSystem(lines)

        val sizeToFree = 30000000 - (70000000 - filesystem.size)
        val nodesList = filesystem.findAllOfTheDirectoriesWithATotalSizeOfAtLeast(sizeToFree)

        return nodesList.minOf { it.size }
    }

    private fun buildFileSystem(lines: List<String>): TreeNode {
        val filesystem = TreeNode("/.")
        var currentNode: TreeNode = filesystem
        var index = 0
        do {
            var currentLine = lines[index]
            when {
                currentLine.startsWith("$ cd") -> {
                    currentNode = when (val directory = currentLine.substringAfter("$ cd ")) {
                        "/" -> filesystem
                        ".." -> currentNode.parent!!
                        else -> currentNode.children.first { it.name == directory }
                    }
                    index++
                }

                currentLine == "$ ls" -> {
                    index++
                    while (index < lines.size) {
                        currentLine = lines[index]
                        if (currentLine.startsWith("$"))
                            break

                        when {
                            currentLine.startsWith("dir") -> {
                                currentNode.add(TreeNode(currentLine.substringAfter("dir "), currentNode))
                            }

                            else -> {
                                val file = currentLine.split(" ")
                                currentNode.add(TreeNode(file.last(), currentNode, file.first().toInt()))
                            }
                        }
                        index++
                    }
                }
            }
        } while (index < lines.size)

        return filesystem.updateSize()
    }

    private class TreeNode(val name: String, val parent: TreeNode? = null, var size: Int = 0) {
        val children: MutableList<TreeNode> = mutableListOf()

        fun add(child: TreeNode) = children.add(child)

        fun findAllOfTheDirectoriesWithATotalSizeOfAtMost(value: Int): List<TreeNode> {
            val nodes = mutableListOf<TreeNode>()
            recursiveAtMost(value, nodes)

            return nodes
        }

        private fun recursiveAtMost(value: Int, nodes: MutableList<TreeNode>) {
            for (child in children) {
                child.recursiveAtMost(value, nodes)
            }
            if (this.isDirectory() && this.size < value) {
                nodes.add(this)
            }
        }

        fun findAllOfTheDirectoriesWithATotalSizeOfAtLeast(value: Int): List<TreeNode> {
            val nodes = mutableListOf<TreeNode>()
            recursiveAtLeast(value, nodes)

            return nodes
        }

        private fun recursiveAtLeast(value: Int, nodes: MutableList<TreeNode>) {
            for (child in children) {
                child.recursiveAtLeast(value, nodes)
            }
            if (this.isDirectory() && this.size >= value) {
                nodes.add(this)
            }
        }

        fun updateSize(): TreeNode {
            for (child in this.children) {
                child.updateSize()
            }
            if (this.isDirectory()) {
                this.size = this.children.sumOf { it.size }
            }

            return this
        }

        fun isDirectory() = children.size > 0
    }

}
