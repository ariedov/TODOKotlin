package com.dleibovich.todokotlin

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import com.dleibovich.todokotlin.add.AddItemActivity
import com.dleibovich.todokotlin.db.getToday
import com.dleibovich.todokotlin.db.getTomorrow
import com.dleibovich.todokotlin.list.ListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        add.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }

        setSupportActionBar(toolbar)

        pages.adapter = MainAdapter(resources, supportFragmentManager)
        tabs.setupWithViewPager(pages)
    }
}

class MainAdapter(private val resources: Resources,
                  fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment =
            when (position) {
                0 -> ListFragment.create(getToday())
                1 -> ListFragment.create(getTomorrow())
                else -> throw IllegalArgumentException("expecting only 2 items")
            }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): CharSequence? =
            when (position) {
                0 -> resources.getString(R.string.title_today)
                1 -> resources.getString(R.string.title_tomorrow)
                else -> throw IllegalArgumentException("expecting only 2 items")
            }

}
