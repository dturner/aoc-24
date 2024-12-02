package dturner.aoc24.day02

import dturner.aoc24.utils.*
import kotlin.math.abs

fun main() {

    val testInput = """
7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9
""".split("\n").filter { it.isNotEmpty() }

    check(part1(testInput) == 2)

    val input = readInput("Day02")
    part1(input).println()

    check(part2(testInput) == 4)
    part2(input).println()
}

fun part1(input: List<String>): Int {
    val reports = input.map {
        it.split(" ").map { level ->
            level.toInt()
        }
    }
    return reports.count { it.isSafe() }
}

fun part2(input: List<String>): Int {
    val reports = input.map {
        it.split(" ").map { level ->
            level.toInt()
        }
    }

    return reports.count { report ->
        report.dampenedReports().any { it.isSafe() }
    }
}

fun List<Int>.isSafe(): Boolean {
    // Rule 1: Values are increasing or decreasing.
    return if (isIncreasing() || isDecreasing()){
        // Rule 2: Any two adjacent levels differ by at least one and at most three.
        isWithinSafeChanges()
    } else {
        false
    }
}

fun List<Int>.isIncreasing() =
    zipWithNext().all { it.first < it.second }

fun List<Int>.isDecreasing() =
    zipWithNext().all { it.first > it.second }

fun List<Int>.isWithinSafeChanges() =
    zipWithNext().all { abs(it.first - it.second) in 1..3 }

fun List<Int>.dampenedReports() : List<List<Int>> {

    val dampenedReports = mutableListOf(this)

    // Sequentially remove one level to create new reports.
    forEachIndexed { parentIndex, _ ->
        dampenedReports.add(this.filterIndexed { childIndex, _ -> childIndex != parentIndex })
    }

    return dampenedReports
}