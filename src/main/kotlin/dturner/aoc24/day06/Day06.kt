package dturner.aoc24.day06

import dturner.aoc24.utils.readInput

fun main() {

    val testInput = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent().split("\n").filter { it.isNotEmpty() }
    val part1result = part1(testInput)
    println(part1result)
    check(part1result == 41)
    val input = readInput("Day06")
    check(part1(input) == 5461)

    val part2result = part2(testInput)
    println(part2result)
    check(part2result == 6)
    check(part2(input) == 1836)
}

fun part1(input: List<String>): Int {
    val labMap = input.toLabMap()
    labMap.patrol()
    return labMap.visitedPositions.size
}

fun part2(input: List<String>): Int {

    val labMap = input.toLabMap()
    labMap.patrol()

    // The visited positions represent the possible points for obstacles.
    val possibleObstacles = labMap.visitedPositions.toMutableSet()
    possibleObstacles.remove(labMap.startingGuardPose.position)

    var loopCount = 0

    possibleObstacles.forEach { newObstacle ->
        val mapWithNewObstacle = labMap.copy(obstacles = labMap.obstacles + newObstacle)
        if (!mapWithNewObstacle.patrol()) loopCount++
    }

    return loopCount
}

enum class Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    fun rotate90() = Direction.entries[(ordinal + 1) % Direction.entries.size]
}

data class Point(val x: Int, val y: Int)

data class GuardPose(val x: Int, val y: Int, val direction: Direction){
    val position get() = Point(x, y)

    fun rotate90() = copy(direction = direction.rotate90())

    fun moveForward() =
        when(direction){
            Direction.UP -> copy(y = y - 1)
            Direction.RIGHT -> copy(x = x + 1)
            Direction.DOWN -> copy(y = y + 1)
            Direction.LEFT -> copy(x = x - 1)
        }
}

fun List<String>.toLabMap() : LabMap {

    val area = map { it.toCharArray() }
    val obstacles = mutableSetOf<Point>()
    val directions = mapOf(
        '<' to Direction.LEFT,
        '>' to Direction.RIGHT,
        '^' to Direction.UP,
        'v' to Direction.DOWN,
    )
    lateinit var guardPose: GuardPose

    area.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, char ->
            val direction = directions[char]
            if (direction != null) {
                guardPose = GuardPose(
                    x = columnIndex,
                    y = rowIndex,
                    direction = direction
                )
            }
            if (char == '#') obstacles.add(Point(columnIndex, rowIndex))
        }
    }

    return LabMap(
        width = first().length,
        height = size,
        startingGuardPose = guardPose,
        obstacles = obstacles
    )
}

class LabMap(
    val width: Int,
    val height: Int,
    val startingGuardPose: GuardPose,
    val obstacles: Set<Point>) {

    private var guardPose = startingGuardPose
    private val visitedPoses = mutableSetOf<GuardPose>()

    val visitedPositions : Set<Point>
        get() = visitedPoses.map { Point(it.x, it.y) }.toSet()

    fun patrol() : Boolean {
        while (isWithinBounds(guardPose.position)) {
            visitedPoses.add(guardPose)
            val targetPose = guardPose.moveForward()
            if (visitedPoses.contains(targetPose)) {
                // This is a loop.
                return false
            } else if (obstacles.contains(targetPose.position)) {
                guardPose = guardPose.rotate90()
            } else {
                guardPose = guardPose.moveForward()
            }
        }
        return true
    }

    fun copy(width: Int = this.width,
             height: Int = this.height,
             startingGuardPose: GuardPose = this.startingGuardPose,
             obstacles : Set<Point> = this.obstacles
    ) = LabMap(width, height, startingGuardPose, obstacles)

    private fun isWithinBounds(point: Point) =
        point.x >= 0 &&
        point.y >= 0 &&
        point.x < width &&
        point.y < height
}
