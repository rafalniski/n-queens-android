package com.rafalniski.nqueens.game.presentation

fun formatElapsedTime(elapsedTimeMillis: Long): String {
    require(elapsedTimeMillis >= 0L) {
        "Elapsed time cannot be negative."
    }

    val totalSeconds = elapsedTimeMillis / 1_000L
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L

    return "${minutes.toTwoDigits()}:${seconds.toTwoDigits()}"
}

private fun Long.toTwoDigits(): String {
    return toString().padStart(
        length = 2,
        padChar = '0',
    )
}
