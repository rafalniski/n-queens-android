package com.rafalniski.nqueens.game.domain

data class GameState(
    val boardSize: Int,
    val queens: Set<Position> = emptySet(),
) {
    init {
        require(boardSize >= MIN_BOARD_SIZE) {
            "Board size must be at least $MIN_BOARD_SIZE."
        }

        require(queens.size <= boardSize) {
            "The number of queens must not exceed the board size."
        }

        require(queens.all { position -> position.isInsideBoard() }) {
            "Every queen must be inside the board."
        }
    }

    private fun Position.isInsideBoard(): Boolean {
        return row in 0 until boardSize &&
            column in 0 until boardSize
    }

    companion object {
        const val MIN_BOARD_SIZE = 4
    }
}
