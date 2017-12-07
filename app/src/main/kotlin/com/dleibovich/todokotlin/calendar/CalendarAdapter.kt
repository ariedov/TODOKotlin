package com.dleibovich.todokotlin.calendar

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.dleibovich.todokotlin.R
import com.dleibovich.todokotlin.list.ListActivity
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(private val activity: Activity) : RecyclerView.Adapter<ViewHolder>() {

    private var format = SimpleDateFormat.getDateInstance()
    private var dates: List<Date> = listOf()

    fun setDates(dates: List<Date>) {
        this.dates = dates
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dates.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.root.text = format.format(dates[position])
        holder.root.setOnClickListener {
            ListActivity.show(activity, dates[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.layout_date, parent, false) as TextView
        return ViewHolder(root)
    }
}

class ViewHolder(val root: TextView) : RecyclerView.ViewHolder(root)

