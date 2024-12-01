package me.nicolas.adventofcode.utils

import java.io.File
import java.time.Year

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please provide a day number as a parameter.")
        return
    }

    val day = args[0].toIntOrNull()

    generateDay(day)
}

private fun generateDay(dayNumber: Int?) {
    if (dayNumber == null || dayNumber <= 0) {
        println("Invalid day number provided.")
        return
    }

    val currentYear = Year.now().value
    val packageName = "year$currentYear"
    val className = "Day%02d".format(dayNumber)
    val fileName = "src/me.nicolas.adventofcode.utils.main/kotlin/me/nicolas/adventofcode/$packageName/$className.kt"

    val fileContent = """
        package me.nicolas.adventofcode.$packageName

        import me.nicolas.adventofcode.utils.AdventOfCodeDay
        import me.nicolas.adventofcode.utils.prettyPrintPartOne
        import me.nicolas.adventofcode.utils.prettyPrintPartTwo
        import me.nicolas.adventofcode.utils.readFileDirectlyAsText

        // --- Day 1: ---
        // https://adventofcode.com/$currentYear/day/$dayNumber
        fun me.nicolas.adventofcode.utils.main() {
            val data = readFileDirectlyAsText("/$packageName/day${dayNumber.toString().padStart(2, '0')}/data.txt")
            val lines = data.split("\n")
            val day = $className($currentYear, $dayNumber, "")
            prettyPrintPartOne { day.partOne(lines) }
            prettyPrintPartTwo { day.partTwo(lines) }
        }

        class $className(year: Int, day: Int, title: String) : AdventOfCodeDay(year, day, title) {
            fun partOne(lines: List<String>): Int {
                return 0
            }

            fun partTwo(lines: List<String>): Int {
                return 0
            }
        }
    """.trimIndent()

    val file = File(fileName)
    file.parentFile.mkdirs()
    file.writeText(fileContent)

    // Generate the data file
    val dataFileName = "src/me.nicolas.adventofcode.utils.main/resources/me/nicolas/adventofcode/$packageName/day${dayNumber.toString().padStart(2, '0')}/data.txt"
    val dataFile = File(dataFileName)
    dataFile.parentFile.mkdirs()
    dataFile.writeText("")

    // Generate the riddle file
    val riddleFileName = "src/me.nicolas.adventofcode.utils.main/resources/me/nicolas/adventofcode/$packageName/day${dayNumber.toString().padStart(2, '0')}/riddle.txt"
    val riddleFile = File(riddleFileName)
    riddleFile.parentFile.mkdirs()
    riddleFile.writeText("")

    // Generate the test file
    val classNameTest = className + "Test"
    val testFileName = "src/test/kotlin/me/nicolas/adventofcode/$packageName/$classNameTest.kt"
    val testFileContent = """
        package me.nicolas.adventofcode.$packageName

        import org.junit.jupiter.api.Test
        import kotlin.test.assertEquals

        class ${className}Test {
            private val day = $className($currentYear, $dayNumber, "")

            @Test
            fun partOne() {
                assertEquals(0, day.partOne(emptyList()))
            }

            @Test
            fun partTwo() {
                assertEquals(0, day.partTwo(emptyList()))
            }
        }
    """.trimIndent()

    val testFile = File(testFileName)
    testFile.parentFile.mkdirs()
    testFile.writeText(testFileContent)

    println("File $fileName has been generated.")
}