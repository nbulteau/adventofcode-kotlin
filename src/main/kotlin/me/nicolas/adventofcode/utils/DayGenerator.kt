package me.nicolas.adventofcode.utils

import java.io.File
import java.time.Year

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please provide a day number as a parameter.")
        return
    }

    val day = args[0].toIntOrNull()
    val year = if (args.size > 1) args[1].toIntOrNull() ?: Year.now().value else Year.now().value

    generateDay(day, year)
}

private fun generateDay(day: Int?, year: Int) {
    if (day == null || day <= 0) {
        println("Invalid day number provided.")
        return
    }

    val packageName = "year$year"
    val className = "Day%02d".format(day)
    val fileName = "src/main/kotlin/me/nicolas/adventofcode/$packageName/$className.kt"

    val fileContent = """
        package me.nicolas.adventofcode.$packageName

        import me.nicolas.adventofcode.utils.AdventOfCodeDay
        import me.nicolas.adventofcode.utils.prettyPrintPartOne
        import me.nicolas.adventofcode.utils.prettyPrintPartTwo
        import me.nicolas.adventofcode.utils.readFileDirectlyAsText

        // --- Day $day: ---
        // https://adventofcode.com/$year/day/$day
        fun main() {
            val data = readFileDirectlyAsText("/$packageName/day${day.toString().padStart(2, '0')}/data.txt")
            val day = $className($year, $day)
            prettyPrintPartOne { day.partOne(data) }
            prettyPrintPartTwo { day.partTwo(data) }
        }

        class $className(year: Int, day: Int, title: String = "") : AdventOfCodeDay(year, day, title) {
            fun partOne(data: String): Int {
                return 0
            }

            fun partTwo(data: String): Int {
                return 0
            }
        }
    """.trimIndent()

    val file = File(fileName)
    file.parentFile.mkdirs()

    if (file.exists()) {
        println("File $fileName already exists.")
        return
    }
    file.writeText(fileContent)

    // Generate the data file
    val dataFileName = "src/main/resources/me/nicolas/adventofcode/$packageName/day${day.toString().padStart(2, '0')}/data.txt"
    val dataFile = File(dataFileName)
    dataFile.parentFile.mkdirs()
    dataFile.writeText("")

    // Generate the riddle file
    val riddleFileName = "src/main/resources/me/nicolas/adventofcode/$packageName/day${day.toString().padStart(2, '0')}/riddle.txt"
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
            private val day = $className($year, $day)
            
            val test = ""${'"'}
                
            ""${'"'}.trimIndent()

            @Test
            fun partOne() {
                assertEquals(0, day.partOne(test))
            }

            @Test
            fun partTwo() {
                assertEquals(0, day.partTwo(test))
            }
        }
    """.trimIndent()

    val testFile = File(testFileName)
    testFile.parentFile.mkdirs()
    testFile.writeText(testFileContent)

    println("File $fileName has been generated.")
}