package com.dleibovich.todokotlin.list

import android.support.v7.util.DiffUtil
import com.dleibovich.todokotlin.db.TodoItem

class TodoDiffUtilsCallback(private val items: List<TodoItem>,
                            private val newItems: List<TodoItem>)
    : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
            = items[oldItemPosition].id == newItems[newItemPosition].id

    override fun getOldListSize(): Int = items.size

    override fun getNewListSize(): Int = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
            = items[oldItemPosition] == newItems[newItemPosition]

}