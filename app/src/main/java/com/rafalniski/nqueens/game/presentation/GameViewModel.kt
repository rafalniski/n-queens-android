package com.rafalniski.nqueens.game.presentation

import androidx.lifecycle.ViewModel
import com.rafalniski.nqueens.game.domain.GameState
import com.rafalniski.nqueens.game.domain.NQueensEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(
    initialBoardSize: Int = DEFAULT_BOARD_SIZE,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        GameUiState(
            game = GameState(boardSize = initialBoardSize),
        ),
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun onAction(action: GameAction) {
        when (action) {
            is GameAction.CellTapped -> {
                val updatedGame = NQueensEngine.toggleQueen(
                    state = _uiState.value.game,
                    position = action.position,
                )
                updateGame(updatedGame)
            }

            is GameAction.BoardSizeSelected -> {
                updateGame(
                    GameState(boardSize = action.boardSize),
                )
            }

            GameAction.ResetClicked,
            GameAction.PlayAgainClicked,
            -> {
                resetGame()
            }
        }
    }

    private fun resetGame() {
        updateGame(
            GameState(boardSize = _uiState.value.boardSize),
        )
    }

    private fun updateGame(game: GameState) {
        _uiState.value = GameUiState(game = game)
    }

    companion object {
        const val DEFAULT_BOARD_SIZE = 8
    }
}
