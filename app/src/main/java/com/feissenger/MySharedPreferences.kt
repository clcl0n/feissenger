package com.feissenger

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {
    val sharedPref: SharedPreferences =
        context.getSharedPreferences("com.feissenger", Context.MODE_PRIVATE)

    fun put(key: String, value: Any) {
        val editor = this.sharedPref.edit()

        when (value::class) {
            Boolean::class -> editor.putBoolean(key, value as Boolean)
            Float::class -> editor.putFloat(key, value as Float)
            Int::class -> editor.putInt(key, value as Int)
            Long::class -> editor.putLong(key, value as Long)
            String::class -> editor.putString(key, value as String)
            else -> {
                return
            }
        }

        editor.apply()
    }

    fun get(key: String, defaultValue: Any = ""): Any {
        return when (defaultValue::class) {
            Boolean::class -> this.sharedPref.getBoolean(key, defaultValue as Boolean)
            Float::class -> this.sharedPref.getFloat(key, defaultValue as Float)
            Int::class -> this.sharedPref.getInt(key, defaultValue as Int)
            Long::class -> this.sharedPref.getLong(key, defaultValue as Long)
            String::class -> this.sharedPref.getString(key, defaultValue as String) as Any
            else -> {
                defaultValue
            }
        }
    }

    fun clear() {
        this.sharedPref.edit().clear().apply()
    }

    fun logout() {
        put("access", "")
        put("refresh", "")
        put("uid", "")
        put("name", "")
    }
}