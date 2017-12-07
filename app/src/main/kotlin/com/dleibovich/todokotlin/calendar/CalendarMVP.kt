package com.dleibovich.todokotlin.calendar

import com.dleibovich.todokotlin.db.ItemsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

interface CalendarView {

    fun setDates(dates: List<Date>)

    fun setEmpty()
}

class CalendarPresenter(private val repo: ItemsRepository) {

    private val stoppables = CompositeDisposable()
    private var view: CalendarView? = null

    fun setView(view: CalendarView?) {
        this.view = view
    }

    fun requestData() {
        stoppables.add(repo
                .getItemDates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { processDates(it) })
    }

    private fun processDates(dates: List<Date>) {
        if (dates.isNotEmpty()) {
            view?.setDates(dates)
        } else {
            view?.setEmpty()
        }
    }

    fun stop() {
        stoppables.dispose()
    }
}