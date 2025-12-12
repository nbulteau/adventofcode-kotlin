package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.utils.readFileDirectlyAsText
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day21Test {

    private val data = """
        mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
        trh fvjkl sbzzf mxmxvkd (contains dairy)
        sqjhc fvjkl (contains soy)
        sqjhc mxmxvkd sbzzf (contains fish)
    """.trimIndent()

    private val day = Day21(2020, 21, "Allergen Assessment")

    @Test
    fun `part one`() {
        assertEquals(5, day.partOne(data))
    }

    @Test
    fun `part two`() {
        assertEquals("mxmxvkd,sqjhc,fvjkl", day.partTwo(data))
    }
}

