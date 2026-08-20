package com.rafalniski.nqueens.game.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ElapsedTimeFormatterTest {
    @Test
    fun `zero milliseconds is formatted as zero minutes and seconds`() {
        assertEquals(
            "00:00",
            formatElapsedTime(0L),
        )
    }

    @Test
    fun `milliseconds are formatted as minutes and seconds`() {
        assertEquals(
            "01:05",
            formatElapsedTime(65_000L),
        )
    }

    @Test
    fun `incomplete second is not rounded up`() {
        assertEquals(
            "00:01",
            formatElapsedTime(1_999L),
        )
    }

    @Test
    fun `negative elapsed time is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            formatElapsedTime(-1L)
        }
    }
}