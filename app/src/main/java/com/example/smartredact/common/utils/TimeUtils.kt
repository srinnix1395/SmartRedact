package com.example.smartredact.common.utils

import java.util.concurrent.TimeUnit


object TimeUtils {

    fun format(duration: Long, showMillis: Boolean): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - 60 * minutes

        return if (showMillis) {
            val millis = duration - TimeUnit.MILLISECONDS.toSeconds(duration) * 1000
            String.format("%02d:%02d.%03d", minutes, seconds, millis)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}