package com.rafalniski.nqueens.game.presentation

import com.rafalniski.nqueens.game.domain.Position
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameViewModelTest {
    @Test
    fun `initial state contains an empty eight by eight board`() {
        val viewModel = GameViewModel()

        val state = viewModel.uiState.value

        assertEquals(8, state.boardSize)
        assertTrue(state.queens.isEmpty())
        assertEquals(8, state.queensLeft)
        assertTrue(state.conflictingQueens.isEmpty())
        assertFalse(state.isSolved)
    }

    @Test
    fun `cell tap places a queen`() {
        val viewModel = GameViewModel(initialBoardSize = 4)
        val position = Position(row = 1, column = 2)

        viewModel.onAction(
            GameAction.CellTapped(position),
        )

        assertEquals(
            setOf(position),
            viewModel.uiState.value.queens,
        )
    }

    @Test
    fun `second tap on occupied cell removes queen`() {
        val viewModel = GameViewModel(initialBoardSize = 4)
        val position = Position(row = 1, column = 2)

        viewModel.onAction(GameAction.CellTapped(position))
        viewModel.onAction(GameAction.CellTapped(position))

        assertTrue(viewModel.uiState.value.queens.isEmpty())
    }

    @Test
    fun `conflicts are available in UI state`() {
        val viewModel = GameViewModel(initialBoardSize = 4)
        val first = Position(row = 0, column = 0)
        val second = Position(row = 0, column = 3)

        viewModel.onAction(GameAction.CellTapped(first))
        viewModel.onAction(GameAction.CellTapped(second))

        assertEquals(
            setOf(first, second),
            viewModel.uiState.value.conflictingQueens,
        )
    }

    @Test
    fun `selecting board size starts empty game with selected size`() {
        val viewModel = GameViewModel(initialBoardSize = 4)
        viewModel.onAction(
            GameAction.CellTapped(
                Position(row = 0, column = 0),
            ),
        )

        viewModel.onAction(
            GameAction.BoardSizeSelected(boardSize = 6),
        )

        val state = viewModel.uiState.value
        assertEquals(6, state.boardSize)
        assertEquals(6, state.queensLeft)
        assertTrue(state.queens.isEmpty())
    }

    @Test
    fun `reset clears board and keeps selected size`() {
        val viewModel = GameViewModel(initialBoardSize = 6)
        viewModel.onAction(
            GameAction.CellTapped(
                Position(row = 0, column = 0),
            ),
        )

        viewModel.onAction(GameAction.ResetClicked)

        val state = viewModel.uiState.value
        assertEquals(6, state.boardSize)
        assertTrue(state.queens.isEmpty())
    }

    @Test
    fun `play again clears solved board and keeps selected size`() {
        val viewModel = GameViewModel(initialBoardSize = 4)
        placeFourByFourSolution(viewModel)
        assertTrue(viewModel.uiState.value.isSolved)

        viewModel.onAction(GameAction.PlayAgainClicked)

        val state = viewModel.uiState.value
        assertEquals(4, state.boardSize)
        assertTrue(state.queens.isEmpty())
        assertFalse(state.isSolved)
    }

    @Test
    fun `placing complete solution marks game as solved`() {
        val viewModel = GameViewModel(initialBoardSize = 4)

        placeFourByFourSolution(viewModel)

        assertTrue(viewModel.uiState.value.isSolved)
    }

    private fun placeFourByFourSolution(
        viewModel: GameViewModel,
    ) {
        val solution = setOf(
            Position(row = 0, column = 1),
            Position(row = 1, column = 3),
            Position(row = 2, column = 0),
            Position(row = 3, column = 2),
        )

        solution.forEach { position ->
            viewModel.onAction(
                GameAction.CellTapped(position),
            )
        }
    }
}
