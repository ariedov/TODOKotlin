package com.dleibovich.todokotlin.db

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.os.Parcelable
import io.reactivex.Maybe
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "items")
@TypeConverters(DateConverter::class)
@Parcelize
data class TodoItem(
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "description") val description: String?,
        @ColumnInfo(name = "date") val date: Date = getTomorrow(),
        @ColumnInfo(name = "isDone") val isDone: Boolean = false,
        @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int = 0)
    : Parcelable

fun getTomorrow(): Date =
        Calendar.getInstance().apply {
            time = getToday()
            add(Calendar.DAY_OF_YEAR, 1)
        }.time

fun getToday(): Date =
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time


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