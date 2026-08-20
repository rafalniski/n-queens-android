package com.rafalniski.nqueens.game.presentation.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rafalniski.nqueens.R
import com.rafalniski.nqueens.game.domain.GameState
import com.rafalniski.nqueens.game.domain.Position
import com.rafalniski.nqueens.game.presentation.GameAction
import com.rafalniski.nqueens.game.presentation.GameUiState
import com.rafalniski.nqueens.game.presentation.formatElapsedTime
import com.rafalniski.nqueens.ui.theme.AppDimensions
import com.rafalniski.nqueens.ui.theme.NQueensTheme

@Composable
fun GameScreen(
    state: GameUiState,
    onAction: (GameAction) -> Unit,
    modifier: Modifier = Modifier,
) {
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
            Text(
                text = stringResource(R.string.game_title),
                style = MaterialTheme.typography.headlineMedium,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BoardSizeSelector(
                    selectedBoardSize = state.boardSize,
                    onBoardSizeSelected = { boardSize ->
                        onAction(GameAction.BoardSizeSelected(boardSize))
                    },
                )

                TextButton(
                    onClick = {
                        onAction(GameAction.ResetClicked)
                    },
                    enabled = state.queens.isNotEmpty(),
                ) {
                    Text(text = stringResource(R.string.game_reset))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = pluralStringResource(
                        R.plurals.game_queens_left,
                        state.queensLeft,
                        state.queensLeft,
                    ),
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = stringResource(
                        R.string.game_elapsed_time,
                        formatElapsedTime(state.elapsedTimeMillis),
                    ),
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Text(
                text = if (state.conflictingQueens.isEmpty()) {
                    ""
                } else {
                    pluralStringResource(
                        R.plurals.game_conflicting_queens,
                        state.conflictingQueens.size,
                        state.conflictingQueens.size,
                    )
                },
                minLines = 1,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
            )

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                val boardDimension = minOf(maxWidth, maxHeight)

                ChessBoard(
                    boardSize = state.boardSize,
                    queens = state.queens,
                    conflictingQueens = state.conflictingQueens,
                    onCellClick = { position ->
                        onAction(GameAction.CellTapped(position))
                    },
                    modifier = Modifier.size(boardDimension),
                )
            }
        }
    }

    if (state.isSolved) {
        GameWonDialog(
            elapsedTimeMillis = state.elapsedTimeMillis,
            onPlayAgainClick = {
                onAction(GameAction.PlayAgainClicked)
            },
        )
    }
}

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
            ),
            onAction = {},
        )
    }
}
