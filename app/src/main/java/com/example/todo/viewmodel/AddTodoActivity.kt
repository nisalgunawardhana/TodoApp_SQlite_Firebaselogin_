package com.example.todo.viewmodel

import com.example.todo.model.Todo
import com.example.todo.database.TodoDatabaseHelper
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.databinding.ActivityAddTodoBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTodoActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddTodoBinding
    private lateinit var db: TodoDatabaseHelper
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        calendar = Calendar.getInstance()
        db = TodoDatabaseHelper(this)

        binding.editTextDueDate.setOnClickListener { showDatePicker() }
        binding.editTextDueTime.setOnClickListener { showTimePicker() }

        binding.buttonAddTodo.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()
            val dueDate = binding.editTextDueDate.text.toString()
            val dueTime = binding.editTextDueTime.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty() && dueDate.isNotEmpty() && dueTime.isNotEmpty()) {
                val todo = Todo(null, title, description, dueDate, dueTime)
                db.addTodo(todo)
                finish()
                Toast.makeText(this@AddTodoActivity, "com.example.todo.model.Todo added successfully", Toast.LENGTH_SHORT).show()
            } else {
                // Show an error message or toast indicating that all fields are required
                // For example:
                Toast.makeText(this@AddTodoActivity, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                updateDateInView()
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)
                updateTimeInView()
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // Change as you need
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.editTextDueDate.setText(sdf.format(calendar.time))
    }

    private fun updateTimeInView() {
        val myFormat = "HH:mm" // Change as you need
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.editTextDueTime.setText(sdf.format(calendar.time))
    }
}