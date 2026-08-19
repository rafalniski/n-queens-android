package com.rafalniski.nqueens.game.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GameStateTest {
    @Test
    fun `new game has an empty set of queens`() {
        val state = GameState(boardSize = 4)

        assertTrue(state.queens.isEmpty())
    }

    @Test
    fun `board size of four is accepted`() {
        val state = GameState(boardSize = 4)

        assertEquals(4, state.boardSize)
    }

    @Test
    fun `board size below four is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            GameState(boardSize = 3)
        }
    }

    @Test
    fun `queen inside the board is accepted`() {
        val queen = Position(row = 3, column = 3)

        val state = GameState(
            boardSize = 4,
            queens = setOf(queen),
        )

        assertEquals(setOf(queen), state.queens)
    }

    @Test
    fun `queen with row outside the board is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            GameState(
                boardSize = 4,
                queens = setOf(Position(row = 4, column = 0)),
            )
        }
    }

    @Test
    fun `queen with column outside the board is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            GameState(
                boardSize = 4,
                queens = setOf(Position(row = 0, column = 4)),
            )
        }
    }

    @Test
    fun `more queens than the board size is rejected`() {
        val queens = setOf(
            Position(row = 0, column = 0),
            Position(row = 0, column = 1),
            Position(row = 0, column = 2),
            Position(row = 0, column = 3),
            Position(row = 1, column = 0),
        )

        assertFailsWith<IllegalArgumentException> {
            GameState(
                boardSize = 4,
                queens = queens,
            )
        }
    }
}
