package com.rafalniski.nqueens.game.presentation

import com.rafalniski.nqueens.game.domain.GameState
import com.rafalniski.nqueens.game.domain.NQueensEngine
import com.rafalniski.nqueens.game.domain.Position

data class GameUiState(
    val game: GameState,
) {
    val boardSize: Int
        get() = game.boardSize

    val queens: Set<Position>
        get() = game.queens

    val conflictingQueens: Set<Position>
        get() = NQueensEngine.findConflictingQueens(game.queens)

    val queensLeft: Int
        get() = game.boardSize - game.queens.size

    val isSolved: Boolean
        get() = NQueensEngine.isSolved(game)
}
