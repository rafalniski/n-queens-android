package com.rafalniski.nqueens.game.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.rafalniski.nqueens.game.domain.Position
import com.rafalniski.nqueens.ui.theme.AppDimensions

@Composable
fun ChessBoard(
    boardSize: Int,
    queens: Set<Position>,
    conflictingQueens: Set<Position>,
    enabled: Boolean,
    onCellClick: (Position) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.clip(
            RoundedCornerShape(AppDimensions.boardCornerRadius),
        ),
    ) {
        repeat(boardSize) { row ->
            Row(
                modifier = Modifier.weight(1f),
            ) {
                repeat(boardSize) { column ->
                    val position = Position(
                        row = row,
                        column = column,
                    )
                    val rank = boardSize - row
                    val file = ('a'.code + column).toChar().toString()

                    ChessBoardCell(
                        position = position,
                        queenPlacementKey = queens.hashCode(),
                        rank = rank,
                        file = file,
                        showRank = column == 0,
                        showFile = row == boardSize - 1,
                        hasQueen = position in queens,
                        isConflicting = position in conflictingQueens,
                        enabled = enabled,
                        onClick = {
                            onCellClick(position)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )
                }
            }
        }
    }
}
