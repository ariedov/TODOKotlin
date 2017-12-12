package com.dleibovich.todokotlin.list

import android.support.annotation.IdRes
import android.support.transition.TransitionManager
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.dleibovich.todokotlin.db.TodoItem
import com.dleibovich.todokotlin.db.forToday
import com.dleibovich.todokotlin.db.forTomorrow
import com.dleibovich.todokotlin.list.view.DoneItemLayout
import com.dleibovich.todokotlin.list.view.TodoItemLayout

class TodoAdapter(private val parent: RecyclerView, private val listener: ItemActionClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val todoItems = mutableListOf<TodoItem>()
    private var expandedPosition = NO_POSITION

    fun setData(todoItems: List<TodoItem>) {
        val diffResult = DiffUtil.calculateDiff(TodoDiffUtilsCallback(this.todoItems, todoItems))
        diffResult.dispatchUpdatesTo(this)
        this.todoItems.clear()
        this.todoItems.addAll(todoItems)
    }

    override fun getItemViewType(position: Int): Int {
        val item = todoItems[position]
        return if (!item.isDone
                && (item.forToday() || item.forTomorrow())) {
            TYPE_TODO
        } else {
            TYPE_DONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TODO -> TodoViewHolder(TodoItemLayout(parent.context), listener)
            TYPE_DONE -> DoneViewHolder(DoneItemLayout(parent.context))
            else -> throw IllegalArgumentException("no such view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val isExpanded = expandedPosition == position
        (holder as Bindable).bind(todoItems[position], isExpanded)

        holder.itemView.setOnClickListener {
            expandedPosition = if (isExpanded) TodoAdapter.NO_POSITION else position
            TransitionManager.beginDelayedTransition(parent)
            notifyDataSetChanged()
        }
    }


    override fun getItemCount(): Int {
        return todoItems.size
    }

    companion object {
        val TYPE_TODO = 1
        val TYPE_DONE = 2
        val NO_POSITION = -1
    }

    interface ItemActionClickListener {

        fun onItemActionClicked(item: TodoItem, @IdRes id: Int)
    }
}

class TodoViewHolder(val view: TodoItemLayout,
                     private val listener: TodoAdapter.ItemActionClickListener)
    : RecyclerView.ViewHolder(view), Bindable {

    override fun bind(item: TodoItem, isExpanded: Boolean) {
        view.isActivated = isExpanded
        if (isExpanded) {
            view.showActions()
        } else {
            view.hideActions()
        }

        view.setTitle(item.title)
        if (!item.description.isNullOrEmpty()) {
            view.setDescription(item.description)
            view.showDescription()
        } else {
            view.hideDescription()
        }

        val listener: ((View) -> Unit) = { listener.onItemActionClicked(item, it.id) }
        view.setActionsClickListener(View.OnClickListener { listener(it) })
    }
}

class DoneViewHolder(val view: DoneItemLayout) : RecyclerView.ViewHolder(view), Bindable {

    override fun bind(item: TodoItem, isExpanded: Boolean) {
        view.setTitle(item.title)
        if (!item.description.isNullOrEmpty()) {
            view.setDescription(item.description)
            view.showDescription()
        } else {
            view.hideDescription()
        }
        view.setDone(item.isDone)
    }

}

interface Bindable {

    fun bind(item: TodoItem, isExpanded: Boolean)
}
