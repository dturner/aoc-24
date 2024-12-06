package dturner.aoc24.day06

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day06KtTest {

    @Test
    fun testToLabMap() {
        val testInput = """
        .^.
        ...
    """.trimIndent().split("\n").filter { it.isNotEmpty() }

        val expected = LabMap(
            width = 3,
            height = 2,
            startingGuardPose = GuardPose(1, 0, Direction.UP),
            obstacles = emptySet()
        )

        assertEquals(expected, testInput.toLabMap())
    }

    @Test
    fun testRotate90() {
        var direction = Direction.UP
        direction = direction.rotate90()
        assertEquals(Direction.RIGHT, direction)
        direction = direction.rotate90()
        assertEquals(Direction.DOWN, direction)
        direction = direction.rotate90()
        assertEquals(Direction.LEFT, direction)
        direction = direction.rotate90()
        assertEquals(Direction.UP, direction)
    }
}