package com.rafalniski.nqueens.game.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BestTimesTest {
    @Test
    fun `new time is inserted in ascending order`() {
        val result = addBestTime(
            currentBestTimes = listOf(1_000L, 3_000L),
            elapsedTimeMillis = 2_000L,
        )

        assertEquals(
            listOf(1_000L, 2_000L, 3_000L),
            result,
        )
    }

    @Test
    fun `only ten fastest times are kept`() {
        val result = addBestTime(
            currentBestTimes = (1L..10L).toList(),
            elapsedTimeMillis = 11L,
        )

        assertEquals((1L..10L).toList(), result)
    }

    @Test
    fun `faster time replaces slowest time in full leaderboard`() {
        val result = addBestTime(
            currentBestTimes = (2L..11L).toList(),
            elapsedTimeMillis = 1L,
        )

        assertEquals((1L..10L).toList(), result)
    }

    @Test
    fun `equal times are retained as separate completed games`() {
        val result = addBestTime(
            currentBestTimes = listOf(1_000L),
            elapsedTimeMillis = 1_000L,
        )

        assertEquals(listOf(1_000L, 1_000L), result)
    }

    @Test
    fun `negative time is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            addBestTime(
                currentBestTimes = emptyList(),
                elapsedTimeMillis = -1L,
            )
        }
    }
}
