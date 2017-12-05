package com.dleibovich.todokotlin.db

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import io.reactivex.Maybe
import io.reactivex.Single

@Entity(tableName = "items")
data class TodoItem(
        @ColumnInfo(name = "description") val description: String,
        @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int)

@Dao
interface TodoItemDao {
    @Query("select * from items")
    fun getAllItems(): Single<List<TodoItem>>

    @Query("select * from items where id = :id")
    fun findItem(id: Int): Maybe<TodoItem>

    @Insert(onConflict = REPLACE)
    fun insertItem(item: TodoItem): Long

    @Update(onConflict = REPLACE)
    fun updateItem(item: TodoItem): Int

    @Delete
    fun deleteItem(item: TodoItem): Int
}