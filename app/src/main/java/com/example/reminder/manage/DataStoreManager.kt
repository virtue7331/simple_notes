package com.example.reminder.manage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("reminders")
class DataStoreManager(val context: Context) {

    private val remindersKey = stringPreferencesKey("reminders_json")
    private val selectedThemeKey = stringPreferencesKey("selected_theme")

    val selectedThemeFlow: Flow<String> = context.dataStore.data.map { pref ->
        pref[selectedThemeKey] ?: "blue"
    }

    suspend fun saveSelectedTheme(theme: String) {
        context.dataStore.edit { pref -> pref[selectedThemeKey] = theme }
    }

    val remindersFlow: Flow<List<ReminderData>> = context.dataStore.data.map { pref ->
        fromJson(pref[remindersKey])
    }

    // Добавить одну заметку к уже сохранённым
    suspend fun addReminder(reminder: ReminderData) {
        context.dataStore.edit { pref ->
            val updated = fromJson(pref[remindersKey]) + reminder
            pref[remindersKey] = toJson(updated)
        }
    }

    suspend fun updateReminder(updated: ReminderData) {
        context.dataStore.edit { pref ->
            val list = fromJson(pref[remindersKey]).map { item ->
                if (item.id == updated.id) updated else item   // подменяем только нужную заметку
            }
            pref[remindersKey] = toJson(list)
        }
    }

    suspend fun deleteRemind(id: Long) {
        context.dataStore.edit { pref ->
            val updated = fromJson(pref[remindersKey]).filter { it.id != id }
            pref[remindersKey] = toJson(updated)
        }
    }

    // Список -> строка
    private fun toJson(list: List<ReminderData>): String {
        val array = JSONArray()
        list.forEach { item ->
            array.put(JSONObject()
                    .put("name", item.name)
                    .put("description", item.description)
                    .put("id", item.id)
            )
        }
        return array.toString()
    }

    // Строка -> список
    private fun fromJson(json: String?): List<ReminderData> {
        if (json.isNullOrEmpty()) return emptyList()
        val array = JSONArray(json)
        return List(array.length()) { i ->
            val obj = array.getJSONObject(i)
            ReminderData(
                obj.getString("name"),
                obj.getString("description"),
                obj.optLong("id", i.toLong()))
        }
    }
}