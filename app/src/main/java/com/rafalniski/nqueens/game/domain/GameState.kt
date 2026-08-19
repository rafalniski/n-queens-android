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

        require(queens.all { position -> isPositionInsideBoard(position) }) {
            "Every queen must be inside the board."
        }
    }

    fun isPositionInsideBoard(position: Position): Boolean {
        return position.row in 0 until boardSize &&
            position.column in 0 until boardSize
    }

    companion object {
        const val MIN_BOARD_SIZE = 4
    }
}
