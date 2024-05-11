package com.example.todo.model

data class Todo(
    var id: Long? = null,
    var title: String = "",
    var description: String = "",
    var dueDate: String = "",
    var dueTime: String = ""
)
