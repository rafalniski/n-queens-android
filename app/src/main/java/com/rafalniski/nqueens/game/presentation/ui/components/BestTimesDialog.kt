package com.rafalniski.nqueens.game.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rafalniski.nqueens.R
import com.rafalniski.nqueens.game.presentation.formatElapsedTime
import com.rafalniski.nqueens.ui.theme.AppDimensions

@Composable
fun BestTimesDialog(
    boardSize: Int,
    bestTimes: List<Long>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.game_best_times_close))
            }
        },
        modifier = modifier,
        title = {
            Text(
                text = stringResource(
                    R.string.game_best_times_title,
                    boardSize,
                    boardSize,
                ),
            )
        },
        text = {
            if (bestTimes.isEmpty()) {
                Text(text = stringResource(R.string.game_best_times_empty))
            } else {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(
                        AppDimensions.contentSpacing,
                    ),
                ) {
                    bestTimes.forEachIndexed { index, elapsedTimeMillis ->
                        Text(
                            text = stringResource(
                                R.string.game_best_time_entry,
                                index + 1,
                                formatElapsedTime(elapsedTimeMillis),
                            ),
                        )
                    }
                }
            }
        },
    )
}
