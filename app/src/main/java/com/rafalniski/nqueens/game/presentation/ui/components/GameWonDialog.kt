package com.rafalniski.nqueens.game.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import com.rafalniski.nqueens.R
import com.rafalniski.nqueens.game.presentation.formatElapsedTime

@Composable
fun GameWonDialog(
    elapsedTimeMillis: Long,
    onPlayAgainClick: () -> Unit,
) {
    val formattedElapsedTime = formatElapsedTime(elapsedTimeMillis)
    var animationStarted by remember { mutableStateOf(false) }
    val dialogScale by animateFloatAsState(
        targetValue = if (animationStarted) 1f else DialogInitialScale,
        animationSpec = spring(
            dampingRatio = DialogSpringDampingRatio,
            stiffness = DialogSpringStiffness,
        ),
        label = "victoryDialogScale",
    )
    val dialogAlpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(DialogFadeDurationMillis),
        label = "victoryDialogAlpha",
    )

    LaunchedEffect(Unit) {
        animationStarted = true
    }

    AlertDialog(
        onDismissRequest = {},
        modifier = Modifier.graphicsLayer {
            alpha = dialogAlpha
            scaleX = dialogScale
            scaleY = dialogScale
        },
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

private const val DialogInitialScale = 0.82f
private const val DialogSpringDampingRatio = 0.58f
private const val DialogSpringStiffness = 420f
private const val DialogFadeDurationMillis = 180
