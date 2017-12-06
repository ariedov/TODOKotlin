package com.dleibovich.todokotlin.list

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.dleibovich.todokotlin.R
import com.dleibovich.todokotlin.TodoApp
import com.dleibovich.todokotlin.add.AddItemActivity
import com.dleibovich.todokotlin.db.TodoItem
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class ListActivity : AppCompatActivity(), TodoListView {

    @Inject lateinit var presenter: TodoListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as TodoApp)
                .dataComponent()
                .inject(this)

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = TodoAdapter(list, ItemActionClickListener())

        add.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }

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
        if (isFinishing) {
            presenter.destroy()
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

    private inner class ItemActionClickListener : TodoAdapter.ItemActionClickListener {

        override fun onItemActionClicked(item: TodoItem, id: Int) {
            when (id) {
                R.id.done -> {
                    presenter.markAsDone(item)
                }
                R.id.edit -> {}
                R.id.delete -> {}
            }
        }
    }
}
