package com.rafalniski.nqueens.game.timer

import android.os.SystemClock

class GameTimer(
    private val currentTimeMillis: () -> Long = {
        SystemClock.elapsedRealtime()
    },
) {
    private var startedAtMillis: Long? = null
    private var stoppedElapsedMillis: Long = 0L

    val isRunning: Boolean
        get() = startedAtMillis != null

    fun start() {
        if (isRunning) {
            return
        }

        stoppedElapsedMillis = 0L
        startedAtMillis = currentTimeMillis()
    }

    fun elapsedTimeMillis(): Long {
        val startedAt = startedAtMillis
            ?: return stoppedElapsedMillis

        return currentTimeMillis() - startedAt
    }

    fun stop(): Long {
        stoppedElapsedMillis = elapsedTimeMillis()
        startedAtMillis = null

        return stoppedElapsedMillis
    }

    fun reset() {
        startedAtMillis = null
        stoppedElapsedMillis = 0L
    }
}
