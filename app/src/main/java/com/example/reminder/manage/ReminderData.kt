package com.example.reminder.manage

data class ReminderData (
    val name: String,
    val description: String,
    val id: Long = System.currentTimeMillis()
)
