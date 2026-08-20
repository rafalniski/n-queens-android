package com.rafalniski.nqueens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rafalniski.nqueens.game.presentation.compose.GameRoute
import com.rafalniski.nqueens.ui.theme.NQueensTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NQueensTheme {
                GameRoute()
            }
        }
    }
}
