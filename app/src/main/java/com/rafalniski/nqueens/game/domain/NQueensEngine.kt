package com.rafalniski.nqueens.game.domain

import kotlin.math.abs

object NQueensEngine {
    fun areThreatening(
        first: Position,
        second: Position,
    ): Boolean {
        val sameRow = first.row == second.row
        val sameColumn = first.column == second.column
        val sameDiagonal =
            abs(first.row - second.row) ==
                abs(first.column - second.column)

        return sameRow || sameColumn || sameDiagonal
    }

    fun toggleQueen(
        state: GameState,
        position: Position,
    ): GameState {
        require(state.isPositionInsideBoard(position)) {
            "Position must be inside the board."
        }
        val updatedQueens = when {
            position in state.queens -> state.queens - position
            state.queens.size < state.boardSize -> state.queens + position
            else -> state.queens
        }
        return state.copy(queens = updatedQueens)
    }

    fun isSolved(state: GameState): Boolean {
        val hasRequiredNumberOfQueens =
            state.queens.size == state.boardSize

        val hasNoConflicts = findConflictingQueens(state.queens).isEmpty()

        return hasRequiredNumberOfQueens && hasNoConflicts
    }

    fun findConflictingQueens(
        queens: Set<Position>,
    ): Set<Position> {
        return queens
            .filter { queen ->
                queens.any { other ->
                    queen != other && areThreatening(queen, other)
                }
            }
            .toSet()
    }
}
