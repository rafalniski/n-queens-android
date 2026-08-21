package com.rafalniski.nqueens.game.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rafalniski.nqueens.R
import com.rafalniski.nqueens.game.presentation.GameStatus
import com.rafalniski.nqueens.game.presentation.ui.theme.GameColors

@Composable
fun GameControls(
    status: GameStatus,
    onStartClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (status) {
        GameStatus.Ready -> {
            GameRaisedButton(
                text = stringResource(R.string.game_start),
                containerColor = GameColors.primaryAction,
                shadowColor = GameColors.primaryActionShadow,
                contentColor = GameColors.onPrimaryAction,
                onClick = onStartClick,
                modifier = modifier,
            )
        }

        GameStatus.Playing -> {
            GameRaisedButton(
                text = stringResource(R.string.game_reset),
                containerColor = GameColors.secondaryAction,
                shadowColor = GameColors.secondaryActionShadow,
                contentColor = GameColors.onSecondaryAction,
                onClick = onResetClick,
                modifier = modifier,
            )
        }

        GameStatus.Won -> Unit
    }
}
