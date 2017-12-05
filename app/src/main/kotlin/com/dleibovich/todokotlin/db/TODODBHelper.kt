package com.dleibovich.todokotlin.db

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase

class TODODBHelper(
        context: Context,
        dbName: String = "TODatabase",
        cursorFactory: SQLiteDatabase.CursorFactory? = null,
        dbVersion: Int = 1)
    : SQLiteOpenHelper(context, dbName, cursorFactory, dbVersion) {

    companion object {
        val TABLE_NAME = "totable"
        val COL_DESCRIPTION = "description"
        val COL_CREATED = "created_at"
        val COL_DONE = "done"

        private val CREATE_TABLE =
                """
        CREATE TABLE $TABLE_NAME
            (_id INTEGER PRIMARY KEY,
            $COL_DESCRIPTION TEXT,
            $COL_DONE INTEGER,
            $COL_CREATED INTEGER);
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE $TABLE_NAME")
        db.execSQL(CREATE_TABLE)
    }
}