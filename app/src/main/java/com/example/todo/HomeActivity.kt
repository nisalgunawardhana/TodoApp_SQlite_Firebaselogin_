package com.example.todo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.adapter.TodoAdapter
import com.example.todo.database.TodoDatabaseHelper
import com.example.todo.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var db: TodoDatabaseHelper
    private lateinit var todoadapter: TodoAdapter
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        db = TodoDatabaseHelper(this)
        todoadapter = TodoAdapter(db.getAllTodos(), this) // Pass the context here

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.homeRecyclerView.adapter = todoadapter

        binding.addTodo.setOnClickListener{
            val intent = Intent(this, AddTodoActivity::class.java)
            startActivity(intent)
        }

        binding.profileImage.setOnClickListener{
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }

        binding.logOut.setOnClickListener {
            // Call the logout function
            logout()
        }


    }

    override fun onResume() {
        super.onResume()
        todoadapter.refreshData(db.getAllTodos())
    }

    private fun logout() {
        // Sign out the current user
        auth.signOut()

        // Navigate to the login activity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        // Finish the current activity to prevent going back to it when pressing back button from the login activity
        finish()
    }
}