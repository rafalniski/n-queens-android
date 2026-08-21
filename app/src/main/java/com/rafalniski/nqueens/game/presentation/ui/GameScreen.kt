package com.rafalniski.nqueens.game.presentation.ui

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rafalniski.nqueens.game.domain.GameState
import com.rafalniski.nqueens.game.domain.Position
import com.rafalniski.nqueens.game.presentation.GameAction
import com.rafalniski.nqueens.game.presentation.GameStatus
import com.rafalniski.nqueens.game.presentation.GameUiState
import com.rafalniski.nqueens.game.presentation.ui.components.BestTimesDialog
import com.rafalniski.nqueens.game.presentation.ui.components.ChessBoard
import com.rafalniski.nqueens.game.presentation.ui.components.ConflictBanner
import com.rafalniski.nqueens.game.presentation.ui.components.GameControls
import com.rafalniski.nqueens.game.presentation.ui.components.GameHeader
import com.rafalniski.nqueens.game.presentation.ui.components.GameStatusCard
import com.rafalniski.nqueens.game.presentation.ui.components.GameWonDialog
import com.rafalniski.nqueens.game.presentation.ui.theme.AppDimensions
import com.rafalniski.nqueens.game.presentation.ui.theme.NQueensTheme

@Composable
fun GameScreen(
    state: GameUiState,
    onAction: (GameAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val boardAlpha by animateFloatAsState(
        targetValue = if (state.status == GameStatus.Ready) {
            ReadyBoardAlpha
        } else {
            1f
        },
        label = "boardAlpha",
    )

    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(AppDimensions.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                AppDimensions.contentSpacing,
            ),
        ) {
            GameHeader(
                onBestTimesClick = {
                    onAction(GameAction.BestTimesClicked)
                },
            )

            GameStatusCard(
                boardSize = state.boardSize,
                queensLeft = state.queensLeft,
                elapsedTimeMillis = state.elapsedTimeMillis,
                boardSizeSelectionEnabled =
                    state.status == GameStatus.Ready,
                onBoardSizeSelected = { boardSize ->
                    onAction(GameAction.BoardSizeSelected(boardSize))
                },
            )

            ConflictBanner(
                conflictingQueensCount = state.conflictingQueens.size,
            )

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                val availableBoardHeight =
                    (maxHeight - AppDimensions.gameActionAreaMinHeight)
                        .coerceAtLeast(0.dp)
                val boardDimension = minOf(
                    maxWidth,
                    availableBoardHeight,
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ChessBoard(
                        boardSize = state.boardSize,
                        queens = state.queens,
                        conflictingQueens = state.conflictingQueens,
                        enabled = state.status == GameStatus.Playing,
                        onCellClick = { position ->
                            onAction(GameAction.CellTapped(position))
                        },
                        modifier = Modifier
                            .size(boardDimension)
                            .graphicsLayer {
                                alpha = boardAlpha
                            },
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        GameControls(
                            status = state.status,
                            onStartClick = {
                                onAction(GameAction.StartGameClicked)
                            },
                            onResetClick = {
                                onAction(GameAction.ResetClicked)
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }

    if (state.isSolved && !state.isBestTimesVisible) {
        GameWonDialog(
            elapsedTimeMillis = state.elapsedTimeMillis,
            onPlayAgainClick = {
                onAction(GameAction.PlayAgainClicked)
            },
            onViewBestTimesClick = {
                onAction(GameAction.BestTimesClicked)
            },
        )
    }

    if (state.isBestTimesVisible) {
        BestTimesDialog(
            boardSize = state.boardSize,
            bestTimes = state.bestTimes,
            onDismissRequest = {
                onAction(GameAction.BestTimesDismissed)
            },
        )
    }
}

private const val ReadyBoardAlpha = 0.72f

@Preview(
    name = "Playing - light",
    showBackground = true,
)
@Preview(
    name = "Playing - dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun GameScreenPreview() {
    NQueensTheme {
        GameScreen(
            state = GameUiState(
                game = GameState(
                    boardSize = 4,
                    queens = setOf(
                        Position(row = 0, column = 0),
                        Position(row = 0, column = 3),
                        Position(row = 2, column = 1),
                    ),
                ),
                status = GameStatus.Playing,
            ),
            onAction = {},
        )
    }
}
