package com.rafalniski.nqueens.game.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafalniski.nqueens.game.domain.GameState
import com.rafalniski.nqueens.game.domain.NQueensEngine
import com.rafalniski.nqueens.game.domain.Position
import com.rafalniski.nqueens.game.timer.GameTimer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    initialBoardSize: Int = DEFAULT_BOARD_SIZE,
    private val gameTimer: GameTimer = GameTimer(),
    private val timerUpdateIntervalMillis: Long = TIMER_UPDATE_INTERVAL_MILLIS,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        GameUiState(
            game = GameState(boardSize = initialBoardSize),
        ),
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun onAction(action: GameAction) {
        when (action) {
            is GameAction.CellTapped -> {
                handleCellTapped(action.position)
            }

            is GameAction.BoardSizeSelected -> {
                startNewGame(action.boardSize)
            }

            GameAction.ResetClicked,
            GameAction.PlayAgainClicked,
            -> {
                startNewGame(_uiState.value.boardSize)
            }
        }
    }

    private fun handleCellTapped(position: Position) {
        val currentGame = _uiState.value.game
        val updatedGame = NQueensEngine.toggleQueen(
            state = currentGame,
            position = position,
        )

        updateGame(updatedGame)

        val isFirstMove = currentGame.queens.isEmpty() &&
            updatedGame.queens.isNotEmpty()

        if (isFirstMove && !gameTimer.isRunning) {
            startTimer()
        }

        if (NQueensEngine.isSolved(updatedGame)) {
            stopTimer()
        }
    }

    private fun startTimer() {
        gameTimer.start()
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(timerUpdateIntervalMillis)
                updateElapsedTime()
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null

        val elapsedTimeMillis = gameTimer.stop()

        _uiState.update { state ->
            state.copy(
                elapsedTimeMillis = elapsedTimeMillis,
            )
        }
    }

    private fun updateElapsedTime() {
        _uiState.update { state ->
            state.copy(
                elapsedTimeMillis = gameTimer.elapsedTimeMillis(),
            )
        }
    }

    private fun startNewGame(boardSize: Int) {
        timerJob?.cancel()
        timerJob = null
        gameTimer.reset()

        _uiState.value = GameUiState(
            game = GameState(boardSize = boardSize),
        )
    }

    private fun updateGame(game: GameState) {
        _uiState.update { state ->
            state.copy(game = game)
        }
    }

    companion object {
        const val DEFAULT_BOARD_SIZE = 8

        private const val TIMER_UPDATE_INTERVAL_MILLIS = 1_000L
    }
}
