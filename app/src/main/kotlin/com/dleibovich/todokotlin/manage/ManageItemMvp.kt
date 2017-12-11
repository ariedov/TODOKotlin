package com.dleibovich.todokotlin.manage

import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.db.TodoItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface ManageItemView {

    fun setSuccess()

    fun setError(error: ItemError)
}

class ManageItemPresenter(private val repo: ItemsRepository) {

    private val disposables = CompositeDisposable()
    private var view: ManageItemView? = null

    fun setView(view: ManageItemView?) {
        this.view = view
    }

    fun updateItem(item: TodoItem) {
        performAfterValidate(item, ::performUpdate)
    }

    private fun performUpdate(item: TodoItem) {
        disposables.add(repo.updateItem(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view?.setSuccess() }))
    }

    fun addItem(item: TodoItem) {
        performAfterValidate(item, ::performInsert)
    }

    private fun performInsert(item: TodoItem) {
        disposables.add(repo.insertItem(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view?.setSuccess() }))
    }

    private fun performAfterValidate(item: TodoItem, action: (item: TodoItem) -> Unit) {
        if (item.title.isEmpty()) {
            view?.setError(ItemError.EmptyTitle)
        } else {
            action(item)
        }
    }

    fun destroy() {
        disposables.dispose()
    }
}

enum class ItemError {
    EmptyTitle
}