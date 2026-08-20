package com.rafalniski.nqueens.game.domain

import kotlinx.coroutines.flow.Flow

interface BestTimesRepository {
    fun observeBestTimes(boardSize: Int): Flow<List<Long>>

    suspend fun saveCompletedTime(
        boardSize: Int,
        elapsedTimeMillis: Long,
    )
}
