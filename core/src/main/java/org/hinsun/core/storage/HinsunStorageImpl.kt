package org.hinsun.core.storage

import android.app.Application
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class HinsunStorageImpl @Inject constructor(
    private val application: Application
) : HinsunStorage {
    private val sharedRef = application.getSharedPreferences(
        SHARED_PREFERENCES_NAME,
        Application.MODE_PRIVATE
    )

    private val gson: Gson by lazy { Gson() }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "HinsunStorage"
    }

    override fun <T> write(key: String, value: T): Boolean {
        val editor = sharedRef.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            is Set<*> -> {
                if (value.isNotEmpty() && value.all { it is String }) {
                    @Suppress("UNCHECKED_CAST")
                    editor.putStringSet(key, value as Set<String>)
                } else {
                    editor.putString(key, gson.toJson(value))
                }
            }

            else -> editor.putString(key, gson.toJson(value))
        }

        editor.apply()
        return true
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> read(key: String, defaultValue: T): T {
        val prefs = sharedRef

        return when (defaultValue) {
            is String -> prefs.getString(key, defaultValue) as T
            is Int -> prefs.getInt(key, defaultValue) as T
            is Boolean -> prefs.getBoolean(key, defaultValue) as T
            is Float -> prefs.getFloat(key, defaultValue) as T
            is Long -> prefs.getLong(key, defaultValue) as T
            is Set<*> -> {
                if (defaultValue.isEmpty() || defaultValue.all { it is String }) {
                    prefs.getStringSet(key, defaultValue as Set<String>) as T
                } else {
                    val json = prefs.getString(key, null)
                    json?.let {
                        gson.fromJson(it, object : TypeToken<T>() {}.type)
                    } ?: defaultValue
                }
            }

            else -> {
                val json = prefs.getString(key, null)
                json?.let {
                    gson.fromJson(it, object : TypeToken<T>() {}.type)
                } ?: defaultValue
            }
        }
    }

    override fun remove(key: String): Boolean {
        val editor = sharedRef.edit()
        editor.remove(key)
        editor.apply()
        return true
    }

    override fun clear(): Boolean {
        val editor = sharedRef.edit()
        editor.clear()
        editor.apply()
        return true
    }
}