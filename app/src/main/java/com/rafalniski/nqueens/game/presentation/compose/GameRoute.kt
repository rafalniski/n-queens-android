package com.rafalniski.nqueens.game.presentation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rafalniski.nqueens.game.data.PreferencesBestTimesRepository
import com.rafalniski.nqueens.game.presentation.GameViewModel

@Composable
fun GameRoute(
    modifier: Modifier = Modifier,
) {
    val applicationContext = LocalContext.current.applicationContext
    val bestTimesRepository = remember(applicationContext) {
        PreferencesBestTimesRepository(applicationContext)
    }
    val viewModelFactory = remember(bestTimesRepository) {
        viewModelFactory {
            initializer {
                GameViewModel(bestTimesRepository = bestTimesRepository)
            }
        }
    }
    val viewModel: GameViewModel = viewModel(factory = viewModelFactory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GameScreen(
        state = uiState,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}
