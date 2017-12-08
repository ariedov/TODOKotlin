package com.dleibovich.todokotlin.list

import android.support.annotation.IdRes
import android.support.transition.TransitionManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.dleibovich.todokotlin.db.TodoItem
import com.dleibovich.todokotlin.list.view.TodoItemView

class TodoAdapter(private val parent: RecyclerView, private val listener: ItemActionClickListener)
    : RecyclerView.Adapter<ViewHolder>() {

    private val todoItems = mutableListOf<TodoItem>()
    private var expandedPosition = NO_POSITION

    fun setData(todoItems: List<TodoItem>) {
        this.todoItems.clear()
        this.todoItems.addAll(todoItems)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TodoItemView(parent.context))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isExpanded = expandedPosition == position

        holder.view.isActivated = true
        if (isExpanded) {
            holder.view.showActions()
        } else {
            holder.view.hideActions()
        }

        val item = todoItems[position]
        holder.view.setTitle(item.title)
        if (!item.description.isNullOrEmpty()) {
            holder.view.setDescription(item.description)
            holder.view.showDescription()
        } else {
            holder.view.hideDescription()
        }

        val listener: ((View) -> Unit) = { listener.onItemActionClicked(item, it.id) }
        holder.view.setOnClickListener(listener)

        holder.itemView.setOnClickListener {
            expandedPosition = if (isExpanded) NO_POSITION else position
            TransitionManager.beginDelayedTransition(parent)
            notifyDataSetChanged()
        }
    }


    override fun getItemCount(): Int {
        return todoItems.size
    }

    companion object {
        val NO_POSITION = -1
    }

    interface ItemActionClickListener {

        fun onItemActionClicked(item: TodoItem, @IdRes id: Int)
    }
}

class ViewHolder(val view: TodoItemView) : RecyclerView.ViewHolder(view)