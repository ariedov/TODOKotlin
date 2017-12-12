package com.dleibovich.todokotlin.calendar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.dleibovich.todokotlin.R
import com.dleibovich.todokotlin.TodoApp
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*
import javax.inject.Inject

class CalendarActivity : AppCompatActivity(), CalendarView {

    @Inject lateinit var presenter: CalendarPresenter
    private val adapter = CalendarAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        (application as TodoApp)
                .dataComponent()
                .inject(this)

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.calendar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        presenter.setView(this)
        presenter.requestData()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setDates(dates: List<Date>) {
        adapter.setDates(dates)

        list.visibility = View.VISIBLE
        empty.visibility = View.GONE
    }

    override fun setEmpty() {
        list.visibility = View.GONE
        empty.visibility = View.VISIBLE
    }
}