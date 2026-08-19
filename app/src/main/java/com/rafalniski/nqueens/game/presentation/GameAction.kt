package com.rafalniski.nqueens.game.presentation

import com.rafalniski.nqueens.game.domain.Position

sealed interface GameAction {
    data class CellTapped(
        val position: Position,
    ) : GameAction

    data class BoardSizeSelected(
        val boardSize: Int,
    ) : GameAction

    data object ResetClicked : GameAction

    data object PlayAgainClicked : GameAction
}
