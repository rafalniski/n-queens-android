package com.rafalniski.nqueens.game.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
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

    @Test
    fun `tapping empty position adds a queen`() {
        val state = GameState(boardSize = 4)
        val position = Position(row = 1, column = 2)

        val result = NQueensEngine.toggleQueen(state, position)

        assertEquals(setOf(position), result.queens)
    }

    @Test
    fun `tapping occupied position removes the queen`() {
        val position = Position(row = 1, column = 2)
        val state = GameState(
            boardSize = 4,
            queens = setOf(position),
        )

        val result = NQueensEngine.toggleQueen(state, position)

        assertTrue(result.queens.isEmpty())
    }

    @Test
    fun `toggling queen does not modify the original state`() {
        val state = GameState(boardSize = 4)
        val position = Position(row = 1, column = 2)

        NQueensEngine.toggleQueen(state, position)

        assertTrue(state.queens.isEmpty())
    }

    @Test
    fun `queen is not added when board already has required number of queens`() {
        val existingQueens = setOf(
            Position(row = 0, column = 0),
            Position(row = 0, column = 1),
            Position(row = 0, column = 2),
            Position(row = 0, column = 3),
        )
        val state = GameState(
            boardSize = 4,
            queens = existingQueens,
        )

        val result = NQueensEngine.toggleQueen(
            state = state,
            position = Position(row = 1, column = 0),
        )

        assertEquals(existingQueens, result.queens)
    }

    @Test
    fun `position outside board is rejected`() {
        val state = GameState(boardSize = 4)

        assertFailsWith<IllegalArgumentException> {
            NQueensEngine.toggleQueen(
                state = state,
                position = Position(row = 4, column = 0),
            )
        }
    }

    @Test
    fun `complete non-conflicting board is solved`() {
        val state = GameState(
            boardSize = 4,
            queens = setOf(
                Position(row = 0, column = 1),
                Position(row = 1, column = 3),
                Position(row = 2, column = 0),
                Position(row = 3, column = 2),
            ),
        )

        val result = NQueensEngine.isSolved(state)

        assertTrue(result)
    }

    @Test
    fun `board with fewer queens is not solved`() {
        val state = GameState(
            boardSize = 4,
            queens = setOf(
                Position(row = 0, column = 1),
                Position(row = 1, column = 3),
                Position(row = 2, column = 0),
            ),
        )

        val result = NQueensEngine.isSolved(state)

        assertFalse(result)
    }

    @Test
    fun `complete board with conflicts is not solved`() {
        val state = GameState(
            boardSize = 4,
            queens = setOf(
                Position(row = 0, column = 0),
                Position(row = 0, column = 1),
                Position(row = 0, column = 2),
                Position(row = 0, column = 3),
            ),
        )

        val result = NQueensEngine.isSolved(state)

        assertFalse(result)
    }
}
