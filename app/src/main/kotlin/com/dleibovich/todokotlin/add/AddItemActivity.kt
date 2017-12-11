package com.dleibovich.todokotlin.add

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.dleibovich.todokotlin.R
import com.dleibovich.todokotlin.TodoApp
import com.dleibovich.todokotlin.db.TodoItem
import com.dleibovich.todokotlin.string
import com.dleibovich.todokotlin.stringOrNull
import kotlinx.android.synthetic.main.activity_manage.*
import javax.inject.Inject

class AddItemActivity : AppCompatActivity(), AddItemView {

    @Inject
    lateinit var presenter: AddItemPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)

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