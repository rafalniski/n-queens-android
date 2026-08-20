package com.rafalniski.nqueens.game.presentation.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rafalniski.nqueens.R

@Composable
fun BoardSizeSelector(
    selectedBoardSize: Int,
    onBoardSizeSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { isExpanded = true },
        ) {
            Text(
                text = stringResource(
                    R.string.game_board_size,
                    selectedBoardSize,
                    selectedBoardSize,
                ),
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
            )
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            supportedBoardSizes.forEach { boardSize ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(
                                R.string.game_board_size,
                                boardSize,
                                boardSize,
                            ),
                        )
                    },
                    onClick = {
                        isExpanded = false
                        onBoardSizeSelected(boardSize)
                    },
                )
            }
        }
    }
}

private val supportedBoardSizes = 4..12
