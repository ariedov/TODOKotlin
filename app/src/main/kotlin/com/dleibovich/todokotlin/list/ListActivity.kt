package com.dleibovich.todokotlin.list

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.dleibovich.todokotlin.R
import com.dleibovich.todokotlin.TodoApp
import com.dleibovich.todokotlin.add.AddItemActivity
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
                .dataComponent()
                .inject(this)

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = TodoAdapter()

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
