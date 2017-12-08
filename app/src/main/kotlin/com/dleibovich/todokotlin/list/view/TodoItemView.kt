package com.dleibovich.todokotlin.list.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.dleibovich.todokotlin.R
import kotlinx.android.synthetic.main.layout_item.view.*

class TodoItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.layout_item, this)
        layoutParams = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    fun setTitle(titleValue: String) { title.text = titleValue }

    fun setDescription(descriptionValue: String?) { description.text = descriptionValue }

    fun showActions() { actions.visibility = VISIBLE }
    fun hideActions() { actions.visibility = GONE }
    fun showDescription() { description.visibility = VISIBLE }
    fun hideDescription() { description.visibility = GONE }

    override fun setOnClickListener(l: OnClickListener?) {
        done.setOnClickListener(l)
        edit.setOnClickListener(l)
        delete.setOnClickListener(l)
    }
}