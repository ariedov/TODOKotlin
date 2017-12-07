package com.dleibovich.todokotlin.list

import android.support.annotation.IdRes
import android.support.transition.TransitionManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.dleibovich.todokotlin.R
import com.dleibovich.todokotlin.db.TodoItem

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
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item, parent, false)

        return ViewHolder(itemView,
                itemView.findViewById(R.id.title),
                itemView.findViewById(R.id.description),
                itemView.findViewById(R.id.actions),
                itemView.findViewById(R.id.done),
                itemView.findViewById(R.id.edit),
                itemView.findViewById(R.id.delete))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isExpanded = expandedPosition == position

        holder.itemView.isActivated = true
        holder.actions.visibility = if (isExpanded) VISIBLE else GONE

        val item = todoItems[position]
        holder.title.text = item.title
        if (!item.description.isNullOrEmpty()) {
            holder.description.text = item.description
            holder.description.visibility = VISIBLE
        } else {
            holder.description.visibility = GONE
        }

        val listener: ((View) -> Unit) = { listener.onItemActionClicked(item, it.id) }
        holder.done.setOnClickListener(listener)
        holder.edit.setOnClickListener(listener)
        holder.delete.setOnClickListener(listener)

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

class ViewHolder(itemView: View,
                 val title: TextView,
                 val description: TextView,
                 val actions: View,
                 val done: ImageButton,
                 val edit: ImageButton,
                 val delete: ImageButton) : RecyclerView.ViewHolder(itemView)