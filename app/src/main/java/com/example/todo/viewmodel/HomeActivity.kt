package com.example.todo.viewmodel

import com.example.todo.database.TodoDatabaseHelper
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.adapter.TodoAdapter
import com.example.todo.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var db: TodoDatabaseHelper
    private lateinit var todoadapter: TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TodoDatabaseHelper(this)
        todoadapter = TodoAdapter(db.getAllTodos(), this) // Pass the context here

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.homeRecyclerView.adapter = todoadapter

        binding.addTodo.setOnClickListener{
            val intent = Intent(this, AddTodoActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        todoadapter.refreshData(db.getAllTodos())
    }
}