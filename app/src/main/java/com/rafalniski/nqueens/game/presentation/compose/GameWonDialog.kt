package com.rafalniski.nqueens.game.presentation.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rafalniski.nqueens.R
import com.rafalniski.nqueens.game.presentation.formatElapsedTime

@Composable
fun GameWonDialog(
    elapsedTimeMillis: Long,
    onPlayAgainClick: () -> Unit,
) {
    val formattedElapsedTime = formatElapsedTime(elapsedTimeMillis)
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = stringResource(R.string.game_won_title))
        },
        text = {
            Text(
                text = stringResource(
                    R.string.game_won_message,
                    formattedElapsedTime,
                ),
            )
        },
        confirmButton = {
            Button(onClick = onPlayAgainClick) {
                Text(text = stringResource(R.string.game_play_again))
            }
        },
    )
}
