package com.dleibovich.todokotlin.add

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.EditText
import com.dleibovich.todokotlin.R
import com.dleibovich.todokotlin.TodoApp
import com.dleibovich.todokotlin.db.TodoItem
import kotlinx.android.synthetic.main.activity_add.*
import javax.inject.Inject

class AddItemActivity : AppCompatActivity(), AddItemView {

    @Inject
    lateinit var presenter: AddItemPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        (application as TodoApp)
                .dataComponent()
                .inject(this)

        setSupportActionBar(toolbar)

        submit.setOnClickListener {
            presenter.insertItem(TodoItem(taskTitle.string(), taskDescription.stringOrNull()))
        }

        presenter.setView(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
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
}

fun EditText.string() = text.toString()

fun EditText.stringOrNull(): String? {
    val string = string()
    return if (string.isEmpty()) {
        null
    } else {
        string
    }
}