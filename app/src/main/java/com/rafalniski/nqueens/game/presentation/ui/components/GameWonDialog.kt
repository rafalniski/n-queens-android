package com.rafalniski.nqueens.game.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rafalniski.nqueens.R
import com.rafalniski.nqueens.game.presentation.formatElapsedTime
import com.rafalniski.nqueens.game.presentation.ui.theme.AppDimensions
import com.rafalniski.nqueens.game.presentation.ui.theme.GameColors

@Composable
fun GameWonDialog(
    elapsedTimeMillis: Long,
    onPlayAgainClick: () -> Unit,
    onViewBestTimesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .widthIn(max = AppDimensions.dialogMaxWidth)
                .graphicsLayer {
                    alpha = dialogAlpha
                    scaleX = dialogScale
                    scaleY = dialogScale
                },
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = AppDimensions.conflictBorderWidth,
        ) {
            Column(
                modifier = Modifier.padding(AppDimensions.screenPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    AppDimensions.contentSpacing,
                ),
            ) {
                Surface(
                    modifier = Modifier.size(
                        AppDimensions.dialogIconContainerSize,
                    ),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(R.drawable.ic_trophy),
                            contentDescription = null,
                            modifier = Modifier.size(
                                AppDimensions.dialogIconSize,
                            ),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.game_won_title),
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = stringResource(R.string.game_won_encouragement),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = stringResource(R.string.game_your_time),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = formatElapsedTime(elapsedTimeMillis),
                    style = MaterialTheme.typography.displaySmall,
                )

                GameRaisedButton(
                    text = stringResource(R.string.game_play_again),
                    containerColor = GameColors.primaryAction,
                    shadowColor = GameColors.primaryActionShadow,
                    contentColor = GameColors.onPrimaryAction,
                    onClick = onPlayAgainClick,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedButton(
                    onClick = onViewBestTimesClick,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.game_view_best_times),
                    )
                }
            }
        }
    }
}

private const val DialogInitialScale = 0.82f
private const val DialogSpringDampingRatio = 0.58f
private const val DialogSpringStiffness = 420f
private const val DialogFadeDurationMillis = 180
