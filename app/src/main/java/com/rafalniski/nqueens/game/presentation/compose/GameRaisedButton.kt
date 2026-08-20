package com.rafalniski.nqueens.game.presentation.compose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rafalniski.nqueens.ui.theme.AppDimensions

@Composable
fun GameRaisedButton(
    text: String,
    containerColor: Color,
    shadowColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonOffset by animateDpAsState(
        targetValue = if (isPressed) {
            AppDimensions.gameButtonDepth
        } else {
            0.dp
        },
        animationSpec = tween(ButtonPressDurationMillis),
        label = "gameButtonOffset",
    )
    val buttonShape = RoundedCornerShape(
        AppDimensions.gameButtonCornerRadius,
    )

    Box(
        modifier = modifier.height(
            AppDimensions.gameButtonHeight + AppDimensions.gameButtonDepth,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimensions.gameButtonHeight)
                .offset(y = AppDimensions.gameButtonDepth)
                .background(
                    color = shadowColor,
                    shape = buttonShape,
                ),
        )

        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimensions.gameButtonHeight)
                .offset(y = buttonOffset),
            shape = buttonShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
            ),
            interactionSource = interactionSource,
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

private const val ButtonPressDurationMillis = 70
