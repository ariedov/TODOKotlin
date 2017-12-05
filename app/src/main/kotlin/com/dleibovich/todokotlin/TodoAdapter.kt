package com.dleibovich.todokotlin

import android.widget.BaseAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.view.LayoutInflater

class TodoAdapter : BaseAdapter() {

    private var todoItems: List<TodoItem>? = null

    fun setData(todos: List<TodoItem>) {
        todoItems = todos
    }

    override fun getCount(): Int {
        return todoItems?.size ?: 0
    }

    override fun getItem(position: Int): TodoItem? {
        return todoItems?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.timeCreated ?: 0
    }

    override fun getView(position: Int, view: View?, container: ViewGroup): View? {
        val context = container.context
        val resultView: View?
        val holder: ViewHolder?
        if (view == null) {
            resultView = LayoutInflater.from(context).inflate(R.layout.row_todo, container, false);
            holder = ViewHolder(resultView?.findViewById(R.id.description) as TextView,
                    resultView.findViewById(R.id.timeCreated) as TextView)
            resultView.tag = holder
        } else {
            resultView = view
            holder = resultView.tag as? ViewHolder
        }

        val item = getItem(position)
        holder?.description?.text = item?.description
        holder?.timeCreated?.text = item?.timeCreated.toString()

        return resultView
    }

    private data class ViewHolder(val description: TextView, val timeCreated: TextView)
}