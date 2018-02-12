package com.dleibovych.todokotlin.calendar

import any
import com.dleibovich.todokotlin.calendar.CalendarPresenter
import com.dleibovich.todokotlin.calendar.CalendarView
import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovych.todokotlin.overrideRx
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when` as whenever
import org.mockito.MockitoAnnotations
import java.util.*

class CalendarPresenterTest {

    @Mock private lateinit var repo: ItemsRepository
    @Mock private lateinit var view: CalendarView

    private lateinit var presenter: CalendarPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        overrideRx()

        presenter = CalendarPresenter(repo)
        presenter.setView(view)
    }

    @Test
    fun testDataLoadingEmpty() {
        whenever(repo.getItemDates()).thenReturn(Observable.just(listOf()))

        presenter.requestData()

        verify(view).setEmpty()
    }

    @Test
    fun testDataLoadingSingle() {
        whenever(repo.getItemDates()).thenReturn(Observable.just(listOf(Date())))

        presenter.requestData()

        verify(view).setDates(any())
    }
}