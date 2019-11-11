package com.example.smartredact.common.extension

import java.text.DecimalFormat
import java.util.regex.Pattern

/**
 * Created by TuHA on 6/6/2019.
 */

fun String?.orText(string: String): String {
    return this ?: string
}

fun String.formatDecimal(format: String = "#,###"): String? {
    if (this.isEmpty()) {
        return ""
    }
    val formatter = DecimalFormat(format)
    return formatter.format(this.replace(",", "").toLong())
}

fun String.isValidEmail(): Boolean {
    val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}