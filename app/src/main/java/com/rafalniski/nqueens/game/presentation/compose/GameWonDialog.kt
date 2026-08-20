package com.rafalniski.nqueens.game.presentation.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rafalniski.nqueens.R

@Composable
fun GameWonDialog(
    onPlayAgainClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = stringResource(R.string.game_won_title))
        },
        text = {
            Text(text = stringResource(R.string.game_won_message))
        },
        confirmButton = {
            Button(onClick = onPlayAgainClick) {
                Text(text = stringResource(R.string.game_play_again))
            }
        },
    )
}
