package com.rafalniski.nqueens.game.presentation.compose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.rafalniski.nqueens.game.domain.GameState
import com.rafalniski.nqueens.game.domain.Position
import com.rafalniski.nqueens.game.presentation.GameAction
import com.rafalniski.nqueens.game.presentation.GameUiState
import com.rafalniski.nqueens.ui.theme.NQueensTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class GameScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyGameShowsQueensLeftAndDisabledReset() {
        setGameContent(
            state = GameUiState(
                game = GameState(boardSize = 4),
            ),
        )

        composeTestRule
            .onNodeWithText("4 queens left")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Time: 00:00")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Reset")
            .assertIsNotEnabled()
    }

    @Test
    fun tappingCellEmitsCellTappedAction() {
        var receivedAction: GameAction? = null

        setGameContent(
            state = GameUiState(
                game = GameState(boardSize = 4),
            ),
            onAction = { action ->
                receivedAction = action
            },
        )

        composeTestRule
            .onNodeWithContentDescription("File a, rank 4, empty")
            .performClick()

        assertEquals(
            GameAction.CellTapped(
                position = Position(row = 0, column = 0),
            ),
            receivedAction,
        )
    }

    @Test
    fun conflictingQueensShowConflictMessage() {
        setGameContent(
            state = GameUiState(
                game = GameState(
                    boardSize = 4,
                    queens = setOf(
                        Position(row = 0, column = 0),
                        Position(row = 0, column = 3),
                    ),
                ),
            ),
        )

        composeTestRule
            .onNodeWithText("2 conflicting queens")
            .assertIsDisplayed()
    }

    @Test
    fun solvedGameShowsDialogAndPlayAgainEmitsAction() {
        var receivedAction: GameAction? = null

        setGameContent(
            state = GameUiState(
                game = solvedFourByFourGame(),
            ),
            onAction = { action ->
                receivedAction = action
            },
        )

        composeTestRule
            .onNodeWithText("Puzzle solved!")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Play again")
            .performClick()

        assertEquals(
            GameAction.PlayAgainClicked,
            receivedAction,
        )
    }

    @Test
    fun resetWithQueenIsEnabledAndEmitsResetAction() {
        var receivedAction: GameAction? = null

        setGameContent(
            state = GameUiState(
                game = GameState(
                    boardSize = 4,
                    queens = setOf(
                        Position(row = 0, column = 0),
                    ),
                ),
            ),
            onAction = { action ->
                receivedAction = action
            },
        )

        composeTestRule
            .onNodeWithText("Reset")
            .assertIsEnabled()
            .performClick()

        assertEquals(
            GameAction.ResetClicked,
            receivedAction,
        )
    }

    @Test
    fun selectingBoardSizeEmitsBoardSizeSelectedAction() {
        var receivedAction: GameAction? = null

        setGameContent(
            state = GameUiState(
                game = GameState(boardSize = 4),
            ),
            onAction = { action ->
                receivedAction = action
            },
        )

        composeTestRule
            .onNodeWithText("Board 4 × 4")
            .performClick()

        composeTestRule
            .onNodeWithText("Board 6 × 6")
            .performClick()

        assertEquals(
            GameAction.BoardSizeSelected(boardSize = 6),
            receivedAction,
        )
    }

    @Test
    fun bestTimesClickEmitsBestTimesAction() {
        var receivedAction: GameAction? = null

        setGameContent(
            state = GameUiState(
                game = GameState(boardSize = 4),
            ),
            onAction = { action ->
                receivedAction = action
            },
        )

        composeTestRule
            .onNodeWithText("Best times")
            .assertIsDisplayed()
            .performClick()

        assertEquals(
            GameAction.BestTimesClicked,
            receivedAction,
        )
    }

    @Test
    fun visibleBestTimesDialogShowsRankedTimesAndDismisses() {
        var wasDismissed = false

        composeTestRule.setContent {
            NQueensTheme {
                BestTimesDialog(
                    boardSize = 4,
                    bestTimes = listOf(8_000L, 11_000L),
                    onDismissRequest = {
                        wasDismissed = true
                    },
                )
            }
        }

        composeTestRule
            .onNodeWithText("Best times - 4 × 4")
            .assertExists()
        composeTestRule
            .onNodeWithText("1. 00:08")
            .assertExists()
        composeTestRule
            .onNodeWithText("2. 00:11")
            .assertExists()

        composeTestRule
            .onNodeWithText("Close")
            .performClick()

        assertEquals(true, wasDismissed)
    }

    private fun setGameContent(
        state: GameUiState,
        onAction: (GameAction) -> Unit = {},
    ) {
        composeTestRule.setContent {
            NQueensTheme {
                GameScreen(
                    state = state,
                    onAction = onAction,
                )
            }
        }
    }

    private fun solvedFourByFourGame(): GameState {
        return GameState(
            boardSize = 4,
            queens = setOf(
                Position(row = 0, column = 1),
                Position(row = 1, column = 3),
                Position(row = 2, column = 0),
                Position(row = 3, column = 2),
            ),
        )
    }
}
