package com.example.todo.database
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todo.model.Todo

class TodoDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "todo_manager"
        private const val TABLE_TODO = "todos"
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DUE_DATE = "due_date"
        private const val KEY_DUE_TIME = "due_time"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TODO_TABLE = "CREATE TABLE $TABLE_TODO(" + "$KEY_ID INTEGER PRIMARY KEY," + "$KEY_TITLE TEXT," + "$KEY_DESCRIPTION TEXT," + "$KEY_DUE_DATE TEXT," + "$KEY_DUE_TIME TEXT" + ")"
        db?.execSQL(CREATE_TODO_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase? , oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TODO")
        onCreate(db)
    }

    // Create operation
    fun addTodo(todo: Todo) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_TITLE, todo.title)
            put(KEY_DESCRIPTION, todo.description)
            put(KEY_DUE_DATE, todo.dueDate)
            put(KEY_DUE_TIME, todo.dueTime)
        }
        db.insert(TABLE_TODO, null, values)
        db.close()
    }

    // Read operation
    fun getTodoById(id: Long): Todo? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_TODO, arrayOf(KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DUE_DATE, KEY_DUE_TIME), "$KEY_ID=?", arrayOf(id.toString()), null, null, null, null)
        val todo: Todo?
        if (cursor != null && cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val titleIndex = cursor.getColumnIndex(KEY_TITLE)
            val descriptionIndex = cursor.getColumnIndex(KEY_DESCRIPTION)
            val dueDateIndex = cursor.getColumnIndex(KEY_DUE_DATE)
            val dueTimeIndex = cursor.getColumnIndex(KEY_DUE_TIME)

            if (idIndex != -1 && titleIndex != -1 && descriptionIndex != -1 && dueDateIndex != -1 && dueTimeIndex != -1) {
                todo = Todo(
                    cursor.getLong(idIndex),
                    cursor.getString(titleIndex),
                    cursor.getString(descriptionIndex),
                    cursor.getString(dueDateIndex),
                    cursor.getString(dueTimeIndex)
                )
            } else {
                todo = null
            }
            cursor.close()
        } else {
            todo = null
        }
        db.close()
        return todo
    }

    // Update operation
    fun updateTodo(todo: Todo): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TITLE, todo.title)
        values.put(KEY_DESCRIPTION, todo.description)
        values.put(KEY_DUE_DATE, todo.dueDate)
        values.put(KEY_DUE_TIME, todo.dueTime)
        return db.update(TABLE_TODO, values, "$KEY_ID=?", arrayOf(todo.id.toString()))
    }

    // Delete operation
    fun deleteTodoById(id: Long): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_TODO, "$KEY_ID=?", arrayOf(id.toString()))
    }

    // Get all todos
    fun getAllTodos(): List<Todo> {
        val todos = ArrayList<Todo>()
        val selectQuery = "SELECT  * FROM $TABLE_TODO"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(KEY_ID)
                val titleIndex = cursor.getColumnIndex(KEY_TITLE)
                val descriptionIndex = cursor.getColumnIndex(KEY_DESCRIPTION)
                val dueDateIndex = cursor.getColumnIndex(KEY_DUE_DATE)
                val dueTimeIndex = cursor.getColumnIndex(KEY_DUE_TIME)

                do {
                    if (idIndex != -1 && titleIndex != -1 && descriptionIndex != -1 && dueDateIndex != -1 && dueTimeIndex != -1) {
                        val todo = Todo(
                            cursor.getLong(idIndex),
                            cursor.getString(titleIndex),
                            cursor.getString(descriptionIndex),
                            cursor.getString(dueDateIndex),
                            cursor.getString(dueTimeIndex)
                        )
                        todos.add(todo)
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        db.close()
        return todos
    }

    // Get all todos by date
    fun getAllTodosByDate(date: String): List<Todo> {
        val todos = ArrayList<Todo>()
        val selectQuery = "SELECT * FROM $TABLE_TODO WHERE $KEY_DUE_DATE = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(date))
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(KEY_ID)
                val titleIndex = cursor.getColumnIndex(KEY_TITLE)
                val descriptionIndex = cursor.getColumnIndex(KEY_DESCRIPTION)
                val dueDateIndex = cursor.getColumnIndex(KEY_DUE_DATE)
                val dueTimeIndex = cursor.getColumnIndex(KEY_DUE_TIME)

                do {
                    if (idIndex != -1 && titleIndex != -1 && descriptionIndex != -1 && dueDateIndex != -1 && dueTimeIndex != -1) {
                        val todo = Todo(
                            cursor.getLong(idIndex),
                            cursor.getString(titleIndex),
                            cursor.getString(descriptionIndex),
                            cursor.getString(dueDateIndex),
                            cursor.getString(dueTimeIndex)
                        )
                        todos.add(todo)
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        db.close()
        return todos
    }






}
