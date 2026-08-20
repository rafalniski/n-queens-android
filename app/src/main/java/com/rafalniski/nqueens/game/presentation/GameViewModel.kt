package com.rafalniski.nqueens.game.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafalniski.nqueens.game.domain.BestTimesRepository
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val bestTimesRepository: BestTimesRepository,
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
    private var bestTimesJob: Job? = null

    init {
        observeBestTimes(initialBoardSize)
    }

    fun onAction(action: GameAction) {
        when (action) {
            is GameAction.CellTapped -> {
                handleCellTapped(action.position)
            }

            is GameAction.BoardSizeSelected -> {
                if (_uiState.value.status == GameStatus.Ready) {
                    startNewGame(action.boardSize)
                }
            }

            GameAction.StartGameClicked -> {
                startGame()
            }

            GameAction.ResetClicked,
            GameAction.PlayAgainClicked,
            -> {
                startNewGame(_uiState.value.boardSize)
            }

            GameAction.BestTimesClicked -> {
                setBestTimesVisibility(isVisible = true)
            }

            GameAction.BestTimesDismissed -> {
                setBestTimesVisibility(isVisible = false)
            }
        }
    }

    private fun handleCellTapped(position: Position) {
        if (_uiState.value.status != GameStatus.Playing) {
            return
        }

        val currentGame = _uiState.value.game
        val updatedGame = NQueensEngine.toggleQueen(
            state = currentGame,
            position = position,
        )
        val isSolved = NQueensEngine.isSolved(updatedGame)

        _uiState.update { state ->
            state.copy(
                game = updatedGame,
                status = if (isSolved) {
                    GameStatus.Won
                } else {
                    GameStatus.Playing
                },
            )
        }

        if (isSolved) {
            val elapsedTimeMillis = stopTimer()
            saveCompletedTime(
                boardSize = updatedGame.boardSize,
                elapsedTimeMillis = elapsedTimeMillis,
            )
        }
    }

    private fun startGame() {
        if (_uiState.value.status != GameStatus.Ready) {
            return
        }

        _uiState.update { state ->
            state.copy(status = GameStatus.Playing)
        }
        startTimer()
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

    private fun stopTimer(): Long {
        timerJob?.cancel()
        timerJob = null

        val elapsedTimeMillis = gameTimer.stop()

        _uiState.update { state ->
            state.copy(
                elapsedTimeMillis = elapsedTimeMillis,
            )
        }

        return elapsedTimeMillis
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
        observeBestTimes(boardSize)
    }

    private fun observeBestTimes(boardSize: Int) {
        bestTimesJob?.cancel()
        bestTimesJob = viewModelScope.launch {
            bestTimesRepository.observeBestTimes(boardSize)
                .collectLatest { bestTimes ->
                    _uiState.update { state ->
                        if (state.boardSize == boardSize) {
                            state.copy(bestTimes = bestTimes)
                        } else {
                            state
                        }
                    }
                }
        }
    }

    private fun saveCompletedTime(
        boardSize: Int,
        elapsedTimeMillis: Long,
    ) {
        viewModelScope.launch {
            bestTimesRepository.saveCompletedTime(
                boardSize = boardSize,
                elapsedTimeMillis = elapsedTimeMillis,
            )
        }
    }

    private fun setBestTimesVisibility(isVisible: Boolean) {
        _uiState.update { state ->
            state.copy(isBestTimesVisible = isVisible)
        }
    }

    companion object {
        const val DEFAULT_BOARD_SIZE = 8

        private const val TIMER_UPDATE_INTERVAL_MILLIS = 1_000L
    }
}
