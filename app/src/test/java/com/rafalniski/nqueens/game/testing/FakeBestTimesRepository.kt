package com.rafalniski.nqueens.game.testing

import com.rafalniski.nqueens.game.domain.BestTimesRepository
import com.rafalniski.nqueens.game.domain.addBestTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeBestTimesRepository : BestTimesRepository {
    private val bestTimesByBoardSize = mutableMapOf<Int, MutableStateFlow<List<Long>>>()

    override fun observeBestTimes(boardSize: Int): Flow<List<Long>> {
        return bestTimesFlow(boardSize)
    }

    override suspend fun saveCompletedTime(
        boardSize: Int,
        elapsedTimeMillis: Long,
    ) {
        val flow = bestTimesFlow(boardSize)
        flow.value = addBestTime(
            currentBestTimes = flow.value,
            elapsedTimeMillis = elapsedTimeMillis,
        )
    }

    fun setBestTimes(
        boardSize: Int,
        bestTimes: List<Long>,
    ) {
        bestTimesFlow(boardSize).value = bestTimes
    }

    private fun bestTimesFlow(boardSize: Int): MutableStateFlow<List<Long>> {
        return bestTimesByBoardSize.getOrPut(boardSize) {
            MutableStateFlow(emptyList())
        }
    }
}
