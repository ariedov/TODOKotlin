package com.dleibovich.todokotlin.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.util.ArrayList
import android.content.ContentValues
import com.dleibovich.todokotlin.TodoItem

class TodoCache(context : Context) {

    private val dbHelper : TODODBHelper = TODODBHelper(context)
    private var db : SQLiteDatabase? = null

    fun open() {
        if (db?.isOpen != true) {
            db = dbHelper.writableDatabase
        }
    }

    fun getTodoItems() : List<TodoItem> {
        db?.query(TODODBHelper.TABLE_NAME, null, null, null, null, null, null, null)
                .use {
                    val result = ArrayList<TodoItem>()
                    if (it!!.moveToFirst()) {
                        do {
                            val description = it.getString(it.getColumnIndex(TODODBHelper.COL_DESCRIPTION)) as String
                            val dateCreated = it.getLong(it.getColumnIndex(TODODBHelper.COL_CREATED))
                            val isDone = it.getInt(it.getColumnIndex(TODODBHelper.COL_DONE))
                            val todoItem = TodoItem(description, dateCreated, isDone == 1)
                            result.add(todoItem)
                        } while (it.moveToNext())
                    }
                    return result
                }
    }

    fun putTodoItem(item : TodoItem) {
        val cv = ContentValues()
        cv.put(TODODBHelper.COL_DESCRIPTION, item.description)
        cv.put(TODODBHelper.COL_CREATED, item.timeCreated)
        cv.put(TODODBHelper.COL_DONE, item.isDone)
        db?.insert(TODODBHelper.TABLE_NAME, null, cv)
    }

    fun close() {
        db?.close()
    }
}