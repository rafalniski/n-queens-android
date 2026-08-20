package com.rafalniski.nqueens.game.presentation.timer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameTimerTest {
    private var currentTimeMillis = 0L
    private val timer = GameTimer(
        currentTimeMillis = { currentTimeMillis },
    )

    @Test
    fun `new timer is not running`() {
        assertFalse(timer.isRunning)
    }

    @Test
    fun `new timer has zero elapsed time`() {
        assertEquals(0L, timer.elapsedTimeMillis())
    }

    @Test
    fun `started timer measures elapsed time`() {
        currentTimeMillis = 1_000L
        timer.start()

        currentTimeMillis = 3_500L

        assertTrue(timer.isRunning)
        assertEquals(2_500L, timer.elapsedTimeMillis())
    }

    @Test
    fun `starting running timer does not restart it`() {
        currentTimeMillis = 1_000L
        timer.start()

        currentTimeMillis = 2_000L
        timer.start()

        currentTimeMillis = 3_000L

        assertEquals(2_000L, timer.elapsedTimeMillis())
    }

    @Test
    fun `stopping timer preserves elapsed time`() {
        currentTimeMillis = 1_000L
        timer.start()

        currentTimeMillis = 3_500L
        val result = timer.stop()

        currentTimeMillis = 8_000L

        assertFalse(timer.isRunning)
        assertEquals(2_500L, result)
        assertEquals(2_500L, timer.elapsedTimeMillis())
    }

    @Test
    fun `reset clears timer`() {
        currentTimeMillis = 1_000L
        timer.start()

        currentTimeMillis = 3_500L
        timer.reset()

        assertFalse(timer.isRunning)
        assertEquals(0L, timer.elapsedTimeMillis())
    }
}
