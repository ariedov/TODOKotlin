package com.dleibovich.todokotlin.list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.dleibovich.todokotlin.R
import com.dleibovich.todokotlin.TodoApp
import com.dleibovich.todokotlin.db.TodoItem
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class ListActivity : AppCompatActivity(), TodoListView {

    @Inject
    lateinit var presenter: TodoListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as TodoApp)
                .listComponent()
                .inject(this)

        list.adapter = TodoAdapter()
    }

    override fun onStart() {
        super.onStart()
        presenter.requestItems()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun setData(items: List<TodoItem>) {
        (list.adapter as TodoAdapter).setData(items)
    }

    override fun setError() {
        TODO("show error")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId ?: 0
        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
