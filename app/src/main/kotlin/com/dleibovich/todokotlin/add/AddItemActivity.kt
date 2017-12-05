package com.dleibovich.todokotlin.add

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dleibovich.todokotlin.R
import com.dleibovich.todokotlin.TodoApp
import com.dleibovich.todokotlin.db.TodoItem
import kotlinx.android.synthetic.main.activity_add.*
import javax.inject.Inject

class AddItemActivity: AppCompatActivity(), AddItemView {

    @Inject
    lateinit var presenter: AddItemPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        (application as TodoApp)
                .dataComponent()
                .inject(this)

        submit.setOnClickListener {
            presenter.insertItem(TodoItem(description.text.toString()))
        }

        presenter.setView(this)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}