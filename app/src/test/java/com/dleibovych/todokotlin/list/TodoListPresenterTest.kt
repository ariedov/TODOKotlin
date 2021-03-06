package com.dleibovych.todokotlin.list

import any
import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.db.TodoItem
import com.dleibovich.todokotlin.db.getToday
import com.dleibovich.todokotlin.db.getTomorrow
import com.dleibovich.todokotlin.list.TodoListPresenter
import com.dleibovich.todokotlin.list.TodoListView
import com.dleibovych.todokotlin.overrideRx
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.`when` as whenever
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*

class TodoListPresenterTest {

    @Mock private lateinit var repo: ItemsRepository
    @Mock private lateinit var view: TodoListView

    private lateinit var date: Date
    private lateinit var presenter: TodoListPresenter

    @Before fun setup() {
        MockitoAnnotations.initMocks(this)

        overrideRx()

        date = getToday()
        whenever(repo.getItemChanges()).thenReturn(Observable.never())

        presenter = TodoListPresenter(repo, date)

        presenter.setView(view)
    }

    @Test
    fun testRequestEmptyData() {
        whenever(repo.getItems()).thenReturn(Observable.just(mutableListOf()))
        presenter.requestItems()

        verify(repo).getItems()
        verify(view).setEmpty()
        verify(view, never()).setData(any())
    }

    @Test
    fun testRequestSingleItem() {
        val list = listOf(TodoItem("title", "description", date))
        whenever(repo.getItems()).thenReturn(Observable.just(list))
        presenter.requestItems()

        verify(repo).getItems()
        verify(view).setData(any())
        verify(view, never()).setEmpty()
    }

    @Test
    fun testRequestItemForDifferentDate() {
        val list = listOf(TodoItem("title", "description", getTomorrow()))
        whenever(repo.getItems()).thenReturn(Observable.just(list))
        presenter.requestItems()

        verify(repo).getItems()
        verify(view).setEmpty()
        verify(view, never()).setData(any())
    }

    @Test
    fun testMarkAsDone() {
        whenever(repo.markAsDone(any())).thenReturn(Observable.never())

        val item = TodoItem("title", "description")
        presenter.markAsDone(item)

        verify(repo).markAsDone(item)
    }

    @Test
    fun testDelete() {
        whenever(repo.delete(any())).thenReturn(Observable.never())

        val item = TodoItem("title", "description")
        presenter.delete(item)

        verify(repo).delete(item)
    }
}

