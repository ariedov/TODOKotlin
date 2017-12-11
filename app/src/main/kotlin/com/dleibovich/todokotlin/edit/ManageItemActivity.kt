package com.dleibovich.todokotlin.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.dleibovich.todokotlin.R
import com.dleibovich.todokotlin.TodoApp
import com.dleibovich.todokotlin.db.TodoItem
import com.dleibovich.todokotlin.edit.ItemError
import com.dleibovich.todokotlin.edit.ManageItemPresenter
import com.dleibovich.todokotlin.edit.ManageItemView
import com.dleibovich.todokotlin.string
import kotlinx.android.synthetic.main.activity_manage.*
import javax.inject.Inject

class ManageItemActivity : AppCompatActivity(), ManageItemView {

    @Inject
    lateinit var presenter: ManageItemPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)

        (application as TodoApp)
                .dataComponent()
                .inject(this)

        setSupportActionBar(toolbar)

        presenter.setView(this)

        val todoItem = intent.getParcelableExtra<TodoItem?>(EXTRA_ITEM)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(
                if (todoItem != null) R.string.edit_item else R.string.create_item)

        if (todoItem != null) {
            taskTitle.setText(todoItem.title)
            taskDescription.setText(todoItem.description)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.manage, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.done -> {
                val todoItem = intent.getParcelableExtra<TodoItem?>(EXTRA_ITEM)
                if (todoItem != null) {
                    presenter.updateItem(todoItem.copy(
                            title = taskTitle.string(),
                            description = taskDescription.string()))
                } else {
                    presenter.addItem(TodoItem(taskTitle.string(), taskDescription.string()))
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.setView(null)
        if (isFinishing) {
            presenter.destroy()
        }
    }

    override fun setSuccess() {
        finish()
    }

    override fun setError(error: ItemError) {
        when (error) {
            ItemError.EmptyTitle -> taskLayout.error = getString(R.string.error_provide_title)
        }
    }

    companion object {
        val EXTRA_ITEM = "EXTRA_ITEM"

        fun start(activity: Activity, item: TodoItem? = null) {
            activity.startActivity(Intent(activity, ManageItemActivity::class.java).apply {
                if (item != null) {
                    putExtra(EXTRA_ITEM, item)
                }
            })
        }
    }
}