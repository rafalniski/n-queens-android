package com.rafalniski.nqueens.game.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.rafalniski.nqueens.game.domain.BestTimesRepository
import com.rafalniski.nqueens.game.domain.GameState
import com.rafalniski.nqueens.game.domain.MAX_BEST_TIMES
import com.rafalniski.nqueens.game.domain.addBestTime
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private val Context.bestTimesDataStore by preferencesDataStore(
    name = "best_times",
)

class DataStoreBestTimesRepository(
    private val dataStore: DataStore<Preferences>,
) : BestTimesRepository {
    constructor(context: Context) : this(
        dataStore = context.applicationContext.bestTimesDataStore,
    )

    override fun observeBestTimes(boardSize: Int): Flow<List<Long>> {
        requireValidBoardSize(boardSize)

        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                List(MAX_BEST_TIMES) { index ->
                    preferences[bestTimeKey(boardSize, index)]
                }.filterNotNull()
            }
            .distinctUntilChanged()
    }

    override suspend fun saveCompletedTime(
        boardSize: Int,
        elapsedTimeMillis: Long,
    ) {
        requireValidBoardSize(boardSize)

        dataStore.edit { preferences ->
            val currentBestTimes = List(MAX_BEST_TIMES) { index ->
                preferences[bestTimeKey(boardSize, index)]
            }.filterNotNull()
            val updatedBestTimes = addBestTime(
                currentBestTimes = currentBestTimes,
                elapsedTimeMillis = elapsedTimeMillis,
            )

            repeat(MAX_BEST_TIMES) { index ->
                preferences.remove(bestTimeKey(boardSize, index))
            }

            updatedBestTimes.forEachIndexed { index, timeMillis ->
                preferences[bestTimeKey(boardSize, index)] = timeMillis
            }
        }
    }

    private fun requireValidBoardSize(boardSize: Int) {
        require(boardSize >= GameState.MIN_BOARD_SIZE) {
            "Board size must be at least ${GameState.MIN_BOARD_SIZE}."
        }
    }

    private fun bestTimeKey(
        boardSize: Int,
        index: Int,
    ) = longPreferencesKey("best_time_${boardSize}_$index")
}
