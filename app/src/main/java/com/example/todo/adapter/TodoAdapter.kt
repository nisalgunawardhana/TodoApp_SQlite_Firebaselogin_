package com.example.todo.adapter

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.AlarmReceiver
import com.example.todo.R
import com.example.todo.UpdateActivity
import com.example.todo.database.TodoDatabaseHelper
import com.example.todo.model.Todo
import java.text.SimpleDateFormat

class TodoAdapter(private var todos: List<Todo>, private val context: Context) : RecyclerView.Adapter<TodoAdapter.TodoHolder>() {

    private var db : TodoDatabaseHelper = TodoDatabaseHelper(context)
    inner class TodoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        var descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        var dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        var timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        var editIcon : ImageView = itemView.findViewById(R.id.editTodo)
        var deleteIcon : ImageView = itemView.findViewById(R.id.deleteTodo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TodoHolder(itemView)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val todo = todos[position]
        holder.titleTextView.text = todo.title
        holder.descriptionTextView.text = todo.description
        holder.dateTextView.text = todo.dueDate
        holder.timeTextView.text = todo.dueTime

        holder.editIcon.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateActivity::class.java).apply {
                putExtra("todo_id", todo.id)
            }
            holder.itemView.context.startActivity(intent)
        }


        holder.deleteIcon.setOnClickListener{
            val currentTodo = todos[position]
            currentTodo.id?.let { id ->
                db.deleteTodoById(id)
                refreshData(db.getAllTodos()) // Refresh the list after deletion
                notifyItemRemoved(position)
            }
        }
    }

    fun refreshData(newTodo:List<Todo>){
        todos = newTodo.toList()
        notifyDataSetChanged()
    }

    fun scheduleAlarm(todo: Todo) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val dueDateTime = todo.dueDate + " " + todo.dueTime // Combine date and time
        val dueMillis = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dueDateTime)?.time ?: return

        val currentTime = System.currentTimeMillis()
        val alarmTime = dueMillis - (15 * 60 * 1000) // 15 minutes before due time

        if (alarmTime > currentTime) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
        }
    }

}
