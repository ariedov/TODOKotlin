package com.dleibovich.todokotlin

import android.widget.EditText

fun EditText.string() = text.toString()

fun EditText.stringOrNull(): String? {
    val string = string()
    return if (string.isEmpty()) {
        null
    } else {
        string
    }
}