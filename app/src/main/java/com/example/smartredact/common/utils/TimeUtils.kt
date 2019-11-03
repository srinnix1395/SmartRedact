package com.example.smartredact.common.utils

import java.util.concurrent.TimeUnit


object TimeUtils {

    fun format(duration: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - 60 * minutes
        val millis = duration - TimeUnit.MILLISECONDS.toSeconds(duration) * 1000

        return String.format("%02d:%02d.%03d", minutes, seconds, millis)
    }
}