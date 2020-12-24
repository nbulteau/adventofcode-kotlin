package me.nicolas.adventofcode.year2020

import me.nicolas.adventofcode.readFileDirectlyAsText

// --- Day 4: Passport Processing ---
// https://adventofcode.com/2020/day/4
fun main() {

    val training = readFileDirectlyAsText("/year2020/day04/training.txt")
    val data = readFileDirectlyAsText("/year2020/day04/data.txt")

    val passports = data.split("\n\n").map { string -> string.replace("\n", " ") }

    val invalidPassports = listOf(
        "eyr:1972 cid:100 hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926",
        "iyr:2019 hcl:#602927 eyr:1967 hgt:170cm ecl:grn pid:012533040 byr:1946",
        "hcl:dab227 iyr:2012 ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277",
        "hgt:59cm ecl:zzz eyr:2038 hcl:74454a iyr:2023 pid:3556412378 byr:2007"
    )
    val validPassports = listOf(
        "pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980 hcl:#623a2f",
        "eyr:2029 ecl:blu cid:129 byr:1989 iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm",
        "hcl:#888785 hgt:164cm byr:2001 iyr:2015 cid:88 pid:545766238 ecl:hzl eyr:2022",
        "iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719"
    )

    // Part One
    partOne(passports)

    // Part Two
    partTwo(passports)
}

private fun partOne(batch: List<String>) {
    val nbValidPassports = batch
        .map { record ->
            record.split(" ")
                .map { entry -> entry.substringBefore(":") to entry.substringAfter(":") }.toMap()
        }
        .count { map ->
            map.size == 8
                    || map.size == 7 && !map.containsKey("cid")
        }

    println(nbValidPassports)
}

private fun partTwo(batch: List<String>) {
    val nbValidPassports = batch
        .map { record ->
            record.split(" ")
                .map { entry -> entry.substringBefore(":") to entry.substringAfter(":") }.toMap()
        }
        .count { map ->
            map["byr"]?.toInt() in 1920..2002
                    && map["iyr"]?.toInt() in 2010..2020
                    && map["eyr"]?.toInt() in 2020..2030
                    && map["hgt"].isValidHeight()
                    && map["hcl"]?.matches("#[0-9a-f]{6}".toRegex()) == true
                    && map["ecl"] in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
                    && map["pid"]?.matches("\\d+(\\.\\d+)?".toRegex()) == true && map["pid"]?.length == 9
        }

    println(nbValidPassports)
}

private fun String?.isValidHeight() = when {
    this?.contains("cm") == true -> this.substringBefore("cm").toInt() in 150..193
    this?.contains("in") == true -> this.substringBefore("in").toInt() in 59..76
    else -> false
}


