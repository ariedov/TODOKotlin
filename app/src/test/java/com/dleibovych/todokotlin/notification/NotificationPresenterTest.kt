package com.dleibovych.todokotlin.notification

import any
import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.db.TodoItem
import com.dleibovich.todokotlin.db.getToday
import com.dleibovich.todokotlin.db.getTomorrow
import com.dleibovich.todokotlin.notification.Controller
import com.dleibovich.todokotlin.notification.NotificationPresenter
import com.dleibovych.todokotlin.overrideRx
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when` as whenever
import org.mockito.MockitoAnnotations

class NotificationPresenterTest {

    @Mock private lateinit var repo: ItemsRepository
    @Mock private lateinit var controller: Controller

    private lateinit var presenter: NotificationPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        overrideRx()

        presenter = NotificationPresenter(repo)
        presenter.setController(controller = controller)
    }

    @Test
    fun testListUpdate() {
        whenever(repo.getItemChanges()).thenReturn(Observable.empty())
        whenever(repo.getItems()).thenReturn(Observable.just(listOf(TodoItem("title", "description", getToday()))))

        presenter.updateList(getToday())

        verify(controller).showNotification(any())
    }

    @Test
    fun testListUpdateEmpty() {
        whenever(repo.getItemChanges()).thenReturn(Observable.empty())
        whenever(repo.getItems()).thenReturn(Observable.just(listOf()))

        presenter.updateList(getToday())

        verify(controller).showNoTasks()
    }

    @Test
    fun testListChanged() {
        val subject = PublishSubject.create<List<TodoItem>>()
        whenever(repo.getItemChanges()).thenReturn(subject)
        whenever(repo.getItems()).thenReturn(Observable.just(listOf()))

        presenter.updateList(getToday())

        verify(controller).showNoTasks()

        subject.onNext(listOf(TodoItem("item", "description", getToday())))

        verify(controller).showNotification(any())
    }

    @Test
    fun testListForTomorrow() {
        whenever(repo.getItemChanges()).thenReturn(Observable.empty())
        whenever(repo.getItems()).thenReturn(Observable.just(listOf(TodoItem("title", "description", getTomorrow()))))

        presenter.updateList(getToday())

        verify(controller).showNoTasks()
    }

    @Test
    fun testListChangedForTomorrow() {
        val subject = PublishSubject.create<List<TodoItem>>()
        whenever(repo.getItemChanges()).thenReturn(subject)
        whenever(repo.getItems()).thenReturn(Observable.just(listOf()))

        presenter.updateList(getToday())
        subject.onNext(listOf(TodoItem("item", "description", getTomorrow())))

        verify(controller, times(2)).showNoTasks()
    }

    @Test
    fun testListCleared() {
        val subject = PublishSubject.create<List<TodoItem>>()
        whenever(repo.getItemChanges()).thenReturn(subject)
        whenever(repo.getItems()).thenReturn(Observable.just(listOf(TodoItem("item", "description", getToday()))))

        presenter.updateList(getToday())

        verify(controller).showNotification(any())

        subject.onNext(listOf())

        verify(controller).showNoTasks()
    }

    @Test
    fun testTaskDone() {
        val subject = PublishSubject.create<List<TodoItem>>()
        whenever(repo.getItemChanges()).thenReturn(subject)
        whenever(repo.getItems()).thenReturn(Observable.just(listOf(TodoItem("item", "description", getToday()))))

        presenter.updateList(getToday())

        verify(controller).showNotification(any())

        subject.onNext(listOf(TodoItem("item", "description", getToday(), isDone = true)))

        verify(controller).showNoTasks()
    }
}