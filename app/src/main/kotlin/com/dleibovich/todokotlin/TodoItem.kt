package com.dleibovich.todokotlin

public data class TodoItem(
        val description : String,
        val timeCreated : Long = System.currentTimeMillis(),
        var isDone : Boolean)
