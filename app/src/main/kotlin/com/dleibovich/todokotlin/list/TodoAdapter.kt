package com.dleibovich.todokotlin.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dleibovich.todokotlin.R
import com.dleibovich.todokotlin.db.TodoItem

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private val todoItems = mutableListOf<TodoItem>()

    fun setData(todoItems: List<TodoItem>) {
        this.todoItems.clear()
        this.todoItems.addAll(todoItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item, parent, false)
        return ViewHolder(itemView, itemView.findViewById(R.id.description))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = todoItems[position]
        holder.description.text = item.description
    }

    override fun getItemCount(): Int {
        return todoItems.size
    }

    data class ViewHolder(private val itemView: View,
                          val description: TextView) : RecyclerView.ViewHolder(itemView)
}