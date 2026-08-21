package com.rafalniski.nqueens.game.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.rafalniski.nqueens.game.presentation.formatElapsedTime
import com.rafalniski.nqueens.game.presentation.ui.theme.AppDimensions
import com.rafalniski.nqueens.game.presentation.ui.theme.GameColors

@Composable
fun BestTimeRow(
    rank: Int,
    elapsedTimeMillis: Long,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier.padding(AppDimensions.statusCardPadding),
            horizontalArrangement = Arrangement.spacedBy(
                AppDimensions.contentSpacing,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(AppDimensions.leaderboardRankSize),
                color = rankColor(rank),
                shape = CircleShape,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = rank.toString(),
                        color = if (rank <= PodiumPlaces) {
                            GameColors.onPodium
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }

            Text(
                text = formatElapsedTime(elapsedTimeMillis),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun rankColor(rank: Int): Color {
    return when (rank) {
        1 -> GameColors.gold
        2 -> GameColors.silver
        3 -> GameColors.bronze
        else -> MaterialTheme.colorScheme.surface
    }
}

private const val PodiumPlaces = 3
