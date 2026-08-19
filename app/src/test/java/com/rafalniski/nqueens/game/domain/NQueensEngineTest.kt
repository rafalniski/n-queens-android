package com.rafalniski.nqueens.game.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NQueensEngineTest {
    @Test
    fun `queens in the same row threaten each other`() {
        val first = Position(row = 1, column = 0)
        val second = Position(row = 1, column = 3)

        val result = NQueensEngine.areThreatening(first, second)

        assertTrue(result)
    }

    @Test
    fun `queens in the same column threaten each other`() {
        val first = Position(row = 0, column = 2)
        val second = Position(row = 3, column = 2)

        val result = NQueensEngine.areThreatening(first, second)

        assertTrue(result)
    }

    @Test
    fun `queens on descending diagonal threaten each other`() {
        val first = Position(row = 0, column = 0)
        val second = Position(row = 3, column = 3)

        val result = NQueensEngine.areThreatening(first, second)

        assertTrue(result)
    }

    @Test
    fun `queens on ascending diagonal threaten each other`() {
        val first = Position(row = 0, column = 3)
        val second = Position(row = 3, column = 0)

        val result = NQueensEngine.areThreatening(first, second)

        assertTrue(result)
    }

    @Test
    fun `queens with different row column and diagonal do not threaten each other`() {
        val first = Position(row = 0, column = 0)
        val second = Position(row = 1, column = 2)

        val result = NQueensEngine.areThreatening(first, second)

        assertFalse(result)
    }

    @Test
    fun `empty set has no conflicting queens`() {
        val result = NQueensEngine.findConflictingQueens(emptySet())

        assertTrue(result.isEmpty())
    }

    @Test
    fun `single queen is not conflicting with itself`() {
        val queen = Position(row = 2, column = 2)

        val result = NQueensEngine.findConflictingQueens(setOf(queen))

        assertTrue(result.isEmpty())
    }

    @Test
    fun `both threatening queens are returned as conflicting`() {
        val first = Position(row = 1, column = 0)
        val second = Position(row = 1, column = 3)

        val result = NQueensEngine.findConflictingQueens(
            setOf(first, second),
        )

        assertEquals(setOf(first, second), result)
    }

    @Test
    fun `non-conflicting queens are not returned`() {
        val conflictingFirst = Position(row = 0, column = 0)
        val conflictingSecond = Position(row = 0, column = 3)
        val safeQueen = Position(row = 3, column = 1)

        val result = NQueensEngine.findConflictingQueens(
            setOf(
                conflictingFirst,
                conflictingSecond,
                safeQueen,
            ),
        )

        assertEquals(
            setOf(conflictingFirst, conflictingSecond),
            result,
        )
    }
}
