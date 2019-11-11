package com.example.smartredact.common.extension

import java.util.*

/**
 * Created by TuHA on 6/6/2019.
 */

fun Calendar.isTheSameDay(calendar: Calendar?): Boolean {
    return if (calendar == null) {
        false
    } else {
        this.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && this.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
    }
}

fun Calendar.isTheSameMonth(calendar: Calendar?): Boolean {
    return if (calendar == null) {
        false
    } else {
        this.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && this.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
    }
}

fun Date?.isToday(): Boolean {
    if (this == null) {
        return false
    }

    val thisCalendar = Calendar.getInstance().apply {
        time = this@isToday
    }
    val currentDay = Calendar.getInstance()
    return thisCalendar.get(Calendar.YEAR) == currentDay.get(Calendar.YEAR) && thisCalendar.get(Calendar.DAY_OF_YEAR) == currentDay.get(Calendar.DAY_OF_YEAR)
}