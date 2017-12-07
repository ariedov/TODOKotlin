package com.dleibovich.todokotlin.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dleibovich.todokotlin.R
import kotlinx.android.synthetic.main.activity_list.*
import java.util.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            val date = intent.getLongExtra(EXTRA_DATE, Date().time)
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, ListFragment.create(Date(date)))
                    .commit()
        }
    }

    companion object {
        private val EXTRA_DATE = "extra_date"

        fun show(activity: Activity, date: Date) {
            activity.startActivity(Intent(activity, ListActivity::class.java)
                    .apply { putExtra(EXTRA_DATE, date.time) })
        }
    }
}