package com.dleibovich.todokotlin

import android.support.v7.app.ActionBarActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.EditText
import com.dleibovich.todokotlin.db.TodoCache
import android.view.KeyEvent
import android.text.TextUtils
import android.view.inputmethod.EditorInfo

public class MainActivity() : ActionBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val todos = findViewById(R.id.todos) as ListView;
        val nextTodo =  findViewById(R.id.next_todo) as EditText;

        val cache = TodoCache(this)
        val adapter = TodoAdapter();

        nextTodo.setOnEditorActionListener { (textView, actionId, keyEvent) ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                cache.open();
                val todoItem = TodoItem(nextTodo.getText().toString(), isDone = false)
                nextTodo.setText("")
                cache.putTodoItem(todoItem)
                adapter.setData(cache.getTodoItems())
                cache.close();
                adapter.notifyDataSetChanged();
            }
            true;
        }
        cache.open();
        cache.close();
        todos.setAdapter(adapter);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item?.getItemId() ?: 0;
        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
