package com.dleibovich.todokotlin.db

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.*

@Entity(tableName = "items")
@TypeConverters(DateConverter::class)
data class TodoItem(
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "description") val description: String?,
        @ColumnInfo(name = "date") val date: Date = Date(),
        @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int = 0)

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

class DateConverter {

    @TypeConverter
    fun toDate(timestamp: Long) = Date(timestamp)

    @TypeConverter
    fun toTimestamp(date: Date) = date.time
}