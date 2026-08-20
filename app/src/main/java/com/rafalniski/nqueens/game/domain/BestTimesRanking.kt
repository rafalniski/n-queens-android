package com.rafalniski.nqueens.game.domain

const val MAX_BEST_TIMES = 10

internal fun addBestTime(
    currentBestTimes: List<Long>,
    elapsedTimeMillis: Long,
): List<Long> {
    require(elapsedTimeMillis >= 0L) {
        "Elapsed time cannot be negative."
    }

    return (currentBestTimes + elapsedTimeMillis)
        .sorted()
        .take(MAX_BEST_TIMES)
}
