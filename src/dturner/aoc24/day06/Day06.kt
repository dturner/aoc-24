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
    check(part1(testInput) == 41)
    val input = readInput("Day06")
    check(part1(input) == 5461)

    check(part2(testInput) == 6)
    println(part2(input))
}

fun part1(input: List<String>): Int {

    // Map the maze to a 2D array of chars
    val area = input.map { it.toCharArray() }

    lateinit var guard: Guard

    val obstacles = mutableSetOf<Point>()

    area.forEachIndexed { y, row ->
        // parse the whole maze
        if (row.contains('^')) {
            guard = Guard(Direction.UP, Point(row.indexOf('^'), y))
        } else if (row.contains('>')) {
            guard = Guard(Direction.RIGHT, Point(row.indexOf('>'), y))
        } else if (row.contains('v')) {
            guard = Guard(Direction.DOWN, Point(row.indexOf('v'), y))
        } else if (row.contains('<')) {
            guard = Guard(Direction.LEFT, Point(row.indexOf('<'), y))
        }

        row.forEachIndexed { x, char ->
            if (char == '#') obstacles.add(Point(x, y))
        }
    }

    val areaWidth = area.first().size
    val areaHeight = area.size
    val distinctPositions = mutableSetOf(guard.position)

    while (
        guard.position.x >= 0 &&
        guard.position.y >= 0 &&
        guard.position.x < areaWidth &&
        guard.position.y < areaHeight
    ) {
        val targetPosition = guard.facing
        if (obstacles.contains(targetPosition)) {
            guard.rotate90()
        } else {
            guard.moveForward()
            distinctPositions.add(guard.position)
        }
    }
    return distinctPositions.size - 1 // Subtract one for the move out of the area.
}

fun part2(input: List<String>): Int {

    // Map the maze to a 2D array of chars
    val area = input.map { it.toCharArray() }
    lateinit var guard: Guard

    val obstacles = mutableSetOf<Point>()

    area.forEachIndexed { y, row ->
        // parse the whole maze
        if (row.contains('^')) {
            guard = Guard(Direction.UP, Point(row.indexOf('^'), y))
        } else if (row.contains('>')) {
            guard = Guard(Direction.RIGHT, Point(row.indexOf('>'), y))
        } else if (row.contains('v')) {
            guard = Guard(Direction.DOWN, Point(row.indexOf('v'), y))
        } else if (row.contains('<')) {
            guard = Guard(Direction.LEFT, Point(row.indexOf('<'), y))
        }

        row.forEachIndexed { x, char ->
            if (char == '#') obstacles.add(Point(x, y))
        }
    }

    val guardStartingPoint = PositionWithDirection(guard.position, guard.direction)

    val areaWidth = area.first().size
    val areaHeight = area.size
    val distinctPositions = mutableSetOf(guard.position)
    val possibleObstaclePositions = mutableSetOf<Point>()

    while (
        guard.position.x >= 0 &&
        guard.position.y >= 0 &&
        guard.position.x < areaWidth &&
        guard.position.y < areaHeight
    ) {
        val targetPosition = guard.facing
        if (obstacles.contains(targetPosition)) {
            guard.rotate90()
        } else {
            // Try putting an obstacle here
            possibleObstaclePositions.add(targetPosition)

            guard.moveForward()
            distinctPositions.add(guard.position)

        }
    }

    println(possibleObstaclePositions)

    var loopingObstaclesCount = 0

    print("Looking for possible obstacle positions with guard at $guard")

    // For each one of the possible obstacles, find the ones which cause a loop
    possibleObstaclePositions.forEach { newObstacle ->

        guard = Guard(guardStartingPoint.direction, guardStartingPoint.position)

        println("Is there a loop with an obstacle at $newObstacle?")
        val obstaclesWithNewOne = obstacles + newObstacle

        val visitedPositions = mutableSetOf(PositionWithDirection(guard.position, guard.direction))

        while (
            guard.position.x >= 0 &&
            guard.position.y >= 0 &&
            guard.position.x < areaWidth &&
            guard.position.y < areaHeight
        ) {
            val targetPositionWithDirection = PositionWithDirection(guard.facing, guard.direction)
            if (visitedPositions.contains(targetPositionWithDirection)){
                // This is a loop. We have found a valid obstacle position

                loopingObstaclesCount++
                break
            } else if (obstaclesWithNewOne.contains(guard.facing)) {
                guard.rotate90()
            } else {
                guard.moveForward()
                visitedPositions.add(PositionWithDirection(guard.position, guard.direction))
            }
        }
    }

    return loopingObstaclesCount
}

class Guard(startingDirection: Direction, startingPosition: Point) {

    var direction = startingDirection
    var position = startingPosition
        private set

    val facing: Point
        get() = when (direction) {
            Direction.UP -> Point(position.x, position.y - 1)
            Direction.RIGHT -> Point(position.x + 1, position.y)
            Direction.DOWN -> Point(position.x, position.y + 1)
            Direction.LEFT -> Point(position.x - 1, position.y)
        }

    fun rotate90() {
        // If direction is an int we can easily wrap around, rather than using an enum
        direction = when (direction) {
            Direction.UP -> Direction.RIGHT
            Direction.RIGHT -> Direction.DOWN
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
        }
    }

    fun moveForward() {
        position = facing
    }

    override fun toString(): String = "Direction($direction) Position (${position.x}, ${position.y})"
}

enum class Direction {
    UP,
    RIGHT,
    LEFT,
    DOWN
}

data class Point(val x: Int, val y: Int)

data class PositionWithDirection(val position: Point, val direction: Direction)