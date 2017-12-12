package com.dleibovich.todokotlin.list.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.dleibovich.todokotlin.R
import kotlinx.android.synthetic.main.layout_done_item.view.*

class DoneItemLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.layout_done_item, this)
        val padding = resources.getDimensionPixelSize(R.dimen.padding_x2)
        setPadding(padding, padding, padding, padding)

        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    fun setTitle(titleValue: String) { title.text = titleValue }
    fun setDescription(descriptionValue: String?) { description.text = descriptionValue }
    fun showDescription() { description.visibility = VISIBLE }
    fun hideDescription() { description.visibility = GONE }

    fun setDone(done: Boolean) {
        icon.setImageResource(if (done) R.drawable.ic_item_done else R.drawable.ic_failed)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        // ignore clicks
    }
}