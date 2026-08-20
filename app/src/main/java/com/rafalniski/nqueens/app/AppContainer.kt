package com.rafalniski.nqueens.app

import android.content.Context
import com.rafalniski.nqueens.game.data.local.PreferencesBestTimesRepository
import com.rafalniski.nqueens.game.domain.BestTimesRepository

class AppContainer(context: Context) {
    val bestTimesRepository: BestTimesRepository =
        PreferencesBestTimesRepository(context.applicationContext)
}
