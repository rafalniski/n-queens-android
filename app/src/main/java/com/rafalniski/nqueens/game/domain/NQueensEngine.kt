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
