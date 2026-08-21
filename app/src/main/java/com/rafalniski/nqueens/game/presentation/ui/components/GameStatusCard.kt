package com.rafalniski.nqueens.game.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.rafalniski.nqueens.R
import com.rafalniski.nqueens.game.presentation.formatElapsedTime
import com.rafalniski.nqueens.game.presentation.ui.theme.AppDimensions

@Composable
fun GameStatusCard(
    boardSize: Int,
    queensLeft: Int,
    elapsedTimeMillis: Long,
    boardSizeSelectionEnabled: Boolean,
    onBoardSizeSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = Modifier.padding(AppDimensions.statusCardPadding),
            horizontalArrangement = Arrangement.spacedBy(
                AppDimensions.contentSpacing,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BoardSizeSelector(
                selectedBoardSize = boardSize,
                enabled = boardSizeSelectionEnabled,
                onBoardSizeSelected = onBoardSizeSelected,
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = pluralStringResource(
                        R.plurals.game_queens_left,
                        queensLeft,
                        queensLeft,
                    ),
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = stringResource(
                        R.string.game_elapsed_time,
                        formatElapsedTime(elapsedTimeMillis),
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
