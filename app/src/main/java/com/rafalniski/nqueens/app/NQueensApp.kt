package com.rafalniski.nqueens.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rafalniski.nqueens.game.presentation.GameViewModel
import com.rafalniski.nqueens.game.presentation.ui.GameRoute
import com.rafalniski.nqueens.game.presentation.ui.theme.NQueensTheme

@Composable
fun NQueensApp(modifier: Modifier = Modifier) {
    val applicationContext = LocalContext.current.applicationContext
    val appContainer = remember(applicationContext) {
        AppContainer(applicationContext)
    }
    val gameViewModelFactory = remember(appContainer) {
        viewModelFactory {
            initializer {
                GameViewModel(
                    bestTimesRepository = appContainer.bestTimesRepository,
                )
            }
        }
    }

    NQueensTheme {
        GameRoute(
            viewModelFactory = gameViewModelFactory,
            modifier = modifier,
        )
    }
}
