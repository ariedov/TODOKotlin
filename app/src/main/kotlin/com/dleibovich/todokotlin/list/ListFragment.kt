package com.dleibovich.todokotlin.list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dleibovich.todokotlin.R
import com.dleibovich.todokotlin.TodoApp
import com.dleibovich.todokotlin.add.ManageItemActivity
import com.dleibovich.todokotlin.db.TodoItem
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.*
import javax.inject.Inject

class ListFragment : Fragment(), TodoListView {

    @Inject lateinit var presenter: TodoListPresenter
    lateinit var date: Date

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        date = Date(arguments!!.getLong(EXTRA_DISPLAY_DATE))
        (activity?.application as TodoApp)
                .getListComponent(date)
                .inject(this)

        list.layoutManager = LinearLayoutManager(context)
        list.adapter = TodoAdapter(list, ItemActionClickListener())

        presenter.setView(this)
        presenter.requestItems()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.setView(null)
        val activity = activity!!
        if (activity.isFinishing) {
            presenter.destroy()
            (activity.application as TodoApp).clearListComponent(date)
        }
    }

    private inner class ItemActionClickListener : TodoAdapter.ItemActionClickListener {

        override fun onItemActionClicked(item: TodoItem, id: Int) {
            when (id) {
                R.id.done -> { presenter.markAsDone(item) }
                R.id.edit -> { ManageItemActivity.start(activity!!, item) }
                R.id.delete -> { presenter.delete(item) }
            }
        }
    }


    override fun setData(items: List<TodoItem>) {
        empty.visibility = View.GONE
        list.visibility = View.VISIBLE
        (list.adapter as TodoAdapter).setData(items)
    }

    override fun setEmpty() {
        list.visibility = View.GONE
        empty.visibility = View.VISIBLE
    }

    override fun setError() {
        TODO("show error")
    }

    companion object {

        private val EXTRA_DISPLAY_DATE = "extra_display_date"

        fun create(date: Date): ListFragment =
                ListFragment().apply {
                    arguments = Bundle().apply {
                        putLong(EXTRA_DISPLAY_DATE, date.time)
                    }
                }
    }
}
