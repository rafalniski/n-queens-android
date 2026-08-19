package com.rafalniski.nqueens.game.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PositionTest {
    @Test
    fun `positions with same coordinates are equal`() {
        val first = Position(row = 2, column = 3)
        val second = Position(row = 2, column = 3)

        assertEquals(first, second)
    }

    @Test
    fun `negative row is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            Position(row = -1, column = 0)
        }
    }

    @Test
    fun `negative column is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            Position(row = 0, column = -1)
        }
    }
}
