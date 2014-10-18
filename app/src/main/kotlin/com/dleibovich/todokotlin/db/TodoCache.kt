package com.dleibovich.todokotlin.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.util.ArrayList
import android.database.Cursor
import android.content.ContentValues
import com.dleibovich.todokotlin.TodoItem

public class TodoCache(val context : Context) {

    private val dbHelper : TODODBHelper = TODODBHelper(context);
    private var db : SQLiteDatabase? = null;

    public fun open() {
        if (!(db?.isOpen() ?: false)) {
            db = dbHelper.getWritableDatabase();
        }
    }

    public fun getTodoItems() : List<TodoItem> {
        val cursor = db?.query(TODODBHelper.TABLE_NAME, null, null, null, null, null, null, null);
        val result = ArrayList<TodoItem>();
        if (cursor!!.moveToFirst()) {
            do {
                val description = cursor.getString(cursor.getColumnIndex(TODODBHelper.COL_DESCRIPTION)) as String;
                val dateCreated = cursor.getLong(cursor.getColumnIndex(TODODBHelper.COL_CREATED));
                val isDone = cursor.getInt(cursor.getColumnIndex(TODODBHelper.COL_DONE));
                val todoItem = TodoItem(description, dateCreated, isDone == 1);
                result.add(todoItem);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public fun putTodoItem(item : TodoItem) {
        val cv = ContentValues();
        cv.put(TODODBHelper.COL_DESCRIPTION, item.description);
        cv.put(TODODBHelper.COL_CREATED, item.timeCreated);
        cv.put(TODODBHelper.COL_DONE, item.isDone)
        db?.insert(TODODBHelper.TABLE_NAME, null, cv);
    }

    public fun changeTodoItemStatus(todoTimeCreated : Long, done : Boolean) {
        val cv = ContentValues();
        cv.put(TODODBHelper.COL_DONE, done);
        val createdValueName = TODODBHelper.COL_CREATED;
        db?.update(TODODBHelper.TABLE_NAME, cv, "$createdValueName=?", array(todoTimeCreated.toString()));
    }

    public fun close() {
        db?.close();
    }
}