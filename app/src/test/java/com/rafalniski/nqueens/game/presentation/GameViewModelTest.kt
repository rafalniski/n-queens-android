package com.rafalniski.nqueens.game.presentation

import com.rafalniski.nqueens.game.MainDispatcherRule
import com.rafalniski.nqueens.game.data.FakeBestTimesRepository
import com.rafalniski.nqueens.game.domain.Position
import com.rafalniski.nqueens.game.timer.GameTimer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial state contains an empty eight by eight board`() {
        val viewModel = createViewModel()

        val state = viewModel.uiState.value

        assertEquals(8, state.boardSize)
        assertTrue(state.queens.isEmpty())
        assertEquals(8, state.queensLeft)
        assertTrue(state.conflictingQueens.isEmpty())
        assertFalse(state.isSolved)
        assertEquals(GameStatus.Ready, state.status)
    }

    @Test
    fun `starting game starts elapsed time updates`() {
        val viewModel = createViewModel(initialBoardSize = 4)

        viewModel.onAction(GameAction.StartGameClicked)

        assertEquals(GameStatus.Playing, viewModel.uiState.value.status)

        mainDispatcherRule.scheduler.advanceTimeBy(1_000L)
        mainDispatcherRule.scheduler.runCurrent()

        assertEquals(
            1_000L,
            viewModel.uiState.value.elapsedTimeMillis,
        )
    }

    @Test
    fun `cell tap before starting game is ignored`() {
        val viewModel = createViewModel(initialBoardSize = 4)

        viewModel.onAction(
            GameAction.CellTapped(Position(row = 0, column = 0)),
        )

        assertTrue(viewModel.uiState.value.queens.isEmpty())
        assertEquals(GameStatus.Ready, viewModel.uiState.value.status)
    }

    @Test
    fun `reset clears elapsed time`() {
        val viewModel = createViewModel(initialBoardSize = 4)

        viewModel.onAction(GameAction.StartGameClicked)
        viewModel.onAction(
            GameAction.CellTapped(
                Position(row = 0, column = 0),
            ),
        )

        mainDispatcherRule.scheduler.advanceTimeBy(2_000L)
        mainDispatcherRule.scheduler.runCurrent()

        viewModel.onAction(GameAction.ResetClicked)

        assertEquals(
            0L,
            viewModel.uiState.value.elapsedTimeMillis,
        )
    }

    @Test
    fun `solving game stops elapsed time`() {
        val viewModel = createViewModel(initialBoardSize = 4)

        viewModel.onAction(GameAction.StartGameClicked)
        viewModel.onAction(
            GameAction.CellTapped(
                Position(row = 0, column = 1),
            ),
        )

        mainDispatcherRule.scheduler.advanceTimeBy(2_500L)
        mainDispatcherRule.scheduler.runCurrent()

        viewModel.onAction(
            GameAction.CellTapped(Position(row = 1, column = 3)),
        )
        viewModel.onAction(
            GameAction.CellTapped(Position(row = 2, column = 0)),
        )
        viewModel.onAction(
            GameAction.CellTapped(Position(row = 3, column = 2)),
        )

        assertTrue(viewModel.uiState.value.isSolved)
        assertEquals(
            2_500L,
            viewModel.uiState.value.elapsedTimeMillis,
        )

        mainDispatcherRule.scheduler.advanceTimeBy(5_000L)
        mainDispatcherRule.scheduler.runCurrent()

        assertEquals(
            2_500L,
            viewModel.uiState.value.elapsedTimeMillis,
        )
    }

    @Test
    fun `cell tap places a queen`() {
        val viewModel = createViewModel(initialBoardSize = 4)
        val position = Position(row = 1, column = 2)

        viewModel.onAction(GameAction.StartGameClicked)
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
        val viewModel = createViewModel(initialBoardSize = 4)
        val position = Position(row = 1, column = 2)

        viewModel.onAction(GameAction.StartGameClicked)
        viewModel.onAction(GameAction.CellTapped(position))
        viewModel.onAction(GameAction.CellTapped(position))

        assertTrue(viewModel.uiState.value.queens.isEmpty())
    }

    @Test
    fun `conflicts are available in UI state`() {
        val viewModel = createViewModel(initialBoardSize = 4)
        val first = Position(row = 0, column = 0)
        val second = Position(row = 0, column = 3)

        viewModel.onAction(GameAction.StartGameClicked)
        viewModel.onAction(GameAction.CellTapped(first))
        viewModel.onAction(GameAction.CellTapped(second))

        assertEquals(
            setOf(first, second),
            viewModel.uiState.value.conflictingQueens,
        )
    }

    @Test
    fun `selecting board size prepares empty game with selected size`() {
        val viewModel = createViewModel(initialBoardSize = 4)

        viewModel.onAction(
            GameAction.BoardSizeSelected(boardSize = 6),
        )

        val state = viewModel.uiState.value
        assertEquals(6, state.boardSize)
        assertEquals(6, state.queensLeft)
        assertTrue(state.queens.isEmpty())
        assertEquals(GameStatus.Ready, state.status)
    }

    @Test
    fun `selecting board size during game is ignored`() {
        val viewModel = createViewModel(initialBoardSize = 4)

        viewModel.onAction(GameAction.StartGameClicked)
        viewModel.onAction(
            GameAction.BoardSizeSelected(boardSize = 6),
        )

        assertEquals(4, viewModel.uiState.value.boardSize)
    }

    @Test
    fun `reset clears board and keeps selected size`() {
        val viewModel = createViewModel(initialBoardSize = 6)
        viewModel.onAction(GameAction.StartGameClicked)
        viewModel.onAction(
            GameAction.CellTapped(
                Position(row = 0, column = 0),
            ),
        )

        viewModel.onAction(GameAction.ResetClicked)

        val state = viewModel.uiState.value
        assertEquals(6, state.boardSize)
        assertTrue(state.queens.isEmpty())
        assertEquals(GameStatus.Ready, state.status)
    }

    @Test
    fun `play again clears solved board and keeps selected size`() {
        val viewModel = createViewModel(initialBoardSize = 4)
        placeFourByFourSolution(viewModel)
        assertTrue(viewModel.uiState.value.isSolved)

        viewModel.onAction(GameAction.PlayAgainClicked)

        val state = viewModel.uiState.value
        assertEquals(4, state.boardSize)
        assertTrue(state.queens.isEmpty())
        assertFalse(state.isSolved)
        assertEquals(GameStatus.Ready, state.status)
    }

    @Test
    fun `placing complete solution marks game as solved`() {
        val viewModel = createViewModel(initialBoardSize = 4)

        placeFourByFourSolution(viewModel)

        assertTrue(viewModel.uiState.value.isSolved)
    }

    @Test
    fun `stored best times are exposed for selected board size`() {
        val repository = FakeBestTimesRepository().apply {
            setBestTimes(
                boardSize = 4,
                bestTimes = listOf(1_000L, 2_000L),
            )
        }
        val viewModel = createViewModel(
            initialBoardSize = 4,
            bestTimesRepository = repository,
        )

        mainDispatcherRule.scheduler.runCurrent()

        assertEquals(
            listOf(1_000L, 2_000L),
            viewModel.uiState.value.bestTimes,
        )
    }

    @Test
    fun `completed game is added to best times`() {
        val repository = FakeBestTimesRepository()
        val viewModel = createViewModel(
            initialBoardSize = 4,
            bestTimesRepository = repository,
        )
        viewModel.onAction(GameAction.StartGameClicked)
        viewModel.onAction(
            GameAction.CellTapped(Position(row = 0, column = 1)),
        )
        mainDispatcherRule.scheduler.advanceTimeBy(2_500L)
        mainDispatcherRule.scheduler.runCurrent()

        viewModel.onAction(
            GameAction.CellTapped(Position(row = 1, column = 3)),
        )
        viewModel.onAction(
            GameAction.CellTapped(Position(row = 2, column = 0)),
        )
        viewModel.onAction(
            GameAction.CellTapped(Position(row = 3, column = 2)),
        )
        mainDispatcherRule.scheduler.runCurrent()

        assertEquals(
            listOf(2_500L),
            viewModel.uiState.value.bestTimes,
        )
    }

    @Test
    fun `best times actions control dialog visibility`() {
        val viewModel = createViewModel()

        viewModel.onAction(GameAction.BestTimesClicked)

        assertTrue(viewModel.uiState.value.isBestTimesVisible)

        viewModel.onAction(GameAction.BestTimesDismissed)

        assertFalse(viewModel.uiState.value.isBestTimesVisible)
    }

    private fun placeFourByFourSolution(
        viewModel: GameViewModel,
    ) {
        viewModel.onAction(GameAction.StartGameClicked)
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

    private fun createViewModel(
        initialBoardSize: Int = GameViewModel.DEFAULT_BOARD_SIZE,
        bestTimesRepository: FakeBestTimesRepository = FakeBestTimesRepository(),
    ): GameViewModel {
        return GameViewModel(
            bestTimesRepository = bestTimesRepository,
            initialBoardSize = initialBoardSize,
            gameTimer = GameTimer(
                currentTimeMillis = {
                    mainDispatcherRule.scheduler.currentTime
                },
            ),
        )
    }
}
