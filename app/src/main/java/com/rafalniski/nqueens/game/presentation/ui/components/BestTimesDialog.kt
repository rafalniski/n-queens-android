package com.rafalniski.nqueens.game.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.rafalniski.nqueens.R
import com.rafalniski.nqueens.game.presentation.ui.theme.AppDimensions

@Composable
fun BestTimesDialog(
    boardSize: Int,
    bestTimes: List<Long>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .widthIn(max = AppDimensions.dialogMaxWidth),
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = AppDimensions.conflictBorderWidth,
        ) {
            Column(
                modifier = Modifier.padding(AppDimensions.screenPadding),
                verticalArrangement = Arrangement.spacedBy(
                    AppDimensions.contentSpacing,
                ),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.game_best_times),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Text(
                            text = stringResource(
                                R.string.game_board_dimensions,
                                boardSize,
                                boardSize,
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(
                                R.string.game_best_times_close,
                            ),
                        )
                    }
                }

                if (bestTimes.isEmpty()) {
                    Text(
                        text = stringResource(R.string.game_best_times_empty),
                        modifier = Modifier.padding(
                            vertical = AppDimensions.screenPadding,
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(
                            max = AppDimensions.leaderboardMaxHeight,
                        ),
                        verticalArrangement = Arrangement.spacedBy(
                            AppDimensions.contentSpacing,
                        ),
                    ) {
                        itemsIndexed(
                            items = bestTimes,
                            key = { index, time -> "$index-$time" },
                        ) { index, elapsedTimeMillis ->
                            BestTimeRow(
                                rank = index + 1,
                                elapsedTimeMillis = elapsedTimeMillis,
                            )
                        }
                    }
                }

                OutlinedButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = stringResource(R.string.game_best_times_close))
                }
            }
        }
    }
}
