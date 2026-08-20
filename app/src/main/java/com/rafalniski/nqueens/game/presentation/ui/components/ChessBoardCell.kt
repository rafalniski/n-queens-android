package com.rafalniski.nqueens.game.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import com.rafalniski.nqueens.R
import com.rafalniski.nqueens.game.domain.Position
import com.rafalniski.nqueens.game.presentation.ui.theme.AppDimensions
import com.rafalniski.nqueens.game.presentation.ui.theme.ChessBoardColors

@Composable
fun ChessBoardCell(
    position: Position,
    queenPlacementKey: Int,
    rank: Int,
    file: String,
    showRank: Boolean,
    showFile: Boolean,
    hasQueen: Boolean,
    isConflicting: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLightSquare = (position.row + position.column) % 2 == 0
    val shakeOffset = remember { Animatable(0f) }
    val shakeDistance = with(LocalDensity.current) {
        AppDimensions.conflictShakeDistance.toPx()
    }

    LaunchedEffect(isConflicting, queenPlacementKey) {
        if (isConflicting) {
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = ConflictShakeDurationMillis
                    -shakeDistance at 40
                    shakeDistance at 80
                    -shakeDistance at 120
                    shakeDistance at 160
                    -shakeDistance / 2 at 200
                    shakeDistance / 2 at 240
                },
            )
        } else {
            shakeOffset.snapTo(0f)
        }
    }

    val squareColor = if (isLightSquare) {
        ChessBoardColors.lightSquare
    } else {
        ChessBoardColors.darkSquare
    }

    val backgroundColor = if (isConflicting) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        squareColor
    }

    val coordinateColor = when {
        isConflicting -> MaterialTheme.colorScheme.onErrorContainer
        isLightSquare -> ChessBoardColors.coordinateOnLightSquare
        else -> ChessBoardColors.coordinateOnDarkSquare
    }

    val descriptionResource = when {
        hasQueen && isConflicting ->
            R.string.game_conflicting_queen_cell_description
        hasQueen ->
            R.string.game_queen_cell_description

        else ->
            R.string.game_empty_cell_description
    }

    val cellDescription = stringResource(
        descriptionResource,
        file,
        rank,
    )

    val toggleActionLabel =
        stringResource(R.string.game_toggle_queen_action)

    val conflictBorderModifier = if (isConflicting) {
        Modifier.border(
            width = AppDimensions.conflictBorderWidth,
            color = MaterialTheme.colorScheme.error,
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .clipToBounds()
            .background(backgroundColor)
            .then(conflictBorderModifier)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClickLabel = toggleActionLabel,
                onClick = onClick,
            )
            .semantics {
                contentDescription = cellDescription
            },
        contentAlignment = Alignment.Center,
    ) {
        if (showRank) {
            Text(
                text = rank.toString(),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(AppDimensions.coordinatePadding)
                    .clearAndSetSemantics {},
                color = coordinateColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall,
            )
        }

        if (showFile) {
            Text(
                text = file,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(AppDimensions.coordinatePadding)
                    .clearAndSetSemantics {},
                color = coordinateColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall,
            )
        }

        AnimatedVisibility(
            visible = hasQueen,
            enter = fadeIn(
                animationSpec = tween(QueenEnterDurationMillis),
            ) + scaleIn(
                initialScale = QueenHiddenScale,
                animationSpec = tween(QueenEnterDurationMillis),
            ),
            exit = fadeOut(
                animationSpec = tween(QueenExitDurationMillis),
            ) + scaleOut(
                targetScale = QueenHiddenScale,
                animationSpec = tween(QueenExitDurationMillis),
            ),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_queen_black),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(QueenSizeFraction)
                    .graphicsLayer {
                        translationX = shakeOffset.value
                    }
                    .clearAndSetSemantics {},
            )
        }
    }
}

private const val QueenSizeFraction = 0.78f
private const val QueenHiddenScale = 0.55f
private const val QueenEnterDurationMillis = 180
private const val QueenExitDurationMillis = 120
private const val ConflictShakeDurationMillis = 280
