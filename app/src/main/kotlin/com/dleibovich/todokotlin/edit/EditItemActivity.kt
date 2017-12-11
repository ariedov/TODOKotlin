package com.dleibovich.todokotlin.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.dleibovich.todokotlin.R
import com.dleibovich.todokotlin.TodoApp
import com.dleibovich.todokotlin.db.TodoItem
import com.dleibovich.todokotlin.edit.EditItemPresenter
import com.dleibovich.todokotlin.edit.EditItemView
import com.dleibovich.todokotlin.string
import kotlinx.android.synthetic.main.activity_manage.*
import javax.inject.Inject

class EditItemActivity : AppCompatActivity(), EditItemView {

    @Inject
    lateinit var presenter: EditItemPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)

        (application as TodoApp)
                .dataComponent()
                .inject(this)

        setSupportActionBar(toolbar)

        val todoItem = intent.getParcelableExtra<TodoItem>(EXTRA_ITEM)
        submit.setOnClickListener {
            presenter.updateItem(todoItem.copy(
                    title = taskTitle.string(),
                    description = taskDescription.string()))
        }

        presenter.setView(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        taskTitle.setText(todoItem.title)
        taskDescription.setText(todoItem.description)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
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

    override fun setError() {
        TODO("not implemented")
    }

    companion object {
        val EXTRA_ITEM = "EXTRA_ITEM"

        fun start(activity: Activity, item: TodoItem) {
            activity.startActivity(Intent(activity, EditItemActivity::class.java).apply {
                putExtra(EXTRA_ITEM, item)
            })
        }
    }
}