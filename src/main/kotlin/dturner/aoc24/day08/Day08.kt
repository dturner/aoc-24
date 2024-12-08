package dturner.aoc24.day08

import dturner.aoc24.utils.readInput

fun main() {

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    return getAntinodeCount(input, ::findAntinodesPart1)
}

fun part2(input: List<String>): Int {
    return getAntinodeCount(input, ::findAntinodesPart2)
}

private fun getAntinodeCount(
    input: List<String>,
    findAntinodeStrategy : (antennaPair: Pair<Point, Point>, mapBounds: Point) -> List<Point>): Int
{
    val antennas = findAntennas(input)
    val mapBounds = Point(input.first().length, input.size)

    return antennas.values
        .flatMap { antenna -> findPairs(antenna) }
        .flatMap { antennaPair -> findAntinodeStrategy(antennaPair, mapBounds) }
        .toSet()
        .size
}

fun findAntinodesPart1(antennaPair: Pair<Point, Point>, mapBounds: Point) : List<Point> {
    val difference = antennaPair.second - antennaPair.first
    return listOf(antennaPair.first - difference, antennaPair.second + difference)
        .filter { it.isWithin(mapBounds) }
}

fun findAntinodesPart2(antennaPair: Pair<Point, Point>, mapSize: Point) : List<Point> {

    val difference = antennaPair.second - antennaPair.first

    val backwardNodes = generateSequence(antennaPair.first - difference) { it - difference }
        .takeWhile { it.isWithin(mapSize) }
        .toList()

    val forwardNodes = generateSequence(antennaPair.second + difference) { it + difference }
        .takeWhile { it.isWithin(mapSize) }
        .toList()

    return listOf(antennaPair.first, antennaPair.second) + backwardNodes + forwardNodes
}

fun <T> findPairs(input: List<T>) : List<Pair<T, T>> {
    return input.flatMapIndexed { index, firstItem ->
        input.subList(index + 1, input.size).map { secondItem ->
            firstItem to secondItem
        }
    }.toList()
}

fun findAntennas(input: List<String>) : Map<Char, List<Point>> {
    return input.flatMapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { colIndex, char ->
            if (char.isLetterOrDigit()) {
                char to Point(colIndex, rowIndex)
            } else {
                null
            }
        }
    }.groupBy({ it.first }, { it.second })
}

data class Point(var x: Int, var y: Int){
    fun isWithin(bounds: Point) : Boolean =
        x >= 0 && y >= 0 && x < bounds.x && y < bounds.y
    operator fun minus(point: Point) : Point {
        return Point(x - point.x, y - point.y)
    }
    operator fun plus(point: Point) : Point {
        return Point(x + point.x, y + point.y)
    }
}

