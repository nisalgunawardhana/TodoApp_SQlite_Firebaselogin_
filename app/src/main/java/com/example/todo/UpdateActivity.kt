package com.example.todo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.database.TodoDatabaseHelper
import com.example.todo.databinding.ActivityUpdateBinding
import com.example.todo.model.Todo

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: TodoDatabaseHelper
    private var todoId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database helper
        db = TodoDatabaseHelper(this)

        // Get the todo ID passed from the previous activity
        todoId = intent.getLongExtra("todo_id", -1)

        // If todoId is invalid, finish the activity
        if (todoId == -1L) {
            finish()
            return
        }

        // Retrieve the todo item from the database
        val todo = db.getTodoById(todoId)

        // Populate the EditText fields with the todo item's data
        binding.editTextTitleEdit.setText(todo?.title)
        binding.editTextDescriptionEdit.setText(todo?.description)
        binding.editTextDueDateEdit.setText(todo?.dueDate)
        binding.editTextDueTimeEdit.setText(todo?.dueTime)

        // Set click listener for the update button
        binding.buttonEditTodo.setOnClickListener {
            // Retrieve the updated values from the EditText fields
            val newTitle = binding.editTextTitleEdit.text.toString()
            val newDescription = binding.editTextDescriptionEdit.text.toString()
            val newDate = binding.editTextDueDateEdit.text.toString()
            val newTime = binding.editTextDueTimeEdit.text.toString()

            // Create a new com.example.todo.model.Todo object with updated values
            val updatedTodo = Todo(todoId, newTitle, newDescription, newDate, newTime)

            // Update the todo item in the database
            db.updateTodo(updatedTodo)

            // Finish the activity and display a toast message
            finish()
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show()
        }
    }
}
