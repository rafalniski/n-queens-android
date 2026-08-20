package com.rafalniski.nqueens.game.data

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import java.io.File
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PreferencesBestTimesRepositoryTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun `repository stores ten fastest times for each board size`() = runTest {
        val dataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = {
                File(temporaryFolder.root, "best_times.preferences_pb")
            },
        )
        val repository = PreferencesBestTimesRepository(dataStore)

        (11L downTo 1L).forEach { elapsedTimeMillis ->
            repository.saveCompletedTime(
                boardSize = 4,
                elapsedTimeMillis = elapsedTimeMillis,
            )
        }
        repository.saveCompletedTime(
            boardSize = 5,
            elapsedTimeMillis = 20L,
        )

        assertEquals(
            (1L..10L).toList(),
            repository.observeBestTimes(boardSize = 4).first(),
        )
        assertEquals(
            listOf(20L),
            repository.observeBestTimes(boardSize = 5).first(),
        )
    }
}
