package com.dleibovich.todokotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import com.dleibovich.todokotlin.db.TodoCache
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cache = TodoCache(this)
        val adapter = TodoAdapter()

        nextTodo.setOnEditorActionListener { _, actionId, _ ->
            var result = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                cache.open()
                val todoItem = TodoItem(nextTodo.text.toString(), isDone = false)
                nextTodo.setText("")
                cache.putTodoItem(todoItem)
                adapter.setData(cache.getTodoItems())
                cache.close()
                adapter.notifyDataSetChanged()
                result = true
            }
            result
        }

        cache.open()
        cache.close()
        todos.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item?.itemId ?: 0
        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
