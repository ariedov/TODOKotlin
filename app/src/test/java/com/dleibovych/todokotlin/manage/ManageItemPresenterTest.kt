package com.dleibovych.todokotlin.manage

import any
import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.db.TodoItem
import com.dleibovich.todokotlin.manage.ManageItemPresenter
import com.dleibovich.todokotlin.manage.ManageItemView
import com.dleibovych.todokotlin.overrideRx
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.`when` as whenever
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class ManageItemPresenterTest {

    @Mock private lateinit var repo: ItemsRepository
    @Mock private lateinit var view: ManageItemView

    private lateinit var presenter: ManageItemPresenter

    @Before fun before() {
        MockitoAnnotations.initMocks(this)

        overrideRx()

        presenter = ManageItemPresenter(repo)
        presenter.setView(view)
    }

    @Test
    fun testUpdateItem() {
        whenever(repo.updateItem(any())).thenReturn(Observable.just(0))

        val item = TodoItem("title", "description")
        presenter.updateItem(item)

        verify(repo).updateItem(item)
        verify(view).setSuccess()
        verify(view, never()).setError(any())
    }

    @Test
    fun testUpdateEmptyTitle() {
        whenever(repo.updateItem(any())).thenReturn(Observable.just(0))

        val item = TodoItem("", "description")
        presenter.updateItem(item)

        verify(view).setError(any())

        verify(repo, never()).updateItem(item)
        verify(view, never()).setSuccess()
    }

    @Test
    fun testAdd() {
        whenever(repo.insertItem(any())).thenReturn(Observable.just(0))

        val item = TodoItem("title", "description")
        presenter.addItem(item)

        verify(repo).insertItem(item)
        verify(view).setSuccess()
        verify(view, never()).setError(any())
    }


    @Test
    fun testAddEmptyTitle() {
        whenever(repo.updateItem(any())).thenReturn(Observable.just(0))

        val item = TodoItem("", "description")
        presenter.addItem(item)

        verify(view).setError(any())

        verify(repo, never()).updateItem(item)
        verify(view, never()).setSuccess()
    }
}